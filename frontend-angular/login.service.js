(function (){

 var app = angular.module("AWS-App");
 app.service("loginService",function($http,jwtHelper){

 	var self =this;

 	self.loginUser = function(loginData){

		return  $http.post("http://35.154.225.58:8080/authenticate",loginData);
			
		}

 });

})();