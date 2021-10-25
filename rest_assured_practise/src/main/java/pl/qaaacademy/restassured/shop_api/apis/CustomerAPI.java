package pl.qaaacademy.restassured.shop_api.apis;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import pl.qaaacademy.restassured.shop_api.enviroment.Environment;
import pl.qaaacademy.restassured.shop_api.models.Customer;

import javax.print.attribute.standard.RequestingUserName;
import java.util.List;

import static io.restassured.RestAssured.given;
import static io.restassured.RestAssured.when;

public class CustomerAPI {
    private final String HOST;
    private final Environment env;
    private RequestSpecification reqSpec;


    private final String CUSTOMERS = "/customers";
    private final String CHANGE_EMAIL_ENDPOINT = "/email";
    private final String ADD_ITEM_TO_CART_ENDPOINT = "/cart";
    private final String SEPARATOR = "/";

    public CustomerAPI(Environment env) {
        this.env = env;
        this.HOST = env.getHost();
        requestSetUp();
    }

    public static CustomerAPI get(Environment env) {
        return new CustomerAPI(env);
    }

    public List<Customer> getAllCustomers() {
        String query = HOST + CUSTOMERS;
        return when().get(query)
                .then().extract().body().jsonPath().getList("", Customer.class);
    }

    private void requestSetUp(){
        RequestSpecBuilder builder = new RequestSpecBuilder();
        builder.setAccept(ContentType.JSON);
        builder.addCookie("cookie1", "key1=value1");
        builder.addHeader("usefulHeasder", "veryUsefulValue");
        builder.build();
    }

    public Customer updateEmailAddressForCustomer(String customerId, String newEmail) {
        String addressString = HOST + CUSTOMERS + SEPARATOR + customerId + CHANGE_EMAIL_ENDPOINT;
         Customer alex =  given()
                .queryParam("email", newEmail)
                .when().patch(addressString)
                .then().extract().body().as(Customer.class);
         return alex;
    }

    public void addItemToCartForCustomer(String customerId, String productId, String quantityOfProduct) {
        String addressString = HOST + CUSTOMERS + SEPARATOR + customerId + ADD_ITEM_TO_CART_ENDPOINT;
        given().queryParam("quantity", quantityOfProduct)
                .queryParam("productId", productId)
                .when().put(addressString)
                .then().log().all();
    }

    public Customer getCustomerFromId(String customerId) {
        String addressString = HOST + CUSTOMERS + SEPARATOR + customerId;
        Customer customerToReturn =
                when().get(addressString)
                .then().extract().body().as(Customer.class);
        return customerToReturn;
    }
}
