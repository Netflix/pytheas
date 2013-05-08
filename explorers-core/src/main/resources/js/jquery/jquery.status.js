/*!
 *
 */
(function($){
	/**********************************************************************/
	/**  default settings											 	 **/
	/**********************************************************************/
	var defaults = {
		margin : 10,
		torusWidth : 15,
		segments : 6,
		position : "grid",
		colorsOk : 	[ "#8e8", "#8c8", "#8a8" ],
		colorsError : [ "#e00", "#c00", "#a00" ],
		colorsWarn : 	[ "#fe0", "#fc0", "#fa0" ],
		colorInactive : "#ddd",
		colorPulse : "#dde",
		colorPulseMax: "#eef",
		colorGraph: "#44e",
		torusWidthAlert : 20,
		smooth : true,
		graphMode : "change",
		curPulse : 0,
		maxPulse: 1000,
		maxSeenPulse: 0,
		step: 1,
		data : null,
		events : {
			"load" : null,
			"dblclick" : null,
		}
	};
	
	/**********************************************************************/
	/**  Methods													 	 **/
	/**********************************************************************/
	var methods = {
		init : function(options) {
			var self = this;
			
			// Initialize
			return this.each(function(){
				// Merge defaults & options, without modifying the defaults
				options = options || {};
				// options.events = options.events || {};
				
				var $this = $(this),
					data = $this.data('status');
				
				if (!data) {
					$(this).data('status', $.extend({}, defaults, options));
				}
				$(self)
					.status("_calc")
					.status("_init");
			});
		},
		
		setPulse: function(curPulse, maxPulse) {
			var self = this;
			data = this.data('status');

			// REMOVE ME: Makes sure changes aren't too big
			var ratio = curPulse / data.curPulse;
			if (ratio < 0.85) 
				curPulse = data.curPulse * 0.85;
			else if (ratio > 1.15)
				curPulse = data.curPulse * 1.15;

			// Adjust the pulse radius
			var radius = data.pulseRadius * curPulse / maxPulse;
			if (data.smooth) 
				data.circPulse.animate({r: radius}, 100);
			else
				data.circPulse.attr({r: radius});
			
			// Add to graph data
			data.delta.shift();
			if (data.graphMode === "change") 
				data.delta.push(curPulse-data.curPulse);
			
			data.absolute.shift();
			data.absolute.push(curPulse);
			
			// Refresh graph
			data.path.animate({path: $(self).status("_path", data.delta, data.step, data.cx-data.pulseRadius)});
			
			data.curPulse = curPulse;
			data.maxPulse = maxPulse;
			data.maxSeenPulse = Math.max.apply(Math, data.absolute);
			data.circPulseMax.animate({r : data.pulseRadius * data.maxSeenPulse / data.maxPulse }, 100);
			
			return this;
		},
		
		setNodes: function(nodes) {
			var self = this;
			var data = $(self).data('status');
			
			for (var i = 0; i < nodes.length; i++) {
				var node = nodes[i];
				var segment = data.segments[node.id];
				if (segment) {
	               	var color = data.colorNone;
	               	var width = data.torusWidth;
 	               	if (node.status == "NORMAL") {
	               		color = data.colorsOk[i%data.colorsOk.length];
	               	}
	               	else if (node.status == "DOWN") {
                        color = data.colorsError[i%data.colorsError.length];
                        width = data.torusWidthAlert;
	               	}
	               	else if (node.status == "WARNING") {
                        color = data.colorsWarn[i%data.colorsWarn.length];
                        width = data.torusWidthAlert;
	               	}
	                segment.attr({"stroke": color});
				}
			}
			return this;
		},
		
		_init : function() {
			var self = this;
			var data = $(self).data('status');
			data.delta = new Array();
			data.absolute = new Array();
			for (var i = 0; i < data.pulseRadius*2/data.step; i++) {
				data.delta.push(0);
				data.absolute.push(0);
			}
			
			data.canvas = Raphael($(this).attr('id'), data.width, data.height);
			data.path = data.canvas.path($(self).status("_path", data.delta, data.step, data.cx-data.pulseRadius)).attr({"stroke-width": "1px", "stroke":data.colorGraph});
			var center = [ data.cx, data.cy ];
            
			data.circPulse = data.canvas.circle(data.cx, data.cy, 10).attr({
				"fill": data.colorPulse, 
				"stroke-width": "0px"}).toBack();
			data.circPulseMax = data.canvas.circle(data.cx, data.cy, 10).attr({
				"stroke-width": "1px", 
				"stroke": data.colorPulseMax}).toBack();
            data.segments = new Object();
            if (data.nodes.length > 0) {
	            var angle = 360.0 / data.nodes.length;
	
	            for (var i = 0; i < data.nodes.length; i++) {
	            	var node = data.nodes[i];
	            	
	               	var start = Math.floor(i * angle);
	               	var end   = Math.ceil(start + angle);
	               	var color = data.colorNone;
 	               	if (node.status == "NORMAL") {
	               		color = data.colorsOk[i%data.colorsOk.length];
	               	}
	               	else if (node.status == "DOWN") {
                        color = data.colorsError[i%data.colorsError.length];
	               	}
	               	else if (node.status == "WARNING") {
                        color = data.colorsWarn[i%data.colorsWarn.length];
	               	}
	                var segment = data.canvas.path($(self).status("_arc", center, data.R, start, end)).attr({
	                	"stroke": color, 
	                	"stroke-width": data.torusWidth, 
	                	"title": "{0}: {1}".format(node.id, node.statusText)});
	                
	                segment.mouseover(function () {
                        this.animate({"stroke-width": data.torusWidth+10}, 100);
                    }).mouseout(function () {
                        this.animate({"stroke-width": data.torusWidth}, 100);
                    });
	                
	                data.segments[node.id] = segment;
	            }
            }
            else {
            	data.canvas.path($(self).status("_arc", center, data.R, 0, 360)).attr({
            		"stroke": colorNone, 
            		"stroke-width": data.torusWidth});
            }
			data.canvas.text(data.cx, data.cy*3/2, data.title).toFront();
			
			return this;
		},
		
		_calc : function() {
			var self = this;
			var data = $(self).data('status');
			
			data.width  = $(self).innerWidth();
			data.height = $(self).innerHeight();
			data.cx = data.width / 2;
			data.cy = data.height / 2; 
			data.R = Math.min(data.cx, data.cy) - data.torusWidth/2 - data.margin;
			data.pulseRadius = data.R - data.margin;
			return this;
		},
		
		_arc : function(center, radius, startAngle, endAngle) {
			var self = this;
			var data = $(self).data('status');
			
		    var angle = startAngle;
		    var coords = $(self).status("_toCoords", center, radius, angle);
		    var path = "M " + coords[0] + " " + coords[1];
		    while(angle<=endAngle) {
		        coords = $(self).status("_toCoords", center, radius, angle);
		        path += " L " + coords[0] + " " + coords[1];
		        angle += 1;
		    }
		    return path;
		},
			
		_toCoords : function(center, radius, angle) {
		    var radians = (angle/180) * Math.PI;
		    var x = center[0] + Math.cos(radians) * radius;
		    var y = center[1] + Math.sin(radians) * radius;
		    return [x, y];
		},

        _path: function(array, step, left) {
        	var self = this;
			var data = $(self).data('status');
			var height = data.pulseRadius*2;
			var scale = height/(data.maxPulse),
                path = '',          
                x=left,            
                y=data.cy; 
            
			var min = Math.min(array);
			var max = Math.max(array);
			
			if (array.length > 0) {
				path += "M" + [x, (y = data.cy + array[0]*scale)];
				for (var i = 0 ; i < array.length; i++) {
					x += step;
					path += "L" + [x, (y = data.cy + array[i]*scale)];
				}
			}
            return path;
        },
	};
	
	/**********************************************************************/
	/**  Core Method												 	 **/
	/**********************************************************************/
	$.fn.status = function( method ){
		//method calling logic
		if ( methods[ method ] ) {
			return methods[ method ].apply( this, Array.prototype.slice.call( arguments, 1 ) );
		} else if ( typeof method === "object" || ! method ) {
			return methods.init.apply( this, arguments );
		} else {
			$.error( "jQuery.status Error : Method <" + method + "> does not exist" );
		}
	};

}(jQuery));