#!/bin/sh -x

TEMP_LOCATION=/tmp/deploy_dhis_omod
USER=bahmni
OMOD_LOCATION=/home/$USER/.OpenMRS/modules

sudo rm -f $OMOD_LOCATION/openmrs-module-dhis*.omod

sudo su - $USER -c "cp -f $TEMP_LOCATION/* $OMOD_LOCATION"
