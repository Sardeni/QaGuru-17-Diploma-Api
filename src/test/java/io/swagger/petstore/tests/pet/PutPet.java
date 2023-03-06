package io.swagger.petstore.tests.pet;

import com.github.javafaker.Faker;
import io.qameta.allure.Issue;
import io.qameta.allure.Owner;
import io.swagger.petstore.models.PostPetRequestModel;
import io.swagger.petstore.models.PostPetResponseModel;
import io.swagger.petstore.models.PutPetRequestModel;
import io.swagger.petstore.models.PutPetResponseModel;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static io.swagger.petstore.specs.ProjectSpecs.RequestSpec;
import static io.swagger.petstore.specs.ProjectSpecs.ResponseSpec;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class PutPet {

    Faker faker = new Faker();
    @Test
    @DisplayName("Update an existing pet")
    @Owner("emelianovpv")
    @Tag("regress")
    public void putPetCorrectData() {

        String petName = faker.funnyName().name();
        String petNameNew = faker.funnyName().name();

        PostPetRequestModel data = new PostPetRequestModel();
        data.setName(petName);
        String[] photoUrlList = {"http://t1.gstatic.com/licensed-image?q=tbn:ANd9GcTVNBVgDTZrFvUARECMzBrur7L34aGgMgeqrY3JE6rWUauX3cRgAjXim93D7cn2UTQM"};
        data.setPhotoUrls(photoUrlList);
        data.setStatus("available");

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

        Long petId = response.getId();

        PutPetRequestModel putData =  new PutPetRequestModel();
        putData.setId(petId);
        putData.setName(petNameNew);
        putData.setStatus("pending");

        PutPetResponseModel putResponse = given(RequestSpec)
                .body(putData)
                .when()
                .put("/pet")
                .then().log().all()
                .spec(ResponseSpec)
                .statusCode(200)
                .extract().as(PutPetResponseModel.class);

        assertEquals(putResponse.getId(), petId);
        assertEquals(putResponse.getName(), petNameNew);
        assertEquals(putResponse.getStatus(), "pending");
    }

    @Test
    @DisplayName("Error in try to update unexisting pet")
    @Owner("emelianovpv")
    @Tag("regress")
    @Disabled("Incorrect work of service: 200 instead of 404 ")
    public void putPetUnExistId() {


        String petNameNew = faker.funnyName().name();
        Long petUnExistId = 10000002098000000L;

        given(RequestSpec)
                .pathParam("petId", petUnExistId)
                .when()
                .get("/pet/{petId}")
                .then().log().all()
                .spec(ResponseSpec)
                .statusCode(404)
                .body("message", is("Pet not found"));


        PutPetRequestModel putData =  new PutPetRequestModel();
        putData.setId(petUnExistId);
        putData.setName(petNameNew);
        putData.setStatus("pending");

        given(RequestSpec)
                .body(putData)
                .when()
                .put("/pet")
                .then().log().all()
                .spec(ResponseSpec)
                .statusCode(404);
    }
}
