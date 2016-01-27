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


    // Override render

    var ARTICLE_LIMIT = 3;

    var oldRender = Client.render;
    Client.render = function(commandString) {
        var args = /^render\s+([\s\S]+)$/mi.exec(commandString);
        if (!args)
            throw new Error("Invalid Command: " + commandString);

        var bodyElm = document.getElementsByTagName('body')[0];

        // Clear body on portrait
        console.log("Clearing: " + (ARTICLE_LIMIT - bodyElm.children.length));
        while(bodyElm.children.length > ARTICLE_LIMIT)
            bodyElm.removeChild(bodyElm.children[bodyElm.children.length-1]); // Remove Last

        return oldRender(commandString);
    }

})();