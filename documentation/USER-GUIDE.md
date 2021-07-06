<h1 align="center">My Private Cloud Reference Design</h1>
<h3 align="center"><i> How to build a Private Cloud for less then $500 that will fit in a Shoe Box</i></h3>

#### Introduction
This page will outline a few things you will need to know to run the Portal Application or extend the My Private Cloud Reference Design.

Go [back](https://github.com/markreha/myprivatecloud) to the Main Page.
<br/>
<br/>

## Using the Portal


[Back to Top](#introduction)

## Extending My Private Cloud


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

