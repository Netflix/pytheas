/**
 * Utility class to manage the url hash
 */
;
(function($) {
    "use strict";

    $.nfDialog = function(options) {
        var dialog = $('<div style="display:none"></div>').appendTo('body');

        var settings = {
            url : null,
            opts : {
                autoOpen    : true,
                width       : 600,
                height      : 500,
                resizable   : true,
                modal       : true,
                dataType    : "json",
                close       : function(event, ui) {
                    dialog.remove();
                }
            }
        };

        // Merge the options
        settings = $.extend(true, {}, settings, options);

        dialog.load(settings.url, function(responseText, textStatus,
                XMLHttpRequest) {
            dialog.dialog(settings.opts);
        });

        dialog.keyup(function(e) {
            if (e.keyCode == 13) {
                // Close dialog and/or submit here...
                e.preventDefault();
            }
        });

        dialog.children('input').keyup(function(e) {
            if (e.keyCode == 13) {
                // Close dialog and/or submit here...
                e.preventDefault();
            }
        });
    };

})(jQuery);