package BVK.GlobalMethod.Encryption;

import java.util.Base64;
import java.util.StringTokenizer;

import static BVK.GlobalMethod.Utils.RandomGenerator.*;

public class Encryption {
    public static String decryptData(String data) {
        String L3 = new String (Base64.getDecoder().decode(data.getBytes()));
        StringTokenizer st = new StringTokenizer(L3, ".");
        String L2 = st.nextToken().substring(7);
        return new String (Base64.getDecoder().decode(L2.getBytes()));
    }
    public static String encryptData(String data){
        String L1 = Base64.getEncoder().encodeToString(data.getBytes());
        String L2 = "$"+GenerateRandomAlphaNumericCharacters(2)+
                "$"+GenerateRandomAlphaNumericCharacters(2)+
                "$"+L1+"."+GenerateRandomAlphaNumericCharacters(Integer.parseInt(GenerateRandomNumber(1)));
        return Base64.getEncoder().encodeToString(L2.getBytes());
    }

    public static String encode64(String data){
        return Base64.getEncoder().encodeToString(data.getBytes());
    }
    public static String decode64(String data){
        return new String (Base64.getDecoder().decode(data.getBytes()));
    }

    public static String encode64(String str1, String str2) {
        return new String(Base64.getEncoder().encode((str1 + ":" + str2).getBytes()));
    }

    public static void main(String[] args) {
    }






}
