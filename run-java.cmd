@echo off

set BASE_URL=https://api.sandbox.ing.com

REM Find the JAR file in the target directory
for %%f in (java\open-banking-demo-app\target\open-banking-demo-app-*.jar) do (
    set JAR_FILE=%%f
)
if not defined JAR_FILE (
    echo Error: No JAR file found matching pattern java\open-banking-demo-app\target\open-banking-demo-app-*.jar
    exit /b 1
)

REM Run the Java application
java -jar "%JAR_FILE%"
