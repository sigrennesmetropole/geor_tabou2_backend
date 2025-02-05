FROM eclipse-temurin:17.0.14_7-jdk-jammy
USER root
MAINTAINER sigsupport@rennesmetropole.fr
ENV TZ=Europe/Paris

RUN apt-get update && apt-get upgrade -y
RUN mkdir -p /tmp/jetty
RUN chmod 777 -R /tmp && chmod o+t -R /tmp

RUN mkdir -p /etc/georchestra
RUN chmod 777 -R /etc/georchestra

RUN mkdir -p /opt/tabou2
ADD tabou2-facade.jar /opt/tabou2/service.jar
RUN chmod 777 -R /opt/tabou2

ENV PROPERTIES_PATH /etc/georchestra/tabou2/tabou2.properties

ADD entrypoint.sh /opt/tabou2/entrypoint.sh
ENTRYPOINT [ "/bin/bash", "/opt/tabou2/entrypoint.sh" ]

CMD [ "sh", "-c", "exec java --add-opens java.base/java.util=ALL-UNNAMED \
      -Duser.language=fr -Duser.country=FR \
      -Djava.io.tmpdir=/tmp/jetty \
      -Dgeorchestra.datadir=/etc/georchestra \
      -Dspring.config.additional-location=file:/etc/georchestra/ \
      ${ADD_JAVA_OPTS}                                   \
      -Xmx${XMX:-1G} -Xms${XMX:-1G}                      \
      -jar /opt/tabou2/service.jar" ]    
      