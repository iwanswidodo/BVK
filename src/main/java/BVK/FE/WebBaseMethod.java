package BVK.FE;

import BVK.GlobalMethod.Logger.Log;
import BVK.GlobalMethod.SpringBoot.BaseTest;
import BVK.GlobalMethod.Utils.RandomGenerator;
import com.google.api.services.sheets.v4.model.ValueRange;
import io.qameta.allure.Attachment;
import org.openqa.selenium.*;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Action;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.beans.factory.annotation.Value;
import org.openqa.selenium.interactions.Actions;
import org.testng.Assert;
import ru.yandex.qatools.ashot.AShot;
import ru.yandex.qatools.ashot.Screenshot;
import ru.yandex.qatools.ashot.coordinates.WebDriverCoordsProvider;
import ru.yandex.qatools.ashot.cropper.indent.IndentCropper;
import ru.yandex.qatools.ashot.shooting.ShootingStrategies;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.*;

import static BVK.GlobalMethod.Encryption.Encryption.decryptData;
import static BVK.GlobalMethod.Encryption.Encryption.encryptData;
import static BVK.GlobalMethod.ErrorHandling.ErrorHandling.elementErrorHandling;
import static BVK.GlobalMethod.Integration.GoogleAPI.GSheet.*;
import static BVK.GlobalMethod.Utils.ExcelUtils.*;
import static BVK.GlobalMethod.Utils.RandomGenerator.*;
import static BVK.GlobalMethod.Utils.ReadAndWriteFile.*;

/*
* collection our method in here
*
*
*
*
*
 */

@BaseTest
public class WebBaseMethod extends ExplicitWaiting {


    @Value("${highlight.element}")
    protected boolean highlight;
    @Value("${screenshot.element}")
    protected boolean screenshot;

    @Value("${maxPageLoadTime}")
    private int maxPageLoadTime;

    final String JS_BUILD_CSS_SELECTOR =
            "for(" +
                    "var e=arguments[0]," +
                    "n=[]," +
                    "i=function(e,n){" +
                    "if(!e||!n)return 0;" +
                    "for(var i=0,a=e.length;a>i;i++)if(-1==n.indexOf(e[i]))return 0;" +
                    "return 1};" +
                    "e&&1==e.nodeType&&'HTML'!=e.nodeName;e=e.parentNode){" +
                    "if(e.id){" +
                    "n.unshift('#'+e.id);" +
                    "break}" +
                    "for(var a=1,r=1,o=e.localName," +
                    "l=e.className&&e.className.trim().split(/[\\s,]+/g)," +
                    "t=e.previousSibling;t;t=t.previousSibling)" +
                    "10!=t.nodeType&&t.nodeName==e.nodeName&&(i(l,t.className)&&(l=null),r=0,++a);" +
                    "for(var t=e.nextSibling;t;t=t.nextSibling)" +
                    "t.nodeName==e.nodeName&&(i(l,t.className)&&(l=null),r=0);" +
                    "n.unshift(r?o:o+(l?'.'+l.join('.'):':nth-child('+a+')'))}return n.join(' > ');";

    //click methods//

    /**
     * Click with many functions
     * logging
     * scroll to element
     * wait
     */
    public void click(WebElement element) {
        Log.info("clicking " + element);
        //ensure element on display
        scrollToCenter(element);
        //wait element to be clickable
        explicitWaitVisibilityOfElement(element, maxPageLoadTime * 1000);
        explicitWaitElementToBeClickable(element, maxPageLoadTime * 1000);

        //highlight
        if (highlight) highlightElement(element);
        if (screenshot) {
            //screenshot element
            try {
                screenshotElement(element);
            } catch (IllegalArgumentException illegalException) {
                //retry without screenshot allure
                Log.warn("Warning: Gagal menyimpan screenshot element ke allure report!");
            }
        }

        try {
            element.click();
        } catch (NoSuchElementException |
                 NoSuchWindowException |
                 TimeoutException |
                 ElementNotInteractableException |
                 NoSuchFrameException errorType) {
            elementErrorHandling(errorType);
        }
    }


    /**
     * Click
     * wait before and after click
     */
    public void click(int waitBeforeClickInSecond, WebElement element, int waitAfterClickInSecond) {
        wait(waitBeforeClickInSecond * 1000);
        click(element);
        wait(waitAfterClickInSecond * 1000);
    }

    /**
     * Click
     * wait before click
     */
    public void click(int waitBeforeClickInSecond, WebElement element) {
        wait(waitBeforeClickInSecond * 1000);
        click(element);
    }

    /**
     * Click
     * wait after click
     */
    public void click(WebElement element, int waitAfterClickInSecond) {
        click(element);
        wait(waitAfterClickInSecond * 1000);
    }


