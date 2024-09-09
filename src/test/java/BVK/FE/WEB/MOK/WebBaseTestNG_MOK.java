package BVK.FE.WEB.MOK;

import BVK.FE.WEB.MOK.Login.PageObject.LoginBVKObject;
import BVK.GlobalMethod.Listener.BVKListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Listeners;

import static BVK.GlobalMethod.Utils.EnvironmentSetup.environmentSetup;

@SpringBootTest
@Listeners(BVKListener.class)
public class WebBaseTestNG_MOK extends AbstractTestNGSpringContextTests {

//    @Autowired
//    protected LoginMOKPageObject loginMOKPageObject;
    @Autowired
    protected LoginBVKObject loginBVKObject;

    @BeforeMethod
    public void beforeTest(){
        environmentSetup();
    }
    @BeforeSuite
    public void beforeSuite() throws InterruptedException {
//        paramAsProperties(getParam("qa-automation-development-bifrost"), "development-bifrost");
    }
}
