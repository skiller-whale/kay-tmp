import java.util.List;

public interface Dao {
    DaoResult getUsers();
    DaoResult getUsers(int page, int limit);
}

record DaoResult(List<User> users, int total) {}
