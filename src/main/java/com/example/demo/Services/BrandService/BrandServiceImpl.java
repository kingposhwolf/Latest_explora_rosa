package com.example.demo.Services.BrandService;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.example.demo.Dto.GetBrandDto;
import com.example.demo.Models.Brand;
import com.example.demo.Repositories.BrandRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BrandServiceImpl implements BrandService{

    private static final Logger logger = LoggerFactory.getLogger(BrandServiceImpl.class);

    private final BrandRepository brandRepository;

    @SuppressWarnings("null")
    @Override
    public ResponseEntity<Object> getBrandById(GetBrandDto getBrandDto) {

        try {
            Optional<Brand> brand = brandRepository.findById(getBrandDto.getId());

            if (!brand.isPresent()) {
                logger.error("Failed to Fetch Brand Info, Invalid brand Id");
                return ResponseEntity.badRequest().body("Invalid profile ID");
            }else{
                logger.info("\nProfile Info Fetched Successful: " + brand);
                return ResponseEntity.status(200).body(brand);
            }
        } catch (Exception exception) {
            logger.error("\nBrand fetching failed , Server Error : " + exception.getMessage());
            return ResponseEntity.status(500).body("Internal Server Error");
        }
    }
    
}
