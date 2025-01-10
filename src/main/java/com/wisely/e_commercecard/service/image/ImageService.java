package com.wisely.e_commercecard.service.image;

import com.wisely.e_commercecard.dto.ImageDto;
import com.wisely.e_commercecard.model.Image;
import com.wisely.e_commercecard.model.Product;
import com.wisely.e_commercecard.repository.ImageRepository;
import com.wisely.e_commercecard.service.product.IProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.sql.rowset.serial.SerialBlob;
import java.io.IOException;
import java.lang.module.ResolutionException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ImageService implements IImageService {
    private final ImageRepository imageRepository;
    private final IProductService productService;
    @Override
    public Image getImageById(Long id) {
        return imageRepository.findById(id).orElseThrow(()->new ResolutionException("not found"));
    }

    @Override
    public List<ImageDto> saveImageById(List<MultipartFile> files, Long productId) {
        Product product = productService.getProductById(productId);
        List<ImageDto> imageDtos = new ArrayList<>();
        for (MultipartFile file : files) {
            try{
            Image image = new Image();
            image.setFileName(file.getOriginalFilename());
            image.setFileType(file.getContentType());
            image.setImage(new SerialBlob(file.getBytes()));
            image.setProduct(product);


            String constDownload ="/api/v1/images/image/download/";
            image.setDownloadUrl(constDownload+image.getId());
            Image savedImage =imageRepository.save(image);

            image.setDownloadUrl(constDownload+savedImage.getId());
            imageRepository.save(savedImage);

            ImageDto imageDto = new ImageDto();
                imageDto.setId(savedImage.getId());
                imageDto.setImageName(savedImage.getFileName());
                imageDto.setDownloadUrl(savedImage.getDownloadUrl());
                imageDtos.add(imageDto);

        }catch (IOException |SQLException e){

                throw new RuntimeException(e.getMessage());
            }
    }return imageDtos;
    }

    @Override
    public void updateImage(MultipartFile file, Long id) {
    Image image = getImageById(id);
        try {
            image.setFileName(file.getOriginalFilename());
            image.setFileType(file.getContentType());
            image.setImage(new SerialBlob(file.getBytes()));
            imageRepository.save(image);
        } catch (IOException | SQLException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public void deleteImageById(Long id) {
    imageRepository.findById(id).ifPresentOrElse(imageRepository::delete, ()->{
       throw  new ResolutionException("not found");
    });
    }
}
