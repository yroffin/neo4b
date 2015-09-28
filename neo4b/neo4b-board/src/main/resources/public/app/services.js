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

/* Services */

/**
 * restApiUrl must contain a valid url rest api
 */
var myAppServices = angular.module('BoardApp.services', [ 'ngResource' ]);
var restApiUrl = '/api';

/**
 * posts services
 */
myAppServices.factory('RestBoards', [ '$resource', function($resource, $windows) {
	return $resource('', {}, {
			/**
			 * get boards collection
			 */
			findAll: {
				method: 'GET',
				url: restApiUrl + '/boardManagement/v1/boards',
				params: {},
				isArray: true,
				cache: false
			},
			/**
			 * get single board by id
			 */
			get: {
				method: 'GET',
				url: restApiUrl + '/boardManagement/v1/boards/:id',
				isArray: false,
				cache: false
			},
			/**
			 * get single board by id
			 */
			delete: {
				method: 'DELETE',
				url: restApiUrl + '/boardManagement/v1/boards/:id',
				isArray: false,
				cache: false
			},
			/**
			 * create single board
			 */
			create: {
				method: 'POST',
				url: restApiUrl + '/boardManagement/v1/boards',
				isArray: false,
				cache: false
			}
		}
	)}]);

/**
 * BoardServices
 */
myAppServices.factory('BoardServices', function() {
  var services = {
      getMenu: function() {
        return [
            {
                name: 'Boards',
                icon: 'mail',
                location: '/boards'
            }
        ];
      }
  };
  return services;
});
