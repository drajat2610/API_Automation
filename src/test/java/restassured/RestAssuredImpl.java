package restassured;

import org.testng.Assert;
import org.testng.annotations.Test;

import io.restassured.RestAssured;
import io.restassured.response.Response;

public class RestAssuredImpl {
    String token;

    @Test()
    public void testLogin() {
        /*
         * Define the base URL for the API
         * String baseUrl = "https://whitesmokehouse.com";
         */
        RestAssured.baseURI = "https://whitesmokehouse.com";

        // Create login request
        String requestBody = "{\n" + //
                "  \"email\": \"drajattt@gmail.com\",\n" + //
                "  \"password\": \"admin123!\"\n" + //
                "}";
        // Send POST request to login endpoint
        Response response = RestAssured.given()
                .contentType("application/json")
                .header("Content-Type", "application/json")
                .body(requestBody)
                .log().all()
                .when()
                .post("/webhook/api/login");
        // Print the response
        System.out.println("Response: " + response.asPrettyString());
        token = response.jsonPath().getString("token");
        System.out.println("Token: " + token);

        // Validate the response
        assert response.getStatusCode() == 200 : "Expected status code 200 but got " + response.getStatusCode();
    }

    @Test(dependsOnMethods = "testLogin")
    public void testRegister() {
        /*
         * Define the base URL for the API
         * String baseUrl = "https://whitesmokehouse.com";
         */
        RestAssured.baseURI = "https://whitesmokehouse.com";

        // Create login request
        String requestBody = "{\n" + //
                "  \"email\": \"drajatpamuji@gmail.com\",\n" + //
                "  \"full_name\": \"Drajat Pamuji\",\n" + //
                "  \"password\": \"@dmin123\",\n" + //
                "  \"department\": \"Finance\",\n" + //
                "  \"phone_number\": \"081807078097\"\n" + //
                "}";
        // Send POST request to login endpoint
        Response response = RestAssured.given()
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer " + token)
                .body(requestBody)
                .log().all()
                .when()
                .post("/webhook/api/register");
        // Print the response
        System.out.println("Response: " + response.asPrettyString());
    }

    @Test(dependsOnMethods = "testLogin")
    public void testGetListAllObjects() {
        /*
         * Define the base URL for the API
         * String baseUrl = "https://whitesmokehouse.com";
         */
        RestAssured.baseURI = "https://whitesmokehouse.com";

        // Send POST request to login endpoint
        Response response = RestAssured.given()
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer " + token)
                .log().all()
                .when()
                .get("/webhook/api/objects");
        // Print the response
        System.out.println("Response: " + response.asPrettyString());

        // Validate the response
        assert response.getStatusCode() == 200 : "Expected status code 200 but got " + response.getStatusCode();
        // assert response.jsonPath().getString("[0].name").equals("Lenovo Ideapad 7i")
        //         : "Expected name Lenovo Ideapad 7i but got " + response.jsonPath().getString("[0].name");
        // assert response.jsonPath().getString("[0].data['CPU model']").equals("Intel Core i7")
        //         : "Expected CPU model Intel Core i7 but got " + response.jsonPath().getString("[0].data['CPU model']");
    }

    @Test(dependsOnMethods = "testLogin")
    public void testListOfObjectsByIds() {
        /*
         * Define the base URL for the API
         * String baseUrl = "https://whitesmokehouse.com";
         */
        String id1 = "108";
        String id2 = "37";

        RestAssured.baseURI = "https://whitesmokehouse.com";

        // Create login request
        // Send POST request to login endpoint
        Response response = RestAssured.given()
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer " + token)
                .log().all()
                .when()
                .get("/webhook/api/objectslistId?id=" + id1 + "&id=" + id2);
        // Print the response
        System.out.println("Response: " + response.asPrettyString());

        // Validate the response
        assert response.getStatusCode() == 200 : "Expected status code 200 but got " + response.getStatusCode();
        assert response.jsonPath().getString("[0].id").equals("108")
                : "Expected id 108 but got " + response.jsonPath().getString("[0].id");
        assert response.jsonPath().getString("[0].data.cpu_model").equals("Intel vPRO Enterprise")
                : "Expected CPU model Intel vPRO Enterprise but got "
                        + response.jsonPath().getString("[0].data.CPU model");
        assert response.jsonPath().getString("[1].id").equals("37")
                : "Expected id 37 but got " + response.jsonPath().getString("[1].id");
    }

    @Test(dependsOnMethods = "testLogin")
    public void testSingleObject() {
        /*
         * Define the base URL for the API
         * String baseUrl = "https://whitesmokehouse.com";
         */
        String id = "108";

        RestAssured.baseURI = "https://whitesmokehouse.com";

        // Create login request
        // Send POST request to login endpoint
        Response response = RestAssured.given()
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer " + token)
                .log().all()
                .when()
                .get("/webhook/8749129e-f5f7-4ae6-9b03-93be7252443c/api/objects/" + id);
        // Print the response
        System.out.println("Response: " + response.asPrettyString());

        // Validate the response
        assert response.getStatusCode() == 200 : "Expected status code 200 but got " + response.getStatusCode();
        assert response.jsonPath().getString("name").equals("Lenovo Thinkpad T14s - Drajatp")
                : "Expected name Lenovo Thinkpad T14s - Drajatp but got " + response.jsonPath().getString("name");
        assert response.jsonPath().getString("data.cpu_model").equals("Intel vPRO Enterprise")
                : "Expected CPU model Intel vPRO Enterprise but got " + response.jsonPath().getString("data.CPU model");
    }

