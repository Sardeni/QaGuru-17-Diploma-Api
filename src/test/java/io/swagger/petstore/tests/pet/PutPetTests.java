package io.swagger.petstore.tests.pet;

import com.github.javafaker.Faker;
import io.qameta.allure.Owner;
import io.swagger.petstore.PetApi;
import io.swagger.petstore.models.PetModel;
import io.swagger.petstore.models.PutPetResponseModel;
import org.junit.jupiter.api.Disabled;
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
public class PutPetTests {

    Faker faker = new Faker();

    @Test
    @DisplayName("Update an existing pet")
    @Tag("smoke")
    public void putPetCorrectData() {

        String petName = faker.funnyName().name();
        String petNameNew = faker.funnyName().name();
        String[] photoUrlList = {"http://t1.gstatic.com/licensed-image?q=tbn:ANd9GcTVNBVgDTZrFvUARECMzBrur7L34aGgMgeqrY3JE6rWUauX3cRgAjXim93D7cn2UTQM"};

        PetModel data = new PetModel();
        data.setName(petName);
        data.setPhotoUrls(photoUrlList);
        data.setStatus("available");

        PetModel newPet = PetApi.createNewPet(data);

        Long petId = newPet.getId();

        PetModel putData = new PetModel();
        putData.setId(petId);
        putData.setName(petNameNew);
        putData.setStatus("pending");

        PutPetResponseModel putResponse =
                step("Updating pet parameters", () -> {
                    return given(requestSpec)
                            .body(putData)
                            .when()
                            .put("/pet")
                            .then().log().all()
                            .spec(responseSpec)
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
    @Disabled("Incorrect work of service: it's creating a new pet with PUT, not updating")
    public void putPetUnExistId() {


        String petNameNew = faker.funnyName().name();
        Long petUnExistId = 10000002098000000L;

        step("Checking petId is not exist in DB", () -> {
            given(requestSpec)
                    .pathParam("petId", petUnExistId)
                    .when()
                    .get("/pet/{petId}")
                    .then().log().all()
                    .spec(responseSpec)
                    .statusCode(404)
                    .body("message", is("Pet not found"));
        });

        PetModel putData = new PetModel();
        putData.setId(petUnExistId);
        putData.setName(petNameNew);
        putData.setStatus("pending");

        step("sending request with not exist Id", () -> {
            given(requestSpec)
                    .body(putData)
                    .when()
                    .put("/pet")
                    .then().log().all()
                    .spec(responseSpec)
                    .statusCode(404);
        });
    }
}
