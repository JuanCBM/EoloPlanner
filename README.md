# EoloPlanner

### RabbitMQ 
Cola de mensajes eoloplantCreationRequests.
> docker run --rm -p 5672:5672 -p 15672:15672 rabbitmq:3-management 


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