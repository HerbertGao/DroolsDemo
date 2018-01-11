package demo;

import org.kie.api.KieServices;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.IOException;

@SpringBootApplication
public class Main {

    public static void main(String[] args) {
        SpringApplication.run(Main.class, args);
        try {
            runDrools();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void runDrools() throws IOException {
        KieServices ks = KieServices.Factory.get();
        KieContainer kieContainer = ks.getKieClasspathContainer();
        KieSession kieSession = kieContainer.newKieSession("demo_ksession");

        Message message = new Message();
        message.setStatus(Message.HELLO);
        kieSession.insert(message);
        kieSession.fireAllRules();
        kieSession.dispose();
    }
}
