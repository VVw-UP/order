# ms-pats-order-api

New Microservice for the retail OMS

## Add your files

```
cd existing_repo
git remote add origin https://gitlab.com/platinum-analytics/ocbc-fx-group/ocbc-retail-oms/ms-pats-order-api.git
git branch -M main
git push -uf origin main
```
## Docker

Using basic docker command to build the image
```
docker build -t ms-pats-order-api:0.0.1-SNAPSHOT  .
```

Using docker command with build caching to build the image
```
DOCKER_BUILDKIT=1 docker build -t ms-pats-order-api:0.0.1-SNAPSHOT .
```

## Docker for Database

### Creating the Database Container
``` 
docker run -d --name postgres_db -e POSTGRES_PASSWORD=mysecretpassword  -p 5432:5432 postgres
```
### Stopping and Destroying the container
``` 
docker stop postgres_db && docker rm postgres_db
```
