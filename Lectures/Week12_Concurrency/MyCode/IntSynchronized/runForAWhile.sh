#!/bin/bash

END=10000
for iteration in $(seq 1 $END)
do
	java SynchronizedSumReduce 1000000 >> output.txt
done
