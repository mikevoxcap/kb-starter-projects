(function() {
  'use strict';

  angular
    .module('angular1SpringBootStarterUi')
    .directive('navbar', navbar);

  /** @ngInject */
  function navbar() {
    var directive = {
      restrict: 'E',
      templateUrl: 'app/components/navbar/navbar.html',
      scope: {
          creationDate: '=',
          userName: '@',
          navLine1: '@',
          navLine2: '@',
          navLine3: '@'
      },
      controller: 'navBarController',
      controllerAs: 'vm',
      bindToController: true
    };

    return directive;
  }

})();
