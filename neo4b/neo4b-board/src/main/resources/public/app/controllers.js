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
    ['$scope', '$mdSidenav', '$location', '$mdBottomSheet', '$window', '$mdDialog', 'ToastServices', 'BoardServices',
     function($scope, $mdSidenav, $location, $mdBottomSheet, $window, $mdDialog, ToastServices, BoardServices){
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
    ['$scope', '$mdDialog', '$mdToast', 'ToastServices', 'RestBoards', '$log', function($scope, $mdDialog, $mdToast, toast, RestBoards, $log){
        RestBoards.findAll({}, function(data) {
            $scope.working.boards = data;
            toast.display(data.length + " board(s)");
        }, function(failure) {
            toast.display(failure, true);
        });

        /**
         * delete current selected board
         */
        $scope.delete = function(board) {
            RestBoards.delete({id:board.id}, function(data) {
                toast.display("Board " + data.id + " deleted");
                /**
                 * delete on client side
                 */
                var index = $scope.working.boards.indexOf(board);
                $scope.working.boards.splice(board, 1); 
            }, function(failure) {
                toast.display(failure, true);
            });
        }
        
        /**
         * dialog for item creation
         */
        $scope.showCreateBoard = function() {
            // Default values
            $scope.createBoard = {
                values : {
                    name:"default name",
                    description:"default description"
                }
            };
            // Dialog creation
            $mdDialog.show({
              scope: $scope,                                            // use parent scope in template
              preserveScope: true,
              controller: function($scope, $mdDialog) {
                  $scope.hide = function() {
                    $mdDialog.hide();
                  };
                  $scope.cancel = function() {
                    $mdDialog.cancel();
                  };
                  $scope.validate = function(validated) {
                      if(!validated) {
                          $scope.createBoard = undefined;
                          $mdDialog.cancel();
                      } else {
                          $mdDialog.hide();
                      }
                  };
              },
              templateUrl: 'partials/dialog/board.html',
              parent: angular.element(document.body),
              clickOutsideToClose:true
            })
            .then(function(answer) {
              $log.debug('Accept:', $scope.createBoard.values);
              $scope.create($scope.createBoard.values);
            }, function() {
              $log.debug('You cancelled the creation');
            });
        };

        /**
         * create current selected board
         */
        $scope.create = function(board) {
            // Create new entity
            RestBoards.create(board, function(data) {
                /**
                 * create on client side
                 */
                $scope.working.boards.push(data);
                toast.display(data.name);
                $log.debug('Push new board', data);
            }, function(failure) {
                toast.display("Unable to create new board",true);
            });
        }

        /**
         * select a single ne page and route browser on it
         */
        $scope.select = function(board) {
            RestBoards.board({id:board.id}, function(data) {
                toast.display(data.name);
                $scope.location("/boards/" + board.id);
            }, function(failure) {
                toast.display(failure, true);
            });
        }
    }])
;
