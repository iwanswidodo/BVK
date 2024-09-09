package BVK.FE;

import BVK.GlobalMethod.Logger.Log;
import BVK.GlobalMethod.SpringBoot.BaseTest;
import BVK.GlobalMethod.SpringBoot.PropertiesReader;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.AndroidStartScreenRecordingOptions;
import io.appium.java_client.android.AndroidStopScreenRecordingOptions;
import io.appium.java_client.android.connection.ConnectionStateBuilder;
import io.appium.java_client.android.nativekey.AndroidKey;
import io.appium.java_client.android.nativekey.KeyEvent;
import io.appium.java_client.ios.IOSStartScreenRecordingOptions;
import io.appium.java_client.ios.IOSStopScreenRecordingOptions;
import io.appium.java_client.screenrecording.BaseStartScreenRecordingOptions;
import io.appium.java_client.screenrecording.BaseStopScreenRecordingOptions;
import io.appium.java_client.screenrecording.CanRecordScreen;
import io.qameta.allure.Attachment;
import org.openqa.selenium.*;

import org.openqa.selenium.html5.Location;
import org.openqa.selenium.interactions.Actions;
import org.springframework.beans.factory.annotation.Autowired;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.InputStream;
import java.util.Base64;
import java.util.Optional;

import static BVK.GlobalMethod.AllureReport.AllureAttachments.deviceName;
import static BVK.GlobalMethod.AllureReport.AllureAttachments.fileToBytes;


@BaseTest
public class MobileBaseMethod extends ExplicitWaiting {

    @Autowired
    private PropertiesReader propertiesReader;


    protected static ThreadLocal<String> platform = new ThreadLocal<String>();

    public String getPlatform() {
        return platform.get();
    }

    private Point start;
    private Point end;
    private boolean isDisplayed;


    //clear element
    public void clear(WebElement e) {
        explicitWaitVisibilityOfElement(e, propertiesReader.getMaxPageLoadTime() * 1000);
        e.clear();
    }

    public void hideKeyboard() {
        if (propertiesReader.getDevice().equals("android")) {
            AndroidDriver androidDriver = (AndroidDriver) ExplicitWaiting.androidDriver;
            androidDriver.hideKeyboard();
        }
    }

    //click
    public void click(WebElement element) {
        Log.info("clicking " + element);
        explicitWaitElementToBeClickable(element, propertiesReader.getMaxPageLoadTime() * 1000 / 5);
        //highlight
        if (propertiesReader.isScreenshotElement()) {
            //screenshot element
            try {
                screenshotElementMobile(element);
            } catch (IllegalArgumentException illegalException) {
                //retry without screenshot allure
                Log.warn("Warning: Gagal menyimpan screenshot element ke allure report!");
            }
        }
        element.click();
    }

    public void clickHold(WebElement element, int durationMilliSeconds) {
        Log.info("clicking and holding " + element);
        explicitWaitElementToBeClickable(element, propertiesReader.getMaxPageLoadTime() * 1000 / 5);

        // Highlight the element if necessary
        if (propertiesReader.isScreenshotElement()) {
            try {
                screenshotElementMobile(element);
            } catch (IllegalArgumentException illegalException) {
                Log.warn("Warning: Failed to save screenshot of element to Allure report!");
            }
        }

        // Perform click and hold action
        Actions actions = new Actions(driver);
        actions.clickAndHold(element).pause(durationMilliSeconds).release().perform();
    }
    //click
    public void clickIOS(WebElement element) {
        Log.info("clicking " + element);
        explicitWaitElementToBeClickable(element, propertiesReader.getMaxPageLoadTime() * 1000 / 5);
        //highlight
        if (propertiesReader.isScreenshotElement()) {
            //screenshot element
            try {
                screenshotElementMobile(element);
            } catch (IllegalArgumentException illegalException) {
                //retry without screenshot allure
                Log.warn("Warning: Gagal menyimpan screenshot element ke allure report!");
            }
        }
        element.click();
    }

    public void click(int waitBefore, WebElement element, int waitAfter) {
        wait(waitBefore * 1000);
        click(element);
        wait(waitAfter * 1000);
    }

    public void click(int waitBefore, WebElement element) {
        wait(waitBefore * 1000);
        click(element);
    }

    public void click(WebElement element, int waitAfter) {
        click(element);
        wait(waitAfter * 1000);

    }

    public void tapByCoordinate(int x, int y) {
        Point tapLoc = new Point(x, y);
        MobileW3cActions.doTap(androidDriver, tapLoc, 500);  //with duration 1s

    }

