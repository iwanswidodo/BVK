package BVK.FE;

import BVK.GlobalMethod.SpringBoot.BaseTest;
import io.appium.java_client.service.local.AppiumDriverLocalService;
import io.appium.java_client.service.local.AppiumServiceBuilder;
import io.appium.java_client.service.local.flags.GeneralServerFlag;
import org.openqa.selenium.NotFoundException;

import java.io.File;
import java.util.HashMap;

@BaseTest
public class AppiumServer {
    public AppiumDriverLocalService getAppiumService() {
        HashMap<String, String> environment = new HashMap<String, String>();
        switch (System.getProperty("os.name")) {
            case "Windows 10" -> {
                environment.put("ANDROID_HOME", "C:\\Users\\" + System.getProperty("user.name") + "\\AppData\\Local\\Android\\Sdk");
                return AppiumDriverLocalService.buildService(new AppiumServiceBuilder()
                        .usingDriverExecutable(new File("C:\\Program Files\\nodejs\\node.exe"))
                        .withAppiumJS(new File("C:\\Users\\" + System.getProperty("user.name") + "\\AppData\\Roaming\\npm\\node_modules\\appium"))
                        .usingPort(4723)
                        .withArgument(GeneralServerFlag.SESSION_OVERRIDE)
                        .withArgument(GeneralServerFlag.BASEPATH, "/wd/hub/")
                        .withArgument(() -> "--allow-insecure", "chromedriver_autodownload")
                        .withArgument(() -> "--allow-insecure", "adb_shell")
                        .withEnvironment(environment)
                        .withLogFile(new File("target/BVK-logs/appium.log")));
            }
            case "Mac OS X" -> {
                environment.put("PATH", "/Library/Internet Plug-Ins/JavaAppletPlugin.plugin/Contents/Home/bin:/Users/" + System.getProperty("user.name") + "/Library/Android/sdk/tools:/Users/" + System.getProperty("user.name") + "/Library/Android/sdk/platform-tools:/usr/local/bin:/usr/bin:/bin:/usr/sbin:/sbin:/Library/Apple/usr/bin" + System.getenv("PATH"));
                environment.put("ANDROID_HOME", "/Users/" + System.getProperty("user.name") + "/Library/Android/sdk");
                return AppiumDriverLocalService.buildService(new AppiumServiceBuilder()
                        .usingDriverExecutable(new File("/opt/homebrew/bin/node"))
//                        .withAppiumJS(new File("/opt/homebrew/lib/node_modules/appium/index.js"))
//                        .withAppiumJS(new File("/Users/albertushadiwidjana/.nvm/versions/node/v18.18.0/lib/node_modules/appium"))
                        .usingPort(4723)
                        .withArgument(GeneralServerFlag.SESSION_OVERRIDE)
//                        .withArgument(GeneralServerFlag.BASEPATH, "/wd/hub/")
                        .withArgument(() -> "--allow-insecure", "chromedriver_autodownload")
                        .withArgument(() -> "--allow-insecure", "adb_shell")
                        .withEnvironment(environment)
                        .withLogFile(new File("target/BVK-logs/appium.log")));
            }
            default -> throw new NotFoundException("OS not supported");
        }
    }
}