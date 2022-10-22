FROM java:8

COPY *.jar /app.jar

CMD ["--server.port=7529"]

EXPOSE 8080

ENTRYPOINT ["java","-jar","/app.jar"]