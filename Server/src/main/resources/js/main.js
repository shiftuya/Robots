var codeMirror;

class BracketsBugAvoiding_0{}

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
            processData: false, // not to process the data
            contentType: false, // not to set contentType
            success: function(data) {
                console.log(data);
                alert(data);
            }
        });
    });
}

function readFiles(files, oArray) {
    oArray = [];
    var reader = new FileReader();
    readFile(0);
    
    function readFile(index) {
        if (index >= files.length) {
            return;
        }
        
        var file = files[index];
        reader.readAsBinaryString(file);

        reader.onload = function(e) {
            oArray[oArray.length] = {
                name: file.name,
                data: e.target.result
            };
            
            readFile(index+1)
        }
    }
}

function ajaxGet(ajaxQuery, handleResponseFunction) {
    $.get("/api/method/" + ajaxQuery, function(data, status) {
        if (status == "success" && data) {
            try {
                handleResponseFunction(data);
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
