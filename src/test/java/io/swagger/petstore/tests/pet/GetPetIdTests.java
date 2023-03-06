package io.swagger.petstore.tests.pet;

import com.github.javafaker.Faker;
import io.qameta.allure.Owner;
import io.swagger.petstore.models.PetModel;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static io.qameta.allure.Allure.step;
import static io.restassured.RestAssured.given;
import static io.swagger.petstore.specs.ProjectSpecs.RequestSpec;
import static io.swagger.petstore.specs.ProjectSpecs.ResponseSpec;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class GetPetIdTests {
    Faker faker = new Faker();

    @Test
    @DisplayName("Finds pets by ID")
    @Owner("emelianovpv")
    @Tag("regress")
    @Tag("smoke")
    public void getFindById() {

        String petName = faker.funnyName().name();

        PetModel data = new PetModel();
        data.setName(petName);
        String[] photoUrlList = {"http://t1.gstatic.com/licensed-image?q=tbn:ANd9GcTVNBVgDTZrFvUARECMzBrur7L34aGgMgeqrY3JE6rWUauX3cRgAjXim93D7cn2UTQM"};
        data.setPhotoUrls(photoUrlList);
        data.setStatus("pending");

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

        step("Checking pet parameters", () -> {
            PetModel responseId = given(RequestSpec)
                    .pathParam("petId", petId)
                    .when()
                    .get("/pet/{petId}")
                    .then().log().all()
                    .spec(ResponseSpec)
                    .statusCode(200)
                    .extract().as(PetModel.class);

            assertEquals(petId, responseId.getId());
            assertEquals(petName, responseId.getName());
            assertEquals(data.getStatus(), responseId.getStatus());
        });
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