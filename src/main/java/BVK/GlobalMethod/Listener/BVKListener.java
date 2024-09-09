package BVK.GlobalMethod.Listener;

import BVK.GlobalMethod.Logger.Log;
import BVK.GlobalMethod.SlackNotif.SlackNotif;
import io.qameta.allure.listener.TestLifecycleListener;
import io.qameta.allure.model.TestResult;
import org.testng.*;

import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static BVK.FE.MobileBaseMethod.startRecordingMobile;
import static BVK.FE.MobileBaseMethod.stopRecordingMobile;
import static BVK.GlobalMethod.AllureReport.AllureAttachments.deviceName;
import static BVK.GlobalMethod.AllureReport.AllureAttachments.recordOnFailure;
import static BVK.GlobalMethod.AllureReport.AllureAttachments.recordOnPassed;
import static BVK.GlobalMethod.AllureReport.AllureGenerate.delAllureResult;
import static BVK.GlobalMethod.Integration.Netlify.NetlifyAPI.*;
import static BVK.GlobalMethod.Integration.TestRail.TestRailAPI.postTestRail;
import static BVK.GlobalMethod.SlackNotif.SlackNotif.*;
import static BVK.GlobalMethod.Utils.ScreenRecord.startRecording;
import static BVK.GlobalMethod.Utils.ScreenRecord.stopRecording;

public class BVKListener implements ITestListener, ISuiteListener, IInvokedMethodListener, IExecutionListener, TestLifecycleListener {

    public static List<ITestNGMethod> passedTests = new ArrayList<>();
    public static List<ITestNGMethod> failedTests = new ArrayList<>();
    public static List<ITestNGMethod> skippedTests = new ArrayList<>();
    private static long startTime;
    private static long endTime;


    /*This belongs to ISuiteListener and will execute before the Suite Starts*/
    @Override
    public void onStart(ISuite arg0) {
        Reporter.log("Execution of the Suite '" + arg0.getName() + "' Started!", false);
    }


    /*This belongs to ISuiteListener and will execute after the Suite Ends*/
    @Override
    public void onFinish(ISuite arg0) {
        Reporter.log("Execution of the Suite '" + arg0.getName() + "' Completed!", false);
    }


    /*This belongs to ITestListener, It will execute at the time of Test Execution */
    @Override
    public void onStart(ITestContext arg0) {
//        testRailAPI.clientInit();
        Reporter.log("About to begin executing Test " + arg0.getName(), false);
    }


    /*This belongs to ITestListener, It will execute at the End of Test*/
    @Override
    public void onFinish(ITestContext arg0) {
        Reporter.log("Completed executing test " + arg0.getName(), false);
    }




    /*This belongs to ITestListener, It will Execute only when the Test is PASSED*/
    @Override
    public void onTestSuccess(ITestResult result) {
        postTestRail(result);
        passedTests.add(result.getMethod());

        Log.infoGreen("SUCCESSFULLY EXECUTED TEST: " + result.getTestClass().getName() + "." + result.getMethod().getMethodName());
    }


    /*This belongs to ITestListener, It will Execute only when the Test is FAILED*/
    @Override
    public void onTestFailure(ITestResult result) {
        postTestRail(result);
        failedTests.add(result.getMethod());
        Log.infoRed("FAILED TEST: " + result.getTestClass().getName() + "." + result.getMethod().getMethodName());
    }


