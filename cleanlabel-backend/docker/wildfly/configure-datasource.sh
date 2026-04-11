#!/bin/bash
set -e

echo "=== Step 1: Creating MySQL module ==="
MODULE_DIR="/opt/jboss/wildfly/modules/com/mysql/driver/main"
mkdir -p "$MODULE_DIR"
cp /opt/jboss/wildfly/mysql-connector.jar "$MODULE_DIR/mysql-connector.jar"

cat > "$MODULE_DIR/module.xml" << 'XML'
<?xml version="1.0" encoding="UTF-8"?>
<module xmlns="urn:jboss:module:1.9" name="com.mysql.driver">
    <resources>
        <resource-root path="mysql-connector.jar"/>
    </resources>
    <dependencies>
        <module name="javax.api"/>
        <module name="javax.transaction.api"/>
    </dependencies>
</module>
XML

echo "=== Step 2: Patching standalone.xml ==="
STANDALONE_XML="/opt/jboss/wildfly/standalone/configuration/standalone.xml"

python3 << PYEOF
import sys, re

with open("$STANDALONE_XML", 'r') as f:
    content = f.read()

# Verifica che il file sia stato letto
print(f"File size: {len(content)} chars")

# Rimuovi il datasource ExampleDS (inclusi eventuali attributi e contenuto)
content = re.sub(
    r'\s*<datasource[^>]*jndi-name="java:jboss/datasources/ExampleDS".*?</datasource>',
    '',
    content,
    flags=re.DOTALL
)

# Il datasource CleanlabelDS da inserire
new_ds = '''
                <datasource jndi-name="java:jboss/datasources/CleanlabelDS" pool-name="CleanlabelDS" enabled="true" use-java-context="true">
                    <connection-url>jdbc:mysql://mysql:3306/cleanlabel_db?serverTimezone=UTC&amp;useSSL=false&amp;allowPublicKeyRetrieval=true</connection-url>
                    <driver>mysql</driver>
                    <security>
                        <user-name>cleanlabel_user</user-name>
                        <password>cleanlabel_pass</password>
                    </security>
                    <validation>
                        <valid-connection-checker class-name="org.jboss.jca.adapters.jdbc.extensions.mysql.MySQLValidConnectionChecker"/>
                        <exception-sorter class-name="org.jboss.jca.adapters.jdbc.extensions.mysql.MySQLExceptionSorter"/>
                    </validation>
                    <pool>
                        <min-pool-size>5</min-pool-size>
                        <max-pool-size>20</max-pool-size>
                    </pool>
                </datasource>'''

# Inserisci CleanlabelDS prima di </datasources>
if '</datasources>' in content:
    content = content.replace('</datasources>', new_ds + '\n            </datasources>')
    print("CleanlabelDS datasource inserted OK")
else:
    print("ERROR: </datasources> tag not found!")
    sys.exit(1)

# Aggiungi il driver MySQL prima di </drivers>
mysql_driver = '''
                    <driver name="mysql" module="com.mysql.driver">
                        <driver-class>com.mysql.cj.jdbc.Driver</driver-class>
                        <xa-datasource-class>com.mysql.cj.jdbc.MysqlXADataSource</xa-datasource-class>
                    </driver>'''

if '</drivers>' in content:
    content = content.replace('</drivers>', mysql_driver + '\n                </drivers>')
    print("MySQL driver registered OK")
else:
    print("ERROR: </drivers> tag not found!")
    sys.exit(1)

with open("$STANDALONE_XML", 'w') as f:
    f.write(content)

print("standalone.xml patched successfully!")
PYEOF

echo "=== Step 3: Verifying patch ==="
grep -c "CleanlabelDS" "$STANDALONE_XML" && echo "CleanlabelDS found in standalone.xml" || echo "ERROR: CleanlabelDS NOT found!"
grep -c "com.mysql.driver" "$STANDALONE_XML" && echo "MySQL driver found in standalone.xml" || echo "ERROR: MySQL driver NOT found!"

echo "=== Configuration complete ==="
