package BVK.GlobalMethod.Assertion;

import BVK.FE.ExplicitWaiting;
import BVK.GlobalMethod.Logger.Log;

import io.qameta.allure.Attachment;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.*;
import org.testng.Assert;
import ru.yandex.qatools.ashot.comparison.ImageDiff;
import ru.yandex.qatools.ashot.comparison.ImageDiffer;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.GregorianCalendar;

import static BVK.FE.MobileBaseMethod.screenshotElementMobile;
import static BVK.FE.WebBaseMethod.screenshotElement;
import static BVK.GlobalMethod.Integration.GoogleAPI.GDrive.downloadGdrive;
import static BVK.GlobalMethod.Utils.GateMath.roundDown4DP;
import static BVK.GlobalMethod.Utils.GateMath.roundHalfUp4DP;
import static org.testng.AssertJUnit.assertFalse;

/**
 * All the validation methods and method to take screenshot
 * are defined in this class.
 */
public class Assertions extends ExplicitWaiting {
    public static boolean testCaseStatus = true;
    private File file;
    private String testScreenshotDir;
    private static boolean testStatus;

    public Assertions(WebDriver driver) {
        file = new File("");
        testScreenshotDir = file.getAbsoluteFile()
                + "//src//test//java//outputFiles//";
    }

    /**
     * method to take screenshot
     *
     * @return path where screenshot has been saved
     */
    public String screenShot() {
        String screenshotPath = "screenshot" + new SimpleDateFormat("MM-dd-yyyy-HH-mm-ss")
                .format(new GregorianCalendar().getTime())
                + ".png";

        System.out.println(screenshotPath);
        File scrFile = ((TakesScreenshot) driver)
                .getScreenshotAs(OutputType.FILE);
        try {
            FileUtils.copyFile(scrFile, new File(testScreenshotDir + screenshotPath));
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            screenshotPath = "";
        }
        return screenshotPath;
    }

    /**
     * overloaded method to take screenshot with desired screenshot name passed
     *
     * @param message string passed to save as name of a screenshot
     */
    public void screenShot(String message) {
        String screenshotPath = message + "screenshot" + new SimpleDateFormat("MM-dd-yyyy-HH-mm-ss")
                .format(new GregorianCalendar().getTime()) +
                ".png";

        System.out.println(screenshotPath);
        File scrFile = ((TakesScreenshot) driver)
                .getScreenshotAs(OutputType.FILE);
        try {
            FileUtils.copyFile(scrFile, new File(testScreenshotDir + screenshotPath));
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            screenshotPath = "";
        }
    }


    /**
     * method to verify the actual value with expected value
     *
     * @param actual   actual text displayed
     * @param expected expected text to be displayed
     * @param message  message should be displayed on failure of assertion
     */
    public static boolean verifyEquals(Object actual, Object expected, String message, boolean screenshootOnPassed, boolean screenshotOnFailure,
                                       boolean exitOnFailure, WebElement element) {
        testStatus = true;
        try {
            Assert.assertEquals(actual, expected);
            Log.info("PASSED");
            if (screenshootOnPassed) {
                screenshotElement(element);
            }

        } catch (AssertionError e) {
            testStatus = false;
            Log.info("FAIL - " + message + " Actual: " + actual.toString() + " Expected: " + expected.toString());
            if (screenshotOnFailure) {
                screenshotElement(element);
            }
            if (exitOnFailure) {
                Log.info("Exiting this function as exitOnFail flag is set to True. Will move to next test.");
                throw e;
            }
        }
        return testStatus;
    }

    // Assert Equals
    public static void verifyWebElementText(WebElement actual, String expected) {
        try {
            Assert.assertEquals(actual.getText(), expected);
            Log.info("PASSED");
        } catch (AssertionError e) {
            Log.info("FAILED");
        }
    }

    public static void verifyWebElementIsDisplayed(WebElement e) {
        Assert.assertTrue(e.isDisplayed(), "Element is not displayed.");
        Log.infoGreen("suitable with Expected");
    }

    public static void verifyWebElementIsClickable(WebElement element) {
        Assert.assertTrue(element.isEnabled(), "Element is not clickable.");
    }

    // Assert FALSE
    public static void verifyWebElementIsNotDisplayed(WebElement element) {
        Assert.assertFalse(element.isDisplayed(), "Element is unexpectedly displayed.");
    }

    public static void verifyWebElementIsNotClickable(WebElement element) {
        Assert.assertFalse(element.isEnabled(), "Element is unexpectedly clickable.");
    }


