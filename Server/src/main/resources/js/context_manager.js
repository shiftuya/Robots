class BracketsBugAvoiding_1{}

class ContextManager {
    currentContextName;

    constructor(dependenciesArray) {
        this.contextMap = new Map(dependenciesArray);
    }

    getContextNames() {
        return this.contextMap.keys();
    }

    changeContext(contextName, ajaxQuery, obj) {
        var dependencies = this.contextMap.get(contextName);
        
        var title = dependencies.title;
        var header = $("#" + dependencies.headerId);
        var content = $("#" + dependencies.contentId);
        ajaxQuery = ajaxQuery || dependencies.defaultAjaxQuery;
        var insertFunction = dependencies.insertFunction;
        
        var deleteData;
        if (this.currentContextName) {
            deleteData = this.contextMap.get(this.currentContextName).deleteData;
        }

        window.history.pushState("", "", contextName);
        document.title = title + " | Robotics Game Server";

        // hide all headers, except the new one
        $("header:not(#" + dependencies.headerId + ")").removeClass("active");

        // activate all the links inside every header
        let ids = [...this.getContextNames()];
        $("header").find("#" + ids.join(",#")).removeClass("inactive-link");

        // deactivate new link
        $("header").find("#" + contextName).addClass("inactive-link");

        // show new header
        if (!header.hasClass("active")) {
            header.addClass("active"); // alert("header added");
        }

        // hide all content-blocks, except the new one
        $(".content:not(#" + dependencies.contentId + ")").removeClass("active");

        // show new content-block
        if (!content.hasClass("active")) {
            content.addClass("active"); // alert("content added");
        }

        // delete current data
        this.removeCurrentData(deleteData);

        // get and insert new data
        this.getAndInsertData(contextName, ajaxQuery, insertFunction, obj);

        this.currentContextName = contextName;
    }

    getAndInsertData(contextName, ajaxQuery, insertFunction, obj) {
        if (!insertFunction) {
            return;
        }
        
        if (!ajaxQuery) {
            insertFunction(obj, this);
            return;
        }

        var contextManager = this;
        sendAjax(ajaxQuery, function(result) {
            insertFunction(result ? JSON.parse(result) : undefined, contextManager);
        });
    }

    removeCurrentData(deleteData) {
        if (!deleteData) {
            return;
        }
        
        $("#" + deleteData.id).find(deleteData.contentUnit + ":not('.skeleton')").remove();
    }
}

function insertLoginData(obj, contextManager) {
    var skeleton = $("#login-content").find("section.skeleton");
    var section = $(skeleton).clone();
    section.removeClass("skeleton");

    var submitButton = section.find(".login-submit-a");
    
    section.find("input").on("keypress", function(e) {
        if (e.which == 13) {
            submitButton.click();
        }
    });

    submitButton.on("click", function() {
        var username = section.find("input[name='name']").val();
        
        var form = $("#login-content").find(".login-shell:not('.skeleton')").find("form")

        var formData = new FormData(form[0]);
        sendAjax("sign.login", function(result) {
            $("#logout").text("Log Out (" + username + ")");
            contextManager.changeContext("list_of_lobbies");
        }, undefined, formData);
    });
    
    $("#login-content").append(section);
    section.find("input[name='name']").focus();
}

function insertListOfLobbiesData(obj, contextManager) {
    var table = $("#lobbies-table");
    if (obj.response.length == 0) {
        $("<tr><td colspan=\"100%\">No active lobbies.</td></tr>").appendTo(table);
    } else {
        var skeleton = table.find("tr.skeleton");
        obj.response.forEach(function(item) {
            var tr = $(skeleton).clone();
            tr.removeClass("skeleton");

            tr.find(".avatar-icon").css("background-image", "url(\".." + item.avatar + "\")");
            tr.find(".username").text(item.host_name);
            tr.find(".level-icon").css("background-image", "url(\".." + item.level_icon + "\")");
            tr.find(".levelname").text(item.level_name);
            tr.find(".difficulty").text(item.level_difficulty);
            tr.find(".players-amount").text(item.players + "/" + item.players_at_most);
            tr.attr("data-lobby-id", item.lobby_id);

            table.append(tr);

            tr.on("click", function() {
                contextManager.changeContext("lobby", "lobby.join?id=" + item.lobby_id);
            });
        });
    }
}

