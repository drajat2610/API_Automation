package apiengine;

import helper.ConfigManager;
import io.restassured.RestAssured;
import io.restassured.response.Response;

public class Endpoints {
    public Endpoints() {
        // Set the base URI for the API
        String baseUrl = ConfigManager.getBaseUrl();
        RestAssured.baseURI = baseUrl;
    }

    public Response loginEmployee(String bodyRequest) {
        Response responseLogin = RestAssured.given()
                .header("Content-Type", "application/json")
                .body(bodyRequest)
                .log().all()
                .when()
                .post("/webhook/employee/login");
        return responseLogin;
    }

    public Response addObject(String bodyRequest, String token) {
        // Send the POST request to add objects
        Response responseAddObject = RestAssured.given()
                .header("Authorization", "Bearer " + token)
                .header("Content-Type", "application/json")
                .body(bodyRequest)
                .log().all()
                .when()
                .post("/webhook/api/objects");
        return responseAddObject;
    }

    public Response getListObjects(String token) {
        // Send the GET request to retrieve objects
        Response responseGetListObjects = RestAssured.given()
                .header("Authorization", "Bearer " + token)        
                .header("Content-Type", "application/json")
                .log().all()
                .when()
                .get("/webhook/api/objects");
        return responseGetListObjects;
    }

    public Response getObjectById(String token, String objectId) {
        Response responseGetObjectById = RestAssured.given()
                .header("Authorization", "Bearer " + token)
                .header("Content-Type", "application/json")
                .log().all()
                .when()
                .get("/webhook/8749129e-f5f7-4ae6-9b03-93be7252443c/api/objects/" + objectId);
        return responseGetObjectById;
    }

    public Response deleteObjectById(String token, String objectId) {
        Response responseDeleteObjectById = RestAssured.given()
                .header("Authorization", "Bearer " + token)
                .header("Content-Type", "application/json")
                .log().all()
                .when()
                .delete("/webhook/8749129e-f5f7-4ae6-9b03-93be7252443c/api/objects/" + objectId);
        return responseDeleteObjectById;
    }
}
