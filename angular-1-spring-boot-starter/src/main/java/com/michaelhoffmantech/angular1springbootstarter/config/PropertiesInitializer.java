package com.michaelhoffmantech.angular1springbootstarter.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Strings;
import com.google.common.collect.ImmutableMap;

/**
 * Example usage from Spring Boot Application main method: <code>
 * SpringApplication app = new SpringApplication(Application.class);
 * app.setDefaultProperties(PropertiesInitializer.getDefaultProperties(APPLICATION_NAME));
 * </code>
 *
 * Example usage from Spring Boot ApplicationWebXml: <code>
 * protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
 * 	   log.info("ApplicationWebXml configure called");
 *     return application.profiles(addDefaultProfile())
 *         .properties(PropertiesInitializer.getDefaultProperties(Application.APPLICATION_NAME))
 *         .sources(Application.class);
 * }
 * </code>
 *
 * Example usage from ApplicationContextLoaderListener: <code>
 * builder.properties(PropertiesInitializer.getDefaultProperties(Application.APPLICATION_NAME));
 * </code>
 *
 */
public class PropertiesInitializer {
   private static Logger log = LoggerFactory.getLogger(PropertiesInitializer.class);

   public static final String SPRING_CONFIG_LOCATION = "spring.config.location";
   public static final String APP_SPRING_CONFIG_LOCATION = "app.spring.config.location";
   public static final String DOMAIN_HOME_PROPERTY = "domain.home";
   public static final String APPRESOURCES_SUBDIR = "/appresources";

   /**
    * Default properties that should be set in the Application's main method,
    * e.g: <code>
    * 		SpringApplication app = new SpringApplication(Application.class);
    * 		app.setDefaultProperties(PropertiesInitializer.getDefaultProperties(APPLICATION_NAME));
    * </code>
    *
    * And also in the ApplicationWebXml, e.g: <code>
    *     	protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
     *			log.info("ApplicationWebXml configure called");
     *			return application.profiles(addDefaultProfile())
     *				.properties(PropertiesInitializer.getDefaultProperties(Application.APPLICATION_NAME))
    *				.sources(Application.class);
     * 		}
     * </code>
    *
    * By setting spring.config.location here, we are able to specify an
    * application specific properties file location that can be used on a server
    * to override any default properties from the application.yml and
    * application-server.yml files.
    *
    * By using 2 levels, it allows for "global properties" at the top level and
    * then application specific properties in an application specific
    * subdirectory. E.g. <code>
    * ${domain.home}/appresources/
    * 	    config/
    * 		    application.properties
    * 		    application-server.properties
    * 		    <applicationName>/
    * 			    application.properties
    * 			    application-server.properties
    * </code>
    *
    * And the order of property resolution will be (last file to have a value
    * wins):
    *
    * classpath:/${applicationName}.properties
    * classpath:/config/${applicationName}.properties
    * file:${applicationName}.properties
    * file:config/${applicationName}.properties
    *
    * file:${domain.home}/appresources/${applicationName}.properties
    * file:${domain.home}/appresources/${applicationName}.yml
    * file:${domain.home}/appresources/config/${applicationName}.properties
    * file:${domain.home}/appresources/config/${applicationName}.yml
    *
    * file:${domain.home}/appresources/application.properties
    * file:${domain.home}/appresources/${applicationName}/application.properties
    * file:${domain.home}/appresources/config/application.properties
    * file:${domain.home}/appresources/config/${applicationName}/application.properties
    *
    * classpath:/application.properties classpath:/config/application.properties
    * file:${domain.home}/appresources/application.properties
    * file:${domain.home}/appresources/${applicationName}/application.properties
    * file:${domain.home}/appresources/config/application.properties
    * file:${domain.home}/appresources/config/${applicationName}/application.properties
    *
    * classpath:/application-server.properties
    * classpath:/config/application-server.properties
    * file:${domain.home}/appresources/application-server.properties
    * file:${domain.home}/appresources/${applicationName}/application-server.properties
    * file:${domain.home}/appresources/config/application-server.properties
    * file:${domain.home}/appresources/config/${applicationName}/application-server.properties
    *
    * To override the default logging configuration, a properties file can set
    * "logging.config" to point to the location of a custom logback.xml or
    * logback-spring.xml file located on the server.
    *
    * And remember that with the Spring Actuator endpoint, it's possible to find
    * out exactly where properties were loaded via the /env endpoint, e.g.
    * http://localhost:9001/angular-1-spring-boot-starter/env
    *
    * @see https://docs.spring.io/spring-boot/docs/current/reference/html/boot-features-external-config.html
    * @return
    */
   public static ImmutableMap<String, Object> getDefaultProperties(
         String applicationName) {
      ImmutableMap.Builder<String, Object> builder = ImmutableMap
            .<String, Object> builder().put("spring.application.name", applicationName);

      String springConfigLocation = computeSpringConfigLocation(applicationName);
      if (springConfigLocation != null) {
         builder.put("spring.config.location", springConfigLocation);
      }
      return builder.build();
   }

