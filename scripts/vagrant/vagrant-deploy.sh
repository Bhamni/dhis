#!/bin/bash -x -e

PATH_OF_CURRENT_SCRIPT="$( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd )"
source $PATH_OF_CURRENT_SCRIPT/vagrant_functions.sh

#All config is here
MODULE_DEPLOYMENT_FOLDER=/tmp/deploy_dhis_omod
CWD=$1
VERSION=$2
SCRIPTS_DIR=$CWD/scripts/vagrant
PROJECT_BASE=$PATH_OF_CURRENT_SCRIPT/../..

# Setup environment
run_in_vagrant -f "$SCRIPTS_DIR/setup_environment.sh"

set +e
# Kill tomcat
run_in_vagrant -f "$SCRIPTS_DIR/tomcat_stop.sh"
set -e

run_in_vagrant -c "mkdir -p $MODULE_DEPLOYMENT_FOLDER"
# Deploy Bhamni core
scp_to_vagrant $PROJECT_BASE/build/libs/openmrs-module-dhis*-$VERSION.omod $MODULE_DEPLOYMENT_FOLDER/openmrs-module-dhis-$VERSION.omod

# Copy omod files to the vagrant box - in /tmp
#Deploy them from Vagrant /tmp to appropriate location
run_in_vagrant -f "$SCRIPTS_DIR/deploy_omods.sh"

# Restart tomcat
run_in_vagrant -f "$SCRIPTS_DIR/tomcat_start.sh"
