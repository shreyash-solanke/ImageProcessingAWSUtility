(function (){

 var app = angular.module("AWS-App");
 app.service("registerService",function($http){

 	var self =this;

 	self.addUser = function(registerData){

		return  $http.post("http://35.154.225.58:8080/register",registerData)
			
		}

 });

})();