package tcpWork;

public class PayMoneyOperation extends CardOperation {
    private String serNum;
    private double money;

    public PayMoneyOperation(String serNum, double amount) {
        this.serNum = serNum;
        this.money = money;
    }
    public PayMoneyOperation() {
        this("null", 0.0);
    }
    public String getSerNum() {
        return serNum;
    }
    public void setSerNum(String serNum) {
        this.serNum = serNum;
    }
    public double getMoney() {
        return money;
    }
    public void setMoney(double amount) {
        this.money = money;
    }
}
