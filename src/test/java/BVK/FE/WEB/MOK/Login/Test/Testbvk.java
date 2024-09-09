package BVK.FE.WEB.MOK.Login.Test;

import BVK.FE.WEB.MOK.WebBaseTestNG_MOK;
import io.qameta.allure.Step;
import org.springframework.boot.test.context.SpringBootTest;
import org.testng.annotations.Test;

@SpringBootTest
public class Testbvk extends WebBaseTestNG_MOK {


    @Step("TC_Login_001-Successful Login with Valid Credentials")
    @Test
    public void TestLogin() {
     loginBVKObject.getUrl().getUrl();
    }
    @Step("TC_Login_003-User is on the login page with invalid username and valid password.")
    @Test
    public void TestLoginfailed() {
        loginBVKObject.getUrl1();
    }


}
