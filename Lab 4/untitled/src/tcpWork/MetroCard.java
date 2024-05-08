package tcpWork;

import java.io.Serializable;
public class MetroCard implements Serializable {
    private String serNum;
    private User usr;
    private String colledge;
    private double balance;
    public MetroCard(String serNum, User usr, String colledge, double balance) {
        this.serNum = serNum;
        this.usr = usr;
        this.colledge = colledge;
        this.balance = balance;
    }
    public void setSerNum(String serNum) {
        this.serNum = serNum;
    }

    public void setUsr(User usr) {
        this.usr = usr;
    }

    public void setColledge(String colledge) {
        this.colledge = colledge;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public String getSerNum() {
        return serNum;
    }

    public User getUsr() {
        return usr;
    }

    public String getColledge() {
        return colledge;
    }

    public double getBalance() {
        return balance;
    }
    public MetroCard() {
    }
    @Override
    public String toString() {
        return "No: " + serNum + "\nUser: " + usr + "\nColledge: " + colledge + "\nBalance: " + balance;
    }
}
