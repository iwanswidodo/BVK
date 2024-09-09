package BVK.FE;

import BVK.GlobalMethod.SpringBoot.BaseTest;
import BVK.GlobalMethod.SpringBoot.PropertiesReader;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.options.UiAutomator2Options;
import io.appium.java_client.gecko.options.GeckoOptions;
import io.appium.java_client.ios.IOSDriver;
import io.appium.java_client.ios.options.XCUITestOptions;
import jakarta.annotation.PostConstruct;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.NotFoundException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.chromium.ChromiumNetworkConditions;
import org.openqa.selenium.devtools.DevTools;
import org.openqa.selenium.devtools.v120.network.Network;
import org.openqa.selenium.devtools.v120.network.model.Request;
import org.openqa.selenium.devtools.v120.network.model.RequestId;
import org.openqa.selenium.devtools.v120.network.model.Response;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.safari.SafariDriver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;

import java.time.Duration;
import java.util.*;


@BaseTest
public class DriverFactory {

    private static ChromeDriver chromeDriver;
    private static FirefoxDriver firefoxDriver;
    private EdgeDriver edgeDriver;
    private InternetExplorerDriver internetExplorerDriver;
    private SafariDriver safariDriver;
    private AndroidDriver androidDriver;
    private IOSDriver iosDriver;
    private ChromeOptions chromeOptions;
    private FirefoxProfile firefoxProfile;
    private FirefoxOptions firefoxOptions;
    @Autowired
    private PropertiesReader propertiesReader;
    @Autowired
    private AppiumServer server;
//    @Autowired
//    private AppDistribution appDistribution;

    String downloadFileLocation;


    @PostConstruct
    public void initDriverFactory() {
        if (System.getProperty("os.name").equals("Windows 10")) {
            downloadFileLocation = System.getProperty("user.dir") + "\\src\\test\\resources\\downloadedFiles";
        } else {
            downloadFileLocation = System.getProperty("user.dir") + "/src/test/resources/downloadedFiles";
        }
    }


    @Bean
    @ConditionalOnMissingBean
    @ConditionalOnProperty(name = "device", havingValue = "chrome")
    @Primary
    public ChromeDriver chromeDriver(@Value("${device.headless}") boolean deviceHeadless, @Value("${device.debug}") boolean deviceDebug, @Value("${device.throttle}") boolean deviceThrottle) {
        chromeOptions = new ChromeOptions();
        HashMap<String, Object> chromePrefs = new HashMap<String, Object>();
        chromePrefs.put("profile.default_content_settings.popups", 0);
        chromePrefs.put("download.default_directory", downloadFileLocation);
        chromeOptions.setExperimentalOption("prefs", chromePrefs);
        chromeOptions.addArguments("--remote-allow-origins=*");
        if (deviceHeadless) {
            chromeOptions.addArguments("enable-automation");
            chromeOptions.addArguments("--no-sandbox");
            chromeOptions.addArguments("--headless=new");
            chromeOptions.addArguments("--disable-browser-side-navigation");
            chromeOptions.addArguments("--disable-gpu");
            chromeOptions.addArguments("--remote-allow-origins=*");
            chromeOptions.addArguments("--disable-dev-shm-usage");
        }
        chromeDriver = new ChromeDriver(chromeOptions);
        if (deviceDebug) GetStatusNetwork("chrome");
        if (deviceThrottle) doThrottle();
        browserConfig(chromeDriver);
        return chromeDriver;
    }

    @Bean
    @ConditionalOnProperty(name = "device", havingValue = "firefox")
    @Primary
    public FirefoxDriver firefoxDriver(@Value("${device.headless}") boolean deviceHeadless) {
        firefoxProfile = new FirefoxProfile();
        firefoxProfile.setPreference("browser.download.folderList", 2);
        firefoxProfile.setPreference("browser.download.dir", downloadFileLocation);
        firefoxProfile.setPreference("browser.helperApps.neverAsk.saveToDisk", "text/csv,application/zip");
        firefoxOptions = new FirefoxOptions();
        if (deviceHeadless) {
            firefoxOptions.addArguments("--headless");
            firefoxOptions.addArguments("--profile-root");
            firefoxOptions.addArguments("--no-sandbox");
        }
        firefoxOptions.setProfile(firefoxProfile);
        firefoxDriver = new FirefoxDriver(firefoxOptions);
        browserConfig(firefoxDriver);
        return firefoxDriver;
    }

    @Bean
    @ConditionalOnProperty(name = "device", havingValue = "ie")
    @Primary
    public InternetExplorerDriver ieDriver() {
        internetExplorerDriver = new InternetExplorerDriver();
        browserConfig(internetExplorerDriver);
        return internetExplorerDriver;
    }

    @Bean
    @ConditionalOnProperty(name = "device", havingValue = "edge")
    @Primary
    public EdgeDriver edgeDriver() {
        edgeDriver = new EdgeDriver();
        browserConfig(edgeDriver);
        return edgeDriver;
    }

