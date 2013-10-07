$(document).ready(function() {
    "use strict";
    document.title = "HelloWorld Explorer";

    $(".breadcrumb").nfBreadcrumbs({
        crumbs : [
            { text : "Countries" }
        ]
    });

    var oTable = $('#country-table').dataTable( {
        "aoColumns": [
            { "sTitle": "Code",  "mDataProp" : "code", "sWidth" : "10%", "sDefaultContent": "-" },
            { "sTitle": "Country",     "mDataProp" : "name", "sWidth" : "90%", "sDefaultContent": "-" }
        ],
        "sAjaxDataProp": "countries",
        "sAjaxSource": "list",
        "bDestroy"       : true,
        "bAutoWidth"     : false,
        "bStateSave"     : true,
        "bPaginate"      : false,
        "bLengthChange"  : false
    });
});