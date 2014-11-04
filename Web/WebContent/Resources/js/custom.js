
$(document).ready(function() {
	errorCheck();
    $('#registerForm').bootstrapValidator({
    	feedbackIcons: {
            valid: 'glyphicon glyphicon-ok',
            invalid: 'glyphicon glyphicon-remove',
            validating: 'glyphicon glyphicon-refresh'
        },
        fields: {
            name: {
                message: 'The name is not valid',
                validators: {
                    notEmpty: {
                        message: 'The name is required and cannot be empty'
                    },
                    stringLength: {
                        min: 6,
                        max: 30,
                        message: 'The name must be more than 6 and less than 30 characters long'
                    },
                    regexp: {
                        regexp: /^[a-z ,.'-]+$/i,
                        message: 'The name is not valid'
                    },
                    different: {
                        field: 'password',
                        message: 'The name and password cannot be the same as each other'
                    }
                }
            },
            email: {
                validators: {
                    notEmpty: {
                        message: 'The email address is required and cannot be empty'
                    },
                    emailAddress: {
                        message: 'The email address is not a valid'
                    }
                }
            },
            password: {
                validators: {
                    notEmpty: {
                        message: 'The password is required and cannot be empty'
                    },
                    different: {
                        field: 'username',
                        message: 'The password cannot be the same as username'
                    },
                    stringLength: {
                        min: 6,
                        message: 'The password must have at least 6 characters'
                    }
                }
            },
            birthday: {
                validators: {
                    date: {
                        format: 'DD/MM/YYYY',
                        message: 'The date of birth is not valid'
                    }
                }
            },
            gender: {
                validators: {
                    notEmpty: {
                        message: 'The gender is required'
                    }
                }
            }
        }
    });
    
    	
    $("#registerButton").onclick(function(){
    	var $form = $("#registerForm");
        $.ajax({
        	  type: "POST",
        	  url: $form.attr("action"),
        	  data: $form.serialize(),
        	  
    	});
    });
    
    
    $('#loginForm').bootstrapValidator({
    	feedbackIcons: {
            valid: 'glyphicon glyphicon-ok',
            invalid: 'glyphicon glyphicon-remove',
            validating: 'glyphicon glyphicon-refresh'
        },
        fields: {
            email: {
                validators: {
                    notEmpty: {
                        message: 'The email address is required and cannot be empty'
                    },
                    emailAddress: {
                        message: 'The email address is not a valid'
                    }
                }
            },
            password: {
                validators: {
                    notEmpty: {
                        message: 'The password is required and cannot be empty'
                    },
                    stringLength: {
                        min: 6,
                        message: 'The password must have at least 6 characters'
                    }
                }
            }
        }
    });
    
    $("#loginButton").onclick(function(){
    	
    	e.preventDefault();
    	var $form = $("#loginForm");
        $.ajax({
        	  type: "POST",
        	  url: $form.attr("action"),
        	  data: $form.serialize(),
 
    	});
    });
    

});

function errorCheck()
{
	if($('input[name=error]').val()=="true"){
		$('#errorPop').modal('show');
		$('input[name=error]').val("false");
	}
}