    @Bean
    @ConditionalOnProperty(name = "device", havingValue = "safari")
    @Primary
    public SafariDriver safariDriver() {
        safariDriver = new SafariDriver();
        browserConfig(safariDriver);
        return safariDriver;
    }

    @Bean
    @ConditionalOnProperty(name = "device", havingValue = "chrome_android")
    @Primary
    public AndroidDriver chromeAndroidDriver() {
        if (server.getAppiumService().isRunning()) server.getAppiumService().stop();
        server.getAppiumService().start();
        server.getAppiumService().clearOutPutStreams();
//        ChromeOptions options = new ChromeOptions();
//        options.addArguments("--no-sandbox");
//        options.addArguments("--disable-dev-shm-usage");
//        options.setBrowserVersion("91.0");
        UiAutomator2Options androidCapabilities = new UiAutomator2Options()
                .setDeviceName(propertiesReader.getAndroidName())
                .setIsHeadless(propertiesReader.isDeviceHeadless())
                .setPlatformName("Android")
                .withBrowserName("Chrome")
                .setNewCommandTimeout(Duration.ofMinutes(3));
        androidDriver = new AndroidDriver(server.getAppiumService().getUrl(), androidCapabilities);
        return androidDriver;
    }

    @Bean
    @ConditionalOnProperty(name = "device", havingValue = "firefox_android")
    @Primary
    public AndroidDriver firefoxAndroidDriver() {
        if (server.getAppiumService().isRunning()) server.getAppiumService().stop();
        server.getAppiumService().start();
        server.getAppiumService().clearOutPutStreams();
        Map<String, Object> firefoxOptions1 = new HashMap<>();
        firefoxOptions1.put("androidPackage", "org.mozilla.firefox");
        firefoxOptions1.put("androidActivity", "org.mozilla.gecko.BrowserApp");
//        firefoxOptions1.put("androidDeviceSerial", propertiesReader.getAndroidUdid());
        GeckoOptions androidFirefoxCapabilities = new GeckoOptions()
                .setAutomationName("Gecko")
                .setPlatformName("mac")
                .withBrowserName("firefox").setMozFirefoxOptions(firefoxOptions1);
        androidDriver = new AndroidDriver(server.getAppiumService().getUrl(), androidFirefoxCapabilities);
        return androidDriver;
    }

    @Bean
    @ConditionalOnProperty(name = "device", havingValue = "android")
    @Primary
    public AndroidDriver androidDriver() {
        boolean serverStarted = false;
        while (!serverStarted) {
            try {
                // If the service is running, stop it
                if (server.getAppiumService().isRunning()) {
                    server.getAppiumService().stop();
                }
                // Try to start the service
                server.getAppiumService().start();
                serverStarted = true;
            } catch (Exception e) {
                System.out.println("Error Starting Server. Retrying...");
            }
        }
        server.getAppiumService().clearOutPutStreams();
        UiAutomator2Options desiredCapabilities = new UiAutomator2Options()
                .setDeviceName(propertiesReader.getAndroidName())
                .setIsHeadless(propertiesReader.isDeviceHeadless())
                .setAdbExecTimeout(Duration.ofSeconds(120))
//                .setUdid(propertiesReader.getAndroidUdid())
                .setPlatformName("Android")
//                .setApp(propertiesReader.getAPK())
                .setNewCommandTimeout(Duration.ofSeconds(3600))
//                .setAppActivity(propertiesReader.getAndroidActivity())
//                .setAppPackage(propertiesReader.getAndroidPackage())
                .gpsEnabled()
                .autoGrantPermissions();
        desiredCapabilities.setCapability("appium:disableIdLocatorAutocompletion",true);
        androidDriver = new AndroidDriver(server.getAppiumService().getUrl(), desiredCapabilities);
        return androidDriver;
    }

    @Bean
    @ConditionalOnProperty(name = "device", havingValue = "safari_ios")
    @Primary
    public IOSDriver iosSafariDriver() {
        if (server.getAppiumService().isRunning()) server.getAppiumService().stop();
        server.getAppiumService().start();
        server.getAppiumService().clearOutPutStreams();
        XCUITestOptions iosCapabilities = new XCUITestOptions()
//                .setDeviceName(propertiesReader.getIosName())
                .setIsHeadless(propertiesReader.isDeviceHeadless())
//                .setPlatformVersion(propertiesReader.getIosPlatform())
                .withBrowserName("Safari")
                .setPlatformName("IOS");
        iosDriver = new IOSDriver(server.getAppiumService().getUrl(), iosCapabilities);
        return iosDriver;
    }

    @Bean
    @ConditionalOnProperty(name = "device", havingValue = "ios")
    @Primary
    public IOSDriver iosDriver() {
        try {
            if (server.getAppiumService().isRunning()) {
                server.getAppiumService().stop();
            }
            server.getAppiumService().start();
            server.getAppiumService().clearOutPutStreams();
            XCUITestOptions desiredCapabilities = new XCUITestOptions()
//.setDeviceName(propertiesReader.getIosName())
//                    .setUdid(propertiesReader.getIosUdid())
                    .setIsHeadless(propertiesReader.isDeviceHeadless())
//                    .setPlatformVersion(propertiesReader.getIosPlatform())
//                    .setApp(propertiesReader.getIosIPA())
                    .setPlatformName("IOS");

            iosDriver = new IOSDriver(server.getAppiumService().getUrl(), desiredCapabilities);
            return iosDriver;
        } catch (Exception e) {
            throw new RuntimeException("Could not initialize IOSDriver: " + e.getMessage());
        }
    }


