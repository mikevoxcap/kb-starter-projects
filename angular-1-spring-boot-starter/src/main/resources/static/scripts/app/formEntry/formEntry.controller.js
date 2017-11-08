(function() {
  'use strict';

  angular
    .module('angular1SpringBootStarterUi')
    .controller('FormEntryController', FormEntryController);

  /** @ngInject */
  function FormEntryController(
      $filter, $http, $location, $log, $q, $parse, $sce, $state, $stateParams,
      $timeout, $window, ngDialog, formEntryService, envService, CONFIG) {
	  var vm = this;

    vm.formNumber = ''; 
    vm.navbarEnabled = false; 
    vm.navBarLine1 = ""; 
    vm.navBarLine2 = "";
    vm.navBarLine3 = "";
    vm.userName = "";    
    
    activate();

    function activate() {
      // Start by initializing the page configs and processing the URL
      var environment = envService.get();
      vm.isLocal = ('local' === environment);
      vm.isUat = ('uat' === environment);
      vm.isProd = ('production' === environment);
      $log.debug('ENV: ', envService.get());
      $log.debug('Is Local: ', vm.isLocal);
      $log.debug('Is UAT: ', vm.isUat);
      $log.debug('Is Production: ', vm.isProd);
      
      if (CONFIG['formEntryUrl']) {
        formEntryApiUrl = CONFIG['formEntryUrl'] + '/api';
      } else {
        formEntryApiUrl = envService.read('formEntryUrl') + '/api';
      }
      $log.debug('Form Entry API URL: ', formEntryApiUrl);
      
      var search = $location.search();
      vm.navbarEnabled = angular.isDefined(search.navbar) ? (search.navbar === "" || search.navbar == 1) : true;
      $log.debug('Navbar Enabled: ', vm.navbarEnabled);
      
      vm.navBarLine1 = 'This is line 1';
      vm.navBarLine2 = 'This is line 2';
      vm.navBarLine3 = 'This is line 3';
      vm.userName = formEntryService.getUsername)();
      
      vm.formNumber = $stateParams.formNumber;
      $log.debug('Form number: ', vm.formNumber); 
    }
  }
})();
