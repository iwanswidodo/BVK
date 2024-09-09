
package BVK.GlobalMethod.SlackNotif;


import jakarta.annotation.PostConstruct;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.InetAddress;
import java.net.UnknownHostException;

import static BVK.GlobalMethod.Integration.Netlify.NetlifyAPI.ALLURE_URL;

@Component
public class SlackNotif {


    @Value("${slack.webhook}")
    private String webHook;
    @Value("${slack.webhookHoursly}")
    private String webHookHours;
    @Value("${slack.webhookLocal}")
    private String webHookLocal;
    @Value("${slack.notif}")
    private String slackEnable;
    @Value("${device}")
    private String device;
    @Value("${spring.profiles.active}")
    private String environment;
    @Value("${netlify.deploy}")
    private String netlifyDeploy;
    public static String WEB_HOOK=null;
    public static String WEB_HOOKHOURS=null;
    public static String WEB_HOOKLOCAL=null;
    public static String SLACK_EN=null;
    public static String DEVICE=null;
    public static String ENV=null;
    public static String NETLIFY_DEPLOY=null;

    @PostConstruct
    public void initSlackNotif(){
        WEB_HOOK=webHook;
        WEB_HOOKHOURS=webHookHours;
        WEB_HOOKLOCAL=webHookLocal;
        SLACK_EN=slackEnable;
        DEVICE=device.replace("_"," ");
        ENV=environment;
        NETLIFY_DEPLOY=netlifyDeploy;
    }
    private static final Logger LOGGER = LoggerFactory.getLogger(SlackNotif.class);

