package tcpWork;

public class Main {
    public static void main(String[] args) {
        MetroServer srv = new MetroServer(7891);
        srv.start();
    }
}
