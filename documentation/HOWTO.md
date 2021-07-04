<h1 align="center">My Private Cloud Reference Design</h1>
<h3 align="center"><i> How to build a Private Cloud for less then $500 that will fit in a Shoe Box</i></h3>

This page will provide precscription instructions to build a Private Cloud based on this Reference Design.

Go [back](https://github.com/markreha/myprivatecloud).

<table>
    <tr>
        <td align="center">[IT Tasks](#it-tasks)</td>
        <td align="center">[Master Node Tasks](#master-node-tasks)</td>
        <td align="center">[Worker Node Tasks](#worker-node-tasks)</td>
        <td align="center">[Portal Application Tasks](#portal-application-tasks)</td>
    </tr>
</table>
   
## IT Tasks

## Master Node Tasks

## Worker Node Tasks

## Portal Application Tasks
1. Clone this repository.
2. Run the DDL script under documentation/design on an instance of a MySQL 8 database.
3. Import the code under apps/portal into the Spring Tool Suite.
4. Download the Kubernetes Config file (located on the Master Node at $HOME/.kube/config) to your local system.
    - scp pi@[PI_IP_ADDRESS]:~/.kube/config ~/config
6. Rename the download Kubernetes Config file from config to config-cluster.
7. Replace the new Kubernetes Config file with the one under src/main/resources/config.
8. Update the application-dev.properties and application-prod.properties files with your database configuration.
9. Run the application.
10. Register a new user. 
11. In the USERS table set the ADMIN column to 1 for this new user.
12. Log in as the new user.


