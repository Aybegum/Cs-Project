import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.FlowPane;
import javafx.scene.shape.Circle;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollBar;
import javafx.stage.Stage;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.fxml.FXMLLoader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import org.w3c.dom.Text;
import javafx.scene.Node;
public class communityHubController {
    private FlowPane flowPane;
        private Stage stage;
        private Scene scene;
        private Parent root;
        private static int coordinateY = 122;
        private static int playlistNoNCounter = 1;
        private static Playlist playlistOnScreen;



    public void goToCommunity(MouseEvent event) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("postPage.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();

    }

    public void goToPlaylist(MouseEvent event)throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("playlistPage.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    public void renderPlaylistsOnSidebar(MouseEvent event) throws SQLException {

            ArrayList<Playlist> playlists = new ArrayList<>();

            Connection connection = Main.connect();
            Statement statement = connection.createStatement();
            ResultSet rs = statement.executeQuery("select * from playlists where creatorid = '" + User.getCurrentUser().getId() + "' order by id desc");
            int count;
            if (rs.next()) {
                  count = rs.getInt("id");
                  for (int i = count - 1; i >= 0; i--) {
                        playlists.add(Playlist.getPlaylistByIdAndUser(i, User.getCurrentUser()));
                  }
            }
            for (Playlist playlist : playlists) {
                  Button newPlaylist = new Button("Playlist");
                  flowPane.getChildren().add(newPlaylist);
            }
            Button newPlaylist = new Button("Playlist " + playlistNoNCounter);
            newPlaylist.setLayoutX(437); // it is the coordinate of the liked songs button
            newPlaylist.setLayoutY(coordinateY);
            root.getChildrenUnmodifiable().add(newPlaylist);
            playlistNoNCounter++;
            coordinateY += 35;
      }
}
