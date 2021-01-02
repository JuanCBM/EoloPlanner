const { spawnSync } = require('child_process');
function exec(serviceName, command){

    console.log(`Installing dependencies for [${serviceName}]`);
    console.log(`Folder: ./${serviceName} Command: ${command}`);

    spawnSync(command, [], { 
        cwd: './' + serviceName,
        shell: true,
        stdio: 'inherit'
    });
}

exec('WeatherService', 'npm install');
exec('TopoService', 'mvn install');
exec('Server','npm install');
exec('Planner','mvn install');
