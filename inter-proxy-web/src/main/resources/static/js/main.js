window.onload = function() {
    document.getElementById("externalAddressActive").onchange =  function(){
        var isActive = !document.getElementById("externalAddressActive").checked;
        var hostname = document.getElementById("hostname");
        var port = document.getElementById("port");
        var scheme = document.getElementById("scheme");

        // toggle
        hostname.disabled = isActive;
        port.disabled = isActive;
        scheme.disabled = isActive;

        //set default values for debug
        hostname.value = '217.61.23.234';
        port.value = '3128';
        scheme.value = 'http';
    };
};