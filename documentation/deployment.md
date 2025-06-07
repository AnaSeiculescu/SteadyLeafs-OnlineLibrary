## Separating local and production environments

- Added string templates to *application.properties* in order to accept environment variables.
- Created *application-local.properties* for local development. 
This required adding an environment variable to load the proper configuration file `SPRING_PROFILES_ACTIVE=local`.

## Containerizing the application

- Created Dockerfile.
  This required configuring two stages, first one to build the application and the second one to run it.
- Created *.env* file for passing environment variables to the Docker container when running locally.
   
**Running Docker locally**
1. Build the image `docker build -t steadyleafs-app .`
2. Run the container `docker run -d -p 8080:8080 --env-file .env --name steadyleafs-app-container steadyleafs-app`
3. Make sure that in your *.env* file the database host url is `host.docker.internal` instead of `localhost`,
if your database instance is running locally.

## Deployment to production

For the production environment I used the online service [Render](https://render.com).

- Created a new Postgres service.
- Created a new Web Service configured for running Docker.
- Added environment variables to the web service. The database connection variables are found in the Postgers
service's connection panel.
- The email username and password variables are necessary for JavaMailSender interface.
I got mine from my personal gmail account.