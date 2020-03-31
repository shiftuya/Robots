var codeMirror;

class BracketsBugAvoiding_0{}

$(document).ready(function() {
    var contextManager = new ContextManager([
        ["login", {
            title: "Login",
            contentId: "login-content"
        }],
        ["list_of_lobbies", {
            title: "List of Lobbies",
            headerId: "header-main",
            contentId: "list-of-lobbies-content",
            defaultAjaxQuery: "lobbies.get",
            insertFunction: insertListOfLobbiesData,
            deleteData: {
                id: "lobbies-table",
                contentUnit: "tr:not(':first-of-type')"
            }
        }],
        ["choose_level", {
            title: "Choose a Level",
            headerId: "header-main",
            contentId: "choose-level-content",
            defaultAjaxQuery: "levels.get",
            insertFunction: insertChooseLevelData,
            deleteData: {
                id: "levels-table",
                contentUnit: "tr:not(':first-of-type')"
            }
        }],
        ["my_solutions", {
            title: "My Solutions",
            headerId: "header-main",
            contentId: "my-solutions-content",
            defaultAjaxQuery: "solutions.get",
            insertFunction: insertSolutionsData,
            deleteData: {
                id: "solutions-table",
                contentUnit: "tbody:not(':first-of-type')"
            }
        }],
        ["levels", {
            title: "Levels",
            headerId: "header-main",
            contentId: "levels-content",
            defaultAjaxQuery: "levels.get",
            insertFunction: insertLevelsData,
            deleteData: {
                id: "teacher-levels-table",
                contentUnit: "tr:not(':first-of-type')"
            }
        }],
        ["level_editor", {
            title: "Level editor",
            headerId: "header-main",
            contentId: "level-editor-content",
            insertFunction: insertLevelEditorData,
            deleteData: {
                id: "level-editor-content",
                contentUnit: ".level-editor-shell"
            }
        }],
        ["simulators", {
            title: "Simulators",
            headerId: "header-main",
            contentId: "simulators-content",
            defaultAjaxQuery: "simulators.get",
            insertFunction: insertSimulatorsData,
            deleteData: {
                id: "simulators-table",
                contentUnit: "tr:not(':first-of-type')"
            }
        }],
        ["options", {
            title: "Options",
            headerId: "header-main",
            contentId: "options-content"
        }],
        ["404", {
            title: "404 Not Found",
            headerId: "header-main",
            contentId: "404-content"
        }],
        ["lobby", {
            title: "Lobby",
            headerId: "header-main",
            contentId: "lobby-content",
            insertFunction: insertLobbyData,
            deleteData: {
                id: "lobby-content",
                contentUnit: ".lobby-shell"
            }
        }],
        ["code_editor", {
            title: "Code Editor",
            headerId: "header-code-editor",
            contentId: "code-editor-content",
            insertFunction: insertCodeEditorData,
            deleteData: {
                id: "code-editor-content",
                contentUnit: ".code-editor-shell"
            }
        }],
        ["simulation_result", {
            title: "Simulation Result",
            headerId: "header-main",
            contentId: "simulation-result-content",
            insertFunction: insertSimulationResultData,
            deleteData: {
                id: "simulation-result-content",
                contentUnit: ".simulation-result-shell"
            }
        }]
    ]);
    
    activateListeners(contextManager);
    contextManager.changeContext(initialContext);

    var contextListeners = new ContextListeners(contextManager);
    contextListeners.activateAll();
});

