package BVK;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class SpringBVKApplication {

    public static void main(String[] args) {SpringApplication.run(SpringBVKApplication.class, args);}

//    @EventListener
//    public void onStartUp(ContextRefreshedEvent event) {
//        paramAsProperties(getParam("qa-automation-development-bifrost"), "development-bifrost");
//    }

//    @EventListener
//    public void onShutDown(ContextClosedEvent event) {
//        deleteParam("development-bifrost");
//    }
}
