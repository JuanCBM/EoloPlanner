const { spawn } = require('child_process');
function exec(serviceName, command){

    console.log(`Stated service [${serviceName}]`);

    let cmd = spawn(command, [], { cwd: './' + serviceName, shell: true });
    cmd.stdout.on('data', function(data){
        process.stdout.write(`[${serviceName}] ${data}`);
    });

    cmd.stderr.on('data', function(data){
        process.stderr.write(`[${serviceName}] ${data}`);
    });
}

exec('WeatherService', 'node src/server.js');
exec('TopoService', 'mvn spring-boot:run');
exec('Server','node app.js');
exec('Planner','mvn spring-boot:run');
