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
import javafx.scene.text.Text;

public class searchPlaylistController implements Initializable {
	@FXML
	private TextField searchBarTextField;

	@FXML
	private VBox songsFlowPane;
	private static int playlistNoCounter = 1;

	public void renderSongs(MouseEvent event) throws SQLException {
		ArrayList<Playlist> searchedPlaylists = new ArrayList<>();
		searchedPlaylists = searchPlaylistsByName(searchBarTextField.getText());
		for (Playlist playlist : searchedPlaylists) {
			HBox playlistHBox = new HBox();
			Text playlistName = new Text();
			try {
				playlistName = new Text(playlistNoCounter + "- " + playlist.getPlaylistName() + " - "
						+ User.getById(playlist.getCreatorID()));
			} catch (SQLException e) {
				e.printStackTrace();
			}
			Button viewButton = new Button("Share");
			viewButton.setFont(Font.font("Times New Roman", 16));
			viewButton.setPrefSize(30, 30);
			// viewButton.setOnAction(e -> popupStage.close());
			playlistHBox.getChildren().addAll(playlistName, viewButton);
			songsFlowPane.getChildren().add(playlistHBox);
			songsFlowPane.setVisible(true);
			playlistNoCounter++;
		}
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
	}
}
