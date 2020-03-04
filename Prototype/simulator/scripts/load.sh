#!/bin/bash
x=1
date
while [ $x -le 5 ]
do
  groovy sender.groovy >/dev/null &
  x=$(( $x + 1 ))
done
x=1
#while [ $x -le 10 ]
#do
#  curl "http://localhost:1337/test"
#  sleep 1
#  x=$(( $x + 1 ))
#done
wait
date