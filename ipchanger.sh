#!/bin/bash
# this script changes the ip address of the application server inside the client from localhost to the VM ip address. Used for windows installation

echo -n "Please enter your VM ip > "
read text
echo "You entered: $text, changing ip..."

X="e=\"http://localhost:4567/\""
Y="e=\"http://"
Y+="$text"
Y+=":4567/\""

sed -i "s@$X@$Y@" Shortify-client/distributionFiles/scripts/scripts.de69d7be.js

echo done!
