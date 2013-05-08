;(function($) {
    "use strict";
    
    var methods = {
        init : function (options) {
            var settings = {
                loading : null,
                pending : null,
                contents : "",
                id : null
            };
            return this.each(function(){
                // Merge the options
                if ( options ) {
                    $.extend( settings.events, options.events );
                    $.extend( settings, options );
                }
                
                // Attach settings to data
                $(this).data('nfAjaxDivLoader', {
                       "target" : $(this),
                       "data"   : settings
                   });

                return this;
            });
        },
        
        load : function (url) {
            var $this = $(this),
                data = $(this).data('nfAjaxDivLoader').data;

            if (data.loading == null) {
                data.loading = url;

                $this.html(data.contents);
                
                if (url != null) {
                    $this.ajaxBusy();
                    
                    var $view;
                    if (data.id != null) {
                        $view = $("#" + data.id);
                    }
                    else {
                        $view = $this;
                    }
                    $view.load(url, function(response, status, xhr) {
                        $this.ajaxBusy('remove');
                        
                        data.loading = null;
                        if (data.pending != null) {
                            var url = data.pending.url;
                            data.pending = null;
                            $this.nfAjaxDivLoader('load', url);
                        }
                        
                        if (status == "error") {
                            $view.html(response);
                        }
                    });
                }
            }
            else {
                data.pending = { url: url };
            }
            
            return this;
        }
    };
    
    $.fn.nfAjaxDivLoader = function(method) {
        if (methods[method]) {
            return methods[method].apply(this, Array.prototype.slice.call(arguments, 1));
        } else if (typeof method == 'object' || !method) {
            return methods.init.apply(this, arguments);
        } else {
            $.error( 'Method ' + method + ' does not exist on jQuery.nfAjaxDivLoader');
        }
    };
})(jQuery);