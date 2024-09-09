package BVK.GlobalMethod.Utils;

import java.math.BigDecimal;

public class GateMath {
    private static BigDecimal a;
    private static BigDecimal roundOff;

    // round down number to 4 decimal places
    public static String roundDown4DP(String num) {
        int i = 4;
        int n = (int) Math.pow(10,i);
        double num2 = Double.parseDouble(num);
        a = new BigDecimal(num);
        roundOff = a.setScale(i, BigDecimal.ROUND_DOWN);
        if (roundOff.doubleValue() % 1 == 0 | roundOff.doubleValue()*1000%1000==0) {
            return String.valueOf(Math.round(num2));
        } else {
            while((roundOff.doubleValue()*n)%10==0){
                i = i-1;
                n= (int) Math.pow(10,i);
                roundOff = a.setScale(i, BigDecimal.ROUND_DOWN);
            }
            return roundOff.toString();
        }
    }

    // round down number to 4 decimal places
    public static String roundDown4DP(double num) {
        int i = 4;
        int n = (int) Math.pow(10,i);
        a = new BigDecimal(num);
         roundOff = a.setScale(4, BigDecimal.ROUND_DOWN);
        if (roundOff.doubleValue() % 1 == 0 | roundOff.doubleValue()*1000%1000==0) {
            return String.valueOf(Math.round(num));
        } else {
            while((roundOff.doubleValue()*n)%10==0){
                i = i-1;
                n= (int) Math.pow(10,i);
                roundOff = a.setScale(i, BigDecimal.ROUND_DOWN);
            }
            return roundOff.toString();
        }
    }

    // round down number to 4 decimal places
    public static String roundDown4DP(long num) {
        int i = 4;
        int n = (int) Math.pow(10,i);
         a = new BigDecimal(num);
         roundOff = a.setScale(4, BigDecimal.ROUND_DOWN);
        if (roundOff.doubleValue() % 1 == 0 | roundOff.doubleValue()*1000%1000==0) {
            return String.valueOf(Math.round(num));
        } else {
            while((roundOff.doubleValue()*n)%10==0){
                i = i-1;
                n= (int) Math.pow(10,i);
                roundOff = a.setScale(i, BigDecimal.ROUND_DOWN);
            }
            return roundOff.toString();
        }
    }


    public static String roundDown4DP(int num) {
        return String.valueOf(num);
    }

    public static String roundHalfUp4DP(String num) {
        double num2 = Double.parseDouble(num);
         a = new BigDecimal(num);
         roundOff = a.setScale(4, BigDecimal.ROUND_HALF_UP);
        if (roundOff.doubleValue() % 1 == 0 | roundOff.doubleValue()*1000%1000==0) {
            return String.valueOf(Math.round(num2));
        } else {
            return roundOff.toString();
        }
    }

    public static String roundHalfUp4DP(double num) {
         a = new BigDecimal(num);
         roundOff = a.setScale(4, BigDecimal.ROUND_HALF_UP);
        if (roundOff.doubleValue() % 1 == 0 | roundOff.doubleValue()*1000%1000==0) {
            return String.valueOf(Math.round(num));
        } else {
            return roundOff.toString();
        }
    }

    public static String roundHalfUp4DP(long num) {
         a = new BigDecimal(num);
         roundOff = a.setScale(4, BigDecimal.ROUND_HALF_UP);
        if (roundOff.doubleValue() % 1 == 0 | roundOff.doubleValue()*1000%1000==0) {
            return String.valueOf(Math.round(num));
        } else {
            return roundOff.toString();
        }
    }


    public static double roundHalfUp2DP(double num) {
        a = new BigDecimal(num);
        roundOff = a.setScale(2, BigDecimal.ROUND_HALF_UP);
        if (roundOff.doubleValue() % 1 == 0 | roundOff.doubleValue()*1000%1000==0) {
            return Math.round(num);
        } else {
            return roundOff.doubleValue();
        }
    }

    public static double roundHalfUp(double number, int decimalPlaces) {
        a = new BigDecimal(number);
        roundOff = a.setScale(decimalPlaces, BigDecimal.ROUND_HALF_UP);
        if (roundOff.doubleValue() % 1 == 0 | roundOff.doubleValue()*1000%1000==0) {
            return Math.round(number);
        } else {
            return roundOff.doubleValue();
        }
    }

    public static String roundHalfUp4DP(int num) {
        return String.valueOf(num);
    }
}
