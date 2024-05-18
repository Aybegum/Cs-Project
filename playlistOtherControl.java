import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
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

import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

import javafx.scene.Node;

public class playlistOtherControl implements Initializable {

      private Stage stage;
      private Scene scene;
      private Parent root;
      private Playlist playlist;
      @FXML
      private Text playlistName;

      @FXML
      private VBox playlistBox;
      @FXML
      private FlowPane playlistContent; 
      @FXML
      private Text otherUsername;
      public void goToCreatePlaylist(MouseEvent event) throws Exception {
            Playlist.setCurrenPlaylistId(Playlist.createPlaylist(User.getCurrentUser(), "Playlist", "efuhjıdfsjjd").getPlaylistID());
            Parent root = FXMLLoader.load(getClass().getResource("createPlaylist2.fxml"));
            stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
    
        }

      public void renderPlaylist() throws Exception {
            ArrayList<Song> songArrayList = Playlist.getSongsInPlaylist(Playlist.getCurrenPlaylistId());
            System.out.println(songArrayList);
            for (Song song : songArrayList) {
                  BorderPane songBox = new BorderPane();
                  songBox.setStyle("-fx-background-color: #053c75; -fx-padding: 5px; -fx-background-radius: 5px;");
                  songBox.setPrefWidth(400);
                  Text songInfo = new Text(song.getSongNameWithSpaces() + " -" + song.getArtistNameWithSpaces());
                  songInfo.setFont(new Font("Times New Roman", 16));
                  songInfo.setFill(Color.WHITE);
                  songInfo.setTextAlignment(TextAlignment.CENTER);
            
                  Button playButton = new Button("Play");
                  playButton.setFont(Font.font("Times New Roman", 12));
                  playButton.setOnMouseClicked(new EventHandler<MouseEvent>() {
                        @Override
                        public void handle(MouseEvent event) {
                              try {
                                    searchpageController.arrangeSong(song);
                                    if (song.getisPlaying()) {
                                          playButton.setText("Pause");
                                    } else {
                                          playButton.setText("Play");
                                          Song.queue.clear();
                                          for (int i = 0; i < playlist.getSongs().size(); i++) {
                                               Song.queue.add(playlist.getSongs().get(i));
                                               
                                               System.out.println(playlist.getSongs().get(i).getName());

                                          }
                                    }
                              } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
                                    e.printStackTrace();
                              }
                        }
                  });
                  songBox.setLeft(songInfo);
                  songBox.setRight(playButton);
                  playlistContent.getChildren().add(songBox);
            }
      }

      public void renderPlaylistsOnSidebar() throws SQLException {

            ArrayList<Playlist> playlists = new ArrayList<>();
    
            Connection connection = Main.connect();
            Statement statement = connection.createStatement();
            ResultSet rs = statement
                    .executeQuery("select * from playlists where creatorid = '" + User.getCurrentUser().getId() + "'");
    
            while(rs.next()) {
                playlists.add(Playlist.getPlaylistById(rs.getInt("id")));
            }
    
            rs.close();
            connection.close();
            for (int i = 0; i < playlists.size(); i++) {
                displayPlaylist(playlists.get(i));
            }
        }

        public void displayPlaylist(Playlist playlist) throws SQLException {
            Button newPlaylist = new Button(playlist.getPlaylistName());
            playlistBox.getChildren().add(newPlaylist);
            newPlaylist.setOnMouseClicked(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent arg0) {
                    try {
                        Playlist.setCurrenPlaylistId(playlist.getPlaylistID());
                        goToPlaylist(arg0);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
            newPlaylist.setFont(Font.font("Times New Roman", 16));
            newPlaylist.setPrefSize(153, 30);
    
        }
        public void setName () throws SQLException {
            playlistName.setText(Playlist.getPlaylistById(Playlist.getCurrenPlaylistId()).getPlaylistName());;
        }
        public void goToPlaylist(MouseEvent event) throws Exception {
            Parent root = FXMLLoader.load(getClass().getResource("playlistPage.fxml"));
            stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        }
        public void goToEdit(MouseEvent event) throws Exception {
            Parent root = FXMLLoader.load(getClass().getResource("editPlaylist.fxml"));
            stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        }
        public void playNextSong() throws UnsupportedAudioFileException, IOException, LineUnavailableException, SQLException {
           searchpageController.arrangeSong(Song.random());
        }
        public void deletePlaylist(MouseEvent event) throws SQLException{
            Playlist.getPlaylistById(Playlist.getCurrenPlaylistId()).deletePlaylist(Playlist.getCurrenPlaylistId());
        }
      public void goToProfile(MouseEvent event) throws Exception {
            Parent root = FXMLLoader.load(getClass().getResource("profilePage.fxml"));
            stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
      }

      public void goToCommunityHub(MouseEvent event) throws Exception {
            Parent root = FXMLLoader.load(getClass().getResource("communityhubPage.fxml"));
            stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
      }
      public void setUsername() {
        otherUsername.setText("@" + User.profileUser.getUsername());
    }

      @Override
      public void initialize(URL location, ResourceBundle resources) {
            try {
                  renderPlaylistsOnSidebar();
                  renderPlaylist();
                  setName();
            } catch (Exception e) {
                  System.out.println("Error");
                  e.printStackTrace();
            }
      }
      
}
