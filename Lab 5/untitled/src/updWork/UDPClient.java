package updWork;

import java.io.*;
import java.net.*;

public class UDPClient {
    private ActiveUsers userList = null;
    private DatagramSocket socket = null;
    private DatagramPacket packet = null;
    private int serverPort = -1;
    private InetAddress serverAddress = null;
    public UDPClient(String address, int port) {
        userList = new ActiveUsers();
        serverPort = port;
        try {
            serverAddress = InetAddress.getByName(address);
            socket = new DatagramSocket();
            socket.setSoTimeout(10000);
        } catch (UnknownHostException e) {
            System.out.println("Error: " + e);
        } catch (SocketException e) {
            System.out.println("Error: " + e);
        }
    }
    public void work(int bufferSize) throws ClassNotFoundException {
        byte[] buffer = new byte[bufferSize];
        try {
            BufferedReader userInput = new BufferedReader(new InputStreamReader(System.in));
            System.out.print("Enter a number to calculate its factorial: ");
            int number = Integer.parseInt(userInput.readLine());
            ByteArrayOutputStream bout = new ByteArrayOutputStream();
            DataOutputStream dout = new DataOutputStream(bout);
            dout.writeInt(number);
            byte[] requestBuffer = bout.toByteArray();
            DatagramPacket requestPacket = new DatagramPacket(requestBuffer, requestBuffer.length, serverAddress, serverPort);
            socket.send(requestPacket);
            System.out.println("Sending request");
            while (true) {
                DatagramPacket responsePacket = new DatagramPacket(buffer, buffer.length);
                socket.receive(responsePacket);
                if (responsePacket.getLength() == 0) break;
                ByteArrayInputStream bin = new ByteArrayInputStream(responsePacket.getData());
                DataInputStream din = new DataInputStream(bin);
                int factorial = din.readInt();
                System.out.println("Factorial of " + number + " is: " + factorial);
                clear(buffer);
            }
        } catch(SocketTimeoutException e) {
            System.out.println("Server is unreachable: " + e);
        } catch (IOException e) {
            System.out.println("Error: " + e);
        } finally {
            socket.close();
        }
        System.out.println("Registered users: " + userList.size());
        System.out.println(userList);
    }
    private void clear(byte[] arr) {
        for (int i = 0; i < arr.length; i++) {
            arr[i] = 0;
        }
    }
    public static void main(String[] args) throws ClassNotFoundException {
        (new UDPClient("127.0.0.1", 1501)).work(256);
    }
}
