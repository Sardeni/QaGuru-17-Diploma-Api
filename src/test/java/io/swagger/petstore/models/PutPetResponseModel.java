package io.swagger.petstore.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
@JsonIgnoreProperties(ignoreUnknown = true)
public @Data class PutPetResponseModel {
    private Long id;
    private String name;
    private Tag[] tags;
    private  String status;
}
