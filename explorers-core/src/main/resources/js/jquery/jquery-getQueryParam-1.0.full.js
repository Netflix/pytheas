// jQuery getQueryParam Plugin 1.0.0 (20100429)
// By John Terenzio | http://plugins.jquery.com/project/getqueryparam | MIT License
(function ($) {
	// jQuery method, this will work like PHP's $_GET[]
	$.getQueryParam = function (param) {
		// get the pairs of params fist
		var pairs = location.search.substring(1).split('&');
		// now iterate each pair
		for (var i = 0; i < pairs.length; i++) {
			var params = pairs[i].split('=');
			if (params[0] == param) {
				// if the param doesn't have a value, like ?photos&videos, then return an empty srting
				return params[1] || '';
			}
		}
		//otherwise return undefined to signify that the param does not exist
		return undefined;
	};
})(jQuery);