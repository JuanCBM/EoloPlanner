# EoloPlanner
### Client	
- AJAX
- WebSocket client
- Disponible en: [http://127.0.0.1:3000/](http://127.0.0.1:3000/)


### Server
- Node (JavaScript)
- WebSocket server
- Mysql
Base de datos mysql
> docker run -p 3306:3306 --name plant-mysql-db -e MYSQL_ROOT_PASSWORD=pass -e MYSQL_DATABASE=eoloplant -e -d mysql:latest  


### RabbitMQ 
Disponemos de dos colas:
* eoloplantCreationRequests: Manda mensajes de creación de plantas.
* eoloplantCreationProgressNotifications: Manda mensajes de porcentaje de creación de la planta.

> docker run --rm -p 5672:5672 --name rabbitmq-eoloplant -p 15672:15672 rabbitmq:3-management 

Enlace al gestor de rabbitmq: [http://localhost:15672/](http://localhost:15672/)
* User: guest 
* Pass: guest


### Planner
Consume mensajes de la cola, conecta con WeatherService y TopoService, escribe mensaje en la cola.
- Spring Boot (Java)
- Consumición de datos en paralelo
- Es necesario hacer un 
> clean install 
para generar las fuentes del Proto.
Posteriormente hay que importar las fuentes:
  - Click derecho sobre la carpeta del proyecto
  - Maven
  - Generate Sources And Update Folders

### WeatherService
Calcula si una ciudad es soleada o lluviosa.
- API gRPC
- Node (JavaScript)


### TopoService
Calcula si una ciudad es montañosa o plana.
- API Rest
- Spring Boot (Java)
- WebFlux
- MongoDB

Comando de Docker para arrancar la base de datos:
> docker run -d -p 27017:27017 --name topo-mongo-db mongo:latest 