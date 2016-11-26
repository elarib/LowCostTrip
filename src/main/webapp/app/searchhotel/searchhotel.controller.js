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

          function initialize() {
              var map = new google.maps.Map(document.getElementById('map-canvas'), {
                mapTypeId: google.maps.MapTypeId.ROADMAP
              });
              var defaultBounds = new google.maps.LatLngBounds(
                  new google.maps.LatLng(-33.8902, 151.1759),
                  new google.maps.LatLng(-33.8474, 151.2631));
              map.fitBounds(defaultBounds);

              var input = /** @type {HTMLInputElement} */(document.getElementById('target'));
              var searchBox = new google.maps.places.SearchBox(input);
              var markers = [];

              input.onkeyup = function(e) {
                setTimeout(function() {
                   var places = document.querySelectorAll('.pac-item');
                   for (var i=0;i<places.length;i++) {
                      var place=places[i];
                      var pac_icon = place.querySelector('.pac-icon').outerHTML
                      var place_name_reduced = place.querySelector('.pac-item-query').outerHTML;
                      place.innerHTML = pac_icon+place_name_reduced;
                   }
                }, 150);
              }

              google.maps.event.addListener(searchBox, 'places_changed', function() {
               input.value=input.value.split(',')[0];
                var places = searchBox.getPlaces();
                for (var i = 0, marker; marker = markers[i]; i++) {
                  marker.setMap(null);
                }
                markers = [];
                var bounds = new google.maps.LatLngBounds();
                for (var i = 0, place; place = places[i]; i++) {
                  var image = {
                    url: place.icon,
                    size: new google.maps.Size(71, 71),
                    origin: new google.maps.Point(0, 0),
                    anchor: new google.maps.Point(17, 34),
                    scaledSize: new google.maps.Size(17, 17)
                  };

                  var marker = new google.maps.Marker({
                    map: map,
                    icon: image,
                    title: place.name,
                    position: place.geometry.location
                  });

                  markers.push(marker);

                  bounds.extend(place.geometry.location);
                }
                map.zoom = 17;
                console.log(map);

                map.fitBounds(bounds);
              });

              google.maps.event.addListener(map, 'bounds_changed', function() {
                var bounds = map.getBounds();
                searchBox.setBounds(bounds);
              });

             var button = document.getElementById('button');
              button.onclick = function() {
                input.value='test';
                input.focus();
              }
            }

            google.maps.event.addDomListener(window, 'load', initialize);


    }



})();
