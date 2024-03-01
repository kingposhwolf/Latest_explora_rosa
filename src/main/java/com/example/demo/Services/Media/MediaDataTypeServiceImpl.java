//package com.example.demo.Services.Media;
//
//import com.example.demo.Dto.MediaDataTypeDto;
//import com.example.demo.Models.MediaDataType;
//import com.example.demo.Repositories.MediaDataTypeRepository;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.stereotype.Service;
//
//import java.util.Optional;
//
//@Service
//public class MediaDataTypeServiceImpl implements MediaDataTypeService {
//
//    private static final Logger logger = LoggerFactory.getLogger(MediaDataTypeServiceImpl.class);
//
//    private final MediaDataTypeRepository mediaDataTypeRepository;
//
//    public MediaDataTypeServiceImpl(MediaDataTypeRepository mediaDataTypeRepository){
//        this.mediaDataTypeRepository = mediaDataTypeRepository;
//    }
//
//    @Override
//    public ResponseEntity<Object> getAllMediaTypes() {
//        try {
//            Iterable<MediaDataType> mediaTypes = mediaDataTypeRepository.findAll();
//
//            if(!mediaTypes.iterator().hasNext()){
//                logger.error("\nThere is Request for Fetching All Countries, But Nothing Registered to the database ");
//                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("There is No  Countries registered in the Database");
//            }else{
//                logger.info("\nSuccessful fetched all Coiuntries");
//                return ResponseEntity.status(200).body(mediaTypes);
//            }
//        } catch (Exception exception) {
//            logger.error("\nFailed to fetch Countries, Server Error: \n" + exception.getMessage());
//            return ResponseEntity.status(500).body("Internal Server Error");
//        }
//    }
//
//    @Override
//    public ResponseEntity<Object> saveMediaType(MediaDataTypeDto mediaDataTypeDto) {
//
//        try {
//            Optional<MediaDataType> existingMediaType = mediaDataTypeRepository.findByName(mediaDataTypeDto.getName());
//
//            if(existingMediaType.isPresent()){
//                logger.error("\nFailed to save Media Type, Media Data Type Already Exists Error");
//                return ResponseEntity.status(400).body("Media Data Type Already Exists!");
//            }
//            else{
//                MediaDataType mediaDataType = new MediaDataType();
//                mediaDataType.setName(mediaDataTypeDto.getName());
//                mediaDataTypeRepository.save(mediaDataType);
//
//                logger.info("Country saved Sucessfully\n" + mediaDataType);
//                return ResponseEntity.status(201).body("Media Data Type Created Successful ");
//            }
//        } catch (Exception exception) {
//            logger.error("\nFailed to save Media Data Type, Server Error: \n" + exception.getMessage());
//            return ResponseEntity.status(500).body("Internal Server Error");
//        }
//    }
//
//}
