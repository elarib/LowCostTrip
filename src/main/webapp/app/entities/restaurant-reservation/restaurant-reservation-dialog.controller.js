(function() {
    'use strict';

    angular
        .module('lowCostTripApp')
        .controller('RestaurantReservationDialogController', RestaurantReservationDialogController);

    RestaurantReservationDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'RestaurantReservation', 'User'];

    function RestaurantReservationDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, RestaurantReservation, User) {
        var vm = this;

        vm.restaurantReservation = entity;
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
            vm.isSaving = true;
            vm.hotelReservation.user = vm.users[vm.users.length - 1];
            console.log(vm.restaurantReservation);
            if (vm.restaurantReservation.id !== null) {
                RestaurantReservation.update(vm.restaurantReservation, onSaveSuccess, onSaveError);
            } else {
                RestaurantReservation.save(vm.restaurantReservation, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('lowCostTripApp:restaurantReservationUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }

        vm.datePickerOpenStatus.date = false;

        function openCalendar (date) {
            vm.datePickerOpenStatus[date] = true;
        }
    }
})();
