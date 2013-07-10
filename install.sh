#!/bin/bash

THIS=$( cd "$( dirname "$0" )" && pwd )

usage()
{
	echo "install.sh DESTINATION"
	echo "		DESTINATION: The location of directory to install workspace in"
}

if [ ! -d "$1" ]; then
	usage
	exit
else
	DEST="$1"
fi

echo "Installing 'emma-agent-loader' ..."

# Copy architecture
cp -r $THIS/src/emma-agent-loader/* $DEST/

# Uncompress JSON library
echo "Copying JSON library ..."
unzip -q $THIS/dep/json/JSON-java-master.zip -d $DEST/lib/json/
mkdir $DEST/lib/json/src/
cp -r $DEST/lib/json/JSON-java-master/* $DEST/lib/json/src/
rm -r $DEST/lib/json/JSON-java-master

# Uncompress Californium library
echo "Copying Californium library ..."
unzip -q $THIS/dep/californium/Californium-coap-13.zip -d $DEST/lib/californium/
mkdir $DEST/lib/californium/src/
cp -r $DEST/lib/californium/Californium-coap-13/californium/src/* $DEST/lib/californium/src/
rm -r $DEST/lib/californium/Californium-coap-13

# Patch Californium library
patch -p5 --directory=$DEST/lib/ --input=$THIS/patch/Californium_CoAP13-to-08_patch.diff

# Copy Californium dependencies
echo "Copying Californium dependencies ..."
mkdir $DEST/lib/californium/lib/
cp -r $THIS/dep/californium/extern/* $DEST/lib/californium/lib/

# Librairies compilation
mkdir $DEST/lib/dist
ant jar_libs -f $DEST/build.xml
 
echo "Done."
