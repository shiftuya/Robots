class BracketsBugAvoiding_1{}

class ContextManager {
    currentContextName;

    constructor(entriesArray) {
        this.contextMap = new Map(entriesArray);
        this.standardAjaxQueryMap = new Map([
            ["list_of_lobbies", "lobbies.get"  ],
            ["choose_level",    "levels.get"   ],
            ["my_solutions",    "solutions.get"],
            ["levels",          "levels.get"   ]
        ]);
        this.insertFunctionMap = new Map([
            ["list_of_lobbies",   insertListOfLobbiesData   ],
            ["choose_level",      insertChooseLevelData     ],
            ["my_solutions",      insertSolutionsData       ],
            ["levels",            insertLevelsData          ],
            ["lobby",             insertLobbyData           ],
            ["code_editor",       insertCodeEditorData      ],
            ["simulation_result", insertSimulationResultData]
        ]);
    }

    getContextNames() {
        return this.contextMap.keys();
    }

    getAjaxStandardQuery(contextName) {
        return this.standardAjaxQueryMap.get(contextName);
    }

    getInsertFunction(contextName) {
        return this.insertFunctionMap.get(contextName);
    }

    changeContext(contextName, ajaxQuery) {
        var dependencies = this.contextMap.get(contextName);
        var title = dependencies.title;
        var header = $("#" + dependencies.headerId);
        var content = $("#" + dependencies.contentId);

        document.title = title + " | Robotics Game Server";

        // скрываем все хедеры, кроме нового
        $("header:not(#" + dependencies.headerId + ")").each(function() {
            $(this).removeClass("active");
        });

        // активируем все ссылки внутри всех хедеров
        let ids = [...this.getContextNames()];
        $("header").find("#" + ids.join(",#")).removeClass("inactive-link");

        // деактивируем новую ссылку
        $("header").find("#" + contextName).addClass("inactive-link");

        // показываем новый хедер
        if (!header.hasClass("active")) {
            header.addClass("active"); // alert("header added");
        }

        // скрываем все контент-блоки, кроме нового
        $(".content:not(#" + dependencies.contentId + ")").each(function() {
            $(this).removeClass("active");
        });

        // показываем новый контент-блок
        if (!content.hasClass("active")) {
            content.addClass("active"); // alert("content added");
        }

        this.getAndInsertData(contextName, ajaxQuery || this.getAjaxStandardQuery(contextName));
        this.removeCurrentData();

        this.currentContextName = contextName;
    }

    getAndInsertData(contextName, ajaxQuery) {
        var contextManager = this;
        ajaxGet(ajaxQuery, function(data) {
            contextManager.getInsertFunction(contextName)(JSON.parse(data), contextManager);
        });
    }

    removeCurrentData() {
        switch (this.currentContextName) {
            case "login":
                $("#login-content").removeClass("active");
                $("#login-content").find("input.login-form-input").val("");
                break;
            case "list_of_lobbies":
                removeNode("lobbies-table", "tr:not(':first-of-type')");
                break;
            case "choose_level":
                removeNode("levels-table", "tr");
                break;
            case "my_solutions":
                removeNode("solutions-table", "tbody");
                break;
            case "levels":
                removeNode("teacher-levels-table", "tr");
                break;
            case "lobby":
                removeNode("lobby-content", ".lobby-shell");
                break;
            case "code_editor":
                removeNode("code-editor-content", ".code-editor-shell");
                break;
            case "simulation_result":
                removeNode("simulation-result-content", ".simulation-result-shell");
                break;
        }

        function removeNode(id, contentUnit) {
            $("#" + id).find(contentUnit + ":not('.skeleton')").remove();
        }
    }
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
                //                        alert("lobby_id is " + item.lobby_id);
                contextManager.changeContext("lobby", "lobby.join?id=" + item.lobby_id);
            });
        });
    }
}

function insertChooseLevelData(obj, contextManager) {
    var table = $("#levels-table");
    if (obj.response.length == 0) {
        $("<tr><td colspan=\"100%\">No created levels.</td></tr>").appendTo(table);
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

function insertSolutionsData(obj, contextManager) {
    var table = $("#solutions-table");
    if (obj.response.length == 0) {
        $("<tbody><tr><td colspan=\"100%\">No created levels.</td></tr></tbody>").appendTo(table);
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
                        result = "successed";
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
                        alert("attempt_id is " + attemptItem.attempt_id);
                    });

                    tbody.find(".list-of-attempts").append(li);
                });

                var cnt = successed + failed;
                var attempts_number = cnt + " Attempt";
                if (cnt > 1) attempts_number += "s";

                var attempts_number_details = "";
                if (successed && failed) attempts_number_details = "(" + successed + " successed, " + failed + " failed)";
                if (successed && !failed) attempts_number_details = "(" + successed + " successed)";
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

function insertLevelsData(obj, contextManager) {
    var table = $("#teacher-levels-table");
    if (obj.response.length == 0) {
        $("<tr><td colspan=\"100%\">No created levels.</td></tr>").appendTo(table);
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

            tr.find(".level-edit-col").find(".a").on("click", function() {
                var id = item.level_id;
                alert(id);
                //                                    contextManager.changeContext("lobby", "lobby.create?id=" + id + "&players_amount=" + players_amount);
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
            section.find(".lobby-common-icon").css("background-image", "url(\".." + item.level_icon + "\")");
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
                ajaxGet("lobby.leave?id=" + item.lobby_id, function(data) {
                    var obj = JSON.parse(data);
                    if (obj.response.length == 0) {
                        alert("Bad response!");
                    } else {
                        if (obj.response.successful) {
                            contextManager.changeContext("list_of_lobbies");
                        } else {
                            alert("Sorry, we could not remove you from the lobby. Try again later.");
                        }
                    }
                });
            });

            section.find("#get-simulation-result").on("click", function() {
                ajaxGet("simulation_result.is_ready?id=" + item.lobby_id, function(data) {
                    var obj = JSON.parse(data);
                    if (obj.response.length == 0) {
                        alert("Bad response!");
                    } else {
                        if (obj.response.simulation_finished) {
                            contextManager.changeContext("simulation_result", "simulation_result.get?id=" + item.lobby_id);
                        } else {
                            alert("The simulation hasn't been processed yet. Try again later.");
                        }
                    }
                });
            });

            lobbyContentSection.append(section);
        }
    }
}

function insertCodeEditorData(obj, contextManager) {
    var skeleton = $("#code-editor-content").find("section.skeleton");
    if (obj.response.length == 0) {
        alert("Bad response!");
    } else {
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
}

function insertSimulationResultData(obj, contextManager) {
    if (obj.response.length == 0) {
        alert("Bad response!");
    } else {
        var skeleton = $("#simulation-result-content").find("section.skeleton");
        var section = $(skeleton).clone();
        section.removeClass("skeleton");

        var log = obj.response.simulation_result_log; // new variable is needed to get exactly a string instead of object (strange, but still)
        section.find(".simulation-results-status").text(obj.response.simulation_result_status ? "Successful" : "Failed");
        section.find(".log-content > textarea").val(log);

        $("#simulation-result-content").append(section);
    }
}
