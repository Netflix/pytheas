$(document).ready(function() {
    "use strict";

    setInterval(function () {
        var uptime = (new Date().getTime() - ${Global.startTime})/1000;
	
		var d = (uptime / 86400)|0;
		var h = ((uptime / 3600) % 24)|0;
		var m = ((uptime / 60) % 60)|0;
		var s = (uptime % 60)|0;
		
		$("#uptime").text("{0} days {1}{2}:{3}{4}:{5}{6}".format(
				d, 
				h > 10 ? "" : "0", h, 
				m > 10 ? "" : "0", m, 
				s > 10 ? "" : "0", s));
    }, 1000);
	
    $('#nf-modules').change(function() {
    	var moduleName = $('#nf-modules').val();
    	window.location = "${RequestContext.pathToRoot}" + moduleName;
    });

    $('#nf-regions').change(function() {
    	window.location = $("#nf-regions option:selected").attr("href");
    });
    
    $('#nf-regions').val("${Global.currentRegion}.${Global.environmentName}");
    
    $('#layoutheader-main h1').click(function() {
    	window.location = <#if RequestContext.contextPath=="">"/"<#else>"${RequestContext.contextPath}"</#if>;
    });

    // fixes Bootstrap modal dialog bug per http://jsfiddle.net/ATeaH/8/
    $('.modal').appendTo($('body'));

    // Very nasty hack to bubble up the active menu class from sub menu items
    var $li = $(".subnav li.active");
    while (true) {
        $li = $li.parent();
        if ($li.is("li")) {
            $li.addClass("active");
        }
        else if ($li.hasClass("nav")) {
            break;
        }
        else if ($li.is("ul")) {
            continue;
        }
        else {
            break;
        }
    }

});