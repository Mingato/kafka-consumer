#!/bin/bash

docker-compose down -v
echo "Down containers"
docker network rm $(docker network ls | grep "transparency-consumer-network" | awk '// { print $1 }')
echo "Deleted transparency-consumer-network"
docker rm -f $(docker ps -a -q --filter "name=kafka" | sort -u)
echo "Deleted docker kafka"
docker rmi -f $(docker image ls -q --filter "reference=kafka" | sort -u)
echo "Deleted images kafka"
