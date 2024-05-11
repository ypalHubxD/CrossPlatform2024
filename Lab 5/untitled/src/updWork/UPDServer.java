package updWork;

import java.io.*;
import java.net.*;
import java.util.Arrays;

public class UPDServer {
    private ActiveUsers userList = null;
    private DatagramSocket socket = null;
    private DatagramPacket packet = null;
    private InetAddress address = null;
    private int port = -1;
    private int number;
    public UPDServer(int serverPort) {
        try {
            socket = new DatagramSocket(serverPort);
        } catch(SocketException e) {
            System.out.println("Error: " + e);
        }
        userList = new ActiveUsers();
    }
    public void work(int bufferSize) {
        try {
            System.out.println("Server start...");
            while (true) {
                getUserData(bufferSize);
                log(address, port);
            }
        } catch(IOException e) {
            System.out.println("Error: " + e);
        } finally {
            System.out.println("Server end...");
            socket.close();
        }
    }
    private void log(InetAddress address, int port) {
        System.out.println("Request from: " + address.getHostAddress() + " port: " + port);
    }
    private void clear(byte[] arr) {
        Arrays.fill(arr, (byte) 0);
    }
    private void getUserData(int bufferSize) throws IOException {
        byte[] buffer = new byte[bufferSize];
        packet = new DatagramPacket(buffer, buffer.length);
        socket.receive(packet);
        address = packet.getAddress();
        port = packet.getPort();
        DataInputStream din = new DataInputStream(new ByteArrayInputStream(packet.getData()));
        int number = din.readInt();
        sendUserData(number);
        User usr = new User(address, port);
        if (userList.isEmpty()) {
            userList.add(usr);
        } else if (!userList.contains(usr)) {
            userList.add(usr);
        }
        clear(buffer);
    }
    private void sendUserData(int number) throws IOException {
        int factorial = computeFactorial(number);
        ByteArrayOutputStream bout = new ByteArrayOutputStream();
        DataOutputStream dout = new DataOutputStream(bout);
        dout.writeInt(factorial);
        byte[] resultBuffer = bout.toByteArray();
        DatagramPacket resultPacket = new DatagramPacket(resultBuffer, resultBuffer.length, address, port);
        socket.send(resultPacket);
    }
    private int computeFactorial(int number) {
        int factorial = 1;
        for (int i = 2; i <= number; i++) {
            factorial *= i;
        }
        return factorial;
    }
    public static void main(String[] args) {
        (new UPDServer(1501)).work(256);
    }
}
