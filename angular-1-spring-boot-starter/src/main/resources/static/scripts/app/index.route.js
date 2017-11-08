(function() {
  'use strict';

  /*
   * Uses ui-router framework
   * See https://github.com/angular-ui/ui-router
   *
   * Routes
   *   formView
   *   f
   *   formApproval
   *   elssHome
   *   elssKitSearch
   */
  angular
    .module('angular1SpringBootStarterUi')
    .config(routerConfig);

  /** @ngInject */
  function routerConfig($stateProvider, $urlRouterProvider) {
    $stateProvider
      .state('home', {
        url: '/',
        templateUrl: 'app/formEntry/formEntry.html',
        controller: 'FormEntryController',
        controllerAs: 'formEntry'
      })
      .state('formEntry', {
        url: '/formEntry/:formNumber',
        templateUrl: 'app/formEntry/formEntry.html',
        controller: 'FormEntryController',
        controllerAs: 'formEntry'
      })
      .state('formView', {
        url: '/formView/:formNumber',
        templateUrl: 'app/formView/formView.html',
        controller: 'FormViewController',
        controllerAs: 'formView'
      });

    $urlRouterProvider.otherwise('/');
  }

})();