   private static String computeSpringConfigLocation(String applicationName) {
      String underscoreApplicationName = applicationName.replaceAll("-", "_");
      String dashApplicationName = applicationName.replaceAll("_", "-");
      String springConfigLocation = System.getProperty(SPRING_CONFIG_LOCATION);
      if (Strings.isNullOrEmpty(springConfigLocation)) {
         log.info("{} not defined, computing value using custom app logic",
               SPRING_CONFIG_LOCATION);
         String appSpringConfigLocation = System.getProperty(APP_SPRING_CONFIG_LOCATION);
         if (Strings.isNullOrEmpty(appSpringConfigLocation)) {
            log.info("{} not defined, checking for domain.home",
                  APP_SPRING_CONFIG_LOCATION);
            String computedDomainHome = System.getProperty(DOMAIN_HOME_PROPERTY);
            if (Strings.isNullOrEmpty(computedDomainHome)) {
               log.debug("{} not defined, using default value of config ~/.domain_home",
                     DOMAIN_HOME_PROPERTY);
               computedDomainHome = System.getProperty("user.home") + "/.domain_home";
            }
            // computedDomainHome should not end in a slash
            log.info("computedDomainHome: {}", computedDomainHome);
            String computedAppResourcesDir = computedDomainHome + APPRESOURCES_SUBDIR;
            // ordered from lowest to highest precedences, so the the last files
            // found
            // in this list will be used before files earlier in the list
            StringBuilder sb = new StringBuilder();

            appendLocations(sb, "classpath:", dashApplicationName,
                  underscoreApplicationName, true);
            appendLocations(sb, "classpath:/config/", dashApplicationName,
                  underscoreApplicationName, true);
            appendLocations(sb, "file:", dashApplicationName, underscoreApplicationName,
                  true);
            appendLocations(sb, "file:/config/", dashApplicationName,
                  underscoreApplicationName, true);

            appendLocations(sb, "file:" + computedAppResourcesDir + "/",
                  dashApplicationName, underscoreApplicationName, true);
            appendLocations(sb, "file:" + computedAppResourcesDir + "/config/",
                  dashApplicationName, underscoreApplicationName, true);

            springConfigLocation = sb.toString();
         } else {
            log.info("{} was defined, using it's value for spring.config.location",
                  APP_SPRING_CONFIG_LOCATION);
            springConfigLocation = appSpringConfigLocation;
         }
      } else {
         log.warn("{} already defined, skipping custom app logic to compute value",
               SPRING_CONFIG_LOCATION);
      }
      log.info("{}: {}", SPRING_CONFIG_LOCATION, springConfigLocation);
      return springConfigLocation;
   }

   private static void appendLocations(StringBuilder sb, String baseDir,
         String dashApplicationName, String underscoreApplicationName,
         boolean appendFinalComma) {
      sb.append(baseDir).append(",").append(baseDir).append(dashApplicationName)
            .append(".properties").append(",").append(baseDir).append(dashApplicationName)
            .append(".yml").append(",").append(baseDir).append(dashApplicationName)
            .append("/").append(",").append(baseDir).append(underscoreApplicationName)
            .append(".properties").append(",").append(baseDir)
            .append(underscoreApplicationName).append(".yml").append(",").append(baseDir)
            .append(underscoreApplicationName).append("/").append(",")

            // Add _config.properties suffix as well
            .append(baseDir).append(dashApplicationName).append("_config.properties")
            .append(",").append(baseDir).append(dashApplicationName).append("_config.yml")
            .append(",").append(baseDir).append(dashApplicationName).append("_config/")
            .append(",").append(baseDir).append(underscoreApplicationName)
            .append("_config.properties").append(",").append(baseDir)
            .append(underscoreApplicationName).append("_config.yml").append(",")
            .append(baseDir).append(underscoreApplicationName).append("_config/");
      if (appendFinalComma) {
         sb.append(",");
      }
   }
}
