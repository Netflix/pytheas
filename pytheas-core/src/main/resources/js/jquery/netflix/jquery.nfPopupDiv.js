/**
 * JQuery plugin that extends the functionality of a simple dropdown list
 * 1.  Populate from AJAX
 * 2.  Filter contents
 */

;(function($) {
    "use strict";
    
	var methods = {
		init : function (options) {
			var settings = {
				"anchor" : null,
				"event" : "hover"
			};
			return this.each(function(){
				var $this = $(this),
					id = $this.attr('id');

				// Merge the options
				if ( options ) {
			        settings = $.extend( true, settings, settings, options );
				}
				
				$this.data('nfPopupDiv', settings);
				
				var $anchor = $("#" + settings.anchor);
				
				if (settings.event == "hover") {
					$anchor.hover(function (e) {
						$this.nfPopupDiv("show");
					});
				}
				else if (settings.event == "click") {
					$anchor.click(function (e) {
						$this.nfPopupDiv("show");
					});
					
					// TODO: close
				}
			});
		},
		
		show : function (options) {
			var $this = this,
			    $settings = $(this).data('nfPopupDiv'),
			    $anchor = $("#" + $settings.anchor),
			    position = $anchor.position();
			
			this.css('left', position.left)
				 .css('top',  position.top + $anchor.height())
				 .show()
				 .hover(function() {},
						function() {
							$this.nfPopupDiv("hide");
						});
			
			return this;
		},
		
		hide : function (options) {
			var $this = this,
			    $settings = $(this).data('nfPopupDiv');
		    
			$this.hide();
			
			return this;
		}
	};
	
	$.fn.nfPopupDiv = function(method) {
		if (methods[method]) {
			return methods[method].apply(this, Array.prototype.slice.call(arguments, 1));
		} else if (typeof method == 'object' || !method) {
			return methods.init.apply(this, arguments);
		} else {
			$.error( 'Method ' + method + ' does not exist on jQuery.nfPopupDiv');
		}
	};
})(jQuery);