
import java.util.ArrayList;
import java.sql.*;

public class Playlist {
    private String name;
    private String pictureURL;
    private int ID;
    private int creatorID;
    private Connection connection;
    private ArrayList<Song> songs = new ArrayList<>();

    public Playlist (String name, String pictureURL, int ID, int creatorID) throws SQLException {
        
        this.name = name;
        this.pictureURL = pictureURL;
        this.ID = ID;
        this.creatorID = creatorID;
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
			
			statement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
			statement.setString(1, name);
	        statement.setString(2, pictureUrl);
	        statement.setInt(3, currentUser.getId());
	        statement.setInt(4, id);
	        statement.executeUpdate();
	        resultSet = statement.getGeneratedKeys();
	        
	        if (resultSet.next()) {
	            return new Playlist(name, pictureUrl, id, currentUser.getId());
	        }
		}
		finally {
	        if (resultSet != null) {
	            resultSet.close();
	        }
	        if (statement != null) {
	            statement.close();
	        }
	    }
		return null;
	}
	
	public static void deletePlaylist(int ID) throws SQLException {
		
		Connection connection = Main.connect();
		
		String query = "delete from playlist_song where playlistid = ?";
		try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, ID);
            statement.executeUpdate();
        }
		
		query = "DELETE FROM playlists WHERE id = ?";
		try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, ID);
            statement.executeUpdate();
        }	

	}
	
	public void addSong (int songID) throws SQLException {
		
		Connection connection = Main.connect();
		
		String query = "INSERT INTO playlist_song (playlistid, songid) VALUES (?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, ID);
            statement.setInt(2, songID);
            statement.executeUpdate();
        } 
	}
	
	public void removeSong (int songID) throws SQLException {
		
		Connection connection = Main.connect();
		
		String query = "DELETE FROM playlist_song WHERE playlistid = ? AND songid = ?";
		try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1,ID);
            statement.setInt(2, songID);
            statement.executeUpdate();
        }
	}
	
	
	public ArrayList<Song> getSongsInPlaylist(int playlistId) throws SQLException {
	    ArrayList<Song> songs = new ArrayList<>();
	    String query = "SELECT * FROM playlistsongs WHERE playlistid = ?";
	    try (PreparedStatement statement = connection.prepareStatement(query)) {
	        statement.setInt(1, playlistId);
	        ResultSet resultSet = statement.executeQuery();
	        while (resultSet.next()) {
	            int songId = resultSet.getInt("song_id");
	            Song song = Song.getSongById(songId); 
	            if (song != null) {
	                songs.add(song);
	            }
	        }
	    }
	    return songs;
	}
	
	public static Playlist getPlaylistByNameAndUser(String name, User user) throws SQLException {
		
		Connection connection = Main.connect();
		Statement statement = connection.createStatement();
		ResultSet rs = statement.executeQuery("select * from playlists where name = '" + name + "' and creatorId = '" + user.getId() +"'");
		
		int id;
		String pictureUrl;
		
		if(rs.next()) {
			
			id = rs.getInt("id");
			pictureUrl = rs.getString("pictureurl");
			return new Playlist(name, pictureUrl, id, user.getId());
			
		}
		return null;
		
	}



}