


import java.util.ArrayList;
import java.sql.*;

public class Playlist {
	private String name;
	private String pictureURL;
	private int ID;
	private int creatorID;
	private ArrayList<Song> songs = new ArrayList<>();
	private static int currenPlaylistId = -1;

	public Playlist(String name, String pictureURL, int ID, int creatorID) throws SQLException {

		this.name = name;
		this.pictureURL = pictureURL;
		this.ID = ID;
		this.creatorID = creatorID;
	}

	public static void setCurrenPlaylistId(int currenPlaylistId) {
		Playlist.currenPlaylistId = currenPlaylistId;
	}

	public static int getCurrenPlaylistId() {
		return currenPlaylistId;
	}

	public String getPlaylistName() {
		return name;
	}

	public void setPlaylistName(String playlistName) {
		this.name = playlistName;
	}

	public String getPictureURL() {
		return pictureURL;
	}

	public void setPictureURL(String pictureURL) {
		this.pictureURL = pictureURL;
	}

	public int getPlaylistID() {
		return ID;
	}

	public void setPlaylistID(int playlistID) {
		this.ID = playlistID;
	}

	public int getCreatorID() {
		return creatorID;
	}

	public void setCreatorID(int creatorID) {
		this.creatorID = creatorID;
	}

	public ArrayList<Song> getSongs() {
		return songs;
	}

	public void setSongs(ArrayList<Song> songs) {
		this.songs = songs;
	}

	public static Playlist createPlaylist(User currentUser, String name, String pictureUrl) throws SQLException {
		String query = "INSERT INTO playlists (name, pictureurl, creatorid, id) VALUES (?, ?, ?, ?)";
		PreparedStatement statement = null;
		ResultSet resultSet = null;
		Connection connection = Main.connect();

		try {
			Statement idStatement = connection.createStatement();
			ResultSet r = idStatement.executeQuery("select id from playlists order by id desc");
			int id;
			if (r.next()) {
				id = r.getInt(1) + 1;
			} else {
				id = 1;
			}

			statement = connection.prepareStatement(query);
			statement.setString(1, name);
			statement.setString(2, pictureUrl);
			statement.setInt(3, currentUser.getId());
			statement.setInt(4, id);
			statement.executeUpdate();

			return new Playlist(name, pictureUrl, id, currentUser.getId());

		} finally {
			if (resultSet != null) {
				resultSet.close();
			}
			if (statement != null) {
				statement.close();
			}
		}
	}

	public boolean isSongInPlaylist(Song song) {
		return getSongs().indexOf(song) != -1;
	}

	public static void deletePlaylist(int ID) throws SQLException {

		Connection connection = Main.connect();

		String query = "delete from playlist_song where playlistid = ?";
		try (PreparedStatement statement = connection.prepareStatement(query)) {
			statement.setInt(1, ID);
			statement.executeUpdate();
			statement.close();
		}

		query = "DELETE FROM playlists WHERE id = ?";
		try (PreparedStatement statement = connection.prepareStatement(query)) {
			statement.setInt(1, ID);
			statement.executeUpdate();
			statement.close();
		}
		connection.close();

	}

	public void addSong(int songID) throws SQLException {

		Connection connection = Main.connect();

		String query = "INSERT INTO playlist_song (playlistid, songid) VALUES (?, ?)";
		try (PreparedStatement statement = connection.prepareStatement(query)) {
			statement.setInt(1, ID);
			statement.setInt(2, songID);
			statement.executeUpdate();
			statement.close();
		}
		connection.close();
	}

	public void removeSong(int songID) throws SQLException {

		Connection connection = Main.connect();

		String query = "DELETE FROM playlist_song WHERE playlistid = ? AND songid = ?";
		try (PreparedStatement statement = connection.prepareStatement(query)) {
			statement.setInt(1, ID);
			statement.setInt(2, songID);
			statement.executeUpdate();
			statement.close();
		}
		connection.close();
	}

	public static ArrayList<Song> getSongsInPlaylist(int playlistId) throws SQLException {
		ArrayList<Song> songs = new ArrayList<>();
		String query = "SELECT * FROM playlist_song WHERE playlistid = ?";
		Connection connection = Main.connect();
		try (PreparedStatement statement = connection.prepareStatement(query)) {
			statement.setInt(1, playlistId);
			ResultSet resultSet = statement.executeQuery();
			while (resultSet.next()) {
				int songId = resultSet.getInt("songid");
				Song song = Song.getSongById(songId);
				if (song != null) {
					songs.add(song);
				}
			}
			statement.close();
		}
		connection.close();
		return songs;
	}

	public static Playlist getPlaylistByNameAndUser(String name, User user) throws SQLException {

		Connection connection = Main.connect();
		Statement statement = connection.createStatement();
		ResultSet rs = statement.executeQuery(
				"select * from playlists where name = '" + name + "' and creatorId = '" + user.getId() + "'");

		int id;
		String pictureUrl;

		if (rs.next()) {

			id = rs.getInt("id");
			pictureUrl = rs.getString("pictureurl");
			statement.close();
			connection.close();
			return new Playlist(name, pictureUrl, id, user.getId());

		}
		statement.close();
		connection.close();
		return null;

	}

	public static Playlist getPlaylistByIdAndUser(int id, User user) throws SQLException {

		Connection connection = Main.connect();
		Statement statement = connection.createStatement();
		ResultSet rs = statement
				.executeQuery("select * from playlists where id = '" + id + "' and creatorId = '" + user.getId() + "'");

		String name;
		String pictureUrl;

		if (rs.next()) {

			name = rs.getString("name");
			pictureUrl = rs.getString("pictureurl");
			statement.close();
			connection.close();
			return new Playlist(name, pictureUrl, id, user.getId());
		}
		connection.close();
		return null;

	}

	public static Playlist getPlaylistById(int id) throws SQLException {

		Connection connection = Main.connect();
		Statement statement = connection.createStatement();
		ResultSet rs = statement.executeQuery("select * from playlists where id = '" + id + "'");

		String name;
		String pictureUrl;
		int userId;

		if (rs.next()) {

			userId = rs.getInt("creatorid");
			name = rs.getString("name");
			pictureUrl = rs.getString("pictureurl");
			statement.close();
			connection.close();
			return new Playlist(name, pictureUrl, id, userId);
		}
		connection.close();
		return null;

	}

	public void updatePlaylistName(String newName) throws Exception {
		Connection connection = Main.connect();
		Statement statement = connection.createStatement();
		statement.executeUpdate("update playlists set name = '" + newName + "' where id = '" + this.ID + "'");
		statement.close();
		connection.close();
	}
}
