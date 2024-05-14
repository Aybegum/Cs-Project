import java.sql.Statement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Community {

    private int id;
    private String name;
    private static int currentCommunityId = -1;
    private static Community currentCommunity = getCommunityById(getCurrentCommunityId());

    public Community(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public static Community getCurrentCommunity() {
        return currentCommunity;
    }

    public static void setCurrentCommunity(Community currentCommunity) {
        Community.currentCommunity = currentCommunity;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public static int getCurrentCommunityId() {
        return currentCommunityId;
    }

    public static void setCurrentCommunityId(int currentCommunityId) {
        Community.currentCommunityId = currentCommunityId;
    }

    public static Community getCommunityById(int id) {
        try {
            Connection connection = Main.connect();
            Statement statement = connection.createStatement();
            ResultSet rs = statement.executeQuery("select * from community where id = '" + id + "'");
            if (rs.next()) {
                String name = rs.getString("name");
                statement.close();
                connection.close();
                return new Community(id, name);
            } else {
                statement.close();
                connection.close();
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    }
}
