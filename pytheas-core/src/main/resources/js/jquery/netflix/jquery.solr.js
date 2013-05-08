/**
 */
"use strict";

function Solr() {
    /**
     * hosts
     * port
     * entity
     * fnSuccess
     * fnFailure
     * fnDone
     * options
     * action
     */
}

Solr.prototype.select = function(params) {
    var defaults = {
        fnSuccess : function() {},
        fnFailure : function() {},
        fnDone    : function() {},
        options   : {},
        hosts     : [],
        port      : 8000,
        entity    : null,
        action    : "select",
        facets    : null,
        query     : null,
        fields    : "*"         // fl
    };
    
    var facetFields = {
        field : true,
        limit : true
    };
    
    params = $.extend({}, defaults, params);
    params.options["version"] = "2.2";
    params.options["wt"] = "json";
    
    if (params.fields) {
        var fl = params.fields.join(",");
        if (fl == "") 
            fl = "*";
    }
    else {
        fl = "*";
    }
    
    if (params.sort) {
        params.options.sort = params.sort.field + " " + params.sort.direction;
    }
    var index = Math.floor(Math.random()*params.hosts.length);
    var url = "http://{0}:{1}/solr/{2}/{3}?".format(params.hosts[index], params.port, params.entity, params.action) 
            + "&fl=" + fl
            + "&" + $.param(params.options) 
    
    if (params.facets != null && params.facets.length > 0) {
        url += "&facet=true";
        $.each(params.facets, function(index, facet) {
            $.each(facet, function(key, value) {
                if (facetFields[key]) {
                    url += "&facet.{0}={1}".format(key, value);
                }
            });
        });
    }
    if (params.query != null && params.query.length > 0) {
        $.each(params.query, function(index, fq) {
            url += "&fq={0}:{1}".format(fq.field, fq.query);
        });
    }
    
    try {
        $.ajax({
            type        : "GET",
            url         : url, 
            dataType    : 'jsonp',
            jsonp       : 'json.wrf',
            contentType : "application/json; charset=utf-8"
        })
        .done(function(data) {
            try {
                params.fnSuccess(data);
            }
            catch (err) {
                params.fnFailure(err);
            }
        })
        .fail(function(jqXHR, textStatus) {
            params.fnFailure(textStatus);
        })
        .always(function() {
            params.fnDone();
        });
    }
    catch (err) {
        params.fnFailure(err);
    }
};
