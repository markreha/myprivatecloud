<h1 align="center">My Private Cloud Reference Design</h1>
<h3 align="center"><i> How to build a Private Cloud for less then $500 that will fit in a Shoe Box</i></h3>

#### Introduction
This page will provide prescriptive instructions that you can use to build your own Private Cloud based on the My Private Cloud Reference Design. There is nothing in this reference design that is specific to a Raspberry Pi 4. The Raspberry Pi 4 could be easily substitued for an Ubuntu Server running in a virtual machine. If you want to try this reference design using a cluster of Ubuntu Servers running in virtual machines then just skip the IT Tasks below.

Go [back](https://github.com/markreha/myprivatecloud) to the Main Page.
<br/>
<br/>

## Instructions
Step | Task | Instructions | Text Instructions
:--: | ---- | ------------ | -----------------
1 | Setup the Hardware  | [IT Tasks](#it-tasks) | [Setup Raspberry Pi](https://github.com/markreha/myprivatecloud/blob/main/documentation/howto-guides/howto-setup-pi.txt)
[Setup Cluster](https://github.com/markreha/myprivatecloud/blob/main/documentation/howto-guides/howto-setup-cluster.txt)
[Setup Network](https://github.com/markreha/myprivatecloud/blob/main/documentation/howto-guides/howto-setup-network.txt)
2 | Setup the Master Node  | [Master Node Tasks](#master-node-tasks) | [Master Node Tasks](https://github.com/markreha/myprivatecloud/blob/main/documentation/howto-guides/howto-setup-master.txt)
3 | Setup the Worker Nodes  | [Worker Node Tasks](#worker-node-tasks) | [Worker Node Tasks](https://github.com/markreha/myprivatecloud/blob/main/documentation/howto-guides/howto-setup-worker.txt)
4 | Setup the Portal Application  | [Portal Application Tasks](#portal-application-tasks) | See below
5 | Setup the Lens Application  | [Lens Application Tasks](#lens-application-tasks) | See below
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
It should be noted that any variable that will be unique to your environment will be noted in the instructions in brackets with the name of variable in capital letters (i.e. [YOUR UNIQUE VARIABLE]).
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
	1. Open a Terminal Window on your development and SSH (enter ssh pi@[PI IP ADDRESS]) into the Raspberry Pi.
	2. Edit the hosts file in each Raspberry Pi per the following requirements:
	```shell
	# Master Node
	sudo nano /etc/hosts
	10.244.100.10   master-node
	
	# Worker Node 1
	sudo nano /etc/hosts
	10.244.100.11   worker-node1

	# Worker Node 2
	sudo nano /etc/hosts
	10.244.100.12   worker-mode2

	# Worker Node 3
	sudo nano /etc/hosts
	10.244.100.13   worker-node3
	3. Edit the DHCP configuration file in each Raspberry Pi per the following requirements:
	# Master Node
	sudo nano /etc/dhcpcd.conf
		interface eth0
		static ip_address=10.244.100.10/24
	sudo hostnamectl --transient set-hostname ${hostname}
	sudo hostnamectl --static set-hostname ${hostname}
	sudo hostnamectl --pretty set-hostname ${hostname}
	sudo reboot

	# Worker Node 1
	sudo nano /etc/dhcpcd.conf
		interface eth0
		static ip_address=10.244.100.11/24
	sudo hostnamectl --transient set-hostname ${hostname}
	sudo hostnamectl --static set-hostname ${hostname}
	sudo hostnamectl --pretty set-hostname ${hostname}
	sudo reboot
	
	# Worker Node 2
	sudo nano /etc/dhcpcd.conf
		interface eth0
		static ip_address=10.244.100.12/24
	sudo hostnamectl --transient set-hostname ${hostname}
	sudo hostnamectl --static set-hostname ${hostname}
	sudo hostnamectl --pretty set-hostname ${hostname}
	sudo reboot

	# Worker Node 3
	sudo nano /etc/dhcpcd.conf
		interface eth0
		static ip_address=10.244.100.13/24
	sudo hostnamectl --transient set-hostname ${hostname}
	sudo hostnamectl --static set-hostname ${hostname}
	sudo hostnamectl --pretty set-hostname ${hostname}
	sudo reboot
	```

[Back to Top](#introduction)

## Master Node Tasks
1. Open a Terminal Window on your development and SSH (enter ssh pi@[PI IP ADDRESS]) into the Raspberry Pi.
2. Install Docker by running the following commands:
	```shell
	# Install Docker
	sudo apt-get update
	sudo apt-get upgrade -y
	sudo apt-get install docker.io
	
	# Configure Docker
	sudo usermod -aG docker $USER
	sudo systemctl start docker
	sudo systemctl enable docker

	# Reboot
	sudo reboot
	
	# Testing
	docker version
	```
3. Install Kubernetes by running the following commands:
	```shell
	# Disable Swap
	echo Adding " Disabling Swap File
	sudo dphys-swapfile swapoff && \
	sudo dphys-swapfile uninstall && \
	sudo update-rc.d dphys-swapfile remove
	
	# Edit the following file and set CONF_SWAPSIZE=0 
	sudo nano /etc/dphys-swapfile

	# Setup CGroups
	echo Adding " cgroup_enable=cpuset cgroup_enable=memory" to /boot/cmdline.txt
	sudo cp /boot/cmdline.txt /boot/cmdline_backup.txt
	orig="$(head -n1 /boot/cmdline.txt) cgroup_enable=cpuset cgroup_memory=1 cgroup_enable=memory"
	echo $orig | sudo tee /boot/cmdline.txt
	
	# Reboot
	sudo reboot

	# Install Kubernetes
	# Install Kubernetes (if curl is not installed run the following command: sudo apt-get install curl)
	sudo apt-get install software-properties-common
	curl -s https://packages.cloud.google.com/apt/doc/apt-key.gpg | sudo apt-key add -
	echo "deb http://packages.cloud.google.com/apt/ kubernetes-xenial main" | sudo tee /etc/apt/sources.list.d/kubernetes.list 
	sudo apt-get update -q
	sudo apt-get install kubeadm kubelet kubectl -y

	# Reboot
	sudo reboot
	```
4. Setup Kubernetes by running the following commands:
	```shell
	# Init network only on MASTER 
 	sudo kubeadm init --pod-network-cidr=10.244.0.0/16
	
	# Setup Master Node configuration only on MASTER
	mkdir -p $HOME/.kube
	sudo cp -i /etc/kubernetes/admin.conf $HOME/.kube/config
	sudo chown $(id -u):$(id -g) $HOME/.kube/config

	# Install Flannel only on MASTER
	kubectl apply -f https://raw.githubusercontent.com/coreos/flannel/master/Documentation/kube-flannel.yml

	# Testing (make sure the Master is in a Ready status)
	kubectl version
	kubectl get nodes
	```
5. Setup Samba by running the following commands:
	```shell
	# Install Samba
	sudo apt update
	sudo apt install samba
	
	# Reboot
	sudo reboot

	# Test
	whereis samba
	
	# Master Node Only: Setup Samba Share
	sudo nano /etc/samba/smb.conf
		[share]
		comment = Samba on Ubuntu
		path = /media/pi/CLUSTERDATA/shared
		read only = no
		browsable = yes
		writeable = yes
		create mask = 0777
		directory mask = 0777
		force directory mode = 2777
		public = yes
		guest ok = no
		
    	# Master Node Only: Final Configuration and set the password on share to clusterdata for the user pi
    	sudo smbpasswd -a pi
    	sudo service smbd restart
	```
6. Setup Kubernetes CIFS Driver by running the following commands:
	```shell
	# NOTE: See https://github.com/fstab/cifs
	# NOTE: See https://www.programmersought.com/article/25171844047/
	# NOTE: Create base64 encoded Samba share username (pi) and passwrod (clusterdata)
	echo -n pi | base64
	echo -n clusterdata | base64

	# START: FROM YOUR LOCAL DEVELOPMENT PC
	# !!! GET cifs-secret.yaml FROM THE REFERENCE DESIGN DOCUMENTATION !!!
	#  Edit the cifs-secret.yaml with the base 64 encoded username from the above command
	#  Edit the cifs-secret.yaml with the base 64 encoded password from the above command
	# Master Node: Create Kubernetes secrets for CIFS username and password on Master and all Worker Nodes
	scp ./cifs-secret.yaml pi@[MASTER NODE IP ADDRESS]:~/cifs-secret.yaml
	# END: FROM YOUR LOCAL DEVELOPMENT PC	
	
	# Master Node: Create Kubernetes secrets for CIFS username and password on Master and all Worker Nodes
	kubectl apply -f cifs-secret.yaml

	# Install CIFS Kubernetes Driver dependencies on Master and all Worker Nodes
	sudo apt-get install jq

	# Install CIFS Kubernetes Driver on Master and all Worker Nodes (also see the YAML code in the cifs-master.zip file in the Reference Design Documentation)
	#  NOTE: This will create a mount point network-pv-volume at /usr/libexec/kubernetes/kubelet-plugins/volume/exec/fstab~cifs
	#        If for some reason the mount gets "stuck" then SSH into Pod, sudo su, and then cd to the directory and umount network-pv-volume
	sudo mkdir -p /usr/libexec/kubernetes/kubelet-plugins/volume/exec/fstab~cifs
	cd /usr/libexec/kubernetes/kubelet-plugins/volume/exec/fstab~cifs
	sudo curl -L -O https://raw.githubusercontent.com/fstab/cifs/master/cifs
	sudo chmod 755 cifs

	# Test
	/usr/libexec/kubernetes/kubelet-plugins/volume/exec/fstab~cifs/cifs init
	```
7. Setup Kubernetes Persistent Volumes by running the following commands:
	```shell
	# NOTE: there will be 2 PV's defined 
	#	1) local storage to SD Card with a max of 20Gb
	#	2) network storage to Samba Share on USB 3 drive with a max of 1.5Tb
	
	# START: FROM YOUR LOCAL DEVELOPMENT PC
	# !!! GET local-pv-volume.yaml FROM THE REFERENCE DESIGN DOCUMENTATION !!!
	# !!! GET local-pv-claims.yaml FROM THE REFERENCE DESIGN DOCUMENTATION !!!
	scp ./local-pv-volume.yaml pi@[MASTER NODE IP ADDRESS]:~/local-pv-volume.yaml
	scp ./local-pv-claims.yaml pi@[MASTER NODE IP ADDRESS]:~/local-pv-claims.yaml
	# END: FROM YOUR LOCAL DEVELOPMENT PC
		
	# Master Node: install Local Persistent Volume Claims
	kubectl apply -f ./local-pv-volume.yaml
	kubectl apply -f ./local-pv-claims.yaml
	
	# Master Node: Test
	kubectl get pv
	kubectl get pvc
	 
	# START: FROM YOUR LOCAL DEVELOPMENT PC
	# !!! GET network-pv-volume.yaml FROM THE REFERENCE DESIGN DOCUMENTATION !!!
	# !!! GET network-pv-claims.yaml FROM THE REFERENCE DESIGN DOCUMENTATION !!!
	scp ./network-pv-volume.yaml pi@[MASTER NODE IP ADDRESS]:~/network-pv-volume.yaml
	scp ./network-pv-claims.yaml pi@[MASTER NODE IP ADDRESS]:~/network-pv-claims.yaml
	# END: FROM YOUR LOCAL DEVELOPMENT PC

	# Master Node: install Network Persistent Volume Claims
	kubectl apply -f ./network-pv-volume.yaml
	kubectl apply -f ./network-pv-claims.yaml
	
	# Master Node: Test
	kubectl get pv
	kubectl get pvc
	```
8. Setup Traefik Proxy by running the following commands:
	```shell
	# START: FROM YOUR LOCAL DEVELOPMENT PC
	# !!! GET traefik-ingress.yaml FROM THE REFERENCE DESIGN DOCUMENTATION !!!
	scp ./traefik-ingress.yaml pi@10.0.0.133:~/traefik-ingress.yaml
	# END: FROM YOUR LOCAL DEVELOPMENT PC

    	# Master Node: Install Traefik Proxy
    	kubectl apply -f traefik-ingress.yaml

	# Master Node: Test (make sure deployment.apps/traefik-ingress-controller and service/traefik-ingress-service are running)
	kubectl get all --all-namespaces	
	```
9. Setup Scheduled Jobs by running the following commands:
	```shell
	# START: FROM YOUR LOCAL DEVELOPMENT PC
	# !!! GET cleanup.sh FROM THE REFERENCE DESIGN DOCUMENTATION !!!
	scp ./cleanup.sh pi@[MASTER NODE IP ADDRESS]:~/cleanup.sh	
	# END: FROM YOUR LOCAL DEVELOPMENT PC
	
	# Make sure script is executable
	chmod 755 ~/cleanup,sh
	
	# Setup the Cronjob to run at midnight
	crontab -e
	00 00 * * * /home/pi/cleanup.sh /media/pi/CLUSTERDATA/shared 0 >> /home/pi/cleanup.log 2>&1
	```

[Back to Top](#introduction)

## Worker Node Tasks
Perform the following on each of the Worker Nodes.
1. Open a Terminal Window on your development and SSH (enter ssh pi@[PI IP ADDRESS]) into the Raspberry Pi.
2. Install Docker by running the following commands:
	```shell
	# Install Docker
	sudo apt-get update
	sudo apt-get upgrade -y
	sudo apt-get install docker.io
	
	# Configure Docker
	sudo usermod -aG docker $USER
	sudo systemctl start docker
	sudo systemctl enable docker

	# Reboot
	sudo reboot
	
	# Testing
	docker version
	```
3. Install Kubernetes by running the following commands:
	```shell
	# Disable Swap
	echo Adding " Disabling Swap File
	sudo dphys-swapfile swapoff && \
	sudo dphys-swapfile uninstall && \
	sudo update-rc.d dphys-swapfile remove
	
	# Edit the following file and set CONF_SWAPSIZE=0 
	sudo nano /etc/dphys-swapfile

	# Setup CGroups
	echo Adding " cgroup_enable=cpuset cgroup_enable=memory" to /boot/cmdline.txt
	sudo cp /boot/cmdline.txt /boot/cmdline_backup.txt
	orig="$(head -n1 /boot/cmdline.txt) cgroup_enable=cpuset cgroup_memory=1 cgroup_enable=memory"
	echo $orig | sudo tee /boot/cmdline.txt
	
	# Reboot
	sudo reboot

	# Install Kubernetes
	# Install Kubernetes (if curl is not installed run the following command: sudo apt-get install curl)
	sudo apt-get install software-properties-common
	curl -s https://packages.cloud.google.com/apt/doc/apt-key.gpg | sudo apt-key add -
	echo "deb http://packages.cloud.google.com/apt/ kubernetes-xenial main" | sudo tee /etc/apt/sources.list.d/kubernetes.list 
	sudo apt-get update -q
	sudo apt-get install kubeadm kubelet kubectl -y

	# Reboot
	sudo reboot
	```
4. Setup Kubernetes by running the following commands:
	```shell
	# Worker Testing (ignore 'The connection to the server localhost:8080 was refused' issue connecting to the Master Node to get its version)
	kubectl version

	# For each Worker Node create a Token and copy it as a bash script to the from the Master Node to the Worker Node
	# START FROM THE MASTER NODE
	# Create a Join Token and save in a file then scp over to Worker Node (get Node 1 IP address by running ip add)
	kubeadm token create --print-join-command --ttl=0 > token.sh
	scp ./token.sh pi@[WORKER NODE IP ADDRESS]:~/token.sh
	# END FROM THE MASTER NODE

	# START FROM THE WORKER NODE
	# Setup Worker Node to join the Master
	nano token.sh
	chmod 777 token.sh
	sudo ./token.sh
	# END FROM THE WORKER NODE
	
	# Master Node Testing to make sure all Worker Nodes are working
	kubectl get all --all-namespaces
	```
5. Setup Samba by running the following commands:
	```shell
	# Install Samba
	sudo apt update
	sudo apt install samba
	
	# Reboot
	sudo reboot

	# Test
	whereis samba
	```
7. Setup Kubernetes Persistent Volumes by running the following commands:
	```shell
	# NOTE: there will be 2 PV's defined 
	#	1) local storage to SD Card with a max of 20Gb
	#	2) network storage to Samba Share on USB 3 drive with a max of 1.5Tb
	
	# Worker Node: create /cloudapps/local directory on all Worker Nodes and make this accessible to everybody to read and write
	sudo mkdir /cloudapps
	sudo chmod 777 /cloudapps
	sudo mkdir /cloudapps/local
	sudo chmod 777 /cloudapps/local
	```
8. Setup Scheduled Jobs by running the following commands:
	```shell
	# START: FROM YOUR LOCAL DEVELOPMENT PC
	# !!! GET cleanup.sh FROM THE REFERENCE DESIGN DOCUMENTATION !!!
	scp ./cleanup.sh pi@[MASTER NODE IP ADDRESS]:~/cleanup.sh	
	# END: FROM YOUR LOCAL DEVELOPMENT PC
	
	# Make sure script is executable
	chmod 755 ~/cleanup,sh
	
	# Setup the Cronjob to run at midnight
	crontab -e
	00 00 * * * /home/pi/cleanup.sh /cloudapps/local 0 >> /home/pi/cleanup.log 2>&1
	```

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

## Lens Application Tasks
  1. Download and install [Lens](https://k8slens.dev) for your operating system.
  2. Setup Lens using the Master Node Kubernetes config file (located in $HOME/.kube/config).
  3. Under the Lens settings install Prometheous by clicking Install under the Metrics Feature. NOTE: There is a bug in Lens, the kube-state-metrics deployment is setup to use a non Arm version of the Docker image. Perform the following steps in Lens to fix this problem:
	- In Lens go to Workloads->Deployments. Go the kube-state-metrics deployment.
	- Make the following edits to the Container Spec for kube-state-metrics:
		- Update the image from quay.io/coreos/kube-state-metrics:v1.9.7 to k8s.gcr.io/kube-state-metrics/kube-state-metrics:v2.0.0
		- Update both kubernetes.io/arch key settings value from amd64 to arm
	- Click the Save & Close button.
	- Restart the Lens deployment.

[Back to Top](#introduction)

<h5 align="center">Copyright @2021 On The Edge Software Consulting LLC</h5>


