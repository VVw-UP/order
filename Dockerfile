FROM adoptopenjdk/openjdk8-openj9:jdk8u292-b10_openj9-0.26.0-alpine as Maven-Build
RUN apk add maven
RUN mkdir /build
WORKDIR /build
COPY pom.xml /build/pom.xml
RUN mvn -B dependency:resolve-plugins dependency:resolve
COPY src /build/src
RUN mvn -B clean package

FROM adoptopenjdk/openjdk8-openj9:jre8u292-b10_openj9-0.26.0-alpine
RUN apk add dumb-init
RUN mkdir /opt/shareclasses
RUN mkdir /opt/app
COPY --from=Maven-Build /build/target/ms-pats-order-api-0.0.1-SNAPSHOT.jar /opt/app/ms-pats-order-api-0.0.1-SNAPSHOT.jar
RUN addgroup --system javauser && adduser -S -s /bin/false -G javauser javauser
RUN chown -R javauser:javauser /opt/app
RUN chown -R javauser:javauser /opt/shareclasses
USER javauser
EXPOSE 8080
CMD ["dumb-init","java", "-Xmx128m", "-XX:+IdleTuningGcOnIdle", "-Xtune:virtualized", "-Xscmx128m", "-Xscmaxaot100m", "-Xshareclasses:cacheDir=/opt/shareclasses", "-jar", "/opt/app/ms-pats-order-api-0.0.1-SNAPSHOT.jar"]


