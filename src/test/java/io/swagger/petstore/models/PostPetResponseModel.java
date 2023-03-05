package io.swagger.petstore.models;

import lombok.Data;

public @Data class PostPetResponseModel {
    private Long id;
    private Category category;
    private String name;
    private  String[] photoUrls;
    private Tag[] tags;
    private  String status;
}