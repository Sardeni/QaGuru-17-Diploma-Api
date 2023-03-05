package io.swagger.petstore.models;

import lombok.Data;

import java.util.List;

public @Data class ArrayListPostPetRequestModel {

    List<PostPetRequestModel> list;
}
