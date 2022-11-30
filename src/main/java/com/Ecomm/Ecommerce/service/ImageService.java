package com.Ecomm.Ecommerce.service;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface ImageService {


    public void saveImage(String email, MultipartFile image) throws IOException;

    public byte[] getImage(String email);
}
