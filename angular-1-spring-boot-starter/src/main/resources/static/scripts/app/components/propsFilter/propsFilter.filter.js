(function() {
  'use strict';

  angular
    .module('angular1SpringBootStarterUi')
    .filter('propsFilter', propsFilter);

  /** @ngInject */
  function propsFilter() {
    return function(items, props) {
      var out = [];
  
      if (angular.isArray(items)) {
        items.forEach(function(item) {
          var itemMatches = false;
  
          var keys = Object.keys(props);
          for (var i = 0; i < keys.length; i++) {
            var prop = keys[i];
            var text = props[prop].toLowerCase();
            
            var itemprop = item[prop]
            if (angular.isDefined(itemprop) && (itemprop != null)) {
              if (angular.isArray(itemprop)) {
                for (var j = 0; j < itemprop.length; j++) {
                  if (   (itemprop[j] != null)
                      && (itemprop[j].toString().toLowerCase().indexOf(text) !== -1)) {
                    itemMatches = true;
                    break;
                  }
                }
                if (itemMatches) {
                  break; 
                }
              } else {
                if (itemprop.toString().toLowerCase().indexOf(text) !== -1) {
                  itemMatches = true;
                  break;
                }
              }
            }
          }
  
          if (itemMatches) {
            out.push(item);
          }
        });
      } else {
        // Let the output be the input untouched
        out = items;
      }
  
      return out;
    }
  }
})();
