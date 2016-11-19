(function() {
    'use strict';
    angular
        .module('lowCostTripApp')
        .factory('HotelReservation', HotelReservation);

    HotelReservation.$inject = ['$resource', 'DateUtils'];

    function HotelReservation ($resource, DateUtils) {
        var resourceUrl =  'api/hotel-reservations/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                        data.checkIn = DateUtils.convertLocalDateFromServer(data.checkIn);
                        data.checkOut = DateUtils.convertLocalDateFromServer(data.checkOut);
                    }
                    return data;
                }
            },
            'update': {
                method: 'PUT',
                transformRequest: function (data) {
                    var copy = angular.copy(data);
                    copy.checkIn = DateUtils.convertLocalDateToServer(copy.checkIn);
                    copy.checkOut = DateUtils.convertLocalDateToServer(copy.checkOut);
                    return angular.toJson(copy);
                }
            },
            'save': {
                method: 'POST',
                transformRequest: function (data) {
                    var copy = angular.copy(data);
                    copy.checkIn = DateUtils.convertLocalDateToServer(copy.checkIn);
                    copy.checkOut = DateUtils.convertLocalDateToServer(copy.checkOut);
                    return angular.toJson(copy);
                }
            }
        });
    }
})();
