public class Community {

    private int id;
    private String name;
    private static int currentCommunityId = 0; //To be changed

    public Community(int id, String name) {
        this.id = id++;
        this.name = name;
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
}
