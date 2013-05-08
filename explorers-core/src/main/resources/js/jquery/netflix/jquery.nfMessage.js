/**
 * Display a progress indicator on top of a div 
 */

;(function($) {
    "use strict";
    
	var methods = {
		init : function (options) {
			var settings = {
				closable: true,
				visible: true,
				level: "default",
				message: null
			};
			return this.each(function(){
				// Merge the options
				if ( options ) {
			        $.extend( settings.events, options.events );
			        $.extend( settings, options );
				}
				
				// Attach settings to data
				$(this).data('nfMessage', {
		               "target" : $(this),
		               "data" : settings
		           });
				
				if (!settings.message) {
					settings.message = $(this).html();
				}
				
				if (settings.message === "") {
					settings.visible = false;
				}
				
				$(this).addClass("message-container ui-corner-all message-" + settings.level);
				
				var body = "<div class='message-header'>";
				if (settings.closable) {
					body += "<span class='ui-icon ui-icon-closethick'></span>";
				}
				body += "</div><div class='message-content'>" + settings.message + "</div></div>";
				if (!settings.visible) 
					$(this).hide();
				$(this).html(body);
				return this;
			});
		},
		
		hide : function() {
			$(this).hide();
			return this;
		},
		
		info : function(message) {
			return $(this).nfMessage("message", 'info', message);
		},
		
		error : function(message) {
			return $(this).nfMessage("message", 'error', message);
		},
		
		warning : function(message) {
			return $(this).nfMessage("message", 'warning',  message);
		},
		
		'default' : function(message) {
			return $(this).nfMessage("message", 'default',  message);
		},
		
		success : function(message) {
			return $(this).nfMessage("message", 'warn',  message);
		},
		
		message : function(level, message) {
			var $data = $(this).data('nfMessage');
			var toRemove = '';
			$.each( $(this).attr('class').split(' '), function (index, clazz) {
				if (clazz.indexOf("message-") == 0 && clazz != "message-container") 
					toRemove += clazz + " ";
			});
			$(this).removeClass(toRemove).addClass("message-" + level);
			
			$(this).find('.message-content').html(message);
			$(this).show();
			return this;
		}
	};
	
	$.fn.nfMessage = function(method) {
		if (methods[method]) {
			return methods[method].apply(this, Array.prototype.slice.call(arguments, 1));
		} else if (typeof method == 'object' || !method) {
			return methods.init.apply(this, arguments);
		} else {
			$.error( 'Method ' + method + ' does not exist on jQuery.nfMessage');
		}
	};
})(jQuery);