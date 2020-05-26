var scene, camera, renderer, controls, ground, grid;
var playback, paused, playerClosed, currentFrame, objects, currentObjectId;
var toRadians = Math.PI / 180;

function init() {
    camera = new THREE.PerspectiveCamera(45, window.innerWidth / (window.innerHeight - 90), 1, playback.camera.renderDistance);
    camera.position.set(playback.camera.position[0], playback.camera.position[1], playback.camera.position[2]);

    scene = new THREE.Scene();
    scene.background = new THREE.Color(playback.backgroundColor);
    
    // lights
    var hemiLight = new THREE.HemisphereLight(0xffffff, 0x444444);
    hemiLight.position.set(0, 200, 0);
    scene.add(hemiLight);

    var directionalLight = new THREE.DirectionalLight(0xffffff);
    directionalLight.position.set(0, 200, 100);
    directionalLight.castShadow = true;
    directionalLight.shadow.camera.top = 180;
    directionalLight.shadow.camera.bottom = -100;
    directionalLight.shadow.camera.left = -120;
    directionalLight.shadow.camera.right = 120;
    scene.add(directionalLight);

    // ground
    ground = new THREE.Mesh(
        new THREE.PlaneBufferGeometry(playback.ground.size, playback.ground.size),
        new THREE.MeshPhongMaterial({ color: playback.ground.color, depthWrite: false })
    );
    ground.rotation.x = -Math.PI / 2;
    ground.receiveShadow = true;
    scene.add(ground);

    grid = new THREE.GridHelper(playback.ground.size, playback.ground.gridDivisions, playback.ground.gridCenterLineColor, playback.ground.gridColor);
    grid.material.opacity = playback.ground.opacity;
    grid.material.transparent = true;
    scene.add(grid);

    // meshes
    objects.forEach(function(object) {
        object.mesh = new THREE.Mesh(
            new THREE.BoxBufferGeometry(1, 1, 1),
            new THREE.MeshPhongMaterial()
        );

        update(object.mesh, object.states[0]);

        scene.add(object.mesh);
    });
    updateSensors();

    // renderer
    renderer = new THREE.WebGLRenderer({ antialias: true });
    renderer.setPixelRatio(window.devicePixelRatio);
    renderer.setSize(window.innerWidth, window.innerHeight - 90);
    renderer.shadowMap.enabled = true;

    $("#player").append(renderer.domElement);
    $("body").addClass("overflow-hidden");
    $("#player").fadeIn("slow");

    // controls
    controls = new THREE.OrbitControls(camera, renderer.domElement);
    controls.enableKeys = false;
    controls.enableRotate = false;
    controls.enableZoom = false;
    controls.update();

    // resize
    window.addEventListener("resize", onWindowResize, false);
}

function onWindowResize() {
    camera.aspect = window.innerWidth / (window.innerHeight - 90);
    camera.updateProjectionMatrix();

    renderer.setSize(window.innerWidth, window.innerHeight - 90);
}

function animate() {
    if (playerClosed) {
        dispose();
        return;
    }

    requestAnimationFrame(animate);

    if (!paused && currentFrame < playback.framesCount) {
        updateSensors();
        
        objects.forEach(function(object) {
            if (object.framesToSleep == 0) {
                update(object.mesh, object.states[object.i]);

                object.framesToSleep = object.states[object.i].endingFrame - 1 - object.states[object.i].startingFrame;
                object.i++;
            } else {
                object.framesToSleep--;
            }
        });

        $("#player-progress-current").css("width", $("#player-rewind-line").width() * currentFrame / (playback.framesCount - 1));
        
        currentFrame++;
    }

    updateCameraDirection();
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

function updateSensors() {
    var i = objects[currentObjectId].i;
    if (i >= objects[currentObjectId].states.length) {
        i = objects[currentObjectId].states.length - 1;
    }
    
    var sensors = objects[currentObjectId].states[i].sensors;
    $("#player-sensors").empty();
    sensors.forEach(function(it) {
        $("<p>" + it.sensor + ": " + it.value + "</p>").appendTo("#player-sensors");
    });
}

function updateCameraDirection() {
    camera.lookAt(objects[currentObjectId].mesh.position);
}

function dispose() {
    $("#player").find("canvas").remove();
    $("body").removeClass("overflow-hidden");
    
    $("#player-users").empty();
    $("#player-progress-current").css("width", 0);
    if (!paused) {
        $("#player-play-pause").click();
    }
    
    scene.dispose();
    ground.geometry.dispose();
    ground.material.dispose();
    grid.geometry.dispose();
    grid.material.dispose();
    objects.forEach(function(object) {
        object.mesh.geometry.dispose();
        object.mesh.material.dispose();
    });
    controls.dispose();
    renderer.dispose();
}
