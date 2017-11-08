package com.michaelhoffmantech.angular1springbootstarter.config;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

//import javax.persistence.spi.PersistenceProvider;
//import javax.persistence.spi.PersistenceProviderResolver;
//import javax.persistence.spi.PersistenceProviderResolverHolder;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.EnumerablePropertySource;
import org.springframework.core.env.MapPropertySource;
import org.springframework.core.env.PropertySource;
import org.springframework.stereotype.Component;

import com.google.common.base.Strings;
import com.google.common.io.BaseEncoding;

@Component
public class ApplicationContextInitializer implements
      org.springframework.context.ApplicationContextInitializer<ConfigurableApplicationContext> {

   private Logger log = LoggerFactory.getLogger(getClass());
   private static final Pattern decodePasswordPattern = Pattern
         .compile("password\\((.*?)\\)");

   private PropertyPasswordDecoder passwordDecoder = new Base64PropertyPasswordDecoder();
   private static AtomicBoolean initialized = new AtomicBoolean();

   public ApplicationContextInitializer() {
      if (!initialized.get()) {
         log.info("initializing one time startup...");
         initializeDefaultProfiles();
         initialized.set(true);
      }
   }

   public static interface PropertyPasswordDecoder {
      public String decodePassword(String encodedPassword);
   }

   public static class Base64PropertyPasswordDecoder implements PropertyPasswordDecoder {

      @Override
      public String decodePassword(String encodedPassword) {
         try {
            byte[] decodedData = BaseEncoding.base64().decode(encodedPassword);
            String decodedString = new String(decodedData, "UTF-8");
            return decodedString;
         } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
         }
      }

   }

   @Override
   public void initialize(ConfigurableApplicationContext applicationContext) {
      log.info("initializing applicationContext...");

      ConfigurableEnvironment environment = applicationContext.getEnvironment();
      for (PropertySource<?> propertySource : environment.getPropertySources()) {
         Map<String, Object> propertyOverrides = new LinkedHashMap<String, Object>();
         decodePasswords(propertySource, propertyOverrides);
         if (!propertyOverrides.isEmpty()) {
            PropertySource<?> decodedProperties = new MapPropertySource(
                  "decoded " + propertySource.getName(), propertyOverrides);
            environment.getPropertySources().addBefore(propertySource.getName(),
                  decodedProperties);
         }
      }
   }

   private void decodePasswords(PropertySource<?> source,
         Map<String, Object> propertyOverrides) {
      if (source instanceof EnumerablePropertySource) {
         EnumerablePropertySource<?> enumerablePropertySource = (EnumerablePropertySource<?>) source;
         for (String key : enumerablePropertySource.getPropertyNames()) {
            Object rawValue = source.getProperty(key);
            if (rawValue instanceof String) {
               String decodedValue = decodePasswordsInString((String) rawValue);
               propertyOverrides.put(key, decodedValue);
            }
         }
      }
   }

   private String decodePasswordsInString(String input) {
      if (input == null)
         return null;
      StringBuffer output = new StringBuffer();
      Matcher matcher = decodePasswordPattern.matcher(input);
      while (matcher.find()) {
         String replacement = passwordDecoder.decodePassword(matcher.group(1));
         // log.info("decoded password: {}", matcher.group(1));
         // log.info("decoded password: {} to {}", matcher.group(1),
         // replacement);
         matcher.appendReplacement(output, replacement);
      }
      matcher.appendTail(output);
      return output.toString();
   }

   /**
    * Autodetecting Spring profiles in the static initializer so that we can do
    * things like automatically set a profile based on hostname or other values.
    * This is better than doing it in "main" because this will also work for
    * unit tests, when initialized as web application via web initializers, etc.
    */
   private void initializeDefaultProfiles() {
      log.warn("java.version: {}", System.getProperty("java.version"));
      log.warn("user.dir: {}", System.getProperty("user.dir"));
      String sysPropActive = System.getProperty("spring.profiles.active");
      log.warn("spring.profiles.active={}", sysPropActive);
      String envPropActive = System.getenv("SPRING_PROFILES_ACTIVE");
      log.warn("SPRING_PROFILES_ACTIVE={}", envPropActive);

      boolean forceDefaultProfiles = false;

      if (forceDefaultProfiles || Strings.isNullOrEmpty(sysPropActive)
            && Strings.isNullOrEmpty(envPropActive)) {
         if (System.getProperty("spring.profiles.autodetect.disabled") != null) {
            log.warn(
                  "No Spring profile configured, and spring.profiles.autodetect.disabled is set");
         } else {
            log.warn("No Spring profile configured, attempting to autodetect profile(s)");

            // Default to local profile
            String defaultProfiles = Constants.SPRING_PROFILE_LOCAL;
            String autoProfiles = defaultProfiles;

            if (defaultProfiles.equals(autoProfiles)) {
               // Attempt to detect docker based on present of /.dockerinit file
               if (new File("/.dockerinit").exists()) {
                  autoProfiles = Constants.SPRING_PROFILE_DOCKER;
               }
            }

            log.warn("autodetected profile(s): {}", autoProfiles);
            System.setProperty("spring.profiles.active", autoProfiles);

         }
      }
   }
}