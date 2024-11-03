@echo off
setlocal

REM Set variables from arguments
set API_ID=%1
set SWAGGER_DIR=%2

echo apiId: %API_ID%
echo swaggerDir: %SWAGGER_DIR%
set prefix=)]}',

REM Function to decode base64 and apply jq filter
set "_jq="
for /f "tokens=*" %%A in ('powershell -Command "[Convert]::FromBase64String((Get-Content -Raw) -replace '\n', '') | % { $_ | ForEach-Object { [System.Text.Encoding]::UTF8.GetString($_) } }"' ) do set "_jq=%%A"

REM Fetch versions
for /f "tokens=*" %%A in ('curl "https://api.developer.ing.com/apis/%API_ID%/versions"') do set "raw_versions=%%A"

REM Remove prefix from raw_versions
set "cleaned_versions=%raw_versions:%prefix%=%"

echo %cleaned_versions%

REM Parse versions
for /f %%A in ('echo %cleaned_versions% ^| jq -r ".apis[] | @base64"') do (
    set "row=%%A"
    for /f %%B in ('echo %row% ^| base64 --decode ^| jq -r ".api.name"') do set "name=%%B"
    for /f %%C in ('echo %row% ^| base64 --decode ^| jq -r ".versionNumber"') do set "versionNumber=%%C"
    for /f %%D in ('echo %row% ^| base64 --decode ^| jq -r ".versionId"') do set "versionId=%%D"
    for /f %%E in ('echo %row% ^| base64 --decode ^| jq -r ".status"') do set "status=%%E"

    echo --------------
    echo Name: %name%
    echo Version Number: %versionNumber%
    echo Version ID: %versionId%
    echo Status: %status%

    if "%status%"=="LIVE" (
        set "file_name=..\api\%SWAGGER_DIR%\%name%.json"
        set "file_name=%file_name: =-%"
        
        if not exist "%file_name%" (
            echo %file_name% does not exist. Downloading...
            curl "https://api.touchpoint.ing.net/apis/%API_ID%/versions/%versionId%/specification/download?format=json&pretty=true" -o "%file_name%"
            git add "%file_name%"
        ) else (
            echo %file_name% exists.
            for /f "tokens=*" %%F in ('jq -r ".info.version" "%file_name%"') do set "currentVersion=%%F"
            echo Current version: %currentVersion%
            if "%versionNumber%"=="%currentVersion%" (
                echo VERSION MATCH
            ) else (
                echo DOWNLOAD NEW VERSION
                curl "https://api.touchpoint.ing.net/apis/%API_ID%/versions/%versionId%/specification/download?format=json&pretty=true" -o "%file_name%"
                git add "%file_name%"
            )
        )
        goto :break
    )
    echo --------------
)

:break
endlocal
