package io.swagger.petstore.tests.pet;

import com.github.javafaker.Faker;
import io.qameta.allure.Owner;
import io.swagger.petstore.models.PetModel;
import io.swagger.petstore.models.PutPetResponseModel;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static io.qameta.allure.Allure.step;
import static io.restassured.RestAssured.given;
import static io.swagger.petstore.specs.ProjectSpecs.RequestSpec;
import static io.swagger.petstore.specs.ProjectSpecs.ResponseSpec;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class PutPetTests {

    Faker faker = new Faker();

    @Test
    @DisplayName("Update an existing pet")
    @Owner("emelianovpv")
    @Tag("regress")
    @Tag("smoke")
    public void putPetCorrectData() {

        String petName = faker.funnyName().name();
        String petNameNew = faker.funnyName().name();

        PetModel data = new PetModel();
        data.setName(petName);
        String[] photoUrlList = {"http://t1.gstatic.com/licensed-image?q=tbn:ANd9GcTVNBVgDTZrFvUARECMzBrur7L34aGgMgeqrY3JE6rWUauX3cRgAjXim93D7cn2UTQM"};
        data.setPhotoUrls(photoUrlList);
        data.setStatus("available");

        PetModel response =
                step("create a new pet", () -> {
                    return given(RequestSpec)
                            .body(data)
                            .when()
                            .post("/pet")
                            .then().log().all()
                            .spec(ResponseSpec)
                            .statusCode(200)
                            .extract().as(PetModel.class);
                });

        Long petId = response.getId();

        PetModel putData = new PetModel();
        putData.setId(petId);
        putData.setName(petNameNew);
        putData.setStatus("pending");

        PutPetResponseModel putResponse =
                step("Updating pet parameters", () -> {
                    return given(RequestSpec)
                            .body(putData)
                            .when()
                            .put("/pet")
                            .then().log().all()
                            .spec(ResponseSpec)
                            .statusCode(200)
                            .extract().as(PutPetResponseModel.class);

                });

        step("Checking new parameters in response", () -> {
            assertEquals(petId, putResponse.getId());
            assertEquals(petNameNew, putResponse.getName());
            assertEquals("pending", putResponse.getStatus());
        });
    }

    @Test
    @DisplayName("Error in try to update unexisting pet")
    @Owner("emelianovpv")
    @Tag("regress")
    @Disabled("Incorrect work of service: 200 instead of 404")
    public void putPetUnExistId() {


        String petNameNew = faker.funnyName().name();
        Long petUnExistId = 10000002098000000L;

        step("Checking petId is not exist in DB", () -> {
            given(RequestSpec)
                    .pathParam("petId", petUnExistId)
                    .when()
                    .get("/pet/{petId}")
                    .then().log().all()
                    .spec(ResponseSpec)
                    .statusCode(404)
                    .body("message", is("Pet not found"));
        });

        PetModel putData = new PetModel();
        putData.setId(petUnExistId);
        putData.setName(petNameNew);
        putData.setStatus("pending");

        step("sending request with not exist Id", () -> {
            given(RequestSpec)
                    .body(putData)
                    .when()
                    .put("/pet")
                    .then().log().all()
                    .spec(ResponseSpec)
                    .statusCode(404);
        });
    }
}
