import javafx.scene.Node;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Circle;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.shape.Line;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
public class searchpageController implements Initializable {
	
    @FXML
    private Rectangle backRect;

    @FXML
    private Polygon backTria;
    @FXML
    private ComboBox <String> comboBoxSearch;
	
    @FXML
    private Circle enterSongCircle;

    @FXML
    private Line enterSongLine;

    @FXML
    private TextField searchBarTextField;

    @FXML
    private VBox songsFlowPane;

    private Stage stage;
    private Scene scene;
    private Parent root;
    private static int songCounter = 1;
	private static int playlistNoCounter = 1;
	private static boolean isPlaying = false;
	

    public void goToCommunityHub(MouseEvent event) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("communityhubPage.fxml"));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    public ArrayList<Song> searchSongsByNameorArtist(String searchTerm) throws SQLException {
		ArrayList<Song> resultSongs = new ArrayList<Song>();
		Connection connection = Main.connect();
		PreparedStatement stat = null;
		ResultSet rs = null;

		try {
			String query = "select * from songs where name like ? OR artist like ?";
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
    
	public void renderSongs(MouseEvent event) {
		ArrayList<Song> searchedSongs = new ArrayList<>();
		ArrayList<User> searchedUsers = new ArrayList<>();
		ArrayList<Playlist> searchedPlaylists = new ArrayList<>();
		
		if (!comboBoxSearch.getSelectionModel().isEmpty()) {
			if (comboBoxSearch.getValue().equals("Song")) {
				try {
					searchedSongs = searchSongsByNameorArtist(searchBarTextField.getText());
				} catch (SQLException e) {
					e.printStackTrace();
				}
				
				for (Song song : searchedSongs) {
					BorderPane songHBox = new BorderPane();
					Label songInfo = new Label(songCounter + "- "  + getSongNameWithSpaces(song));
					songInfo.setFont(Font.font("Times New Roman", 12));
					Button playButton = new Button("Play");
					playButton.setFont(Font.font("Times New Roman", 12));
					Region spacer = new Region();
					HBox.setHgrow(spacer, Priority.ALWAYS);
					
					playButton.setOnAction(new EventHandler<ActionEvent>() {
						@Override
						public void handle(ActionEvent event) {
							try {
								arrangeSong(song);
								if(song.getisPlaying()){
									playButton.setText("Pause");
								}
								else{
									playButton.setText("Play");
								}
							} catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
								e.printStackTrace();
							}
						}
					});
					
					songHBox.setLeft(songInfo);
					songHBox.setRight(playButton);
					songsFlowPane.getChildren().add(songHBox);
					songsFlowPane.setVisible(true);
					songCounter++;
				}
			} else if (comboBoxSearch.getValue().equals("User")) {
				try {
					searchedUsers = searchUserByName(searchBarTextField.getText());
				} catch (SQLException e) {
					e.printStackTrace();
				}
				for (User user : searchedUsers) {
					HBox userHBox = new HBox();
					Label userName = new Label(songCounter + "- " + user.getUsername() + " - " + user.getUsername());
					Button deleteButton = new Button(" - ");
					userHBox.getChildren().addAll(userName, deleteButton);
					songsFlowPane.getChildren().add(userHBox);
					songsFlowPane.setVisible(true);
					if(songsFlowPane == null){
						songCounter = 1;
					}
				}
			} else if (comboBoxSearch.getValue().equals("Playlist")) {
				try {
					searchedPlaylists = searchPlaylistsByName(searchBarTextField.getText());
				} catch (SQLException e) {
					e.printStackTrace();
				}
				for (Playlist playlist : searchedPlaylists) {
					HBox playlistHBox = new HBox();
					Label playlistName = new Label();
					try {
						playlistName = new Label(playlistNoCounter + "- " + playlist.getPlaylistName() + " - " + User.getById(playlist.getCreatorID()));
					} catch (SQLException e) {
						e.printStackTrace();
					}
					Button viewButton = new Button("View");
					playlistHBox.getChildren().addAll(playlistName, viewButton);
					songsFlowPane.getChildren().add(playlistHBox);
					songsFlowPane.setVisible(true);
					playlistNoCounter++;
				}
			}
		}
	}
    public ArrayList<User> searchUserByName(String searchTerm) throws SQLException {
		ArrayList<User> resultUsers = new ArrayList<User>();
		Connection connection = Main.connect();
		PreparedStatement stat = null;
		ResultSet rs = null;

		try {
			String query = "select * from users where name like ?";
			stat = connection.prepareStatement(query);
			stat.setString(1, "%" + searchTerm + "%");

			rs = stat.executeQuery();
			while (rs.next()) {
				int id = rs.getInt("id");
				String username = rs.getString("name");
                String password = rs.getString("password");
                String pictureUrl = rs.getString("pictureURL");
                String email = rs.getString("email");
				User user = new User(id, username, password, pictureUrl, email);
				resultUsers.add(user);
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
		return resultUsers;
	}

    public ArrayList<Playlist> searchPlaylistsByName(String searchTerm) throws SQLException {
		ArrayList<Playlist> resultPlaylists = new ArrayList<Playlist>();
		Connection connection = Main.connect();
		PreparedStatement stat = null;
		ResultSet rs = null;

		try {
			String query = "select * from playlists where name like ?";
			stat = connection.prepareStatement(query);
			stat.setString(1, "%" + searchTerm + "%");

			rs = stat.executeQuery();
			while (rs.next()) {
				int id = rs.getInt("id");
				String name = rs.getString("name");
                String pictureUrl = rs.getString("pictureURL");
                int creatorID = rs.getInt("creatorID");
                String email = rs.getString("email");
				Playlist playlist = new Playlist(name, pictureUrl, id, creatorID);
				resultPlaylists.add(playlist);
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
		return resultPlaylists;
	}

    @Override
    public void initialize(java.net.URL location, ResourceBundle resources) {
		songsFlowPane.setStyle("-fx-background-color: 053c75;");
        ObservableList<String> options = FXCollections.observableArrayList("User", "Song", "Playlist");
        comboBoxSearch.setItems(options);
    }
	
	public static void arrangeSong(Song song) throws UnsupportedAudioFileException, IOException, LineUnavailableException {
		String filePath = song.getUrl().substring(39);
		File file = new File(filePath);
		AudioInputStream audioStream = AudioSystem.getAudioInputStream(file);
	
		Clip clip = song.getClip();
		if (clip == null) {
			clip = AudioSystem.getClip();
			clip.open(audioStream);
			song.setClip(clip);
		}

		if (Song.getCurrentlyPlayingClip() != null && Song.getCurrentlyPlayingClip().isRunning() && Song.getCurrentlyPlayingClip() != clip) {
			Song.getCurrentlyPlayingClip().stop();
			Song.getCurrentlyPlayingsSong().setIsPlaying(false);
		}
		if (song.getIsPlaying() || User.getCurrentUser() == null) {
			song.setPausedPosition(clip.getMicrosecondPosition());
			clip.stop();
			song.setIsPlaying(false);
		} else {
			clip.setMicrosecondPosition(song.getPausedPosition());
			clip.start();
			song.setIsPlaying(true);
		}
		Song.currentlyPlayingSong = song;
		Song.currentlyPlayingClip = clip;
	}
	
	public String getSongNameWithSpaces(Song song) {

		String name = song.getName().replace("_", " ");
		return name;
	}
	
	/*public void stopSong(Song song) throws UnsupportedAudioFileException, IOException, LineUnavailableException{
		String a = song.getUrl().substring(39);
		File file = new File(a);
        AudioInputStream audioStream = AudioSystem.getAudioInputStream(file);
        Clip clip = AudioSystem.getClip();
        clip.open(audioStream);
		clip.stop();
		//clip.setMicrosecondPosition(0);
		// we'll use these methods  
		isPlaying = false;
	}*/
}
