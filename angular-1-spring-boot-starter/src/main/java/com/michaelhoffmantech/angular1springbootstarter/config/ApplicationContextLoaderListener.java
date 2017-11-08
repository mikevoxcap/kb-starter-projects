package com.michaelhoffmantech.angular1springbootstarter.config;

import javax.servlet.ServletContext;

import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.legacy.context.web.SpringBootContextLoaderListener;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.util.StringUtils;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.GenericWebApplicationContext;

import com.michaelhoffmantech.angular1springbootstarter.Application;

/**
 * Override of Spring's Legacy SpringBootContextLoaderListener
 */
public class ApplicationContextLoaderListener extends SpringBootContextLoaderListener {
   private static final String INIT_PARAM_DELIMITERS = ",; \t\n";

   @Override
   public WebApplicationContext initWebApplicationContext(
         final ServletContext servletContext) {
      String configLocationParam = servletContext.getInitParameter(CONFIG_LOCATION_PARAM);
      SpringApplicationBuilder builder = new SpringApplicationBuilder(
            (Object[]) StringUtils.tokenizeToStringArray(configLocationParam,
                  INIT_PARAM_DELIMITERS));

      builder.properties(
            PropertiesInitializer.getDefaultProperties(Application.APPLICATION_NAME));

      @SuppressWarnings("unchecked")
      Class<? extends ConfigurableApplicationContext> contextClass = (Class<? extends ConfigurableApplicationContext>) determineContextClass(
            servletContext);
      builder.contextClass(contextClass);
      builder.initializers(
            new ApplicationContextInitializer<GenericWebApplicationContext>() {
               @Override
               public void initialize(GenericWebApplicationContext applicationContext) {
                  applicationContext.setServletContext(servletContext);
               }
            });
      WebApplicationContext context = (WebApplicationContext) builder.run();
      servletContext.setAttribute(
            WebApplicationContext.ROOT_WEB_APPLICATION_CONTEXT_ATTRIBUTE, context);
      return context;
   }

}
