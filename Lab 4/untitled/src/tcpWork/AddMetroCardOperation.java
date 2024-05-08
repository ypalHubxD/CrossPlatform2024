package tcpWork;

public class AddMetroCardOperation extends CardOperation {
    private MetroCard crd = null;

    public AddMetroCardOperation(String serNum, User usr, String colledge, double balance) {
        this.crd = new MetroCard(serNum, usr, colledge, balance);
    }
    public MetroCard getCrd() {
        return crd;
    }
}
