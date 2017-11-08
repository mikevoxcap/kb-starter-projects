(function() {
  'use strict';

  angular
  	.module('angular1SpringBootStarterUi')
  	.controller('navBarController', navBarController);
  
  /** @ngInject */
  function navBarController($filter, $http, $location, $log, $q, $parse,
      $sce, $state, $stateParams, $timeout, $window, ngDialog, 
      formEntryService, envService, moment, CONFIG) {

    var vm = this;

    // Helpful for debugging scope
    vm["_type"] = "navbar";
  }
})();