    @Test(dependsOnMethods = "testLogin")
    public void testAddObject() {
        /*
         * Define the base URL for the API
         * String baseUrl = "https://whitesmokehouse.com";
         */
        RestAssured.baseURI = "https://whitesmokehouse.com";

        // Create login request
        String requestBody = "{"
                + "\"name\": \"Lenovo Ideapad 7i\","
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
        // Send POST request to login endpoint
        Response response = RestAssured.given()
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer " + token)
                .body(requestBody)
                .log().all()
                .when()
                .post("/webhook/api/objects");
        // Print the response
        System.out.println("Response: " + response.asPrettyString());

        // Validate the response
        assert response.getStatusCode() == 200 : "Expected status code 200 but got " + response.getStatusCode();
        assert response.jsonPath().getString("[0].name").equals("Lenovo Ideapad 7i")
                : "Expected name Lenovo Ideapad 7i but got " + response.jsonPath().getString("name");
        assert response.jsonPath().getString("[0].data.cpu_model").equals("Intel Core i7")
                : "Expected CPU model Intel Core i7 but got " + response.jsonPath().getString("data.cpu_model");
    }

    @Test(dependsOnMethods = "testLogin")
    public void testUpdateObject() {
        /*
         * Define the base URL for the API
         * String baseUrl = "https://whitesmokehouse.com";
         */
        String id = "233";

        RestAssured.baseURI = "https://whitesmokehouse.com";

        // Create login request
        String requestBody = "{"
                + "\"name\": \"Lenovo Ideapad 7i\","
                + "\"data\": {"
                + "  \"year\": 2023,"
                + "  \"price\": 1849.99,"
                + "  \"cpu_model\": \"Intel Core i7\","
                + "  \"hard_disk_size\": \"1 TB\","
                + "  \"capacity\": \"2 cpu\","
                + "  \"screen_size\": \"14 Inch\","
                + "  \"color\": \"blue\""
                + "}"
                + "}";

        // Send POST request to login endpoint
        Response response = RestAssured.given()
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer " + token)
                .body(requestBody)
                .log().all()
                .when()
                .put("/webhook/37777abe-a5ef-4570-a383-c99b5f5f7906/api/objects/" + id);
        // Print the response
        System.out.println("Response: " + response.asPrettyString());

        // Validate the response
        assert response.getStatusCode() == 200 : "Expected status code 200 but got " + response.getStatusCode();
        // assert response.jsonPath().getString("name").equals("Lenovo Ideapad 7i")
        //         : "Expected name Lenovo Ideapad 7i but got " + response.jsonPath().getString("name");
        // assert response.jsonPath().getString("data.cpu_model").equals("Intel Core i7")
        //         : "Expected CPU model Intel Core i7 but got " + response.jsonPath().getString("data.cpu_model");
    }

    @Test(dependsOnMethods = "testLogin")
    public void testDeleteObject() {
        /*
         * Define the base URL for the API
         * String baseUrl = "https://whitesmokehouse.com";
         */
        String id = "208";

        RestAssured.baseURI = "https://whitesmokehouse.com";

        // Send POST request to login endpoint
        Response response = RestAssured.given()
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer " + token)
                .log().all()
                .when()
                .delete("/webhook/d79a30ed-1066-48b6-83f5-556120afc46f/api/objects/" + id);
        // Print the response
        System.out.println("Response: " + response.asPrettyString());

        // Validate the response
        assert response.getStatusCode() == 200 : "Expected status code 200 but got " + response.getStatusCode();
        assert response.jsonPath().getString("status").equals("deleted")
                : "Expected status Object deleted successfully but got " + response.jsonPath().getString("status");
        assert response.jsonPath().getString("message").equals("Object with id = " + id + ", has been deleted.")
                : "Expected message Object deleted successfully but got " + response.jsonPath().getString("message");
    }

    @Test(dependsOnMethods = "testLogin")
    public void testGetAllDepartments() {
        /*
         * Define the base URL for the API
         * String baseUrl = "https://whitesmokehouse.com";
         */
        RestAssured.baseURI = "https://whitesmokehouse.com";

        // Send POST request to login endpoint
        Response response = RestAssured.given()
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer " + token)
                .log().all()
                .when()
                .get("/webhook/api/department");
        // Print the response
        System.out.println("Response: " + response.asPrettyString());

        // Validate the response
        assert response.getStatusCode() == 200 : "Expected status code 200 but got " + response.getStatusCode();
        Assert.assertTrue(response.jsonPath().getString("department").contains("Finance"),
                "Expected department Finance but got " + response.jsonPath().getString("department"));
    }

    @Test(dependsOnMethods = "testLogin")
    public void testPartiallyUpdateObject() {
        /*
         * Define the base URL for the API
         * String baseUrl = "https://whitesmokehouse.com";
         */
        String id = "18";

        RestAssured.baseURI = "https://whitesmokehouse.com";

        // Create login request
        String requestBody = "{"
                + "\"name\": \"Lenovo Ideapad 7i test patch\","
                + "\"year\": 2023"
                + "}";

        // Send POST request to login endpoint
        Response response = RestAssured.given()
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer " + token)
                .body(requestBody)
                .log().all()
                .when()
                .patch("/webhook/39a0f904-b0f2-4428-80a3-391cea5d7d04/api/object/" + id);
        // Print the response
        System.out.println("Response: " + response.asPrettyString());

        // Validate the response
        assert response.getStatusCode() == 200 : "Expected status code 200 but got " + response.getStatusCode();
    }
}
