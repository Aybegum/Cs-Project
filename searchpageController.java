import javafx.scene.Node;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Circle;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.HBox;
import javafx.fxml.FXML;
import javafx.scene.shape.Line;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Rectangle;
public class searchpageController {

    @FXML
    private Rectangle backRect;

    @FXML
    private Polygon backTria;

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

    public static ArrayList<Song> searchSongsByNameorArtist(String searchTerm) throws SQLException {
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
    
    public void renderSearchedSongs(MouseEvent event) throws SQLException{
        ArrayList<Song> searchedSongs = searchSongsByNameorArtist(searchBarTextField.getText());
        for(Song songs: searchedSongs){
            HBox song = new HBox();
            Button playButton = new Button("start");
            Label songName = new Label(songCounter + "- " + songs.getName() + " - " + songs.getArtist());
            Button deleteButton = new Button(" - ");
            song.getChildren().addAll(playButton, songName, deleteButton);
            songsFlowPane.getChildren().add(song);
	    songsFlowPane.setVisible(true);
        }
    } 
}
