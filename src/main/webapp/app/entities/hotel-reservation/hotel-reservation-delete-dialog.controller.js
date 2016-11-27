(function() {
    'use strict';

    angular
        .module('lowCostTripApp')
        .controller('HotelReservationDeleteController',HotelReservationDeleteController);

    HotelReservationDeleteController.$inject = ['$uibModalInstance', 'entity', 'HotelReservation'];

    function HotelReservationDeleteController($uibModalInstance, entity, HotelReservation) {
        var vm = this;

        vm.hotelReservation = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;
        
        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            HotelReservation.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