function activateListeners(contextManager) {
    $("#login-submit").on("click", function() {
        var username = $("#login-content").find("input.login-form-input[type='text']").val();
        
        ajaxGet("sign.login?username=" + username, function(data) {
            var obj = JSON.parse(data);
            if (obj.response.length == 0) {
                alert("Bad response!");
            } else if (obj.response.logged_in) {
                $("#logout").text("Log Out (" + username + ")");
                contextManager.changeContext("list_of_lobbies");
            } else {
                alert(obj.response.message);
            }
        });
    });

    $("#logout").on("click", function() {
        ajaxGet("sign.logout", function(data) {
            var obj = JSON.parse(data);
            if (obj.response.length == 0) {
                alert("Bad response!");
            } else if (obj.response.logged_out) {
                contextManager.changeContext("login");
            } else {
                alert(obj.response.message);
            }
        });
    });

    $("#header-code-editor").find(".open").on("click", loadFile);

    $("#header-code-editor").find(".save").on("click", function() {
        downloadFile(codeMirror.getValue(), "robotics.groovy", "application/groovy");
    });

    $("#header-code-editor").find(".back").on("click", function() {
        contextManager.changeContext("lobby", "lobby.return?id=" + $(this).attr("data-lobby-id"));
    });

    $("#header-code-editor").find(".play").on("click", function() {
        var id = $(this).attr("data-lobby-id");
        var code = codeMirror.getValue();

        $.post("/api/method/lobby.submit?id=" + id, {code: code}, function(data, status) {
            if (status == "success" && data) {
                try {
                    var obj = JSON.parse(data);
                    if (obj.response.length == 0) {
                        alert("Bad response!");
                    } else {
                        alert(obj.response.message);
                        if (obj.response.simulated) {
                            contextManager.changeContext("simulation_result", "simulation_result.get?id=" + id);
                        } else if (obj.response.compiled) {
                            contextManager.changeContext("lobby", "lobby.return?id=" + id);
                        } else {
                            alert("Debug: not compiled and not simulated!");
                        }
                    }
                } catch(e) {
                    alert(e);
                }
            } else {
                alert("Bad request!");
            }
        });
    });

    $("#submit-level").on("click", function() {
        var form = $("#level-editor-content").find(".level-editor-shell:not('.skeleton')").find("form")
        form.find("textarea[name='code']").val(codeMirror.getValue());

        var formData = new FormData(form[0]);
        $.ajax({
            url: "/api/method/level.submit",
            type: "POST",
            data: formData,
            processData: false,
            contentType: false,
            cache: false,
            success: function(result, status, xhr) {
                var obj = JSON.parse(result);
                if (obj.response) {
                    alert(obj.response);
                } else if (obj.error) {
                    alert(obj.error);
                }
            },
            error: function(xhr, status, error) {
                alert(error);
            }
        });
    });

    $("#add-simulator").on("click", function() {
        var url = prompt("Type simulator url:", "");
        if (url == null || url == "") {
            alert("Canceled");
            return;
        }
        
        ajaxGet("simulator.add?url=" + url, function(result) {
            var obj = JSON.parse(result);
            alert(obj.response.added ? "Added" : "Not added");
            contextManager.changeContext("simulators");
        });
    });
}

function ajaxGet(ajaxQuery, handleResponseFunction) {
    $.get("/api/method/" + ajaxQuery, function(result, status) {
        if (status == "success" && result) {
            try {
                handleResponseFunction(result);
            } catch(e) {
                alert(e);
            }
        } else {
            alert("Bad request!");
        }
    });
}

function downloadFile(data, filename, type) {
    var file = new Blob([data], {type: type});
    
    if (window.navigator.msSaveOrOpenBlob) { // IE10+
        window.navigator.msSaveOrOpenBlob(file, filename);
    } else { // Others
        var a = document.createElement("a"),
            url = URL.createObjectURL(file);
        
        a.href = url;
        a.download = filename;
//        document.body.appendChild(a);
        a.click();
        setTimeout(function() {
//            document.body.removeChild(a);
            window.URL.revokeObjectURL(url);
        }, 0);
    }
}

function loadFile() {
    var input = document.createElement("input");
    
    input.type = "file";
    input.onchange = function(e) {
        var file = e.target.files[0];
        if (!file) {
            alert("Sorry, we could not open the file.");
            return;
        }
        
        var reader = new FileReader();
        reader.onload = function(e) {
            codeMirror.setValue(e.target.result);
        };
        reader.readAsText(file);
    };
    
    input.click();
}
