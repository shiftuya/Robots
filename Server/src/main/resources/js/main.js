var codeMirror;

class BracketsBugAvoiding_0{}

$(document).ready(function() {
    var contextManager = new ContextManager([
        ["login", {
            title: "Login",
            contentId: "login-content",
            insertFunction: insertLoginData,
            deleteData: {
                id: "login-content",
                contentUnit: ".login-shell"
            }
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
        ["users", {
            title: "Users",
            headerId: "header-main",
            contentId: "users-content",
            defaultAjaxQuery: "users.get",
            insertFunction: insertUsersData,
            deleteData: {
                id: "users-table",
                contentUnit: "tr:not(':first-of-type')"
            }
        }],
        ["user", {
            title: "User",
            headerId: "header-main",
            contentId: "user-content",
            insertFunction: insertUserData,
            deleteData: {
                id: "user-content",
                contentUnit: ".user-shell"
            }
        }],
        ["user_editor", {
            title: "User editor",
            headerId: "header-main",
            contentId: "user-editor-content",
            insertFunction: insertUserEditorData,
            deleteData: {
                id: "user-editor-content",
                contentUnit: ".user-editor-shell"
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
                id: "simulation-result-table",
                contentUnit: "tr:not(':first-of-type')"
            }
        }]
    ]);
    
    activateListeners(contextManager);
    contextManager.changeContext(initialContext);

    var contextListeners = new ContextListeners(contextManager);
    contextListeners.activateAll();
});

function activateListeners(contextManager) {
    $("#logout").on("click", function() {
        sendAjax("sign.logout", function(data) {
            contextManager.changeContext("login");
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

        sendAjax("lobby.submit?id=" + id, function(result) {
            var obj = JSON.parse(result);
            if (obj.response.length == 0) {
                alert("Bad response!");
            } else {
                if (obj.response.message) {
                    alert(obj.response.message);
                }
                
                if (obj.response.simulated) {
                    contextManager.changeContext("simulation_result", "simulation_result.get?id=" + id);
                } else if (obj.response.compiled) {
                    contextManager.changeContext("lobby", "lobby.return?id=" + id);
                } else {
                    alert("Debug: not compiled and not simulated!");
                }
            }
        }, {code: code});
    });

    $("#submit-level").on("click", function() {
        var form = $("#level-editor-content").find(".level-editor-shell:not('.skeleton')").find("form")
        form.find("textarea[name='code']").val(codeMirror.getValue());

        var formData = new FormData(form[0]);
        sendAjax("level.submit", function(result) {
            contextManager.changeContext("levels");
        }, undefined, formData);
    });

    $("#add-simulator").on("click", function() {
        var url = prompt("Type simulator url:", "");
        if (!url) {
            return;
        }
        
        sendAjax("simulator.add?url=" + url, function(result) {
            contextManager.changeContext("simulators");
        });
    });
}

function sendAjax(ajaxQuery, handleResponseFunction, data, formData) {
    $.ajax({
        url: "/api/method/" + ajaxQuery,
        type: formData || data ? "POST" : "GET",
        data: formData || data,
        processData: !formData,
        contentType: false,
        cache: false,
        success: function(result, status, xhr) {
            handleResponseFunction(result);
        },
        error: function(xhr, status, error) {
            var obj = JSON.parse(xhr.responseText);
            alert("Server error: " + obj.error);
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






var scene, camera, renderer, controls;
var currentFrame, framesCount, objects;

function init() {
    camera = new THREE.PerspectiveCamera( 45, window.innerWidth / window.innerHeight, 1, 10000 );
    camera.position.set( 200, 100, 200 );

    scene = new THREE.Scene();
    scene.background = new THREE.Color( 0xa0a0a0 );
    //scene.fog = new THREE.Fog( 0xa0a0a0, 200, 10000 );

    var hemiLight = new THREE.HemisphereLight( 0xffffff, 0x444444 );
    hemiLight.position.set( 0, 200, 0 );
    scene.add( hemiLight );

    var directionalLight = new THREE.DirectionalLight( 0xffffff );
    directionalLight.position.set( 0, 200, 100 );
    directionalLight.castShadow = true;
    directionalLight.shadow.camera.top = 180;
    directionalLight.shadow.camera.bottom = -100;
    directionalLight.shadow.camera.left = -120;
    directionalLight.shadow.camera.right = 120;
    scene.add( directionalLight );

    // ground

    var ground = new THREE.Mesh( new THREE.PlaneBufferGeometry( 2000, 2000 ), new THREE.MeshPhongMaterial( { color: 0x999999, depthWrite: false } ) );
    ground.rotation.x = - Math.PI / 2;
    ground.receiveShadow = true;
    scene.add( ground );

    var grid = new THREE.GridHelper( 2000, 20, 0x000000, 0x000000 );
    grid.material.opacity = 0.2;
    grid.material.transparent = true;
    scene.add( grid );

    // export mesh

    objects.forEach(function(object) {
        var geometry = new THREE.BoxBufferGeometry(object.states[0].dimension[0], object.states[0].dimension[1], object.states[0].dimension[2]);
        var material = new THREE.MeshPhongMaterial({color: object.states[0].color});

        object.mesh = new THREE.Mesh(geometry, material);
        //object.mesh.castShadow = true;
        scene.add(object.mesh);
    });

    //

    renderer = new THREE.WebGLRenderer( { antialias: true } );
    renderer.setPixelRatio( window.devicePixelRatio );
    renderer.setSize( window.innerWidth, window.innerHeight );
    renderer.shadowMap.enabled = true;
    document.body.appendChild( renderer.domElement );

    //

    controls = new THREE.OrbitControls( camera, renderer.domElement );
    controls.target.set( 0, 5, 0 );
    controls.update();

    //

    window.addEventListener( 'resize', onWindowResize, false );
}

function onWindowResize() {
    camera.aspect = window.innerWidth / window.innerHeight;
    camera.updateProjectionMatrix();

    renderer.setSize( window.innerWidth, window.innerHeight );
}

function animate() {
    requestAnimationFrame(animate);

    if (currentFrame < framesCount) {
        objects.forEach(function(object) {
            if (object.framesToSleep == 0) {
                object.mesh.position.x = object.states[object.i].position[0];
                object.mesh.position.y = object.states[object.i].position[1];
                object.mesh.position.z = object.states[object.i].position[2];

                object.framesToSleep = object.states[object.i].endingFrame - object.states[object.i].startingFrame - 1;
                object.i++;
            } else {
                object.framesToSleep--;
            }
        });

        currentFrame++;
    }

    //camera.position.x += 0.4;
    //camera.position.z += 0.4;

    camera.lookAt(objects[0].mesh.position);
    renderer.render(scene, camera);
}
