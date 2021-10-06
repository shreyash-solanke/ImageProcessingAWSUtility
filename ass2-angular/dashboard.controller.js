
(function (){

 var app = angular.module("AWS-App");
 app.controller("dashboardController",dashboardController);

 function dashboardController(dashboardService,$window){


 	var self = this;
 	this.loginData = {};
 	self.successMesg = undefined;
    self.errorMesg = undefined;
    self.dashboardMode = "false";
    self.data = undefined ;
    self.transformTypes = ["Gray", "Crop", "Resize"];
    self.imgAdded = [];
    self.selectedType = undefined;
    self.imgNames=[];
    self.viewMesg = undefined;
    self.viewMesg1 = undefined;
    self.addMesg = [];
    self.jobId = undefined;
   // self.xx;
   // self.myFile = undefined;
    //self.userId = $window.sessionStorage.userId;


    this.toggledashboardMode = function(){
  	this.dashboardMode = !this.dashboardMode;

  	}

  	this.transform = function(){

//  		self.imgId = self.selImg.userimages.imgId
			
  		dashboardService.transform(self.imgNames,self.selectedType)
  			.then(function(response){
  			var x = response.data.status;
	  	 	if (x) {
	  	 	self.successMesg = "Image added successfully for transformation will notify you through mail";
	  	 	self.jobId = response.data.data;	
	  	 //	console.log("data is "+self.data);
	  	 
	  	 	}
	  	 	else{
	  	 	self.errorMesg = response.data.message;	
	  	 	}

	  	 } , function (){
	  	 	
	  	 });

  		self.addMesg = [];
		self.selectedType = undefined;
  				

  	}

  	this.dashboardDeleteImage = function(imgId){

//  		self.imgId = self.selImg.userimages.imgId

  		dashboardService.dashboardDeleteImage(imgId)
  			.then(function(response){
  			var x = response.data.status;
	  	 	if (x) {
	  	 	self.successMesg = "Image deleted successfully click view Images to see changes";
	  	// 	self.data = response.data.data;	
	  	 //	console.log("data is "+self.data);
	  	 
	  	 	}
	  	 	else{
	  	 	self.errorMesg = response.data.message;	
	  	 	}

	  	 } , function (){
	  	 	
	  	 });

  	}


  	this.dashboardAddImage = function(imgId){

//  		self.imgId = self.selImg.userimages.imgId
			var idx = imgId;
			console.log("image added for transformatiion :: " + imgId);
			self.imgAdded.push(imgId);
			console.log("array : " + self.imgAdded);
			console.log("array : " + self.Images);
			//console.log("array : " + self.Images[4].userimages.imgId);
			for (var i=0; i<7; i++) {
     			 if(self.Images[i].userimages.imgId == idx){
					self.imgNames.push(self.Images[i].userimages.imgName);
					self.addMesg[i] = "imageAdded";

     			 }
    			}
			console.log("imgNames :: "+self.imgNames)
			
  	}

  	this.selectImg = function(index){

    self.selImg = this.Images[index];
    console.log("selected img id :: "+ self.selImg.userimages.imgId);

   }

   this.hello = function(){
   	console.log("HELLLO Sankujain");
   	self.successMesg = undefined;
   	self.errorMesg = undefined;

   }

   this.upload = function(){
   	alert("hiii");
   	var ipfile = self.myFile;
   	console.log("file is :: " + ipfile.name);
   	console.dir(ipfile);

		   	 //var uploadUrl = "/fileUpload";
           dashboardService.uploadFileToUrl(ipfile)
      		.then(function (response){
	  	 	console.log("status:: and x is " + response.data.status);
	  	 	var x = response.data.status;
	  	 	if (x) {
	  	 	self.successMesg = "image uploaded successfully click on view Image to see";
	  	 	//self.data = response.data.data;	
	  	 	//self.Images = response.data.data;
	  	 //	console.log("data is "+self.data);
	  	 //	console.log("loll"+self.data[0].imgLink);
	  	 	}
	  	 	else{
	  	 	self.errorMesg = response.data.message;	
	  	 	}

	  	 } , function (){
	  	 	
	  	 });      
   }

    this.viewTransform = function(){
 		//this.toggleLoginMode();
 		console.log("userid is ::"+$window.sessionStorage.userId);
 		dashboardService.viewTransform(self.jobId)
	  	 .then(function (response){
	  	 	console.log("status:: and x is " + response.data.status);
	  	 	var x = response.data.status;
	  	 	if (x) {
	  	 	self.successMesg = response.data.message;
	  	 	//self.data = response.data.data;	
	  	 	self.ImagesTrans = response.data.data;
	  	 	//console.log("data is "+self.data);
	  	 	//console.log("loll"+self.data[0].imgLink);
	  	 	self.viewMesg = "now show";
	  	 	}
	  	 	else{
	  	 	self.errorMesg = response.data.message;	
	  	 	}

	  	 } , function (){
	  	 	
	  	 });
	  

 	}


 	this.dashboardUser = function(){
 		//this.toggleLoginMode();
 		console.log("userid is ::"+$window.sessionStorage.userId);
 		dashboardService.dashboardUser()
	  	 .then(function (response){
	  	 	console.log("status:: and x is " + response.data.status);
	  	 	var x = response.data.status;
	  	 	if (x) {
	  	 	self.successMesg = response.data.message;
	  	 	//self.data = response.data.data;	
	  	 	self.Images = response.data.data;
	  	 	//console.log("data is "+self.data);
	  	 	//console.log("loll"+self.data[0].imgLink);
	  	 	self.viewMesg1 = "now show";
	  	 	}
	  	 	else{
	  	 	self.errorMesg = response.data.message;	
	  	 	}

	  	 } , function (){
	  	 	
	  	 });

 	}
 }
})();