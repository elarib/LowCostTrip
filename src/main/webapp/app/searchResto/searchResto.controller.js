(function() {
    'use strict';

  

     angular
        .module('lowCostTripApp')
        .controller('SearchRestoController', SearchRestoController);
    


       
angular.module('lowCostTripApp').controller('modalController', ['$scope','$rootScope', 'uiGmapGoogleMapApi', '$document' ,'uiGmapIsReady', '$timeout', function($scope, $rootScope,uiGmapGoogleMapApi,$document, uiGmapIsReady, $timeout) {
    
  console.log(uiGmapGoogleMapApi);
  var self = this;

   $scope.map = { 
    control: {},
    center: {
      latitude: $rootScope.coords[0],
      longitude: $rootScope.coords[1]
    },
    zoom: 9,
    options: {
      scrollwheel: false,
      panControl: false,
      scaleControl: false,
      draggable: true,
      maxZoom: 22,
      minZoom: 0
    },
    clusterOptions: {},
    clusterEvents: {},
    refresh : false,
    bounds: {},
    events: { 
      idle: function() {
        console.log('idle');
      }
    },
  };

  uiGmapGoogleMapApi.then(function(maps) {
    console.log('start');
    $scope.directionsDisplay = new maps.DirectionsRenderer();

      uiGmapIsReady.promise(1).then(function(instances) {
      directions(maps, $rootScope.coords[0], $rootScope.coords[1]);
        $timeout(function() {
          directions(maps, $rootScope.restoCoord[0], $rootScope.restoCoord[1]);
        }, 1000)
      });
  }); 

  function directions(maps, newLat, newLong) {
      console.log('getting directions');
      var directionsService = new maps.DirectionsService();
      $scope.directionsDisplay.setMap($scope.map.control.getGMap());

      var origin = $scope.map.center.latitude + ", " + $scope.map.center.longitude;

      var request = {
          origin: origin,
          destination: newLat + ", " + newLong,
          travelMode: maps.TravelMode['DRIVING'],
          optimizeWaypoints: true
      };

      directionsService.route(request, function (response, status) {
          console.log('directions found');
          if (status === google.maps.DirectionsStatus.OK) {
              $scope.directionsDisplay.setDirections(response);
          } else {
              console.log('Directions request failed due to ' + status);
          }
      });

  }




}]). 
  config(['uiGmapGoogleMapApiProvider', function (GoogleMapApiProvider) {
    GoogleMapApiProvider.configure({
          key: 'AIzaSyATB7ZMCBSPtpaJH_s6dQDHRVKlPfDqx8U',
          v: '3.17',
          libraries: 'weather,geometry,visualization,places'
      });
  }]);;



    SearchRestoController.$inject = ['$scope', 'Principal', 'LoginService','HotelService', '$state','$http', 'HotelReservation', 'User','$stateParams', '$uibModal', '$rootScope'];

    function SearchRestoController ($scope, Principal, LoginService, HotelService, $state,$http,  HotelReservation, User, $stateParams, $uibModal,  $rootScope) {
        var vm = this;
        initialize();
        
      $scope.reservedDone = false;
      $scope.isLoading = true;


      $scope.coord = $stateParams.coord;
      $scope.coord = $scope.coord;
      $rootScope.coords = $scope.coord.split(",");


       $scope.map = new google.maps.Map(document.getElementById('map-canvas'), {
              zoom: 14,
              center: {lat: parseFloat($scope.coords[0]), lng: parseFloat($scope.coords[1])},
              mapTypeId: google.maps.MapTypeId.ROADMAP
      });

        $scope.checkInDate = null;
        $scope.dateOptions = {
            formatYear: 'yy',
            maxDate: new Date(2020, 5, 22),
            minDate: new Date(),
            startingDay: 1
          };

          $scope.$watch("checkInDate", function(v){
            $scope.dateOptions2.minDate = new Date($scope.checkInDate);
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

     
        $scope.SearchResto = function(){
          addMarkerHotel();
           $scope.isLoading = true;
            $http({
              method: 'GET',
              url: "http://localhost:8080/api/searchResto?coord="+$scope.coord
            }).then(function successCallback(response) {
               
               $scope.isLoading = false;
               vm.restos=response.data.results;

               
              angular.forEach(vm.restos, function(value, key) {

                var loc = { lat: parseFloat(value.lat), lng: parseFloat(value.lng) };
                addMarker(loc,key+"" , value.name, value.vicinity);
              });
              }, function errorCallback(response) {
               console.log(response);
              });
        }
        $scope.SearchResto();

        function SearchResto(id){
           $scope.isLoading = true;
            $http({
              method: 'GET',
              url: "http://localhost:8080/api/SearchResto?cityID="+id
            }).then(function successCallback(response) {
               console.log(response);
               $scope.isLoading = false;
               

              }, function errorCallback(response) {
               console.log(response);
              });
        }



        vm.users = User.query();

        $scope.reserver = function(hotel){


          var hotelReserv = {
            'checkIn' : $scope.checkInDate ,
            'checkOut' : $scope.checkOutDate,
            'id' : null,
            'idHotel': hotel.id,
            'pricePerNight' : hotel.price,
            'user' : vm.users[vm.users.length - 1]
          }
          console.log(hotelReserv);

          HotelReservation.save(hotelReserv,function(result){
            console.log(result);
            $scope.hotelName = hotel.name;
            $scope.reservedDone = true;

          }, function(result){
            console.log(result);
          })
        }



        $scope.modalController =function(){
          console.log("Cons");
        }

        $scope.checkIterinaire = function(r){
          console.log(r);
          $rootScope.restoCoord=[
            parseFloat(r.lat),
            parseFloat(r.lng)
          ]
          $uibModal.open({
                  templateUrl: 'app/searchResto/directions.html',
                  controller: 'modalController',
                  controllerAs: 'vm',
                  size: 'lg'
              })
              .result.then(function() {

              }, function() {
              });
        }


     
        // Adds a marker to the map.
      function addMarker(location, number, name, address) {
              var marker = new google.maps.Marker({
                position: location,
                label: number,
                map: $scope.map
              });
              var contentString = '<div><p>Restraurant : '+name+' </p><p>Address : '+address+'</p></div>';
              var infowindow = new google.maps.InfoWindow({
                content: contentString
              });

              marker.addListener('click', function() {
                infowindow.open($scope.map, marker);
              });
      }

      function addMarkerHotel() {
             var image = 'https://developers.google.com/maps/documentation/javascript/examples/full/images/beachflag.png';
              var marker = new google.maps.Marker({
                position: { lat: parseFloat($scope.coords[0]), lng: parseFloat($scope.coords[1]) },
                icon: image,
                map: $scope.map
              });
               var infowindow = new google.maps.InfoWindow({
                content: "My Hotel"
              });

              marker.addListener('click', function() {
                infowindow.open($scope.map, marker);
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
