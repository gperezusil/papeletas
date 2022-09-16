FROM openjdk:8
VOLUME /tmp
RUN echo "America/Lima" > /etc/timezone
RUN ln -fs /usr/share/zoneinfo/America/Lima /etc/localtime
RUN dpkg-reconfigure -f noninteractive tzdata
ADD ./target/papeletas-0.0.2-SNAPSHOT.jar papeletas.jar
ENTRYPOINT ["java","-jar","/papeletas.jar"]