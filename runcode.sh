#!/usr/bin/env bash
set -euo pipefail

# Project directory
cd "$(dirname "$0")"

# Tomcat configuration
export CATALINA_HOME="$HOME/Downloads/apache-tomcat-10.1.59"

# Validate Tomcat
if [[ ! -f "$CATALINA_HOME/bin/catalina.sh" ]]; then
    echo "ERROR: Tomcat installation not found!"
    exit 1
fi

# Build project
echo "==> Building project..."
mvn clean package -DskipTests

# Deploy WAR
echo "==> Deploying application..."
cp target/gym-management-1.0.0.war \
   "$CATALINA_HOME/webapps/gym-management.war"

# Start Tomcat
echo "==> Starting Tomcat..."
exec bash "$CATALINA_HOME/bin/catalina.sh" run
