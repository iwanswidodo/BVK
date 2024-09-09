package BVK.GlobalMethod.SpringBoot;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Getter
@Component
public class PropertiesReader {
    @Value("${spring.profiles.active}")
    private String environment;
    @Value("${device}")
    private String device;
    @Value("${device.headless}")
    private boolean deviceHeadless;
    @Value("${device.size}")
    private String deviceSize;
    @Value("${highlight.element}")
    private boolean highlightElement;
    @Value("${maxPageLoadTime}")
    private int maxPageLoadTime;
    @Value("${android.name}")
    private String androidName;
//    @Value("${android.package}")
//    private String androidPackage;
//    @Value("${android.pathAPK}")
//    private String APK;
//    @Value("${android.activity}")
//    private String androidActivity;
//    @Value("${android.pathAPK}")
//    private String androidAPK;
//    @Value("${ios.name}")
//    private String iosName;
//    @Value("${ios.platform}")
//    private String iosPlatform;
//    @Value("${android.udid}")
//    private String androidUdid;
//    @Value("${ios.udid}")
//    private String iosUdid;
//    @Value("${ios.pathIPA}")
//    private String iosIPA;
    @Value("${screenshot.element}")
    private boolean screenshotElement;
    @Value("${record.mode}")
    private String recordMode;

}