const MIN_DELAY = 1;
const MAX_DELAY = 3;

async function GetWeather(call, callback){

    console.log('Request received: '+JSON.stringify(call));

    let { city } = call.request;
    let weather = 'Sunny';

    if(startsWithVocal(city)){
        weather = 'Rainy'
    }

    await sleep(randomNumber(MIN_DELAY, MAX_DELAY + 1));

    callback(null, { city: city,
                    weather: weather });
}

function startsWithVocal (chain) {
    if (!chain || chain.length <= 0)
        return false;

    let firstCharacter = chain.toLowerCase().charAt(0);

    const vocals = ['a', 'e', 'i', 'o', 'u'];

    if (vocals.indexOf(firstCharacter) === -1) {
        return false;
    } else {
        return true;
    }
}

function sleep(s) {
    return new Promise(resolve => setTimeout(resolve, s/1000));
}

function randomNumber(minIncluded, maxNotIncluded) {
    return Math.random() * (maxNotIncluded - minIncluded) + minIncluded;
}

exports.GetWeather = GetWeather;