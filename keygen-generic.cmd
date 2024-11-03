@echo off
setlocal

REM Check if the required arguments are provided
if "%1"=="" (
    echo Usage: %0 SRC_DIR TLS SIGN DESTINATION FILENAME
    echo Example: %0 certs\ example_client_tls example_client_signing java\open-banking-demo-app\src\main\resources\ keystore-premium.jks
    exit /b 1
)

REM Variables passed as arguments
set SRC_DIR=%1
set TLS=%2
set SIGN=%3
set DESTINATION=%4
set FILENAME=%5

set TEMP_KEYSTORE_NAME=store-open.keys

REM Check if destination directory exists
if not exist "%DESTINATION%" (
    echo.
    echo Directory %DESTINATION% DOES NOT exist.
    echo.
    exit /b
)

REM Prompt for password
echo.
echo Please provide a password of at least 6 characters.
echo This password should be used in your application to retrieve your certificates and keys.
echo.
set /p PASSWORD=Enter password: 

REM Suppress password input by using the choice command in Windows (or PowerShell for more control if needed)
REM Create tls.p12 and sign.p12 with OpenSSL
openssl pkcs12 -export -in "%SRC_DIR%%TLS%.pem" -inkey "%SRC_DIR%%TLS%.key" -name tls -password pass:%PASSWORD% -out tls.p12
openssl pkcs12 -export -in "%SRC_DIR%%SIGN%.pem" -inkey "%SRC_DIR%%SIGN%.key" -name sign -password pass:%PASSWORD% -out sign.p12

REM Import into keystore with keytool
keytool -importkeystore -srckeystore tls.p12 -destkeystore "%TEMP_KEYSTORE_NAME%" -srcstoretype pkcs12 -alias tls -srcstorepass %PASSWORD% -deststorepass %PASSWORD%
keytool -importkeystore -srckeystore sign.p12 -destkeystore "%TEMP_KEYSTORE_NAME%" -srcstoretype pkcs12 -alias sign -srcstorepass %PASSWORD% -deststorepass %PASSWORD%

REM Copy the keystore file to the destination with the specified filename
copy "%TEMP_KEYSTORE_NAME%" "%DESTINATION%%FILENAME%"

REM Clean up temporary files
del "%TEMP_KEYSTORE_NAME%"
del tls.p12
del sign.p12

endlocal
