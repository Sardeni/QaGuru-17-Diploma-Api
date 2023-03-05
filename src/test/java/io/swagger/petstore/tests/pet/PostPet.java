package io.swagger.petstore.tests.pet;

import io.qameta.allure.Owner;
import io.swagger.petstore.models.PostPetRequestModel;
import io.swagger.petstore.models.PostPetResponseModel;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import com.github.javafaker.Faker;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static io.swagger.petstore.specs.ProjectSpecs.RequestSpec;
import static io.swagger.petstore.specs.ProjectSpecs.ResponseSpec;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class PostPet {
    Faker faker = new Faker();
   @Test
    @DisplayName("Add a new pet to the store")
    @Owner("emelianovpv")
    @Tag("regress")
    public void postPetCorrectData() {

        String petName = faker.funnyName().name();

        PostPetRequestModel data = new PostPetRequestModel();
        data.setName(petName);
        String[] photoUrlList = {"http://t1.gstatic.com/licensed-image?q=tbn:ANd9GcTVNBVgDTZrFvUARECMzBrur7L34aGgMgeqrY3JE6rWUauX3cRgAjXim93D7cn2UTQM"};
        data.setPhotoUrls(photoUrlList);
        data.setStatus("pending");

       PostPetResponseModel response = given(RequestSpec)
                .body(data)
                .when()
                .post("/pet")
                .then().log().all()
                .spec(ResponseSpec)
                .statusCode(200)
               .extract().as(PostPetResponseModel.class);

       assertEquals(petName, response.getName());
       assertEquals(data.getStatus(), response.getStatus());
     //  assertEquals(data.getPhotoUrls(), response.getPhotoUrls());

    }
}
