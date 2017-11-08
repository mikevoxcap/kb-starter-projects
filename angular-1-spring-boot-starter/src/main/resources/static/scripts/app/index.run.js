(function() {
  'use strict';

  angular
    .module('angular1SpringBootStarterUi')
    .run(runBlock);

  /** @ngInject */
  function runBlock($log) {
    $log.debug('runBlock end');
  }

})();
