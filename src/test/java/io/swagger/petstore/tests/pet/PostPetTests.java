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
import static org.junit.jupiter.api.Assertions.assertEquals;

public class PostPetTests {
    Faker faker = new Faker();

    @Test
    @DisplayName("Add a new pet to the store")
    @Owner("emelianovpv")
    @Tag("regress")
    @Tag("smoke")
    public void postPetCorrectData() {

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

        step("validating pet data", () -> {
            assertEquals(petName, response.getName());
            assertEquals(data.getStatus(), response.getStatus());
          //  assertEquals(data.getPhotoUrls(), response.getPhotoUrls());
        });

    }

    @Test
    @DisplayName("Request without body, expecting HTTP 405")
    @Owner("emelianovpv")
    @Tag("regress")
    public void postPetWithoutBody() {

        given(RequestSpec)
                .when()
                .post("/pet")
                .then().log().all()
                .spec(ResponseSpec)
                .statusCode(405);
    }
}
