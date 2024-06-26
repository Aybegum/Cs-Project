import javafx.collections.ListChangeListener;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.FlowPane;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Polygon;
import javafx.scene.text.Font;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import javafx.scene.Parent;
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

import javafx.scene.text.Text;
import javafx.scene.Node;

public class communityPostPageController implements Initializable{
    private Stage stage; 
    private Scene scene;
    private Parent root;
    @FXML
    private FlowPane flowPane;
    @FXML
    private Text communityText;
    @FXML
    private Polygon backButton; 
    @FXML
    private Polygon nextButton;
    @FXML
    private Text artistText;
    @FXML
    private Text songNameText;
    private static int playlistNoNCounter = 1;
    private static int countPlay = 0;
    private static Playlist playlistOnScreen;
    
public void goToPost(MouseEvent  event) throws Exception{
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
    public void goToProfile(MouseEvent  event) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("profilePage.fxml"));
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
                        playlistNoNCounter++;
                  }
            }
            displayPlaylist();
              
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
            }
      }


      public void displayPlaylist()throws SQLException{
            Button newPlaylist = new Button("Playlist");
            newPlaylist.setFont(Font.font("Times New Roman", 16));
            newPlaylist.setPrefSize(154,29);
            flowPane.getChildren().add(newPlaylist);
            flowPane.getChildren().addListener(new ListChangeListener<Node>() {
                  @Override
                  public void onChanged(Change<? extends Node> c) {
                      while (c.next()) {
                          if (c.wasAdded()) {
                              flowPane.setPrefHeight(flowPane.getHeight() + 50);
                          }
                      }
                  }
              });
      }
      public void createPlaylist1 (MouseEvent event) throws SQLException{
            Playlist.createPlaylist(User.getCurrentUser(), "Playlist", "efuhjıdfsjjd");
            renderPlaylistsOnSidebar(event);
        } 
        public void setSongNameText(Song song) {
            songNameText.setText(song.getSongNameWithSpaces());
        }
    
        public void setSongArtist(Song song) {
            artistText.setText(song.getArtistNameWithSpaces());
        }
        public void playNextSong()
            throws UnsupportedAudioFileException, IOException, LineUnavailableException, SQLException {
        searchpageController.arrangeSong(Song.random());
        setSongNameText(Song.currentlyPlayingSong);
        setSongArtist(Song.currentlyPlayingSong);
    }
        @Override
    public void initialize(URL location, ResourceBundle resources) {
        if (Song.currentlyPlayingSong == null) {
            songNameText.setText(" ");
            artistText.setText(" ");
        } else {
            setSongNameText(Song.currentlyPlayingSong);
            setSongArtist(Song.currentlyPlayingSong);
        }
        try {
            communityText.setText(Community.getCommunityById(Community.getCurrentCommunityId()).getName());
            renderPlaylistsOnSidebar();
        } catch (SQLException e) {
            System.out.println("Error in rendering messages: SQLException");
        }
    }
}
