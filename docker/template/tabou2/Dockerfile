FROM jetty:9.4.55-jdk17-eclipse-temurin
USER root

ADD georchestra-tabou2-1.0-SNAPSHOT.war /var/lib/jetty/webapps/tabou2.war

ENV PROPERTIES_PATH /etc/georchestra/tabou2/tabou2.properties

ADD entrypoint.sh /opt/tabou2/entrypoint.sh
ENTRYPOINT [ "/bin/bash", "/opt/tabou2/entrypoint.sh" ]

ENV PROPERTIES_PATH /etc/georchestra/tabou2/tabou2.properties

RUN mkdir -p /opt/tabou2
ADD entrypoint.sh /opt/tabou2/entrypoint.sh
ENTRYPOINT [ "/bin/bash", "/opt/tabou2/entrypoint.sh" ]

CMD ["sh", "-c", "exec java -Djava.io.tmpdir=/tmp/jetty \
  ${ADD_JAVA_OPTS} \
  -Dgeorchestra.datadir=/etc/georchestra \
  -Duser.language=fr -Duser.country=FR \
  -Xmx${XMX:-1G} -Xms${XMX:-1G} \
  -jar /usr/local/jetty/start.jar"]
      