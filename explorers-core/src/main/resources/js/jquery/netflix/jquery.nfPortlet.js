;(function($) {
    "use strict";
    
    var methods = {
        init : function (options) {
            var settings = {
                sUrl: null,
                sTitle: null,
                oCollapsible: false,
                oFilterable: false,
                oRefreshable : false,
                sFilterPlaceholder : "filter"
            };
            return this.each(function(){
                // Merge the options
                if ( options ) {
                    $.extend( settings.events, options.events );
                    $.extend( settings, options );
                }
               
                $(this).addClass("mini-layout");
                var $header = $("<div class='portlet-header'></div>").appendTo($(this));
                $("<legend>{0}</legend>".format(settings.sTitle)).appendTo($header);
                if (settings.oCollapsible) {
                    $("<i class='icon-minus'></i>").appendTo($header);
                }
                if (settings.oRefreshable) {
                    $("<i class='icon-minus'></i>").appendTo($header);
                }
//                if (settings.oFilterable) {
//                    $("<input type='text' class='portlet-filter' style='float:right;margin:0!important;' placeholder='{0}'></input>".format(settings.sFilterPlaceholder)).appendTo($header);
//                }
                
                var $contents = $("<div class='portlet-content'></div>").appendTo($(this));
                
                if (settings.sUrl) {
                    $contents.load(settings.sUrl, function(reponse, status, xhr) {
                        if (status == "error") {
                            $contents.html("<span class='error'>Error loading portlet contents from: " + settings.sUrl + "<br/>" + xhr.status + " " + xhr.statusText + "</span>");
                        }
                    });
                }
                return this;
            });
        }
    };
    
    $.fn.nfPortlet = function(method) {
        if (methods[method]) {
            return methods[method].apply(this, Array.prototype.slice.call(arguments, 1));
        } else if (typeof method == 'object' || !method) {
            return methods.init.apply(this, arguments);
        } else {
            $.error( 'Method ' + method + ' does not exist on jQuery.nfPortlet');
        }
    };
})(jQuery);