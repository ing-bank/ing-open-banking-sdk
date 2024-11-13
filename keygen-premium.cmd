@echo off
setlocal

REM Set variables
set SRC_DIR=certs/
set TLS=example_client_tls
set SIGN=example_client_signing
set DESTINATION=java\open-banking-demo-app\src\main\resources\
set FILENAME=keystore-premium.jks
set SIMPLE_DESTINATION=java\open-banking-simple-app\src\main\resources\

REM Run the key generation script with parameters
call keygen-generic.cmd %SRC_DIR% %TLS% %SIGN% %DESTINATION% %FILENAME%

REM Copy the generated keystore file to the simple app destination
copy "%DESTINATION%%FILENAME%" "%SIMPLE_DESTINATION%%FILENAME%"

endlocal
