#!/bin/bash
echo "building artifact"
sbt $SBT_BUILD
cd target/docker/stage
docker build --tag $REPOSITORY_URI:$TAG .
docker push $REPOSITORY_URI:$TAG

