class BracketsBugAvoiding_2{}

class ContextListeners {
    constructor(contextManager) {
        this.contextManager = contextManager;
    }

    activateAll() {
        var contextManager = this.contextManager;
        for (let contextName of contextManager.getContextNames()) {
            $("#" + contextName).on("click", function() {
                if (!$(this).hasClass("inactive-link")) {
                    contextManager.changeContext(contextName);
                }
            });
        }
    }
}
