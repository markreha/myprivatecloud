#!/bin/sh

# Copy the default NodeJS Server to the volume mount
echo "Copying default NodeJS server to volume mount.........."
cd /
cd defaultapp
cp server.js /usr/src/app
cp index.html /usr/src/app
cp package.json /usr/src/app

# Run NPM script from package.json as specified in Environment Variable NPM_SCRIPT
echo "Passed in NPM script is" $NPM_SCRIPT
if [ -z "$NPM_SCRIPT" ]
then
	SCRIPT="start-nodemon"
else
	SCRIPT=$NPM_SCRIPT
fi
echo "Running npm script $SCRIPT .........."
npm run $SCRIPT >> /var/log/app.log




