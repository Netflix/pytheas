$.fn.dataTableExt.oApi.fnReloadAjax = function ( oSettings, sNewSource, fnCallback, bStandingRedraw ) {
	if ( typeof sNewSource != 'undefined' && sNewSource != null ) {
		oSettings.sAjaxSource = sNewSource;
	}
	this.oApi._fnProcessingDisplay( oSettings, true );
	var that = this;
	var iStart = oSettings._iDisplayStart;
	oSettings.fnServerData( oSettings.sAjaxSource, [], function(json) {
		that.oApi._fnClearTable( oSettings );
		var aData = (oSettings.sAjaxDataProp !== "") ?
				that.oApi._fnGetObjectDataFn( oSettings.sAjaxDataProp )( json ) : json;
		for ( var i=0 ; i<aData.length ; i++ ) {
			that.oApi._fnAddData( oSettings, aData[i] );
		}
		oSettings.aiDisplay = oSettings.aiDisplayMaster.slice();
		that.fnDraw();
		if ( typeof bStandingRedraw != 'undefined' && bStandingRedraw === true ) {
			oSettings._iDisplayStart = iStart;
			that.fnDraw( false );
		}
		that.oApi._fnProcessingDisplay( oSettings, false );
		if ( typeof fnCallback == 'function' && fnCallback != null ) {
			fnCallback( oSettings );
		}	
	}, oSettings );
}

jQuery.fn.dataTableExt.oSort['formatted-num-asc'] = function(x,y){
	x = x.replace(/[^\d\-\.\/]/g,'');
	y = y.replace(/[^\d\-\.\/]/g,'');
	if(x.indexOf('/')>=0)x = eval(x);
	if(y.indexOf('/')>=0)y = eval(y);
	return x/1 - y/1;
}
jQuery.fn.dataTableExt.oSort['formatted-num-desc'] = function(x,y){
	x = x.replace(/[^\d\-\.\/]/g,'');
	y = y.replace(/[^\d\-\.\/]/g,'');
	if(x.indexOf('/')>=0)x = eval(x);
	if(y.indexOf('/')>=0)y = eval(y);
	return y/1 - x/1;
}