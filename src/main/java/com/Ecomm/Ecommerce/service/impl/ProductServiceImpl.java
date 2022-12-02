package com.Ecomm.Ecommerce.service.impl;

import com.Ecomm.Ecommerce.Dto.ProductDto;
import com.Ecomm.Ecommerce.Dto.ResponseDto.ProductViewDto;
import com.Ecomm.Ecommerce.Dto.UpdateDto.ProductUpdateDto;
import com.Ecomm.Ecommerce.entities.Category;
import com.Ecomm.Ecommerce.entities.Product;
import com.Ecomm.Ecommerce.entities.Seller;
import com.Ecomm.Ecommerce.entities.User;
import com.Ecomm.Ecommerce.handler.InvalidException;
import com.Ecomm.Ecommerce.handler.ResourceNotFoundException;
import com.Ecomm.Ecommerce.handler.UserAlreadyExistsException;
import com.Ecomm.Ecommerce.repository.CategoryRepo;
import com.Ecomm.Ecommerce.repository.ProductRepo;
import com.Ecomm.Ecommerce.repository.SellerRepo;
import com.Ecomm.Ecommerce.repository.UserRepo;
import com.Ecomm.Ecommerce.service.EmailService;
import com.Ecomm.Ecommerce.service.ProductService;
import com.Ecomm.Ecommerce.utils.IgnoreNull;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

@Service
@Transactional
public class ProductServiceImpl implements ProductService {

 @Autowired
    CategoryRepo categoryRepo;

 @Autowired
    MessageSource messageSource;

 @Autowired
    SellerRepo sellerRepo;
 @Autowired
    UserRepo userRepo;

 @Autowired
 ProductRepo productRepo;

 @Autowired
    EmailService emailService;

    public String addProduct(String sellerEmail, ProductDto productDto){
        Locale locale  = LocaleContextHolder.getLocale();
        Product product  = new Product();
        //Get product Category ID.
        Long productCategoryId = productDto.getCategoryId();
        Category category = categoryRepo.findById(productCategoryId).orElseThrow(
                () -> new ResourceNotFoundException(
                        messageSource.getMessage("api.response.invalidCatId",null,locale)
                ));
        if (!category.getChildren().isEmpty()) {
            throw new ResourceNotFoundException(
                    messageSource.getMessage("api.response.invalidCatId",null,locale)
            );
        }
        User user = userRepo.findByEmail(sellerEmail);
        Seller seller = sellerRepo.findByUser(user);
        String  productName = productDto.getName();
        long sellerId = seller.getId();
        String productBrand = productDto.getBrand();
        long CategoryId = productDto.getCategoryId();
        if (productRepo.findExistingProduct(productName, sellerId, productBrand, CategoryId).isPresent()) {
            throw new UserAlreadyExistsException(
                    messageSource.getMessage("api.response.productAlreadyExists",null,locale)
            );
        }
        product.setCategory(category);
        product.setSeller(seller);
        product.setName(productDto.getName());
        product.setBrand(productDto.getBrand());
        product.setDescription(productDto.getDescription());
        product.setCancellable(false);
        product.setReturnable(false);
        long prodId = productRepo.save(product).getId();
        emailService.sendProductAddEmail(user,product.toString());
        return messageSource.getMessage("api.response.productAdd", null,Locale.ENGLISH) + prodId;
    }
    public ProductViewDto fetchProduct(String email, Long prodId){
        Locale locale  = LocaleContextHolder.getLocale();
        Product product = productRepo.findById(prodId).orElseThrow(
                () -> new ResourceNotFoundException(
                        messageSource.getMessage("api.response.productNotFound",null,locale)
                ));
        User user = userRepo.findByEmail(email);
        Seller seller=sellerRepo.findByUser(user);
        boolean isProductExists = product.getSeller().getId()==seller.getId();
        if(!isProductExists){
            throw new ResourceNotFoundException(
                    messageSource.getMessage("api.response.productNotFound",null,locale)
            );
        }
        ProductViewDto productDto = new ProductViewDto();
        productDto.setCategory(product.getCategory());
        productDto.setName(product.getName());
        productDto.setBrand(product.getBrand());
        productDto.setDescription(product.getDescription());
        productDto.setProductId(product.getId());
        productDto.setActive(product.isActive());
        return productDto;
    }
    public String removeProduct(String sellerEmail, Long productId) {
        Locale locale  = LocaleContextHolder.getLocale();
        Product product = productRepo.findById(productId).orElseThrow(
                () -> new ResourceNotFoundException(
                        messageSource.getMessage("api.response.invalidProdId" + productId,null,locale)));
        User user = userRepo.findByEmail(sellerEmail);
        Seller seller=sellerRepo.findByUser(user);
        boolean isProductExists = product.getSeller().getId()==seller.getId();
        if(!isProductExists){
            throw new ResourceNotFoundException(
                    messageSource.getMessage("api.response.productNotFound",null,locale)
            );
        }
        productRepo.delete(product);
        return messageSource.getMessage("api.response.productDeleted",null,locale);
    }

