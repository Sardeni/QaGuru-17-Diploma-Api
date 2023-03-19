package io.swagger.petstore.tests.pet;

import com.github.javafaker.Faker;
import io.qameta.allure.Owner;
import io.swagger.petstore.PetApi;
import io.swagger.petstore.models.PetModel;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static io.qameta.allure.Allure.step;
import static io.restassured.RestAssured.given;
import static io.swagger.petstore.specs.ProjectSpecs.requestSpec;
import static io.swagger.petstore.specs.ProjectSpecs.responseSpec;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertEquals;

@Owner("emelianovpv")
@Tag("regress")
public class PostPetIdTests {

    Faker faker = new Faker();

    @Test
    @DisplayName("Update a pet in the store with form data")
    @Tag("smoke")
    public void postPetIdCorrectData() {

        String petName = faker.funnyName().name();
        String petNameNew = faker.funnyName().name();
        String[] photoUrlList = {"http://t1.gstatic.com/licensed-image?q=tbn:ANd9GcTVNBVgDTZrFvUARECMzBrur7L34aGgMgeqrY3JE6rWUauX3cRgAjXim93D7cn2UTQM"};

        PetModel data = new PetModel();
        data.setName(petName);
        data.setPhotoUrls(photoUrlList);
        data.setStatus("pending");

        PetModel newPet = PetApi.createNewPet(data);

        Long petId = newPet.getId();
        String petStatusNew = "available";

        step("Updating pet info with form data", () -> {
            given(requestSpec)
                    .contentType("application/x-www-form-urlencoded")
                    .pathParam("petId", petId)
                    .formParam("name", petNameNew)
                    .formParam("status", petStatusNew)
                    .when()
                    .post("/pet/{petId}")
                    .then().log().all()
                    .spec(responseSpec)
                    .statusCode(200);
        });

        PetModel responseId =
                step("Request for checking new pet data", () -> {
                    return given(requestSpec)
                            .pathParam("petId", petId)
                            .when()
                            .get("/pet/{petId}")
                            .then().log().all()
                            .spec(responseSpec)
                            .statusCode(200)
                            .extract().as(PetModel.class);
                });

        step("Checking new parameters", () -> {
            assertEquals(petId, responseId.getId());
            assertEquals(petNameNew, responseId.getName());
            assertEquals(petStatusNew, responseId.getStatus());
        });
    }

    @Test
    @DisplayName("Update a pet with wrong Id type")
    public void postPetIdWithoutFormData() {

        String petNameNew = faker.funnyName().name();

        given(requestSpec)
                .contentType("application/x-www-form-urlencoded")
                .pathParam("petId", "Test")
                .formParam("name", petNameNew)
                .when()
                .post("/pet/{petId}")
                .then().log().all()
                .spec(responseSpec)
                .statusCode(404)
                .body("type", is("unknown"))
                .body("message", is("java.lang.NumberFormatException: For input string: \"Test\""));
    }
}
