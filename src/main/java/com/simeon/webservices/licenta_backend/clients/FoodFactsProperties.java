package com.simeon.webservices.licenta_backend.clients;


import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@ConfigurationProperties(prefix = "clients.food-facts")
public class FoodFactsProperties {
    private String url;
    private List<String> productSearchFields;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public List<String> getProductSearchFields() {
        return productSearchFields;
    }

    public void setProductSearchFields(List<String> productSearchFields) {
        this.productSearchFields = productSearchFields;
    }
}
