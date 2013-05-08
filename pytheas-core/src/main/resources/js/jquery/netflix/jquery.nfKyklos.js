/**
 * 
 */
(function($) {
    "use strict";
    
	function getHeapPct(d) {
		if (d.heap_memory_mb) {
			var parts = d.heap_memory_mb.split('/');
			if (parts.length == 2)
				return parts[0] / parts[1];
		}
		return 0.0;
	}
	
	var methods = {
		init : function (options) {
			var settings = {
				"url"    : null,
				"svg"	 : {labels:[]},
				"focus"  : null,
				"dr"	 : 25,
				"has_alarm" : false,
				"last_updated" : 0,
				"busy" : false,
				"cycle_index" : 0,
				
				"bAutoHide" : true,
				"bAutoCycle" : true,
				"bStalenessIndicator" : true,
				"sHeapThreshold" : 0.75,
				"oCycleDelay" : 5000,
				"oStickInfo" : true,
				"oAutoRefresh" : 30000,
				"fnGetLabelCount" : function(d, setting) { return 1; },
				"fnGetLeftText" : function(d, setting) { return "?"; },
				"fnGetLeftClass" : function(d, setting) { return "?"; },
				"fnGetCenterText" : function(d, setting) { return "?"; },
				"fnGetCenterClass" : function(d, setting) { return "?"; },
				"fnGetRightText" : function(d, setting) { return "?"; },
				"fnGetRightClass" : function(d, setting) { return "?"; },
				"fnGetRoot" : function(d, settings) { return d; },
				"fnGetLastUpdateTime" : function(d, settings) { return 0; },
				"fnGetText" : function(d, setting) {
					var ar = ["?"];
				},
				"fnGetArcClass" : function(d, setting) { return "?"; },
				"fnIsAlert" : function(d, setting) { return "?"; },
				"fnGetArcId" : function(d, setting) { return "?"; }
			};
			
			return this.each(function(){
				var $this = $(this),
					id = $this.attr('id');

				// Merge the options
				if ( options ) {
			        settings = $.extend( true, {}, settings, options );
				}
				
				$this.data('nfKyklos', settings);

				var $data = settings;
				
				$data.h = $(this).height();
				$data.w = $(this).width();
				$data.r = Math.min($data.w, $data.h) / 2 - 6;
				$data.innerR = $data.r - $data.dr - 4;

				$data.vis = d3.select("#" + id).append("svg")
					.attr("width", $data.w)
					.attr("height", $data.h)
					.append("g")
						.attr("transform", "translate(" + $data.w / 2 + "," + $data.h / 2 + ")");
				
				$data.status_text = $data.vis
						.append("text")
							.attr("class", "node")
							.text("loading...");
				
				$data.none_circle = $data.vis
						.append("path")
							.attr("d", d3.svg.arc().innerRadius($data.r-$data.dr).outerRadius($data.r).startAngle(0).endAngle(Math.PI*2))
							.attr("fill", "#ddd");
					
				// Info group displayed in the center of the Kyklo
				$data.svg.info = $data.vis.append("g")
					.attr("opacity", "0");

			    var r = $data.innerR - 1,
			        dx = 36,
				    dy = 12,
				    x1 = Math.sqrt(r*r - dy*dy),
				    y1 = -dy,
				    x2 = x1,
				    y2 = dy;
			    
				// Highlight circle
				$data.svg.center = $data.svg.info.append("circle")
					.attr("r", $data.innerR);

			    $data.svg.rightStatus = $data.svg.info.append("path")
			    	.attr("d", "M{0},{1} a{2},{3} 0 0,1 {4},{5} L{6},{7} L{8},{9} z".format(x1,y1, r, r, x1-x2,y2-y1, dx,dy, dx,-dy));
	
			    $data.svg.leftStatus = $data.svg.info.append("path")
					.attr("d", "M{0},{1} a{2},{3} 0 0,0 {4},{5} L{6},{7} L{8},{9} z".format(-x1,y1, r,r, x1-x2,y2-y1, -dx,dy, -dx,-dy));
				    
			    $data.svg.centerStatus = $data.svg.info.append("rect")
					.attr("x", -dx+2)
					.attr("y", -dy)
					.attr("width", dx * 2-4)
					.attr("height", dy * 2);
			    
			    var n = $data.fnGetLabelCount,
			        pos = -Math.ceil(n/2) * 2;

			    var i;
			    for (i = 0; i < n; i++) {
			    	$data.svg.labels.push(
			    		$data.svg.info.append("text")
							.attr("class", "node")
							.attr("y", "{0}em".format(pos))
							.text("node"));
			    	if (pos + 2 >= 0 && pos < 0)
			    		pos += 3;
		    		pos += 2;
			    }
			    
				$data.svg.leftText = $data.svg.info.append("text")
					.attr("class", "node")
					.attr("y", "4")
					.attr("x", "-50")
					.text("D");
				$data.svg.centerText = $data.svg.info.append("text")
					.attr("class", "node")
					.attr("y", "4")
					.attr("x", "0")
					.text("centerText");
				$data.svg.rightText = $data.svg.info.append("text")
					.attr("class", "node")
					.attr("y", "4")
					.attr("x", "50")
					.text("node");
				
				$data.svg.staleness = $data.vis.append("text")
					.attr("class", "staleness")
					.attr("x", -$data.r)
					.attr("text-anchor", "left")
					.attr("y", $data.r)
					.text("none");
				
				$data.svg.node_count = $data.vis.append("text")
					.attr("class", "node-count")
					.attr("x", $data.r-20)
					.attr("y", $data.r)
					.text("0");
				
				$this.nfKyklos("reload");
		        
		        // Staleness timer
		        if ($data.oAutoRefresh != null && settings.bStalenessIndicator) {
		        	(function() {
	        			setTimeout(arguments.callee, 1000);
	        			
	        			if ($data.last_updated != 0) {
				            var diff = Math.ceil((new Date().getTime() - $data.last_updated)/1000);
				            var opacity = Math.min(1, Math.max(0, (diff * 1000)/(3 * $data.oAutoRefresh) - 1)) ;
				            $data.svg.staleness
				            	.text("{0} s".format(diff))
				            	.attr("opacity", opacity);
	        			}
	        			else {
				            $data.svg.staleness
			            	.text("- s")
			            	.attr("opacity", 1.0);
	        			}
			        })();
		        }
		   	});
		},
		
		hasAlarm : function() {
			var $this = this,
				$data = $(this).data('nfKyklos');
			
			return $data.has_alarm;
		},
		
		/**
		 * Called after the ajax reload completes
		 */
		_reloadDone : function() {
			var $this = this,
				$data = $(this).data('nfKyklos');
			
			$data.busy = false;
			
	        // Update timer
        	if ($data.oAutoRefresh != null) {
    			setTimeout(function() {
    				$this.nfKyklos("reload");
    			}, $data.oAutoRefresh);
        	}
		        
			return this;
		},
		
		/**
		 * Reload from the url and redraw
		 */
		reload : function() {
			var $this = this,
				$data = $(this).data('nfKyklos');
	
			if ($data.busy) {
				return this;
			}
			
			$data.busy = true;
			d3.json($data.url, function(json) { 
				$data.cycle = [];
				
				if (json == null) {
					$data.none_circle.attr("display", null);
					$data.status_text.attr("display", null).text("Error getting data");
					$data.has_alarm = true;
				}
				else {
					var root = $data.fnGetRoot(json, $data);
					$data.svg.node_count
						.text(root.length);
					
					// Figure out the arc sizes
					var angle = d3.scale.linear().domain([0, root.length]).range([0, 2 * Math.PI]);
					var arc = d3.svg.arc()
					    .startAngle(function(d,i)  { return angle(i); })
					    .endAngle(function(d,i)    { return angle(i+1) - 0.03; })
					    .innerRadius(function(d,i) { return d.focus ? $data.r-$data.dr-4 : $data.r - $data.dr; })
					    .outerRadius(function(d,i) { return d.focus ? $data.r+5 : $data.r; });
	
					// Select all datapoints
					var path = $data.vis.selectAll(".arc")
							.data(root, function(d) { return $data.fnGetArcId(d, $data); });
					
					// Adding new arcs (nodes)
					path.enter().append("path")
							.attr("d", arc)
							.attr("class", function(d) { return "arc arc-" + $data.fnGetArcClass(d, $data); });
					
					// Removing old nodes
					path.exit().remove();
	
					// Hide the status if there are no more error nodes
					$data.none_circle.attr("display", (root.length == 0) ? null : "none");
					$data.status_text.attr("display", (root.length == 0) ? null : "none");
						
					// Update the state of all existing nodes
					$data.vis.selectAll(".arc")
						.each(function(d, i) {
							d3.select(this)
								.attr("d", function() { return arc(d,i);})
								.attr("class", function() { return "arc arc-" + $data.fnGetArcClass(d, $data); })
								.on('mouseover', function() { arcSelect(d, i, this); })
								.on('mouseout', function() { if (!$data.oStickInfo) arcDeselect(d, i, this); })
								.on('click', function() { 
								});
							
							if ($data.fnIsAlert(d, $data)) {
								$data.cycle.push({d: d, i: i, it : this });
							}
						});
						
					if ($data.cycleTimer) {
						clearTimeout($data.cycleTimer);
						$data.cycleTimer = 0;
					}
					
					$data.has_alarm = ($data.cycle.length > 0);
					
					if ($data.bAutoCycle) {
						if ($data.has_alarm) {
							$data.cycleTimer = setTimeout(function() {
								if ($data.cycle.length > 1)
									$data.cycleTimer = setTimeout(arguments.callee, $data.oCycleDelay);
								if ($data.cycle.length > 0) {
									var d = $data.cycle[$data.cycle_index++ % $data.cycle.length];
									arcSelect(d.d, d.i, d.it);
								}
							}, 1);
						}
						else {
						    $data.svg.info.attr("opacity", 0.0);
						}
					}
					
					$data.last_updated = $data.fnGetLastUpdateTime(json, $data);
					
					function arcTween(a, i) {
						return function(t) {
							return arc(a, i);
						};
					}
	
					function arcSelect(d, i, it) {
						if ($data.focus) {
							$data.focus.d.focus = false;
							 d3.select($data.focus.it).attr('d', arc($data.focus.d, $data.focus.i)); 
							$data.focus = null;
						}
						
						$data.focus = { "d" : d , "i" : i, "it" : it }; 
						d.focus = true;
						
						d3.select(it).attr('d', arc(d, i)); 
					    
					    $data.svg.center
					    	.attr("class", function() { return "circle-" + $data.fnGetArcClass(d, $data); });
					    
					    $data.svg.info
					    	.attr("opacity", function(d, i, a) { return d3.interpolate(a, 1.0); });
						
					    var labels = $data.fnGetText(d, $data);
					    for (var i = 0; i < labels.length; i++) {
					    	$data.svg.labels[i].text(labels[i]);
					    }

					    $data.svg.centerText.text($data.fnGetCenterText(d, $data));
					    $data.svg.leftText.text($data.fnGetLeftText(d, $data));
					    $data.svg.rightText.text($data.fnGetRightText(d, $data));
					    
					    $data.svg.centerStatus.attr("class", "arc-" + $data.fnGetCenterClass(d, $data));
					    $data.svg.leftStatus.attr("class", "arc-" + $data.fnGetLeftClass(d, $data));
					    $data.svg.rightStatus.attr("class", "arc-" + $data.fnGetRightClass(d, $data));
					}
					
					function arcDeselect(d, i, it) {
						$data.mouseout_timer = setTimeout(function() {
							d.focus = false;
							d3.select(it).attr('d', arc(d, i)); 
							if ($data.focus && $data.focus.it == it) {
							    $data.svg.info.attr("opacity", 0.0);
							}
						}, 100);
					}
				}
				
				$this.nfKyklos("_reloadDone");
			}); 
			
			return this;
		}
	};
	
	$.fn.nfKyklos = function(method) {
		if (methods[method]) {
			return methods[method].apply(this, Array.prototype.slice.call(arguments, 1));
		} else if (typeof method == 'object' || !method) {
			return methods.init.apply(this, arguments);
		} else {
			$.error( 'Method ' + method + ' does not exist on jQuery.nfKyklos');
		}
	};
})(jQuery);