function insertChooseLevelData(obj, contextManager) {
    var table = $("#levels-table");
    if (obj.response.length == 0) {
        $("<tr><td colspan=\"100%\">No created levels</td></tr>").appendTo(table);
    } else {
        var skeleton = table.find("tr.skeleton");
        obj.response.forEach(function(item) {
            var tr = $(skeleton).clone();
            tr.removeClass("skeleton");

            tr.find(".levels-table-icon").css("background-image", "url(\".." + item.level_icon + "\")");
            tr.find(".level-name").text(item.level_name);
            tr.find(".level-difficulty").text(item.level_difficulty);
            tr.find(".level-type").text(item.level_type);
            tr.find(".level-details-description").find(".level-details-text").text(item.description);
            tr.find(".level-details-rules").find(".level-details-text").text(item.rules);
            tr.find(".level-details-goal").find(".level-details-text").text(item.goal);
            tr.find(".level-players-number").attr("value", item.min_players);
            tr.find(".level-players-number").attr("min", item.min_players);
            tr.find(".level-players-number").attr("max", item.max_players);
            tr.attr("data-level-id", item.level_id);

            table.append(tr);

            tr.find(".start-level-icon").on("click", function() {
                var id = item.level_id;
                var players_amount = tr.find(".level-players-number").val();
                contextManager.changeContext("lobby", "lobby.create?id=" + id + "&players_amount=" + players_amount);
            });
        });
    }
}

function insertSolutionsData(obj, contextManager, inbuiltTable) {
    var table = inbuiltTable || $("#solutions-table");
    if (obj.response.length == 0) {
        $("<tbody><tr><td colspan=\"100%\">No created levels</td></tr></tbody>").appendTo(table);
    } else {
        var skeleton = table.find("tbody.skeleton");
        obj.response.forEach(function(item) {
            var tbody = $(skeleton).clone();
            tbody.removeClass("skeleton");

            tbody.find(".levels-table-icon").css("background-image", "url(\".." + item.level_icon + "\")");
            tbody.find(".level-name").text(item.level_name);
            tbody.find(".level-difficulty").text(item.level_difficulty);
            tbody.find(".level-type").text(item.level_type);
            tbody.find(".level-details-description").find(".level-details-text").text(item.description);
            tbody.find(".level-details-rules").find(".level-details-text").text(item.rules);
            tbody.find(".level-details-goal").find(".level-details-text").text(item.goal);

            if (item.attempts.length == 0) {
                tbody.find(".attempts-number").text("No Attempts");
                tbody.find(".solution-status-icon").addClass("no-attempts-icon");
                tbody.find(".list-of-attempts-shell").remove();
            } else {
                var successed = 0;
                var failed = 0;
                var liSkeleton = tbody.find("li.skeleton");
                item.attempts.forEach(function(attemptItem) {
                    var result;
                    if (attemptItem.attempt_result) {
                        result = "successful";
                        successed++;
                    } else {
                        result = "failed";
                        failed++;
                    }

                    var li = $(liSkeleton).clone();
                    li.removeClass("skeleton");

                    li.find(".attempt-date").text(attemptItem.attempt_date);
                    li.find(".attempt-result").text(result);
                    li.attr("data-attempt-id", attemptItem.attempt_id);

                    li.on("click", function() {
                        contextManager.changeContext("simulation_result", "simulation_result.get?id=" + attemptItem.attempt_id);
                    });

                    tbody.find(".list-of-attempts").append(li);
                });

                var cnt = successed + failed;
                var attempts_number = cnt + " Attempt";
                if (cnt > 1) attempts_number += "s";

                var attempts_number_details = "";
                if (successed && failed) attempts_number_details = "(" + successed + " successful, " + failed + " failed)";
                if (successed && !failed) attempts_number_details = "(" + successed + " successful)";
                if (!successed && failed) attempts_number_details = "(" + failed + " failed)";

                tbody.find(".attempts-number").text(attempts_number);
                tbody.find(".attempts-number-details").text(attempts_number_details);
                tbody.find(".solution-status-icon").addClass((successed) ? "successed-icon" : "failed-icon");
            }

            var solutionRow = tbody.find(".solution-row");
            var solutionDetailsRow = tbody.find(".solution-details-row");
            solutionRow.on("click", function() {
                solutionDetailsRow.toggle();
            });

            table.append(tbody);
        });
    }
}

