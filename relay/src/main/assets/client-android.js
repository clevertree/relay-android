document.addEventListener('DOMContentLoaded', function () {

    // Render Navigation Menu and about box
        // Client.tryConnectToPortListener('oajoebolmmcpcfpmlehbcaahkdnpfhge', 'relay-render-proxy');
        // TODO: fix for browsers

        Client.execute("UI.MENU");
        Client.execute("UI.LOGIN");
        Client.execute("UI.CONTACTS");
        Client.execute("SETTINGS.AUTORUN");
        Client.execute("CHAT /debug"); // Connect to socket server


    function onResize() {
        document.body.classList.add(window.innerWidth > window.innerHeight / 1.2 ? 'layout-horizontal' : 'layout-vertical');
        document.body.classList.remove(window.innerWidth > window.innerHeight / 1.2 ? 'layout-vertical' : 'layout-horizontal');
    }
    window.onresize = onResize;
    setTimeout(onResize, 500);
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