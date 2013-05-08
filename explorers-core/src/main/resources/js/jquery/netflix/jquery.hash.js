/**
 * Utility class to manage the url hash
 */
(function ($) {
    "use strict";
    
	var previousParams = {};
	
	/**
	 * Listen for changes to the hash params and invoke a function for each change
	 * TOOD: Should all notification be sent to a single function?
	 */
	$.listenHashChange = function (params) {
		previousParams = $.getHashParams();
		$(window).hashchange( function() {
			var currentParams = $.getHashParams();
			
			if (typeof params == 'function') {
				params(previousParams, currentParams);
			}
			else {
			    var param;
				for (param in params) {
					var prev = previousParams[param];
					var curr = currentParams[param];
					if (prev != curr) {
						params[param](prev, curr);
					}
				}
			}
			previousParams = currentParams;
		});
		
		if (typeof params == 'function')
			params({}, $.getHashParams());
	};
	
	/**
	 * Modify the hash params by either adding or removing parameters
	 */
	$.modifyHashParams = function(toAdd, toRemove) {
		var params = $.getHashParams(),
		    attrname;
		if (toAdd) {
			for (attrname in toAdd) { 
				params[attrname] = toAdd[attrname]; 
			}
		}
		if (toRemove) {
			for (attrname in toRemove) {
				delete params[attrname];
			}
		}
		var str = '';
		for (attrname in params) {
			if (params[attrname]) 
				str += "{0}={1}&".format(attrname, encodeURIComponent(params[attrname]));
		}
		window.location.hash = str;
		window.location.hash = str;
		
		return str;
	};
	
	/**
	 * Modify the hash params by either adding or removing parameters, no encoding of URL arguments.
	 */
	$.modifyHashParamsNoEncode = function(toAdd, toRemove) {
		var params = $.getHashParams(),
		    attrname;
		if (toAdd) {
			for (attrname in toAdd) { 
				params[attrname] = toAdd[attrname]; 
			}
		}
		if (toRemove) {
			for (attrname in toRemove) {
				delete params[attrname];
			}
		}
		var str = '';
		for (attrname in params) {
			if (params[attrname]) 
				str += "{0}={1}&".format(attrname, params[attrname]);
		}
		window.location.hash = str;
		window.location.hash = str;
		
		return str;
	};
	/**
	 * Get an object representing all parameters in the hash
	 */
    $.getHashParams = function() {
        var hashParams = {},
            e,
            a = /\+/g,  // Regex for replacing addition symbol with a space
            r = /([^&;=]+)=?([^&;]*)/g,
            d = function (s) { return decodeURIComponent(s.replace(a, " ")); },
            q = window.location.hash.substring(1);

        while (e = r.exec(q))
           hashParams[d(e[1])] = d(e[2]);

        return hashParams;
    };	
    
})(jQuery);