<table>
        <td><img src="https://github.com/markreha/myprivatecloud/blob/main/documentation/logo.jpg"/></td>
        <td align="center"><h1>My Private Cloud Reference Design</h1></td>
        <td><img src="https://github.com/markreha/myprivatecloud/blob/main/documentation/logo.jpg"/></td>
    </tr>
    <tr>
        <td align="center" colspan="3"><h3><i> How to build a Private Cloud for about $500 that will fit in a Shoe Box</i></h3></td>
    </tr>
</table>

#### Introduction
Can you design and build a fully functioning Private Cloud Platform using a cluster of Raspberry PI’s using current Cloud Technologies?
<br/>
I set out to answer this question.

This repository contains the design, code, and documentation for the My Private Cloud Reference Design, which is a project that implements a FULLY functioning Private Cloud using a cluster of Raspberry Pi 4's running on Raspbian OS, Docker, and Kubernetes that is all managed by a Portal Application implemented using Spring Boot and Java 15. By the way, there is nothing in this reference design that is specific to a Raspberry Pi 4. The Raspberry Pi 4 could easily be substitued for an Ubuntu Server running in a virtual machine. The research paper I wrote on this project can be found <a href="https://github.com/markreha/myprivatecloud/blob/main/documentation/My%20Private%20Cloud.pdf">here.</a>

Feel free to use this Reference Design to get started building your own Private Cloud!
<br/>
<br/>
A video demonstration can be seen [here](https://youtu.be/b0HLzCCKaRU).
<br/>
You can see how to build your own Private Cloud [here](https://github.com/markreha/myprivatecloud/blob/main/documentation/howto-guides/HOWTO.md).
<br/>
You can find the User Guide [here](https://github.com/markreha/myprivatecloud/blob/main/documentation/USER-GUIDE.md).
<br/>
<br/>
<table>
    <tr>
        <td><img src="https://github.com/markreha/myprivatecloud/blob/main/documentation/design/Logical%20Architecture.png"/></td>
        <td><img src="https://github.com/markreha/myprivatecloud/blob/main/documentation/design/Physical%20Architecture.png"/></td>
    </tr>
</table>
       
## Features
- Fully functioning Private Cloud:
    - Capable of running any number of Web Servers, common Web Stacks, Databases, and Tools. Examples include:
        - Web Servers: Nginx and Apache (can be used to host  too)
        - Web Stacks: PHP (7.1 and 7.4), PHP 7.4 Laravel, Java Tomcat (8.5 and 9.0), ASP.NET Core 5.0
        - Databases: MySQL, PostgreSQL, MongoDB
        - Tools: Visual Studio Code, NodeJS Development Server
    - Deploy Web Sites, Java Spring and Spring Boot applications, PHP and PHP Laravel applications, Express, React, Angular applications
- Cloud Management:
    - Web based Portal Application
    - Desktop based Lens Application (with Prometheus)
    - RealVNC and SSH for accessing the Master Node
- Low cost:
    - A single Master Node and three Worker Node Cluster with Storage will cost about $500
- Cloud Compute Resources are easily extendable:
    - You can add a new Worker Node in less then an hour and that will only cost about $75
- Portal Application is easily extendable:
    - New Stacks can be easily added to the Catalog
    - You can add a new Stack with simple Portal database update and this can be done in just minutes
    - You can leverage your own custom Docker Images that you pushed to your Docker Hub repository
    - Communication with Kubernetes via a custom Java Cluster API that leverages the Fabric8 library
- Portal Application is easy to use:
    - Go to the Register menu and register for your own account
    - Go to the Login menu and log into the application
    - Go to the Catalog menu:
        - Pick your Stack or Database
        - Click the Customize button
        - Give your Application a Name
        - Select between a Tiny, Medium, and Large Container Size (amount of CPU and RAM)
        - Click the Provision button
    - Go to the My Apps menu:
        - Click the Deploy button to deploy a compressed version of your application
        - Click the Go button to access your application

## Portal Screenshots
View the [JavaDoc](https://htmlpreview.github.io/?https://github.com/markreha/myprivatecloud/blob/main/documentation/javadoc/simple/index.html).
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
- [Raspberry Pi 4 Model B with 4GB of RAM](https://www.amazon.com/gp/product/B07TVVJZQT/ref=ppx_yo_dt_b_asin_title_o01_s02?ie=UTF8&psc=1)
- [Raspberry Pi 4 32Gb SD Cards](https://www.amazon.com/gp/product/B06XWN9Q99/ref=ppx_yo_dt_b_asin_title_o00_s00?ie=UTF8&psc=1)
- [Raspberry Pi 4 Heat Sinks and Fans](https://www.amazon.com/gp/product/B07W3MJ9Y2/ref=ppx_yo_dt_b_asin_title_o01_s01?ie=UTF8&psc=1)
- [7-Layer Dog Bone Enclosure](https://www.amazon.com/gp/product/B01D916RNK/ref=ppx_yo_dt_b_asin_title_o02_s00?ie=UTF8&psc=1)
- [Cat6 Ethernet Cables](https://www.amazon.com/gp/product/B00E5I7T9I/ref=ppx_yo_dt_b_asin_title_o02_s00?ie=UTF8&psc=1)
- [Port Gigibit Ethernet Network Switch](https://www.amazon.com/gp/product/B000BCC0LO/ref=ppx_yo_dt_b_asin_title_o02_s00?ie=UTF8&psc=1)
- [Ehternet Cables](https://www.amazon.com/gp/product/B00E5I7T9I/ref=ppx_yo_dt_b_asin_title_o02_s00?ie=UTF8&psc=1)
- [500GB WD USB-3 SSD Drive](https://www.amazon.com/Passport-Cobalt-Portable-External-Storage/dp/B07MVWJZVS/ref=sr_1_1?dchild=1&keywords=wd+500gb+my+passport&qid=1625792055&s=electronics&sr=1-1)
- [Power Strip](https://www.amazon.com/dp/B091THXBFJ?ref=ppx_pop_mob_ap_share)

## Software
- Raspberry Pi:
    - Raspbian OS (32-bit), Docker, Kubernetes
- Portal Application:
    - Spring Boot, Thymeleaf, Bootstrap, Java 15, and MySQL 8.0 database
- Other:
    -  Lens (with Prometheus)
    -  SQL Client (MySQL Workbench, DBeaver, and MongoDB Compass)

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
        <td align="center"><b>Lens - View Entire Cluster</b><br/><img src="https://github.com/markreha/myprivatecloud/blob/main/documentation/screenshots/Lens1.png"/></td>
        <td align="center"><b>Lens - View All Nodes</b><br/><img src="https://github.com/markreha/myprivatecloud/blob/main/documentation/screenshots/Lens2.png"/></td>
    </tr>
    <tr>
        <td align="center"><b>Lens - View All Pods</b><br/><img src="https://github.com/markreha/myprivatecloud/blob/main/documentation/screenshots/Lens3.png"/></td>
        <td align="center"><b>Manage Portal Users</b><br/><img src="https://github.com/markreha/myprivatecloud/blob/main/documentation/screenshots/Admin1.png"/></td>
    </tr>
</table>

[Back to Top](#introduction)

<h5 align="center">Copyright @2021 On The Edge Software Consulting LLC</h5>


