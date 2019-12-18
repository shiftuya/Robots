class Nothing{}

class ContextManager {
    currentContextName;
    
    constructor(entriesArray) {
        this.contextMap = new Map(entriesArray);
    }

    getContextNames() {
        return this.contextMap.keys();
    }

    changeContext(contextName, getQuery) {
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
        
        this.getData(contextName, this, getQuery);
        this.removeCurrentData();

        this.currentContextName = contextName;
    }

    getData(contextName, contextManager, getQuery) {
        if (contextName == "list_of_lobbies") {
            $.get("/api/method/lobbies.get", function(data, status) {
                if (status == "success" && data) {
                    try {
                        var obj = JSON.parse(data);
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
//                                    alert("lobby_id is " + item.lobby_id);
                                    contextManager.changeContext("lobby", "/api/method/lobby.join?id=" + item.lobby_id);
                                });
                            });
                        }
                    } catch(e) {
                        alert(e);
                    }
                } else {
                    alert("Bad request!");
                }
            });
        }

        if (contextName == "choose_level") {
            $.get("/api/method/levels.get", function(data, status) {
                if (status == "success" && data) {
                    try {
                        var obj = JSON.parse(data);
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
//                                    alert("level_id is " + item.level_id);
                                    contextManager.changeContext("lobby", "/api/method/lobby.create?id=" + item.level_id);
                                });
                            });
                        }
                    } catch(e) {
                        alert(e);
                    }
                } else {
                    alert("Bad request!");
                }
            });
        }

        if (contextName == "my_solutions") {
            $.get("/api/method/solutions.get", function(data, status) {
                if (status == "success" && data) {
                    try {
                        var obj = JSON.parse(data);
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
//                                            alert("attempt_id is " + attemptItem.attempt_id);
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
                    } catch(e) {
                        alert(e);
                    }
                } else {
                    alert("Bad request!");
                }
            });
        }

        if (contextName == "lobby") {
            $.get(getQuery, function(data, status) {
                if (status == "success" && data) {
                    try {
                        var obj = JSON.parse(data);
                        var lobbyContentSection = $("#lobby-content");
                        if (obj.response.length == 0) {
                            $("<section class=\"lobby-shell\"><h1>Lobby was not created.</h1></section>").appendTo(lobbyContentSection);
                        } else {
                            var skeleton = lobbyContentSection.find("section.skeleton");
                            var item = obj.response;

                            var section = $(skeleton).clone();
                            section.removeClass("skeleton");

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
                            
                            section.find("#leave-lobby").on("click", function() {
                                contextManager.changeContext("lobby", "/api/method/lobby.join?id=" + item.lobby_id);
                                
                                return false;
                            });

                            lobbyContentSection.append(section);
                        }
                    } catch(e) {
                        alert(e);
                    }
                } else {
                    alert("Bad request!");
                }
            });
        }
    }
    
    removeCurrentData() {
        if (this.currentContextName == "list_of_lobbies") {
            $("#lobbies-table").find("tr:not(':first-of-type'):not('.skeleton')").each(function() {
                $(this).remove();
            });
        }

        if (this.currentContextName == "choose_level") {
            $("#levels-table").find("tr:not('.skeleton')").each(function() {
                $(this).remove();
            });
        }

        if (this.currentContextName == "my_solutions") {
            $("#solutions-table").find("tbody:not('.skeleton')").each(function() {
                $(this).remove();
            });
        }

        if (this.currentContextName == "lobby") {
            $("#lobby-content").find(".lobby-shell:not('.skeleton')").each(function() {
                $(this).remove();
            });
        }

        if (this.currentContextName == "login") {
            $("#login-content").removeClass("active");
            $("#login-content").find("input.login-form-input").each(function() {
                $(this).val("");
            });
        }
    }
}

class ContextListeners {
    constructor(contextManager) {
        this.contextManager = contextManager;
    }
    
    activateAll() {
        var contextManager = this.contextManager;
        for (let contextName of contextManager.getContextNames()) {
            $("#" + contextName).on("click", function() {
                contextManager.changeContext(contextName);
            });
        }
    }
}

function activateLoginListeners(contextManager) {
    $("#login-submit").on("click", function() {
        var username = $("#login-content").find("input.login-form-input[type='text']").val();
        
        $.get("/api/method/sign.login?username=" + username, function(data, status) {
            if (status == "success") { /////////////////////////////////////////////////////////////////////
                contextManager.changeContext("list_of_lobbies");
            } else {
                alert("Bad request!");
            }
        });
    });

    $("#logout").on("click", function() {
        $.get("/api/method/sign.logout", function(data, status) {
            if (status == "success") { /////////////////////////////////////////////////////////////////////
                contextManager.changeContext("login");
            } else {
                alert("Bad request!");
            }
        });
    });
}











