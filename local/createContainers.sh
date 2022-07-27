#!/bin/bash

docker network rm $(docker network ls | grep "transparency-consumer-network" | awk '// { print $1 }')
echo "Recive network docker"
docker rm -f $(docker ps -a -q --filter "name=kafka" | sort -u)
echo "Delete kafka"
docker network create "transparency-consumer-network"
echo "Created transparency-consumer-network"
echo "Loading compose..."
docker-compose -f ./docker-compose.yml up -d --build
