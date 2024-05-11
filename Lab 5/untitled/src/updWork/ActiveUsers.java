package updWork;

import java.util.ArrayList;
import java.util.List;

public class ActiveUsers {
    private List<User> userList;
    public ActiveUsers() {
        this.userList = new ArrayList<>();
    }
    public void add(User user) {
        userList.add(user);
    }
    public int size() {
        return userList.size();
    }
    public User get(int i) {
        return userList.get(i);
    }
    public boolean contains(User usr) {
        return userList.contains(usr);
    }
    public boolean isEmpty() {
        return userList.isEmpty();
    }
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Active Users:\n");
        for (User user : userList) {
            sb.append(user.toString()).append("\n");
        }
        return sb.toString();
    }
}
