package com.wisely.e_commercecard.controllers;


import com.wisely.e_commercecard.dto.ImageDto;
import com.wisely.e_commercecard.exception.ResourceNotFoundException;
import com.wisely.e_commercecard.model.Image;
import com.wisely.e_commercecard.response.ApiResponse;
import com.wisely.e_commercecard.service.image.IImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.sql.SQLException;
import java.util.List;

import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@RestController
@RequestMapping("${api.prefix}/images")
@RequiredArgsConstructor
public class ImageController {
    private final IImageService imageService;
    @PostMapping("/upload")
    public ResponseEntity<ApiResponse> saveImage(@RequestParam List<MultipartFile> file , @RequestParam Long productId) {
        try {
            List<ImageDto> images = imageService.saveImageById(file, productId);
            return ResponseEntity.ok(new ApiResponse("success", images));
        } catch (Exception e) {
            return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(new ApiResponse("error", e.getMessage()));
        }
    }

    @GetMapping("/image/download/{imageId}")
    public ResponseEntity<Resource> downloadImage(@PathVariable Long imageId) throws SQLException {
        Image image = imageService.getImageById(imageId);
        ByteArrayResource mamdouh = new ByteArrayResource(image.getImage().getBytes(1, (int) image.getImage().length()));
        return ResponseEntity.ok().contentType(MediaType.parseMediaType(image.getFileType()))
                .header(HttpHeaders.CONTENT_DISPOSITION , "attachment; filename =\""+image.getFileName())
                .body(mamdouh);
    }
    @PutMapping("/image/{imageId}/update")
    public ResponseEntity<ApiResponse> updateImage(@PathVariable Long imageId, @RequestBody MultipartFile image) {
       try{
        Image newimage =imageService.getImageById(imageId);
        if(newimage != null) {
            imageService.updateImage( image ,imageId);
            return ResponseEntity.ok(new ApiResponse("success updated", null));
        }}catch (ResourceNotFoundException e)
       {return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
    }return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(new ApiResponse("error updated", INTERNAL_SERVER_ERROR));
    }

    @DeleteMapping("/image/{imageId}/delete")
    public ResponseEntity<ApiResponse> deleteImageById(@PathVariable Long imageId){
        try {
            Image image = imageService.getImageById(imageId);
            if(image != null){
                imageService.deleteImageById(imageId);
                ResponseEntity.ok(new ApiResponse("image deleted succesfully" ,null));
            }
        } catch (ResourceNotFoundException e) {
            ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(),null));
        }return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(new ApiResponse("error",INTERNAL_SERVER_ERROR));

    }
}
