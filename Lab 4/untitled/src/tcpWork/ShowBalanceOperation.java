package tcpWork;

public class ShowBalanceOperation extends CardOperation {
    private String serNum;
    public ShowBalanceOperation(String serNum) {
        this.serNum = serNum;
    }
    public String getSerNum() {
        return serNum;
    }
    public void setSerNum(String serNum) {
        this.serNum = serNum;
    }
}
