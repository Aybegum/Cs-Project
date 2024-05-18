import java.io.File;
import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

import javax.sound.sampled.Clip;

public class Song {
	private int ID;
	private String name;
	private String artist;
	private String url;
	private boolean isPlaying;
	private Clip clip;
	private long pausedPosition;
	public static boolean needToSwitch = true;
	public static Song currentlyPlayingSong;
	public static Clip currentlyPlayingClip;
	public static ArrayList<Song> queue = new ArrayList<>();

	public Song(int ID, String name, String artist, String url) {
		this.ID = ID;
		this.name = name;
		this.artist = artist;
		this.url = url;
	}
	// public Song chooseRandomSong () {

	// }

	public static Song getCurrentlyPlayingsSong() {
		return currentlyPlayingSong;
	}

	public static Clip getCurrentlyPlayingClip() {
		return currentlyPlayingClip;
	}

	public long getPausedPosition() {
		return pausedPosition;
	}

	public void setPausedPosition(long pausedPosition) {
		this.pausedPosition = pausedPosition;
	}

	public boolean getIsPlaying() {
		return isPlaying;
	}

	public void setIsPlaying(boolean isPlaying) {
		this.isPlaying = isPlaying;
	}

	public void setClip(Clip clip) {
		this.clip = clip;
	}

	public Clip getClip() {
		return clip;
	}

	public String getUrl() {
		return this.url;
	}

	public int getID() {
		return ID;
	}

	public void setID(int ID) {
		this.ID = ID;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getArtist() {
		return artist;
	}

	public void setArtist(String artist) {
		this.artist = artist;
	}

	public boolean getisPlaying() {
		return this.isPlaying;
	}

	public static Song getSongById(int songId) throws SQLException {
		String query = "SELECT * FROM songs WHERE id = ?";
		Connection connection = Main.connect();
		try (PreparedStatement statement = connection.prepareStatement(query)) {
			statement.setInt(1, songId);
			ResultSet resultSet = statement.executeQuery();
			if (resultSet.next()) {
				String songName = resultSet.getString("name");
				String artist = resultSet.getString("artist");
				String url = resultSet.getString("url");
				return new Song(songId, songName, artist, url);
			}
			statement.close();
			connection.close();
		}
		connection.close();
		return null; // Return null if song with given ID is not found
	}

	public static Song random() throws SQLException {
		Random random = new Random();
		int number = random.nextInt(95);
		return getAllSongs().get(number);
	}

	public static Song getSongByName(String songName) throws SQLException {
		String query = "SELECT * FROM songs WHERE name = ?";
		Connection connection = Main.connect();
		try (PreparedStatement statement = connection.prepareStatement(query)) {
			statement.setString(1, songName);
			ResultSet resultSet = statement.executeQuery();
			if (resultSet.next()) {
				int songId = resultSet.getInt("id");
				String artist = resultSet.getString("artist");
				String url = resultSet.getString("url");
				return new Song(songId, songName, artist, url);
			}
			statement.close();
			connection.close();
		}
		connection.close();
		return null; // Return null if song with given ID is not found
	}

	public static ArrayList<Song> searchSongsByNameorArtist(String searchTerm) throws SQLException {
		ArrayList<Song> resultSongs = new ArrayList<Song>();
		Connection connection = Main.connect();
		PreparedStatement stat = null;
		ResultSet rs = null;

		try {
			String query = "select * from songs where name like ? OR artist LIKE ?";
			stat = connection.prepareStatement(query);
			stat.setString(1, "%" + searchTerm + "%");
			stat.setString(2, "%" + searchTerm + "%");

			rs = stat.executeQuery();
			while (rs.next()) {
				int id = rs.getInt("id");
				String songTitle = rs.getString("name");
				String artist = rs.getString("artist");
				String url = rs.getString("url");
				Song song = new Song(id, songTitle, artist, url);
				resultSongs.add(song);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (rs != null) {
					rs.close();
				}
				if (rs != null) {
					stat.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return resultSongs;
	}

	public String getSongNameWithSpaces() {

		String name = getName().replace("_", " ");
		return name;
	}

	public String getArtistNameWithSpaces() {

		String artist = getArtist().replace("_", " ");
		return artist;
	}

	public void setisPlaying() {
		this.isPlaying = !this.isPlaying;
	} 

	public static ArrayList<Song> getAllSongs() throws SQLException {
		ArrayList<Song> allSongs = new ArrayList<Song>();
		Connection connection = Main.connect();
		String query = "SELECT id, name, artist, url FROM songs";
		Statement stat = connection.createStatement();
		ResultSet rs = stat.executeQuery(query);

		while (rs.next()) {
			int id = rs.getInt("id");
			String name = rs.getString("name");
			String artist = rs.getString("artist");
			String url = rs.getString("url");

			Song song = new Song(id, name, artist, url);
			allSongs.add(song);
		}
		connection.close();
		stat.close();
		return allSongs;
	}
}
