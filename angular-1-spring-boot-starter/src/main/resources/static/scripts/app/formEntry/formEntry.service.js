(function() {
  'use strict';

  angular
  .module('angular1SpringBootStarterUi')
  .factory('formEntryService', formEntryService);

  /** @ngInject */
  function formEntryService($log,$http,$q,envService, CONFIG) {

    var formEntryUrl;
	if (CONFIG['formEntryUrl']) {
      formEntryUrl	=	CONFIG['formEntryUrl'];
    } else {
      formEntryUrl	=	envService.read('formEntryUrl');
    }
    var formEntryApiUrl = formEntryUrl + '/api';
    var environment = envService.get();
    var localEnv = (environment === 'local');

    var service = {
        getUsername : getUsername
    };

    return service;

    function getUsername() {
    	return 'Test user';
    }
  }
})();
