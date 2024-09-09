package BVK.GlobalMethod.Utils;

import java.math.RoundingMode;
import java.text.DecimalFormat;

public class MathUtils {
    public String roundingFourDP(Double number){
        DecimalFormat df = new DecimalFormat("#.####");
        df.setRoundingMode(RoundingMode.CEILING);
        return df.format(number);
    }
}
