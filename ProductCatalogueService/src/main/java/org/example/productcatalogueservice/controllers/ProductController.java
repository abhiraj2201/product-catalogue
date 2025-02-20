package org.example.productcatalogueservice.controllers;


import org.example.productcatalogueservice.dtos.ProductDto;
import org.example.productcatalogueservice.models.Product;
import org.example.productcatalogueservice.services.IProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
public class ProductController {

    @Autowired
    private final IProductService productService;

    public ProductController(IProductService productService) {
        this.productService = productService;
    }

    @GetMapping("/products")
    public List<ProductDto> getProducts(){
        List<Product> products = productService.getProducts();

        return productsToDto(products);
    }

    private List<ProductDto> productsToDto(List<Product> products) {
        List<ProductDto> productDtos = new ArrayList<>();
        for (Product product : products) {
            productDtos.add(toProductDto(product));
        }
        return productDtos;
    }

    private ProductDto toProductDto(Product product) {
        ProductDto productDto = new ProductDto();
        productDto.setName(product.getName());
        productDto.setDescription(product.getDescription());
        productDto.setId(product.getId());
        return productDto;
    }

    @GetMapping("/products/{id}")
    public ResponseEntity<ProductDto> getProductById(@PathVariable("id") Long productId){
        Product product = productService.getProductById(productId);
        ProductDto productDto = toProductDto(product);
        return ResponseEntity.ok(productDto);
    }

    @PostMapping("/products")
    public ResponseEntity<ProductDto> saveProduct(@RequestBody ProductDto productDto){
        Product product =  productService.saveProduct(productDTOtoProduct(productDto));
        productDto = toProductDto(product);
        return ResponseEntity.ok(productDto);
    }

    private Product productDTOtoProduct(ProductDto productDto) {
        Product product = new Product();
        product.setName(productDto.getName());
        product.setDescription(productDto.getDescription());
        product.setId(productDto.getId());
        product.setPrice(productDto.getPrice());
        return product;
    }
}
