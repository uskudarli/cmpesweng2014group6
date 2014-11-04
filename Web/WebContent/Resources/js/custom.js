
$(document).ready(function() {
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
        	  success: function(data)
        	  {
        		  if(data == "true")
    			  {
        			  $('#registerSuccess').modal('show');
    			  }
        		  else
    			  {
        			  $form.trigger('reset');
        			  $form.data('bootstrapValidator').resetForm();
        			  $('#registerError').modal('show');
    			  }
        		  
        	  },
        	  error: function(data)
        	  {
        		  alert("error");
        	  }
        	  
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
        	  success: function(data)
        	  {
        		  if(data == "false")
    			  {
        			  $form.trigger('reset');
        			  $form.data('bootstrapValidator').resetForm();
        			  $('#loginError').modal('show');
    			  }
        		  
        	  },
        	  error: function(data)
        	  {
        		  alert("error");
        	  }
        	  
    	});
    });

});

