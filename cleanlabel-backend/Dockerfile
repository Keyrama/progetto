FROM quay.io/wildfly/wildfly:30.0.0.Final-jdk17

# Tutti gli step di build come root per evitare problemi di permessi
USER root

# Step 1: scarica il connettore MySQL
ADD https://repo1.maven.org/maven2/com/mysql/mysql-connector-j/8.3.0/mysql-connector-j-8.3.0.jar \
    /opt/jboss/wildfly/mysql-connector.jar

# Step 2: copia ed esegui lo script di configurazione
COPY docker/wildfly/configure-datasource.sh /opt/jboss/configure-datasource.sh
RUN chmod +x /opt/jboss/configure-datasource.sh && /opt/jboss/configure-datasource.sh

# Step 3: copia il WAR e ripristina i permessi
COPY target/cleanlabel-backend-1.0-SNAPSHOT.war \
     /opt/jboss/wildfly/standalone/deployments/cleanlabel-backend.war
RUN chown -R jboss:jboss /opt/jboss/wildfly/standalone /opt/jboss/wildfly/modules/com

# Runtime come jboss
USER jboss

EXPOSE 8080 9990

CMD ["/opt/jboss/wildfly/bin/standalone.sh", "-b", "0.0.0.0", "-bmanagement", "0.0.0.0"]
