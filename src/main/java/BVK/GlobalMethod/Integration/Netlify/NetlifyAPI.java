package BVK.GlobalMethod.Integration.Netlify;

import BVK.GlobalMethod.Logger.Log;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import jakarta.annotation.PostConstruct;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static BVK.GlobalMethod.AllureReport.AllureGenerate.*;
import static io.restassured.RestAssured.given;

@Component
public class NetlifyAPI {

    @Value("${netlify.token}")
    private String netlifyBearer;
    @Value("${allure.delete-report}")
    private String deleteAllureReport;
    @Value("${allure.delete-result}")
    private String deleteAllureResult;
    @Value("${netlify.deploy}")
    private String deployNetlify;

    private static String NETLIFY_BEARER;
    public static String ALLURE_URL;
    public static String DELETE_ALLURE_REPORT;
    public static String DEPLOY_ALLURE;
    public static String DELETE_ALLURE_RESULT;
    private static List<String> siteIds;

    @PostConstruct
    public void initNetlifyAPI() {
        NETLIFY_BEARER = netlifyBearer;
        DELETE_ALLURE_REPORT = deleteAllureReport;
        DEPLOY_ALLURE = deployNetlify;
        DELETE_ALLURE_RESULT = deleteAllureResult;
    }

    public static void deployAllure() throws Exception {

        generateReport();
        Thread.sleep(5000);
        zipReport();
        RestAssured.baseURI = "https://api.netlify.com";
        File file = new File("allure-report.zip");
        Response res = given().headers(
                        "Authorization",
                        "Bearer " + NETLIFY_BEARER,
                        "Content-Type",
                        "application/zip").
                body(file).
                when().
                post("/api/v1/sites").
                then().
                assertThat().statusCode(201).
                extract().response();
        JSONParser parser = new JSONParser();
        JSONObject jsonResponse = (JSONObject) parser.parse(res.getBody().prettyPrint());
        ALLURE_URL = (String) jsonResponse.get("url");
        delZipReport();
        if (DELETE_ALLURE_REPORT.contains("on")) {
            delAllureReport();
        }
    }

    public static void getSites() throws ParseException {
        RestAssured.baseURI = "https://api.netlify.com";
        Response res = given().headers(
                        "Authorization",
                        "Bearer " + NETLIFY_BEARER).
                when().
                get("/api/v1/sites").
                then().
                assertThat().statusCode(200).extract().response();
        JSONParser parser = new JSONParser();
        JSONArray jsonResponse = (JSONArray) parser.parse(res.getBody().asString());
        siteIds = new ArrayList<String>();
        for (int i = 0; i < jsonResponse.size(); i++) {
            JSONObject siteData = (JSONObject) jsonResponse.get(i);
            siteIds.add((String) siteData.get("id"));
        }
    }

    public static void deleteAllSites() throws ParseException {
        getSites();
        for (int i = 0; i < siteIds.size(); i++) {
            RestAssured.baseURI = "https://api.netlify.com";
            Response res = given().headers(
                            "Authorization",
                            "Bearer " + NETLIFY_BEARER).
                    when().
                    delete("/api/v1/sites/" + siteIds.get(i)).
                    then().
                    assertThat().statusCode(204).extract().response();
        }
        Log.infoGreen("All sites has been deleted");
    }

    //    mvn exec:java -Dexec.mainClass="Gokomodo.GlobalMethod.Integration.Netlify.NetlifyAPI" -Dexec.args="deploy,5M0Aa4WiZRZhVv93mlXCuzs6J3qY9rf-OCOGLX5lEJc,off"
    public static void main(String[] args) throws Exception {
        if (NETLIFY_BEARER == null) NETLIFY_BEARER = args[1];
        if (DELETE_ALLURE_REPORT == null) DELETE_ALLURE_REPORT = args[2];
        switch (args[0]) {
            case "delete" -> deleteAllSites();
            case "deploy" -> deployAllure();
            default -> Log.infoRed(args[0]+" is not a valid argument");
        }
        System.out.println(ALLURE_URL);
    }
}