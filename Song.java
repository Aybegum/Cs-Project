
import java.io.File;
import java.io.FilenameFilter;
import java.sql.*;


public class Song {
	private int ID;
	private String name; 
	private String artist;
	
    public Song (int ID, String name, String artist) {
    	this.ID = ID;
    	this.name = name;
    	this.artist = artist;
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
	
	public static Song getSongById(int songId) throws SQLException {
	    String query = "SELECT * FROM songs WHERE id = ?";
	    Connection connection = Main.connect();
	    try (PreparedStatement statement = connection.prepareStatement(query)) {
	        statement.setInt(1, songId);
	        ResultSet resultSet = statement.executeQuery();
	        if (resultSet.next()) {
	            String songName = resultSet.getString("name");
	            String artist = resultSet.getString("artist");
	            return new Song(songId, songName, artist);
	        }
	    }
	    return null; // Return null if song with given ID is not found
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
	            return new Song(songId, songName, artist);
	        }
	    }
	    return null; // Return null if song with given ID is not found
	}
	
	// Method to upload songs from a folder to a database table
    public static void uploadSongs(int id, String folderPath, String url, String user, String password) {

    }
	
    public static void main(String[] args) {
        
		Class.forName("com.mysql.cj.jdbc.Driver");

        String folderPath = "C:\\Users\\beren\\OneDrive\\Masaüstü\\songs";

        uploadSongs(folderPath);
	}
}
