#!/bin/bash

if [ $# = 0 ]; then
   set -- build
fi

true
while [ $? = 0 -o $? = 142 ]; do
   if gradle "$@"; then
      echo build successfully, rebuild after 60s, or enter to run immediately
      read -r -t 60
   else
      echo build failed, rebuild after 8s, or enter to run immediately
      read -r -t 8
   fi
done
