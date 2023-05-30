package api.test;

import api.endpoints.UserEndPoints;
import api.payload.User;
import com.github.javafaker.Faker;
import io.restassured.response.Response;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.*;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import java.io.IOException;

public class UserTests {
    Faker faker;
    User userPaylod;
    public Logger logger;

    @BeforeClass
    public void setUp() throws IOException {   //setupData
        faker = new Faker();
        userPaylod = new User();

        userPaylod.setId(faker.idNumber().hashCode());
        userPaylod.setUsername(faker.name().username());
        userPaylod.setFirstName(faker.name().firstName());
        userPaylod.setLastName(faker.name().lastName());
        userPaylod.setEmail(faker.internet().safeEmailAddress());
        userPaylod.setPassword(faker.internet().password(5,10));
        userPaylod.setPhone(faker.phoneNumber().cellPhone());

        logger= LogManager.getLogger(this.getClass());
    }

    @Test(priority=1)
    public void testPostUser(){
        logger.info("*** Send post request ***");
        Response response= UserEndPoints.createUser(userPaylod);
        response.then().log().all();
        Assert.assertEquals(response.getStatusCode(),200);
        logger.info("-= Send post request successfull =-");
    }

    @Test(priority=2)
    public void testGetUserByName(){
        logger.info("*** Send Get request by Name ***");
        Response response=UserEndPoints.readUser(this.userPaylod.getUsername());
        response.then().log().all();
        Assert.assertEquals(response.getStatusCode(),200);
        logger.info("-= Send Get request successfull =-");
    }

    @Test(priority = 3)
    public void testUpdateUserByName(){
        //Update data using payload
        logger.info("*** Send Put request by Name ***");
        userPaylod.setFirstName(faker.name().firstName());
        userPaylod.setLastName(faker.name().lastName());
        userPaylod.setEmail(faker.internet().safeEmailAddress());

        Response response=UserEndPoints.updateUser(this.userPaylod.getUsername(),userPaylod);
        response.then().log().body();

        Assert.assertEquals(response.getStatusCode(),200);

        // Checking data after update
        Response responseAfterupdate=UserEndPoints.readUser(this.userPaylod.getUsername());
        Assert.assertEquals(response.getStatusCode(),200);
        logger.info("-= Send Put request successfull =-");
    }
    @Test(priority=4)
    public void testDeleteUserByName(){
        logger.info("*** Send Delete request by Name ***");
        Response response=UserEndPoints.deleteUser(this.userPaylod.getUsername());
        Assert.assertEquals(response.getStatusCode(),200);
        logger.info("-= Send Delete request successfull =-");
    }

}
