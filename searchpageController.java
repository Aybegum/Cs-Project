import javafx.scene.Node;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.ResourceBundle;

import javax.print.DocFlavor.URL;

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
				Label songNo = new Label(songCounter + "");
				Button playButton = new Button("start");
				Label songName = new Label("- " + songs.getName() + " - " + songs.getArtist());
				Button deleteButton = new Button(" - ");
				song.getChildren().addAll(songNo, playButton, songName, deleteButton);
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
				Label songName = new Label(songCounter + "- " + users.getUsername() + " - " + users.getArtist());
				Button deleteButton = new Button(" - ");
				user.getChildren().addAll(songName, deleteButton);
				songsFlowPane.getChildren().add(user);
				songsFlowPane.setVisible(true);
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
}
