(function() {
    'use strict';

    angular
        .module('lowCostTripApp')
        .controller('HotelReservationDialogController', HotelReservationDialogController);

    HotelReservationDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'HotelReservation','User'];

    function HotelReservationDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, HotelReservation, User) {
        var vm = this;

        vm.hotelReservation = entity;
        vm.clear = clear;
        vm.datePickerOpenStatus = {};
        vm.openCalendar = openCalendar;
        vm.save = save;
        vm.users = User.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {

            vm.hotelReservation.user = vm.users[vm.users.length - 1];
            vm.isSaving = true;
            console.log(vm.hotelReservation);
            if (vm.hotelReservation.id !== null) {
                HotelReservation.update(vm.hotelReservation, onSaveSuccess, onSaveError);
            } else {
                HotelReservation.save(vm.hotelReservation, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('lowCostTripApp:hotelReservationUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }

        vm.datePickerOpenStatus.checkIn = false;
        vm.datePickerOpenStatus.checkOut = false;

        function openCalendar (date) {
            vm.datePickerOpenStatus[date] = true;
        }
    }
})();
