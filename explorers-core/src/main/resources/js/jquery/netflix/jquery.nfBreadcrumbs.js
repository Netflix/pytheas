;(function($) {
    "use strict";
    
    var methods = {
        init : function (options) {
            return this.each(function(){
                var $this = $(this);
                $.each(options.crumbs, function(index, crumb) {
                    var id="";
                    if (crumb.id) {
                        id = " id='{0}'".format(crumb.id);
                    }
                    if (index == options.crumbs.length-1) {
                        $this.append("<li class='active'><span {0}>{1}</span></li>".format(id, crumb.text));
                    }
                    else {
                        $this.append("<li><a href='{0}'><span{1}>{2}</span></a><span class='divider'>/</span></li>".format(crumb.href, id, crumb.text));
                    }
                });
                return this;
            });
        }
    };
    
    $.fn.nfBreadcrumbs = function(method) {
        if (methods[method]) {
            return methods[method].apply(this, Array.prototype.slice.call(arguments, 1));
        } else if (typeof method == 'object' || !method) {
            return methods.init.apply(this, arguments);
        } else {
            $.error( 'Method ' + method + ' does not exist on jQuery.nfBreadcrumbs');
        }
    };
})(jQuery);