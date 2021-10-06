
(function (){

 var app = angular.module("AWS-App");
 app.controller("loginController",loginController);

 function loginController(loginService,$window,jwtHelper,$http){


 	var self = this;
 	this.loginData = {};
 	self.successMesg = undefined;
    self.errorMesg = undefined;
    self.loginMode = "false";
    self.data = undefined ;

    this.toggleLoginMode = function(){
  	this.loginMode = !this.loginMode;

  	}


 	this.loginUser = function(){
 		//this.toggleLoginMode();
 		self.loginData.inputEmail = self.userEmail;
 		self.loginData.inputPassword	=self.userPassword;
 		
 		loginService.loginUser(self.loginData)
	  	 .then(function (response){
	  	 	console.log("status:: and x is " + response.data.status);
	  	 	var x = response.data.status;
	  	 	if (x) {
	  	 	self.successMesg = response.data.message;
	  	 	//self.data = response.data.data;	
	  	 	 var tokenPayload = jwtHelper.decodeToken(response.data.data);


	  	 	$window.sessionStorage.accessToken = response.data.data;
	  	 	 $window.sessionStorage.userId  =tokenPayload.role;

	  	 	console.log("data is :: "+$window.sessionStorage.accessToken);
	  	 	console.log("user id  is :: "+$window.sessionStorage.userId);

	  	 	alert("data is :: "+$window.sessionStorage.accessToken);
	  	 //	 $http.defaults.headers.common.Authorization = 'Bearer ' + response.data.data;
	  	 	 $window.location.assign('./dashboard.html');
	  	 	}
	  	 	else{
	  	 	self.errorMesg = response.data.message;	
	  	 	}

	  	 } , function (){
	  	 	
	  	 });

 	}
 }
})();