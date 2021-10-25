package pl.qaaacademy.restassured.shop_api.apis;

import io.restassured.http.ContentType;
import pl.qaaacademy.restassured.shop_api.enviroment.Environment;
import pl.qaaacademy.restassured.shop_api.models.Product;

import java.util.Arrays;
import java.util.stream.Collectors;

import static io.restassured.RestAssured.given;
import static io.restassured.RestAssured.when;

public class ProductAPI {
    private final String HOST;
    private final Environment env;

    private final String PRODUCTS = "/products";
    private final String SEPARATOR = "/";


    public ProductAPI(Environment env) {
        this.env = env;
        this.HOST = env.getHost();
    }

    public static ProductAPI get(Environment currentEnvironment) {
        return new ProductAPI(currentEnvironment);
    }


    public Product addNewProduct(Product newProductToAdd) {
        String addressString = HOST + PRODUCTS;
        return given().contentType(ContentType.JSON)
                .body(newProductToAdd)
                .when().post(addressString)
                .then().extract().body()
                .as(Product.class);
    }

    public Product getProductFromDescription(String productDescription) {
        Product[] listOfProducts = getListOfProducts();
        return Arrays.stream(listOfProducts)
                .filter(product -> product.getDescription().equals(productDescription))
                .limit(1)
                .collect(Collectors.toList())
                .get(0);
    }

    public Product[] getListOfProducts() {
        String addressString = HOST + PRODUCTS;
        return when().get(addressString)
                .then().extract().body().as(Product[].class);
    }

    public Product updateProduct(Product productToBeUpdated) {
        String addressString = HOST + PRODUCTS + SEPARATOR + "1";
        return given().contentType(ContentType.JSON).body(productToBeUpdated)
                .when().put(addressString)
                .then().extract().body().as(Product.class);
    }

    public Product getProductFromId(String productId) {
        return when().get(HOST + PRODUCTS + SEPARATOR + productId)
                .then().extract().body().as(Product.class);
    }

    public boolean deleteProductFromID(String productId) {
        String addressString = HOST + PRODUCTS + SEPARATOR + productId;
        return when().delete(addressString)
                .then().extract().as(Boolean.class);
    }
}
