document.addEventListener('DOMContentLoaded', function () {
    Client.execute("UI.LOGIN");
    Client.execute("SETTINGS.AUTORUN");
});

(function() {

    if(typeof Host === 'undefined')
        throw new Error("Host is undefined");
    if(typeof Host.execute === 'undefined')
        throw new Error("Host.execute is undefined");

    var Client = typeof self.Client !== 'undefined' ? self.Client : self.Client = function(){};

    Client.execute = function(commandString) {
        var args = /^\w+/.exec(commandString);
        if (!args)
            throw new Error("Invalid Command: " + commandString);

        Host.execute(commandString);
    };

    // TODO: listen for host response

    console.log("Android Client loaded", Host);
})();