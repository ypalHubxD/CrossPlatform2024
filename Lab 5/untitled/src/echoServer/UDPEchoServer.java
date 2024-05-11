package echoServer;

import java.io.*;
import java.net.*;

public class UDPEchoServer extends UDPServer {
    @Override
    public void respond(DatagramSocket socket, DatagramPacket request)
            throws IOException {
        DatagramPacket reply = new DatagramPacket(request.getData(),
                request.getLength(), request.getAddress(), request.getPort());
        socket.send(reply);
    }
    public static void main(String[] args) {
        UDPServer server = new UDPEchoServer();
        Thread t = new Thread(server);
        t.start();
    }
    public final static int DEFAULT_PORT = 7;
    public UDPEchoServer() {
        super(DEFAULT_PORT);
    }
}
