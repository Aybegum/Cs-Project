import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.FlowPane;
import javafx.scene.shape.Circle;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.stage.Stage;
import javafx.scene.Parent;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.scene.Scene;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.ResourceBundle;

import com.mysql.cj.xdevapi.AddResult;

import javafx.scene.Node;
public class playlistController implements Initializable{
      
      private Stage stage;
      private Scene scene;
      private Parent root;
      private static int coordinateY = 122;
      private static int playlistNoNCounter = 1;
      private static Playlist playlistOnScreen;
      @FXML
      private ScrollPane allPlaylistScrollPane;
      @FXML
      private FlowPane flowPane;
      public int countPlay = 0;

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
            if(playlistNoNCounter <= 6){
                  displayPlaylist();
            }
      }
      public void renderPlaylistsOnSidebar()throws SQLException{
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
                  displayPlaylist();
                  countPlay++;
                  if(countPlay == 6){
                        break;
                  }
            }
      }

      public void displayPlaylist()throws SQLException{
            Button newPlaylist = new Button("Playlist");
            newPlaylist.setFont(Font.font("Times New Roman", 16));
            newPlaylist.setPrefSize(154,29);
            flowPane.getChildren().add(newPlaylist);
      }
        
        public void goToProfile(MouseEvent event) throws Exception{
              Parent root = FXMLLoader.load(getClass().getResource("profilePage.fxml"));
              stage = (Stage)((Node)event.getSource()).getScene().getWindow();
              scene = new Scene(root);
              stage.setScene(scene);
              stage.show();
        }
        public void goToCommunityHub(MouseEvent event) throws Exception{
              Parent root = FXMLLoader.load(getClass().getResource("communityhubPage.fxml"));
              stage = (Stage)((Node)event.getSource()).getScene().getWindow();
              scene = new Scene(root);
              stage.setScene(scene);
              stage.show();
        }

        public void createPlaylist1 (MouseEvent event) throws SQLException{
            Playlist.createPlaylist(User.getCurrentUser(), "Playlist", "efuhjıdfsjjd");
            renderPlaylistsOnSidebar(event);
        } 
        @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            renderPlaylistsOnSidebar();
        } catch (SQLException e) {
            System.out.println("Error in rendering messages: SQLException");
        }
    }
} 