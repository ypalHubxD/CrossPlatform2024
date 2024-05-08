package tcpWork;

import java.io.*;
import java.net.*;
import java.util.ArrayList;

public class ClientHandler extends Thread {
    private ObjectInputStream is = null;
    private ObjectOutputStream os = null;
    private boolean work = true;
    private MetroCardBank bnk = null;
    private Socket s = null;

    public ClientHandler(MetroCardBank bnk, Socket s) {
        this.bnk = bnk;
        this.s = s;
        this.work = true;
        try {
            this.is = new ObjectInputStream(s.getInputStream());
            this.os = new ObjectOutputStream(s.getOutputStream());
        } catch (IOException e) {
            System.err.println("Error initializing streams: " + e);
        }
    }
    @Override
    public void run() {
        synchronized (bnk) {
            System.out.println("Client Handler Started for: " + s);
            try {
                while (work) {
                    Object obj = is.readObject();
                    processOperation(obj);
                }
            } catch (IOException | ClassNotFoundException e) {
                System.err.println("Error during communication: " + e);
            } finally {
                closeResources();
            }
        }
    }
    private void closeResources() {
        try {
            if (is != null)
                is.close();
            if (os != null)
                os.close();
            if (s != null)
                s.close();
            System.out.println("Client Handler Stopped for: " + s);
        } catch (IOException ex) {
            System.err.println("Error closing resources: " + ex);
        }
    }
    private void processOperation(Object obj) throws IOException, ClassNotFoundException {
        if (obj instanceof StopOperation) {
            finish();
        } else if (obj instanceof AddMetroCardOperation) {
            addCard(obj);
        } else if (obj instanceof AddMoneyOperation) {
            addMoney(obj);
        } else if (obj instanceof PayMoneyOperation) {
            payMoney(obj);
        } else if (obj instanceof RemoveCardOperation) {
            removeCard(obj);
        } else if (obj instanceof ShowBalanceOperation) {
            showBalance(obj);
        } else if (obj instanceof GetAllCardsOperation) {
            sendAllCards();
        } else if (obj instanceof GetAllUsersOperation) {
            sendAllUsers();
        } else {
            error();
        }
    }
    private void sendAllUsers() throws IOException {
        os.writeObject(bnk.getUsers());
        os.flush();
    }
    private void sendAllCards() throws IOException {
        ArrayList<MetroCard> allCards = bnk.getStore();
        os.writeObject(bnk.getStore());
        os.flush();
    }
    private void finish() throws IOException {
        work = false;
        os.writeObject("Finish Work " + s);
        os.flush();
    }
    private void addCard(Object obj) throws IOException, ClassNotFoundException {
        bnk.addCard(((AddMetroCardOperation) obj).getCrd());
        os.writeObject("Card Added");
        os.flush();
    }
    private void addMoney(Object obj) throws IOException, ClassNotFoundException {
        AddMoneyOperation op = (AddMoneyOperation) obj;
        boolean res = bnk.addMoney(op.getSerNum(), op.getMoney());
        os.writeObject(res ? "Balance Added" : "Cannot Balance Added");
        os.flush();
    }
    private void payMoney(Object obj) throws IOException, ClassNotFoundException {
        PayMoneyOperation op = (PayMoneyOperation) obj;
        boolean res = bnk.getMoney(op.getSerNum(), op.getMoney());
        os.writeObject(res ? "Money Paid" : "Cannot Pay Money");
        os.flush();
    }
    private void removeCard(Object obj) throws IOException, ClassNotFoundException {
        RemoveCardOperation op = (RemoveCardOperation) obj;
        boolean res = bnk.removeCard(op.getSerNum());
        os.writeObject(res ? "Metro Card Successfully Removed: " + op.getSerNum() : "Cannot Remove Card" + op.getSerNum());
        os.flush();
    }
    private void showBalance(Object obj) throws IOException, ClassNotFoundException {
        ShowBalanceOperation op = (ShowBalanceOperation) obj;
        int ind = bnk.findMetroCard(op.getSerNum());
        if (ind >= 0) {
            os.writeObject("Card : " + op.getSerNum() + " balance: " + bnk.getStore().get(ind).getBalance());
            os.flush();
        } else {
            os.writeObject("Cannot Show Balance for Card: " + op.getSerNum());
        }
    }
    private void error() throws IOException {
        os.writeObject("Bad Operation");
        os.flush();
    }
}
