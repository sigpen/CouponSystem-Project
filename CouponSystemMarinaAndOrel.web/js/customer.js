var CouponSystemCustomerApp = angular.module("CouponSystemCustomerApp", []);

CouponSystemCustomerApp.controller("CouponSystemCustomerController", function($scope, $http, $rootScope){
	
	$scope.restUrl = "/CouponSystem.web/rest/customer";
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
					$scope.infomessage = "Login successful , Welcome customer";
				}, function(response){
					$scope.errormessage = "login failed , Please try again";
				});
		
	}
	
	$scope.purchaseCoupon = function(){
		url = $scope.restUrl+"/purchaseCoupon";
		console.log(url);
		$http.post(url,
				{
			
				"id" : $scope.id  

		}).then(
				function(response){
					$scope.infomessage = response.data.message;
				}, function(response){
					$scope.errormessage = response.data.message;
				});
		
	}
	
	
	$scope.getAllPurchasedCoupons = function(){
		url = $scope.restUrl + "/getAllPurchasedCoupons";
		console.log(url);
		$http.get(url).then(
				function(response){
					$scope.purchasedcoupons = response.data;
				}, function(response){
					$scope.errormessage = response.data.message;
				});
	};
	
	$scope.getAllPurchasedCouponsByType = function(){
		url = $scope.restUrl + "/getAllPurchasedCouponsByType/"+ $scope.type;
		console.log(url);
		$http.get(url).then(
				function(response){
 					$scope.purchasedcouponsbytype = response.data;
				}, function(response){
					$scope.errormessage = response.data.message;
				});
	};
	
	$scope.getAllPurchasedCouponsByPrice = function(){
		url = $scope.restUrl + "/getAllPurchasedCouponsByPrice/"+ $scope.price;
		console.log(url);
		$http.get(url).then(
				function(response){
 					$scope.purchasedcouponsbyprice = response.data;
				}, function(response){
					$scope.errormessage = response.data.message;
				});
	};
	

	$scope.logout = function(){
		url = $scope.restUrl + "/logout";
		console.log(url);
		$http.get(url).then(
				function(response){
					$scope.infomessage = "Logout successful , goodbye customer";
				}, function(response){
					$scope.errormessage = "Customer logout failed because you are not logged in";
				});
	};
	
});