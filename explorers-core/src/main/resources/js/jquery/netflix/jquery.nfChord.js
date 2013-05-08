/**
 * Display a Chord diagram.  Requires D3
 */

;(function($) {
    "use strict";
    
    var methods = {
        init : function (options) {
            var settings = {
                // data = [ [] ],
                padding:    0.5,
                width:      600,
                height:     600,
                labels:     [],
                linkColor:    "#44C",
                segmentColor: "#448"
            };
            return this.each(function(){
                // Merge the options
                if ( options ) {
                    $.extend( settings.events, options.events );
                    $.extend( settings, options );
                }
                
                // Attach settings to data
                $(this).data('nfChord', {
                       "target" : $(this),
                       "data"   : settings
                   });

                var id = $(this).attr('id');
                
                $(this).html("");
                
                // From http://mkweb.bcgsc.ca/circos/guide/tables/
                var chord = d3.layout.chord()
                    .padding(0.05)
                    .sortSubgroups(d3.descending)
                    .matrix(settings.data);

                var width  = settings.width,
                    height = settings.height,
                    innerRadius = Math.min(width, height) * .31,
                    outerRadius = innerRadius * 1.1;


                var svg = d3.select("#" + id)
                  .append("svg")
                    .attr("width", width)
                    .attr("height", height)
                  .append("g")
                    .attr("transform", "translate(" + width / 2 + "," + height / 2 + ")");

                svg.append("g")
                  .selectAll("path")
                    .data(chord.groups)
                  .enter().append("path")
                    .style("fill",   function(d) { return settings.segmentColor; })
                    .style("stroke", function(d) { return settings.segmentColor; })
                    .attr("d", d3.svg.arc().innerRadius(innerRadius).outerRadius(outerRadius))
                    .on("mouseover", fade(.1))
                    .on("mouseout",  fade(1));

                var ticks = svg.append("g")
                  .selectAll("g")
                    .data(chord.groups)
                  .enter().append("g")
                  .selectAll("g")
                    .data(groupTicks)
                  .enter().append("g")
                    .attr("transform", function(d) {
                      return "rotate(" + (d.angle * 180 / Math.PI - 90) + ")"
                          + "translate(" + outerRadius + ",0)";
                    });

                ticks.append("line")
                    .attr("x1", 1)
                    .attr("y1", 0)
                    .attr("x2", 5)
                    .attr("y2", 0)
                    .style("stroke", "#000");
    
                ticks.append("text")
                    .attr("x",  8)
                    .attr("dy", ".35em")
                    .attr("text-anchor", function(d) { return d.angle > Math.PI ? "end" : null; })
                    .attr("transform",   function(d) { return d.angle > Math.PI ? "rotate(180)translate(-16)" : null; })
                    .text(               function(d) { return d.label; });
    
                svg.append("g")
                    .attr("class", "chord")
                  .selectAll("path")
                    .data(chord.chords)
                  .enter().append("path")
                    .style("fill", function(d) { return settings.linkColor; })
                    .attr("d", d3.svg.chord().radius(innerRadius))
                    .style("opacity", 1);
    
                /** Returns an array of tick angles and labels, given a group. */
                function groupTicks(d) {
                    return [{angle: (d.endAngle + d.startAngle)/2, label: settings.labels[d.index]}];
//                    var k = (d.endAngle - d.startAngle) / d.value;
//                    var ticks = d3.range(0, d.value, 1000).map(function(v, i) {
//                        return {
//                            angle: v * k + d.startAngle,
//                            label: i % 5 ? null : v / 1000 + "k"
//                        };
//                    });
//                    
//                    console.log(ticks);
//                    return ticks;
                }
    
                /** Returns an event handler for fading a given chord group. */
                function fade(opacity) {
                    return function(g, i) {
                        svg.selectAll("g.chord path")
                            .filter(function(d) {
                                return d.source.index != i && d.target.index != i;
                            })
                            .transition()
                            .style("opacity", opacity);
                    };
                }                
                    
                return this;
            });
        }
    };
    
    $.fn.nfChord = function(method) {
        if (methods[method]) {
            return methods[method].apply(this, Array.prototype.slice.call(arguments, 1));
        } else if (typeof method == 'object' || !method) {
            return methods.init.apply(this, arguments);
        } else {
            $.error( 'Method ' + method + ' does not exist on jQuery.nfChord');
        }
    };
})(jQuery);