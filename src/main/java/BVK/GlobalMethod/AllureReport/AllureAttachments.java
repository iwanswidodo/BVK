package BVK.GlobalMethod.AllureReport;

import BVK.GlobalMethod.SpringBoot.PropertiesReader;
import io.qameta.allure.Attachment;
import jakarta.annotation.PostConstruct;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.yandex.qatools.ashot.AShot;
import ru.yandex.qatools.ashot.Screenshot;
import ru.yandex.qatools.ashot.coordinates.WebDriverCoordsProvider;
import ru.yandex.qatools.ashot.cropper.indent.IndentCropper;
import ru.yandex.qatools.ashot.shooting.ShootingStrategies;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

@Component
public class AllureAttachments {


    @Autowired
    private PropertiesReader propertiesReader;
    public static String deviceName = null;


    public static boolean recordOnFailure;
    public static boolean recordOnPassed;

    @PostConstruct
    public void initAttachmentConfig() {
        deviceName = propertiesReader.getDevice();
        switch (propertiesReader.getRecordMode().toLowerCase()){
            case "on" -> {
                recordOnPassed = true;
                recordOnFailure = true;
            }
            case "off" -> {
                recordOnPassed = false;
                recordOnFailure = false;
            }
            case "on_failure" -> {
                recordOnPassed = false;
                recordOnFailure = true;
            }
        }
    }

    public static byte[] saveFullPageScreenshot(String name, WebDriver driver) {
        try {
            Screenshot screenshot = new AShot().shootingStrategy(ShootingStrategies.viewportNonRetina(1000,0,0)).takeScreenshot(driver);
//            Screenshot screenshot = new AShot().shootingStrategy(ShootingStrategies.simple()).takeScreenshot(driver);
//            Screenshot screenshot = new AShot().shootingStrategy(ShootingStrategies.viewportRetina(1000,0,0,2)).takeScreenshot(driver);
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

    @Attachment(value = "Attachment of WebElement {0}", type = "image/png")
    public static byte[] saveWebElement(WebDriver driver, WebElement element) {
        try {
            BufferedImage image = new AShot()
//                    .shootingStrategy(ShootingStrategies.viewportRetina(100,0,0,2))
                    .coordsProvider(new WebDriverCoordsProvider())
                    .imageCropper(new IndentCropper())     //.addIndentFilter(blur()))
                    .takeScreenshot(driver,element).getImage();
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
     * To Convert File to Bytes
     */
//    private static byte[] fileToBytes(String fileName) {
//        try {
//            return IOUtils.toByteArray(new FileInputStream(fileName));
//        } catch (IOException e) {
//            return new byte[0];
//        }
//    }

    public static byte[] fileToBytes(String fileName) throws Exception {
        File file = new File(fileName);
        return Files.readAllBytes(Paths.get(file.getAbsolutePath()));
    }

    @Attachment(value = "Attachment of Video {0}", type = "video/mp4", fileExtension = ".mp4")
    public static byte[] attachVideoToAllure(String fileName) throws Exception {
        return fileToBytes(fileName);
    }
//    public static void attachVideoToAllure(String fileName) {
//        Allure.addAttachment("VideoFile", "video/mp4", new ByteArrayInputStream(fileToBytes(fileName)), "mp4");
//    }


    @Attachment(value = "Attachment of Video {0}", type = "video/webm", fileExtension = ".webm")
    public static byte[] attachWebmToAllure(String fileName) throws Exception {
        return fileToBytes(fileName);
    }

    /**
     * To Attach the CSV File to the Allure Report
     */
    @Attachment(value = "CSV Attachment", type = "text/csv")
    public static byte[] attachFileType_CSV(String filePath) throws Exception {
        return fileToBytes(filePath);
    }

    /**
     * To Attach the XML File to the Allure Report
     */
    @Attachment(value = "XML Attachment", type = "text/xml")
    public static byte[] attachFileType_XML(String filePath) throws Exception {
        return fileToBytes(filePath);
    }

    /**
     * To Attach the XLSX File to the Allure Report
     */
    @Attachment(value = "MS Excel - XLSX Attachment", type = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet")
    public static byte[] attachFileType_XLSX(String filePath) throws Exception {
        return fileToBytes(filePath);
    }

    /**
     * To Attach the XLS File to the Allure Report
     */
    @Attachment(value = "MS Excel - XLS Attachment", type = "application/vnd.ms-excel")
    public static byte[] attachFileType_XLS(String filePath) throws Exception {
        return fileToBytes(filePath);
    }

    /**
     * To Attach the TXT File to the Allure Report
     */
    @Attachment(value = "TXT Attachment", type = "text/plain")
    public static byte[] attachFileType_TXT(String filePath) throws Exception {
        return fileToBytes(filePath);
    }

    /**
     * To Attach the JSON File to the Allure Report
     */
    @Attachment(value = "JSON Attachment", type = "text/json")
    public static byte[] attachFileType_JSON(String filePath) throws Exception {
        return fileToBytes(filePath);
    }

    /**
     * To Attach the DOCX File to the Allure Report
     */
    @Attachment(value = "MS Word - DOCX Attachment", type = "application/vnd.openxmlformats-officedocument.wordprocessingml.document")
    public byte[] attachFileType_DOCX(String filePath) throws Exception {
        return fileToBytes(filePath);
    }

    /**
     * To Attach the DOC File to the Allure Report
     */
    @Attachment(value = "MS Word - DOC Attachment", type = "application/msword")
    public static byte[] attachFileType_DOC(String filePath) throws Exception {
        return fileToBytes(filePath);
    }

    /**
     * To Attach the JPEG Image File to the Allure Report
     */
    @Attachment(value = "JPEG Attachment", type = "image/jpg")
    public static byte[] attachFileType_JPEG(String filePath) throws Exception {
        return fileToBytes(filePath);
    }

    /**
     * To Attach the PNG Image File to the Allure Report
     */
    @Attachment(value = "PNG Attachment", type = "image/png")
    public static byte[] attachFileType_PNG(String filePath) throws Exception {
        return fileToBytes(filePath);
    }


}
