package io.swagger.petstore.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class PetModel {
    private Long id;
    private Category category;
    private String name;
    private String[] photoUrls;
    private Tag[] tagsList;
    private String status;
}
