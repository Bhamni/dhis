#!/bin/sh -x -e

TEMP_LOCATION=/tmp/deploy_dhis_omod
USER=bahmni
OMOD_LOCATION=/home/$USER/.OpenMRS/modules

sudo rm -f $OMOD_LOCATION/dhis*.omod

sudo su - $USER -c "cp -f $TEMP_LOCATION/* $OMOD_LOCATION"
let returnCode=$?
let condition=$returnCode -ne 0
if [ $returnCode -ne 0 ]; then {
    exit $returnCode;
}
fi