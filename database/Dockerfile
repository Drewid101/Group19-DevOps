FROM mysql
WORKDIR /tmp
COPY db/world.sql /docker-entrypoint-initdb.d
ENV MYSQL_ROOT_PASSWORD root