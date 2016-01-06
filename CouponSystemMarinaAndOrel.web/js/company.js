var CouponSystemCompanyApp = angular.module("CouponSystemCompanyApp", []);

CouponSystemCompanyApp.controller("CouponSystemCompanyController", function($scope, $http, $rootScope){
	
	$scope.restUrl = "/CouponSystem.web/rest/company";
	$scope.username = "";
	$scope.password = "";
	$scope.id = "";
	$scope.title ="";
	$scope.message ="";
	$scope.image = "";
	$scope.startDate = "";
	$scope.endDate = "";
	$scope.amount = "";
	$scope.type = "";
	$scope.price = "";
	$scope.infomessage = "";
	$scope.errormessage = "";
	
	
	$scope.login = function(){
		url = $scope.restUrl+"/login/"+ $scope.username +"/" + $scope.password;
		console.log(url);
		$http.get(url).then(
				function(response){
					$scope.infomessage = "Login successful , Welcome Company";
				}, function(response){
					$scope.errormessage = "login failed , Please try again";
				});
		
		
	}
	
	$scope.createCoupon = function(){
		url = $scope.restUrl+"/createCoupon";
		console.log(url);
		$http.post(url,
				{
			
				"title" : $scope.title,  

				"message" : $scope.message, 

				"image" : $scope.image, 
				
				"startDate" : $scope.startDate, 
				
				"endDate" : $scope.endDate, 

				"amount": $scope.amount,

				"type": $scope.type,
				
				"price": $scope.price
				
		}).then(
				function(response){
					$scope.infomessage = response.data.message;
				}, function(response){
					$scope.errormessage = response.data.message;
				});
		
	}
	
	$scope.removeCoupon = function(){
		url = $scope.restUrl+"/removeCoupon/"+$scope.id;
		console.log(url);
		$http.delete(url).then(
				function(response){
					$scope.infomessage = response.data.message;
				}, function(response){
					$scope.errormessage = response.data.message;
				});
		
	};
	
	
	$scope.updateCoupon = function(){
		url = $scope.restUrl+"/updateCoupon" ;
		console.log(url);
		$http.put(url,
				{
			

			"title" : $scope.title,  

			"message" : $scope.message, 

			"image" : $scope.image, 
			
			"startDate" : $scope.startDate, 
			
			"endDate" : $scope.endDate, 

			"amount": $scope.amount,

			"type": $scope.type,
			
			"price": $scope.price
			
		}).then(
					function (response){
						$scope.infomessage = response.data.message;
					}, function(response){
						$scope.errormessage = response.data.message;
					});
			
	};
	
	
	$scope.getCoupon = function(){
		url = $scope.restUrl + "/getCoupon/"+ $scope.id;
		console.log(url);
		$http.get(url).then(
				function(response){
					$scope.infomessage =  "successful getCoupon:" + response.data.id + ", title:" + response.data.title + ", message :" + response.data.message + " , image:" + response.data.image + ", startDate :" + response.data.startDate + " , endDate:" + response.data.endDate + " , amount:" + response.data.amount + ", type :" + response.data.type + " , price:" + response.data.price;
				}, function(response){
					$scope.errormessage = response.data.message;
				});
	};
	
	$scope.getAllCoupons = function(){
		url = $scope.restUrl+"/getAllCoupons";
		console.log(url);
		$http.get(url).then(
				function(response){
					$scope.coupons = response.data;
				}, function(response){
					$scope.errormessage = response.data.message;
				});
	};
	
	$scope.getCouponByType = function(){
		url = $scope.restUrl + "/getCouponByType/"+ $scope.type;
		console.log(url);
		$http.get(url).then(
				function(response){
 					$scope.couponsbytype = response.data;
				}, function(response){
					$scope.errormessage = response.data.message;
				});
	};
	
	$scope.logout = function(){
		url = $scope.restUrl + "/logout";
		console.log(url);
		$http.get(url).then(
				function(response){
					$scope.infomessage = "Logout successful , goodbye Company";
				}, function(response){
					$scope.errormessage = "Company logout failed because you are not logged in";
				});
	};
	
	
	
	
	});