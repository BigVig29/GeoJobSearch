# Use the mariadb image
FROM mariadb

# Copy all the files in the docker-entrypoint-initdb.d inside the container 
# which will run automatically when the sql server will start
COPY ./scripts/geoJobSearchSetup.sql /docker-entrypoint-initdb.d/
