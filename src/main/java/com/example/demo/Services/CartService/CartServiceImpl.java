package com.example.demo.Services.CartService;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.example.demo.InputDto.Ecommerce.Cart.CartDto;
import com.example.demo.InputDto.Ecommerce.Cart.CartItemDto;
import com.example.demo.InputDto.Ecommerce.Cart.ViewCartDto;
import com.example.demo.Models.Ecommerce.Cart.CartItem;
import com.example.demo.Repositories.SocialMedia.Content.BusinessPostRepository;
import com.example.demo.Repositories.Business.Order.CartItemRepository;
import com.example.demo.Repositories.Business.Order.CartRepository;
import com.example.demo.Repositories.UserManagement.AccountManagement.ProfileRepository;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class CartServiceImpl implements CartService{
    private static final Logger logger = LoggerFactory.getLogger(CartServiceImpl.class);

    private final CartRepository cartRepository;

    private final CartItemRepository cartItemRepository;

    private final ProfileRepository profileRepository;

    private final BusinessPostRepository businessPostRepository;

    @SuppressWarnings("null")
    @Override
    @Transactional
    public ResponseEntity<Object> addToCart(CartDto cartDto) {
        try {
            Long profile = profileRepository.findProfileIdById(cartDto.getProfileId());
            if(profile != null){
                Optional<Long> cart = cartRepository.findCartIdByCustomerId(profile);

                if (cart.isPresent()) {
                    Long cart2 = cart.get();

                    for (CartItemDto item : cartDto.getItems()) {
                        Optional<Long> userPostId = businessPostRepository.getBusinessPostIdByItsId(item.getPostId());
                        if(userPostId.isPresent()){
                            Optional<CartItem> cartItemOptional = cartItemRepository.findCartItemByProductIdAndCartId(userPostId.get(), cart2);
                            if(cartItemOptional.isPresent()){
                                CartItem cartItem = cartItemOptional.get();
                                if(cartItem.isDeleted()){
                                    cartItemRepository.updateDeletedCartItem(item.getQuantity(),cart2);
                                }else{
                                    cartItemRepository.updateExistingCartItem(item.getQuantity(),cart2);
                                }

                                logger.info("Cart item updated Successful : "+ cartItem);
                            }else{
                                cartItemRepository.saveCartItem(userPostId.get(),item.getQuantity(), cart2);

                                logger.info("Cart item added Successful");
                            }
                        }else{
                            logger.error("Failed to add product in the cart, Product not found with id: "+ item.getPostId());
                            return ResponseEntity.status(404).body("Post not found");
                        }
                    }
                    return ResponseEntity.status(200).body("Cart Updated Successful");
                } else {
                    cartRepository.saveCart(profile);
                    Optional<Long> cart2 = cartRepository.findCartIdByCustomerId(profile);

                    for (CartItemDto item : cartDto.getItems()) {
                        Optional<Long> userPostId = businessPostRepository.getBusinessPostIdByItsId(item.getPostId());
                        if(userPostId.isPresent()){
                                cartItemRepository.saveCartItem(userPostId.get(),item.getQuantity(), cart2.get());

                                logger.info("Cart item added Successful : ");
                        }else{
                            logger.error("Failed to add product in the cart, Product not found with id: "+ item.getPostId());
                            return ResponseEntity.status(404).body("Product Not found");
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
            Long profile = profileRepository.findProfileIdById(cartDto.getProfileId());
            if(profile != null){
                Optional<Long> cart = cartRepository.findCartIdByCustomerId(profile);

                if (cart.isPresent()) {
                    Long cart2 = cart.get();

                    for (CartItemDto item : cartDto.getItems()) {
                        Optional<Long> userPostId = businessPostRepository.getBusinessPostIdByItsId(item.getPostId());
                        if(userPostId.isPresent()){
                            Optional<CartItem> cartItemOptional = cartItemRepository.findCartItemByProductIdAndCartId(userPostId.get(), cart2);
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

    @Override
    public ResponseEntity<Object> viewCart(ViewCartDto ViewCartDto){
        try {
            // Iterable<City> cities = cityRepository.findAll();
            // if(!cities.iterator().hasNext()){
            //     logger.error("\nThere is Request for Fetching All Cities, But Nothing Registered to the database ");
            //     return ResponseEntity.status(HttpStatus.NOT_FOUND).body("There is No  Cities in the Database");
            // }else{
            //     logger.info("\nSuccessful fetched all Cities");
                return ResponseEntity.status(200).body("cityRepository.findCitiesWithoutCountry()");
            // }
        } catch (Exception exception) {
            logger.error("\nFailed to fetch all Cities, Server Error: \n" + exception.getMessage());
            return ResponseEntity.status(500).body("Internal Server Error");
        }
    }
}
