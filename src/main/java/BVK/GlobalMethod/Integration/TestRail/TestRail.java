package BVK.GlobalMethod.Integration.TestRail;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD) //can use in method only.
public @interface TestRail
{
    //TestRail Test Case Id
    String[] testCaseId() default "none";

}
