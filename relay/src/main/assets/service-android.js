document.addEventListener('DOMContentLoaded', function () {
//    ClientMainThread.addPortListener('client-android-service');
});


(function() {

    var Client = self.Client !== 'undefined' ? self.Client : self.Client = function(){};


    Client.processResponse = function(responseString) {
        var args = /^\w+/.exec(responseString);
        if (!args)
            throw new Error("Invalid Command: " + responseString);

        Host.processResponse(responseString);

    };
    // TODO: listen for host execute

    console.log("Android Service loaded: ");
})();
