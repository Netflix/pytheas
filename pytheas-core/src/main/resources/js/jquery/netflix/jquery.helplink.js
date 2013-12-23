;(function($) {
    "use strict";

    var helpLinkIds = [];
    var helpPopoverOpen = false;
    var currentHelpId = null;

    function attachBodyClickEvent() {
        $('body').unbind('click').bind('click', function(evt) {
            var target = evt.target;
            if (hasHelpLink(target)) {
                return;
            }

            if (helpPopoverOpen && ! $.contains($('.popover')[0], target)) {
                evt.preventDefault();
                hideCurrent();
                helpPopoverOpen = false;
            }
        });
    }

    function hideCurrent() {
        $('#' + currentHelpId).popover('hide');
    }

    function hasHelpLink(target) {
        var targetId = $(target).attr('id');
        return targetId && (helpLinkIds.indexOf(targetId) > -1);
    }

    var methods = {
        init : function (options) {
            var settings = {
                title : "Help Title",
                simpleContent : "Help Content",
                fnBuildHelpContent : null,
                trigger : 'hover'
            };

            return this.each(function(){
                var $this = $(this),
                    id = $this.attr('id');

                helpLinkIds.push(id);

                // Merge the options
                if ( options ) {
                    $.extend( settings.events, options.events );
                    $.extend( settings, options );
                }

                var popoverTrigger = 'hover';
                if (settings.trigger != 'hover') {
                    popoverTrigger = 'click';
                    attachBodyClickEvent();
                }

                if ($.isFunction(settings.fnBuildHelpContent)) {
                    $('#' + id).popover({
                        trigger : popoverTrigger,
                        title : settings.title,
                        content : settings.fnBuildHelpContent()
                    });
                } else {
                    $('#' + id).popover({
                        trigger : popoverTrigger,
                        title : settings.title,
                        content : '<p>' + settings.simpleContent + '</p>'
                    });
                }

                $('#' + id).unbind('click').bind('click', function(evt) {
                    evt.preventDefault();
                    $this.helplink('_show');
                    window.setTimeout(function() {
                        helpPopoverOpen = true;
                        currentHelpId = $(evt.target).attr('id');
                    }, 0);
                });

            });
        },


        _show : function() {
            var $this = $(this), id = $this.attr('id');
            $('#' + id).popover('show');
        },

        _hide : function() {
            var $this = $(this), id = $this.attr('id');
            $('#' + id).popover('hide');
        }

    };

    $.fn.helplink = function(method) {
        if (methods[method]) {
            return methods[method].apply(this, Array.prototype.slice.call(arguments, 1));
        } else if (typeof method == 'object' || !method) {
            return methods.init.apply(this, arguments);
        } else {
            $.error( 'Method ' + method + ' does not exist on jQuery.ajaxBusy');
        }
    };
})(jQuery);