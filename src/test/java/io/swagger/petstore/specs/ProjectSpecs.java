package io.swagger.petstore.specs;

import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;

import static io.restassured.RestAssured.with;
import static io.restassured.filter.log.LogDetail.ALL;
import static io.restassured.http.ContentType.JSON;
import static io.swagger.petstore.helpers.CustomApiListener.withCustomTemplates;

public class ProjectSpecs {

    public static RequestSpecification RequestSpec = with()
            .header("api_key", "special-key")
            .log().all()
            .filter(withCustomTemplates())
            .baseUri("https://petstore.swagger.io")
            .basePath("/v2")
            .contentType(JSON);


    public static ResponseSpecification ResponseSpec = new ResponseSpecBuilder()
            .log(ALL)
            .build();
}