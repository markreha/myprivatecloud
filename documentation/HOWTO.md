<h1 align="center">My Private Cloud Reference Design</h1>
<h3 align="center"><i> How to build a Private Cloud for less then $500 that will fit in a Shoe Box</i></h3>

<br/>
<br/>
This page will provide precscription instructions to build your own Private Cloud based on the My Private Cloud Reference Design.

Go [back](https://github.com/markreha/myprivatecloud) to the Main Page.

Task  | Go To
------------- | -------------
Setup the Hardware  | [IT Tasks](#it-tasks)
Setup the Master Node  | [Master Node Tasks](#master-node-tasks)
Setup the Worker Nodes  | [Worker Node Tasks](#worker-node-tasks)
Setup the Portal Application  | [Portal Application Tasks](#portal-application-tasks)

## IT Tasks
1. Order the Equipment
- 4 [Raspberry Pi 4 Model B with 4GB of RAM](https://www.amazon.com/gp/product/B07TVVJZQT/ref=ppx_yo_dt_b_asin_title_o01_s02?ie=UTF8&psc=1)
- 4 [Raspberry Pi 4 32Gb SD Cards](https://www.amazon.com/gp/product/B06XWN9Q99/ref=ppx_yo_dt_b_asin_title_o00_s00?ie=UTF8&psc=1)
- 4 [Raspberry Pi 4 Heat Sinks and Fans](https://www.amazon.com/gp/product/B07W3MJ9Y2/ref=ppx_yo_dt_b_asin_title_o01_s01?ie=UTF8&psc=1)
- 1 [7-Layer Dog Bone Enclosure](https://www.amazon.com/gp/product/B01D916RNK/ref=ppx_yo_dt_b_asin_title_o02_s00?ie=UTF8&psc=1)
- 1 [Cat6 Ethernet Cables](https://www.amazon.com/gp/product/B00E5I7T9I/ref=ppx_yo_dt_b_asin_title_o02_s00?ie=UTF8&psc=1)
- 1 [Port Gigibit Ehternet Network Switch](https://www.amazon.com/gp/product/B000BCC0LO/ref=ppx_yo_dt_b_asin_title_o02_s00?ie=UTF8&psc=1)
- 1 [500GB WD USB 3 SSD Drive](https://www.amazon.com/dp/B07VSPL8FJ?ref=ppx_pop_mob_ap_share)
- 1 [Power Strip](https://www.amazon.com/dp/B091THXBFJ?ref=ppx_pop_mob_ap_share)
2. Assemble the Cluster:
- TODO

## Master Node Tasks
TODO

## Worker Node Tasks
TODO

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

[Back to Top](#it-tasks)

<h5 align="center">Copyright @2021 On The Edge Software Consulting LLC</h5>


