document.addEventListener('DOMContentLoaded', function () {

    // Render Navigation Menu and about box
        // ClientMainThread.tryConnectToPortListener('oajoebolmmcpcfpmlehbcaahkdnpfhge', 'relay-render-proxy');
        // TODO: fix for browsers

        ClientMainThread.execute("UI.MENU");
        ClientMainThread.execute("UI.LOGIN");
        ClientMainThread.execute("UI.CONTACTS");
        ClientMainThread.execute("SETTINGS.AUTORUN");
        ClientMainThread.execute("CHAT /debug"); // Connect to socket server


    function onResize() {
        document.body.classList.add(window.innerWidth > window.innerHeight / 1.2 ? 'layout-horizontal' : 'layout-vertical');
        document.body.classList.remove(window.innerWidth > window.innerHeight / 1.2 ? 'layout-vertical' : 'layout-horizontal');
    }
    window.onresize = onResize;
    setTimeout(onResize, 500);
});



(function() {

    var Client = self.Client !== 'undefined' ? self.Client : self.Client = function(){};

    Client.execute = function(commandString) {
        var args = /^\w+/.exec(commandString);
        if (!args)
            throw new Error("Invalid Command: " + commandString);

        Host.execute(commandString);
    };

    // TODO: listen for host response

    console.log("Android Client loaded");
})();