    public List<ProductViewDto> fetchAllProducts(String sellerEmail){
        Locale locale  = LocaleContextHolder.getLocale();
        User user = userRepo.findByEmail(sellerEmail);
        Seller seller=sellerRepo.findByUser(user);
        List<Product> productList=productRepo.findBySeller(seller);
        if(productList.isEmpty()){
            throw new ResourceNotFoundException(
                    messageSource.getMessage("api.response.productNotFound",null,locale)
            );
        }
        List<ProductViewDto> productViewDtoList=new ArrayList<>();
        productList.forEach((product)->{
            ProductViewDto productDto = new ProductViewDto();
            productDto.setCategory(product.getCategory());
            productDto.setName(product.getName());
            productDto.setBrand(product.getBrand());
            productDto.setDescription(product.getDescription());
            productDto.setProductId(product.getId());
            productDto.setActive(product.isActive());
            productViewDtoList.add(productDto);
        });
        return productViewDtoList;
    }

    public String updateProduct(String name, Long prodId, ProductUpdateDto newProduct){
        Locale locale  = LocaleContextHolder.getLocale();
        Product product=productRepo.findById(prodId).orElseThrow(
                ()-> new ResourceNotFoundException(
                        messageSource.getMessage("api.response.productNotFound",null,locale)
                ));

        Seller seller=sellerRepo.findByUser(userRepo.findByEmail(name));
        boolean isProductExists = product.getSeller().getId()==seller.getId();
        if(!isProductExists){
            throw new ResourceNotFoundException(
                    messageSource.getMessage("api.response.productNotFound",null,locale)
            );
        }

        ProductUpdateDto updatedProduct=new ProductUpdateDto();
        if(newProduct.equals(updatedProduct)){
            throw new InvalidException(
                    messageSource.getMessage("api.response.jsonEmpty",null,locale)
            );
        }

        BeanUtils.copyProperties(newProduct,product, IgnoreNull.getNullPropertyNames(newProduct));
        productRepo.save(product);
        return messageSource.getMessage("api.response.productUpdateSuccess",null,locale) ;
    }

    public String activateProduct(Long productId){
        Locale locale  = LocaleContextHolder.getLocale();
        Product product=productRepo.findById(productId).orElseThrow(
                ()->new ResourceNotFoundException(
                        messageSource.getMessage("api.response.productNotFound",null,locale)
                ));

        if(product.isActive()){
            throw new UserAlreadyExistsException(
                    messageSource.getMessage("api.response.productAlreadyActive",null,locale)
            );
        }
        product.setActive(true);
        User seller=product.getSeller().getUser();
        emailService.sendProductActivateEmail(seller,product.getName());
        productRepo.save(product);
        return messageSource.getMessage("api.response.productActivateSuccess",null,locale);
    }

    public String deActivateProduct(Long productId){
        Locale locale  = LocaleContextHolder.getLocale();
        Product product=productRepo.findById(productId).orElseThrow(
                ()->new ResourceNotFoundException(
                        messageSource.getMessage("api.response.productNotFound",null,locale)
                ));

        if(!product.isActive()){
            throw new UserAlreadyExistsException(
                    messageSource.getMessage("api.response.productAlreadydeActive",null,locale)
            );
        }
        product.setActive(false);
        User seller=product.getSeller().getUser();
        emailService.sendProductdeActivateEmail(seller,product.getName());
        productRepo.save(product);
        return messageSource.getMessage("api.response.productDeActiveSuccess",null,locale);
    }


}
