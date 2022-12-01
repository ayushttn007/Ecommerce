package com.Ecomm.Ecommerce.service.impl;

import com.Ecomm.Ecommerce.entities.User;
import com.Ecomm.Ecommerce.handler.ResourceNotFoundException;
import com.Ecomm.Ecommerce.repository.UserRepo;
import com.Ecomm.Ecommerce.service.ImageService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

@Service
public class ImageServiceImpl implements ImageService {
    @Value("${project.profile.img}")
    private String imgPath;

    Logger logger = LoggerFactory.getLogger(ImageService.class);

    @Autowired
    UserRepo userRepo;

    public void saveImage(String email, MultipartFile image) throws IOException {

        User user= userRepo .findByEmail(email);
        String userImgName = user.getFirstName() + user.getId();
        File folder = new File(imgPath);
        if(!folder.exists()){
            folder.mkdir();
        }
        String filePath = imgPath + File.separator + userImgName+ ".png";
        Files.copy(image.getInputStream(), Paths.get(filePath), StandardCopyOption.REPLACE_EXISTING);

    }


    public byte[] getImage(String email) {
        logger.info("getImage :execution start.");
        final String imgName = email;
        User user= userRepo .findByEmail(email);
        String userImgName = user.getFirstName() + user.getId();
        String filename = imgPath + File.separator + userImgName + ".png";

        File folder = new File(imgPath);
        File[] files = folder.listFiles(new FilenameFilter() {
            public boolean accept(File folder , String name) {
                return name.startsWith(userImgName);
            }});

        if (files.length==0){
            logger.debug("getImage: Image Not found");
            logger.info("getImage:Execution end.");
            return new byte[0];
        }
        else {
            try {
                logger.debug("getImage :  returning byte array of the image");
                InputStream inputStream = new FileInputStream(filename);
                logger.info("getImage :  Execution End.");
                return inputStream.readAllBytes();
            } catch (ResourceNotFoundException e) {
                logger.error("getImage :: Exception : Image not found");
                throw new RuntimeException(e);
            }
            catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

}
