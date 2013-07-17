/**
 */
;(function($) {
    "use strict";
    
    var methods = {
        init : function (options) {
            var settings = {
                oRules      : {},
                oConfirm    : null,     // { sTitle, fnMessage : function(data) {} }
                sSourceUrl  : null,
                fnAction    : function(action)          { return action; },
                fnBefore    : function(data)            { return data;   },
                fnSuccess   : function(data)            {   },
                fnFailure   : function(code, message)   {   },
                fnPreShow   : function(data)            {   },
                sSourcePrefix : null,
                sSuccess    : "Operation completed successfuly",
                sSuccessDelay: 0,
                fnSuccessRedirectUrl : function(data)   { return null;   },
                sProcessing : "Processing...",
                payloadType : "string"
            };
            
            return this.each(function(){
                // Merge the options
                if ( options ) {
                    $.extend( settings.events, options.events );
                    $.extend( settings, options );
                }
                
                // Make a copy of the dialog
                var $this  = $(this).clone().appendTo('body'),
                    $alert = $this.find(".modal-footer > .alert").hide(),
                    $form  = $this.find('form');

                if (settings.oRules) {
	                $form.validate({
	                    rules        : settings.oRules,
	                    errorClass   : 'error',
	                    validClass   : 'success',
	                    errorElement : 'span',
	                    highlight    : function(element, errorClass, validClass) {
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
                }
                
                $form.submit(function(event) {
                    event.preventDefault();
                    var data = settings.fnBefore(form2js($form[0], ".", false));

                    if (settings.payloadType == 'string') {
                        data = JSON.stringify(data);
                    }
                    
                    $alert.removeClass("alert-error alert-success");
                    function sendRequest() {
                        $alert.addClass("alert-info").html(settings.sProcessing).show();
                        try {
                            $.ajax({
                                type        : $form.attr('method') ? $form.attr('method') : "POST",
                                url         : settings.fnAction($form.attr("action")), 
                                data        : data,
                                dataType    : "json",
                                contentType : "application/json; charset=utf-8",
                                success     : function(json) {
                                    $alert.removeClass("alert-info").addClass('alert-success').html(settings.sSuccess).show();
                                    
                                    function successHandler() {
                                        settings.fnSuccess(data);
                                        var url = settings.fnSuccessRedirectUrl(data);
                                        if (url) {
                                            window.location = url;
                                        }
                                        else {
                                            $this.modal('hide').remove();
                                        }
                                    }
                                    
                                    if (settings.sSuccessDelay > 0) {
                                        setTimeout(successHandler, settings.sSuccessDelay);
                                    }
                                    else {
                                        successHandler();
                                    }
                                },
                                error       : function(jqXHR, textStatus, errorThrown) {
                                    var obj = jQuery.parseJSON( jqXHR.responseText );
                                    $alert.removeClass("alert-info").addClass('alert-error').html(obj.error).show();
                                    if (obj.error) {
                                        settings.fnFailure(textStatus, obj.error);
                                    } else {
                                        settings.fnFailure(textStatus, obj);
                                    }
                                }
                            });
                        }
                        catch (err) {
                            $alert.removeClass("alert-info").addClass('alert-error').html(err).show();
                            settings.fnFailure(500, err);
                        }
                    }
                    
                    if ($form.valid()) {
                        if (settings.oConfirm) {
                            $.nfConfirm({ 
                                title   : settings.oConfirm.sTitle,
                                text    : settings.oConfirm.fnMessage(data),
                                proceed : sendRequest,
                                fail    : function(err) {
                                    $alert.removeClass("alert-info").addClass('alert-error').show().html(err);
                                    settings.fnFailure(500, err);
                                }
                            });
                        }
                        else {
                            sendRequest();
                        }
                    }
                    return false;
                });
                $this.find('.cancel').on("click", function(event) {
                    event.preventDefault();
                    $this.modal('hide').remove();
                });
                
                function focusFirstElement() {
                    if (settings.sFocusElementName) {
                        var $elem = $this.find("[name='{0}']".format(settings.sFocusElementName));
                        if ($elem) {
                            $elem.focus() 
                            return;
                        }
                    }
                    $this.find('input')[0].focus();
                }
                if (settings.sSourceUrl) {
                    $.ajax({
                            type        : "GET",
                            url         : settings.sSourceUrl,
                            dataType    : "json",
                            contentType : "application/json; charset=utf-8"
                        })
                        .success(function(data) {
                            js2form($form[0], data, null);
                            settings.fnPreShow.apply($form[0], [data]);
                            $this.modal({show:true});
                            focusFirstElement();
                        })
                        .error(function() {
                            
                        });
                }
                else {
                    if (settings.oSourceData) {
                        js2form($form[0], settings.oSourceData, null);
                    }
                    settings.fnPreShow.apply($form, [settings.oSourceData]);
                    $this.modal({show:true});
                    focusFirstElement();
                }
                return this;
            });
        }
    };
    
    $.fn.nfTemplateDialog = function(method) {
        if (methods[method]) {
            return methods[method].apply(this, Array.prototype.slice.call(arguments, 1));
        } else if (typeof method == 'object' || !method) {
            return methods.init.apply(this, arguments);
        } else {
            $.error( 'Method ' + method + ' does not exist on jQuery.nfTemplateDialog');
        }
    };
})(jQuery);