function insertUsersData(obj, contextManager) {
    var table = $("#users-table");
    if (obj.response.length == 0) {
        $("<tr><td colspan=\"100%\">No created users</td></tr>").appendTo(table);
    } else {
        var skeleton = table.find("tr.skeleton");
        obj.response.forEach(function(item) {
            var tr = $(skeleton).clone();
            tr.removeClass("skeleton");

            tr.find(".avatar-icon").css("background-image", "url(\".." + item.avatar + "\")");
            tr.find(".user-name").text(item.name);
            tr.find(".user-type").text(item.type);
            tr.find(".user-last-active").text(item.last_active);
            
            if (item.is_blocked) {
                tr.addClass("blocked-user");
            }

            table.append(tr);

            tr.on("click", function() {
                contextManager.changeContext("user", "user.get?username=" + item.name);
            });
        });
    }
}

function insertUserData(obj, contextManager) {
    var skeleton = $("#user-content").find("section.skeleton");
    var item = obj.response.info;

    var section = $(skeleton).clone();
    section.removeClass("skeleton");

    section.find(".common-big-icon").css("background-image", "url(\".." + item.avatar + "\")");
    section.find(".user-name").text(item.name);
    section.find(".user-type").text(item.type);
    section.find(".user-last-active").text(item.last_active);
    section.find(".user-block-a").text(item.is_blocked ? "Unblock" : "Block");
    
    if (item.is_blocked) {
        section.find(".user-info").addClass("blocked-user");
    }
    
    var solutionsTable = $("#solutions-table").clone().removeAttr("id");
    insertSolutionsData({response: obj.response.solutions}, contextManager, solutionsTable);
    
    section.append(solutionsTable);
    $("#user-content").append(section);

    section.find(".user-edit-a").on("click", function() {
        contextManager.changeContext("user_editor", undefined, obj);
    });

    section.find(".user-block-a").on("click", function() {
        sendAjax("user.block?username=" + item.name + "&block=" + !item.is_blocked, function(result) {
//            var obj = JSON.parse(result);
            alert("User is " + (item.is_blocked ? "unblocked" : "blocked"));
            contextManager.changeContext("user", "user.get?username=" + item.name);
        });
    });

    section.find(".user-delete-a").on("click", function() {
        if (confirm("Are you sure you want to delete the user? This action cannot be undone.")) {
            sendAjax("user.delete?username=" + item.name, function(result) {
//                var obj = JSON.parse(result);
                alert("User is deleted");
                contextManager.changeContext("users");
            });
        }
    });
}

function insertUserEditorData(obj, contextManager) {
    var skeleton = $("#user-editor-content").find("section.skeleton");
    var section = $(skeleton).clone();
    section.removeClass("skeleton");

    var ajaxQuery, header, message;
    if (!obj) {
        ajaxQuery = "user.create";
        header = "Create user:";
        message = "User created";
    } else {
        ajaxQuery = "user.edit";
        header = "Edit user:";
        message = "User edited";
        
        var item = obj.response.info;
        section.find("input[name=name]").attr("value", item.name).attr("readonly", true).addClass("readonly");
        section.find("select[name=type]").val(item.type);
    }
    
    section.find(".content-header").text(header);

    section.find(".user-submit-a").on("click", function() {
        var form = $("#user-editor-content").find(".user-editor-shell:not('.skeleton')").find("form")

        var formData = new FormData(form[0]);
        sendAjax(ajaxQuery, function(result) {
//            var obj = JSON.parse(result);
            alert(message);
            contextManager.changeContext("users");
        }, undefined, formData);
    });

    $("#user-editor-content").append(section);
}

