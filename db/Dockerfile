FROM mysql:8.0

COPY --chown=mysql:mysql --chmod=0444 world.sql /docker-entrypoint-initdb.d/001-world.sql
