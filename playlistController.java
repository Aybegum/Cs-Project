import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.FlowPane;
import javafx.scene.shape.Circle;
import javafx.scene.control.Button;
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

import com.mysql.cj.xdevapi.AddResult;

import javafx.scene.Node;
    public class playlistController {
      @FXML
      private FlowPane flowPane;
        private Stage stage;
        private Scene scene;
        private Parent root;
        private static int coordinateY = 122;
        private static int playlistNoNCounter = 1;
        private static Playlist playlistOnScreen;


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

            //for (Playlist playlist : playlists) {
            //      Button newPlaylist = new Button("Playlist");
            //      flowPane.getChildren().add(newPlaylist);
            //}

            Button newPlaylist = new Button("Playlist " + playlistNoNCounter);
            newPlaylist.setLayoutX(437); // it is the coordinate of the liked songs button
            newPlaylist.setLayoutY(coordinateY);
            root.getChildrenUnmodifiable().add(newPlaylist);
            playlistNoNCounter++;
            coordinateY += 35;

            newPlaylist.setOnAction(e -> {
                  try {
                      root = FXMLLoader.load(getClass().getResource("communityhubPage.fxml"));
                      stage = (Stage) ((Node) e.getSource()).getScene().getWindow();
                      scene = new Scene(root);
                      stage.setScene(scene);
                      stage.show();
                  } catch (IOException ex) {
                      ex.printStackTrace();
                  }
              });              
      }

        /*public void createPlaylist (String playlistName, String coverURL, Song song) {
              
        }*/
        public void goToProfile(MouseEvent event) throws Exception{
              root = FXMLLoader.load(getClass().getResource("profilePage.fxml"));
              stage = (Stage)((Node)event.getSource()).getScene().getWindow();
              scene = new Scene(root);
              stage.setScene(scene);
              stage.show();
        }
        public void goToCommunityHub(MouseEvent event) throws Exception{
              root = FXMLLoader.load(getClass().getResource("communityhubPage.fxml"));
              stage = (Stage)((Node)event.getSource()).getScene().getWindow();
              scene = new Scene(root);
              stage.setScene(scene);
              stage.show();
        }
} 

