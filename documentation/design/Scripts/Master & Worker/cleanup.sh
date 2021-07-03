#!/bin/bash

# Check for proper command line argments
if [ $# -lt 2 ]
then
	echo "Invalid number of script arguments: arg1 is the source directory to scan and arg2 is whether to trial run (else will do delete action)"
	exit 1
fi

# Script Arguments
SRC_DIR=$1
TRIAL=$2

echo "Begining My Private Cloud Directory Cleanup.... at " $(date -u)
if [ $TRIAL -eq 0 ]
then
	find $1 -maxdepth 1 -mindepth 1 -empty -type d -print -delete
else
	echo "Doing trial run:"
	find $1 -maxdepth 1 -mindepth 1 -empty -type d -print
fi
echo "End My Private Cloud Directory Cleanup"
echo " "
echo " "

 

