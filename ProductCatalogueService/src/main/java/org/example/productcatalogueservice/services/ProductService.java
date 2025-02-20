package org.example.productcatalogueservice.services;

import org.example.productcatalogueservice.dtos.FakeStoreProductDto;
import org.example.productcatalogueservice.dtos.ProductDto;
import org.example.productcatalogueservice.models.Category;
import org.example.productcatalogueservice.models.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

@Service
public class ProductService implements IProductService {

    @Autowired
    private RestTemplateBuilder restTemplateBuilder;

    @Override
    public List<Product> getProducts(){
        RestTemplate restTemplate = restTemplateBuilder.build();
        ResponseEntity<FakeStoreProductDto[]> response = restTemplate.getForEntity("https://fakestoreapi.com/products",
                FakeStoreProductDto[].class);
        if (response.getStatusCode().is5xxServerError() || response.getBody() == null){
            return null;
        }
        return fakeStoreProductDtoTOProductList(response.getBody());

    }

    @Override
    public Product getProductById(@PathVariable("id") Long productId){
        RestTemplate restTemplate = restTemplateBuilder.build();
        ResponseEntity<FakeStoreProductDto> fakeStoreProductDtoResponseEntity = restTemplate.getForEntity("https://fakestoreapi.com/products/{productId}",
                FakeStoreProductDto.class, productId);
        if (fakeStoreProductDtoResponseEntity.getStatusCode().is5xxServerError() || fakeStoreProductDtoResponseEntity.getBody() == null){
            return null;
        }
       return fakeStoreProductDtoTOProduct( fakeStoreProductDtoResponseEntity.getBody());
    }

    private Product fakeStoreProductDtoTOProduct(FakeStoreProductDto fakeStoreProductDto){
        Product product = new Product();
        product.setName(fakeStoreProductDto.getTitle());
        product.setDescription(fakeStoreProductDto.getCategory());
        product.setId(fakeStoreProductDto.getId());
        product.setPrice(fakeStoreProductDto.getPrice());
        product.setImageUrl(fakeStoreProductDto.getImage());
        Category category = new Category();
        category.setName(fakeStoreProductDto.getCategory());
        product.setCategory(category);
        return product;
    }

    private List<Product> fakeStoreProductDtoTOProductList(FakeStoreProductDto[] fakeStoreProductDto){
        List<Product> products = new ArrayList<>();
        for (FakeStoreProductDto fakeStoreProductDto1 : fakeStoreProductDto) {
            products.add(fakeStoreProductDtoTOProduct(fakeStoreProductDto1));
        }
        return products;
    }

    @Override
    public Product saveProduct(Product product) {
        RestTemplate restTemplate = restTemplateBuilder.build();
        FakeStoreProductDto fakeStoreProductDto = productToFakeProductDto(product);
        ResponseEntity<FakeStoreProductDto> fakeStoreProductDtoResponseEntity = restTemplate.postForEntity("https://fakestoreapi.com/products",
                fakeStoreProductDto, FakeStoreProductDto.class);
        if (fakeStoreProductDtoResponseEntity.getStatusCode().is5xxServerError() || fakeStoreProductDtoResponseEntity.getBody() == null){
            return null;
        }
        return fakeStoreProductDtoTOProduct(fakeStoreProductDtoResponseEntity.getBody());
    }

    private FakeStoreProductDto productToFakeProductDto(Product product){
        FakeStoreProductDto fakeStoreProductDto = new FakeStoreProductDto();
        fakeStoreProductDto.setId(product.getId());
        fakeStoreProductDto.setTitle(product.getName());
        fakeStoreProductDto.setPrice(product.getPrice());
        fakeStoreProductDto.setImage(product.getImageUrl());
        return fakeStoreProductDto;
    }

}
