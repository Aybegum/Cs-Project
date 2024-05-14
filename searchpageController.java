import javafx.scene.Node;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.ResourceBundle;

import javax.print.DocFlavor.URL;
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
import javafx.scene.layout.HBox;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.shape.Line;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Rectangle;
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
				Song song = new Song(id, songTitle, artist);
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
    
    public void renderSongs(MouseEvent event){
        ArrayList<Song> searchedSongs = new ArrayList<>();
		ArrayList<User> searchedUser = new ArrayList<>();
		ArrayList<Playlist> searchedPlaylist = new ArrayList<>();
		if(comboBoxSearch.getValue().equals("Song") || comboBoxSearch.getValue() == null){
			try {
				searchedSongs = searchSongsByNameorArtist(searchBarTextField.getText());
			} catch (SQLException e) {
				e.printStackTrace();
			}
			for(Song songs: searchedSongs){
				HBox song = new HBox();
				Label songInfo = new Label(songCounter + "- " + songs.getName() + " - " + songs.getArtist() + "                                          ");
				Button playButton = new Button("play");
				playButton.setOnAction(new EventHandler<ActionEvent>() {
					@Override
					public void handle(ActionEvent e) {
					
						//playSong(song);
                }
            });
				song.getChildren().addAll(songInfo, playButton);
				songsFlowPane.getChildren().add(song);
				songsFlowPane.setVisible(true);
				songCounter++;
			}
		}
		else if(comboBoxSearch.getValue().equals("User")){
			try {
				searchedUser = searchUserByName(searchBarTextField.getText());
			} catch (SQLException e) {
				e.printStackTrace();
			}
			for(User users: searchedUser){
				HBox user = new HBox();
				Label songName = new Label(songCounter + "- " + users.getUsername() + " - " + users.getUsername());
				Button deleteButton = new Button(" - ");
				user.getChildren().addAll(songName, deleteButton);
				songsFlowPane.getChildren().add(user);
				songsFlowPane.setVisible(true);
			}
		}
		else if(comboBoxSearch.getValue().equals("Playlist")){{
			try {
				searchedPlaylist = searchPlaylistsByName(searchBarTextField.getText());
			} catch (SQLException e) {
				e.printStackTrace();
			}
			for(Playlist playlists: searchedPlaylist){
				HBox playlist = new HBox();
				Label playlistName = new Label();
				try {
					playlistName = new Label(playlistNoCounter + "- " + playlists.getPlaylistName() + " - " + User.getById(playlists.getCreatorID()));
				} catch (SQLException e) {
					e.printStackTrace();
				}
				Button viewButton = new Button("view");
				playlist.getChildren().addAll(playlistName, viewButton);
				songsFlowPane.getChildren().add(playlist);
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
        ObservableList<String> options = FXCollections.observableArrayList("User", "Song", "Playlist");
        comboBoxSearch.setItems(options);
    }
	public void playSong(String songname) throws UnsupportedAudioFileException, IOException, LineUnavailableException{
		File file = new File("we have to get the files name to play");
        AudioInputStream audioStream = AudioSystem.getAudioInputStream(file);
        Clip clip = AudioSystem.getClip();
        clip.open(audioStream);
		clip.start();
		clip.stop();
		clip.setMicrosecondPosition(0);
		// we'll use these methods  
	}
}
