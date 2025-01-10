package com.wisely.e_commercecard.service.image;

import com.wisely.e_commercecard.dto.ImageDto;
import com.wisely.e_commercecard.model.Image;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface IImageService {
     Image getImageById(Long id);
      List<ImageDto> saveImageById(List<MultipartFile> files, Long id);
      void updateImage(MultipartFile file , Long id);
      void deleteImageById(Long id);

}
