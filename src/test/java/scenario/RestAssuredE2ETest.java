package scenario;

import java.util.List;

import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import io.restassured.RestAssured;
import io.restassured.response.Response;

public class RestAssuredE2ETest {
    /*
     * List of tests to be implemented:
     * 1. Register
     * 2. Login
     * 3. GetListAllObjects
     * 4. ListOfObjectsByIds
     * 5. SingelObject
     * 6. AddObject
     * 7. UpdateObject
     * 8. DeleteObject
     * 9. GetAllDepartments
     * 10.PartiallyUpdateObject
     */

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

    @BeforeClass
    public void setup() {
        /*
         * Define the base URL for the API
         * String baseUrl = "https://whitesmokehouse.com";
         */
        RestAssured.baseURI = "https://whitesmokehouse.com";

        // Login to the API and get the token
        String jsonLogin = "{\n" + //
                "  \"email\": \"drajattt@gmail.com\",\n" + //
                "  \"password\": \"admin123!\"\n" + //
                "}";
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

    @Test(priority = 1)
    public void testAddObject() {
        // Define the JSON body for adding an object
        String jsonAddObject = "{"
                + "\"name\": \"Lenovo ThinkBook 7i\","
                + "\"data\": {"
                + "  \"year\": 2023,"
                + "  \"price\": 1849.99,"
                + "  \"cpu_model\": \"Intel Core i7\","
                + "  \"hard_disk_size\": \"1 TB\","
                + "  \"capacity\": \"2 cpu\","
                + "  \"screen_size\": \"14 Inch\","
                + "  \"color\": \"silver\""
                + "}"
                + "}";

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
        // Validate the response
        Assert.assertEquals(responseAddObject.getStatusCode(), 200,
                "Expected status code 200 but got " + responseAddObject.getStatusCode());
        Assert.assertNotNull(responseAddObject.jsonPath().get("[0].id"),
                "Expected id but got null");
        Assert.assertEquals(responseAddObject.jsonPath().get("[0].name"), "Lenovo ThinkBook 7i",
                "Expected name Lenovo ThinkBook 7i"
                        + " but got " + responseAddObject.jsonPath().get("[0].name"));

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

        // Ambil semua nama device dari array response
        List<String> nameDevice = responseGetListAllObjects.jsonPath().getList("name");
        System.out.println("Names: " + nameDevice);
        // Pastikan nama spesifik ada
        Assert.assertTrue(nameDevice.contains("Lenovo ThinkBook 7i"),
                "Expected name Lenovo ThinkBook 7i not found in list");

        // Hit the endpoint Single object with valid data
        Response responseSingleObject = RestAssured.given()
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer " + tokenLogin)
                .log().all()
                .when()
                .get("/webhook/8749129e-f5f7-4ae6-9b03-93be7252443c/api/objects/"
                        + responseAddObject.jsonPath().get("[0].id"));
        // Print the response
        System.out.println("Response: " + responseSingleObject.asPrettyString());
        // Validate the response
        Assert.assertEquals(responseSingleObject.getStatusCode(), 200,
                "Expected status code 200 but got " + responseSingleObject.getStatusCode());
        Assert.assertEquals(responseSingleObject.jsonPath().get("name"), "Lenovo ThinkBook 7i",
                "Expected name Lenovo ThinkBook 7i"
                        + " but got " + responseSingleObject.jsonPath().get("name"));
        Assert.assertEquals(responseSingleObject.jsonPath().get("data.cpu_model"), "Intel Core i7",
                "Expected cpu_model Intel Core i7"
                        + " but got " + responseSingleObject.jsonPath().get("data.cpu_model"));
    }

    @Test(priority = 2)
    public void testDeleteObject() {
        // Define the JSON body for adding an object
        String jsonAddObject = "{"
                + "\"name\": \"Lenovo ThinkBook 7i\","
                + "\"data\": {"
                + "  \"year\": 2023,"
                + "  \"price\": 1849.99,"
                + "  \"cpu_model\": \"Intel Core i7\","
                + "  \"hard_disk_size\": \"1 TB\","
                + "  \"capacity\": \"2 cpu\","
                + "  \"screen_size\": \"14 Inch\","
                + "  \"color\": \"silver\""
                + "}"
                + "}";

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
        // Validate the response
        Assert.assertEquals(responseAddObject.getStatusCode(), 200,
                "Expected status code 200 but got " + responseAddObject.getStatusCode());
        Assert.assertNotNull(responseAddObject.jsonPath().get("[0].id"),
                "Expected id but got null");
        Assert.assertEquals(responseAddObject.jsonPath().get("[0].name"), "Lenovo ThinkBook 7i",
                "Expected name Lenovo ThinkBook 7i"
                        + " but got " + responseAddObject.jsonPath().get("[0].name"));

        // Hit the endpoint Delete object with valid data
        Response responseDeleteObject = RestAssured.given()
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer " + tokenLogin)
                .log().all()
                .when()
                .delete("/webhook/d79a30ed-1066-48b6-83f5-556120afc46f/api/objects/"
                        + responseAddObject.jsonPath().get("[0].id"));
        // Print the response
        System.out.println("Response: " + responseDeleteObject.asPrettyString());
        // Validate the response
        Assert.assertEquals(responseDeleteObject.getStatusCode(), 200,
                "Expected status code 200 but got " + responseDeleteObject.getStatusCode());
        Assert.assertEquals(responseDeleteObject.jsonPath().get("message"),
                "Object with id = " + responseAddObject.jsonPath().get("[0].id") + ", has been deleted.",
                "Expected message " + "Object with id = " + responseAddObject.jsonPath().get("[0].id") + ", has been deleted." + " but got "
                        + responseDeleteObject.jsonPath().get("message"));
        
        // Hit the endpoint Single object with valid data
        Response responseSingleObject = RestAssured.given()
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer " + tokenLogin)
                .log().all()
                .when()
                .get("/webhook/8749129e-f5f7-4ae6-9b03-93be7252443c/api/objects/"
                        + responseAddObject.jsonPath().get("[0].id"));
        // Print the response
        System.out.println("Response: " + responseSingleObject.asPrettyString());
        // Validate the response
        Assert.assertEquals(responseSingleObject.getStatusCode(), 200,
                "Expected status code 200 but got " + responseSingleObject.getStatusCode());
        // Assert body kosong (object kosong)
        String body = responseSingleObject.getBody().asString();
        Assert.assertEquals(body.trim(), "{}", "Expected empty JSON object");
    }
}
