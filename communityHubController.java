import javafx.collections.ListChangeListener;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Polygon;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollBar;
import javafx.scene.control.ScrollPane;
import javafx.stage.Stage;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.ResourceBundle;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

import javafx.scene.Node;

public class communityHubController implements Initializable {
    private Stage stage;
    private Scene scene;
    private Parent root;
    private static int playlistNoNCounter = 1;
    private static int countPlay = 0;
    private static Playlist playlistOnScreen;
    private ArrayList<Song> queue = new ArrayList<>();
    @FXML
    private FlowPane flowPane;
    @FXML
    private Text rock;
    @FXML
    private Text pop;
    @FXML
    private Text indie;
    @FXML
    private Text classical;
    @FXML
    private Text jazz;
    @FXML
    private Text metal;
    @FXML
    private Text alternative;
    @FXML
    private Text hiphop;
    @FXML
    private Text RandB;
    @FXML
    private Text country;
    @FXML
    private Text songNameText;
    @FXML
    private VBox playlistBox;   
    @FXML
    private Text artistText;
    @FXML
    private Polygon backButton; 
    @FXML
    private Polygon nextButton;

    public void playNextSong()
            throws UnsupportedAudioFileException, IOException, LineUnavailableException, SQLException {
        searchpageController.arrangeSong(Song.random());
        setSongNameText(Song.currentlyPlayingSong);
        setSongArtist(Song.currentlyPlayingSong);
    }

    public void arrangeSongOnCommunityHub(MouseEvent event)
            throws UnsupportedAudioFileException, IOException, LineUnavailableException, SQLException {
        setSongNameText(Song.currentlyPlayingSong);
        setSongArtist(Song.currentlyPlayingSong);
        searchpageController.arrangeSong(Song.currentlyPlayingSong);
        setSongNameText(Song.currentlyPlayingSong);
        setSongArtist(Song.currentlyPlayingSong);

    }
    
    public void setName () {

    }

    public void goToCommunity(MouseEvent event) throws Exception {
        Text clickedText = (Text) event.getSource();
        Community.setCurrentCommunityId(Integer.parseInt(((Node) clickedText).getAccessibleText()));
        int a = Integer.parseInt(((Node) clickedText).getAccessibleText());
        System.out.println(a);
        goCommunity(event);

    }

    public void setSongNameText(Song song) {
        songNameText.setText(song.getSongNameWithSpaces());
    }

    public void setSongArtist(Song song) {
        artistText.setText(song.getArtistNameWithSpaces());
    }

    public void goCommunity(MouseEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("postPage.fxml"));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();

    }

    public void goToPlaylist(MouseEvent event) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("playlistPage.fxml"));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
    
    public void goToCreatePlaylist(MouseEvent event) throws Exception {
        Playlist.setCurrenPlaylistId(Playlist.createPlaylist(User.getCurrentUser(), "Playlist", "efuhjıdfsjjd").getPlaylistID());
        Parent root = FXMLLoader.load(getClass().getResource("createPlaylist2.fxml"));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();

    }

    public void goToProfile(MouseEvent event) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("profilePage.fxml"));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    public void goToSearchPage(MouseEvent event) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("searchpage.fxml"));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
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

    public void createPlaylist1(MouseEvent event) throws SQLException {
        Playlist.createPlaylist(User.getCurrentUser(), "Playlist", "efuhjıdfsjjd");
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
            renderPlaylistsOnSidebar();
        } catch (SQLException e) {
            System.out.println("Error in rendering messages: SQLException");
            e.printStackTrace();
        }
    }
    public Song getCurrentlyPlayingSong() throws SQLException{
        ArrayList<Song> allSongs = Song.getAllSongs();
        for (int i = 0; i < allSongs.size(); i++) {
            if(allSongs.get(i).getIsPlaying() == true){
                return allSongs.get(i);
            }
        }
        // otherwise return the first song as default
        return allSongs.get(0);
    }
}
