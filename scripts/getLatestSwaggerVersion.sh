#!/bin/bash

echo "apiId: $1"
echo "swaggerDir:$2"
prefix=")]}',"

_jq() {
    echo "${row}" | base64 --decode | jq -r "${1}"
}

raw_versions=$(curl "https://api.developer.ing.com/apis/$1/versions")

cleaned_versions=$(echo "$raw_versions" | sed -e "s/^$prefix//") 

echo "$cleaned_versions"



for row in $(echo "${cleaned_versions}" | jq -r '.apis[] | @base64'); do
    
    name=$(_jq '.api.name')
    versionNumber=$(_jq '.versionNumber')
    versionId=$(_jq '.versionId')
    status=$(_jq '.status')

    echo "--------------"
    echo "$name"
    echo "$versionNumber"
    echo "$versionId"
    echo "$status"

    if [ "$status" == "LIVE" ]; then 
	file_name=$(echo "api/${2}/${name}.json" | tr " " "-")

        if [ ! -f "$file_name" ]; then
             echo "$file_name does not exist. Downloading...."
             curl "https://api.developer.ing.com/apis/$1/versions/${versionId}/specification/download?format=json&pretty=true&resolved=false" -o "$file_name"
	     git add "$file_name"
        else
             echo "$file_name exist."
             currentVersion=$(jq -r .info.version "${file_name}")
             echo "currentversion: $currentVersion"
             if [ $versionNumber == $currentVersion ]; then
                 echo "VERSION MATCH"        
             else
                 echo "DOWNLOAD NEW VERSION"
                 curl "https://api.developer.ing.com/apis/$1/versions/${versionId}/specification/download?format=json&pretty=true&resolved=false" -o "$file_name"
                 git add "$file_name"
             fi
        fi
    fi
    echo "--------------"
done
