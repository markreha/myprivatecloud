# Shutdown all the Work Nodes
for ip in 10.0.0.169 10.0.0.7 10.0.0.56
do
	echo 'Shutting down Worker Node at' $ip
    ssh pi@$ip 'sudo shutdown now'
done

# Shutdown the Master Node
echo 'Shutting down Master Node at 10.0.0.133'
ssh pi@10.0.0.133 'sudo shutdown now'