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
      private FlowPane trial = new FlowPane();
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

            flowPane.setPrefHeight(180);

            if(playlists.size() <= 6){
                  displayPlaylist();
            } else {
                  flowPane.setPrefHeight(flowPane.getPrefHeight() + 30);
                  displayPlaylist();
            }
            flowPane.getChildren().add(trial);
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
            for (int i = 0; i < playlists.size(); i++) {
                  
                  displayPlaylist();

                  if(i > 6){
                        flowPane.setPrefHeight(flowPane.getPrefHeight() + 30);
                        trial.setPrefHeight(trial.getPrefHeight() + 30);
                        displayPlaylist();
                  }
            }
            flowPane.getChildren().add(trial);
      }

      public void displayPlaylist()throws SQLException{
            Button newPlaylist = new Button("Playlist");
            newPlaylist.setFont(Font.font("Times New Roman", 16));
            newPlaylist.setPrefSize(154,30);
            trial.getChildren().add(newPlaylist);
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
