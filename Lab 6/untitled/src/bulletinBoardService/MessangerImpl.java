package bulletinBoardService;

import javax.swing.*;
import java.io.IOException;
import java.net.*;

public class MessangerImpl implements Messanger {
    private final UITasks ui;
    private final MulticastSocket group;
    private final InetAddress addr;
    private final int port;
    private final String name;
    private boolean canceled = false;

    public MessangerImpl(InetAddress addr, int port, String name, UITasks ui) {
        this.name = name;
        this.ui = ui;
        this.addr = addr;
        this.port = port;
        try {
            group = new MulticastSocket(port);
            group.setTimeToLive(2);
            group.joinGroup(addr);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    @Override
    public void start() {
        new Receiver().start();
    }
    @Override
    public void stop() {
        cancel();
        try {
            group.leaveGroup(addr);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Disconnect error\n" + e.getMessage());
        } finally {
            group.close();
        }
    }
    @Override
    public void send() {
        new Sender().start();
    }
    private class Sender extends Thread {
        @Override
        public void run() {
            try {
                String msg = name + ": " + ui.getMessage();
                byte[] out = msg.getBytes();
                DatagramPacket pkt = new DatagramPacket(out, out.length, addr, port);
                group.send(pkt);
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, "Sending error\n" + e.getMessage());
            }
        }
    }
    private class Receiver extends Thread {
        @Override
        public void run() {
            try {
                byte[] in = new byte[512];
                DatagramPacket pkt = new DatagramPacket(in, in.length);
                while (!isCanceled()) {
                    group.receive(pkt);
                    ui.setText(new String(pkt.getData(), 0, pkt.getLength()));
                }
            } catch (Exception e) {
                if (isCanceled()) {
                    JOptionPane.showMessageDialog(null, "The connection is complete");
                } else {
                    JOptionPane.showMessageDialog(null, "Reception error\n" + e.getMessage());
                }
            }
        }
    }
    private synchronized boolean isCanceled() {
        return canceled;
    }
    public synchronized void cancel() {
        canceled = true;
    }
}
