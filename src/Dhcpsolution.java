import java.util.*;
import java.util.regex.Pattern;

public class Dhcpsolution implements AllocationService, HeartbeatService
/*
@Author : a.Karmakar
The DHCP solution class implements the Allocationservice & Heartbeatservice and overrides the methods
Allocate and Refresh.

Another method
 */

{
    Pattern PATTERN;
    String startIp;
    String endIp;
    long timeout;
    List<String> ipAddresses;
    List<NodeEntity> nodeEntities;

    public Dhcpsolution(String startIp, String endIp, long timeout) {

        PATTERN = Pattern.compile("^(([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\.){3}([01]?\\d\\d?|2[0-4]\\d|25[0-5])$");
        this.startIp = startIp;
        this.endIp = endIp;
        this.timeout = timeout;
        // a list of IP addressed should be generated and kept with itself when the application starts.
        ipAddresses = generateIpAddress(startIp, endIp);

        // keep a list of all the nodes which are currently active.
        nodeEntities = new ArrayList<NodeEntity>();

    }


    @Override
    public String allocate(String macAddress) {
        String result = "";
        // check if macAddress is correct or not.
        if (macisValid(macAddress)) {
            if (!macisPresent(macAddress)) // If mac address is not assigned to an IP Address then assign mac address to an IP Address
            {
                //create a new entity and add it to the nodearray
                NodeEntity nodeEntity = new NodeEntity();

                //check from the list of all the IP address is there any ip-address assigned to any node.
                for (int i = 0; i < ipAddresses.size(); i++) //O(1) time based on given number of ipaddress.
                {
                    // there is no node in the list. Lets add the first element.
                    if(nodeEntities.size() == 0)
                    {
                        nodeEntity.setNodeid(i);
                        nodeEntity.setIpAdress(ipAddresses.get(i));
                        nodeEntity.setMacAddress(macAddress);
                        nodeEntity.setTimeoutinSeconds(timeout);

                        result = ipAddresses.get(i);
                        return result;

                    }

                    for (NodeEntity nodeobj : nodeEntities) // will take O(n) time //TODO:check how to reduce this time.
                    {
                        if (!nodeobj.getIpAdress().contains(ipAddresses.get(i))) {
                            nodeEntity.setNodeid(i);
                            nodeEntity.setIpAdress(ipAddresses.get(i));
                            nodeEntity.setMacAddress(macAddress);
                            nodeEntity.setTimeoutinSeconds(timeout);

                            result = ipAddresses.get(i);
                            return result;
                        }
                    }
                }
            } else {
                result = "IP address already assigned to this mac address.";
            }
        } else {
            result = "Invalid macAddress";
        }
        // each mac address should have a unique IP address.
        // If mac address

        return result;
    }

    @Override
    public Boolean refresh(String macAddress, String allocatedIPAddr)
    {
        Date currentTime = new Date();

        // If a IP Address and Its macaddress hit the Refresh meaning it can have only two states.
        // loop through all the node objects in the NodeEntityArray to find the IP.
        for(NodeEntity nodeEntity : nodeEntities)
        {
            // if IP is a match then check if the mac address is also a match
            if(allocatedIPAddr.equals(nodeEntity.getIpAdress().toString()))
            {
                if(macAddress.equals(nodeEntity.getMacAddress().toString()))
                {
                    // if ip and mac address match then increase the valid time of the node.
                    int id = nodeEntity.getNodeid();
                    //get that nodeid then set the timeofcreation to current time.
                    nodeEntities.get(id).settimeofcreation(currentTime);
                    return true;
                }
                return false;
            }
            else
            {

                System.out.println("The IP-Address is not found in the list, call the allocate service");
                return false;
            }
        }
        return false;
    }



    public void deleteExpiredService()
    {
        Timer dhcptimer;
        dhcptimer = new Timer();
        dhcptimer.schedule(new serviceExpired(), 0, timeout * 1000);

    }

// Check for element's expired time. If element is > 5 seconds old then remove it


    private boolean macisValid(String macAddress)
    {
        // TODO : check if the mac address is valid or not
        return true;

    }

    private boolean macisPresent(String macAddress)
    {
        // if we find the mac address present in the node entity then return true.
        for(NodeEntity nodeobj : nodeEntities)
        {
            if(nodeobj.getMacAddress().contentEquals(macAddress))
            {
                return true;
            }
        }

        return false;
    }

    // method to validate IP.
    public boolean validate(final String ip) {
        return PATTERN.matcher(ip).matches();
    }


    // https://stackoverflow.com/questions/7303720/how-do-i-get-generate-an-ip-address-range-given-start-and-end-ip-address
    private List<String> generateIpAddress(String startIp, String endIp) {
        List<String> arrayList = new ArrayList<String>();

        String[] startParts = startIp.split("(?<=\\.)(?!.*\\.)");
        String[] endParts = endIp.split("(?<=\\.)(?!.*\\.)");

        int first = Integer.parseInt(startParts[1]);
        int last = Integer.parseInt(endParts[1]);

        for (int i = first; i <= last; i++) {
            arrayList.add(startParts[0] + i);
            // System.out.println(startParts[0] + i);
        }

        return arrayList;

    }

    class serviceExpired extends TimerTask
    {
        public void run()
        {
            // We are checking for expired element from map every second
            ClearExipredElements(nodeEntities);
        }

        private void ClearExipredElements(List<NodeEntity> nodeEntities)
        {
            Date currentTime = new Date();
            Date actualExpiredTime = new Date();

            // if element time stamp and current time stamp difference is 5 second then delete element
            actualExpiredTime.setTime(currentTime.getTime() - timeout * 1000l);

            System.out.println("Number of nodes:" + nodeEntities.size());

            for (NodeEntity nodeEntity : nodeEntities)
            {
                if(actualExpiredTime.compareTo(nodeEntity.gettimeofcreation()) < 0)
                    nodeEntities.remove(nodeEntity);
            }
        }
    }
}
