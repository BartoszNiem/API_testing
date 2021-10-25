package pl.qaaacademy.restassured.shop_api;

import org.testng.Assert;
import org.testng.annotations.Test;
import pl.qaaacademy.restassured.shop_api.models.Customer;
import pl.qaaacademy.restassured.shop_api.models.OrderItem;
import pl.qaaacademy.restassured.shop_api.models.Product;

import java.util.Arrays;

import static io.restassured.RestAssured.given;
import static io.restassured.RestAssured.when;

public class CustomComplexTest {
    private String basePath = "http://localhost:3000/customers";
    private String SEPARATOR = "/";
    private String addItemToCartPath = "http://localhost:3000/customers/{customerId}/cart" +
            "?quantity={quantity}&productId={productId}";


    @Test
    public void customerShoppingCartItemListShouldHave0Length(){

        Customer customer = when().get(basePath + SEPARATOR+ "3").then().extract().as(Customer.class);
        Assert.assertEquals(customer.getShoppingCart().getItems().length, 0);
    }

    @Test
    public void addProductsToShoppingCart(){
        int quantityOfProductToAdd = 5;
        int productIdToAddToCart = 2;
        /*
        String params = String.format("cart?quantity=%d&productId=%d", quantityOfProductToAdd, productIdToAddToCart);
        when().put(basePath + SEPARATOR + "3" + SEPARATOR + params)
                .then().log().all().statusCode(200);

         */
        String customerId = "3";
        emptyCartForCustomer(customerId);
        putProductToCartForCustomerPathParams(customerId, productIdToAddToCart, quantityOfProductToAdd);
        Customer customer = when().get(basePath + SEPARATOR+ customerId).then().extract().as(Customer.class);

        String productDescription = Arrays.stream(customer.getShoppingCart().getItems())
                        .map(OrderItem::getProduct)
                        .map(Product::getDescription)
                        .findAny().get();

        String expectedProductDescription = "Banana";
        Assert.assertEquals(productDescription, expectedProductDescription);
        Assert.assertEquals(customer.getShoppingCart().getItems().length, 1);

    }

    private void emptyCartForCustomer(String customerId) {
        when().delete(basePath + SEPARATOR + customerId + SEPARATOR + "cart/empty")
                .then().statusCode(200);
    }

    private void putProductToCartForCustomer(String customerId, int productId, int productQuantity){
        String query = basePath + SEPARATOR + customerId + SEPARATOR + "cart";
        given().queryParam("quantity", productQuantity)
                .queryParam("productId", productId)
                .when()
                .put(query)
                .then().log().all();
    }
    private void putProductToCartForCustomerPathParams(String customerId, int productId, int productQuantity){
       given().pathParams("customerId", customerId)
               .pathParams("quantity", productQuantity)
               .pathParams("productId", productId)
               .when().put(addItemToCartPath).then().log().all();
    }
    private void deleteProductFromCartForCustomer(String customerId, int productId, int productQuantity){
        String query = basePath + SEPARATOR + customerId + SEPARATOR + "cart";
        given().queryParam("quantity", productQuantity)
                .queryParam("productId", productId)
                .when()
                .put(query)
                .then().log().all();
    }

}
