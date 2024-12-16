#!/bin/bash

## load input
input=$(<$1)

function loadLineLength() {
   lineLength=$(head -n 1 "$1" | tr -d '\n' | wc -c)
   echo "$(($lineLength))"
}

function loadLineNumber() {
    linesNumber=$(wc -l < "$1")
    echo "$(($linesNumber + 1))"
}

lineLength=$(loadLineLength "$1")
linesNumber=$(loadLineNumber "$1")
data=()
for (( i=0; i<${#input}; i++ )); do
    data[i]="${input:i:1}"
done

getChar() {
    echo "${data[$(($1 * $lineLength + $2 + $1))]}"
}

foundAntennas=()
counter=0
for (( i=0; i < $linesNumber; i++ )); do
    for (( j=0; j < $lineLength; j++ )); do
        if [ $(getChar $i $j) != '.' ]; then
            foundAntennas[$counter]="$(getChar $i $j),$i,$j"
            counter=$((counter + 1))
        fi
    done
done

isInGrid() {
    if [ "$1" -ge 0 ] && [ "$1" -lt "$linesNumber" ] && [ "$2" -ge 0 ] && [ "$2" -lt "$lineLength" ]; then
        echo "1"
    else
        echo "0"
    fi
}

antiNodes1=()
antiNodesCounter1=0
antiNodes2=()
antiNodesCounter2=0
for (( i=0; i<$counter; i++ )); do
    for (( j=$i+1; j<$counter; j++ )); do
        echo "current element: $i $j"
        char1=$(echo "${foundAntennas[i]}" | awk -F "," '{print $1}')
        char2=$(echo "${foundAntennas[j]}" | awk -F "," '{print $1}')
        if [ $char1 = $char2 ]; then
            x1=$(echo "${foundAntennas[i]}" | awk -F "," '{print $2}')
            y1=$(echo "${foundAntennas[i]}" | awk -F "," '{print $3}')
            x2=$(echo "${foundAntennas[j]}" | awk -F "," '{print $2}')
            y2=$(echo "${foundAntennas[j]}" | awk -F "," '{print $3}')
            x_diff=$(($x1 - $x2))
            y_diff=$(($y1 - $y2))
            if [ "$(isInGrid $(($x1 + $x_diff)) $(($y1 + $y_diff)))" = "1" ]; then
                antiNodes1[$antiNodesCounter1]="$(($x1 + $x_diff))_$(($y1 + $y_diff))"
                antiNodesCounter1=$((antiNodesCounter1 + 1))
            fi
            while [ "$(isInGrid $(($x2 + $x_diff)) $(($y2 + $y_diff)))" = "1" ]
            do
                antiNodes2[$antiNodesCounter2]="$(($x2 + $x_diff))_$(($y2 + $y_diff))"
                antiNodesCounter2=$((antiNodesCounter2 + 1))
                x2=$(($x2 + $x_diff))
                y2=$(($y2 + $y_diff))
            done
            x1=$(echo "${foundAntennas[i]}" | awk -F "," '{print $2}')
            y1=$(echo "${foundAntennas[i]}" | awk -F "," '{print $3}')
            x2=$(echo "${foundAntennas[j]}" | awk -F "," '{print $2}')
            y2=$(echo "${foundAntennas[j]}" | awk -F "," '{print $3}')
            if [ "$(isInGrid $(($x2 - $x_diff)) $(($y2 - $y_diff)))" = "1" ]; then
                antiNodes1[$antiNodesCounter1]="$(($x2 - $x_diff))_$(($y2 - $y_diff))"
                antiNodesCounter1=$((antiNodesCounter1 + 1))
            fi
            while [ "$(isInGrid $(($x1 - $x_diff)) $(($y1 - $y_diff)))" = "1" ]
            do
                antiNodes2[$antiNodesCounter2]="$(($x1 - $x_diff))_$(($y1 - $y_diff))"
                antiNodesCounter2=$((antiNodesCounter2 + 1))
                x1=$(($x1 - $x_diff))
                y1=$(($y1 - $y_diff))
            done
        fi
    done
done

uniq=($(printf "%s\n" "${antiNodes1[@]}" | sort -u | tr '\n' ' '))
uniq2=($(printf "%s\n" "${antiNodes2[@]}" | sort -u | tr '\n' ' '))

echo "part 1: ${#uniq[@]}"
echo "part 2: ${#uniq2[@]}"
