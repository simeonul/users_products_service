package com.simeon.webservices.licenta_backend.services;

import com.simeon.webservices.licenta_backend.entities.products.Product;
import com.simeon.webservices.licenta_backend.entities.users.UserAccount;
import com.simeon.webservices.licenta_backend.repositories.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductService {
    private final ProductRepository productRepository;
    private final UserAccountService userAccountService;

    @Autowired
    public ProductService(ProductRepository productRepository,
                          UserAccountService userAccountService) {
        this.productRepository = productRepository;
        this.userAccountService = userAccountService;
    }
    
    public void insertProduct(Product product){
        productRepository.save(product);
    }

    public void updateProduct(Product product){
        List<UserAccount> usersWithProduct = userAccountService.getAllUsersWithSafeProduct(product.getBarcode());
        for (UserAccount user : usersWithProduct) {
            userAccountService.removeSafeProduct(user.getAccountDetails().getEmail(), product.getBarcode());
        }
        productRepository.save(product);
    }

    public Optional<Product> getProduct(String barcode){
        return productRepository.findByBarcode(barcode);
    }

    public void deleteProduct(String barcode) {
        Optional<Product> product = productRepository.findByBarcode(barcode);
        product.ifPresent(productRepository::delete);
    }
}
