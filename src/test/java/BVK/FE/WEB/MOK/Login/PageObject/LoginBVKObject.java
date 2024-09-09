package BVK.FE.WEB.MOK.Login.PageObject;

import BVK.FE.WebBaseMethod;
import BVK.GlobalMethod.SpringBoot.Page;
import io.qameta.allure.Step;
import jakarta.annotation.PostConstruct;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.testng.Assert;

import java.io.File;
import java.util.List;
import java.util.NoSuchElementException;

import static com.aspose.barcode.internal.rrf.ww.By;

@Page
public class LoginBVKObject extends WebBaseMethod {
    @Autowired
    private WebDriver webDriver;
    @PostConstruct
    public void initDonationPage(){
        PageFactory.initElements(webDriver,this);
    }

    @FindBy(css = "p")
    private WebElement txtcongrats;
    @FindBy(xpath = "/html[1]/body[1]/div[2]/div[1]/div[1]/h3[1]")
    private WebElement txtupload;

    @FindBy(css = "input#file-upload")
    private WebElement fileupload;
    @FindBy(css = "input[type='submit']")
    private WebElement btnSubmit;

    @FindBy(css = "button.added-manually")
    private WebElement removeElementBtns;

    @FindBy(xpath = "//button[contains(text(),'Add Element')]")
    private WebElement addButton;

    @FindBy(xpath = "//div[@id='elements']/button")
    private WebElement elements;



    @Value("${URL-Test}")
    private String getUrl;
    @Value("${URL-TestInvalidusername}")
    private String getUrl1;
    @Value("${URL-Upload}")
    private String URLupload;
    @Value("${URL-TestInvalidPassword}")
    private String getUrl2;



        @Step("TC_Login_001 - Successful Login with Valid Credentials ")
    public LoginBVKObject getUrl(){
        webDriver.get(getUrl);
        System.out.println(txtcongrats.getText());
        Assert.assertEquals(txtcongrats.getText(),"Congratulations! You must have the proper credentials.");
        return this;
    }
    @Step("TC_Login_001 - Successful Login with invalid Credentials Username ")
    public LoginBVKObject getUrl1(){
        webDriver.get(getUrl1);
 //       Assert.assertEquals(txtcongrats.getText(),"Congratulations! You must have the proper credentials.");
        return this;
    }
    @Step("TC_FileUpload_001 - Upload a File Successfully")
    public LoginBVKObject uploadFile(){
        webDriver.get(getUrl);
        webDriver.get(URLupload);
        try {
            String filePath = new File("src/test/resources/asset/Asset1.png").getAbsolutePath();
            sendKeys(fileupload,filePath);
            click(btnSubmit);
            Assert.assertEquals(txtupload.getText(),"File Uploaded!");
        }catch (NoSuchElementException e){
            System.out.println("Element Not Found");
            Assert.fail("Element should not be present");
        }


        return this;
    }
    @Step("TC_Login_005 - Password Field Masking")
    public LoginBVKObject passwordmasking() throws InterruptedException {
        webDriver.get(getUrl);
        Thread.sleep(4000);
        fullPageScreenshot("passwordmasked.png");
        Thread.sleep(4000);
        return this;
    }
    @Step("TC_Login_004 - Successful Login with invalid Credentials Password ")
    public LoginBVKObject invalidPass() throws InterruptedException {
       webDriver.get(getUrl2);
        return this;
    }






}
