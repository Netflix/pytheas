/**
 * Wrapper for epic
 */
(function ($) {
    "use strict";
    
	$.epic = {
		/** 
		 * List all the EPIC dataset for a given group
		 * @param parms
		 */
		listDatasets: function(params) {
			var defaults = {
				env : "test",
				port : "7001",
				region : "us-east-1"
			};
			
			var options = $.extend({}, defaults, params);

			var url = "http://epic_backend_proxy.{0}.{1}.netflix.com:{2}/epic?ds_list=1&a=query&n={3}.aws{4}&jsonp=?".format(
				options.region,
				options.env,
				options.port,
				options.group,
				options.env
			);
			
			$.getJSON(url, options.success);
		},
		
		/**
		 * Retrieve JSON data fro a single dataset
		 */
		getDataset: function(params) {
			var defaults = {
				env : "test",
				port : "7001",
				region : "us-east-1",
				cluster : null,
				dataset : null,
				start : null,
				width : 700,
				height : 200,
				success : function() {
					
				},
				error : function() {
					
				}
			};
		
			var options = $.extend({}, defaults, params);
			
            $.ajax({
            	url : "http://epic_backend_proxy.${Global.currentRegion}.${Global.environmentName}.netflix.com:7001/epic?a=json&ds={0}&ng={1}&s=e-{2}&cf=avg&w={3}&h={4}&v=2&jsonp=?".format(
                    options.dataset,
                    options.cluster,
                    options.start,
                    options.width,
                    options.height),
                success : options.success,
                error : options.error
            });
		}
		
	};
    
})(jQuery);