function insertLevelsData(obj, contextManager) {
    var table = $("#teacher-levels-table");
    if (obj.response.length == 0) {
        $("<tr><td colspan=\"100%\">No created levels</td></tr>").appendTo(table);
    } else {
        var skeleton = table.find("tr.skeleton");
        obj.response.forEach(function(item) {
            var tr = $(skeleton).clone();
            tr.removeClass("skeleton");

            tr.find(".levels-table-icon").css("background-image", "url(\".." + item.level_icon + "\")");
            tr.find(".level-name").text(item.level_name);
            tr.find(".level-difficulty").text(item.level_difficulty);
            tr.find(".level-type").text(item.level_type);
            tr.find(".level-details-description").find(".level-details-text").text(item.description);
            tr.find(".level-details-rules").find(".level-details-text").text(item.rules);
            tr.find(".level-details-goal").find(".level-details-text").text(item.goal);

            table.append(tr);

            tr.find(".level-edit-a").on("click", function() {
                contextManager.changeContext("level_editor", "level.get?id=" + item.level_id);
            });

            tr.find(".level-delete-a").on("click", function() {
                if (confirm("Are you sure you want to delete the level? This action cannot be undone.")) {
                    sendAjax("level.delete?id=" + item.level_id, function(result) {
                        contextManager.changeContext("levels");
                    });
                }
            });
        });
    }
}

function insertLevelEditorData(obj, contextManager) {
    var skeleton = $("#level-editor-content").find("section.skeleton");
    var section = $(skeleton).clone();
    section.removeClass("skeleton");
    
    var code = "level code";
    if (obj) {
        var item = obj.response;
        
        section.find("form.level-editor-form").append('<input name="id" type="text" value="' + item.level_id + '" style="display: none">');
        section.find("input[name=name]").attr("value", item.level_name);
        section.find("select[name=difficulty]").val(item.level_difficulty);
        section.find("input[name=min_players]").attr("value", item.min_players);
        section.find("input[name=max_players]").attr("value", item.max_players);
        section.find("input[name=description]").attr("value", item.description);
        section.find("input[name=rules]").attr("value", item.rules);
        section.find("input[name=goal]").attr("value", item.goal);
        code = item.code;
    }
    
    section.find(".open").on("click", loadFile);

    section.find(".save").on("click", function() {
        downloadFile(codeMirror.getValue(), "robotics.groovy", "application/groovy");
    });

    $("#level-editor-content").append(section);

    // creating after section appending for CodeMirror's bugs avoiding
    codeMirror = CodeMirror(section.find(".level-code-editor-shell")[0], {
        mode: "groovy",
        value: code,
        theme: "darcula",
        lineNumbers: true
    });
}

function insertSimulatorsData(obj, contextManager) {
    var table = $("#simulators-table");
    if (obj.response.length == 0) {
        $("<tr><td colspan=\"100%\">No created simulators</td></tr>").appendTo(table);
    } else {
        var skeleton = table.find("tr.skeleton");
        obj.response.forEach(function(item, number) {
            var tr = $(skeleton).clone();
            tr.removeClass("skeleton");

            tr.find(".simulator-number-col").text(number + 1);
            tr.find(".simulator-url-col").text(item.url);

            table.append(tr);

            tr.find(".simulator-delete-a").on("click", function() {
                sendAjax("simulator.delete?url=" + item.url, function(result) {
                    contextManager.changeContext("simulators");
                });
            });
        });
    }
}

