#!/bin/bash

API_ID=$1
SWAGGER_DIR=$2
echo "apiId: $API_ID"
echo "swaggerDir: $SWAGGER_DIR"
prefix=")]}',"

_jq() {
    echo "${2}" | base64 --decode | jq -r "${1}"
}

raw_versions=$(curl "https://api.developer.ing.com/apis/$API_ID/versions")

cleaned_versions="${raw_versions//$prefix}"

echo "$cleaned_versions"

for row in $(echo "${cleaned_versions}" | jq -r '.apis[] | @base64'); do
    name=$(_jq '.api.name' "${row}")
    versionNumber=$(_jq '.versionNumber' "${row}")
    versionId=$(_jq '.versionId' "${row}")
    status=$(_jq '.status' "${row}")

    echo "--------------"
    echo "$name"
    echo "$versionNumber"
    echo "$versionId"
    echo "$status"

    if [ "$status" == "LIVE" ]; then
	    file_name=$(echo "../api/${SWAGGER_DIR}/${name}.json" | tr " " "-")
        if [ ! -f "$file_name" ]; then
             echo "$file_name does not exist. Downloading...."
             curl "https://api.developer.ing.com/apis/$API_ID/versions/${versionId}/specification/download?format=json&pretty=true&resolved=false" -o "$file_name"
	     git add "$file_name"
        else
             echo "$file_name exist."
             currentVersion=$(jq -r .info.version "${file_name}")
             echo "currentversion: $currentVersion"
             if [ "$versionNumber" == "$currentVersion" ]; then
                 echo "VERSION MATCH"
             else
                 echo "DOWNLOAD NEW VERSION"
                 curl "https://api.developer.ing.com/apis/$API_ID/versions/${versionId}/specification/download?format=json&pretty=true&resolved=false" -o "$file_name"
                 git add "$file_name"
             fi
        fi
        break
    fi
    echo "--------------"
done