    public static void sendSlackNotification(String webHookHours,  String message) {

        CloseableHttpClient client = HttpClients.createDefault();
        HttpPost httpPost = new HttpPost(webHookHours);
        try {
            StringEntity entity = new StringEntity(message);
            httpPost.setEntity(entity);
            httpPost.setHeader("Accept", "application/json");
            httpPost.setHeader("Content-type", "application/json");
            client.execute(httpPost);
            client.close();
            LOGGER.info("Slack notification sent");
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        } catch (ClientProtocolException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public static void sendSlackNotification2(String webHook,  String message) {

        CloseableHttpClient client = HttpClients.createDefault();
        HttpPost httpPost = new HttpPost(webHook);
        try {
            StringEntity entity = new StringEntity(message);
            httpPost.setEntity(entity);
            httpPost.setHeader("Accept", "application/json");
            httpPost.setHeader("Content-type", "application/json");
            client.execute(httpPost);
            client.close();
            LOGGER.info("Slack notification sent");
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        } catch (ClientProtocolException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    public static String composeMessage( int passed, int failed, int skipped,
                                        int total, String duration, String failedTests) throws UnknownHostException {

        String branch = "-";
        String commit = "-";
        String job = "-";
        String jobUrl = "-";
        String jobReport = "-";
        String os = System.getProperty("os.name");
        String deviceImage = null;
        String osImage = null;
        String branchName = null;
        String reportLink = null;
        String author = InetAddress.getLocalHost().getHostName();


        if (DEVICE.toLowerCase().contains("api")) os = "API";
        if (DEVICE.toLowerCase().contains("android")) os = "ANDROID";
        if (DEVICE.toLowerCase().contains("ios")) os = "IOS";

        switch (DEVICE.toLowerCase()) {
            case "chrome", "chrome headless", "chrome debug", "chrome throttle", "chrome android" ->
                    deviceImage = "https://drive.google.com/uc?export=download&id=1wJ8A9Y6apPgDJiepGqVvNx7BwXKAMVWu";
            case "firefox", "firefox headless" ->
                    deviceImage = "https://drive.google.com/uc?export=download&id=1BlAISbU1bfkHabzk9I2LplwgnO-kYgBR";
            case "android", "android emulator" ->
                    deviceImage = "https://drive.google.com/uc?export=download&id=19btzrUqZzdM1h_dBVuVTyKTDNT54mhIw";
            case "ios", "ios emulator" ->
                    deviceImage = "https://drive.google.com/uc?export=download&id=1lIDskSHOzb1h5XtoGQqb-5FXMF9tTrUh";
            case "safari", "safari ios" ->
                    deviceImage = "https://drive.google.com/uc?export=download&id=1ramvQOzmKQKzFcl7zxfa5beQrrik8lIi";
            case "api" ->
                    deviceImage = "https://drive.google.com/uc?export=download&id=1kTIzdjb_L6cgVkxLKdhZXEWiwCOW5C4z";
        }

        //Linux or Jenkins
        if (System.getProperty("os.name").toLowerCase().contains("linux")) {
            branch = System.getProperty("branch");
            job = System.getProperty("job");
            jobUrl = System.getProperty("job.url");
            commit = System.getProperty("commit");
            osImage = "https://drive.google.com/uc?export=download&id=1Ig1tiTCUssOBxFfOMicDg9D9n29-8v5U";
            // Added code for trying to get report link from Jenkins if it's null initially
            int retryCount = 3;
            reportLink = null;
            for (int i = 0; reportLink == null && i < retryCount; i++) {
                System.out.println("Retry getting report link...");
                reportLink = "<" + jobUrl + "allure |Test Report>";
                jobReport = "<" + jobUrl + "|#" + job + ">";
            }
        }
        //Mac
        if (System.getProperty("os.name").toLowerCase().contains("mac")) {
            osImage = "https://drive.google.com/uc?export=download&id=1lIDskSHOzb1h5XtoGQqb-5FXMF9tTrUh";
            reportLink = "<" + ALLURE_URL + "|Test Report>";
        }
        if (System.getProperty("os.name").toLowerCase().contains("mac") && NETLIFY_DEPLOY.toLowerCase().equals("false")) {
            reportLink = "Test Report Disabled";
        }

        //Windows
        if (System.getProperty("os.name").toLowerCase().contains("windows")) {
            osImage = "https://drive.google.com/uc?export=download&id=1k5fffFHQCogec21m4RNHz1foS8uqxjfq";
            reportLink = "<" + ALLURE_URL + "|Test Report>";
        }
        if (System.getProperty("os.name").toLowerCase().contains("windows") && NETLIFY_DEPLOY.toLowerCase().equals("false")) {
            reportLink = "Test Report Disabled";
        }

        if (branch.contains("origin")) {
            branchName = "tree/" + branch.substring(7);
        }

        return new String(
                "{" +
                        "  \"blocks\": [" +
                        "    {" +
                        "      \"type\": \"section\"," +
                        "      \"text\": {" +
                        "        \"type\": \"mrkdwn\"," +
                        "        \"text\": \"*Test Result:* *" + job + "*\"" +
                        "      }" +
                        "    }," +
                        "    {" +
                        "      \"type\": \"divider\"" +
                        "    }," +
                        "    {" +
                        "      \"type\": \"section\"," +
                        "      \"text\": {" +
                        "          \"type\": \"mrkdwn\"," +
                        "          \"text\": \"*\\u2022 Passed:* " + passed + "\\n*\\u2022 Failed:* " + failed +
                        "\\n*\\u2022 Skipped:* " + skipped + "\\n\\n*\\u2022 Total:* " + total + "\"" +
                        "        }" +
                        "    }," +
                        "    {" +
                        "      \"type\": \"divider\"" +
                        "    }," +
                        "    {" +
                        "      \"type\": \"section\"," +
                        "      \"fields\": [" +
                        "        {" +
                        "          \"type\": \"mrkdwn\"," +
                        "          \"text\": \"*Failed Test Script:*\\n" + failedTests + "\"" +
                        "        }" +
                        "      ]" +
                        "    }," +
                        "    {" +
                        "      \"type\": \"section\"," +
                        "      \"fields\": [" +
                        "        {" +
                        "          \"type\": \"mrkdwn\"," +
                        "          \"text\": \"*Note:* Please check the report for more details.\"" +
                        "        }" +
                        "      ]" +
                        "    }," +
                        "    {" +
                        "      \"type\": \"divider\"" +
                        "    }," +
                        "    {" +
                        "      \"type\": \"section\"," +
                        "      \"fields\": [" +
                        "        {" +
                        "          \"type\": \"mrkdwn\"," +
                        "          \"text\": \"*Report:*\\n "+reportLink+"\"" +
                        "        }" +
                        "      ]" +
                        "    }" +
                        "  ]" +
                        "}");
    }
}