function insertLobbyData(obj, contextManager) {
    var lobbyContentSection = $("#lobby-content");
    if (obj.response == null) {
        $("<section class=\"lobby-shell\"><h1>Lobby is full.</h1></section>").appendTo(lobbyContentSection);
    } else {
        if (obj.response.length == 0) {
            $("<section class=\"lobby-shell\"><h1>Lobby was not created.</h1></section>").appendTo(lobbyContentSection);
        } else {
            var skeleton = lobbyContentSection.find("section.skeleton");
            var item = obj.response;

            var section = $(skeleton).clone();
            section.removeClass("skeleton");

            section.attr("data-lobby-id", item.lobby_id);
            section.find(".common-big-icon").css("background-image", "url(\".." + item.level_icon + "\")");
            section.find(".lobby-level-name").text(item.level_name);
            section.find(".lobby-players").text(item.players + "/" + item.players_at_most);
            section.find(".level-difficulty").text(item.level_difficulty);
            section.find(".level-type").text(item.level_type);
            section.find(".level-details-description").find(".level-details-text").text(item.description);
            section.find(".level-details-rules").find(".level-details-text").text(item.rules);
            section.find(".level-details-goal").find(".level-details-text").text(item.goal);

            var trSkeleton = lobbyContentSection.find("tr.skeleton");
            item.players_list.forEach(function(playerItem) {
                var tr = $(trSkeleton).clone();
                tr.removeClass("skeleton");

                tr.find(".players-table-icon").css("background-image", "url(\".." + playerItem.avatar + "\")");
                tr.find(".username").text(playerItem.user_name);
                tr.find(".solution-submitted").text(playerItem.submitted ? "Submitted" : "Not submitted");

                section.find(".players-table").append(tr);
            });

            for (var i = item.players_list.length; i < item.players_at_most; i++) {
                section.find(".players-table").append($("<tr><td colspan=\"100%\" class=\"waiting-player\">Waiting for the player</td></tr>"));
            }

            section.find("#edit-solution").on("click", function() {
                $("#header-code-editor").find(".back, .play").attr("data-lobby-id", item.lobby_id);
                contextManager.changeContext("code_editor", "code.edit?id=" + item.lobby_id);
            });

            section.find("#leave-lobby").on("click", function() {
                sendAjax("lobby.leave?id=" + item.lobby_id, function(data) {
                    contextManager.changeContext("list_of_lobbies");
                });
            });

            section.find("#get-simulation-result").on("click", function() {
                sendAjax("simulation_result.is_ready?id=" + item.lobby_id, function(data) {
                    var obj = JSON.parse(data);
                    if (obj.response.simulation_finished) {
                        contextManager.changeContext("simulation_result", "simulation_result.get?id=" + item.lobby_id);
                    } else {
                        alert("The simulation hasn't been processed yet. Try again later.");
                    }
                });
            });

            lobbyContentSection.append(section);
        }
    }
}

function insertCodeEditorData(obj, contextManager) {
    var skeleton = $("#code-editor-content").find("section.skeleton");
    var section = $(skeleton).clone();
    
    section.removeClass("skeleton");
    var code = obj.response.code; // new variable is needed to get exactly a string instead of object (strange, but still)

    $("#code-editor-content").append(section);

    // creating after section appending for CodeMirror's bugs avoiding
    codeMirror = CodeMirror(section[0], {
        mode: "groovy",
        value: code,
        theme: "darcula",
        lineNumbers: true
    });
}

