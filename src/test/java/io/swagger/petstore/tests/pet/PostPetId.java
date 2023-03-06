package io.swagger.petstore.tests.pet;

import com.github.javafaker.Faker;
import io.qameta.allure.Owner;
import io.swagger.petstore.models.GetPetIdResponseModel;
import io.swagger.petstore.models.PostPetRequestModel;
import io.swagger.petstore.models.PostPetResponseModel;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static io.swagger.petstore.specs.ProjectSpecs.RequestSpec;
import static io.swagger.petstore.specs.ProjectSpecs.ResponseSpec;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class PostPetId {

    Faker faker = new Faker();

    @Test
    @DisplayName("Update a pet in the store with form data")
    @Owner("emelianovpv")
    @Tag("regress")
    public void postPetIdCorrectData() {

        String petName = faker.funnyName().name();
        String petNameNew = faker.funnyName().name();

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

        Long petId = response.getId();
        String petStatusNew = "available";

        given(RequestSpec)
                .contentType("application/x-www-form-urlencoded")
                .pathParam("petId", petId)
                .formParam("name", petNameNew)
                .formParam("status", petStatusNew)
                .when()
                .post("/pet/{petId}")
                .then().log().all()
                .spec(ResponseSpec)
                .statusCode(200);

        GetPetIdResponseModel responseId = given(RequestSpec)
                .pathParam("petId", petId)
                .when()
                .get("/pet/{petId}")
                .then().log().all()
                .spec(ResponseSpec)
                .statusCode(200)
                .extract().as(GetPetIdResponseModel.class);

        assertEquals(petId, responseId.getId());
        assertEquals(petNameNew, responseId.getName());
        assertEquals(petStatusNew, responseId.getStatus());
    }

    @Test
    @DisplayName("Update a pet with wrong Id type")
    @Owner("emelianovpv")
    @Tag("regress")
    public void postPetIdWithoutFormData() {

        String petName = faker.funnyName().name();
        String petNameNew = faker.funnyName().name();

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

        given(RequestSpec)
                .contentType("application/x-www-form-urlencoded")
                .pathParam("petId", "Test")
                .formParam("name", petNameNew)
                .when()
                .post("/pet/{petId}")
                .then().log().all()
                .spec(ResponseSpec)
                .statusCode(404)
                .body("type", is("unknown"))
                .body("message", is("java.lang.NumberFormatException: For input string: \"Test\""));
    }
}
