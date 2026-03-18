package me.dinuka.guideme;

import org.apache.catalina.Context;
import org.apache.catalina.LifecycleException;
import org.apache.catalina.startup.Tomcat;

import java.io.File;

public class Main {
    private static final int SERVER_PORT = 8080;

    public static void main(String[] args) {
        try {
            Tomcat tomcat = new Tomcat();
            tomcat.setPort(SERVER_PORT);
            tomcat.getConnector();

            // Set base directory for Tomcat
            String baseDir = System.getProperty("user.dir");
            tomcat.setBaseDir(new File(baseDir + "/tomcat.8080").getAbsolutePath());

            // Configure webapp directory
            String webappDirLocation = "src/main/webapp";
            File webappDir = new File(webappDirLocation);

            // Check if directory exists and print debug info
            System.out.println("Base directory: " + baseDir);
            System.out.println("Webapp directory: " + webappDir.getAbsolutePath());
            System.out.println("Webapp exists: " + webappDir.exists());

            if (webappDir.exists()) {
                Context context = tomcat.addWebapp("/guide_me", webappDir.getAbsolutePath());
//                Tomcat.addServlet(context, "RestApp", new ServletContainer(new AppConfig()));
//                context.addServletMappingDecoded("/api/*", "RestApp");
            } else {
                System.err.println("ERROR: Webapp directory not found!");
                return;
            }

            tomcat.start();
            System.out.println("Server started successfully!");
            System.out.println("Access at: http://localhost:" + SERVER_PORT + "/guide_me/");

            tomcat.getServer().await();
        }catch (LifecycleException e){
            e.printStackTrace();
        }
    }
}
