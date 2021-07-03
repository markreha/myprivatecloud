# My Private Cloud Reference Design
Can we design and build a fully functioning Private Cloud Platform using a cluster of Raspberry PI’s using current Cloud Technologies? I set out to answer this question.

This repository contains the design, code, and documentation for the My Private Cloud Reference Design, which is a project that implements a FULLY functioning Private Cloud using a cluster of Raspberry Pi 4's running on RaspbianOS, Docker, and Kubernetes that is all managed by a Port Application implmeneted using Spring Boot and Java 15.

<table>
    <tr>
        <td><img src="https://github.com/markreha/myprivatecloud/blob/main/documentation/design/Logical%20Architecture.png" alt="My Private Cloud Logical Architecture"/></td>
         <td><img src="https://github.com/markreha/myprivatecloud/blob/main/documentation/design/Physical%20Architecture.png" alt="My Private Cloud Physical Architecture"/></td>
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
## Portal Screenshots
- [Login](https://github.com/markreha/myprivatecloud/blob/main/documentation/screenshots/Login.png)
- [Catalog - Web Servers](https://github.com/markreha/myprivatecloud/blob/main/documentation/screenshots/Catalog1.png)
- [Catalog - Web App Servers](https://github.com/markreha/myprivatecloud/blob/main/documentation/screenshots/Catalog2.png)
- [Catalog - Databases](https://github.com/markreha/myprivatecloud/blob/main/documentation/screenshots/Catalog3.png)
- [Catalog - Tools](https://github.com/markreha/myprivatecloud/blob/main/documentation/screenshots/Catalog4.png)
- [My Apps](https://github.com/markreha/myprivatecloud/blob/main/documentation/screenshots/MyApps1.png)
- [Admin](https://github.com/markreha/myprivatecloud/blob/main/documentation/screenshots/Admin1.png)
## Hardware
- Raspberry Pi 4 with 4GB of RAM with 32Gb SD Card
- D-Link DGS-108 8 Port Gigibit Network Switch
- Power Switch
- 500Gb WD USB3 SSD Drive
## Software
- Raspberry Pi: 32 bit Raspbian OS, Docker, Kubernetes
- Port Application: Spring Boot, Java 15, on MySQL 8.0 database
## Design
- [Logical Architecture](https://github.com/markreha/myprivatecloud/blob/main/documentation/design/Logical%20Architecture.png)
- [Physical Architecture](https://github.com/markreha/myprivatecloud/blob/main/documentation/design/Physical%20Architecture.png)
- [Portal Database](https://github.com/markreha/myprivatecloud/blob/main/documentation/design/ER%20Database%20Diagram.png)
## Management
- [Lens - Cluster](https://github.com/markreha/myprivatecloud/blob/main/documentation/screenshots/Lens1.png)
- [Lens - Nodes](https://github.com/markreha/myprivatecloud/blob/main/documentation/screenshots/Lens2.png)
- [Lens - Pods](https://github.com/markreha/myprivatecloud/blob/main/documentation/screenshots/Lens3.png)
- [Portal - Users](https://github.com/markreha/myprivatecloud/blob/main/documentation/screenshots/Admin1.png)



