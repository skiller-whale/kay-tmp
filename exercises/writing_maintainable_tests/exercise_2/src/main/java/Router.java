import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * This is the router being tested, it expects a DAO object to be passed in
 * to its constructor.
 *
 * The DAO is responsible for fetching data from the database, and the router is
 * responsible for formatting that data into a response.
 */
public class Router {
    private final Dao dao;
    private static final int PAGINATION_SIZE = 10;

    public Router(Dao dao) {
        this.dao = dao;
    }

    public Response get(String path) {
        if ("/users".equals(path)) {
            DaoResult result = dao.getUsers();
            List<User> users = result.users();
            int total = result.total();

            Integer nextPage = total > PAGINATION_SIZE ? 2 : null;
            Integer previousPage = null;

            UsersResponseData responseData = new UsersResponseData(users, total, nextPage, previousPage);
            return new Response(200, responseData);
        }

        Map<String, String> error = new HashMap<>();
        error.put("error", "Not found");
        return new Response(404, error);
    }
}

record UsersResponseData(List<User> data, int total, Integer nextPage, Integer previousPage) {}
