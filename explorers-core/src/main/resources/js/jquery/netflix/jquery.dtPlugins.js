$(document).ready(function() {
    "use strict";
    
    $.extend( $.fn.DataTable.defaults, {
        "fnPostProcessServerData" : function(json, fnCallback) {
            fnCallback(json);
        },
        "fnServerData": function ( sSource, aoData, fnCallback ) {
            var settings,
                $error;
            if (this.fnSettings) {
                settings = this.fnSettings();
                $error = this.parent().find(".dataTables_error");
            }
            else { 
                settings = this;
                $error = $(settings.nTableWrapper).find(".dataTables_error");
            }
            $error.html("");
            $.getJSON( sSource )
                .done(function (json) { 
                    if (!json[settings.sAjaxDataProp]) {
                        json[settings.sAjaxDataProp] = [];
                    }
                    
                    if (settings.oInit.fnPostProcessServerData)
                        settings.oInit.fnPostProcessServerData(json, fnCallback);
                    else 
                        fnCallback(json);
                })
                .fail(function(jqXHR, textStatus, errorThrown) {
                    var json = {};
                    json[settings.sAjaxDataProp] = [];
                    if (settings.oInit.fnPostProcessServerData)
                        settings.oInit.fnPostProcessServerData(json, fnCallback);
                    else 
                        fnCallback(json);
                    
                    $error.html(textStatus + " : " + errorThrown);
                });
       }
   });
   
   /*
    Options:
        sTitle - 
        bAjaxRefresh - Show the ajax refresh
        aoFields [{
            sType  - "input", "button", "checkbox", "label"
            sClass - class name
            sText  - text 
            sIcon  - icon name
            fnHandler - click event handler
        }]
    */
   $.fn.dataTableExt.aoFeatures.push( {
        "fnInit": function( oSettings ) {
            var nInfo = document.createElement( 'div' );
            nInfo.className = "table-header form-inline";
            
            if (oSettings.oInit.oHeader) {
                if (oSettings.oInit.oHeader.sTitle) {
                    $(nInfo).append("<h1 class='left'>{0}</h1>".format(oSettings.oInit.oHeader.sTitle));
                }
                if (oSettings.oInit.oHeader.bAjaxRefresh) {
                    $("<a class='btn refresh-btn' style='display:block-inline;' href='#'><i class='icon-refresh'></i> Refresh</a>").appendTo($(nInfo)).click(function(e) {
                        e.preventDefault();
                        oSettings.oInstance.fnReloadAjax();
                    });
                }
                if (oSettings.oInit.oHeader.aoFields) {
                    $.each(oSettings.oInit.oHeader.aoFields, function(index, field) {
                        if (field.sType == "input") {
                            $(nInfo).append("<input type='text' class='{0}' name='{1}' {2}></input>".format(
                                    field.sClass ? field.sClass : "span1",
                                    field.sName, 
                                    field.sPlaceholder ? " placeholder='{0}'".format(field.sPlaceholder) : ""));
                        }
                        else if (field.sType == "list") {
                            var $label = $("<label class='{0}'>{1} </label>".format(
                                    field.sClass ? field.sClass : "",
                                    field.sText)).appendTo($(nInfo));
                            var $select = $("<select name='{0}'></select>".format(field.sName ? field.sName : "")).appendTo($label)
                                .change(field.fnHandler);
                            $.each(field.oOptions, function(value, text) {
                                $select.append("<option value='{0}'>{1}</option>".format(value, text));
                            });
                        }
                        else if (field.sType == "checkbox") {
                            
                        }
                        else if (field.sType == "button") {
                            var $div;
                            var $style='';
                            if (field.bAnchor) {
                                $div = $(nInfo).children("input:last-child").wrap("<div class='input-append'></div>").parent();
                                $style = 'margin:0px;margin-left:-1px;';
                            }
                            else {
                                $div = $(nInfo);
                            }
                            
                            $("<button class='{0} btn' type='button' style='{1}'>{2}</button>".format(
                                field.sClass ? field.sClass : "",
                                $style,
                                field.sText)).appendTo($div).on("click", function(e) { field.fnHandler(e); e.preventDefault(); });
                        }
                        else if (field.sType == "label") {
                            $(nInfo).append("<label class='{0}' style='display:block-inline;'>{1}</label>".format(
                                    field.sClass ? field.sClass : "span1",
                                    field.sText));
                        }
                    });
                }
            }
            else {
                if (oSettings.oInit.sTitle) {
                    $(nInfo).append("<h1 class='left'>{0}</h1>".format(oSettings.oInit.sTitle));
                }
                if (oSettings.oInit.bAjaxRefresh) {
                    $(nInfo).append("<a class='btn refresh-btn left' style='display:block-inline;' href='#'><i class='icon-refresh'></i> Refresh</a>").click(function(e) {
                        e.preventDefault();
                        oSettings.oInstance.fnReloadAjax();
                    });
                }
            }
            $(nInfo).appendTo("<span class='dataTables_error'></span>");
            return nInfo;
        },
        "cFeature": "H",
        "sFeature": "Header"
    } );
});