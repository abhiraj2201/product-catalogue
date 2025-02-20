package org.example.productcatalogueservice.services;

import org.example.productcatalogueservice.dtos.ProductDto;
import org.example.productcatalogueservice.models.Product;

import java.util.List;

public interface IProductService {
    public List<Product> getProducts();

    public Product getProductById(Long productId);

    public Product saveProduct(Product product);
}
