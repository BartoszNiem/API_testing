package pl.qaaacademy.restassured.shop_api;

import io.restassured.http.ContentType;
import io.restassured.parsing.Parser;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.Test;
import pl.qaaacademy.restassured.shop_api.models.Product;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

public class BasicProductAPIVerificationTest {
    private final String  BASE_PATH = "http://localhost:3000/products";
    private final String SEPARATOR = "/";
    @Test
    public void shouldAddNewProduct(){
        given().contentType(ContentType.JSON)
                .body("{\n" +
                "    \"description\": \"Rosted Coffe\",\n" +
                "    \"manufacturer\": 2,\n" +
                "    \"price\": 13.99\n" +
                "}")
        .when().post(BASE_PATH)
        .then().log().all()
                .statusLine(containsString("OK"));
    }

    @Test
    public void shouldUpdateProductPriceForProductWithIDEqualTo2(){
        given().contentType(ContentType.JSON)
                .body("{\n" +
                        "        \"description\": \"Banana\",\n" +
                        "        \"id\": \"2\",\n" +
                        "        \"manufacturer\": 1,\n" +
                        "        \"price\": 3.9\n" +
                        "    }")
                .when()
                .put(BASE_PATH + "/1")
                .then().log().all().statusLine(containsString("OK"));
    }

    @Test
    public void shouldUpdateProductWithTheNewPrice(){
        Float newPrice = 7.9f;
        HashMap<String, Object> productData = new HashMap<>();
        productData.put("description", "Banana");
        productData.put("id", "2");
        productData.put("manufacturer", 1);
        productData.put("price", newPrice);
        given().contentType(ContentType.JSON)
                .body(productData)
                .when()
                .put(BASE_PATH + "/1")
                .then()
                .log().all()
                .body("price", equalTo(newPrice));

    }
    @Test
    public void listOfProductsShouldContainPeachAndStrawberry(){
        Response responseFromGetProductsListRequest = doGetRequest(BASE_PATH);

        List<String> listOfProductsDescriptions = responseFromGetProductsListRequest.jsonPath().getList("description");

        Assert.assertTrue(listOfProductsDescriptions.contains("Strawberry")
                && listOfProductsDescriptions.contains("Peach"));
    }
    @Test
    public void verifyStrawberryPrice(){
        float actualPrice = 18.3f;
        Product[] products = getProducts();
        float price = Arrays.stream(products)
                .filter(product -> product.getDescription().equals("Strawberry"))
                .limit(1).map(product -> product.getPrice())
                .collect(Collectors.toList()).get(0);
        Assert.assertEquals(price, actualPrice);

    }

    private Product[] getProducts() {
        Product[] products = when().get(BASE_PATH).then()
                .statusCode(200)
                .log()
                .all()
                .extract()
                .as(Product[].class);
        return products;
    }

    @Test
    public void shouldDeleteAProductFromTheList(){
        //creating product to delete
        Product newProduct = new Product("Roasted Coffee", 5, 13.77f);
        given().contentType(ContentType.JSON)
                .body(newProduct)
                .when().post(BASE_PATH)
                .then().log().all()
                .statusLine(containsString("OK"));

        Product[] listOfProducts = getProducts();
        String idOfNewProduct = Arrays.stream(listOfProducts)
                .filter(product -> product.getDescription().equals(newProduct.getDescription()))
                .map(Product::getId).collect(Collectors.toList()).get(0);

        when().delete(BASE_PATH + "/" + idOfNewProduct).then()
                .log().all()
                .statusCode(200);

    }
    @Test
    public void extractedProductShouldHaveExpectedDescription() {
        //verify that product with id=7 is Orange and price is 10.5
        String productID = "7";
        String expectedDescription = "Orange";
        float expectedPrice = 10.5f;
        Product product = given()
                .when().get(BASE_PATH + SEPARATOR + productID)
                .then().extract().as(Product.class);

        Assert.assertEquals(expectedDescription, product.getDescription());
        Assert.assertEquals(expectedPrice, product.getPrice());

    }
    public static Response doGetRequest(String endpoint){
        defaultParser = Parser.JSON;
        return  when().get(endpoint)
                .then()
                .statusCode(200)
                .contentType(ContentType.JSON)
                .log()
                .all()
                .extract()
                .response();
    }

}
