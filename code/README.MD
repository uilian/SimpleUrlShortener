## Simple URLShortener
This is a sample REST application built with SpringBoot, Redis and Docker which provides
an API to shorten URLs and redirect the generated URLs back to the original addresses.

### Versions
```
SpringBoot: 2.1.2
Maven: 3.*
Java JDK: 8
Docker: 18.09.1
Docker-compose: 1.21.2
```

#### Building the application:
Compile and build the application before the docker image generation:

```
mvn clean install -DskipTests
```

Generate the docker image (may take a while)

```
docker build . -t urlshortener:latest
```

### Running
Using docker-compose is the most straightforward way:

```
docker-compose up webapp
```

### Using the service

Getting a short URL:

````
curl --header "Content-Type: application/json" \
    --request POST \
    --data '{"url":"http://www.google.com/"}' \      
    http://localhost:8080/  
```


Calling the url saved:

``` 
curl --request GET http://localhost:8080/[URL_ID]
```

See some statistics:

``` 
curl --request GET http://localhost:8080/statistics
```


### TODO
* Add Swagger documentation to the API.
* Improve the statistics with more information.
* Improve the environment switching.
