var scene, camera, renderer, controls;

var paused;
var currentFrame;
var framesCount;
var objects;
var toRadians = Math.PI / 180;

$(document).ready(function() {
    $("#start").on("click", function() {
        paused = true;
        currentFrame = 0;
        framesCount = 0;
        objects = [];
        sendAjax("playback.get", function(result) {
            var obj = JSON.parse(result).response;
            framesCount = obj.framesCount;
            obj.gameObjectsStates.forEach(function(it) {
                objects.push({states: it, i: 0, framesToSleep: 0});
            });
            
            init();
            animate();
        });
    });
    
    $("#player-close").on("click", function() {
        $("#player").fadeOut(250, function() {
            $("#player").find("canvas").remove();
            $("body").removeClass("overflow-hidden");
        });
    });

    $("#player-rewind-line").on("mousemove", function(event) {
        $("#player-progress-mouse").css("width", event.pageX - $(this).offset().left);
    });

    $("#player-rewind-line").on("mouseleave", function() {
        $("#player-progress-mouse").css("width", 0);
    });

    $("#player-rewind-line").on("click", function(event) {
        currentFrame = Math.floor(framesCount * (event.pageX - $(this).offset().left) / $(this).width());
        if (currentFrame == framesCount) {
            alert("currentFrame == framesCount !!!");
        }
        
        objects.forEach(function(object) {
            for (var i = 0; i < object.states.length; i++) {
                var state = object.states[i];
                if (currentFrame >= state.startingFrame && currentFrame < state.endingFrame) {
                    object.i = i;
                    object.framesToSleep = state.endingFrame - 1 - currentFrame;
                    
                    update(object.mesh, object.states[i]);
                    $("#player-progress-current").css("width", $("#player-rewind-line").width() * currentFrame / (framesCount - 1));

                    break;
                }
            }
        });
    });

    $("#player-play-pause").on("click", function() {
        if (paused) {
            paused = false;
            $("#player-play-pause").removeClass("player-play").addClass("player-pause");
        } else {
            paused = true;
            $("#player-play-pause").removeClass("player-pause").addClass("player-play");
        }
    });

    $("#player-stop").on("click", function() {
        if (!paused) {
            $("#player-play-pause").click();
        }
        currentFrame = 0;
        objects.forEach(function(object) {
            object.i = 0;
            object.framesToSleep = 0;
            update(object.mesh, object.states[0]);
        });

        $("#player-progress-current").css("width", 0);
    });
});

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
        var geometry = new THREE.BoxBufferGeometry(1, 1, 1);
        var material = new THREE.MeshPhongMaterial();
        object.mesh = new THREE.Mesh(geometry, material);
        
        update(object.mesh, object.states[0]);
        
        //object.mesh.castShadow = true;
        scene.add(object.mesh);
    });

    //

    renderer = new THREE.WebGLRenderer( { antialias: true } );
    renderer.setPixelRatio( window.devicePixelRatio );
    renderer.setSize( window.innerWidth, window.innerHeight );
    renderer.shadowMap.enabled = true;
    
    $("#player").append(renderer.domElement);
    $("body").addClass("overflow-hidden");
    $("#player").fadeIn(250);

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
    
    if (!paused && currentFrame < framesCount) {
        objects.forEach(function(object) {
            if (object.framesToSleep == 0) {
                update(object.mesh, object.states[object.i]);
                
                object.framesToSleep = object.states[object.i].endingFrame - 1 - object.states[object.i].startingFrame;
                object.i++;
            } else {
                object.framesToSleep--;
            }
        });

        $("#player-progress-current").css("width", $("#player-rewind-line").width() * currentFrame / (framesCount - 1));
        currentFrame++;
    }

    //camera.position.x += 0.4;
    //camera.position.z += 0.4;

    camera.lookAt(objects[0].mesh.position);
    renderer.render(scene, camera);
}

function update(mesh, state) {
    mesh.position.x = state.position[0];
    mesh.position.y = state.position[1];
    mesh.position.z = state.position[2];
    
    mesh.scale.x = state.dimension[0];
    mesh.scale.y = state.dimension[1];
    mesh.scale.z = state.dimension[2];
    
    mesh.rotation.x = state.rotation[0] * toRadians;
    mesh.rotation.y = state.rotation[1] * toRadians;
    mesh.rotation.z = state.rotation[2] * toRadians;
    
    mesh.material.color.setHex(state.color);
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
