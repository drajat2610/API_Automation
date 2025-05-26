package scenario;

import java.util.List;

import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.apiautomation.model.request.RequestAddObject;
import com.apiautomation.model.request.RequestLogin;
import com.apiautomation.model.response.ResponseListObject;
import com.apiautomation.model.response.ResponseAddObject;
import com.apiautomation.model.response.ResponseSingelObject;

import io.restassured.RestAssured;
import io.restassured.response.Response;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class E2EPojoImpl {

    /*
     * Scenario : RestAssured E2E Test
     * Test Case 01 : Add Object
     * 1. Hit the endpoint login with valid data
     * 2. Hit the endpoint add object with valid data
     * 3. Hit the endpoint get list all objects
     * 4. Hit the endpoint singel object with valid data
     * 
     * Test Case 02 : Delete Object
     * 1. Hit the endpoint login with valid data
     * 2. Hit the endpoint add object with valid data
     * 3. Hit the endpoint delete object with valid data
     * 4. Hit the endpoint singel object with valid data
     */

    String tokenLogin;
    int objectId;

    @BeforeClass()
    public void setup() throws JsonProcessingException {
        /*
         * Define the base URL for the API
         * String baseUrl = "https://whitesmokehouse.com";
         */
        RestAssured.baseURI = "https://whitesmokehouse.com";

        // Login to the API and get the token
        // Serialize the object to JSON
        RequestLogin requestLogin = new RequestLogin("drajattt@gmail.com", "admin123!");
        ObjectMapper objectMapper = new ObjectMapper();

        String jsonLogin = objectMapper.writeValueAsString(requestLogin);
        // Print the JSON string
        System.out.println("JSON String: " + jsonLogin);

        // Send POST request to login endpoint
        Response responseLogin = RestAssured.given()
                .header("Content-Type", "application/json")
                .body(jsonLogin)
                .log().all()
                .when()
                .post("/webhook/api/login");
        Assert.assertEquals(responseLogin.getStatusCode(), 200,
                "Expected status code 200 but got " + responseLogin.getStatusCode());
        tokenLogin = responseLogin.jsonPath().getString("token");
        // Print the token
        System.out.println("Token: " + tokenLogin);
    }

    // -------------------- TEST CASE 01 --------------------
    @Test(priority = 1)
    public void testAddObject() throws JsonProcessingException {
        
        // Serialize the object to JSON
        RequestAddObject requestAdd = new RequestAddObject(
                "Lenovo ThinkBook 7i", 
                new RequestAddObject.Data(2023, 1849.99, "Intel Core i7", "1 TB", "silver", "2 cpu", "14 Inch"));
        String jsonAddObject = new ObjectMapper().writeValueAsString(requestAdd);
        ObjectMapper objectMapper = new ObjectMapper();

        // Print the JSON string
        System.out.println("JSON String: " + jsonAddObject);
        // Print the object
        System.out.println("Object: " + objectMapper.writeValueAsString(requestAdd));

        // Send POST request to add object endpoint
        Response responseAddObject = RestAssured.given()
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer " + tokenLogin)
                .body(jsonAddObject)
                .log().all()
                .when()
                .post("/webhook/api/objects");
        // Print the response
        System.out.println("Response: " + responseAddObject.asPrettyString());

        ResponseAddObject[] resAdd = responseAddObject.as(ResponseAddObject[].class);
        objectId = resAdd[0].id; // simpan ID
        // Validate the response
        Assert.assertNotNull(resAdd[0].id, "Response should not be null");
        Assert.assertEquals(resAdd[0].name, "Lenovo ThinkBook 7i",
                "Expected name Lenovo ThinkBook 7i but got " + resAdd[0].name);
        Assert.assertEquals(resAdd[0].data.year, "2023",
                "Expected year 2023 but got " + resAdd[0].data.year);
        Assert.assertEquals(resAdd[0].data.price, "1849.99",
                "Expected price 1849.99 but got " + resAdd[0].data.price);
        Assert.assertEquals(resAdd[0].data.cpuModel, "Intel Core i7",
                "Expected cpu_model Intel Core i7 but got " + resAdd[0].data.cpuModel);
        Assert.assertEquals(resAdd[0].data.hardDiskSize, "1 TB",
                "Expected hard_disk_size 1 TB but got " + resAdd[0].data.hardDiskSize);
        Assert.assertEquals(resAdd[0].data.color, "silver",
                "Expected color silver but got " + resAdd[0].data.color);
        Assert.assertEquals(resAdd[0].data.capacity, "2 cpu",
                "Expected capacity 2 cpu but got " + resAdd[0].data.capacity);
        Assert.assertEquals(resAdd[0].data.screenSize, "14 Inch",
                "Expected screen_size 14 Inch but got " + resAdd[0].data.screenSize);    


        // Hit the endpoint Get list of all objects
        Response responseGetListAllObjects = RestAssured.given()
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer " + tokenLogin)
                .log().all()
                .when()
                .get("/webhook/api/objects");
        // Print the response
        System.out.println("Response: " + responseGetListAllObjects.asPrettyString());
        // Validate the response
        Assert.assertEquals(responseGetListAllObjects.getStatusCode(), 200,
                "Expected status code 200 but got " + responseGetListAllObjects.getStatusCode());

        // Get all device name from array response
        ResponseListObject[] resGetList = responseGetListAllObjects.as(ResponseListObject[].class);
        List<String> nameDevice = java.util.Arrays.stream(resGetList)
            .map(obj -> obj.name)
            .collect(java.util.stream.Collectors.toList());
        System.out.println("Names: " + nameDevice);
        // Make sure the specific name exists
        Assert.assertTrue(nameDevice.contains("Lenovo ThinkBook 7i"),
                "Expected name Lenovo ThinkBook 7i not found in list");

        // Hit the endpoint Single object with valid data
        Response responseSingleObject = RestAssured.given()
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer " + tokenLogin)
                .log().all()
                .when()
                .get("/webhook/8749129e-f5f7-4ae6-9b03-93be7252443c/api/objects/"
                        + objectId);
        // Print the response
        System.out.println("Response: " + responseSingleObject.asPrettyString());

        ResponseSingelObject resSingel = responseSingleObject.as(ResponseSingelObject.class);
        Assert.assertEquals(resSingel.name, "Lenovo ThinkBook 7i",
                "Expected name Lenovo ThinkBook 7i but got " + resSingel.name);
        Assert.assertEquals(resSingel.data.year, "2023",
                "Expected year 2023 but got " + resSingel.data.year);
        Assert.assertEquals(resSingel.data.price, "1849.99",
                "Expected price 1849.99 but got " + resSingel.data.price);
        Assert.assertEquals(resSingel.data.cpuModel, "Intel Core i7",
                "Expected cpu_model Intel Core i7 but got " + resSingel.data.cpuModel);
        Assert.assertEquals(resSingel.data.hardDiskSize, "1 TB",
                "Expected hard_disk_size 1 TB but got " + resSingel.data.hardDiskSize);
        Assert.assertEquals(resSingel.data.color, "silver",
                "Expected color silver but got " + resSingel.data.color);
    }

    // -------------------- TEST CASE 02 --------------------
    @Test(priority = 2, dependsOnMethods = "testAddObject")
    public void testDeleteObject() throws JsonProcessingException {
        // Hit the endpoint Delete object with valid data
        Response responseDeleteObject = RestAssured.given()
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer " + tokenLogin)
                .log().all()
                .when()
                .delete("/webhook/d79a30ed-1066-48b6-83f5-556120afc46f/api/objects/"
                        + objectId);
        
        // Print the response
        System.out.println("Response: " + responseDeleteObject.asPrettyString());
        
        // Validate the response
        Assert.assertEquals(responseDeleteObject.getStatusCode(), 200,
                "Expected status code 200 but got " + responseDeleteObject.getStatusCode());
        Assert.assertEquals(responseDeleteObject.jsonPath().get("message"),
                "Object with id = " + objectId + ", has been deleted.",
                "Expected message " + "Object with id = " + objectId + ", has been deleted." + " but got "
                        + responseDeleteObject.jsonPath().get("message"));
        
        // Hit the endpoint Single object with valid data
        Response responseSingleObject = RestAssured.given()
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer " + tokenLogin)
                .log().all()
                .when()
                .get("/webhook/8749129e-f5f7-4ae6-9b03-93be7252443c/api/objects/"
                        + objectId);
        // Print the response
        System.out.println("Response: " + responseSingleObject.asPrettyString());
        // Validate the response
        Assert.assertEquals(responseSingleObject.getStatusCode(), 200,
                "Expected status code 200 but got " + responseSingleObject.getStatusCode());
        String body = responseSingleObject.getBody().asString();
        Assert.assertEquals(body.trim(), "{}", "Expected empty JSON object");
    }
}
