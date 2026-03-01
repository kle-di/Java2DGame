package Main;

public class Session {
    private static boolean isLoggedIn = false;
    public static String user_name;

    public static boolean isLoggedIn() {
        return isLoggedIn;
    }

    public static void setLoggedIn(boolean loggedIn) {
        isLoggedIn = loggedIn;
    }
    public static void setUsername(String username) {
        user_name = username;
    }
}
