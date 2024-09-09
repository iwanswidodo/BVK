package BVK.GlobalMethod.AllureReport;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;

import static org.zeroturnaround.zip.ZipUtil.pack;

public class AllureGenerate {

    public static void generateReport(){
        //generate allure report
        try {
            Runtime rt = Runtime.getRuntime();
            Process pr;
            if(System.getProperty("os.name").toLowerCase().contains("windows")) pr = rt.exec("cmd /c allure generate");
            else pr = rt.exec("allure generate");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void delAllureReport(){
        try {
            FileUtils.deleteDirectory(new File(System.getProperty("user.dir")+"/allure-report"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void delAllureResult(){
        try {
            FileUtils.deleteDirectory(new File(System.getProperty("user.dir")+"/allure-results"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void zipReport(){
        //zipping allure report
        pack(new File(System.getProperty("user.dir")+"/allure-report"),new File(System.getProperty("user.dir")+"/allure-report.zip"));
    }

    public static void delZipReport(){
        File file = new File(System.getProperty("user.dir")+"/allure-report.zip");
        file.delete();
    }
}
