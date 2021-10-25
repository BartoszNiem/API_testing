package pl.qaaacademy.restassured.shop_api;

import io.restassured.http.ContentType;
import io.restassured.http.Header;
import io.restassured.response.Response;
import org.testng.annotations.Test;

import java.net.URI;

import static io.restassured.RestAssured.given;
import static io.restassured.RestAssured.when;
import static org.hamcrest.core.IsEqual.equalTo;

public class BasicCustomerAPIVerificationTest {

    private String basePath = "http://localhost:3000/customers";

    @Test
    public void should200WhenFethingCustomersList(){
        Header h1 = new Header("h1", "v1");
        given().header(h1).log().headers()
                .when().get(basePath)
                .then()
                .statusCode(200)
                .contentType(ContentType.JSON);
    }

    @Test
    public void shouldReturnCustomerInfoForIDEquals2(){
        Response resp = given().cookie("cookie", "key1/value,key2/value").log().cookies().
        when().get("http://localhost:3000/customers/2")
                .then()
              //  .statusCode(200)
                //.body("person.email", equalTo("john.doe@customDomain.com"))
                //.body("address.phone", equalTo("33 55 789 123"))
                .extract()
                .response();
        System.out.println(resp.getBody().prettyPrint());
        System.out.println(resp.getStatusCode());
        System.out.println(resp.getStatusLine());
        System.out.println(resp.getCookies());
        System.out.println(resp.getHeaders().asList());
    }

    @Test
    public void verifyAddressCityForCustomerWithGivenID(){
        String query = "find {it.id == '3'}.address.city";
        String expectedCity = "Auckland";
        when().get(basePath)
                .then()
                .log().ifValidationFails()
                .body(query, equalTo(expectedCity));
    }
}
