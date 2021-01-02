const axios = require('axios').default;
const WebSocket = require('ws');

let webSocket = new WebSocket("ws://" + window.location.hostname + "/plantNotifications");
const baseUrlPath = "http://localhost:3000/eolicplants";
const topographyUrlPath = "http://localhost:8080/api/topographicdetails/cityLandscapes";
let availableCities = [];
let socketId = null;
let plantsCreated = [];

loadPlants();
loadAvailableCities();



function subscribeToNotificationPlant() {
    webSocket.onopen = function (e) {
        console.log("WebSocket connection established");
    };

    webSocket.onmessage = function (event) {
        let plant = JSON.parse(event.data);
        console.log(`Message from socket: ${JSON.stringify(plant)}`);
        if (plant.socketId) {
            socketId = plant.socketId;
        }else{
            updateProgress(plant);
        }
    };

    webSocket.onclose = function (event) {
        if (event.wasClean) {
            console.log(`[close] Connection closed cleanly, code=${event.code} reason=${event.reason}`);
        } else {
            console.log('[close] Connection died');
        }
    };

    webSocket.onerror = function (error) {
        console.log(`[error] ${error.message}`);
    };

}

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
    let createPlant = document.getElementById("createPlant");
    createPlant.disabled ? createPlant.disabled = false : createPlant.disabled = true;
}

function createPlant() {
    let city = document.getElementById("city").value;
    let plant = { "city": city, "progress": 0};

  if (city == "" || !isCityAvailable(city)) {
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
                manageCreatingPlantButton();
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
    plantsCreated.push({ id: plant.id, city: plant.city });
    let ul = document.getElementById("plants");
    let li = document.createElement("li");
    li.appendChild(document.createTextNode(plant.city));
    ul.appendChild(li);
}

function addCityLandscapeToList(cityLandscape) {
    availableCitiesCreated.push({city: cityLandscape.id});
    let ul = document.getElementById("availableCities");
    let li = document.createElement("li");
    li.appendChild(document.createTextNode(cityLandscape.id));
    ul.appendChild(li);
}

async function getPlantInfo() {
    if (plantId != undefined) {
        console.log('Getting plant info through API REST');
        const url = baseUrlPath + '/plants/' + plantId;
        try {
            let response = await axios.get(url);
            console.log(response.data);
        } catch (error) {
            console.error(error);
        }
    } else {
        console.warn('Plant not created');
    }
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
    fetch(topographyUrlPath, {
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
        .then(function (landscapes) {
            for (let landscape of landscapes) {
                addCityLandscapeToList(landscape);
            }
        })
        .catch(function (err) {
            console.log(err);
        });
}

function isCityAvailable(nameCity){
    for (var i=0; i < availableCitiesCreated.length; i++) {
        if (availableCitiesCreated[i].city.toLowerCase() === nameCity.toLowerCase()) {
            return true;
        }
    }
    return false;
}

$('#createPlant').click(createPlant);
$('#getPlantInfo').click(getPlantInfo);
$('#subscribeToNotificationPlant').click(subscribeToNotificationPlant);


//TODO 2 FORMS OF MAKE THAT, I PREFER THE SECOND BUT NEED TODISCUSS

/* $(document).ready(function () {
    const urlRest = 'http://localhost:8080';

    let plantId;

    async function createPlant() {
        console.log('Creating plant through API REST');
        const url = urlRest + '/plants';
        const body = { text: 'creando planta desde el cliente en NodeJS' };

        try {

            console.log('TEXT: creando planta desde el cliente en NodeJS');
            let response = await axios.post(url, body);
            plantId = response.data.id;
            console.log(response.data);
            console.log('ID plant received: ', plantId);

        } catch (error) {
            console.error(error);
        }
    }

    async function getPlantInfo() {
        if (plantId != undefined) {
            console.log('Getting plant info through API REST');
            const url = urlRest + '/plants/' + plantId;
            try {
                let response = await axios.get(url);
                console.log(response.data);
            } catch (error) {
                console.error(error);
            }
        } else {
            console.warn('Plant not created');
        }
    }

    function subscribeToNotificationPlant() {
        let socket = new WebSocket('ws://localhost:8080/notifications');

        console.log(
            'Subscribing to WebSocket to get progress plant notifications',
        );

        socket.onopen = e => {
            console.log('WebSocket connection established');
        };

        socket.onmessage = data => {
            console.log('[message]: ' + data.data);
        };

        socket.onclose = event => {
            if (event.wasClean) {
                console.log(
                    `[close] Connection closed cleanly, code=${event.code} reason=${event.reason}`,
                );
            } else {
                console.log('[close] Connection died');
            }
        };

        socket.onerror = error => {
            console.log(`[error] ${error.message}`);
        };
    }
    $('#createPlant').click(createPlant);
    $('#getPlantInfo').click(getPlantInfo);
    $('#subscribeToNotificationPlant').click(subscribeToNotificationPlant);
}); */