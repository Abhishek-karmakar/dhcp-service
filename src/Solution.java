import java.util.Scanner;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class Solution
{
    public static void main(String[] args)
    {
        // load all configurations according to the requirement.
        Scanner scanner = new Scanner(System.in);

        String macAddress;

        Config config = new Config();
        String startIpAddress = config.getProperty("startIpAddress");
        String endIpAddress  = config.getProperty("endIpAddress");
        long timeout = Long.parseLong(config.getProperty("timeout"));

        System.out.println("Please enter MacAddress of the new device.");
        macAddress = scanner.next();
        //config.getProperty();

        // start the service.
        Dhcpsolution objDhcp = new Dhcpsolution(startIpAddress, endIpAddress, timeout);

        //Allocate the IP address to the macId
        String ipaddress = objDhcp.allocate(macAddress);

        // get the ip address of the mac address.
        System.out.println("The IP acclocated to MAC" + macAddress + " is :" + ipaddress);

        // start the service which will keep running and will delete all the expired nodes every 10 minutes.

        //https://stackoverflow.com/questions/24939840/how-to-keep-running-a-thread-after-specific-time
        ScheduledExecutorService executorService = Executors.newScheduledThreadPool(1);
            executorService.scheduleAtFixedRate(new Runnable() {
            public void run()
            {
                System.out.println("Sending Heartbeat...");
                objDhcp.deleteExpiredService();

            }
        }, 0, 10, TimeUnit.MINUTES);

    }

}
