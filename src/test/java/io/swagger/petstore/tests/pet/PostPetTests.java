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
import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

@Owner("emelianovpv")
@Tag("regress")
public class PostPetTests {
    Faker faker = new Faker();

    @Test
    @DisplayName("Add a new pet to the store")
    @Tag("smoke")
    public void postPetCorrectData() {

        String petName = faker.funnyName().name();
        PetModel data = new PetModel();
        data.setName(petName);
        String[] photoUrlList = {"http://t1.gstatic.com/licensed-image?q=tbn:ANd9GcTVNBVgDTZrFvUARECMzBrur7L34aGgMgeqrY3JE6rWUauX3cRgAjXim93D7cn2UTQM"};
        data.setPhotoUrls(photoUrlList);
        data.setStatus("pending");

        PetModel newPet = PetApi.createNewPet(data);

        step("validating pet data", () -> {
            assertEquals(petName, newPet.getName());
            assertEquals(data.getStatus(), newPet.getStatus());
            assertArrayEquals(data.getPhotoUrls(), newPet.getPhotoUrls());
        });

        Long petId = newPet.getId();
        step("Checking pet is created in DB", () -> {
            PetModel responseId = given(requestSpec)
                    .pathParam("petId", petId)
                    .when()
                    .get("/pet/{petId}")
                    .then().log().all()
                    .spec(responseSpec)
                    .statusCode(200)
                    .extract().as(PetModel.class);

            assertEquals(petId, responseId.getId());
            assertEquals(petName, responseId.getName());
            assertEquals(data.getStatus(), responseId.getStatus());
        });

    }

    @Test
    @DisplayName("Request without body, expecting HTTP 405")
    public void postPetWithoutBody() {

        given(requestSpec)
                .when()
                .post("/pet")
                .then().log().all()
                .spec(responseSpec)
                .statusCode(405);
    }
}
