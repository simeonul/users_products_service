package com.simeon.webservices.licenta_backend.clients;

import com.simeon.webservices.licenta_backend.clients.entities.FoodFactsResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;


@FeignClient(name = "openFoodFactsClient", url = "${clients.food_facts.url}")
public interface FoodFactsProxy {
    @GetMapping(
            path = "/product/{barcode}/",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    FoodFactsResponse getProductInfo(
            @PathVariable("barcode") String barcode,
            @RequestParam(value = "fields") String fields
    );
}
