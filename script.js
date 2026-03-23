function validateForm(){

var name = document.getElementById("name").value.trim();
var email = document.getElementById("email").value.trim();
var mobile = document.getElementById("mobile").value.trim();
var department = document.getElementById("department").value;
var feedback = document.getElementById("feedback").value.trim();
var gender = document.querySelector('input[name="gender"]:checked');

// Name validation
if(name == ""){
alert("Name cannot be empty");
return false;
}

// Email validation
var emailPattern = /^[^ ]+@[^ ]+\.[a-z]{2,3}$/;
if(!emailPattern.test(email)){
alert("Invalid email format");
return false;
}

// Mobile validation
var mobilePattern = /^[0-9]{10}$/;
if(!mobilePattern.test(mobile)){
alert("Mobile must be 10 digits");
return false;
}

// Department validation
if(department == ""){
alert("Please select department");
return false;
}

// Gender validation
if(!gender){
alert("Please select gender");
return false;
}

// Feedback validation (min 10 words)
if(feedback.split(" ").length < 10){
alert("Feedback must contain at least 10 words");
return false;
}

alert("Form submitted successfully");

return true;
}