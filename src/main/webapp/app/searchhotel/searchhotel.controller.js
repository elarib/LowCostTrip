(function() {
    'use strict';

  

     angular
        .module('lowCostTripApp')
        .controller('SearchHotelController', SearchHotelController);
    


       

    SearchHotelController.$inject = ['$scope', 'Principal', 'LoginService','HotelService', '$state','$http'];

    function SearchHotelController ($scope, Principal, LoginService, HotelService, $state,$http) {
        var vm = this;
        initialize();

        $scope.errorDisplay = true;
        $scope.errors=[];
        $scope.checkInDate=null;
        $scope.checkInDate=null;
        $scope.choosenCity=null;
        $scope.isLoading = false;
        $scope.isLoaded = false;
      //searchHotel(800029889);

        $scope.chooseCity = function (selected) {
           
            if(typeof selected !== "undefined"){
               console.log(selected); 
               $scope.choosenCity = selected.originalObject;
               updateMap(selected.originalObject.coord.split(","))

                updateVars();
                console.log($scope.errors);
                //searchHotel(selected.originalObject.id);


               }
            


        };

        function updateVars(){
           $scope.errors = [
                    {
                    'name':"Check In Date",
                    'state' : $scope.checkInDate==null
                    },
                    {
                    'name':"Check Out Date",
                    'state' : $scope.checkOutDate==null
                    },
                    {
                    'name':"City",
                    'state' : $scope.choosenCity==null
                    }
                ];
        }

        $scope.checkInDate = null;
        $scope.dateOptions = {
            formatYear: 'yy',
            maxDate: new Date(2020, 5, 22),
            minDate: new Date(),
            startingDay: 1
          };

          $scope.$watch("checkInDate", function(v){
            $scope.dateOptions2.minDate = new Date($scope.checkInDate);
            updateVars();
          });
          $scope.$watch("checkOutDate", function(v){
          
            updateVars();
          });

          $scope.dateOptions2 = {
            formatYear: 'yy',
            maxDate: new Date(2020, 5, 22),
            minDate: new Date($scope.checkInDate),
            startingDay: 1
          };

          $scope.altInputFormats = ['M!/d!/yyyy'];

         $scope.open1 = function() {
          $scope.popup1.opened = true;
        };
         $scope.open2 = function() {
          $scope.popup2.opened = true;
        };

         $scope.popup1 = {
            opened: false
          };

          $scope.popup2 = {
            opened: false
          };


        $scope.searchHotel = function(){
           $scope.isLoading = true;
            $http({
              method: 'GET',
              url: "http://localhost:8080/api/searchHotel?cityID="+$scope.choosenCity.id
            }).then(function successCallback(response) {
               console.log(response);
               $scope.isLoading = false;
               vm.hotels=response.data;
              $scope.getWeather();
              }, function errorCallback(response) {
               console.log(response);
              });
        }



        $scope.getWeather = function()
        {
          $http({
              method: 'GET',
              //url: "http://localhost:8080/api/getWeather?coord=43.2983,5.3780"
              url: "http://localhost:8080/api/getWeather?coord="+$scope.choosenCity.coord
            }).then(function successCallback(response) {
               console.log(response);
               $scope.isLoaded = true;
               $scope.weather=response.data;
              }, function errorCallback(response) {
               console.log(response);
              });
        }


        $scope.reserver = function(hotel){


          console.log(hotel);
        }

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
