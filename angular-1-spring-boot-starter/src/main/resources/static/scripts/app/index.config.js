(function() {
  'use strict';

  angular
    .module('angular1SpringBootStarterUi')
    .config(['uiMask.ConfigProvider', function(uiMaskConfigProvider) {
      uiMaskConfigProvider.addDefaultPlaceholder(false);

  }])
    .config(function(envServiceProvider) {
        // set the domains and variables for each environment
      envServiceProvider.config({
            domains: {
              local: ['localhost','127.0.0.1', 'test.local','dev.local'],
              development: ['localhost', '127.0.0.1', 'test.local', 'dev.local']
            },
            vars: {
                development: {
                  mainUrl: 'http://localhost:9002/spring-boot-service-starter', 
                },
                local: {
                  mainUrl: 'http://localhost:9002/spring-boot-service-starter',
                }
            }
        });

        // run the environment check, so the comprobation is made
        // before controllers and services are built
        envServiceProvider.check();

    });
})();
