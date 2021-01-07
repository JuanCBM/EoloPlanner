let socket = new WebSocket("ws://" + window.location.host + "/plantNotifications");
const baseUrlPath = "http://localhost:3000/plants";
let availableCitiesCreated = [];
let socketId = null;
let plantsCreated = [];

loadPlants();

socket.onopen = function (e) {
    console.log("WebSocket connection established");
};

socket.onmessage = function (event) {
    console.debug("WebSocket message received:", event);

    let plant = JSON.parse(event.data);
    console.log("eventoMensaje")
    console.log(`Message from socket: ${JSON.stringify(plant)}`);
    if (plant.socketId) {
        socketId = plant.socketId;
    }else{
        updateProgress(plant);
    }
};

socket.onclose = function (event) {
    if (event.wasClean) {
        console.log(`[close] Connection closed cleanly, code=${event.code} reason=${event.reason}`);
    } else {
        console.log('[close] Connection died');
    }
};

socket.onerror = function (error) {
    console.log(`[error] ${error.message}`);
};


function updateProgress(plant) {
    let progressText = document.getElementById("progressText");

    if (plant.progress === 100) {
        progressText.innerText = "";
        controlCreatingPlantButton();
        addPlantToList(plant);
    } else {
        progressText.innerText = `Creating plant in ${plant.city}, progress:  ${plant.progress}%`;
    }
}

function controlCreatingPlantButton() {
    let createPlant = document.getElementById("creationButtonPlant");
    if (createPlant!=null){
        createPlant.disabled ? createPlant.disabled = false : createPlant.disabled = true;
    }
}

function createPlant() {
    let city = document.getElementById("city").value;
    let plant = { "city": city, "progress": 0};

  if (city === "" || !isCityAvailable(city)){
    alert("You must enter a valid city");
  } else {
    fetch(baseUrlPath, {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
            'socketid': socketId
        },
        body: JSON.stringify(plant)
    })
        .then(function (response) {
            if (response.ok) {
                controlCreatingPlantButton();
                return response.json()
            } else {
                throw "Error en la llamada Ajax";
            }
        })
        .then(function (plant) {
            updateProgress(plant);
        })
        .catch(function (err) {
            console.log(err);
        });
    }
}

function addPlantToList(plant) {
    plantsCreated.push({ id: plant.id, city: plant.city, progress: plant.progress, planning: plant.planning });
    let ul = document.getElementById("plants");
    let li = document.createElement("li");
    li.appendChild(document.createTextNode(plant.city  + " " + plant.progress + "% " + plant.planning));
    ul.appendChild(li);
}


function loadPlants() {
    fetch(baseUrlPath, {
        method: 'GET',
        headers: {
            'Content-Type': 'application/json'
        }
    })
        .then(function (response) {
            if (response.ok) {
                return response.json();
            } else {
                throw "Error getting eolic plants created";
            }
        })
        .then(function (plants) {
            for (let plant of plants) {
                addPlantToList(plant);
            }
        })
        .catch(function (err) {
            console.log(err);
        });
}


function loadAvailableCities() {
    let cities = [
        {name: "Madrid"},
        {name: "Barcelona"},
        {name: "Soria"},
        {name: "Santander"},
        {name: "Albacete"},
        {name: "Bilbao"}];

    for (let city of cities) {
        availableCitiesCreated.push({city: city.name});
        let ul = document.getElementById('availableCitiesCreated');
        let li = document.createElement('li');
        li.appendChild(document.createTextNode(city.name));
        ul.appendChild(li);
    }

}

function isCityAvailable(nameCity){
    for (let i=0; i < availableCitiesCreated.length; i++) {
        if (availableCitiesCreated[i].city.toLowerCase() === nameCity.toLowerCase()) {
            return true;
        }
    }
    return false;
}
