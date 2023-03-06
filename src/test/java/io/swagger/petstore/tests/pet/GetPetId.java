package io.swagger.petstore.tests.pet;

import io.qameta.allure.Owner;
import io.swagger.petstore.models.GetPetIdResponseModel;
import io.swagger.petstore.models.PostPetRequestModel;
import io.swagger.petstore.models.PostPetResponseModel;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import com.github.javafaker.Faker;

import static io.restassured.RestAssured.given;
import static io.swagger.petstore.specs.ProjectSpecs.RequestSpec;
import static io.swagger.petstore.specs.ProjectSpecs.ResponseSpec;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class GetPetId {

    Faker faker = new Faker();
    @Test
    @DisplayName("Finds pets by ID")
    @Owner("emelianovpv")
    @Tag("regress")
    public void getFindById() {

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

        Long petId = response.getId();

        GetPetIdResponseModel responseId = given(RequestSpec)
                    .pathParam("petId", petId)
                    .when()
                    .get("/pet/{petId}")
                    .then().log().all()
                    .spec(ResponseSpec)
                    .statusCode(200)
                    .extract().as(GetPetIdResponseModel.class);

        assertEquals(petId, responseId.getId());
        assertEquals(petName, responseId.getName());
        assertEquals(data.getStatus(), responseId.getStatus());
    }
    @Test
    @DisplayName("Request with unknown Id, expecting HTTP 404")
    @Owner("emelianovpv")
    @Tag("regress")
    public void getFindByIdUnkownId() {

        given(RequestSpec)
                .pathParam("petId", "00000209800000")
                .when()
                .get("/pet/{petId}")
                .then().log().all()
                .spec(ResponseSpec)
                .statusCode(404)
                .body("message", is("Pet not found"));
    }
}
