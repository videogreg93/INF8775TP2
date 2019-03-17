#!/bin/bash

OPTIONS=""
SOLUTION=false
TIME=false
while [[ $# -gt 0 ]]
do
key="$1"

case $key in
    -a)
    ALGO="$2"
    shift
    ;;
    -e)
    EX_PATH="$2"
    shift
    ;;
    -p)
    SOLUTION=true
	;;
	-t)
    TIME=true
	;;
    *)
        echo "Argument inconnu: ${1}"
        exit
    ;;
esac
shift
done

java -jar out/artifacts/TP2_INF8775_jar/TP2_INF8775.jar $ALGO $EX_PATH $SOLUTION $TIME