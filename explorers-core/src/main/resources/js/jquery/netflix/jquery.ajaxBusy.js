/**
 * Display a progress indicator on top of a div 
 */

;(function($) {
    "use strict";
    
	var methods = {
		init : function (options) {
			var settings = {
				label       : "Loading...",
				background  : "#bbb",
				color       : "red"
			};
			return this.each(function(){
				var $this = $(this),
					id = $this.attr('id');

				// Merge the options
				if ( options ) {
			        $.extend( settings.events, options.events );
			        $.extend( settings, options );
				}
				
				// Attach settings to data
				$this.data('ajaxBusy', {
		               "target"   : $this,
		               "settings" : settings
		           });
				
				$this.ajaxBusy('_show');
			});
		},
		
		_show : function() {
			var $this = $(this),
				id = $this.attr('id'),
				data = $this.data('ajaxBusy').settings;
			
			$('#{0}-ajaxBusy'.format(id)).remove();
			$("<div id='{0}-ajaxBusy' class='ajax-busy-indicator'><p style='height:100%;width:100%'>{1}</p></div>".format(id, data.label))
				.appendTo($this);

			$('#{0}-ajaxBusy'.format(id)).css({
			    width    : "{0}px".format($this.width()),
			    height   : "{0}px".format($this.height()),
			    position : "absolute"
			}).show();
		},
		
		remove : function() {
			var id = $(this).attr('id');
			$('#{0}-ajaxBusy'.format(id)).remove();
		}
	};
	
	$.fn.ajaxBusy = function(method) {
		if (methods[method]) {
			return methods[method].apply(this, Array.prototype.slice.call(arguments, 1));
		} else if (typeof method == 'object' || !method) {
			return methods.init.apply(this, arguments);
		} else {
			$.error( 'Method ' + method + ' does not exist on jQuery.ajaxBusy');
		}
	};
})(jQuery);