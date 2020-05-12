var scene, camera, renderer, mesh, controls;

var currentFrame = 0;
var framesCount;
var objects = [];

$(document).ready(function() {
    $("#start").on("click", function() {
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
    
    $("#stop").on("click", function() {
        alert("stop");
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
