<<<<<<< Updated upstream

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Post {
	
	private int id;
	private String text;
	private String imageUrl;
	private int songId;
	private int playlistId;
	private int userId;
	
	public Post(int id, String text, String imageUrl, int songId, int playlistId, int userId) {
		this.id = id;
		this.text = text;
		this.imageUrl = imageUrl;
		this.songId = songId;
		this.playlistId = playlistId;
		this.userId = userId;
	}
	
	public static Post createTextPost(String text, int userId) throws SQLException {
		
		Connection connection = Main.connect();
		Statement idStatement = connection.createStatement();
		ResultSet r = idStatement.executeQuery("select id from posts order by id desc");
		r.next();
		int id = r.getInt(1) + 1;
		
		String query = "insert into posts (id, text, userId) values (?, ?, ?)";
		PreparedStatement statement = connection.prepareStatement(query);
		statement.setInt(1, id);
		statement.setString(2, text);
		statement.setInt(3, userId);
		statement.executeUpdate();
		
		statement.close();
		connection.close();
		
		return new Post(id, text, null, -1, -1, userId);
		
	}
	
	public static Post createImagePost(String imageUrl, int userId) throws SQLException{
		
		Connection connection = Main.connect();
		Statement idStatement = connection.createStatement();
		ResultSet r = idStatement.executeQuery("select id from posts order by id desc");
		r.next();
		int id = r.getInt(1) + 1;
		
		String query = "insert into posts (id, imageUrl, userId) values (?, ?, ?)";
		PreparedStatement statement = connection.prepareStatement(query);
		statement.setInt(1, id);
		statement.setString(2, imageUrl);
		statement.setInt(3, userId);
		statement.executeUpdate();
		
		statement.close();
		connection.close();
		
		return new Post(id, imageUrl, null, -1, -1, userId);
		
	}
	
	public static Post createSongPost(int songId, int userId) throws SQLException {
		
		Connection connection = Main.connect();
		Statement idStatement = connection.createStatement();
		ResultSet r = idStatement.executeQuery("select id from posts order by id desc");
		r.next();
		int id = r.getInt(1) + 1;
		
		String query = "insert into posts (id, songId, userId) values (?, ?, ?)";
		PreparedStatement statement = connection.prepareStatement(query);
		statement.setInt(1, id);
		statement.setInt(2, songId);
		statement.setInt(3, userId);
		statement.executeUpdate();
		
		statement.close();
		connection.close();
		
		return new Post(id, null, null, songId, -1, userId);
		
	}
	
	public static Post createPlaylistPost(int playlistId, int userId) throws SQLException{
		
		Connection connection = Main.connect();
		Statement idStatement = connection.createStatement();
		ResultSet r = idStatement.executeQuery("select id from posts order by id desc");
		r.next();
		int id = r.getInt(1) + 1;
		
		String query = "insert into posts (id, text, playlistId) values (?, ?, ?)";
		PreparedStatement statement = connection.prepareStatement(query);
		statement.setInt(1, id);
		statement.setInt(2, playlistId);
		statement.setInt(3, userId);
		statement.executeUpdate();
		
		statement.close();
		connection.close();
		
		return new Post(id, null, null, -1, playlistId, userId);
		
	}

	
	
	
}
=======
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Post {
	
	private int id;
	private String text;
	private String imageUrl;
	private int songId;
	private int playlistId;
	private int userId;
	
	public Post(int id, String text, String imageUrl, int songId, int playlistId, int userId) {
		this.id = id;
		this.text = text;
		this.imageUrl = imageUrl;
		this.songId = songId;
		this.playlistId = playlistId;
		this.userId = userId;
	}
	
	public static Post createTextPost(String text, int userId) throws SQLException {
		
		Connection connection = Main.connect();
		Statement idStatement = connection.createStatement();
		ResultSet r = idStatement.executeQuery("select id from posts order by id desc");
		r.next();
		int id = r.getInt(1) + 1;
		
		String query = "insert into posts (id, text, userId) values (?, ?, ?)";
		PreparedStatement statement = connection.prepareStatement(query);
		statement.setInt(1, id);
		statement.setString(2, text);
		statement.setInt(3, userId);
		statement.executeUpdate();
		
		statement.close();
		connection.close();
		
		return new Post(id, text, null, -1, -1, userId);
		
	}
	
	public static Post createImagePost(String imageUrl, int userId) throws SQLException{
		
		Connection connection = Main.connect();
		Statement idStatement = connection.createStatement();
		ResultSet r = idStatement.executeQuery("select id from posts order by id desc");
		r.next();
		int id = r.getInt(1) + 1;
		
		String query = "insert into posts (id, imageUrl, userId) values (?, ?, ?)";
		PreparedStatement statement = connection.prepareStatement(query);
		statement.setInt(1, id);
		statement.setString(2, imageUrl);
		statement.setInt(3, userId);
		statement.executeUpdate();
		
		statement.close();
		connection.close();
		
		return new Post(id, imageUrl, null, -1, -1, userId);
		
	}
	
	public static Post createSongPost(int songId, int userId) throws SQLException {
		
		Connection connection = Main.connect();
		Statement idStatement = connection.createStatement();
		ResultSet r = idStatement.executeQuery("select id from posts order by id desc");
		r.next();
		int id = r.getInt(1) + 1;
		
		String query = "insert into posts (id, songId, userId) values (?, ?, ?)";
		PreparedStatement statement = connection.prepareStatement(query);
		statement.setInt(1, id);
		statement.setInt(2, songId);
		statement.setInt(3, userId);
		statement.executeUpdate();
		
		statement.close();
		connection.close();
		
		return new Post(id, null, null, songId, -1, userId);
		
	}
	
	public static Post createPlaylistPost(int playlistId, int userId) throws SQLException{
		
		Connection connection = Main.connect();
		Statement idStatement = connection.createStatement();
		ResultSet r = idStatement.executeQuery("select id from posts order by id desc");
		r.next();
		int id = r.getInt(1) + 1;
		
		String query = "insert into posts (id, text, playlistId) values (?, ?, ?)";
		PreparedStatement statement = connection.prepareStatement(query);
		statement.setInt(1, id);
		statement.setInt(2, playlistId);
		statement.setInt(3, userId);
		statement.executeUpdate();
		
		statement.close();
		connection.close();
		
		return new Post(id, null, null, -1, playlistId, userId);
		
	}

	
	
	
}
>>>>>>> Stashed changes
