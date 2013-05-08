/**
 * [ Select ] [ Attach ] [ Start Upload ] [ Cancel ]
 * [ filename ] [ size ] [ File Type ] 
 * [ Progress ] [ Remaining ] [ Bytes Transferred ] [ Speed ] 
 */

;(function($) {
    "use strict";
    
    var methods = {
        init : function (options) {
            var settings = {
                sUrl : null,
                iMaxFileSize : 0,
                fnSuccessHandler : function(e) { return "Done!"; },
                fnErrorHandler : function(e) { },
                fnAbortHandler : function(e) { },
                fnFormatFilename : function(oFile) { return oFile.name + "  (" + oFile.size/1000000.0 + " MB;  " + oFile.type + ")"; }
            };
            return this.each(function(){
                // Merge the options
                if ( options ) {
                    $.extend( settings.events, options.events );
                    $.extend( settings, options );
                }
                
                var $this = $(this),
                    $filename = $this.find("input[type='file']"),
                    $select   = $this.find("button[name='select']"),
                    $upload   = $this.find("button[name='attach']").attr("disabled", true),
                    $cancel   = $this.find("button[name='abort']").attr("disabled", true),
                    $progress = $this.find(".progress"),
                    $bar      = $this.find(".bar");
                
                $select.click(function(e) {
                    e.preventDefault();
                    $filename.click();
                    $upload.attr("disabled", false);
                });
                
                // Select a file
                $this.find("input[type='file']").change(function(e) {
                    var oFile = this.files[0];
                    // little test for filesize
                    if (settings.iMaxFileSize > 0 && oFile.size > settings.iMaxFileSize) {
                        $this.nfFileUpload("_showError", "File too big");
                        return;
                    }
                    $this.find("input[name='filename']").val(oFile.name);
                    $this.find("input[name='filesize']").val(oFile.size);
                    $this.find("input[name='contenttype']").val(oFile.type);
                    $upload.attr("disabled", false);
                    $progress.hide();
                });

                // Upload the selected file
                $upload.click(function(e) {
                    e.preventDefault();
                    $cancel.attr("disabled", false);
                    
                    // check a file is selected
                    var filename = $this.find("input[type='file']").val();
                    if (filename == '') {
                        $this.nfFileUpload("_showError", "Please select a file first");
                        return;
                    }
                    
                    $this.nfFileUpload("_hideMessage");
                    
                    $progress.show();
                    $bar.width("0px");

                    // create XMLHttpRequest object, adding few event listeners, and POSTing our data
                    var oXHR = new XMLHttpRequest();
                    oXHR.upload.addEventListener('progress', function(e) { // upload process in progress
                            if (e.lengthComputable) {
                                var iPercentComplete = Math.round(e.loaded * 100 / e.total);
                                $bar.width(iPercentComplete + "%");
                                if (iPercentComplete == 100) {
                                    $this.nfFileUpload("_showMessage", "info", "Please wait... processing...");
                                }
                            } else {
                                $this.nfFileUpload("_showMessage", "error", "unable to compute");
                            }
                        } , false);
                    
                    oXHR.addEventListener('load',  function(e) { // upload successfully finished
                            if (e.target.status == 200) {
                                $this.nfFileUpload("_showMessage", "success", settings.fnSuccessHandler(e));
                            }
                            else {
                                $this.nfFileUpload("_showMessage", "error", e.target.statusText);
                            }
                        }, false);
                    
                    oXHR.addEventListener('error', function(e) { // upload error
                            $this.nfFileUpload("_showMessage", "error", "error...");
                            settings.fnErrorHandler(e);
                        } , false);
                        
                    oXHR.addEventListener('abort', function(e) { // upload abort
                            $this.nfFileUpload("_showMessage", "info", "aborted");
                            settings.fnAbortHandler(e);
                        }, false);
                    
                    var url = settings.sUrl;
                    if (!url)
                        url = $this.attr("action");
                    oXHR.open('POST', url);// '../v1/hermes/FileUpload');
                    oXHR.send(new FormData($this[0]));
                });
                
                $cancel.click(function(e) {
                    e.preventDefault();
                });
                
                return this;
            });
        },
        
        _showMessage : function(level, message) {
            $(this).find(".alert").show().removeClass("alert-info alert-error alert-success").addClass("alert-" + level).html(message);
            return this;
        },
        
        _hideMessage : function(level, message) {
            $(this).find(".alert").hide().html("");
        }
    };
    
    $.fn.nfFileUpload = function(method) {
        if (methods[method]) {
            return methods[method].apply(this, Array.prototype.slice.call(arguments, 1));
        } else if (typeof method == 'object' || !method) {
            return methods.init.apply(this, arguments);
        } else {
            $.error( 'Method ' + method + ' does not exist on jQuery.nfFileUpload');
        }
    };
})(jQuery);