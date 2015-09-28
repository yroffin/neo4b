/*
 * Copyright 2015 Yannick Roffin.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

'use strict';

/* Controllers */

angular.module('BoardApp',['ngMaterial', 'ngMdIcons', 'ngRoute', 'ngSanitize', 'BoardApp.services', 'BoardApp.routes'])
    /**
     * main controller
     */
    .controller('RootCtrl',
    ['$scope', '$mdSidenav', '$location', '$mdBottomSheet', '$window', '$mdDialog', 'BoardServices',
     function($scope, $mdSidenav, $location, $mdBottomSheet, $window, $mdDialog, BoardServices){
        /**
         * initialize configuration
         */
        $scope.toastPosition = {
            bottom: false,
            top: true,
            left: true,
            right: false
        }

        /**
         * internal properties
         */
        if($scope.working === undefined) $scope.working = {};
         
        /**
         * default navbar position
         */
        $scope.menuId = "left";

        /**
         * find toast position
         */
        $scope.getToastPosition = function() {
            return Object.keys($scope.toastPosition)
                .filter(function(pos) { return $scope.toastPosition[pos]; })
                .join(' ');
        }

        /**
         * toggle navbar
         * @param menuId
         */
        $scope.toggleSideNav = function() {
            $mdSidenav($scope.menuId).toggle();
        }

        /**
         * load configuration
         */
        $scope.location = function(target) {
            $mdSidenav($scope.menuId).close();
            if(target != undefined) {
                $location.path(target);
            }
        }
    }])
    .controller('MenuCtrl',
    ['$scope', 'BoardServices',
     function($scope, BoardServices){
        /**
         * load left menu
         */
        $scope.working.menu = BoardServices.getMenu();
        console.info($scope.working.menu);
    }])
    /**
     * home controller
     */
    .controller('BoardsHomeCtrl',
    ['$scope', '$mdSidenav', '$location', '$mdBottomSheet', '$window', '$mdDialog',
     function($scope, $mdSidenav, $location, $mdBottomSheet, $window, $mdDialog){
    }])
    /**
     * posts controller
     */
    .controller('BoardsCtrl',
    ['$scope', '$mdToast', 'RestBoards', function($scope, $mdToast, RestBoards){
        RestBoards.findAll({}, function(data) {
            $scope.working.boards = data;
            console.info(data);
            $mdToast.show(
                $mdToast.simple()
                    .content(data.length + " board(s)")
                    .position($scope.getToastPosition())
                    .hideDelay(3000)
            );
        }, function(failure) {
            $mdToast.show(
                $mdToast.simple()
                    .content(failure)
                    .position($scope.getToastPosition())
                    .hideDelay(3000).theme("failure-toast")
            );
        });

        /**
         * select a single ne page and route browser on it
         */
        $scope.delete = function(board) {
            RestBoards.delete({id:board.id}, function(data) {
                $mdToast.show(
                    $mdToast.simple()
                        .content(board.title)
                        .position($scope.getToastPosition())
                        .hideDelay(3000)
                );
                /**
                 * delete on client side
                 */
                var index = $scope.working.boards.indexOf(board);
                $scope.working.boards.splice(board, 1); 
            }, function(failure) {
                $mdToast.show(
                    $mdToast.simple()
                        .content(failure)
                        .position($scope.getToastPosition())
                        .hideDelay(3000).theme("failure-toast")
                );
            });
        }

        /**
         * select a single ne page and route browser on it
         */
        $scope.select = function(board) {
            RestBoards.board({id:board.id}, function(data) {
                $mdToast.show(
                    $mdToast.simple()
                        .content(board.title)
                        .position($scope.getToastPosition())
                        .hideDelay(3000)
                );
                $scope.location("/boards/" + board.id);
            }, function(failure) {
                $mdToast.show(
                    $mdToast.simple()
                        .content(failure)
                        .position($scope.getToastPosition())
                        .hideDelay(3000).theme("failure-toast")
                );
            });
        }
    }])
;
