package com.michaelhoffmantech.angular1springbootstarter.web.rest;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.michaelhoffmantech.angular1springbootstarter.config.ApplicationProperties;

/**
 * Endpoint for retrieving properties instead of the /env endpoint
 */
@RestController
@RequestMapping("/api")
public class PropertyResource {
   @Autowired
   private ApplicationProperties applicationProperties;

   /**
    * GET /webapp/props -> get webapp properties
    */
   @RequestMapping(value = "/webapp/props", method = RequestMethod.GET,
         produces = MediaType.APPLICATION_JSON_VALUE)
   public Map<String, String> getWebappProperties() {
      Map<String, String> webappProps = new HashMap<String, String>();
      webappProps.put("mainUrl", applicationProperties.getServices().getMainUrl());
      return webappProps;
   }

}
