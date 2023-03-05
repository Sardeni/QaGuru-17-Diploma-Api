package io.swagger.petstore.tests.pet;

import io.qameta.allure.Owner;
import io.restassured.path.json.JsonPath;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;

import static io.restassured.RestAssured.given;
import static io.swagger.petstore.specs.ProjectSpecs.RequestSpec;
import static io.swagger.petstore.specs.ProjectSpecs.ResponseSpec;

public class GetFindByStatus {

    //@Test
    @DisplayName("Finds pets by status")
    @Owner("emelianovpv")
    @Tag("regress")
    public void getFindByStatus() {

        JsonPath response = given(RequestSpec)

                .param("status", "available")
                .when()
                .get("/pet/findByStatus")
                .then().log().all()
                .spec(ResponseSpec)
                .statusCode(200)
                .extract()
                .jsonPath();

      Long id = response.get("[0].id");
      String name = response.get("[0].name");
        System.out.println(id);
        System.out.println(name);
    }

}