    public void tapByCoordinate(int x, int y, int duration) {
        Point tapLoc = new Point(x, y);
        MobileW3cActions.doTap(androidDriver, tapLoc, duration);  //with duration 1s

    }


    //send key or text
    public void sendKeys(WebElement element, String text) {
        Log.info("typing " + text + " on " + element.toString());
        //wait element to be ready
        explicitWaitVisibilityOfElement(element, propertiesReader.getMaxPageLoadTime() * 1000);
        //highlight
        if (propertiesReader.isScreenshotElement()) {
            //screenshot element
            try {
                screenshotElementMobile(element);
            } catch (IllegalArgumentException illegalException) {
                //retry without allure
                Log.warn("Warning: Gagal menyimpan screenshot element ke allure report!");
            }
        }
        element.clear();
        element.sendKeys(text);
    }

    public void sendKeys(int waitBefore, WebElement element, String text, int waitAfter) {
        wait(waitBefore * 1000);
        sendKeys(element, text);
        wait(waitAfter * 1000);
    }

    public void sendKeys(WebElement element, String text, int waitAfter) {
        sendKeys(element, text);
        wait(waitAfter * 1000);
    }

    public void sendKeys(int waitBefore, WebElement element, String text) {
        wait(waitBefore * 1000);
        sendKeys(element, text);
    }

    public void sendKeysKeyboard(String text) {
        new Actions(androidDriver).sendKeys(text).perform();
    }

    public void sendKeysNum(String numbers) {
        char[] numArr = numbers.toCharArray();
        double ratio = (double) 1565 / 2280;
        int x = 0;
        int y = 0;
        double n = 0; //keyboard height
        for (char numChar : numArr) {
            Dimension dimension = androidDriver.manage().window().getSize();

            switch (numChar) {
                case '1' -> {
                    x = (int) (dimension.width * 0.13);
                    y = (int) (dimension.height * (0.743 + n));
                }
                case '2' -> {
                    x = (int) (dimension.width * 0.38);
                    y = (int) (dimension.height * (0.743 + n));
                }
                case '3' -> {
                    x = (int) (dimension.width * 0.62);
                    y = (int) (dimension.height * (0.743 + n));
                }
                case '4' -> {
                    x = (int) (dimension.width * 0.13);
                    y = (int) (dimension.height * (0.807 + n));
                }
                case '5' -> {
                    x = (int) (dimension.width * 0.38);
                    y = (int) (dimension.height * (0.807 + n));
                }
                case '6' -> {
                    x = (int) (dimension.width * 0.62);
                    y = (int) (dimension.height * (0.807 + n));
                }
                case '7' -> {
                    x = (int) (dimension.width * 0.13);
                    y = (int) (dimension.height * (0.87 + n));
                }
                case '8' -> {
                    x = (int) (dimension.width * 0.38);
                    y = (int) (dimension.height * (0.87 + n));
                }
                case '9' -> {
                    x = (int) (dimension.width * 0.62);
                    y = (int) (dimension.height * (0.87 + n));
                }
                case ',' -> {
                    x = (int) (dimension.width * 0.13);
                    y = (int) (dimension.height * (0.932 + n));
                }
                case '0' -> {
                    x = (int) (dimension.width * 0.38);
                    y = (int) (dimension.height * (0.932 + n));
                }
                case '.' -> {
                    x = (int) (dimension.width * 0.62);
                    y = (int) (dimension.height * (0.932 + n));
                }
                case '<' -> { //delete
                    x = (int) (dimension.width * 0.87);
                    y = (int) (dimension.height * (0.87 + n));
                }

            }
            Point tapLoc = new Point(x, y);
            MobileW3cActions.doTap(androidDriver, tapLoc, 500);  //with duration 1s
            Log.infoGreen("tapping " + numChar + " at x: " + x + ", y: " + y);
            wait(1000);
        }

    }


    //get attribute
    public String getAttribute(WebElement e, String attribute) {
        explicitWaitVisibilityOfElement(e, propertiesReader.getMaxPageLoadTime() * 1000);
        return e.getAttribute(attribute);
    }

    //get text
    public String getText(WebElement e, String msg) {
        String txt = null;
        switch (getPlatform()) {
            case "Android":
                txt = getAttribute(e, "text");
                break;
            case "iOS":
                txt = getAttribute(e, "label");
                break;
        }
        return txt;
    }

    public void openNotif() {
        Dimension dimension = androidDriver.manage().window().getSize();
        start = new Point((int) (dimension.width * 0.2), (int) (dimension.height * 0));
        end = new Point((int) (dimension.width * 0.2), (int) (dimension.height * 0.8));
        MobileW3cActions.doSwipe(androidDriver, start, end, 1000);  //with duration 1s
    }

