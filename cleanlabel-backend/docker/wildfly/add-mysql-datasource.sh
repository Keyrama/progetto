#!/bin/bash
# Questo script configura WildFly con il datasource MySQL PRIMA del deploy.
set -e

JBOSS_HOME=/opt/jboss/wildfly
CLI=$JBOSS_HOME/bin/jboss-cli.sh

echo "[entrypoint] Starting WildFly in admin-only mode to configure datasource..."
$JBOSS_HOME/bin/standalone.sh --admin-only &
WILDFLY_PID=$!

echo "[entrypoint] Waiting for WildFly admin mode..."
until $CLI --connect --command="ls" > /dev/null 2>&1; do
    sleep 2
done
echo "[entrypoint] WildFly admin mode ready. Configuring MySQL..."

$CLI --connect << CLIEOF

module add \
  --name=com.mysql.driver \
  --resources=/opt/jboss/wildfly/mysql-connector.jar \
  --dependencies=javax.api,javax.transaction.api

/subsystem=datasources/jdbc-driver=mysql:add(\
  driver-name=mysql,\
  driver-module-name=com.mysql.driver,\
  driver-class-name=com.mysql.cj.jdbc.Driver,\
  driver-xa-datasource-class-name=com.mysql.cj.jdbc.MysqlXADataSource\
)

data-source add \
  --name=CleanlabelDS \
  --jndi-name=java:jboss/datasources/CleanlabelDS \
  --driver-name=mysql \
  --connection-url="jdbc:mysql://mysql:3306/cleanlabel_db?serverTimezone=UTC&useSSL=false&allowPublicKeyRetrieval=true" \
  --user-name=cleanlabel_user \
  --password=cleanlabel_pass \
  --min-pool-size=5 \
  --max-pool-size=20 \
  --valid-connection-checker-class-name=org.jboss.jca.adapters.jdbc.extensions.mysql.MySQLValidConnectionChecker \
  --exception-sorter-class-name=org.jboss.jca.adapters.jdbc.extensions.mysql.MySQLExceptionSorter

:shutdown

CLIEOF

echo "[entrypoint] Datasource configured. Starting WildFly normally..."
exec $JBOSS_HOME/bin/standalone.sh -b 0.0.0.0 -bmanagement 0.0.0.0
