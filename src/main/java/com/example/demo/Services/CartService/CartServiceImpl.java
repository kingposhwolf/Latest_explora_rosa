package com.example.demo.Services.CartService;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.example.demo.InputDto.Ecommerce.Cart.CartDto;
import com.example.demo.InputDto.Ecommerce.Cart.CartItemDto;
import com.example.demo.Models.Ecommerce.Cart.Cart;
import com.example.demo.Models.Ecommerce.Cart.CartItem;
import com.example.demo.Models.SocialMedia.UserPost;
import com.example.demo.Models.UserManagement.Profile;
import com.example.demo.Repositories.CartItemRepository;
import com.example.demo.Repositories.CartRepository;
import com.example.demo.Repositories.ProfileRepository;
import com.example.demo.Repositories.UserPostRepository;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class CartServiceImpl implements CartService{
    private static final Logger logger = LoggerFactory.getLogger(CartServiceImpl.class);

    private final CartRepository cartRepository;

    private final CartItemRepository cartItemRepository;

    private final ProfileRepository profileRepository;

    private final UserPostRepository userPostRepository;

    @SuppressWarnings("null")
    @Override
    @Transactional
    public ResponseEntity<Object> addToCart(CartDto cartDto) {
        try {
            Optional<Profile> profile = profileRepository.findById(cartDto.getProfileId());
            if(profile.isPresent()){
                Optional<Cart> cart = cartRepository.findByCustomer(profile.get());

                if (cart.isPresent()) {
                    Cart cart2 = cart.get();

                    for (CartItemDto item : cartDto.getItems()) {
                        Optional<UserPost> userPost = userPostRepository.findById(item.getPostId());
                        if(userPost.isPresent()){
                            Optional<CartItem> cartItemOptional = cartItemRepository.findByCartAndProduct(cart2, userPost.get());
                            if(cartItemOptional.isPresent()){
                                CartItem cartItem = cartItemOptional.get();
                                cartItem.setQuantity(item.getQuantity());
                                cartItemRepository.save(cartItem);

                                logger.info("Cart item updated Successful : "+ cartItem);
                            }else{
                                CartItem cartItem = new CartItem();
                                cartItem.setCart(cart2);
                                cartItem.setQuantity(item.getQuantity());
                                cartItem.setProduct(userPost.get());
                                cartItemRepository.save(cartItem);

                                logger.info("Cart item added Successful : "+ cartItem);
                            }
                        }else{
                            logger.error("Failed to add product in the cart, Product not found with id: "+ item.getPostId());
                            return ResponseEntity.status(404).body("Profile not found");
                        }
                    }
                return ResponseEntity.status(200).body("Cart Updated Successful");
                } else {
                    Cart newCart = new Cart();
                    newCart.setCustomer(profile.get());
                    Cart savedCart = cartRepository.save(newCart);

                    for (CartItemDto item : cartDto.getItems()) {
                        Optional<UserPost> userPost = userPostRepository.findById(item.getPostId());
                        if(userPost.isPresent()){
                                CartItem cartItem = new CartItem();
                                cartItem.setCart(savedCart);
                                cartItem.setQuantity(item.getQuantity());
                                cartItem.setProduct(userPost.get());
                                cartItemRepository.save(cartItem);

                                logger.info("Cart item added Successful : "+ cartItem);
                        }else{
                            logger.error("Failed to add product in the cart, Product not found with id: "+ item.getPostId());
                            return ResponseEntity.status(404).body("Profile not found");
                        }
                    }
                    return ResponseEntity.status(200).body("Cart Updated Successful");
                }
            }else{
                logger.error("Profile Not Found Error During Cart Operation");
                return ResponseEntity.status(404).body("Profile not found");
            }
        } catch (Exception exception) {
            logger.error("\nFailed to add item to cart, Server Error: \n" + exception.getMessage());
            return ResponseEntity.status(500).body("Internal Server Error");
        }
    }

    @SuppressWarnings("null")
    @Override
    public ResponseEntity<Object> removeToCart(CartDto cartDto) {
        try {
            Optional<Profile> profile = profileRepository.findById(cartDto.getProfileId());
            if(profile.isPresent()){
                Optional<Cart> cart = cartRepository.findByCustomer(profile.get());

                if (cart.isPresent()) {
                    Cart cart2 = cart.get();

                    for (CartItemDto item : cartDto.getItems()) {
                        Optional<UserPost> userPost = userPostRepository.findById(item.getPostId());
                        if(userPost.isPresent()){
                            Optional<CartItem> cartItemOptional = cartItemRepository.findByCartAndProduct(cart2, userPost.get());
                            if(cartItemOptional.isPresent()){
                                cartItemRepository.delete(cartItemOptional.get());
                                logger.info("Cart item removed sucessful from the cart");
                                return ResponseEntity.status(200).body("Item Removed successful from the Cart");
                            }else{
                                logger.error("Cart Item Not Found");
                                return ResponseEntity.status(404).body("Cart Item Not Found");
                            }
                        }else{
                            logger.error("Failed to update cart, Product not found with id: "+ item.getPostId());
                            return ResponseEntity.status(404).body("Product Not Found");
                        }
                    }
                return ResponseEntity.status(200).body("Cart Updated Successful");
                } else {
                    logger.error("Cart Not Found Error During Cart Operation,for profile with ID : " + cartDto.getProfileId());
                    return ResponseEntity.status(400).body("Cart NOT Found");
                }
            }else{
                logger.error("Profile Not Found Error During Cart Operation");
                return ResponseEntity.status(404).body("Profile Not Found");
            }
        } catch (Exception exception) {
            logger.error("\nFailed to remove item to cart, Server Error: \n" + exception.getMessage());
            return ResponseEntity.status(500).body("Internal Server Error");
        }
    }
}