    /**
     * Click second element if first element not found
     */
    public void clickConditional(WebElement element1, WebElement element2) {
        try {
            //bypass wait too long
            if (element1.isDisplayed()) click(element1);
        } catch (NoSuchElementException element) {
            if (element2.isDisplayed()) click(element2);
        }
    }

    /**
     * Click using Javascript Executor
     */
    /* To click a certain Web Element using DOM/ JavaScript Executor */
    public void JSclick(WebElement element) {
        ((JavascriptExecutor) driver).executeScript("return arguments[0].click();", element);
    }
    //click ends//

    //sendKeys methods//

    /**
     * sendKeys with many functions
     * logging
     * wait
     * scroll
     * highlight
     * screenshot
     * clear
     * sendkeys
     */
    public void sendKeys(WebElement element, String value) {
        Log.info("typing " + value + " on " + element.toString());
        //wait element to be ready
        explicitWaitVisibilityOfElement(element, maxPageLoadTime * 1000);
        //ensure element on display
        scrollToCenter(element);
        //highlight
        if (highlight) highlightElement(element);
        if (screenshot) {
            //screenshot element
            try {
                screenshotElement(element);
            } catch (IllegalArgumentException illegalException) {
                //retry without allure
                Log.warn("Warning: Gagal menyimpan screenshot element ke allure report!");
            }
        }
        try {
            element.clear();
            element.sendKeys(value);
        } catch (java.util.NoSuchElementException | NoSuchWindowException | TimeoutException | ElementNotInteractableException |
                 NoSuchFrameException errorType) {
            elementErrorHandling(errorType);
        }
    }

    /**
     * sendKeys random double
     */
    public double sendKeysRandomDouble(WebElement webElement, int minRandom, int maxRandom) {
        double randomDouble = RandomGenerator.randomDouble(minRandom, maxRandom);
        sendKeys(webElement, String.valueOf(randomDouble).replace(".", ","));
        return randomDouble;
    }

    /**
     * sendKeys random double and store it in gsheet
     */
    public double sendKeysRandomDoubleSheet(WebElement webElement, int minRandom, int maxRandom, String spreadSheetName,
                                            String column) {
        //generate random number
        double randomDouble = RandomGenerator.randomDouble(minRandom, maxRandom);
        //sendkeys number
        sendKeys(webElement, String.valueOf(randomDouble).replace(".", ","));
        //add to gsheet
        addSpreadsheetValue(String.valueOf(randomDouble), spreadSheetName, column);
        //return random double
        return randomDouble;
    }

    public int sendKeysRandomIntegerSheet(WebElement webElement, int minRandom, int maxRandom, String spreadSheetName,
                                          String column) {
        //generate random number
        int randomInteger = RandomGenerator.randomInteger(minRandom, maxRandom);
        //sendkeys number
        sendKeys(webElement, String.valueOf(randomInteger).replace(".", ","));
        //add to gsheet
        addSpreadsheetValue(String.valueOf(randomInteger), spreadSheetName, column);
        //return random double
        return randomInteger;
    }

    public void sendKeysStaticIntegerSheet(WebElement webElement, int value, String spreadSheetName, String column) {
        //sendkeys number
        sendKeys(webElement, String.valueOf(value));
        //add to gsheet
        addSpreadsheetValue(String.valueOf(value), spreadSheetName, column);
    }


    /**
     * sendKeys get value from gsheet
     */
    public void sendKeysGetFromSheetList(WebElement webElement, String spreadSheetName,
                                         String column, String rowSrc) {

        int row = Integer.parseInt(readString(rowSrc)) + 1;
        ValueRange valueRange = getSpreadsheetValues(initTestDataSpreadsheet(spreadSheetName), column + row);
        List<List<Object>> cellValue = valueRange.getValues();
        //sendkeys text
        sendKeys(webElement, String.valueOf(cellValue.get(0).get(0)));

        writeString(rowSrc, String.valueOf(row));


    }


    //method sendkey
    // -> auto masukin double

