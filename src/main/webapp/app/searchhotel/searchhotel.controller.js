(function() {
    'use strict';

  

     angular
        .module('lowCostTripApp')
        .controller('SearchHotelController', SearchHotelController);
    


       

    SearchHotelController.$inject = ['$scope', 'Principal', 'LoginService','HotelService', '$state'];

    function SearchHotelController ($scope, Principal, LoginService, HotelService, $state) {
        var vm = this;
        initialize();


        vm.hotels=HotelService.getHotelList();


        $scope.chooseCity = function (selected) {
           
            if(typeof selected !== "undefined"){
               console.log(selected); 
               updateMap(selected.originalObject.coord.split(","))
            }
            


        };

        function updateMap($coord){
           var map = new google.maps.Map(document.getElementById('map-canvas'), {
              zoom: 10,
              center: {lat: parseFloat($coord[0]), lng: parseFloat($coord[1])},
              mapTypeId: google.maps.MapTypeId.ROADMAP
            });

            // Add the circle for this city to the map.
              var cityCircle = new google.maps.Circle({
                strokeColor: '#FF0000',
                strokeOpacity: 0.8,
                strokeWeight: 2,
                fillColor: '#FF0000',
                fillOpacity: 0.35,
                map: map,
                center: {lat: parseFloat($coord[0]), lng: parseFloat($coord[1])},
                radius: 10000
              });

        }




          function initialize() {
              var map = new google.maps.Map(document.getElementById('map-canvas'), {
                mapTypeId: google.maps.MapTypeId.ROADMAP,
                zoom: 1,
                center: {lat: 43.2983, lng: 5.3780},
              });
              

              }

            google.maps.event.addDomListener(window, 'load', initialize);

    }





})();
