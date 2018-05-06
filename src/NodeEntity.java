import java.util.Date;

public class NodeEntity
{
    public String getIpAdress() {
        return IpAdress;
    }

    public void setIpAdress(String ipAdress) {
        IpAdress = ipAdress;
    }

    public String getMacAddress() {
        return macAddress;
    }

    public void setMacAddress(String macAddress) {
        this.macAddress = macAddress;
    }

    public long getTimeoutinSeconds() {
        return timeoutinSeconds;
    }

    public void setTimeoutinSeconds(long timeoutinSeconds) {
        this.timeoutinSeconds = timeoutinSeconds;
    }


    public int getNodeid() {
        return nodeid;
    }

    public void setNodeid(int nodeid) {
        this.nodeid = nodeid;
    }
    public Date gettimeofcreation() {
        return timeofcreation;
    }

    public void settimeofcreation(Date currentTime) {
        this.timeofcreation = timeofcreation;
    }

    String IpAdress;
    String macAddress;
    long timeoutinSeconds;
    int nodeid;
    Date timeofcreation;
    boolean isValid;

    private boolean isnodevalid()
    {


        // check if object needs to refresh after expirytime.
        return false;
    }

}
