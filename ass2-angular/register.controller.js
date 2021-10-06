
(function (){

 var app = angular.module("AWS-App");
 app.controller("registerController",registerController);

 function registerController(registerService,$window){


 	var self = this;
 	this.registerData = {};
 	self.successMesg = undefined;
    self.errorMesg = undefined;

 	this.registerUser = function(){
 		
 		self.registerData.firstName = self.firstName;
 		self.registerData.lastName	=self.lastName;
 		self.registerData.email = self.email;
 		self.registerData.password	=self.password;
 		
 		registerService.addUser(self.registerData)
	  	 .then(function (response){
	  	 	console.log("status:: and x is " + response.data.status);
	  	 	var x = response.data.status;
	  	 	if (x) {
	  	 	self.successMesg = response.data.message;	
	  	 	 $window.location.assign('./login.html');
	  	 	
	  	 	}
	  	 	else{
	  	 	self.errorMesg = response.data.message;	
	  	 	}

	  	 } , function (){
	  	 	
	  	 });

 	}
 }
})();