    /**
     * sendKeys
     * wait before and after sendKeys
     */
    public void sendKeys(int waitBeforeClickInSecond, WebElement element, String value, int waitAfterClickInSecond) {
        try {
            Thread.sleep(waitBeforeClickInSecond * 1000);
            sendKeys(element, value);
            Thread.sleep(waitAfterClickInSecond * 1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * sendKeys
     * wait before sendKeys
     */
    public void sendKeys(int waitBeforeClickInSecond, WebElement element, String value) {
        try {
            Thread.sleep(waitBeforeClickInSecond * 1000);
            sendKeys(element, value);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * sendKeys
     * wait after sendKeys
     */
    public void sendKeys(WebElement element, String value, int waitAfterClickInSecond) {
        try {
            sendKeys(element, value);
            Thread.sleep(waitAfterClickInSecond * 1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * sendKeys
     * censor logging
     */
    public void incognitoSendKeys(WebElement element, String value) {
        Log.info("typing ******* on " + element.toString());

        //wait element to be ready
        explicitWaitVisibilityOfElement(element, maxPageLoadTime * 1000);

        //highlight
        if (highlight) highlightElement(element);
        if (screenshot) {
            //screenshot element
            try {
                screenshotElement(element);
            } catch (IllegalArgumentException illegalException) {
                //retry without allure
                Log.warn("Warning: Gagal menyimpan screenshot element ke allure report!");
            }
        }

        try {
            element.clear();
            element.sendKeys(value);
        } catch (java.util.NoSuchElementException | NoSuchWindowException | TimeoutException | ElementNotInteractableException |
                 NoSuchFrameException errorType) {
            elementErrorHandling(errorType);
        }
    }


    /**
     * Click second element if first element not found
     */
    public void sendKeysConditional(WebElement element1, WebElement element2, String text) throws Exception {
        try {
            //bypass wait too long
            if (element1.isDisplayed()) sendKeys(element1, text);
        } catch (java.util.NoSuchElementException element) {
            if (element2.isDisplayed()) sendKeys(element2, text);
        }
    }

    public void sendKeysWriteTxtandRandomNumber(WebElement element, String fileLocation, int length) {
        String randomNumber = GenerateRandomNumber(length);
        sendKeys(element, randomNumber);
        writeString(fileLocation, randomNumber);
    }
    //sendKeys ends//

    //Hover mouse methods//
    public void hover(WebElement element) {
        Actions action = new Actions(driver);
        action.moveToElement(element).perform();
        wait(500);
//        screenshotElement()(driver,element);
    }

    //Hover mouse methods//
    public void clickHold(WebElement clickAndHoldElement, WebElement element) {
        Actions actions = new Actions(driver);
        Action action = actions.clickAndHold(clickAndHoldElement).build();
        action.perform();
        screenshotElement(element);
        action = actions.release(clickAndHoldElement).build();
        action.perform();
        wait(500);
    }

    public void hoverClick(WebElement elementHover, WebElement elementClick) throws Exception {
        Actions actions = new Actions(driver);
        actions.moveToElement(elementHover);
        actions.moveToElement(elementClick);
        actions.click().build().perform();
        wait(500);
        screenshotElement(elementClick);
    }


    //scroll methods//
    /* To ScrollUp using JavaScript Executor */
    public void scrollUp()  {
        ((JavascriptExecutor) driver).executeScript("window.scrollBy(0, -100);");
        wait(500);
    }

    /* To ScrollDown using JavaScript Executor */
    //OLD
    public void scrollDown100()  {
        ((JavascriptExecutor) driver).executeScript("window.scrollBy(0, 100);");
        wait(500);
    }

    //500
    public void scrollDown500()   {
        ((JavascriptExecutor) driver).executeScript("window.scrollBy(0, 500);");
        wait(500);

    }

    //700
    public void scrollDown700() {
        ((JavascriptExecutor) driver).executeScript("window.scrollBy(0, 700);");
        wait(500);

    }


    public void scrollToTop() {
        ((JavascriptExecutor) driver).executeScript("scroll(0, 0);");
        wait(500);
    }


    //New
    //scroll vertical using pixel
    public void scrollDown(WebElement table, int pixel) {
        ((JavascriptExecutor) driver).executeScript(String.format("document.querySelector('%s').scrollTop=%s", getCSS(table), String.valueOf(pixel)));
        wait(500);
    }

    public void scrollAlternative(int pixel) {
        ((JavascriptExecutor) driver).executeScript(String.format("document.scrollingElement.scroll(0,%s)", pixel));
        wait(500);
    }

    //scroll horizontal using pixel
    public void scrollToRight(WebElement table, int pixel) {
        ((JavascriptExecutor) driver).executeScript(String.format("document.querySelector('%s').scrollLeft=%s", getCSS(table), String.valueOf(pixel)));
        wait(500);
    }

    public void scrollToCenter(WebElement e) {
        explicitWaitVisibilityOfElement(e,5000);
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block: 'center', inline: 'nearest'})", e);
    }

    public void scrollToElement(WebElement e) {
        explicitWaitVisibilityOfElement(e,5000);
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView();", e);
    }
    //scroll end//

    /* To Accept the Alert Dialog Message */
    public void refreshPage() {
        driver.navigate().refresh();
        wait(3000);
    }


    // Assert TRUE
    public void verifyWebElementIsDisplayed(WebElement element) {
        for (int i = 1; i <= 5; i++) {
            try {
                wait(500);
                List<WebElement> elements = driver.findElements(By.xpath(getXpath(element)));
                Assert.assertTrue(!elements.isEmpty(), "Element is not displayed.");
                Log.infoGreen("Element is displayed");
                break;
            } catch (AssertionError e) {
                Log.infoYellow("retry looking for element " + i);
                if (i == 5) e.printStackTrace();
            }
        }
    }

    public void verifyWebElementIsClickable(WebElement element) {
        for (int i = 1; i <= 5; i++) {
            try {
                Thread.sleep(500);
                Assert.assertTrue(!element.isEnabled(), "Element is not enabled.");
                Log.infoGreen("Element is enabled");
                break;
            } catch (AssertionError | InterruptedException e) {
                Log.infoYellow("retry waiting for element " + i);
                if (i == 5) e.printStackTrace();
            }
        }
    }

    public void waitPageToLoad() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(maxPageLoadTime));
        wait.until(driver -> ((JavascriptExecutor) driver).executeScript("return document.readyState").equals("complete"));
    }

    public void highlightElement(WebElement element) {
        JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript("arguments[0].setAttribute('style',' border: 2px dashed red;');", element);
    }


    /* To Clear the content in the input location */
    public void clear(WebElement element) {
        element.clear();
    }

    /*To Switch To Frame By Index */
    public void switchToFrameByIndex(int index) {
        driver.switchTo().frame(index);
    }


    /*To Switch To Frame By Frame Name */
    public void switchToFrameByFrameName(String frameName) {
        driver.switchTo().frame(frameName);
    }


    /*To Switch To Frame By Web Element */
    public void switchToFrameByWebElement(WebElement element) {
        driver.switchTo().frame(element);
    }


    /*To Switch out of a Frame */
    public void switchOutOfFrame() {
        driver.switchTo().defaultContent();
    }


    /*continue after open a new tab ALAM Method*/
    public void continueOnNewTab() {
        String currentTab = driver.getWindowHandle();
        for (String tab : driver.getWindowHandles()) {
            if (!tab.equals(currentTab)) {
                driver.switchTo().window(tab);
            }
        }
    }


    /*To Get Tooltip Text */
    public String getTooltipText(WebElement element) {
        return element.getAttribute("title").trim();
    }

    /*To Close all Tabs/Windows except the First Tab */
    public void closeAllTabsExceptFirst() {
        ArrayList<String> tabs = new ArrayList<>(driver.getWindowHandles());
        for (int i = 1; i < tabs.size(); i++) {
            driver.switchTo().window(tabs.get(i));
            driver.close();
        }
        driver.switchTo().window(tabs.get(0));
    }

    /*To Print all the Windows */
    public void printAllTheWindows() {
        ArrayList<String> al = new ArrayList<>(driver.getWindowHandles());
        for (String window : al) {
            System.out.println(window);
        }
    }

    public void UploadFile(String Location, WebElement element) throws Exception {
        element.sendKeys(System.getProperty("user.dir") + "/" + Location);
        wait(4000);
    }

    public String excelTemplateEN(int no) {
        String RFQID = readString("src/test/resources/ExcelFiles/RFQList" + no + ".txt").replaceAll("\\x00", "");
        String templateFileName = RFQID.replace("/", "_") + " - Quotation.xlsx";
        return "src/test/resources/downloadedFiles/" + templateFileName;
    }

    public String excelTemplateID(int no) {
        String RFQID = readString("src/test/resources/ExcelFiles/RFQList" + no + ".txt").replaceAll("\\x00", "");
        String templateFileName = RFQID.replace("/", "_") + " - Penawaran.xlsx";
        return "src/test/resources/downloadedFiles/" + templateFileName;
    }

    public void editRFQXlsx(int no) throws Exception {
        String filePath;
        try {
            filePath = System.getProperty("user.dir") + "/" + excelTemplateEN(no);
            System.out.println(filePath);
            getExcelFile(filePath, "Headquarter Demo (1)");
        } catch (FileNotFoundException e) {
            filePath = System.getProperty("user.dir") + "/" + excelTemplateID(no);
            System.out.println(filePath);
            getExcelFile(filePath, "Headquarter Demo (1)");
        }
        updateCell(24, 6, 10);
        updateCell(25, 6, 10);
        updateCell(26, 6, 10);
        updateCell(24, 7, 10);
        updateCell(25, 7, 10);
        updateCell(26, 7, 10);
        writeExcelFile(filePath);
    }

    public void deleteTemplate(int no) {
        try {
            deleteFile(System.getProperty("user.dir") + "/" + excelTemplateEN(no));
        } catch (Exception e) {
            deleteFile(System.getProperty("user.dir") + "/" + excelTemplateID(no));
        }
    }


    //============================================ Eproc Web =========================================================//

    public void UploadFileGATE(String Location, WebElement element) {

        try {
            wait(3000);
            element.sendKeys(System.getProperty("user.dir") + "/" + Location);
        } catch (InvalidArgumentException e) {
            element.sendKeys(System.getProperty("user.dir") + "\\" + Location.replace("/", "\\"));
        }
    }


    public void editQuotationXlsx(int no) throws Exception {
        String filePath;
        try {
            filePath = System.getProperty("user.dir") + "/" + excelTemplateEN(no);
            System.out.println(filePath);
            getExcelFile(filePath, "HQ 1 (1)");
        } catch (FileNotFoundException e) {
            filePath = System.getProperty("user.dir") + "/" + excelTemplateID(no);
            System.out.println(filePath);
            getExcelFile(filePath, "HQ 1 (1)");
        }
        //Brand Name
        updateCell(24, 5, "Kangen Water");
        updateCell(25, 5, "Thermal Paste");
        updateCell(26, 5, "Keyboard Lenovo");
        //quantity
        updateCell(24, 6, 10.12359);
        updateCell(25, 6, 10.12354);
        updateCell(26, 6, 10.12352);
        //unit cost
        updateCell(24, 7, 10.432167);
        updateCell(25, 7, 20.432523);
        updateCell(26, 7, 12.432589);
        //Discount
        updateCell(24, 8, "Amount (Total)");
        updateCell(25, 8, "Percentage (%)");
        updateCell(26, 8, "Amount (Per Unit)");
        //Discount Value
        updateCell(24, 9, 5.247845);
        updateCell(25, 9, 3.903932);
        updateCell(26, 9, 1.99999);

        writeExcelFile(filePath);

    }

    public void editQuotationBulkXlsx(int no) throws Exception {
        String filePath;
        try {
            filePath = System.getProperty("user.dir") + "/" + excelTemplateEN(no);
            System.out.println(filePath);
            getExcelFile(filePath, "HQ 1 (1)");
        } catch (FileNotFoundException e) {
            filePath = System.getProperty("user.dir") + "/" + excelTemplateID(no);
            System.out.println(filePath);
            getExcelFile(filePath, "HQ 1 (1)");
        }
        //Brand Name
        updateCell(24, 5, "Kangen Water");
        updateCell(25, 5, "Thermal Paste");
        updateCell(26, 5, "Keyboard Lenovo");
        //quantity
        updateCell(24, 6, 10.12359);
        updateCell(25, 6, 10.12354);
        updateCell(26, 6, 10.12352);
        //unit cost
        updateCell(24, 7, 101.432167);
        updateCell(25, 7, 201.432523);
        updateCell(26, 7, 112.432589);
        //Discount
        updateCell(24, 8, "Amount (Total)");
        updateCell(25, 8, "Percentage (%)");
        updateCell(26, 8, "Amount (Per Unit)");
        //Discount Value
        updateCell(24, 9, 20.247845);
        updateCell(25, 9, 3.903932);
        updateCell(26, 9, 1.99999);
        //Delivery Cost
        updateCell(27, 12, 10000);

        writeExcelFile(filePath);

    }

    public void editRfqXlsxEproc(double Qty1, double Qty2, double Qty3) throws Exception {
        String filePath;
        filePath = System.getProperty("user.dir") + "/src/test/resources/downloadedFiles/gokomodo-rfq-excel-template.xlsx";
        System.out.println(filePath);
        getExcelFile(filePath, "Sheet1");
        //product
        updateCell(1, 3, "ASUS ROG");
        updateCell(2, 3, "Pepsimen");
        //qty
        updateCell(1, 5, Qty1);
        updateCell(2, 5, Qty2);
        updateCell(3, 5, Qty3);
        //uom
        updateCell(1, 6, "Kg");
        updateCell(2, 6, "Liter");
        updateCell(3, 6, "Pcs");
        //item name
        updateCell(3, 4, "Brand T");
        //Product name add ke 3
        updateCell(3, 3, "Vitamin K");
        //No material ke 3
        updateCell(1, 2, "YUI111");
        updateCell(2, 2, "POP222");
        updateCell(3, 2, "RET888");
        //PR ID ke 3
        updateCell(3, 1, "RTE321");

        writeExcelFile(filePath);
    }

    public void editRFQfromDTCXlsx(double qty3) throws Exception {
        String filePath;
        filePath = System.getProperty("user.dir") + "/src/test/resources/downloadedFiles/gokomodo-tc-excel-template.xlsx";
        System.out.println(filePath);
        getExcelFile(filePath, "Sheet1");
        //Product Name
        updateCell(1, 3, "Kangen Water");
        updateCell(2, 3, "Thermal Paste");
        updateCell(3, 3, "Keyboard Asus");
        //Product Name
        updateCell(1, 4, "Le mineral");
        updateCell(2, 4, "Pepsodent");
        updateCell(3, 4, "Asus");
        //quantity
        updateCell(1, 5, 15.543);
        updateCell(2, 5, 10.5);
        updateCell(3, 5, qty3);
        //UOM
        updateCell(3, 6, "Pcs");
        //unit cost
        updateCell(1, 9, 10.500);
        updateCell(2, 9, 20.700);
        updateCell(3, 9, 17.300);
        //Total
        updateCell(1, 10, 163.2015);
        updateCell(2, 10, 227.85);
        updateCell(3, 10, 346);

        writeExcelFile(filePath);

    }

    public void editVendorXlsxEproc2(String name1, String name2, String name3, String name4, String name5) throws
            Exception {
        String filePath;
        filePath = System.getProperty("user.dir") + "/src/test/resources/downloadedFiles/Invite New Vendor.xlsx";
        System.out.println(filePath);
        getExcelFile(filePath, "Invite New Vendor");
        //Entity
        updateCell(1, 1, "CV");
        updateCell(2, 1, "PT");
        updateCell(3, 1, "PD");
        updateCell(4, 1, "Toko");
        updateCell(5, 1, "Koperasi");
        //Company name
        updateCell(1, 2, "Maju Makmur");
        updateCell(2, 2, "Saka Unity Terdepan");
        updateCell(3, 2, "Indah Terang");
        updateCell(4, 2, "Kelontong");
        updateCell(5, 2, "Karcupil");
        //First Name
        updateCell(1, 3, name1);
        updateCell(2, 3, name2);
        updateCell(3, 3, name3);
        updateCell(4, 3, name4);
        updateCell(5, 3, name5);
        //Last Name
        updateCell(1, 4, "Makmur");
        updateCell(2, 4, "Sakaru");
        updateCell(3, 4, "Permai");
        updateCell(4, 4, "Sijabat");
        updateCell(5, 4, "Rodriguez");
        //Contact
        updateCell(1, 6, 81234561);
        updateCell(2, 6, 87654322);
        updateCell(3, 6, 46372813);
        updateCell(4, 6, 81357914);
        updateCell(5, 6, 82468025);
        //Email
        updateCell(1, 7, "Makmur@mail.com");
        updateCell(2, 7, "Sakaru@mail.com");
        updateCell(3, 7, "Permai@mail.com");
        updateCell(4, 7, "Sijabat@mail.com");
        updateCell(5, 7, "Rodri@mail.com");

        writeExcelFile(filePath);
    }

    public void editVendorXlsxEproc1(String name1) throws Exception {
        String filePath;
        filePath = System.getProperty("user.dir") + "/src/test/resources/downloadedFiles/Invite New Vendor.xlsx";
        System.out.println(filePath);
        getExcelFile(filePath, "Invite New Vendor");
        //Entity
        updateCell(1, 1, "CV");
        //Company name
        updateCell(1, 2, "Maju Makmur");
        //First Name
        updateCell(1, 3, name1);
        //Last Name
        updateCell(1, 4, "Makmur");
        //Contact
        updateCell(1, 6, 81234561);
        //Email
        updateCell(1, 7, "Makmur@mail.com");

        writeExcelFile(filePath);
    }

    public void editRfqXlsOfflineVendor(double Qty1, double Qty2, double Qty3, String ExcelLoc) throws
            Exception {
        String filePath;
        filePath = System.getProperty("user.dir") + "/" + ExcelLoc;
        System.out.println(filePath);
        getExcelFile(filePath, "HQ 1 (1)");
        //Brand
        updateCell(23, 5, "Mindong");
        updateCell(24, 5, "Kangen Water");
        updateCell(25, 5, "Rucika");
        //qty
        updateCell(23, 6, Qty1);
        updateCell(24, 6, Qty2);
        updateCell(25, 6, Qty3);
        //unit cost
        updateCell(23, 7, 101);
        updateCell(24, 7, 56.456);
        updateCell(25, 7, 21.84718);
        //Notes
        updateCell(23, 10, "Product 1 check");
        updateCell(24, 10, "Product 2 check");
        updateCell(25, 10, "Product 3 check");

        writeExcelFile(filePath);
    }

    //=========================================== Issue Invalid TC ===================================================//
    //DTC
    public void editDTCXlsxInvalidTC(double Qty1, double Qty2) throws Exception {
        String filePath;
        filePath = System.getProperty("user.dir") + "/src/test/resources/downloadedFiles/gokomodo-tc-excel-template.xlsx";
        System.out.println(filePath);
        getExcelFile(filePath, "Sheet1");
        //Product name
        updateCell(1, 3, "Product 1");
        updateCell(2, 3, "Product 2");
        //qty
        updateCell(1, 5, Qty1);
        updateCell(2, 5, Qty2);
        //uom
        updateCell(1, 6, "Pcs");
        updateCell(2, 6, "Ton");
        //Price
        updateCell(1, 9, 12000);
        updateCell(1, 9, 10500);

        writeExcelFile(filePath);
    }
    //DTC

    //RFQ
    public void editRFQXlsxInvalidTC(double Qty1, double Qty2, double Qty3) throws Exception {
        String filePath;
        filePath = System.getProperty("user.dir") + "/src/test/resources/downloadedFiles/komodo-rfq-excel-template.xlsx";
        System.out.println(filePath);
        getExcelFile(filePath, "Sheet1");
        //Product name
        updateCell(1, 3, "Product 1");
        updateCell(2, 3, "Product 2");
        //qty
        updateCell(1, 5, Qty1);
        updateCell(2, 5, Qty2);
        //uom
        updateCell(1, 6, "Pcs");
        updateCell(2, 6, "Ton");
        //Price
        updateCell(1, 9, 12000);
        updateCell(1, 9, 10500);

        writeExcelFile(filePath);
    }
    //RFQ
    //=========================================== Issue Invalid TC ===================================================//

    public void deleteTemplateRfq() {
        deleteFile(System.getProperty("user.dir") + "/src/test/resources/downloadedFiles/gokomodo-rfq-excel-template.xlsx");
    }

    public void deleteTemplateVendor() {
        deleteFile(System.getProperty("user.dir") + "/src/test/resources/downloadedFiles/Invite New Vendor.xlsx");
    }


    public void deleteTemplateDTC() {
        deleteFile(System.getProperty("user.dir") + "/src/test/resources/downloadedFiles/gokomodo-tc-excel-template.xlsx");
    }

    public void deleteTemplateRfq(String ExcelLoc) {
        deleteFile(ExcelLoc);
    }

    public void deleteTemplateQuotation(int no) {
        try {
            deleteFile(System.getProperty("user.dir") + "/" + excelTemplateEN(no));
        } catch (Exception e) {
            deleteFile(System.getProperty("user.dir") + "/" + excelTemplateID(no));
        }
    }

    public String getCSS(WebElement ele) {
        String selector = (String) ((JavascriptExecutor) driver).executeScript(JS_BUILD_CSS_SELECTOR, ele);
        return selector;
    }

    public static String getXpath(WebElement ele) {
        String str = ele.toString();
        String[] listString;
        if (str.contains("xpath")) {
            listString = str.split("xpath:");
        } else if (str.contains("id")) {
            listString = str.split("id:");
        } else {
            throw new NotFoundException("No Xpath or ID found");
        }
        String last = listString[1].trim();
        return last.substring(0, last.length() - 1);
    }

    public static Set<Cookie> getCookies() {
        return staticDriver.manage().getCookies();
    }

    public static void deleteCookies() {
        staticDriver.manage().deleteAllCookies();
    }

    public static void setCookies(Set<Cookie> cookies) {
        deleteCookies();
        for (Cookie cookie : cookies) {
            staticDriver.manage().addCookie(cookie);
        }
    }

    @Attachment(value = "Attachment of WebElement {0}", type = "image/png")
    public static byte[] screenshotElement(WebElement element) {
        try {
            BufferedImage image = new AShot().shootingStrategy(ShootingStrategies.viewportRetina(100, 0, 0, 2))
                    .coordsProvider(new WebDriverCoordsProvider())
                    .imageCropper(new IndentCropper())     //.addIndentFilter(blur()))
                    .takeScreenshot(staticDriver, element).getImage();
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageIO.write(image, "png", baos);
            baos.flush();
            byte[] imageInByte = baos.toByteArray();
            baos.close();
            return imageInByte;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "Unable to Get WebElement.".getBytes();
    }

    /**
     * To Attach the Entire Page Screenshot
     */
    @Attachment(value = "Entire Page Screenshot of {0}", type = "image/png")
    public static byte[] fullPageScreenshot(String name) {
        try {
//            Screenshot screenshot = new AShot().shootingStrategy(ShootingStrategies.simple()).takeScreenshot(driver);
            Screenshot screenshot = new AShot().shootingStrategy(ShootingStrategies.viewportRetina(1000, 0, 0, 2)).takeScreenshot(staticDriver);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageIO.write(screenshot.getImage(), "PNG", baos);
            baos.flush();
            byte[] imageInByte = baos.toByteArray();
            baos.close();
            return imageInByte;
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return "Unable to Get Screenshot.".getBytes();
    }

    public static void saveSession(String fileName) {
        // create file named Cookies to store Login Information
        File file = new File("target/cookies/" + fileName + ".data");
        file.mkdirs();
        try {
            // Delete old file if exists
            file.delete();
            file.createNewFile();
            FileWriter fileWrite = new FileWriter(file);
            BufferedWriter Bwrite = new BufferedWriter(fileWrite);
            // loop for getting the cookie information

            // loop for getting the cookie information
            for (Cookie ck : staticDriver.manage().getCookies()) {
                Bwrite.write(encryptData(ck.getName() + ";" + ck.getValue() + ";" + ck.getDomain() + ";" + ck.getPath() + ";" + ck.getExpiry() + ";" + ck.isSecure()));
                Bwrite.newLine();
            }
            Bwrite.close();
            fileWrite.close();

        } catch (
                Exception ex) {
            ex.printStackTrace();
        }
    }

    public static void getSession(String fileName) {
        try {
            staticDriver.manage().deleteAllCookies();
            File file = new File("target/cookies/" + fileName + ".data");
            FileReader fileReader = new FileReader(file);
            BufferedReader Buffreader = new BufferedReader(fileReader);
            String strline;
            while ((strline = Buffreader.readLine()) != null) {
                StringTokenizer token = new StringTokenizer(decryptData(strline), ";");
                while (token.hasMoreTokens()) {
                    String name = token.nextToken();
                    String value = token.nextToken();
                    String domain = token.nextToken();
                    String path = token.nextToken();
                    Date expiry = null;
                    String val;
                    if (!(val = token.nextToken()).equals("null")) {
                        expiry = new SimpleDateFormat("E MMM dd hh:mm:ss z yyyy").parse(val);
                    }
                    Boolean isSecure = Boolean.valueOf(token.nextToken());
                    Cookie ck = new Cookie(name, value, domain, path, expiry, isSecure);
                    staticDriver.manage().addCookie(ck); // This will add the stored cookie to your current session
                }

            }


        } catch (
                Exception ex) {
            ex.printStackTrace();
        }

    }

    //Sales Order company SSA Export Status //
    public void editDocSalesOrderSsa(int no) throws Exception {
        String filePath;
        filePath = System.getProperty("user.dir") + "/src/test/resources/downloadedFiles/sale.order.xlsx";
        System.out.println(filePath);
        getExcelFile(filePath, "Sheet1");

        //spk status
        updateCell(1, 2, "Finished");
        writeExcelFile(filePath);
    }
    //END//

    public void deleteTemplateSalesOrderSSA(int no) {
        deleteFile(System.getProperty("user.dir") + "/src/test/resources/downloadedFiles/sale.order.xlsx");
    }

    //Sales Order company SSA Export Status //
    public void editDocSalesOrderSsaCanceled(int no) throws Exception {
        String filePath;
        filePath = System.getProperty("user.dir") + "/src/test/resources/downloadedFiles/sale.order.xlsx";
        System.out.println(filePath);
        getExcelFile(filePath, "Sheet1");

        //spk status
        updateCell(1, 2, "Cancelled");
        writeExcelFile(filePath);
    }
    //END//

    /**
     * @param webElement
     * @param spreadSheetName name of the spreadsheet
     * @param column          column name. e.g A,B,C,D
     * @param row             row number e.g 1,2,3,4,5
     * @return
     */
    public String getTextToGsheet(WebElement webElement, String spreadSheetName, String column, int row) {

        String elementText = webElement.getText();

        //write location
        List<Object> y = new ArrayList<>();
        List<List<Object>> x = new ArrayList<>();
        y.add(elementText);
        x.add(y);

        //get spreadsheet ID
        String spreadsheetId = initTestDataSpreadsheet(spreadSheetName);
        String cell = column + row;

        //update spreadsheet
        updateSpreadsheetValues(spreadsheetId, cell, "USER_ENTERED", x);

        //return element text
        return elementText;
    }

    public String getTextToGsheet(WebElement webElement, String spreadSheetName, String column) {

        String elementText = webElement.getText();

        //write location
        List<Object> y = new ArrayList<>();
        List<List<Object>> x = new ArrayList<>();
        y.add(elementText);
        x.add(y);

        //get spreadsheet ID
        String spreadsheetId = initTestDataSpreadsheet(spreadSheetName);

        int row = 1;
        String cell;
        ValueRange valueRange;
        List<List<Object>> cellValue;

        //check column values
        do {
            cell = column + row;
            valueRange = getSpreadsheetValues(spreadsheetId, cell);
            cellValue = valueRange.getValues();
            row++;
        } while (cellValue != null);

        //update spreadsheet
        updateSpreadsheetValues(spreadsheetId, cell, "USER_ENTERED", x);

        //return element text
        return elementText;
    }

}
