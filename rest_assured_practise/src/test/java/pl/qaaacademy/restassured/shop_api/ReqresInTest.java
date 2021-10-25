package pl.qaaacademy.restassured.shop_api;

import org.testng.annotations.Test;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.hasItems;

public class ReqresInTest {
    @Test
    public void shouldGetUserIds(){
        when().get("https://reqres.in/api/users?page=2")
                .then().log().all()
                .body("data.id", hasItems(7,8));
    }

}
