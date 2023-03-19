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
public class DeletePetTests {

    Faker faker = new Faker();

    @Test
    @DisplayName("Delete a pet")
    @Tag("smoke")
    public void deletePetCorrectData() {

        String[] photoUrlList = {"http://t1.gstatic.com/licensed-image?q=tbn:ANd9GcTVNBVgDTZrFvUARECMzBrur7L34aGgMgeqrY3JE6rWUauX3cRgAjXim93D7cn2UTQM"};
        String petName = faker.funnyName().name();

        PetModel data = new PetModel();
        data.setName(petName);
        data.setPhotoUrls(photoUrlList);
        data.setStatus("pending");

        PetModel newPet = PetApi.createNewPet(data);

        step("validating newPet data", () -> {
            assertEquals(petName, newPet.getName());
            assertEquals(data.getStatus(), newPet.getStatus());
        });

        Long petId = newPet.getId();

        step("Request to delete created pet", () -> {
            given(requestSpec)
                    .pathParam("petId", petId)
                    .when()
                    .delete("/pet/{petId}")
                    .then().log().all()
                    .spec(responseSpec)
                    .statusCode(200)
                    .body("type", is("unknown"))
                    .body("message", is(petId.toString()));
        });

        step("Checking pet is not exist in DB", () -> {
            given(requestSpec)
                    .pathParam("petId", petId)
                    .when()
                    .get("/pet/{petId}")
                    .then().log().all()
                    .spec(responseSpec)
                    .statusCode(404)
                    .body("message", is("Pet not found"));
        });
    }

    @Test
    @DisplayName("Deleting a pet, request with String type Id")
    public void deletePetWithTextId() {

        given(requestSpec)
                .pathParam("petId", "Test")
                .when()
                .delete("/pet/{petId}")
                .then().log().all()
                .spec(responseSpec)
                .statusCode(404)
                .body("type", is("unknown"))
                .body("message", is("java.lang.NumberFormatException: For input string: \"Test\""));
    }
}