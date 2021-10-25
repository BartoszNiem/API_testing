package pl.qaaacademy.restassured.shop_api.apis;

import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import pl.qaaacademy.restassured.shop_api.enviroment.Environment;
import pl.qaaacademy.restassured.shop_api.enviroment.EnvironmentManager;
import pl.qaaacademy.restassured.shop_api.models.Customer;

import java.util.List;

public class BasicCustomerVerificationWithAPIs {

    private  CustomerAPI customerAPI;

    @BeforeClass
    public void setUp(){
        String env = "dev";//System.getProperty("env");
        Environment currentEnvironment = EnvironmentManager.getEnvironment(env);
        customerAPI = CustomerAPI.get(currentEnvironment);
    }

    @Test
    public void shouldGetListOfExistingCustomers(){
        List<Customer> customers = customerAPI.getAllCustomers();
        Assert.assertTrue(customers.size() > 0,
                "Customer list size is 0. Check logs for additional details");
    }

    @Test
    public void shouldChangeCustomerEmailWithNewOne(){
        String customerId = "3";
        String newEmail = "alex.kowalsky2@changed.yes";

        Customer alex2 = customerAPI.updateEmailAddressForCustomer(customerId, newEmail);

        Assert.assertEquals(alex2.getPerson().getEmail(), newEmail
                , "Alex's email not updated. Check logs for additional details");
    }

    @Test
    public void shouldAddItemToShoppingCart(){
        String customerId = "4";
        String productId = "3";
        String quantityOfProduct = "4";

        customerAPI.addItemToCartForCustomer(customerId, productId, quantityOfProduct);
        Customer customer = customerAPI.getCustomerFromId(customerId);
        Assert.assertEquals(customer.getShoppingCart().getItems().length, 1);
    }
}
