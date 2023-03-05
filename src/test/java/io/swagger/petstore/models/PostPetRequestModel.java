package io.swagger.petstore.models;

import lombok.Data;
public @Data class PostPetRequestModel {
    private Long id;
    private Category category;
    private String name;
    private  String[] photoUrls;
    private Tag[] tagsList;
    private  String status;
}
