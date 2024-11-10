@echo off
echo Compiling Pokemon Game...

:: Create necessary directories
if not exist bin mkdir bin
if not exist dist mkdir dist
if not exist dist\lib mkdir dist\lib

:: Clean old files
del /Q bin\* 2>nul
del /Q dist\PokemonGame.jar 2>nul

:: Compile with all files in src directory
javac -cp "lib/*;." -d bin src\Main.java

:: Check if compilation was successful
if %errorlevel% neq 0 (
    echo Compilation failed!
    pause
    exit /b %errorlevel%
)

:: Create manifest file
echo Main-Class: Main> manifest.txt
echo Class-Path: lib/json-20240303.jar>> manifest.txt

:: Create JAR file
cd bin
jar cfm ..\dist\PokemonGame.jar ..\manifest.txt *.class
cd ..

:: Copy dependencies
copy lib\*.jar dist\lib\

:: Check if JAR was created successfully
if exist dist\PokemonGame.jar (
    echo Game package created successfully!
    echo Running the game...
    java -jar dist\PokemonGame.jar
) else (
    echo Failed to create JAR file!
)

pause