    /*This belongs to ITestListener, It and will execute before the Main Test Starts (@Test)*/
    @Override
    public void onTestStart(ITestResult result) {
        try {
            Log.infoBlue("STARTED TESTING: " + result.getTestClass().getName() + "." + result.getMethod().getMethodName());
            if(recordOnPassed | recordOnFailure) {
                if (deviceName.contains("android") || deviceName.contains("ios")) startRecordingMobile();
                else startRecording(returnMethodName(result.getMethod()));
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    /*This belongs to ITestListener, It will execute only if any of the Main Test(@Test) Gets Skipped*/
    @Override
    public void onTestSkipped(ITestResult result) {
        postTestRail(result);
        skippedTests.add(result.getMethod());
        Log.infoYellow("SKIPPED TEST: " + result.getTestClass().getName() + "." + result.getMethod().getMethodName() + "\n");

    }

    @Override
    public void beforeTestStop(TestResult result) {
        if(recordOnPassed | recordOnFailure) {
            try {
                switch (result.getStatus()) {
                    case PASSED -> {
                        if (deviceName.contains("android") || deviceName.contains("ios"))
                            stopRecordingMobile(recordOnPassed);
                        else stopRecording(recordOnPassed, true, true);
                    }
                    default -> {
                        if (deviceName.contains("android") || deviceName.contains("ios"))
                            stopRecordingMobile(recordOnFailure);
                        else stopRecording(recordOnFailure, true, true);
                    }
                }
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }



    /*This belongs to IInvokedMethodListener, It will execute before every method Including @Before @After @Test*/
    public void beforeInvocation(IInvokedMethod arg0, ITestResult arg1) {
        String textMsg = "About to begin executing following method : " + returnMethodName(arg0.getTestMethod());
        Reporter.log(textMsg, false);
    }


    /*This belongs to IInvokedMethodListener and will execute after every method Including @Before @After @Test*/
    public void afterInvocation(IInvokedMethod arg0, ITestResult arg1) {
        String textMsg = "Completed executing following method : " + returnMethodName(arg0.getTestMethod());
        Reporter.log(textMsg, false);
    }


    /*This will return method names to the calling function*/
    private String returnMethodName(ITestNGMethod method) {
        return method.getRealClass().getSimpleName() + "." + method.getMethodName();
    }


    @Override
    public void onTestFailedButWithinSuccessPercentage(ITestResult result) {
    }

    @Override
    public void onExecutionStart() {
        startTime = System.currentTimeMillis();
    }

    @Override
    public void onExecutionFinish() {

        endTime = System.currentTimeMillis();


        //deploy allure report
        if(DEPLOY_ALLURE.contains("on")){
            try {
                deployAllure();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        if (SLACK_EN.toLowerCase().equals("yes")) {
            if (failedTests.isEmpty()) {
                System.out.println(failedTests);
                // send webhook to all testcase is success
                try {
                    SlackNotif.sendSlackNotification(WEB_HOOK, getTestSummary());
                } catch (UnknownHostException e) {
                    throw new RuntimeException(e);
                }
            } else {
                // send webhook when failed test case is failed
                try {
                    SlackNotif.sendSlackNotification(WEB_HOOKHOURS, getTestSummary());
                } catch (UnknownHostException e) {
                    throw new RuntimeException(e);
                }
            }

        }


        //slack notification
        if (SLACK_EN.toLowerCase().equals("local")) {
            try {
                SlackNotif.sendSlackNotification(WEB_HOOKLOCAL, getTestSummary());
            } catch (UnknownHostException e) {
                throw new RuntimeException(e);
            }
        }

        if(DELETE_ALLURE_RESULT.toLowerCase().contains("on")) {
            delAllureResult();
        }
    }

    public static String getTestSummary() throws UnknownHostException {
        int passed = passedTests.size();
        int failed = failedTests.size();
        int skipped = skippedTests.size();
        int total = passed + failed + skipped;
        String duration = getExecutionTime(endTime - startTime);

        return SlackNotif.composeMessage(passed, failed, skipped, total, duration, getFailedTestsList());
    }


    private static String getExecutionTime(long millisecond) {

        long min = TimeUnit.MILLISECONDS.toMinutes(millisecond);
        long sec = TimeUnit.MILLISECONDS.toSeconds(millisecond) -
                TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millisecond));

        return min + " minute(s) " + sec + " seconds";
    }

    public static String getFailedTestsList() {
        StringBuilder builder = new StringBuilder();
        if (failedTests.size() > 0) {
            for (ITestNGMethod test : failedTests) {
                builder.append("\\n\\u2022 " + test.getMethodName());
            }
        } else {
            builder.append("\\n\\u2022 None");
        }
        return builder.toString();
    }

}
