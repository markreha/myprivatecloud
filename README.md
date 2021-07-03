<h1 align="center">My Private Cloud Reference Design</h1>
<h3 align="center"><i> How to build a Private Cloud for less then $500 that will fit in a Shoe Box</i></h3>

Can we design and build a fully functioning Private Cloud Platform using a cluster of Raspberry PI’s using current Cloud Technologies?
<br/>
<br/>
I set out to answer this question.

This repository contains the design, code, and documentation for the My Private Cloud Reference Design, which is a project that implements a FULLY functioning Private Cloud using a cluster of Raspberry Pi 4's running on RaspbianOS, Docker, and Kubernetes that is all managed by a Port Application implmeneted using Spring Boot and Java 15.

<table>
    <tr>
        <td><img src="https://github.com/markreha/myprivatecloud/blob/main/documentation/design/Logical%20Architecture.png"/></td>
         <td><img src="https://github.com/markreha/myprivatecloud/blob/main/documentation/design/Physical%20Architecture.png"/></td>
    </tr>
</table>

## Features
- Fully functioning Private Cloud:
    - Capable of running a number of Web Servers, common Web Stacks, Databases, and Tools.
    - Web Servers: Nginx and Apache
    - Web Stacks: PHP, PHP Laravel, Java Tomcat
    - Databases: MySQL, PostgreSQL, MongoDB
    - Tools: VS Code
- Managed via a web based Portal Application and Lens.
- Low cost:
    - A 3 Node Cluster with Storage was less than $500.
- Cluster Hardware is easily extendable:
    - You can add a new Node in less then an hour and it only cost about $75.
- Portal Application is easily extendable:
    - New Stacks can be easily added to the Catalog.
    - You can add a new Stack with a database entry and this can be done in less than an hour.
    - Can leverage custom Docker Images pushed to Docker Hub.

## Portal Screenshots
<table>
    <tr>
        <td align="center"><b>Login</b><br/><img src="https://github.com/markreha/myprivatecloud/blob/main/documentation/screenshots/Login.png"/></td>
        <td align="center"><b>Admin</b><br/><img src="https://github.com/markreha/myprivatecloud/blob/main/documentation/screenshots/Admin1.png"/></td>
    </tr>
    <tr>
        <td align="center"><b>Catalog - Web Servers</b><br/><img src="https://github.com/markreha/myprivatecloud/blob/main/documentation/screenshots/Catalog1.png"/></td>
        <td align="center"><b>Catalog - Web App Servers</b><br/><img src="https://github.com/markreha/myprivatecloud/blob/main/documentation/screenshots/Catalog2.png"/></td>
    </tr>
    <tr>
        <td align="center"><b>Catalog - Databases</b><br/><img src="https://github.com/markreha/myprivatecloud/blob/main/documentation/screenshots/Catalog3.png"/></td>
        <td align="center"><b>Catalog - Tools</b><br/><img src="https://github.com/markreha/myprivatecloud/blob/main/documentation/screenshots/Catalog4.png"/></td>
    </tr>
    <tr>
        <td align="center"><b>My Deployed Apps</b><br/><img src="https://github.com/markreha/myprivatecloud/blob/main/documentation/screenshots/MyApps1.png"/></td>
        <td align="center"></td>
    </tr>
</table>

## Hardware
- Raspberry Pi 4 with 4GB of RAM with 32Gb SD Card
- D-Link DGS-108 8 Port Gigibit Network Switch
- Power Switch
- 500Gb WD USB3 SSD Drive

## Software
- Raspberry Pi: 32 bit Raspbian OS, Docker, Kubernetes
- Port Application: Spring Boot, Java 15, on MySQL 8.0 database

## System Design
<table>
    <tr>
        <td align="center"><b>Logical Architecture</b><br/><img src="https://github.com/markreha/myprivatecloud/blob/main/documentation/design/Logical%20Architecture.png"/></td>
        <td align="center"><b>Physical Architecture</b><br/><img src="https://github.com/markreha/myprivatecloud/blob/main/documentation/design/Physical%20Architecture.png"/></td>
    </tr>
     <tr>
        <td align="center"><b>Building the Cluster</b><br/><img src="https://github.com/markreha/myprivatecloud/blob/main/documentation/screenshots/Cluster1.jpeg"/></td>
        <td align="center"><b>Building the Cluster</b><br/><img src="https://github.com/markreha/myprivatecloud/blob/main/documentation/screenshots/Cluster2.jpeg"/></td>
    </tr>
   <tr>
         <td align="center"><b>Final Cluster</b><br/><img src="https://github.com/markreha/myprivatecloud/blob/main/documentation/screenshots/Cluster3.jpeg""/>         </td>
       <td align="center"><b>Portal Database</b><br/><img src="https://github.com/markreha/myprivatecloud/blob/main/documentation/design/ER%20Database%20Diagram.png"/></td>
    </tr>
</table>

## Cloud Management and Monitoring
<table>
    <tr>
        <td align="center"><b>Lens - Cluster</b><br/><img src="https://github.com/markreha/myprivatecloud/blob/main/documentation/screenshots/Lens1.png"/></td>
        <td align="center"><b>Lens - Nodes</b><br/><img src="https://github.com/markreha/myprivatecloud/blob/main/documentation/screenshots/Lens2.png"/></td>
    </tr>
    <tr>
        <td align="center"><b>Lens - Pods</b><br/><img src="https://github.com/markreha/myprivatecloud/blob/main/documentation/screenshots/Lens3.png"/></td>
        <td align="center"><b>Portal Users</b><br/><img src="https://github.com/markreha/myprivatecloud/blob/main/documentation/screenshots/Admin1.png"/></td>
    </tr>
</table>



