package io.swagger.petstore;

import io.swagger.petstore.models.PetModel;

import static io.qameta.allure.Allure.step;
import static io.restassured.RestAssured.given;
import static io.swagger.petstore.specs.ProjectSpecs.requestSpec;
import static io.swagger.petstore.specs.ProjectSpecs.responseSpec;

public class PetApi {

    public static PetModel createNewPet(PetModel data) {

        PetModel response =
                step("create a new pet", () -> {
                    return given(requestSpec)
                            .body(data)
                            .when()
                            .post("/pet")
                            .then().log().all()
                            .spec(responseSpec)
                            .statusCode(200)
                            .extract().as(PetModel.class);
                });

        return response;
    }

}
