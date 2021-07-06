<h1 align="center">My Private Cloud Reference Design</h1>
<h3 align="center"><i> How to build a Private Cloud for less then $500 that will fit in a Shoe Box</i></h3>

#### Introduction
This page will outline a few things you will need to know to run the Portal Application or extend the My Private Cloud Reference Design.

Go [back](https://github.com/markreha/myprivatecloud) to the Main Page.
<br/>
<br/>

## Using the Portal Application
1. Registration and Logging In:
  - First you will need to register as a new User. This can be done by clicking the Register menu from the Main menu. Complete the Registration Form and click the Register button. It should be noted that the Username must be unique across all Portal Users. If you need Portal Administration functionality you will to need update the USERS table in the Portal Database and update the ADMIN column to a value of 1.
  - To log in, click the Log In menu from the Main menu, enter the Username and Password you entered during Registration, and click the Log In button. Once you are logged in the Catalog page will be displayed.
2. Creating an Application:
  - You can create an Application by selecting the Catalog manu from the Main menu. The Catalog is divided into different categories of Stacks.
    - Web Servers: select this to run basic static web sites.
    - Web App Servers: select this to run dynamic web application in Spring, Spring Boot, PHP, or PHP Laravel.
    - Database Servers: select this to create a relation or non relational database for your dynamic web application.
    - Tools: select this to create various tools, such as a web based IDE.
  - Once you have selected your Stack, click the Configure button, give your Application a Name, select the CPU and RAM configuration, and click the Provision button. After 10-15 seconds the Stack will be created for you and the My Apps page will be displayed. You should carefully select the amount of CPU and RAM for your Application because these are both limited resources in My Private Cloud.
3. Deploying Code and Running an Application:
  - You can see all of your Applications by clicking the My Apps menu from the Main menu.
  - To run your Application just click the Go button. A default page is deployed for an Application when it is provisioned.
  - To see the details, such as the CPU and RAM, ports, or Environment Variables, of your Application just click the Details button.
  - To deploy code, you will first need to compress all your application code in Tar Gz format (i.e. a Tarball). This can be done by creating a folder for all of your application code, copying all the code to this folder, and running the following command from a Terminal Window. You can then click the Deploy button, select the Tar Gz file, and click the Deploy button. This will take a couple of minutes to complete. Your new Application can be run by clicking the Go button.  
    - OSX: COPYFILE_DISABLE=1 tar -pczvf deploy.tar.gz .
    - Windows: tar -pczvf deploy.tar.gz .
4. Deleting an Application:
  - You can see all of your Applications by clicking the My Apps menu from the Main menu.
  - To delete your Application just click the Delete button. You will be prompted and you can proceed with the deletion by clicking the Yes button.
5. Database Administration:
  - The databases in the Reference Design are pretty much provisioned with the default root username and password. Some databases have been customized using standard Docker Image Environment Variables. These can be found by displaying the Details for an Application. The IP Address and Port can also be found in the Details of the Application. You can use a SQL Client, such as MySQL Workbench, DBeaver, or MongoDB Compass, to access your database as the root user. From there you can execute desired DDL scripts and create additional database users for your Applications.
6. Administration:
  - You can access Administration features by clicking the Admin menu from the Main menu. This will only be visible for users with Administrative privileges.
  - The Portal Administration features are limited at this point. Currently, if you have Administrative access, you will be able to see all of the Portal Users, and if desired Supend or Activate them.

[Back to Top](#introduction)

## Extending My Private Cloud
  - The Reference Design can be extended by adding new Stacks to the Catalog. This can be done by adding new a database entry to the STACKS table in the Portal Database. You can also customize Environment Variables for a Docker Image by database entries to the EN_VARIABLES table. You can use the Test Application, which is a Java Console Application, found in the code of this repository to explore and play with new Stacks and Docker Images before adding them to the Portal Database. 
  - The following are specifications for the STACKS table:
    - ID: this is the auto generated PK (do not enter a value)
    - SHORT_NAME: short name with no spaces (this is used directory names and internal application ID generation)
    - LONG_NAME: long name that is a single sentance (displayed on the Stack Card)
    - DESCRIPTION: description that is 2-3 sentances (displayed on the Stack Card)
    - IMAGE: binary image file contents that should be 300x300 transparent gif or jpeg (displayed on the Stack Card)
    - DOCKER_IMAGE: Docker Image name from Docker Hub (you can also publish your own public Docker Images in your public repository)
    - POD_PATH: the Docker Image Volume that will be mounted on the Local or Network Storage in the Cloud
    - CATEGORY: set to web, webapp, database, or tool (aligns with the Catalog Tabs)
    - TARGET_PORT: Docker Image internal port
    - PUBLISH_PORT: Dokcer Image external port
    - CAN_DEPLOY_CODE: set to 1 to enable Code Tar Gz deployments (should be set to 0 for databases and tools, disables Traefik Proxy and uses Node Port)
    - PV_CLAIM_FK: set to 1 to deploy to Local Storage (Raspberry Pi SD Card) else set to 2 to deploy to Network Storage (USB 3 SD Drive)
  - The following are the specifications for the ENV_VARIABLES table:
    - ID: this is the auto generated PK (do not enter a value)
    - STACK_FK: Primary Key ID to the Stack that this Environment Variable is set for
    - NAME: standard Docker Image Environment Variable Name (see documentation in Docker Hub for the desired Docker Image)
    - VALUE: standard Docker Image Environment Variable Value (see documentation in Docker Hub for the desired Docker Image)

[Back to Top](#introduction)

## Known Issues
1. There is an exception from the Fabric8 API when copying a file to Pod. This is due to a timeout waiting for the Pod to be ready, which is still downloding a new Docker Image. The working around is to just delete the Deployment and do it again or preload all the Docker Images in each of the Worker Nodes.
2. MariaDB and PostgreSQL both do not work on Network Volume Claim. This might be fixed by using a USB 3 drive that is formatted as ext4 or by finding a Docker Image that supports setting MYSQL_AIO to 0.
3. The subPath does not get deleted for all Deployments. For now the clean up Cronjob Job does its best to try and cleanup everyting. This could be improved by creating an empty "delete" marker file in Deployment and then have the clean up Cronjob Job look for this file to indicate which directories to clean up.
4. Deployment of Tomcat (with logs of jars) requires a Deployment restart after deployment (via Lens). Also running the application the first time takes a long time before the application starts running. It is not know what is causing this problem. For Spring Boot, we might be able to enable Dev support to get Live Updating.

[Back to Top](#introduction)

## Future Ideas
  - Portal: Create Admin Screen to add/edit Stacks
  - Portal: Create Admin Screen to delete Deployments
  - Portal: Shutdown and restart Cluster (for now use Bash Scripts found in the Reference Design documentation)
  - Portal: Tail or real-time log Window (for now use Lens)
  - Portal: Interactive Console Window (for now use Lens)
 	- Portal: Enable Replicas by adding option to Configuration options
  - System: Disable Desktop UI on all Worker Nodes
  - System: Get DNS working

[Back to Top](#introduction)

