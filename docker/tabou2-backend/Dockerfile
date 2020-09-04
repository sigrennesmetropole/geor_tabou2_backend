FROM jetty:9.3-jre8
MAINTAINER dcottencin@open-groupe.com
ENV TZ=Europe/Paris

ADD /var/lib/jetty/webapps/tabou2.war /var/lib/jetty/webapps/tabou2.war
#ADD etc/ /etc

CMD ["sh", "-c", "exec java -Djava.io.tmpdir=/tmp/jetty \
  ${ADD_JAVA_OPTS} \
  -Dgeorchestra.datadir=/etc/georchestra \
  -Duser.language=fr -Duser.country=FR \
  -Xmx${XMX:-1G} -Xms${XMX:-1G} \
  -jar /usr/local/jetty/start.jar"]
