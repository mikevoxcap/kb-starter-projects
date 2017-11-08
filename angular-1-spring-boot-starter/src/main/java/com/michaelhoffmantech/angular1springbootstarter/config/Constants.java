package com.michaelhoffmantech.angular1springbootstarter.config;

/**
 * Application constants.
 */
public final class Constants {

   public static final String SPRING_PROFILE_LOCAL = "local";
   public static final String SPRING_PROFILE_DOCKER = "docker";

   public static final String SPRING_PROFILE_PRODUCTION = "prod";
   public static final String SPRING_PROFILE_FAST = "fast";
   // Spring profile used when deploying with Spring Cloud (used when deploying
   // to CloudFoundry)
   public static final String SPRING_PROFILE_CLOUD = "cloud";
   // Spring profile used when deploying to Heroku
   public static final String SPRING_PROFILE_HEROKU = "heroku";

   public static final String SYSTEM_ACCOUNT = "system";

   private Constants() {
   }
}
