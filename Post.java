import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class Post {

	private int id;
	private String text;
	private String imageUrl;
	private String title;
	private int songId;
	private int playlistId;
	private int userId;
	private int communityId;

	public Post(int id, String title, String text, String imageUrl, int songId, int playlistId, int userId,
			int communityId) {
		this.id = id;
		this.text = text;
		this.imageUrl = imageUrl;
		this.songId = songId;
		this.playlistId = playlistId;
		this.userId = userId;
		this.communityId = communityId;
		this.title = title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getTitle() {
		return title;
	}

	public int getCommunityId() {
		return communityId;
	}

	public void setCommunityId(int communityId) {
		this.communityId = communityId;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getImageUrl() {
		return imageUrl;
	}

	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}

	public int getPlaylistId() {
		return playlistId;
	}

	public void setPlaylistId(int playlistId) {
		this.playlistId = playlistId;
	}

	public int getSongId() {
		return songId;
	}

	public void setSongId(int songId) {
		this.songId = songId;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public static Post createTextPost(String text, int userId, int communityId, String title) throws SQLException {

		Connection connection = Main.connect();
		Statement idStatement = connection.createStatement();
		ResultSet r = idStatement.executeQuery("select postid from posts order by postid desc");
		int id = 1;
		if (r.next()) {
			id = r.getInt(1) + 1;
		}

		String query = "insert into posts (postid, text, userId, communityId, title) values (?, ?, ?, ?, ?)";
		PreparedStatement statement = connection.prepareStatement(query);
		statement.setInt(1, id);
		statement.setString(2, text);
		statement.setInt(3, userId);
		statement.setInt(4, communityId);
		statement.setString(5, title);
		statement.executeUpdate();

		statement.close();
		connection.close();

		return new Post(id, title, text, null, -1, -1, userId, communityId);

	}

	public static Post createImagePost(String imageUrl, int userId, int communityId, String title) throws SQLException {

		Connection connection = Main.connect();
		Statement idStatement = connection.createStatement();
		ResultSet r = idStatement.executeQuery("select postid from posts order by postid desc");
		int id = 1;
		if (r.next()) {
			id = r.getInt(1) + 1;
		}

		String query = "insert into posts (postid, imageUrl, userId, communityId, title) values (?, ?, ?, ?, ?)";
		PreparedStatement statement = connection.prepareStatement(query);
		statement.setInt(1, id);
		statement.setString(2, imageUrl);
		statement.setInt(3, userId);
		statement.setInt(4, communityId);
		statement.setString(5, title);
		statement.executeUpdate();

		statement.close();
		connection.close();

		return new Post(id, title, imageUrl, null, -1, -1, userId, communityId);

	}

	public static Post createSongPost(int songId, int userId, int communityId, String title) throws SQLException {

		Connection connection = Main.connect();
		Statement idStatement = connection.createStatement();
		ResultSet r = idStatement.executeQuery("select postid from posts order by postid desc");
		int id = 1;
		if (r.next()) {
			id = r.getInt(1) + 1;
		}

		String query = "insert into posts (postid, songId, userId, communityId, title) values (?, ?, ?, ?, ?)";
		PreparedStatement statement = connection.prepareStatement(query);
		statement.setInt(1, id);
		statement.setInt(2, songId);
		statement.setInt(3, userId);
		statement.setInt(4, communityId);
		statement.setString(5, title);
		statement.executeUpdate();

		statement.close();
		connection.close();

		return new Post(id, title, null, null, songId, -1, userId, communityId);

	}

	public static Post createPlaylistPost(int playlistId, int userId, int communityId, String title)
			throws SQLException {

		Connection connection = Main.connect();
		Statement idStatement = connection.createStatement();
		ResultSet r = idStatement.executeQuery("select postid from posts order by postid desc");
		int id = 1;
		if (r.next()) {
			id = r.getInt(1) + 1;
		}

		String query = "insert into posts (postid, playlistId, userId, communityId, title) values (?, ?, ?, ?, ?)";
		PreparedStatement statement = connection.prepareStatement(query);
		statement.setInt(1, id);
		statement.setInt(2, playlistId);
		statement.setInt(3, userId);
		statement.setInt(4, communityId);
		statement.setString(5, title);
		statement.executeUpdate();

		statement.close();
		connection.close();

		return new Post(id, title, null, null, -1, playlistId, userId, communityId);
	}

	public static ArrayList<Post> getPostsByCommunityId(int communityId) throws Exception {
		ArrayList<Post> posts = new ArrayList<Post>();
		Connection connection = Main.connect();
		Statement statement = connection.createStatement();
		ResultSet r = statement.executeQuery("select * from posts where communityId = '" + communityId + "'");
		while (r.next()) {
			int id = r.getInt("postid");
			String title = r.getString("title");
			String text = r.getString("text");
			String imageUrl = r.getString("imageUrl");
			int songId = r.getInt("songid");
			int playlistId = r.getInt("playlistid");
			int userId = r.getInt("userid");
			posts.add(new Post(id, title, text, imageUrl, songId, playlistId, userId, communityId));
		}
		statement.close();
		connection.close();
		return posts;
	}
}
