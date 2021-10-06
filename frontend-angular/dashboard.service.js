(function (){

 var app = angular.module("AWS-App");
 app.service("dashboardService",function($http,$window){

 	var self =this;

 	self.uploadFileToUrl = function(ipfile){

 		console.log("file uploaded : :" + ipfile.name);
 		$http.defaults.headers.common.Authorization =  $window.sessionStorage.accessToken;
 		//$http.defaults.headers.common.Content-Type =  "undefined";
 		 var fd = new FormData();
        fd.append('file', ipfile);
 		 var request = {
                    method: 'POST',
                    url: "http://35.154.225.58:8080/dashboard/user/"+$window.sessionStorage.userId+"/aws/upload",
                    data: fd,
                    headers: {
                       //  'Content-Type': 'multipart/form-data'

                        'Content-Type': undefined
                    }
                };
              return $http(request);  
	//  	return  $http.post("http://192.168.0.29:8080/dashboard/user/"+$window.sessionStorage.userId+"/aws/upload",ipfile);
		
 	}

 	self.transform = function(imgNames,selectedType){
 		console.log("selectedType ::"+selectedType);
 		 $http.defaults.headers.common.Authorization =  $window.sessionStorage.accessToken;
	 	//console.log("hii" + $window.sessionStorage.accessToken);
	 	var tObj = {
	 		"operationType":selectedType,
	 		"imgNames": imgNames
	 	};
	 	console.log("the ibject is :: "+tObj);
		return  $http.post("http://35.154.225.58:8080/dashboard/user/"+$window.sessionStorage.userId+"/aws/sendjob",tObj);
			
	}


 	self.viewTransform = function(jobId){
 		 		console.log("jobId is::"+jobId);

 		 		console.log("userid is sdfsd ::"+$window.sessionStorage.userId);
 		 $http.defaults.headers.common.Authorization =  $window.sessionStorage.accessToken;
	  	 	console.log("hii" + $window.sessionStorage.accessToken);
		return  $http.get("http://35.154.225.58:8080/dashboard/user/"+$window.sessionStorage.userId+"/aws/getjob/"+jobId);
			
		}

 	self.dashboardUser = function(){

 		 		console.log("userid is sdfsd ::"+$window.sessionStorage.userId);
 		 $http.defaults.headers.common.Authorization =  $window.sessionStorage.accessToken;
	  	 	console.log("hii" + $window.sessionStorage.accessToken);
		return  $http.get("http://35.154.225.58:8080/dashboard/user/"+$window.sessionStorage.userId+"/aws/allimages");
			
		}

	self.dashboardDeleteImage = function(imgId){

 		 		console.log("userid is sdfsd ::"+$window.sessionStorage.userId);
 		 $http.defaults.headers.common.Authorization =  $window.sessionStorage.accessToken;
	  	 	console.log("hii" + $window.sessionStorage.accessToken);
		return  $http.get("http://35.154.225.58:8080/dashboard/user/"+$window.sessionStorage.userId+"/aws/delete/"+imgId);
			
		}



 });

})();