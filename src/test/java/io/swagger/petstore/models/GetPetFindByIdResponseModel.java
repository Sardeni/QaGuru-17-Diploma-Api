package io.swagger.petstore.models;

import lombok.Data;

public @Data class GetPetFindByIdResponseModel {
     Long id;
     Category category;
     String name;
     String[] photoUrls;
     Tag[] tags;
     String status;
}