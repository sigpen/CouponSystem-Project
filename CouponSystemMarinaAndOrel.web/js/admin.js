var CouponSystemApp = angular.module("CouponSystemApp", []);

CouponSystemApp.controller("CouponSystemController", function($scope, $http, $rootScope){
	
	$scope.restUrl = "/CouponSystem.web/rest/admin";
	$scope.id = "";
	$scope.username = "";
	$scope.compName ="";
	$scope.cust_name ="";
	$scope.password = "";
	$scope.email = "";
	$scope.infomessage = "";
	$scope.errormessage = "";
	
	
	$scope.login = function(){
		url = $scope.restUrl+"/login/"+ $scope.username +"/" + $scope.password;
		console.log(url);
		$http.get(url).then(
				function(response){
					$scope.infomessage = "Login successful , Welcome administrator";
				}, function(response){
					$scope.errormessage = "login failed , Please try again";
				});
		
		
	}
	
	$scope.createCompany = function(){
		url = $scope.restUrl+"/createCompany";
		console.log(url);
		$http.post(url,
				{
			
				"compName":$scope.compName,  

				"password":$scope.password, 

				"email":$scope.email
				
		}).then(
				function(response){
					$scope.infomessage = response.data.message;
				}, function(response){
					$scope.errormessage = response.data.message;
				});
		
	}
	
	$scope.removeCompany = function(){
		url = $scope.restUrl+"/removeCompany/"+$scope.id;
		console.log(url);
		$http.delete(url).then(
				function(response){
					$scope.infomessage = response.data.message;
				}, function(response){
					$scope.errormessage = response.data.message;
				});
		
	};
	
	
	$scope.updateCompany = function(){
		url = $scope.restUrl+"/updateCompany" ;
		console.log(url);
		$http.put(url,
				{
			
			"compName" : $scope.compName,  

			"password" : $scope.password, 

			"email": $scope.email
	
		}).then(
					function (response){
						$scope.infomessage = response.data.message;
					}, function(response){
						$scope.errormessage = response.data.message;
					});
			
	};
	
	
	$scope.getCompany = function(){
		url = $scope.restUrl + "/getCompany/"+ $scope.id;
		console.log(url);
		$http.get(url).then(
				function(response){
					$scope.infomessage =  "successful getCompany:" + response.data.id + ", compName:" + response.data.compName + ", password :" + response.data.password + " , email:" +response.data.email;
				}, function(response){
					$scope.errormessage = response.data.message;
				});
	};
	
	$scope.getAllCompanies = function(){
		url = $scope.restUrl + "/getAllCompanies";
		console.log(url);
		$http.get(url).then(
				function(response){
					$scope.companies = response.data;
				}, function(response){
					$scope.errormessage = response.data.message;
				});
	};
	
	$scope.createCustomer = function(){
		url = $scope.restUrl+"/createCustomer";
		console.log(url);
		$http.post(url,
				{
			
				"cust_name" : $scope.cust_name,  

				"password" : $scope.password

		}).then(
				function(response){
					$scope.infomessage =response.data.message;
				}, function(response){
					$scope.errormessage = response.data.message;
				});
		
	}
	
	$scope.removeCustomer = function(){
		url = $scope.restUrl+"/removeCustomer/"+$scope.id;
		console.log(url);
		$http.delete(url).then(
				function(response){
					$scope.infomessage = response.data.message;
				}, function(response){
					$scope.errormessage = response.data.message;
				});
		
	};
	
	$scope.updateCustomer = function(){
		url = $scope.restUrl+"/updateCustomer";
		console.log(url);
		$http.put(url,{
			
			"cust_name" : $scope.cust_name,  

			"password" : $scope.password
	
		}).then(
					function (response){
						$scope.infomessage = response.data.message;
					}, function(response){
						$scope.errormessage = response.data.message;
					});
			
	};
	
	$scope.getCustomer= function(){
		url = $scope.restUrl + "/getCustomer/"+ $scope.id;
		console.log(url);
		$http.get(url).then(
				function(response){
					$scope.infomessage = "successful getCustomer: " + response.data.id + "cust_name "+ response.data.cust_name + ", password :" + response.data.password ;
				}, function(response){
					$scope.errormessage = response.data.message;
				});
		
	};
	
	
	$scope.getAllCustomer = function(){
		url = $scope.restUrl + "/getAllCustomer";
		console.log(url);
		$http.get(url).then(
				function(response){
					$scope.customers = response.data;
				}, function(response){
					$scope.errormessage = response.data.message;
				});
	};
	
	$scope.logout = function(){
		url = $scope.restUrl + "/logout";
		console.log(url);
		$http.get(url).then(
				function(response){
					$scope.infomessage = "Logout successful , goodbye administrator";
				}, function(response){
					$scope.errormessage = "Admin logout failed because you are not logged in";
				});
	};
	
	
	
	});