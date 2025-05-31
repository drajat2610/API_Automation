package cucumber.definitions;

import com.apiautomation.model.request.RequestAddObject;
import com.apiautomation.model.request.RequestLogin;
import com.apiautomation.model.response.ResponseAddObject;
import com.apiautomation.model.response.ResponseListObject;
import com.apiautomation.model.response.ResponseSingelObject;
import com.fasterxml.jackson.databind.ObjectMapper;
// import io.cucumber.java.en.*;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.testng.Assert;

import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ObjectDefinitions {

    String token;
    int objectId;
    Response response;
    RequestAddObject requestAddObject;

    @Given("the API is available")
    public void the_api_is_available() {
        RestAssured.baseURI = "https://whitesmokehouse.com";
    }

    @And("I login with email {string} and password {string}")
    public void i_login_with_email_and_password(String email, String password) throws Exception {
        RequestLogin login = new RequestLogin(email, password);
        String json = new ObjectMapper().writeValueAsString(login);

        response = RestAssured.given()
                .header("Content-Type", "application/json")
                .body(json)
                .post("/webhook/api/login");

        Assert.assertEquals(response.getStatusCode(), 200);
        token = response.jsonPath().getString("token");
    }

    @When("I add a new object with the following data:")
    public void i_add_a_new_object_with_the_following_data(io.cucumber.datatable.DataTable dataTable) throws Exception {
        Map<String, String> dataMap = dataTable.asMap();

        requestAddObject = new RequestAddObject(
                dataMap.get("name"),
                new RequestAddObject.Data(
                        Integer.parseInt(dataMap.get("year")),
                        Double.parseDouble(dataMap.get("price")),
                        dataMap.get("cpuModel"),
                        dataMap.get("hardDiskSize"),
                        dataMap.get("color"),
                        dataMap.get("capacity"),
                        dataMap.get("screenSize")
                )
        );

        String json = new ObjectMapper().writeValueAsString(requestAddObject);

        response = RestAssured.given()
                .header("Authorization", "Bearer " + token)
                .header("Content-Type", "application/json")
                .body(json)
                .post("/webhook/api/objects");

        ResponseAddObject[] res = response.as(ResponseAddObject[].class);
        objectId = res[0].id;

        Assert.assertEquals(res[0].name, requestAddObject.name);
    }

    @Then("the response status code should be {int}")
    public void the_response_status_code_should_be(Integer statusCode) {
        Assert.assertEquals((int) response.getStatusCode(), (int) statusCode);
    }

    @Then("the object should be present in the object list")
    public void the_object_should_be_present_in_the_object_list() {
        response = RestAssured.given()
                .header("Authorization", "Bearer " + token)
                .get("/webhook/api/objects");

        ResponseListObject[] resList = response.as(ResponseListObject[].class);
        List<String> names = List.of(resList).stream().map(o -> o.name).collect(Collectors.toList());

        Assert.assertTrue(names.contains(requestAddObject.name));
    }

    @Then("I can retrieve the object details and verify it")
    public void i_can_retrieve_the_object_details_and_verify_it() {
        response = RestAssured.given()
                .header("Authorization", "Bearer " + token)
                .get("/webhook/8749129e-f5f7-4ae6-9b03-93be7252443c/api/objects/" + objectId);

        ResponseSingelObject single = response.as(ResponseSingelObject.class);

        Assert.assertEquals(single.name, requestAddObject.name);
        Assert.assertEquals(single.data.year, String.valueOf(requestAddObject.data.year));
        Assert.assertEquals(single.data.price, String.valueOf(requestAddObject.data.price));
        Assert.assertEquals(single.data.cpuModel, requestAddObject.data.cpuModel);
    }

    @When("I delete the object")
    public void i_delete_the_object() {
        response = RestAssured.given()
                .header("Authorization", "Bearer " + token)
                .delete("/webhook/d79a30ed-1066-48b6-83f5-556120afc46f/api/objects/" + objectId);
    }

    @Then("retrieving the object should return an empty response")
    public void retrieving_the_object_should_return_an_empty_response() {
        response = RestAssured.given()
                .header("Authorization", "Bearer " + token)
                .get("/webhook/8749129e-f5f7-4ae6-9b03-93be7252443c/api/objects/" + objectId);

        Assert.assertEquals(response.getStatusCode(), 200);
        Assert.assertEquals(response.getBody().asString().trim(), "{}");
    }


}
