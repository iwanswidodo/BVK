package BVK.GlobalMethod.Integration.TestRail;

import BVK.GlobalMethod.Logger.Log;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.testng.ITestResult;

import java.lang.annotation.Annotation;
import java.util.HashMap;
import java.util.Map;

import static BVK.GlobalMethod.Encryption.Encryption.decryptData;

@Component
public class TestRailAPI {


    @Value("${testrail.username}")
    private String username;
    private static String TESTRAIL_USERNAME;

    @Value("${testrail.password}")
    private String password;
    private static String TESTRAIL_PASSWORD;


    @Value("${testrail.url}")
    private String url;
    private static String TESTRAIL_URL;


    @Value("${testrail.tester}")
    private int tester;
    private static int TESTRAIL_TESTER_ID;


    @Value("${testrail.testRun}")
    private String runId;
    private static String TESTRAIL_RUN_ID;

    @Value("${testrail.enable}")
    private String testRailEnable;
    private static String TESTRAIL_ENABLE;

    @PostConstruct
    public void initTestRailApi() {
        TESTRAIL_USERNAME = username;
        TESTRAIL_PASSWORD = password;
        TESTRAIL_URL = url;
        TESTRAIL_TESTER_ID = tester;
        TESTRAIL_RUN_ID = runId;
        TESTRAIL_ENABLE = testRailEnable;
    }


    public static APIClient clientInit() {
        APIClient client = new APIClient(TESTRAIL_URL);
        client.setUser(TESTRAIL_USERNAME);
        client.setPassword(decryptData(TESTRAIL_PASSWORD));
        return client;
    }

    public static Map getResult(ITestResult result) {
        Map data = new HashMap();
        if (result.isSuccess()) {
            data.put("status_id", 1);
        } else {
            data.put("status_id", 5);
            data.put("comment", result.getThrowable().toString());
        }
        data.put("custom_tested_by", TESTRAIL_TESTER_ID);
        return data;
    }

    public static void postTestRail(ITestResult result) {
        Annotation testRailAnnotation = result.getMethod().getConstructorOrMethod().getMethod().getAnnotation(TestRail.class);
        if(testRailAnnotation != null && TESTRAIL_ENABLE.toLowerCase().equals("on")) {
            TestRail testInfo = (TestRail) testRailAnnotation;
            String[] caseIds=testInfo.testCaseId();
            for (String caseId: caseIds){
                clientInit().sendPost("add_result_for_case/" + TESTRAIL_RUN_ID + "/" + caseId, getResult(result));
            }
            Log.infoMagenta("TestRail test run " + TESTRAIL_RUN_ID + ", test case " + String.join(",",caseIds) + " are updated!");
        }
    }

}
