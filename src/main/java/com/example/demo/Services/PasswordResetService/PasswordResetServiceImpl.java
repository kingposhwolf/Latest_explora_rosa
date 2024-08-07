package com.example.demo.Services.PasswordResetService;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.InputDto.UserManagement.Authentication.NewPasswordDto;
import com.example.demo.Models.UserManagement.User;
import com.example.demo.Models.UserManagement.Management.PasswordResetToken;
import com.example.demo.Repositories.UserManagement.AccountManagement.PasswordResetRepository;
import com.example.demo.Repositories.UserManagement.AccountManagement.UserRepository;

import lombok.AllArgsConstructor;


@Service
@AllArgsConstructor
public class PasswordResetServiceImpl implements PasswordResetService{

    private static final Logger logger = LoggerFactory.getLogger(PasswordResetServiceImpl.class);
    

    private PasswordEncoder passwordEncoder;

    private PasswordResetRepository tokenRepository;

    private UserRepository userRepository;

    private JavaMailSender mailSender;

    @Override
    @Transactional
    public ResponseEntity<Object> sendPasswordResetToken(String email) {
        try {

        User user = userRepository.findByEmail(email).orElse(null);

        String token = UUID.randomUUID().toString();

        PasswordResetToken resetToken = new PasswordResetToken();
        resetToken.setToken(token);
        resetToken.setUser(user);
        resetToken.setExpiryDate(LocalDateTime.now().plusHours(4));
        tokenRepository.save(resetToken);

        String resetLink = "http://127.0.0.1:8081/reset-password?token=" + token;
        String emailBody = "Click on the link to reset your password: " + resetLink;

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(user.getEmail());
        message.setSubject("Password Reset");
        message.setText(emailBody);

        mailSender.send(message);

        logger.info("\nPassword Reset Email Successful send which conatains :\n" + message);

        return ResponseEntity.ok("Password reset link sent to the email : " + email);
        } catch (Exception exception) {
            logger.error("\nPassword Reset Email Send Fail Server side Error :\n" + exception.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(exception.getMessage());
        }
    }

    @Transactional
    @Override
    public ResponseEntity<Object> resetPassword(NewPasswordDto newPasswordDto) {
        try {

        Optional<PasswordResetToken> optionalToken = tokenRepository.findByToken(newPasswordDto.getToken());

        if (optionalToken.isPresent()) {
            PasswordResetToken resetToken = optionalToken.get();

            if (resetToken.getExpiryDate().isAfter(LocalDateTime.now())) {
                User user = resetToken.getUser();
                user.setPassword(passwordEncoder.encode(newPasswordDto.getPassword()));
                userRepository.save(user);
                tokenRepository.delete(resetToken);

                logger.info("\nPassword Reset Sucessful For User :\n" + user);
                return ResponseEntity.ok("Password reset successfully");
            } else {
                logger.error("\nPassword Reset Fail, Token Expired for user :\n"+optionalToken.get().getUser());
                return ResponseEntity.status(403).body("Not Allowed");
            }
        } else {
            logger.error("\nPassword Reset Fail, Token does not exist for user:\n" + optionalToken.get().getUser());
            return ResponseEntity.status(403).body("Not Allowed");
        }
    }catch(Exception exception){
        logger.error("\nPassword Reset operation failed , server side error :\n"+exception.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Something Wrong At Our End");
    }
    }

}