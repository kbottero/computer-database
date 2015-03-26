$(function() {
    $('#introduced').bind('blur', function() {
        var txtVal =  $('#introduced').val();
        if (!isDateValid(txtVal)) {
            alert('Invalid Date');
    	}
    });
    
    $('#discontinued').bind('blur', function() {
        var txtVal =  $('#discontinued').val();
        if (!isDateValid(txtVal)) {
            alert('Invalid Date');
    	}
    });
    
    $('#computerName').bind('blur', function() {
        var txtVal =  $('#computerName').val();
        if (!isNameValid(txtVal)) {
            alert('Invalid Name');
    	}
    });
    
    $('#formEditComputer').submit(function(f) {
    	if ($('#introduced').val() != null) {
    		if (!isDateValid($('#introduced').val())) {
    			f.preventDefault();
                return;
        	}
    	}
    	if ($('#discontinued').val() != null) {
    		if (!isDateValid($('#discontinued').val())) {
    			f.preventDefault();
                return;
        	}
    	}
    	if ($('#computerName').val() != null) {
    		if (!isNameValid($('#computerName').val())) {
    			f.preventDefault();
                return;
        	}
    	}
    });

function isDateValid(txtDate)
{
    var currVal = txtDate;
    if(currVal == '')
        return true;
    
    var rxDatePattern = /^(19|20\d{2})(-)([0][1-9]|[1][0|1|2])(-)([0][1-9]|[12][0-9]|[3][0,1])$/; //Declare Regex
    var dtArray = currVal.match(rxDatePattern); // is format OK?
    
    if (dtArray == null) {
        return false;
    }
    
    //Checks for yyyy-mm-dd format.
    dtYear = dtArray[1];
    dtDay= dtArray[3];    
    dtMonth = dtArray[5];    
    
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
function isNameValid(txtName)
{
    var currVal = txtName;
    if(currVal == '') {
        return false;
    }
    
    var rxNamePattern = /[A-Za-z0-9_~\-@#\$%\^&\*\(\).]+$/; //Declare Regex
    var dtArray = currVal.match(rxNamePattern); // is format OK?
    
    if (dtArray == null) {
        return false;
    }
    return true;
}

});