#!/bin/bash
echo "Compiling Pokemon Game..."

# Create bin directory if it doesn't exist
mkdir -p bin

# Compile
javac -cp "lib/*:." -d bin src/Main.java

# Create JAR
cd bin
jar cfm ../PokemonGame.jar ../manifest.txt *.class
cd ..

echo "Creating standalone package..."
# Create distribution folder
mkdir -p dist
cp PokemonGame.jar dist/
mkdir -p dist/lib
cp lib/*.jar dist/lib/

echo "Done! Game package created in 'dist' folder."
echo "Running the game..."
java -jar dist/PokemonGame.jar