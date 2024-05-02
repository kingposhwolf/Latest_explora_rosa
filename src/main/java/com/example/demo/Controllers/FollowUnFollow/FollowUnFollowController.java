package com.example.demo.Controllers.FollowUnFollow;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.Components.GlobalValidationFormatter.GlobalValidationFormatter;
import com.example.demo.InputDto.SocialMedia.FollowUnFollow.FetchFollowInfoDto;
import com.example.demo.InputDto.SocialMedia.FollowUnFollow.FollowUnFollowDto;
import com.example.demo.Services.FollowUnFollowService.FollowUnFollowServiceImpl;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/engage")
@AllArgsConstructor
public class FollowUnFollowController {
    private final FollowUnFollowServiceImpl followUnFollowService;

    private GlobalValidationFormatter globalValidationFormatter;
    
    @PostMapping("/follow-operation")
    public ResponseEntity<Object> followOperation(@RequestBody @Valid FollowUnFollowDto followUnFollowDto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return globalValidationFormatter.validationFormatter(bindingResult);
        }
        return followUnFollowService.followOperation(followUnFollowDto);
    }

    @PostMapping("/followers")
    public ResponseEntity<Object> fetchFollowers(@RequestBody @Valid FetchFollowInfoDto fetchFollowInfoDto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return globalValidationFormatter.validationFormatter(bindingResult);
        }
        return followUnFollowService.fetchFollowers(fetchFollowInfoDto.getProfileId());
    }

    @PostMapping("/followings")
    public ResponseEntity<Object> fetchFollowings(@RequestBody @Valid FetchFollowInfoDto fetchFollowInfoDto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return globalValidationFormatter.validationFormatter(bindingResult);
        }
        return followUnFollowService.fetchFollowing(fetchFollowInfoDto.getProfileId());
    }
}
