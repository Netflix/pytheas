var mainLayout;

$(document).ready(function() {
    mainLayout = $('body').layout({
    	defaults : {
            resizable:          false,
            slidable:           false,
    		spacing_open:		0,
    		spacing_closed:		0,
            togglerLength_open: 0            // HIDE the toggler button
    	},
    	south : {
    		paneSelector:	".outer-south"
    	},
    	center : {
    		paneSelector:	".outer-center"
    	},
    	north : {
    		paneSelector:	".outer-north",
            showOverflowOnHover: true
    	}
    });
    
    <#if Explorer?exists>
    	var module = $("#nf-modules option[value='${Explorer.name}']");
    	if (module.length > 0) 
    		module.attr('selected', 'selected');
        document.title = "${Global.applicationName} - ${Explorer.title}";  
    <#else>
        document.title = "${Global.applicationName}";  
    </#if>
	
	$( ".message-container .ui-icon-closethick" ).live("click", function() {
		$( this ).parent().parent().hide();
	});
	
	$( ".portlet-header .portlet-toggle" ).live("click", function() {
		$( this ).toggleClass( "ui-icon-minusthick" ).toggleClass( "ui-icon-plusthick" );
		$( this ).parents( ".portlet:first" ).find( ".portlet-content" ).toggle();
	});

    $("#breadcrumbs").jBreadCrumb({minimumCompressionElements: 10});
    
});