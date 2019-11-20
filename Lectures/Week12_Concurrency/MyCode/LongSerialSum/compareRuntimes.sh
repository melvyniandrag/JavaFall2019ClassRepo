N1=$1
T0=$(java SerialSum $N1)
T1=$(java SynchronizedSumReduce $N1)
echo $T0
echo $T1

