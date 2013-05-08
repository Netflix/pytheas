/**
 */
;(function($) {
    "use strict";
    
    var methods = {
        init : function (options) {
            var settings = {
                oRules          : {},
                sConfirmTitle   : "Confirm",
                fnConfirmMessage: function(data) { return "Do you really want to do this?"; },
                fnBefore        : function(data) { return data; },
                fnSuccess       : function(data) {},
                fnFailure       : null,
                sSuccess        : "Operation completed successfuly",
                sSuccessRedirectDelay: null,
                fnSuccessRedirectUrl : function(data) { return null; },
                sProcessing     : "Processing..."
            };
            return this.each(function(){
                // Merge the options
                if ( options ) {
                    $.extend( settings.events, options.events );
                    $.extend( settings, options );
                }
                
                var $this = $(this),
                    id = $(this).attr('id'),
                    $alert = $(this).find(".alert").hide();
                
                $this.validate({
                    rules       : settings.oRules,
                    errorClass  : 'error',
                    validClass  : 'success',
                    errorElement: 'span',
                    highlight   : function(element, errorClass, validClass) {
                        if (element.type === 'radio') {
                            this.findByName(element.name).parent('div').parent('div').removeClass(validClass).addClass(errorClass);
                        } else {
                            $(element).parent('div').parent('div').removeClass(validClass).addClass(errorClass);
                        }
                    },
                    unhighlight : function(element, errorClass, validClass) {
                        if (element.type === 'radio') {
                            this.findByName(element.name).parent('div').parent('div').removeClass(errorClass).addClass(validClass);
                        } else {
                            $(element).parent('div').parent('div').removeClass(errorClass).addClass(validClass);
                        }
                    },
                    errorPlacement: function(error, element) {
                        error.addClass("help-inline").insertAfter( element );
                    }
                });
                
                $(this).submit(function() {
                    var data = form2js(id, ".", false);
                    data = settings.fnBefore(data);
                    
                    $alert.removeClass("alert-error alert-success");
                    
                    if ($this.valid()) {
                        try {
                            $.nfConfirm({ 
                                title       : settings.sConfirmTitle,
                                text        : settings.fnConfirmMessage(data),
                                type        : "POST",
                                url         : $this.attr("action"), 
                                data        : JSON.stringify(data[id]),
                                dataType    : "json",
                                contentType : "application/json; charset=utf-8",
                                afterSuccess : function(data, result) {
                                    data = JSON.parse( data );
                                    $alert.removeClass("alert-info").addClass('alert-success').html(settings.sSuccess).show();
                                    settings.fnSuccess(data, result);
                                  
                                    var url = settings.fnSuccessRedirectUrl(data, result);
                                    if (url) {
                                        if (settings.sSuccessRedirectDelay) {
                                            setTimeout(function() { 
                                                  window.location = url; 
                                            }, 
                                            settings.sSuccessRedirectDelay);
                                        }
                                        else {
                                            window.location = url;
                                        }
                                    }
                                },
                                afterFailure : function(err) {
                                    $alert.removeClass("alert-info").addClass('alert-error').show().html(err);
                                    settings.fnFailure(500, err);
                                }
                            });
                        } catch (err) {
                            $.nfAlert({
                                title:  "Javscript error",
                                text :  err
                            });
                        }
                    }
                    return false;
                });
                return this;
            });
        }
    };
    
    $.fn.nfAjaxForm = function(method) {
        if (methods[method]) {
            return methods[method].apply(this, Array.prototype.slice.call(arguments, 1));
        } else if (typeof method == 'object' || !method) {
            return methods.init.apply(this, arguments);
        } else {
            $.error( 'Method ' + method + ' does not exist on jQuery.nfMessage');
        }
    };
})(jQuery);