    /**
     * method to verify if the condition is true
     *
     * @param condition           statement to verify
     * @param message             message should be displayed on failure of assertion
     * @param screenshotOnFailure true if screenshot has to be taken in case of failure
     * @param exitOnFailure       true if execution to be stopped in case of failure
     * @return true if assertion passes, false if fails
     */
    public static boolean verifyTrue(boolean condition,
                                     String message,
                                     boolean screenshotOnFailure,
                                     boolean screenshootOnPassed,
                                     boolean exitOnFailure,
                                     WebDriver driver,
                                     WebElement element) {

        try {
            Assert.assertTrue(condition, message);
            Log.info("PASS - " + message);
            if (screenshootOnPassed) {
                screenshotElement(element);
            }
        } catch (AssertionError e) {
            testCaseStatus = false;
            Log.info("FAIL - " + message + " Actual: FALSE Expected: TRUE.");
            if (screenshotOnFailure) {
                screenshotElement(element);
            } else {
                Log.info("FAIL - " + message);
            }
            if (exitOnFailure) {
                Log.info("Exiting this function as exitOnFail flag is set to True.");
                throw e;
            }
        }
        return testCaseStatus;
    }


//    public static void assertUrlEquals(String urlExpect) throws InterruptedException {
//        Thread.sleep(2000);
//        Assert.assertEquals(driver.getCurrentUrl(), urlExpect);
//        Log.info("================ Assert URL ================");
//        Log.info("Actual URL: " + driver.getCurrentUrl());
//        Log.info("Expected URL: " + urlExpect);
//        Log.info("Result: PASS");
//        Log.info("============================================");
//    }

    public static void assertNumberDown(String numExpect, String numActual) throws InterruptedException {
        Assert.assertEquals(numActual, roundDown4DP(numExpect));
        Log.info("================ Assert number ================");
        Log.info("Actual number: " + numActual);
        Log.info("Expected number: " + roundDown4DP(numExpect));
        Log.info("Result: PASS");
        Log.info("===============================================");
    }

    public static void assertNumberHalfUp(String numExpect, String numActual) throws InterruptedException {
        Assert.assertEquals(numActual, roundHalfUp4DP(numExpect));
        Log.info("================ Assert number ================");
        Log.info("Actual number: " + numActual);
        Log.info("Expected number: " + roundHalfUp4DP(numExpect));
        Log.info("Result: PASS");
        Log.info("===============================================");
    }

    public static void assertEquals(Object Actual, Object Expected) {
        Assert.assertEquals(Actual, Expected);
        Log.info("================ Assert equals ================");
        Log.info("Actual : " + Actual);
        Log.info("Expected : " + Expected);
        Log.info("Result: PASS");
        Log.info("===============================================");
    }

    public static void assertTrue(Boolean conditionTrue) {
        Assert.assertTrue(conditionTrue);
        Log.info("================ Assert true ================");
        Log.info("Condition True: " + conditionTrue);
        Log.info("Result: PASS");
        Log.info("===============================================");
    }

    public static void assertElementTextEquals(WebElement element, String expectedText) {
        explicitWaitVisibilityOfElement(element,5000);
        Assert.assertEquals(element.getText(),expectedText);
        Log.info("================ Assert element text ================");
        Log.info("Actual : " + element.getText());
        Log.info("Expected : " + expectedText);
        Log.info("Result: PASS");
        Log.info("===============================================");
    }



    public static void assertElementIsVisible(WebElement element){
        explicitWaitVisibilityOfElement(element,5000);
        assertTrue(element.isDisplayed());
        Log.info("================ Assert element is displayed ================");
        Log.info("Condition True: " + element.isDisplayed());
        Log.info("Result: PASS");
        Log.info("===============================================");
    }

    private static ImageDiff imageDiff;

    public static void compareVisualMobile(String expectedImageFileName, WebElement element) {
        //throw error when error
        checkImageDifferenceMobile(expectedImageFileName, element);
        assertFalse("Visual differences found!", imageDiff.hasDiff());
    }

    @Attachment(value = "Visual Test Result of Element {0}", type = "image/png")
    public static byte[] checkImageDifferenceMobile(String expectedImageFileName, WebElement element) {
        byte[] image = null;
        try {
            //actual
            byte[] bytes = screenshotElementMobile(element);
            InputStream is = new ByteArrayInputStream(bytes);
            BufferedImage actualImage = ImageIO.read(is);
            BufferedImage expectedImage = ImageIO.read(new ByteArrayInputStream(downloadGdrive(expectedImageFileName).toByteArray()));
            //compare
            ImageDiffer imageDiffer = new ImageDiffer();
            imageDiff = imageDiffer.makeDiff(expectedImage, actualImage);
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            ImageIO.write(imageDiff.getMarkedImage(), "png", bos);
            image = bos.toByteArray();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return image;
    }

    public static void getComparisonImageMobile(String fileLocation, WebElement element) {
        try {
            byte[] bytes = screenshotElementMobile(element);
            InputStream is = new ByteArrayInputStream(bytes);
            BufferedImage actualImage = ImageIO.read(is);
            ImageIO.write(actualImage, "png", new File(fileLocation));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void getComparisonImage(String fileLocation, WebElement element) {
        try {
            byte[] bytes = screenshotElement(element);
            InputStream is = new ByteArrayInputStream(bytes);
            BufferedImage actualImage = ImageIO.read(is);
            ImageIO.write(actualImage, "png", new File(fileLocation));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }




}


