# Hyrule Identifier Generator

The requirements can be found in the file `request.md`.
You may found the answers to those requirements in the file `answers.md`.

## How to :

### Build self-running jar file

To build this project you need :
- java 14
- maven

Then type in a command line :
```shell script
cd <project-root>
mvn clean install
```

The binary will be found at `target/hyrule-id-<version>.jar`
You can run it with the command
```shell script
cd target
java -jar target/hyrule-id-<version>.jar
``` 

For mutation testing you may run 
```shell script
mvn org.pitest:pitest-maven:mutationCoverage
```

### Build docker image

To build this image you need (obviously) :
- docker

Then type in a command line :
```shell script
docker build -t "verron.pro/hyrule-id" .
```
You can run it with the command
```shell script
docker run -p 8888:8888 "verron.pro/hyrule-id"
``` 

### Use the service
Once you got the server running, then you will be able to query `localhost:8888/hyrule/new-id/` to get new identifiers.

When you want to kill the server, you can query `localhost:8888/kill/` and it will try to die gracefully.

