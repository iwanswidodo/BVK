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

import static BVK.GlobalMethod.Encryption.Encryption.decryptData;

@Page
public class LoginPageObject extends WebBaseMethod {
    @Autowired

    private WebDriver webDriver;


    @PostConstruct
    public void initDonationPage(){
        PageFactory.initElements(webDriver,this);
    }

    @FindBy(xpath = "//*[@id='input-16']")
    private WebElement txtusername;

    @FindBy(xpath = "//*[@id='input-22']")
    private WebElement txtinput2;

    @FindBy(xpath = "//*[text() = ' Login ']")
    private WebElement btnLogin;

    @Value("${URL-Test}")
    private String getUrl;
    @Value("${emailMOKAdminTest}")
    private String usernameAdmin;
    @Value("${passwordMOKAdminTest}")
    private String passwordAdmin;




    @Step("As a user should be able access URL MOK")
    public LoginPageObject getUrl(){
        webDriver.get(getUrl);
        return this;
    }
    @Step("As a user should be able fill username")
    public LoginPageObject txtusername(){
        sendKeys(txtusername, decryptData(usernameAdmin));
        return this;
    }
    @Step("As a user should be able fill password")
    public LoginPageObject txtpassword(){
        sendKeys(txtusername, decryptData(passwordAdmin));
        return this;
    }
    @Step("As a user should be able click Login Button")
    public LoginPageObject Loginbutton(){
        click(btnLogin);
        return this;
    }

}
