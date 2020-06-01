class BracketsBugAvoiding_2{}

class ContextListeners {
    activateAll() {
        for (let contextName of contextManager.getContextNames()) {
            $("#" + contextName).on("click", function() {
                if (!$(this).hasClass("inactive-link")) {
                    contextManager.changeContext(contextName);
                }
            });
        }
    }
}
