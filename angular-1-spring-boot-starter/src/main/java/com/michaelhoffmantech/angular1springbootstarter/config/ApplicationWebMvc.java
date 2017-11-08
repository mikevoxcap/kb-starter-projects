package com.michaelhoffmantech.angular1springbootstarter.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@Component
public class ApplicationWebMvc extends WebMvcConfigurerAdapter {
   private Logger log = LoggerFactory.getLogger(getClass());

   @Override
   public void addResourceHandlers(ResourceHandlerRegistry registry) {
      log.info("adding custom resource handlers");

      // Service /dev from both /dev and /scripts similar to "gulp serve"
      registry.addResourceHandler("/dev/serve/**")
            .addResourceLocations("classpath:/static/dev/serve/",
                  "classpath:/static/scripts/")
            .setCachePeriod(0);

      // Adding extra handling for bower if served from /dev/serve/index.html
      registry.addResourceHandler("/dev/bower_components/**")
            .addResourceLocations("classpath:/static/bower_components/")
            .setCachePeriod(0);
   }

   @Override
   public void addViewControllers(ViewControllerRegistry registry) {
      log.info("adding custom view controllers");
      // TODO: Make the forwarding dynamic based on spring profile
      registry.addViewController("/").setViewName("redirect:/dev/serve/index.html");
      registry.addViewController("/dev/serve/")
            .setViewName("forward:/dev/serve/index.html");
      registry.addViewController("/dist/").setViewName("forward:/dist/index.html");
   }
}
