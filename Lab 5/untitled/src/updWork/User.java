package updWork;

import java.io.Serializable;
import java.net.InetAddress;

public class User implements Serializable {
    private InetAddress address;
    private int port;
    public User(InetAddress address, int port) {
        this.address = address;
        this.port = port;
    }
    public InetAddress getAddress() {
        return address;
    }
    public int getPort() {
        return port;
    }
    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (!(obj instanceof User)) return false;
        User other = (User) obj;
        return this.address.equals(other.address) && this.port == other.port;
    }
    @Override
    public String toString() {
        return "User: " + address.getHostAddress() + ":" + port;
    }
}
