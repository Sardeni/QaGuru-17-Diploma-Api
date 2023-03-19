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
public class GetPetIdTests {
    Faker faker = new Faker();

    @Test
    @DisplayName("Finds pets by ID")
    @Tag("smoke")
    public void getFindById() {

        String petName = faker.funnyName().name();
        String[] photoUrlList = {"http://t1.gstatic.com/licensed-image?q=tbn:ANd9GcTVNBVgDTZrFvUARECMzBrur7L34aGgMgeqrY3JE6rWUauX3cRgAjXim93D7cn2UTQM"};

        PetModel data = new PetModel();
        data.setName(petName);
        data.setPhotoUrls(photoUrlList);
        data.setStatus("pending");

        PetModel newPet = PetApi.createNewPet(data);

        Long petId = newPet.getId();

        step("Checking pet parameters", () -> {
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
    @DisplayName("Request with unknown Id, expecting HTTP 404")
    public void getFindByIdUnkownId() {

        given(requestSpec)
                .pathParam("petId", "00000209800000")
                .when()
                .get("/pet/{petId}")
                .then().log().all()
                .spec(responseSpec)
                .statusCode(404)
                .body("message", is("Pet not found"));
    }
}