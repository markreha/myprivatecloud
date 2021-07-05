<h1 align="center">My Private Cloud Reference Design</h1>
<h3 align="center"><i> How to build a Private Cloud for less then $500 that will fit in a Shoe Box</i></h3>

#### Introduction
This page will provide prescriptive instructions that you can use to build your own Private Cloud based on the My Private Cloud Reference Design. There is nothing in this reference design that is specific to a Raspberry Pi 4. The Raspberry Pi 4 could be easily substitued for an Ubuntu Server running in a virtual machine. If you want to try this reference design using a cluster of Ubuntu Servers running in virtual machines then just skip the IT Tasks below.

Go [back](https://github.com/markreha/myprivatecloud) to the Main Page.
<br/>
<br/>

## Instructions
Step | Task | Instructions
:--: | ---- | ------------
1 | Setup the Hardware  | [IT Tasks](#it-tasks)
2 | Setup the Master Node  | [Master Node Tasks](#master-node-tasks)
3 | Setup the Worker Nodes  | [Worker Node Tasks](#worker-node-tasks)
4 | Setup the Portal Application  | [Portal Application Tasks](#portal-application-tasks)
<br/>
<br/>

## Prerequisites
You need to have moderate experience and skills in the following in order to complete many of the tasks:
1. [Raspberry Pi](https://www.raspberrypi.org)
2. [Linux](https://www.linux.org)
3. [Networks](https://www.geeksforgeeks.org/computer-network-tutorials/)
4. [Bash Shell Scripting](https://ryanstutorials.net/bash-scripting-tutorial/)
5. [Docker](https://www.docker.com)
6. [Kubernetes](https://kubernetes.io)

## IT Tasks
It should be noted that any variable that will be unique to your environment will be noted in the instructions in brackets with the name of variable (i.e. [YOUR IP ADDRESS]).
1. Order the Equipment
- 4 [Raspberry Pi 4 Model B with 4GB of RAM](https://www.amazon.com/gp/product/B07TVVJZQT/ref=ppx_yo_dt_b_asin_title_o01_s02?ie=UTF8&psc=1)
- 4 [Raspberry Pi 4 32Gb SD Cards](https://www.amazon.com/gp/product/B06XWN9Q99/ref=ppx_yo_dt_b_asin_title_o00_s00?ie=UTF8&psc=1)
- 4 [Raspberry Pi 4 Heat Sink and Fan](https://www.amazon.com/gp/product/B07W3MJ9Y2/ref=ppx_yo_dt_b_asin_title_o01_s01?ie=UTF8&psc=1)
- 1 [7-Layer Dog Bone Enclosure](https://www.amazon.com/gp/product/B01D916RNK/ref=ppx_yo_dt_b_asin_title_o02_s00?ie=UTF8&psc=1)
- 1 [Cat6 Ethernet Cable](https://www.amazon.com/gp/product/B00E5I7T9I/ref=ppx_yo_dt_b_asin_title_o02_s00?ie=UTF8&psc=1)
- 1 [Port Gigibit Ethernet Network Switch](https://www.amazon.com/gp/product/B000BCC0LO/ref=ppx_yo_dt_b_asin_title_o02_s00?ie=UTF8&psc=1)
- 1 [500GB WD USB 3 SSD Drive](https://www.amazon.com/dp/B07VSPL8FJ?ref=ppx_pop_mob_ap_share)
- 1 [Power Strip](https://www.amazon.com/dp/B091THXBFJ?ref=ppx_pop_mob_ap_share)
2. Create the Pi Boot Image:
	1. Insert your Micro SD card into your computer. 
    	2. Download and install the Raspberry Pi Imager from [here](https://www.raspberrypi.org/downloads/).
    	3. Run Raspberry Pi Imager application:
      	- Click the Choose OS button. Choose the Raspberry Pi OS option.
     	 - Click the Choose SD Card button. Choose the SD Card from step 1.
      	- Click the Write button. This will copy the Raspberry Pi operating system to the SD Card. This will take several minutes to complete.
	4. Using any text editor create an empty text file named ssh (with no file extension) in the root of the directory (/boot) of the SD card. This will enable SSH on the Raspberry Pi.
    	5. Configure Wi-Fi by using any text editor and create a text file named wpa_supplicant.conf in the root of the directory (/boot) of the SD card. Enter the following content and replace your Wi-Fi SSID and Password (these must be entered with the double quotes).
		```
        ctrl_interface=DIR=/var/run/wpa_supplicant GROUP=netdev
		update_config=1
		country=US
		network={
			scan_ssid=1
			ssid=[YOUR SSID]
			psk=[YOUR PASSWORD]
			key_mgmt=WPA-PSK
		}
        ```
    	6. Open the file "config.txt" located in the root of the directory (/boot) of the SD card using any text editor. Remove the "#" in front of the line "hdmi_force_hotplug=1". Save the file.
    	7. Unmount the SD card, place the SD card carefully in the Raspberry Pi, and power up the Raspberry Pi.
    	8. Find the IP address of the Raspberry Pi on your Wi-Fi network
    	9. Enable VNC and GUI Desktop (both are optional):
	  - Open a Terminal window
	  - SSH into your Raspberry Pi by using the following command and enter raspberry for the password when prompted: ssh pi@[PI IP ADDRESS] 
	  - Enable the VNC Interface by running the following command: sudo raspi-config
			Select the Interfacing Options menu option, select the VNC menu option, tab to the Yes option, select the Yes option when prompted, select the OK option and enter the return key.
	  - Enable the Desktop GUI by running the following command: sudo raspi-config
			Select the Boot Options menu option, select the Desktop /CLI menu option, select the Desktop Autologin menu option, tab to the Finish option and enter the return key, select Reboot when prompted.
	10. Power up the Raspberry Pi and use VNC Viewer from [here](https://www.realvnc.com/en/connect/download/viewer/)) to access the Raspberry Pi and to make sure boot image works.
3. Assemble the Cluster:
	1. Attach the Raspberry Pi heat sinks per the vendor instructions.
	2. Attach the Raspberry Pi fan per the vendor instructions.
	3. Label each of Raspberry Pi's: M1 for Master Node, N1 for Worker Node 1, N2 for Worker Node 2, and N3 for Worker Node 3.
	4. Mount the Master Node to the 1st Layer of the Dog Bone Cluster per the vendor instructions.
	5. Mount the Worker Node 1 to the 2nd Layer of the Dog Bone Cluster per the vendor instructions. Mount the Raspberry Pi fan for the Master Node to the side of the enclosure.
	6. Mount the Worker Node 2 to the 3rd Layer of the Dog Bone Cluster per the vendor instructions. Mount the Raspberry Pi fan for the Worker Node 1 to the side of the enclosure.
	7. Mount the Worker Node 3 to the 4th Layer of the Dog Bone Cluster per the vendor instructions. Mount the Raspberry Pi fan for the Worker Node 2 and Worker Node 3 to the side of the enclosure.
	8. Plug the Power Strip into the wall and make sure the power is turned off.
	9. Plug each of the Raspberry Pi's power adapters into the Power Strip and then plug in each connector of the power adaptor into the Raspberry Pi.
	10. Plug an Ethernet Cable into each of the Raspberry Pi and a port on the Ethernet Network Switch. Power on the Ethernet Network Switch.
	11. Turn the power on the Power Strip.
	12. Use VNC Viewer to access each of the Raspberry Pi's and to make sure they all work properly.
4. Setup the Cluster Network:

[Back to Top](#introduction)

## Master Node Tasks
TODO

[Back to Top](#introduction)

## Worker Node Tasks
TODO

[Back to Top](#introduction)

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

[Back to Top](#introduction)

<h5 align="center">Copyright @2021 On The Edge Software Consulting LLC</h5>