    public void doThrottle() {
        ChromiumNetworkConditions networkConditions = new ChromiumNetworkConditions();
        networkConditions.setOffline(false);
        networkConditions.setLatency(Duration.ofMillis(5));
        networkConditions.setDownloadThroughput(100000);//how fast download is (in bps)
        networkConditions.setUploadThroughput(100000);//how fast upload is (in bps)
        chromeDriver.setNetworkConditions(networkConditions);
    }

    public void GetStatusNetwork(String browserType) {
        //Lihat Di lognya
        // Source nya Chrome Network ada di sini:
        // https://chromedevtools.github.io/devtools-protocol/tot/Network/
        DevTools devTools;
        switch (browserType.toLowerCase()) {
            case "chrome":
                devTools = chromeDriver.getDevTools();
                break;
            case "firefox":
                devTools = firefoxDriver.getDevTools();
                break;
            default:
                throw new NotFoundException("browser not found");
        }

//        DevTools devTools = chromeDriver.getDevTools();
        devTools.createSession();
        devTools.send(Network.enable(Optional.empty(), Optional.empty(), Optional.empty()));

        // part untuk mendapatkan response dari webnya
        // tanda ini " -> " adalah Lambda di gunakan untuk menerima handler

        devTools.addListener(Network.requestWillBeSent(), request ->
        {
            System.out.println("=========== Network Status Request ===========");
            Request req = request.getRequest();
            System.out.println("This is Request URL : " + req.getUrl());
            System.out.println("Request Method => " + request.getRequest().getMethod());
            System.out.println("Request Headers => " + request.getRequest().getHeaders().toString());
            System.out.println("==============================================");
            System.out.println(req.getHeaders().get("Authorization"));
        });

        devTools.addListener(Network.responseReceived(), response ->
        {
            System.out.println("=========== Network Status Response ===========");
            Response res = response.getResponse();
            //System.out.println(res.getUrl());
            System.out.println("Response Url => " + res.getUrl());
            System.out.println("This is Response status: " + res.getStatus());
            System.out.println("Response Headers => " + res.getHeaders().toString());
            System.out.println("Response MIME Type => " + res.getMimeType().toString());
            System.out.println("==============================================");
        });
    }

    public static List<String> getResponseHeader() {
        DevTools devTools = chromeDriver.getDevTools();
        devTools.createSession();
        devTools.send(Network.enable(Optional.empty(), Optional.empty(), Optional.empty()));
        final RequestId[] requestIds = new RequestId[1];
        List<String> responseBody = new ArrayList<>();
        devTools.addListener(Network.responseReceived(), responseReceived -> {
            if (responseReceived.getResponse().getUrl().contains("gokomodo.co")) {
                System.out.println("URL: " + responseReceived.getResponse().getUrl());
                System.out.println("Status: " + responseReceived.getResponse().getStatus());
                System.out.println("Type: " + responseReceived.getType().toJson());
                responseReceived.getResponse().getHeaders().toJson().forEach((k, v) -> System.out.println((k + ":" + v)));
                requestIds[0] = responseReceived.getRequestId();
                System.out.println("Response Body: \n" + devTools.send(Network.getResponseBody(requestIds[0])).getBody() + "\n");
                responseBody.add(devTools.send(Network.getResponseBody(requestIds[0])).getBody());
            }
        });
        return responseBody;
    }

    public void setMaxPageLoadTimeMethod(int timeInSeconds, WebDriver driver) {
        driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(timeInSeconds));
    }

    private void browserConfig(WebDriver driver) {
        if (propertiesReader.getMaxPageLoadTime() > 0) {
            setMaxPageLoadTimeMethod(propertiesReader.getMaxPageLoadTime(), driver);
        }
        if (!propertiesReader.getDevice().toLowerCase().contains("android") || !propertiesReader.getDevice().toLowerCase().contains("ios")) {
            try {
                switch (propertiesReader.getDeviceSize()) {
                    case "auto" -> driver.manage().window().maximize();
                    case "QHD" -> {
                        Dimension dimension = new Dimension(2560, 1440);
                        driver.manage().window().setSize(dimension);
                    }
                    case "FHD" -> {
                        Dimension dimension = new Dimension(1920, 1080);
                        driver.manage().window().setSize(dimension);
                    }
                    case "HD+" -> {
                        Dimension dimension = new Dimension(1600, 900);
                        driver.manage().window().setSize(dimension);
                    }
                    case "HD" -> {
                        Dimension dimension = new Dimension(1280, 800);
                        driver.manage().window().setSize(dimension);
                    }
                    default -> throw new Exception("Wrong device size settings");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

}
