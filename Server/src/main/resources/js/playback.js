var scene, camera, renderer, controls, ground, grid;
var paused, playerClosed, currentFrame, framesCount, objects, groundObj;
var toRadians = Math.PI / 180;

function init() {
    camera = new THREE.PerspectiveCamera( 45, window.innerWidth / (window.innerHeight - 90), 1, 10000 );
    camera.position.set( 200, 100, 200 );

    scene = new THREE.Scene();
    scene.background = new THREE.Color( 0xa0a0a0 );
    //scene.fog = new THREE.Fog( 0xa0a0a0, 200, 10000 );
    
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
        new THREE.PlaneBufferGeometry(groundObj.size, groundObj.size),
        new THREE.MeshPhongMaterial({ color: groundObj.color, depthWrite: false })
    );
    ground.rotation.x = -Math.PI / 2;
    ground.receiveShadow = true;
    scene.add(ground);

    grid = new THREE.GridHelper(groundObj.size, groundObj.gridDivisions, groundObj.gridCenterLineColor, groundObj.gridColor);
    grid.material.opacity = groundObj.opacity;
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
    controls.target.set(0, 5, 0);
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

function dispose() {
    $("#player").find("canvas").remove();
    $("body").removeClass("overflow-hidden");
    
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
