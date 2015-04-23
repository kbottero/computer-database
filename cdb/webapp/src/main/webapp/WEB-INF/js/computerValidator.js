$(function() {
	
    $('#introduced').bind('keyup change blur',  function () {testFormValidity ()});
    
    $('#discontinued').bind('keyup change blur', function () {testFormValidity ()});
    
    $('#name').bind('keyup change blur', function () {testFormValidity ()});
    
    $('#formEditComputer').submit(function(f) {
    	if (!testFormValidity ()) {
			f.preventDefault();
            return;
    	}
    });
	    
	function testFormValidity () {
		var isValid = true;
        if (!isDateValid($('#introduced').val())) {
        	isValid = false;
    		$('#introduced').addClass('error');
        } else {
    		$('#introduced').removeClass('error');
        }
        if (!isDateValid($('#discontinued').val())) {
        	isValid = false;
    		$('#discontinued').addClass('error');
        } else {
    		if ($('#introduced').val() != '' && isDateValid($('#introduced').val()) ) {
    			if (isDateBefore()) {
    	        	isValid = false;
    	    		$('#discontinued').addClass('error');
    			} else {
    	    		$('#discontinued').removeClass('error');
    			}
    		} else {
	    		$('#discontinued').removeClass('error');
    		}
        }
    	if (!isNameValid($('#name').val())) {
    		isValid = false;
    		$('#name').addClass('error');
        } else {
    		$('#name').removeClass('error');
        }
    	
    	if (!isValid) {
    		$('#formEditComputerSubmit').attr('disabled','disabled');
    	} else {
    		$('#formEditComputerSubmit').removeAttr('disabled');
    	}
    	return isValid;
	}
	
	function isDateBefore () {
		var introduced =  $('#introduced').val();
		var discontinued =  $('#discontinued').val();
		
		var userLang = navigator.language || navigator.userLanguage;

		if(userLang.split('-')[0] == "fr") {
		    var rxDatePattern = /^([0][1-9]|[12][0-9]|[3][0,1])(\/)([0][1-9]|[1][0|1|2])(\/)(19|20)(\d{2})$/;	
		} else {
			var rxDatePattern = /^([0][1-9]|[1][0|1|2])(\/)([0][1-9]|[12][0-9]|[3][0,1])(\/)(19|20)(\d{2})$/;
		}			
		var dtArrayIntroduced = introduced.match(rxDatePattern);
		var dtArrayDiscontinued = discontinued.match(rxDatePattern); // is format OK?

		if (dtArrayIntroduced == null || dtArrayDiscontinued == null ) {
		    return false;
		}
		if ((dtArrayIntroduced[5]+dtArrayIntroduced[6] > dtArrayDiscontinued[5]+dtArrayDiscontinued[6]) ||
			(dtArrayIntroduced[3] > dtArrayDiscontinued[3]) ||
			(dtArrayIntroduced[1] > dtArrayDiscontinued[1])) {
			return true;
		}
	}
	
	function isDateValid(txtDate) {
	    var currVal = txtDate;
	    if(currVal == '')
	        return true;
	    
	    var userLang = navigator.language || navigator.userLanguage;
	    
		if(userLang.split('-')[0] == "fr") {
		    var rxDatePattern = /^([0][1-9]|[12][0-9]|[3][0,1])(\/)([0][1-9]|[1][0|1|2])(\/)(19|20)(\d{2})$/; //Declare Regex
		    var dtArray = currVal.match(rxDatePattern); // is format OK?
		    
		    if (dtArray == null) {
		        return false;
		    }
		    
		    //Checks for yyyy-mm-dd format.
		    dtYear = dtArray[5]+dtArray[6];
		    dtMonth = dtArray[2];    
		    dtDay= dtArray[1];    
		} else {
			var rxDatePattern = /^([0][1-9]|[1][0|1|2])(\/)([0][1-9]|[12][0-9]|[3][0,1])(\/)(19|20)(\d{2})$/; //Declare Regex
		    var dtArray = currVal.match(rxDatePattern); // is format OK?
		    
		    if (dtArray == null) {
		        return false;
		    }
		    
		    //Checks for yyyy-mm-dd format.
		    dtYear = dtArray[5]+dtArray[6];
		    dtMonth = dtArray[1];    
		    dtDay= dtArray[2];    
		}
	    
	    if (dtMonth < 1 || dtMonth > 12) {
	        return false;
	    } else if (dtDay < 1 || dtDay> 31) {
	        return false;
	    } else if ((dtMonth==4 || dtMonth==6 || dtMonth==9 || dtMonth==11) && dtDay ==31) {
	        return false;
	    } else if (dtMonth == 2) {
	        var isleap = (dtYear % 4 == 0 && (dtYear % 100 != 0 || dtYear % 400 == 0));
	        if (dtDay> 29 || (dtDay ==29 && !isleap)) 
	                return false;
	    }
	    return true;
	}	
	
	function isNameValid(txtName) {
	    var currVal = txtName;
	    if(currVal == '') {
	        return false;
	    }
	    var rxNamePattern = /[A-Za-z0-9_~\-@#\$%\^&\*\(\).]+$/; 
	    var dtArray = currVal.match(rxNamePattern);
	    
	    if (dtArray == null) {
	        return false;
	    }
	    return true;
	}
});