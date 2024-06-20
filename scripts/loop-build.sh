#!/bin/bash

s_timeout=120
f_timeout=8

if [ $# = 0 ]; then
   set -- build
fi

true
while [ $? = 0 -o $? = 142 ]; do
   if gradle "$@"; then
      echo build successfully, rebuild after ${s_timeout}s, or enter to run immediately
      read -r -t $s_timeout
   else
      echo build failed, rebuild after ${f_timeout}s, or enter to run immediately
      read -r -t $f_timeout
   fi
done
