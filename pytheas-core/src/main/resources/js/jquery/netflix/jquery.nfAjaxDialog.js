/**
 * Utility class to load a modal dialog.
 */
(function($) {
    "use strict";

    var containerMethods = {
        init : function(options) {
            var settings = {
                url : null,
                cancel : function() {}, // Notification that the dialog was cancelled
                close : function()  {}, // Notification that the dialog was closed successfully
                error : function(e) {} // Notification of any other error
            };

            return this.each(function() {
                // Merge the options
                settings = $.extend(true, {}, settings, options);
                // Attach settings to data
                $(this).data('nfDialogContainer', settings);
            });
        },

        close : function() {
            $(this).data('nfDialogContainer').close();
            return this;
        }
    };

    $.fn.nfDialogContainer = function(method) {
        if (containerMethods[method]) {
            return containerMethods[method].apply(this, Array.prototype.slice
                    .call(arguments, 1));
        } else if (typeof method == 'object' || !method) {
            return containerMethods.init.apply(this, arguments);
        } else {
            $.error('Method ' + method
                    + ' does not exist on jQuery.nfDialogContainer');
        }
    };

    /**
     * Utility method to attach a new div for the dialog to the DOM, ajax load
     * the content and associate the context with the dialog so the dom can be
     * autodeleted.
     */
    $.nfDialogLoader = function(options) {
        var parent = options.parent ? options.parent : 'body';
        var $dialog = $('<div style="display:none"></div>').appendTo(parent);
        $dialog.nfDialogContainer(options);
        if (options.method == "POST") {
            $dialog.load(options.url, {});
        } else {
            $dialog.load(options.url);
        }
    };

    var managerMethods = {
        init : function(options) {
            return this.each(function() {
                var $this = $(this),
                    $dialog = $this.parent();
                options.close = function(event, ui) {
                    $dialog.nfDialogContainer("close");
                    $dialog.remove();
                };

                return $dialog.dialog(options);
            });
        }
    };

    $.fn.nfDialogManager = function(method) {
        if (managerMethods[method]) {
            return managerMethods[method].apply(this, Array.prototype.slice
                    .call(arguments, 1));
        } else if (typeof method == 'object' || !method) {
            return managerMethods.init.apply(this, arguments);
        } else {
            $.error('Method ' + method
                    + ' does not exist on jQuery.nfDialogManager');
        }
    };

    $.nfConfirm = function(settings) {
        var options = {
            proceed       : undefined,
            afterSuccess  : function() {},
            afterFailure  : function() {},
            cancel        : function() {},
            url           : undefined,
            dataType      : "json",
            data          : undefined,
            type          : "GET",
            errorTitle    : "Error",
            contentType   : undefined,
            fnErrorMessage: function(jqXHR, textStatus, errorThrown) {
                try {
                    var obj = jQuery.parseJSON( jqXHR.responseText );
                    return obj.error + "\n" + obj.trace;
                }
                catch (e) {
                    return jqXHR.responseText;
                }
            }
        };
        
        options = $.extend(true, {}, options, settings);
        try {
        	var $dialog = $('<div class="modal"><div class="modal-header"><button class="close" data-dismiss="modal">x</button><h3>{0}</h3></div><div class="modal-body"><p>{1}</p></div><div class="modal-footer"><div class="alert alert-success span4 ellipsis" style="display: none; "></div><a href="#" class="btn cancel">Cancel</a><a href="#" class="btn btn-primary">Yes</a></div></div>'
                    .format(options.title, options.text));

        	// Cancel button
            $dialog.modal({});
            $dialog.find('.cancel').on('click', function(event) {
                event.preventDefault();
                options.cancel();
                $dialog.modal('hide').remove();
            });
            var $alert = $dialog.find(".alert");
            
            var handleError = function(error) {
                $dialog.modal('hide').remove();
                $.nfAlert({
                    title:  "Javascript error",
                    text :  error,
                });
                options.afterFailure(error);
            }
            
            // Continue button
            $dialog.find('.btn-primary').on('click', function(event) {
                event.preventDefault();
                if (options.proceed) {
                    try {
                        options.proceed();
                        $dialog.modal('hide').remove();
                    } catch (err) {
                        options.fail(err);
                    }
                }
                else {
                    try {
                        $alert.addClass("alert-info").html("Processing...").show();
                        $.ajax({
                            type        : options.type, 
                            url         : options.url,
                            dataType    : options.dataType,
                            data        : options.data,
                            contentType : options.contentType,
                            success     : function(result) {
                                $dialog.modal('hide').remove();
                                
                            	if (options.successRedirect) {
                            		window.location = options.successRedirect;
                            	}
                            	else {
                                	if (options.fnSuccessMessage) {
                                		$.nfAlert({title: "Success", text : options.fnSuccessMessage(result)});
                                	}
                            	}
                            	
                            	options.afterSuccess(options.data, result);
                            },
                            error : function(jqXHR, textStatus, errorThrown) {
                                $dialog.modal('hide').remove();
                                
                                var message = {
                                    title:  options.errorTitle,
                                    text :  options.fnErrorMessage(jqXHR, textStatus, errorThrown)
                                };
                                $.nfAlert(message);
                                options.afterFailure(message.text);
                            }
    
                        });                	
                    } catch (err) {
                        handleError(err);
                    }
                }
            });
        } catch (err) {
            handleError(err);
        }
    };

    $.nfAlert = function(options) {
        var $dialog = $('<div class="modal"><div class="modal-header"><button class="close" data-dismiss="modal">x</button><h3>{0}</h3></div><div class="modal-body"><p>{1}</p></div><div class="modal-footer"><a href="#" class="btn btn-primary">Close</a></div></div>'
                .format(options.title, options.text));

        $dialog.modal({});
        $dialog.find('.btn-primary').on('click', function(obj) {
            $dialog.modal('hide').remove();
        });
    };
})(jQuery);