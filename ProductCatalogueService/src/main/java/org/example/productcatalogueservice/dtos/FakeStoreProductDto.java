package org.example.productcatalogueservice.dtos;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FakeStoreProductDto {
     Long id;
     String title;
     String description;
     Double price;
     String image;
    String category;
}
