
<p align='center'>
  <h1 align='center'>DDD Events Webflux Spring Reactive | Work 2 - Distributed System</h1>
</p>

<p align='center'>
  <h1 align='center'>Throw an event when the driver moves away from transportation</h1>
 <img src='https://user-images.githubusercontent.com/28990749/102723069-6de4e600-42ba-11eb-965f-e820d4af9b30.png'/>
</p>


## Route

- From the first stop the mobile is away until the last coordinate at which it returns to transport

<p align='center'>
 <img src='https://user-images.githubusercontent.com/28990749/102723107-b8fef900-42ba-11eb-8e4f-001f89da5ae0.png'/>
</p>

## Create Route 

**POST: localhost:8080/routes**
```json
{
    "id": 10000,
    "name": "Route Test",
    "datePlans": 1607525341441,
    "equipment": {
        "id": 10000,
        "type": "TRACK"
    },
	 "mobileEquipment": {
      "id": 13,
      "type": "MOBILE"
    },
    "stops": [{
        "id": 1,
        "address": "Rua Padre joaquim de menezes, 976",
        "latitude": -5.0720683,
        "longitude": -37.9894671
    },{
        "id": 2,
        "address": "Rua Veriador efisio costa, 01",
        "latitude": -5.0747838,
        "longitude":-37.9908696
    },{
        "id": 4,
        "address": "Last ~> Rua Veriador efisio costa, 1027",
        "latitude": -5.0775801,
        "longitude":-37.9848137
    }]
}

```

## Running 

- use db mongo 
- in resource.properties change the url mongo
  - if you use docker
    - pull the image mongo `docker pull mongo`
    - run `docker -p 27017:27017 -d mongo` 
- install deps springboot - Marvem
- run RouteApplication
- when print `Iniciate send coordinates`, create route with item above

## Logs

<p align='center'>
 <img src='https://user-images.githubusercontent.com/28990749/104813635-3a18b580-57bf-11eb-8b7d-cb9989772320.png'/>
</p>



