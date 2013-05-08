/**
 * JQuery plugin that extends the functionality of a simple dropdown list
 * 1.  Populate from AJAX
 * 2.  Filter contents
 */

(function($) {
    "use strict";
    
    var methods = {
        init : function (options) {
            var settings = {
                url       : null,
                label     : "filter",
                hasFilter : false,
                refresh   : true,
                prependList : null,
                initFilter: null,
                initValue : null,
                root      : function(data) { 
                    return data; 
                },
                getText      : function(element) { 
                    if (typeof(element) == "string") 
                        return element;
                    return element.id; 
                },
                getValue  : function(element) { 
                    if (typeof(element) == "string") 
                        return element;
                    return element.id; 
                },
                getMeta: function(element) { 
                    if (typeof(element) == "string")
                        return null;
                    return element; 
                },
                refreshArgs : "",
                events : {
                    onFilterChange    : function(filter) {},
                    onSelectionChange : function(value) {}
                }
            };
            return this.each(function(){
                var $this = $(this),
                    id = $this.attr('id');

                // Merge the options
                settings = $.extend( true, {}, settings, options );
                
                // Attach settings to data
                $this.data('nfSelect', {
                       "target"   : $this,
                       "settings" : settings
                   });

                $this.wrap("<div style='display:inline;' />");
                
                // With filtering
                if (settings.hasFilter) {
                    $("<select id='{0}-hidden' style='display:none'></select>".format(id)).insertAfter($this);
                    
                    // Copy over data from the original select to the hidden one
                    if ( !settings.url ) { 
                        var $hidden = $("#{0}-hidden".format(id));
                        $("#{0} option".format(id)).each(function() {
                            $hidden.append($(this).clone());
                        });
                    }
                    
                    // Add the filter text box
                    var $filter = $("<input class='select-filter span2' type='text' id='{0}-filter' placeholder='{1}' value='{2}'/>"
                            .format(id, settings.label, settings.initFilter ? settings.initFilter : ""))
                            .insertAfter($this);
                    $filter.keyup(function() {
                        settings.events.onFilterChange($filter.val());
                        $this.nfSelect("_applyFilter");
                    });
                }
                
                $this.change(function() {
                    settings.events.onSelectionChange($this.val());
                });
                
                // Load the contents from the URL for the first time
                if (settings.url) {
                    $this.nfSelect("refreshContents");
                }
                
                if (settings.refresh) {
                    $("<a href='#' id='{0}-refresh' style='display:inline-block;'><span class='ui-icon ui-icon-refresh' style='inline-block'>refresh</span></a>".format(id)).insertAfter($this);
                    $("#{0}-refresh".format(id))
                        .click(function() {
                            $this.nfSelect("refreshContents");
                            return false;
                        });
                }
                $("<span id='{0}-count' class='select-counter'>{1}</span>".format(id, $this.find('option').length))
                    .insertAfter($this);
                
                return this;
            });
        },
        
        /**
         * Reload the contents from the last known url 
         */
        refreshContents : function() {
            var $this = $(this),
                url = $(this).data('nfSelect').settings.url;
            
            if (url) {
                if (url.indexOf("?") == -1)
                    url += "?";
                url += "&" + $this.data('nfSelect').settings.refreshArgs;    // TODO: $.params
                $.getJSON(url, function(data) { $this.nfSelect("setContents", data); });            
            }
            return this;
        },
        
        /**
         * Set the current selection
         * @param val
         */
        setSelection : function(value) {
            var $this = $(this);
                
            // Modify the selection if it changed
            var $option = $this.children("option[value='{0}']".format(value));
            if ($option.length > 0) {
                var curr = $this.val();
                $this.val(value);
                if (curr != $this.val()) 
                    $this.change();
            }
            
            return this;
        },
        
        /**
         * Get the current selection
         * @param val
         */
        getSelection : function() {
            var $this = $(this),
                data = $(this).data('nfSelect'),
                id = $(this).attr('id');
                
            return $this.val();
        },
        
        /**
         * Get the metadata for the current selection
         */
        getSelectionMeta : function() {
            var $this = $(this),
                id = $(this).attr('id');
            
            var $option = $("#{0} option[value='{1}']".format(id, $this.val()));
            if ($option.length > 0) {
                return $option.data('nfSelectOption');
            }
            return null;
        },

        /**
         * Re-populate the list contents from the specified url
         * @param url
         */
        loadUrl : function(url) {
            var $this = $(this),
                data = this.data('nfSelect');
            
            data.settings.url = url;
            $this.nfSelect("refreshContents");
            return this;
        },
        
        /**
         * Set the contents of the select and re-apply the filter
         * @param data
         */
        setContents : function(data) {
            var id = $(this).attr('id'),
                $this = $(this),
                $list = $("#{0}-hidden".format(id)),
                settings = $this.data('nfSelect').settings;

            // Clear the hidden list and read in options from the data
            $list.find('option').remove().end();
            if(settings.prependList) {
                $.each(settings.prependList, function(key, value) {
                    $list.append("<option value='{0}'>{1}</option>".format(key, value));
                });
            }
            $.each(settings.root(data), function(index, element) {
                var $option = $("<option value='{0}'>{1}</option>".format(
                        settings.getValue(element), 
                        settings.getText(element)))
                    .appendTo($list);
                $option.data("nfSelectOption", settings.getMeta(element));
            });
            
            $this.nfSelect("_applyFilter");
            return this;
        },
        
        /**
         * Clear all the contents 
         */
        clearContents : function(data) {
            var id = $(this).attr('id');
            
            $("#{0}-hidden".format(id)).find('option').remove().end();
            $(this).find('option').remove().end();
            $("#{0}-filter".format(id)).val("");
            return this;
        },
        
        /**
         * Programmatically set the filter.
         */
        setFilter : function(filter) {
            var id        = $(this).attr('id'),
                $filter = $("#{0}-filter".format(id));
            
            $filter.val(filter);
            $(this).nfSelect("_applyFilter");
            return this;
        },
        
        /**
         * Apply the filter to the list
         */
        _applyFilter : function() {
            var id        = $(this).attr('id'),
                $this  = $(this),
                data   = $(this).data('nfSelect'),
                filter = $("#{0}-filter".format(id)).val(),
                prev   = (data.settings.initValue != null) ? data.settings.initValue : $this.val();
            
            // Clear the list, iterate through the hidden options and copy over only those that match the filter
            $this.find('option').remove().end();
            $("#{0}-hidden option".format(id)).each(function() {
                var match = (filter == null || filter == "")
                        ? true 
                        : ($(this).text().search(new RegExp(filter, "i")) != -1);
                if (match) {
                    $this.append($(this).clone().data("nfSelectOption", $(this).data("nfSelectOption")));
                }
            });
            
            // Update the text count
            $("#{0}-count".format(id)).text($this.find('option').length);

            // Modify the selection if it changed
            if (prev != null) {
                // Had a previous selection
                var $option = $this.children("option[value='{0}']".format(prev));
                if ($option.length > 0) {
                    // Still in the list so select it
                    $this.val(prev);
                    if (data.settings.initValue != null)
                        $this.change();
                }
                else {
                    // No longer in the list 
                    $this.change();
                }
            }
            else {
                // List was previously empty
                if ($this.val() != null) 
                    $this.change();
            }
            
            data.settings.initValue = null;
            return this;
        }
    };
    
    $.fn.nfSelect = function(method) {
        if (methods[method]) {
            return methods[method].apply(this, Array.prototype.slice.call(arguments, 1));
        } else if (typeof method == 'object' || !method) {
            return methods.init.apply(this, arguments);
        } else {
            $.error( 'Method ' + method + ' does not exist on jQuery.nfSelect');
        }
    };
})(jQuery);