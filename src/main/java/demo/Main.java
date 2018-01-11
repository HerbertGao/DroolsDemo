package demo;

import org.drools.core.impl.InternalKnowledgeBase;
import org.drools.core.impl.KnowledgeBaseFactory;
import org.kie.api.definition.KiePackage;
import org.kie.api.io.ResourceType;
import org.kie.api.runtime.KieSession;
import org.kie.internal.builder.KnowledgeBuilder;
import org.kie.internal.builder.KnowledgeBuilderFactory;
import org.kie.internal.io.ResourceFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.IOException;
import java.util.Collection;
import java.util.Scanner;

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
        InternalKnowledgeBase knowledgeBase = KnowledgeBaseFactory.newKnowledgeBase();
        KnowledgeBuilder knowledgeBuilder = KnowledgeBuilderFactory.newKnowledgeBuilder();

        knowledgeBuilder.add(ResourceFactory.newClassPathResource("rules/demo.drl", Main.class), ResourceType.DRL);
        if (knowledgeBuilder.hasErrors()) {
            System.out.println(knowledgeBuilder.getErrors().toString());
            throw new IOException("Can't load demo.drl");
        }

        Collection<KiePackage> pkgs = knowledgeBuilder.getKnowledgePackages();
        knowledgeBase.addPackages(pkgs);
        KieSession kieSession = knowledgeBase.newKieSession();

        Message message = new Message();
        message.setStatus(Message.HELLO);
        kieSession.insert(message);
        kieSession.fireAllRules();
        kieSession.dispose();

        System.out.println("Input 'y' to continue");
        Scanner s = new Scanner(System.in);
        String in = s.nextLine();
        if (in.equals("y")) {
            runDrools();
        }
    }
}
