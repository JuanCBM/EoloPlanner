<h1 align="center">EoloPlanner 👨🏻‍💻 </h1>

<p align="center">
  <a href="/docs" target="_blank">
    <img alt="Documentation" src="https://img.shields.io/badge/documentation-yes-brightgreen.svg" />
  </a>
  <a href="#" target="_blank">
    <img alt="License: MIT" src="https://img.shields.io/badge/License-MIT-yellow.svg" />
  </a>
</p>

## Authors
👤 **JuanCBM**: Juan Carlos Blázquez Muñoz
* Github: [@JuanCBM](https://github.com/JuanCBM)

👤 **mahuerta**: Miguel Ángel Huerta Rodríguez
* Github: [@mahuerta](https://github.com/mahuerta)

## Ejecución de la aplicación:
**1.** Primero debemos ejecutar los comandos docker para disponer de las diferentes BBDD y de la cola de RabbitMQ
    
- RabbitMQ
    > docker run --rm -p 5672:5672 --name rabbitmq-eoloplant -p 15672:15672 rabbitmq:3-management
- Server Mysql
    > docker run -p 3306:3306 --name plant-mysql-db -e MYSQL_ROOT_PASSWORD=pass -e MYSQL_DATABASE=eoloplant -e -d mysql:latest
- TopoService MongoDB
    > docker run -d -p 27017:27017 --name topo-mongo-db mongo:latest

**2.** Situándonos en la carpeta del proyecto, ejecutamos el comando que permitirá la instalación de las dependencias de todos los proyectos
> node install.js

**3.** Finalmente, en la misma carpeta del proyecto, ejecutamos el comando que permitirá la ejecución de los diferentes servicios
> node exec.js

**4.** La aplicación Eoloplanner está en ejecución y se encuentra disponible en la url:
###[http://127.0.0.1:3000/](http://127.0.0.1:3000/)

**OPCIONAL:** 
- Si se desea ejecutar cada módulo por separado basta con comentar los módulos del script 'exec.js' que no se desean ejecutar.  

**COMENTARIOS:**
- Hemos adjuntado las siguientes dos peticiones en una colección de postman disponible en la raíz del proyecto
  para probar el Server, ya que desde el client no existía requisito de que esas operaciones se pudieran realizar.
  - Obtener los detalles de una planta
  - Borrar una planta  


## Servicios de la aplicación:

### Client
- Alojado en el Server
- AJAX
- WebSocket conexión con el Server
- Disponible en: [http://127.0.0.1:3000/](http://127.0.0.1:3000/)

### Server
- Node (JavaScript)
- WebSocket conexión con el Client
- Mysql
Base de datos mysql
> docker run -p 3306:3306 --name plant-mysql-db -e MYSQL_ROOT_PASSWORD=pass -e MYSQL_DATABASE=eoloplant -e -d mysql:latest  
- Para hacer pruebas hemos utilizado una extensión del navegador Chrome: Simple Web Socket Client

### RabbitMQ 
Disponemos de dos colas:
* eoloplantCreationRequests: Manda mensajes de creación de plantas
* eoloplantCreationProgressNotifications: Manda mensajes de porcentaje de creación de la planta

> docker run --rm -p 5672:5672 --name rabbitmq-eoloplant -p 15672:15672 rabbitmq:3-management 

Enlace al gestor de rabbitmq: [http://localhost:15672/](http://localhost:15672/)
* User: guest 
* Pass: guest

### Planner
Consume mensajes de la cola, conecta con WeatherService y TopoService, escribe mensaje en la cola.
- Spring Boot (Java)
- Consumición de datos en paralelo
- Es necesario ejecutar el comando siguiente para generar las fuentes del Proto.
> clean install 

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
- Implementado de forma reactiva funcional con WebFlux
- Spring Boot (Java)
- MongoDB

Comando de Docker para arrancar la base de datos:
> docker run -d -p 27017:27017 --name topo-mongo-db mongo:latest 