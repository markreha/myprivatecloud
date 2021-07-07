# Reboot all the Work Nodes
for ip in 10.0.0.169 10.0.0.7 10.0.0.56
do
	echo 'Rebooting Worker Node at' $ip
    ssh pi@$ip 'sudo reboot'
done

# Reboot the Master Node
echo 'Rebooting Master Node at 10.0.0.133'
ssh pi@10.0.0.133 'sudo reboot'