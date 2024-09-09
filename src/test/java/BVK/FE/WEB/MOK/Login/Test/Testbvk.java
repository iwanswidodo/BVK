package BVK.FE.WEB.MOK.Login.Test;

import BVK.FE.WEB.MOK.WebBaseTestNG;
import io.qameta.allure.Step;
import org.springframework.boot.test.context.SpringBootTest;
import org.testng.annotations.Test;

@SpringBootTest
public class Testbvk extends WebBaseTestNG {


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
    @Step("TC_FileUpload_001-Upload a File Successfully ")
    @Test
    public void UploadFile() {
        loginBVKObject.uploadFile();
    }
    @Step("TC_Login_005 - Password Field Masking")
    @Test
    public void PasswordMasking() throws InterruptedException {
        loginBVKObject.passwordmasking();
    }



}
