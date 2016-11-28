(function() {
    'use strict';

  

     angular
        .module('lowCostTripApp')
        .controller('SearchRestoController', SearchRestoController);
    


       

    SearchRestoController.$inject = ['$scope', 'Principal', 'LoginService','HotelService', '$state','$http', 'HotelReservation', 'User','$stateParams'];

    function SearchRestoController ($scope, Principal, LoginService, HotelService, $state,$http,  HotelReservation, User, $stateParams) {
        var vm = this;
        initialize();
        
      $scope.reservedDone = false;
      $scope.isLoading = true;


      $scope.coord = $stateParams.coord;
      $scope.coords = $scope.coord.split(",");

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