function insertSimulationResultData(obj, contextManager) {
    $("#playback").on("click", function() {
        paused = true;
        playerClosed = false;
        currentFrame = 0;
        objects = [];

        playback = JSON.parse(JSON.stringify(obj.response.playback));
        playback.gameObjectsStates.forEach(function(states) {
            var newStates = [];

            if (states[0].startingFrame != 0) {
                var invisibleState = {
                    startingFrame: 0,
                    endingFrame: states[0].startingFrame,
                    visible: false,
                    position: [0, 0, 0]
                };

                newStates.push(invisibleState);
            }

            for (var i = 0; i < states.length; i++) {
                states[i].visible = true;
                newStates.push(states[i]);

                if (i < states.length - 1) {
                    if (states[i].endingFrame == states[i+1].startingFrame) {
                        states[i].endingFrame = states[i].startingFrame + 1;

                        var dFrames = states[i+1].startingFrame - states[i].startingFrame;

                        var dPosX = (states[i+1].position[0] - states[i].position[0]) / dFrames;
                        var dPosY = (states[i+1].position[1] - states[i].position[1]) / dFrames;
                        var dPosZ = (states[i+1].position[2] - states[i].position[2]) / dFrames;

                        var dDimX = (states[i+1].dimension[0] - states[i].dimension[0]) / dFrames;
                        var dDimY = (states[i+1].dimension[1] - states[i].dimension[1]) / dFrames;
                        var dDimZ = (states[i+1].dimension[2] - states[i].dimension[2]) / dFrames;

                        var dRotX = (states[i+1].rotation[0] - states[i].rotation[0]) / dFrames;
                        var dRotY = (states[i+1].rotation[1] - states[i].rotation[1]) / dFrames;
                        var dRotZ = (states[i+1].rotation[2] - states[i].rotation[2]) / dFrames;

                        for (var frame = 1; frame < dFrames; frame++) {
                            var newState = {
                                startingFrame: states[i].startingFrame + frame,
                                endingFrame: states[i].startingFrame + frame + 1,
                                visible: true,
                                position: [],
                                dimension: [],
                                rotation: [],
                                color: states[i].color,
                                sensors: states[i].sensors
                            };

                            newState.position.push(states[i].position[0] + frame * dPosX);
                            newState.position.push(states[i].position[1] + frame * dPosY);
                            newState.position.push(states[i].position[2] + frame * dPosZ);

                            newState.dimension.push(states[i].dimension[0] + frame * dDimX);
                            newState.dimension.push(states[i].dimension[1] + frame * dDimY);
                            newState.dimension.push(states[i].dimension[2] + frame * dDimZ);

                            newState.rotation.push(states[i].rotation[0] + frame * dRotX);
                            newState.rotation.push(states[i].rotation[1] + frame * dRotY);
                            newState.rotation.push(states[i].rotation[2] + frame * dRotZ);

                            newStates.push(newState);
                        }
                    } else {
                        var invisibleState = {
                            startingFrame: states[i].endingFrame,
                            endingFrame: states[i+1].startingFrame,
                            visible: false,
                            position: [0, 0, 0]
                        };

                        newStates.push(invisibleState);
                    }
                } else if (states[i].endingFrame != playback.framesCount) {
                    var invisibleState = {
                        startingFrame: states[i].startingFrame,
                        endingFrame: playback.framesCount,
                        visible: false,
                        position: [0, 0, 0]
                    };

                    newStates.push(invisibleState);
                }
            }

            objects.push({states: newStates, i: 0, framesToSleep: 0});
        });

        playback.bindings.forEach(function(it) {
            $("<div>" + it.user + "</div>").addClass("player-user").attr("data-id", it.id).appendTo("#player-users");
        });

        var firstUser = $("#player-users").find(".player-user").first();
        firstUser.addClass("player-user-selected");
        currentObjectId = firstUser.attr("data-id");

        $("#player-users").find(".player-user").on("click", function() {
            $("#player-users").find(".player-user").removeClass("player-user-selected");
            $(this).addClass("player-user-selected");
            currentObjectId = $(this).attr("data-id");
            updateSensors();
            updateCameraDirection();
        });

        init();
        animate();
    });
    
    var table = $("#simulation-result-table");
    var skeleton = table.find("tr.skeleton");
    obj.response.users.forEach(function(item) {
        var tr = $(skeleton).clone();
        tr.removeClass("skeleton");

        tr.find(".avatar-icon").css("background-image", "url(\".." + item.avatar + "\")");
        tr.find(".username").text(item.username);
        tr.find(".result").text(item.result ? "Successful" : "Failed");

        var id = obj.response.id;

        tr.find(".log").on("click", function() {
            sendAjax("log.get?username=" + item.username + "&id=" + id, function(result) {
                var obj = JSON.parse(result);
                downloadFile(obj.response, "log_" + id + "_" + item.username + ".txt", "plain/text");
            });
        });

        tr.find(".script").on("click", function() {
            sendAjax("script.get?username=" + item.username + "&id=" + obj.response.id, function(result) {
                var obj = JSON.parse(result);
                downloadFile(obj.response, "script_" + id + "_" + item.username + ".groovy", "application/groovy");
            });
        });

        table.append(tr);
    });
}
