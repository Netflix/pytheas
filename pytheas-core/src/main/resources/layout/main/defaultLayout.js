$(document).ready(function() {
	var state = {
		"app": null,
		"id" : null,
		"view": null,
		"filter" : null
	};
	
    $('body').layout({
    	defaults : {
            resizable:          false,
            slidable:           false,
    		spacing_open:		0,
    		spacing_closed:		0,
            togglerLength_open: 0,           // HIDE the toggler button
    	},
    	center : {
    		paneSelector:	".outer-center"
    	}
    });
    
	var module = $("#nf-modules option[value='${module}']");
	if (module.length > 0) 
		module.attr('selected', 'selected');
	
    $('#nf-regions').change(function() {
    	//window.location = "http://explorers.{0}.dyntest.netflix.net:${port}${homepage}".format($('#nf-regions').val());
    });
    
	var region = $("#nf-regions option[value='${region}.${env}']");
	if (region.length > 0) 
		region.attr('selected', 'selected');
	

});