    public void scrollDown() {
        Dimension dimension = androidDriver.manage().window().getSize();
        start = new Point((int) (dimension.width * 0.5), (int) (dimension.height * 0.8));
        end = new Point((int) (dimension.width * 0.5), (int) (dimension.height * 0.2));
        MobileW3cActions.doSwipe(androidDriver, start, end, 1000);  //with duration 1s

    }


    public void scrollDown(int duration) {
        Dimension dimension = androidDriver.manage().window().getSize();
        start = new Point((int) (dimension.width * 0.5), (int) (dimension.height * 0.8));
        end = new Point((int) (dimension.width * 0.5), (int) (dimension.height * 0.2));
        MobileW3cActions.doSwipe(androidDriver, start, end, duration);  //with duration 1s
    }
    public void scrollDownIOS(int duration) {
        Dimension dimension = iosDriver.manage().window().getSize();
        start = new Point((int) (dimension.width * 0.5), (int) (dimension.height * 0.8));
        end = new Point((int) (dimension.width * 0.5), (int) (dimension.height * 0.2));
        int fastSwipeDuration = 100; // Swipe duration is in milliseconds
        MobileW3cActions.doSwipe(iosDriver, start, end, fastSwipeDuration);
    }

    public void scrollUpOnYear(int duration) {
        Dimension dimension = androidDriver.manage().window().getSize();
        start = new Point((int) (dimension.width * 0.75), (int) (dimension.height * 0.7));
        end = new Point((int) (dimension.width * 0.75), (int) (dimension.height * 0.93));
        MobileW3cActions.doSwipe(androidDriver, start, end, duration);  //with duration 1s
    }

    public void scrollUpOnYeariOS(int duration) {

        Dimension dimension = iosDriver.manage().window().getSize();
        start = new Point((int) (dimension.width * 0.75), (int) (dimension.height * 0.7));
        end = new Point((int) (dimension.width * 0.75), (int) (dimension.height * 0.93));
        MobileW3cActions.doSwipe(iosDriver, start, end, duration);  //with duration 1s
    }

    public void scrollVertical(double startRatio, double endRatio, int duration) {
        Dimension dimension = androidDriver.manage().window().getSize();
        start = new Point((int) (dimension.width * 0.5), (int) (dimension.height * startRatio));
        end = new Point((int) (dimension.width * 0.5), (int) (dimension.height * endRatio));
        MobileW3cActions.doSwipe(androidDriver, start, end, duration);  //with duration 1s

    }


    public void scrollUp() {
        Dimension dimension = androidDriver.manage().window().getSize();
        end = new Point((int) (dimension.width * 0.5), (int) (dimension.height * 0.8));
        start = new Point((int) (dimension.width * 0.5), (int) (dimension.height * 0.2));
        MobileW3cActions.doSwipe(androidDriver, start, end, 1000);  //with duration 1s
    }
    public void scrollUpiOS() {
        Dimension dimension = iosDriver.manage().window().getSize();
        end = new Point((int) (dimension.width * 0.5), (int) (dimension.height * 0.8));
        start = new Point((int) (dimension.width * 0.5), (int) (dimension.height * 0.2));
        MobileW3cActions.doSwipe(iosDriver, start, end, 1000);  //with duration 1s
    }

    public void scrollUpiOS(int duration) {
        Optional.ofNullable(iosDriver).ifPresent(driver -> {
            Dimension dimension = driver.manage().window().getSize();
            end = new Point((int) (dimension.width * 0.5), (int) (dimension.height * 0.8));
            start = new Point((int) (dimension.width * 0.5), (int) (dimension.height * 0.2));
            MobileW3cActions.doSwipe(driver, start, end, duration);
        });
    }
    public void scrollUp(int duration) {
        Dimension dimension = androidDriver.manage().window().getSize();
        end = new Point((int) (dimension.width * 0.5), (int) (dimension.height * 0.8));
        start = new Point((int) (dimension.width * 0.5), (int) (dimension.height * 0.2));
        MobileW3cActions.doSwipe(androidDriver, start, end, duration);  //with duration 1s
    }

    public void scrollRight(double heightRatio, int duration) {
        Dimension dimension = androidDriver.manage().window().getSize();
        start = new Point((int) (dimension.width * 0.8), (int) (dimension.height * heightRatio));
        end = new Point((int) (dimension.width * 0.2), (int) (dimension.height * heightRatio));
        MobileW3cActions.doSwipe(androidDriver, start, end, duration);  //with duration 1s
    }

