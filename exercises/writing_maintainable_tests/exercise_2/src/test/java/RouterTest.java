import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class UserRouterTest {

    @Test
    public void testGetZeroUsersOk() {
        // empty array of users
        List<User> testUsers = Collections.emptyList();
        MockDao mockDao = new MockDao();
        mockDao.setUsers(testUsers);
        Router router = new Router(mockDao);

        Response response = router.get("/users");

        assertEquals(200, response.statusCode()); // OK
        assertEquals("application/json", response.getHeader("Content-Type"));

        UsersResponseData payload = response.getJson(UsersResponseData.class);
        assertEquals(testUsers, payload.data());
        assertEquals(0, payload.total());
        assertNull(payload.nextPage());
        assertNull(payload.previousPage());
    }

    @Test
    public void testGetTenUsersOk() {
        // array of 10 users
        List<User> testUsers = getUsersForTests().subList(0, 10);
        MockDao mockDao = new MockDao();
        mockDao.setUsers(testUsers);
        Router router = new Router(mockDao);

        Response response = router.get("/users");

        assertEquals(200, response.statusCode()); // OK
        assertEquals("application/json", response.getHeader("Content-Type"));

        UsersResponseData payload = response.getJson(UsersResponseData.class);
        assertEquals(testUsers, payload.data());
        assertEquals(10, payload.total());
        assertNull(payload.nextPage());
        assertNull(payload.previousPage());
    }

    @Test
    public void testGetTwentyUsersOk() {
        // array of 20 users
        List<User> testUsers = getUsersForTests().subList(0, 20);
        MockDao mockDao = new MockDao();
        mockDao.setUsers(testUsers);
        Router router = new Router(mockDao);

        Response response = router.get("/users");

        assertEquals(200, response.statusCode()); // OK
        assertEquals("application/json", response.getHeader("Content-Type"));

        UsersResponseData payload = response.getJson(UsersResponseData.class);
        assertEquals(testUsers.subList(0, 10), payload.data());
        assertEquals(20, payload.total());
        assertEquals(2, payload.nextPage());
        assertNull(payload.previousPage());
    }

    // Helper method for creating test users
    private static List<User> getUsersForTests() {
        return new ArrayList<>(List.of(
                new User(1, "User 1", "user1@example.com"),
                new User(2, "User 2", "user2@example.com"),
                new User(3, "User 3", "user3@example.com"),
                new User(4, "User 4", "user4@example.com"),
                new User(5, "User 5", "user5@example.com"),
                new User(6, "User 6", "user6@example.com"),
                new User(7, "User 7", "user7@example.com"),
                new User(8, "User 8", "user8@example.com"),
                new User(9, "User 9", "user9@example.com"),
                new User(10, "User 10", "user10@example.com"),
                new User(11, "User 11", "user11@example.com"),
                new User(12, "User 12", "user12@example.com"),
                new User(13, "User 13", "user13@example.com"),
                new User(14, "User 14", "user14@example.com"),
                new User(15, "User 15", "user15@example.com"),
                new User(16, "User 16", "user16@example.com"),
                new User(17, "User 17", "user17@example.com"),
                new User(18, "User 18", "user18@example.com"),
                new User(19, "User 19", "user19@example.com"),
                new User(20, "User 20", "user20@example.com")
        ));
    }
}
