package BVK.GlobalMethod.Utils;

import java.io.File;
import java.io.FileOutputStream;
import java.net.InetAddress;
import java.util.Properties;


public class EnvironmentSetup {

    public static void environmentSetup() {
        try {
            Properties properties = new Properties();
            properties.setProperty("Author", InetAddress.getLocalHost().getHostName());
            properties.setProperty("Browser", "Firefox");
            properties.setProperty("OS", System.getProperty("os.name"));
            properties.setProperty("OS Architecture", System.getProperty("os.arch"));
            properties.setProperty("OS Bit", System.getProperty("sun.arch.data.model"));
            properties.setProperty("Java Version", System.getProperty("java.version"));
            properties.setProperty("Host Name", InetAddress.getLocalHost().getHostName());
            properties.setProperty("Host IP Address", InetAddress.getLocalHost().getHostAddress());

            File file = new File("./src/main/resources/environment.properties");
            FileOutputStream fileOut = new FileOutputStream(file);
            properties.store(fileOut, "Environment Setup");
            fileOut.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
