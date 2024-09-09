package BVK.GlobalMethod.AwsSsm;

import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.ssm.SsmClient;
import software.amazon.awssdk.services.ssm.model.GetParameterRequest;
import software.amazon.awssdk.services.ssm.model.GetParameterResponse;
import software.amazon.awssdk.services.ssm.model.SsmException;

import static BVK.GlobalMethod.Utils.ReadAndWriteFile.deleteFile;
import static BVK.GlobalMethod.Utils.ReadAndWriteFile.writeString;

public class ParamStore {

    public static String getParam(String paraName){
        Region region = Region.AP_SOUTHEAST_1;
        SsmClient ssmClient = SsmClient.builder()
                .region(region)
                .build();
        try {
            GetParameterRequest parameterRequest = GetParameterRequest.builder()
                    .name(paraName)
                    .build();

            GetParameterResponse parameterResponse = ssmClient.getParameter(parameterRequest);
            return parameterResponse.parameter().value();

        } catch (SsmException e) {
            System.err.println(e.getMessage());
            System.exit(1);
        }
        return null;
    }

    public static void paramAsProperties(String paramValue, String envName){
        writeString("src/main/resources/application-"+envName+".properties",paramValue);
    }

    public static void deleteParam(String envName){
        deleteFile("src/main/resources/application-"+envName+".properties");
    }
}
