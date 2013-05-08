;(function($) {
    var methods = {
        init : function (options) {
            "use strict";
            var settings = {
                aoFields    : [],
                sName       : "",
                sAddLabel   : "Add",
                sClearLabel : "Clear",
                sResetLabel : "Reset",
                sLinePrefix : "<label> </label>",
                fnLabel     : function(index) { return ""; },
                bShowHeader : true,
                bEditMode   : false,
                sDelimiter  : ".",
                sDeletedFieldName : "deleted",
                count       : 0,
                _defaultFieldSettings : {
                    width       : 1,
                    sType       : "text",
                    sClass      : "",
                    sPlaceholder: "",
                    sDefault    : "",
                    sTitle      : ""
                }
            };
            
            return this.each(function(){
                var $this = $(this);
                // Merge the options
                if ( options ) {
                    $.extend( settings.events, options.events );
                    $.extend( settings, options );
                }
                
                // Attach settings to data
                var $data = settings;
                $this.data('nfDynamicFormList', $data);
                $this.addClass("dynamic-form-list");

                // Create header div
                if ($data.bShowHeader) {
                    var $header = $("<div class='form-inline header ui-helper-clearfix'>{1}</div>"
                            .format($data.sName.replace(".", "-"), $data.sLinePrefix))
                            .appendTo($this);
                    
                    $.each($data.aoFields, function(index, field) {
                        var oField = $.extend({}, $data._defaultFieldSettings, field);
                        $("<span class='span{0}'>{1}</span>"
                           .format(oField.width , oField.sTitle)).appendTo($header);
                    });                
                }
                
                // Create contents div
                var $contents = $("<div class='contents ui-helper-clearfix'></div>").appendTo($this);
                
                // Create controls
                var $controls = $("<div class='form-inline actions ui-helper-clearfix'>{1}</div>"
                        .format($data.sName.replace(".", "-"), $data.sLinePrefix))
                        .appendTo($this);
                $data.controls = $controls;
                $data.contents = $contents;
               
                var $add = $controls.append("<button class='btn right'>{0}</button> ".format($data.sAddLabel)).children("button");
                $add.click(function() {
                    try {
                        $this.nfDynamicListForm("_insertInternal", {}, "new");
                        var oField = $data.aoFields[0];
                        $("input[name='{0}[{1}]{2}{3}']".format($data.sName, $data.count-1, $data.sDelimiter, oField.sName)).focus();
                    }
                    catch (err) {
                        debugMessage(err);
                    }
                    return false;
                });
                
                $add.after("<button class='btn right'>{0}</button> ".format($data.sClearLabel)).next()
                    .click(function() {
                        try {
                            $this.nfDynamicListForm("clear");
                        }
                        catch (err) {
                            
                        }
                        return false;
                     });
                
                $add.after("<button class='btn right'>{0}</button> ".format($data.sResetLabel)).next()
                    .click(function() {
                        try {
                            $this.nfDynamicListForm("reset");
                        }
                        catch (err) {
                            
                        }
                        return false;
                     });
                
                if ($data.data) {
                    $.each($data.data, function(index, obj) {
                        $this.nfDynamicListForm("_insertInternal", obj, "edit");
                    });
                }
                return this;
            });
        },
        
        clear : function() {
            var $data = $(this).data('nfDynamicFormList');
            $(this).find(".control-group.new").remove();
            
            $.each($(this).find(".contents .control-group"), function(index, row) {
                $(this).addClass("deleted");
                $(this).find("i").removeClass("icon-remove").addClass("icon-plus");
                $(this).find(".delete").val(true);
            });
            return this;
        },

        reset : function() {
            var $data = $(this).data('nfDynamicFormList');
            $(this).find(".control-group.new").remove();
            $.each($(this).find(".contents .control-group"), function(index, row) {
                $(this).removeClass("deleted");
                $(this).find("i").removeClass("icon-plus").addClass("icon-remove");
                $(this).find(".delete").val(false);
            });
            return this;
        },
        
        _relabel : function() {
            var $this = $(this);
            var $data = $(this).data('nfDynamicFormList');
            
            $(this).find(".control-group").each(function(index, item) {
                $(item).children(".caption").html($data.fnLabel(index));
            });
            
            return this;
        },
        
        insert : function(values) {
            return $(this).nfDynamicListForm("_insertInternal", values, "new");
        },
        
        _insertInternal : function(values, type) {
            var $this = $(this);
            var $data = $(this).data('nfDynamicFormList');
            $row = $("<div class='form-inline control-group {0}'></div>".format(type)).appendTo($data.contents);
            
            var sBaseName = "{0}[{1}]{2}".format($data.sName, $data.count, $data.sDelimiter)
            var $deleted = $("<input type='hidden' name='{0}{1}' class='delete'></input> ".format(
                    sBaseName, 
                    $data.sDeletedFieldName
                )).appendTo($row);
            
            $.each($data.aoFields, function(index, field) {
                var oField = $.extend({}, $data._defaultFieldSettings, field);
                var sName = "{0}{1}".format(sBaseName, oField.sName)
                
                if (oField.sFieldType == "text") {
                    $("<input type='text' class='{0} span{1}' name='{2}' placeholder='{3}' value='{4}'></input> ".format(
                            oField.sClass, 
                            oField.width,
                            sName,
                            oField.sPlaceholder,
                            values[oField.sName] ? values[oField.sName] : oField.sDefault)
                     ).appendTo($row);
                }
                else if (oField.sFieldType == "select") {
                    var $list = $("<select name='{0}' class='{1} span{2}'></select> ".format(
                            sName, 
                            oField.sClass,
                            oField.width
                         )).appendTo($row);
                    $.each(oField.aOptions, function(index, element) {
                        $("<option value={0}>{1}</option>".format(element[0], element[1])).appendTo($list);
                    });
                    if (values[oField.sName]) {
                        $list.val(values[oField.sName]);
                    }
                }
                else {
                    $("error").appendTo($row);
                }
            });
            $data.count++;
            $("<a href='#'><i class='icon-remove'><i></a>").appendTo($row).click(function() {
                if (!$data.bEditMode || $(this).parent().hasClass("new")) {
                    $(this).parent().remove();
                    $this.nfDynamicListForm("_relabel", {});
                }
                else {
                    $(this).parent().toggleClass("deleted");
                    $deleted.val($(this).parent().hasClass("deleted"));
                    if ($(this).parent().hasClass("deleted")) {
                        $(this).find("i").removeClass("icon-remove").addClass("icon-plus");
                    }
                    else {
                        $(this).find("i").addClass("icon-remove").removeClass("icon-plus");
                    }
                }
                return false;
            });
            
            $this.nfDynamicListForm("_relabel", {});
            return this;
        }
    };
    
    $.fn.nfDynamicListForm = function(method) {
        if (methods[method]) {
            return methods[method].apply(this, Array.prototype.slice.call(arguments, 1));
        } else if (typeof method == 'object' || !method) {
            return methods.init.apply(this, arguments);
        } else {
            $.error( 'Method ' + method + ' does not exist on jQuery.nfDynamicListForm');
        }
    };
})(jQuery);