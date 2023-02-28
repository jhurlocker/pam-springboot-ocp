FROM registry.access.redhat.com/ubi8/openjdk-11:latest

USER 0

##############################
# vulenerability remediation #
##############################
RUN microdnf update -y && \
    microdnf clean all

# NOTE / WARNING / IMPORTANT:
#   work around for https://bugzilla.redhat.com/show_bug.cgi?id=1798685
RUN rm -f /var/log/lastlog

###############
# install app #
###############
RUN mkdir /app
#ADD target/*.jar /app/app.jar
COPY root /
RUN chown -R 1001:0 /opt/spring-service && chmod -R 774 /opt/spring-service
EXPOSE 8090

###########
# run app #
###########
USER 1001
ENTRYPOINT ["java", "-jar","-Duser.home=$HOME","-Xms500M", "-Xmx2048M", "-XX:+UseG1GC"]
CMD ["/opt/spring-service/eforms-service-1.0-SNAPSHOT.jar"]
