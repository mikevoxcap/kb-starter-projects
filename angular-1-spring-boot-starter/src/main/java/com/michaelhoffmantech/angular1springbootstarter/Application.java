package com.michaelhoffmantech.angular1springbootstarter;

import java.awt.Desktop;
import java.io.IOException;
import java.net.InetAddress;
import java.net.URI;
import java.util.Arrays;
import java.util.Collection;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.actuate.autoconfigure.MetricFilterAutoConfiguration;
import org.springframework.boot.actuate.autoconfigure.MetricRepositoryAutoConfiguration;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.core.env.SimpleCommandLinePropertySource;

import com.michaelhoffmantech.angular1springbootstarter.config.ApplicationProperties;
import com.michaelhoffmantech.angular1springbootstarter.config.PropertiesInitializer;
import com.michaelhoffmantech.angular1springbootstarter.config.Constants;

/**
 * The main Spring Boot Application class.
 *
 */
@SpringBootApplication
@ComponentScan
@EnableAutoConfiguration(exclude = { MetricFilterAutoConfiguration.class,
      MetricRepositoryAutoConfiguration.class })
@EnableConfigurationProperties({ ApplicationProperties.class })
@PropertySource(value = "classpath:META-INF/build-info.properties",
      ignoreResourceNotFound = true)
public class Application {
   public static final String APPLICATION_NAME = "angular-1-spring-boot-starter";

   private static final Logger log = LoggerFactory.getLogger(Application.class);

   @Inject
   private Environment env;

   /**
    * Initializes application.
    * <p/>
    * Spring profiles can be configured with a program arguments
    * --spring.profiles.active=your-active-profile
    * <p/>
    */
   @PostConstruct
   public void initApplication() throws IOException {
      if (env.getActiveProfiles().length == 0) {
         log.warn("No Spring profile configured, running with default configuration");
      } else {
         log.info("Running with Spring profile(s) : {}",
               Arrays.toString(env.getActiveProfiles()));
         Collection<String> activeProfiles = Arrays.asList(env.getActiveProfiles());
         if (activeProfiles.contains(Constants.SPRING_PROFILE_LOCAL)
               && activeProfiles.contains(Constants.SPRING_PROFILE_PRODUCTION)) {
            log.error("You have misconfigured your application! "
                  + "It should not run with both the 'local' and 'prod' profiles at the same time.");
         }
         if (activeProfiles.contains(Constants.SPRING_PROFILE_PRODUCTION)
               && activeProfiles.contains(Constants.SPRING_PROFILE_FAST)) {
            log.error("You have misconfigured your application! "
                  + "It should not run with both the 'prod' and 'fast' profiles at the same time.");
         }
         if (activeProfiles.contains(Constants.SPRING_PROFILE_LOCAL)
               && activeProfiles.contains(Constants.SPRING_PROFILE_CLOUD)) {
            log.error("You have misconfigured your application! "
                  + "It should not run with both the 'local' and 'cloud' profiles at the same time.");
         }
      }
   }

   public static void main(String[] args) throws Exception {
      log.info("java.version: {}", System.getProperty("java.version"));
      log.info("user.dir: {}", System.getProperty("user.dir"));
      SpringApplication app = new SpringApplication(Application.class);
      app.setDefaultProperties(
            PropertiesInitializer.getDefaultProperties(APPLICATION_NAME));
      SimpleCommandLinePropertySource source = new SimpleCommandLinePropertySource(args);
      Environment env = app.run(args).getEnvironment();
      log.info(
            "Access URLs:\n----------------------------------------------------------\n\t"
                  + "Local: \t\thttp://127.0.0.1:{}{}\n\t"
                  + "External: \thttp://{}:{}{}\n----------------------------------------------------------",
            new Object[] { env.getProperty("server.port"),
                  env.getProperty("server.contextPath", ""),
                  InetAddress.getLocalHost().getHostAddress(),
                  env.getProperty("server.port"),
                  env.getProperty("server.contextPath", "") });

      String localUrl = "http://127.0.0.1:" + env.getProperty("server.port", "80")
            + env.getProperty("server.contextPath", "");
      openUrl(localUrl);
   }

   /**
    * Platform specific ways to try to open the browser
    *
    * @param url
    * @throws Exception
    */
   private static void openUrl(String url) throws Exception {
      log.info("opening browser to url: {}", url);
      if (Desktop.isDesktopSupported()) {
         Desktop.getDesktop().browse(new URI(url));
      } else {
         String os = System.getProperty("os.name").toLowerCase();
         if (os.indexOf("win") >= 0) {
            Runtime rt = Runtime.getRuntime();
            rt.exec("rundll32 url.dll,FileProtocolHandler " + url);
         } else if (os.indexOf("mac") >= 0) {
            Runtime rt = Runtime.getRuntime();
            rt.exec("open " + url);
         } else if (os.indexOf("nix") >= 0 || os.indexOf("nux") >= 0) {
            Runtime rt = Runtime.getRuntime();
            String[] browsers = { "epiphany", "firefox", "mozilla", "konqueror",
                  "netscape", "opera", "links", "lynx" };

            StringBuffer cmd = new StringBuffer();
            for (int i = 0; i < browsers.length; i++)
               cmd.append((i == 0 ? "" : " || ") + browsers[i] + " \"" + url + "\" ");

            rt.exec(new String[] { "sh", "-c", cmd.toString() });
         }
      }
   }
}
