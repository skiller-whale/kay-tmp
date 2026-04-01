import java.util.ArrayList;
import java.util.List;

public class MockDao implements Dao {
    private List<User> users = new ArrayList<>();

    public void setUsers(List<User> users) {
        this.users = users;
    }

    @Override
    public DaoResult getUsers() {
        return getUsers(1, 10);
    }

    @Override
    public DaoResult getUsers(int page, int limit) {
        int start = (page - 1) * limit;
        int end = Math.min(start + limit, users.size());

        List<User> pageUsers = start < users.size() ?
                users.subList(start, end) :
                new ArrayList<>();

        return new DaoResult(pageUsers, users.size());
    }
}
