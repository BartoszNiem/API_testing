package pl.qaaacademy.restassured.shop_api.apis;

import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import pl.qaaacademy.restassured.shop_api.enviroment.Environment;
import pl.qaaacademy.restassured.shop_api.enviroment.EnvironmentManager;
import pl.qaaacademy.restassured.shop_api.models.Product;

import java.util.Arrays;
import java.util.List;


public class BasicProductVerificationWithAPIs {
    private ProductAPI productAPI;

    @BeforeClass(groups = {"productAPITest"})
    public void setUp(){
        String env = "dev";//System.getProperty("env");
        Environment currentEnvironment = EnvironmentManager.getEnvironment(env);
        productAPI = ProductAPI.get(currentEnvironment);
    }

    @Test(groups = {"productAPITest"})
    public void shouldAddNewProduct(){
        Product newProductToAdd = new Product("Honey", 3, 13.42f);
        Product addedProduct = productAPI.addNewProduct(newProductToAdd);
        Assert.assertEquals(addedProduct.getDescription(), newProductToAdd.getDescription());
    }

    @Test(groups = {"productAPITest"})
    public void shouldUpdatePriceOfProduct(){
        String productDescription = "Honey";
        Product productToBeUpdated = productAPI.getProductFromDescription(productDescription);
        float newPrice = 13.99f;
        productToBeUpdated.setPrice(newPrice);
        Product updatedProduct = productAPI.updateProduct(productToBeUpdated);
        Assert.assertEquals(updatedProduct.getPrice(), newPrice);
    }

    @Test(groups = {"productAPITest"})
    public void checkIfProductListContainsStrawberryAndPeach(){
        List<String> listOfProductsDescriptions = Arrays.stream(productAPI.getListOfProducts()).map(Product::getDescription).toList();
        Assert.assertTrue(listOfProductsDescriptions.contains("Strawberry") && listOfProductsDescriptions.contains("Peach"));
    }

    @Test(groups = {"productAPITest"})
    public void checkIfProductHasProperValues(){
        String correctDescription = "Strawberry";
        float correctPrice = 18.3f;
       // Product productWhichValuesWeVerify = productAPI.getProductFromDescription("Honey");
        Product productWhichValuesWeVerify = productAPI.getProductFromId("5");
        Assert.assertEquals(productWhichValuesWeVerify.getDescription(), correctDescription);
        Assert.assertEquals(productWhichValuesWeVerify.getPrice(), correctPrice);
    }

    @Test(groups = {"productAPITest"})
    public void shouldDeleteProductFromID(){
        String productId = productAPI.getProductFromDescription("Honey").getId();
        Assert.assertTrue(productAPI.deleteProductFromID(productId));
    }
}
