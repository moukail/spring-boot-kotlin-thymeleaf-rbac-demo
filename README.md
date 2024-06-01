#### Gradle build
```bash
 cd web
./gradlew build
```

#### Docker up
```bash
docker compose up -d
```

#### Docker down
```bash
docker compose down
```

#### > java.io.FileNotFoundException: /home/ismail/IdeaProjects/spring-boot-kotlin-bootstrap-rbac-demo/web/.gradle/8.7/fileHashes/fileHashes.lock (Permission denied)
```bash
sudo rm -rf web/.gradle/
sudo rm -rf web/build

```

### Liquibase
https://docs.liquibase.com/start/install/liquibase-linux-debian-ubuntu.html
#### Install - Ubuntu
```bash
wget -O- https://repo.liquibase.com/liquibase.asc | gpg --dearmor > liquibase-keyring.gpg && \
cat liquibase-keyring.gpg | sudo tee /usr/share/keyrings/liquibase-keyring.gpg > /dev/null && \
echo 'deb [arch=amd64 signed-by=/usr/share/keyrings/liquibase-keyring.gpg] https://repo.liquibase.com stable main' | sudo tee /etc/apt/sources.list.d/liquibase.list

sudo apt-get update
sudo apt-get install liquibase
#sudo apt-get upgrade liquibase

liquibase --version
```
#### generateChangeLog
```bash
liquibase --changeLogFile=src/main/resources/db/changelog/changes/db.changelog-master.sql \
--url jdbc:postgresql://localhost:32768/mydatabase \
--username myuser --password secret generateChangeLog

liquibase --changeLogFile=src/main/resources/db/changelog/changes/db.changelog-master.sql \
--url hibernate:spring:nl.moukafih.rbac?dialect=org.hibernate.dialect.PostgreSQLDialect generateChangeLog

liquibase --log-level debug --changeLogFile=src/main/resources/db/changelog/changes/db.changelog-1.sql \
--referenceUrl hibernate:spring:nl.moukafih.rbac?dialect=org.hibernate.dialect.PostgreSQLDialect \
--referenceDriver liquibase.ext.hibernate.database.connection.HibernateDriver \
--url jdbc:postgresql://localhost:32768/mydatabase --driver org.postgresql.Driver \
--username myuser --password secret diff

liquibase --logLevel=INFO --defaultsFile=liquibase.properties generateChangeLog

```

### Diff
```bash
./gradlew liquibaseDiff
```

### DiffChangelog
```bash
./gradlew clean
./gradlew liquibaseDiffChangelog
```

### Update
```bash
./gradlew liquibaseUpdate
```