    public void scrollLeft(double heightRatio, int duration) {
        Dimension dimension = androidDriver.manage().window().getSize();
        start = new Point((int) (dimension.width * 0.2), (int) (dimension.height * heightRatio));
        end = new Point((int) (dimension.width * 0.8), (int) (dimension.height * heightRatio));
        MobileW3cActions.doSwipe(androidDriver, start, end, duration);  //with duration 1s
    }

    public void scrollLeftiOS(double heightRatio, int duration) {
        Dimension dimension = iosDriver.manage().window().getSize();
        start = new Point((int) (dimension.width * 0.2), (int) (dimension.height * heightRatio));
        end = new Point((int) (dimension.width * 0.8), (int) (dimension.height * heightRatio));
        MobileW3cActions.doSwipe(iosDriver, start, end, duration);  //with duration 1s
    }


    public void scrollToElement(WebElement element, String direction) {
        for (int i = 0; i < 10; i++) {
            if (explicitWaitVisibilityOfElement(element, propertiesReader.getMaxPageLoadTime() * 1000)) {
                break;
            } else {
                if (direction.equalsIgnoreCase("up")) {
                    scrollUp();
                } else {
                    scrollDown();
                }
            }
        }
    }

//    public boolean isElementDisplayed(WebElement e) {
//        new WebDriverWait(androidDriver, Duration.ofMillis(500));
//        try {
//            WebDriverWait wait = new WebDriverWait(androidDriver, Duration.ofMillis(500));
//            return wait.until(new ExpectedCondition<Boolean>() {
//                public Boolean apply(WebDriver appiumDriver) {
//                    return e.isDisplayed();
//                }
//            });
//        } catch (Exception ex) {
//            return false;
//        }
//    }

    @Attachment(value = "Attachment of Element {0}", type = "image/png")
    public static byte[] screenshotElementMobile(WebElement element) {
        String srcFile = element.getScreenshotAs(OutputType.BASE64).replaceAll("\n", "");
        return Base64.getDecoder().decode(srcFile);
    }

    public static void screenshotElementMobile(WebElement element, String location) {
        try {
            byte[] bytes = screenshotElementMobile(element);
            InputStream is = new ByteArrayInputStream(bytes);
            BufferedImage actualImage = ImageIO.read(is);
            ImageIO.write(actualImage, "PNG", new File(location));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Attachment(value = "Attachment of Page Screenshot", type = "image/png")
    public byte[] screenshotPage() throws Exception {
        File srcFile = androidDriver.getScreenshotAs(OutputType.FILE);
        return fileToBytes(srcFile.getPath());
    }

    public static void startRecordingMobile() {
        BaseStartScreenRecordingOptions options;
        if (deviceName.toLowerCase().equals("android")) {
            options = new AndroidStartScreenRecordingOptions();
        } else {
            options = new IOSStartScreenRecordingOptions();
        }
        try {
            ((CanRecordScreen) staticDriver).startRecordingScreen(options);
        } catch (Exception e) {
            Log.warn("Could not start recording screen on mobile platform: " + e.getMessage());
        }
        Log.info("Mobile recording started");
    }

    @Attachment(value = "Attachment of Mobile Video", type = "video/mp4", fileExtension = ".mp4")
    public static byte[] stopRecordingMobile(boolean enable) {
        BaseStopScreenRecordingOptions options;
        byte[] recording;
        if (deviceName.toLowerCase().equals("android")) {
            options = new AndroidStopScreenRecordingOptions();
        } else {
            options = new IOSStopScreenRecordingOptions();
        }
        try {
            if (enable)
                return Base64.getDecoder().decode(((CanRecordScreen) staticDriver).stopRecordingScreen(options));
        } catch (Exception e) {
            Log.warn("Could not stop recording screen on mobile platform: " + e.getMessage());
        }
        return null;
    }

    protected void takePhoto() {
        androidDriver.pressKey(new KeyEvent(AndroidKey.CAMERA));
        androidDriver.pressKey(new KeyEvent(AndroidKey.COMMA));
        androidDriver.pressKey(new KeyEvent(AndroidKey.TAB));
        androidDriver.pressKey(new KeyEvent(AndroidKey.SPACE));
    }

    protected void airplaneMode(boolean isOn) {
        if (isOn) {
            androidDriver.setConnection(new ConnectionStateBuilder().withWiFiDisabled().withDataDisabled().build());
        } else {
            androidDriver.setConnection(new ConnectionStateBuilder().withWiFiEnabled().withDataEnabled().build());
        }
    }

    protected void setLocation(double latitude, double longitude, double altitude) {
        androidDriver.setLocation(new Location(latitude, longitude, altitude));
    }

    public void navigateBack() {
        androidDriver.navigate().back();
    }


}
