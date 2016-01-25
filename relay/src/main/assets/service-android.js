document.addEventListener('DOMContentLoaded', function () {
//    Client.addPortListener('client-android-service');
});


(function() {
    console.log("Starting Android Service");

    var Client = typeof self.Client !== 'undefined' ? self.Client : self.Client = function(){};


    Client.processResponse = function(responseString) {
        var args = /^\w+/.exec(responseString);
        if (!args)
            throw new Error("Invalid Command: " + responseString);

        try {
            Host.processResponse(responseString);
        } catch (e) {
            console.error(e);
        }
    };

    console.log("Android Service loaded");
})();
