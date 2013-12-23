(function( $ ){

    function currentTimeInEpochMins() {
        return Math.floor((new Date().getTime())/60000);
    }



    $.fn.cmc = function( method ) {

        if (localStorage) {
            var cmcValue = localStorage.getItem('cmc');
            var expirationInEpochMins = localStorage.getItem('cmc-expire');
            if (currentTimeInEpochMins() > expirationInEpochMins) {
               // expire cmc value
                localStorage.removeItem('cmc');
                localStorage.removeItem('cmc-expire');
            } else {
                this.val(cmcValue);
            }

        }

        var elm = this;
        this.blur(function() {
            if (localStorage) {
                localStorage.setItem('cmc', elm.val());
                localStorage.setItem('cmc-expire', currentTimeInEpochMins() + (4 * 60)); // 4 hours
            }
        });
    };

})( jQuery );