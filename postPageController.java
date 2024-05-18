import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.PopupWindow;
import javafx.stage.Stage;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
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

import java.sql.SQLException;
import javafx.scene.Node;
import javafx.stage.PopupWindow;

public class postPageController implements Initializable {
    private Stage stage;
    private Scene scene;
    private Stage popupStage = new Stage();;
    private Stage popupStageSong = new Stage();;
    private Parent root;
    @FXML
    private TextField postText;
    @FXML
    private TextField postTitle;
    @FXML
    private AnchorPane close;
    @FXML
    private FlowPane postPane;
    @FXML
    private VBox playlistBox;
    @FXML
    private Text communityText;
    private static Post newPost;
    private static int songCounter = 1;
    @FXML
    private TextField searchBarTextField;
    @FXML
    private Rectangle rectaanglee;
    @FXML
    private VBox songsFlowPane;
    @FXML
    private TextField searchSongTextField;
    @FXML
    private VBox songsFlowPane2;
    @FXML
    private Polygon playNext;
    private static int playlistNoCounter = 1;
    @FXML
    private Text artistText;
    @FXML
    private Text songNameText;

    public void renderPost() throws Exception {
        ArrayList<Post> posts = Post.getPostsByCommunityId(Community.getCurrentCommunityId());
        for (Post post : posts) {
            String text = post.getText();
            if (post.getImageUrl() == null && post.getPlaylistId() < 1 && post.getSongId() < 1) {
                // render text
                Pane textPane = postText(post);
                postPane.setPrefHeight(postPane.getPrefHeight() + textPane.getPrefHeight());
                postPane.getChildren().add(textPane);
            } else if (post.getImageUrl() != null) {
                // render image
                Pane imaPane = postImage(post.getImageUrl().toString());
                postPane.setPrefHeight(postPane.getPrefHeight() + imaPane.getPrefHeight());
                postPane.getChildren().add(imaPane);

            } else if (post.getPlaylistId() > 0) {
                // render playlist
                Pane playlistPane = renderPlaylist(post);
                postPane.setPrefHeight(postPane.getPrefHeight() + playlistPane.getPrefHeight());
                postPane.getChildren().add(playlistPane);

            } else if (post.getSongId() > 0) {
                // render song
                Pane songPane = renderSong(post);
                postPane.setPrefHeight(postPane.getPrefHeight() + songPane.getPrefHeight());
                postPane.getChildren().add(songPane);

            }
        }
    }

    public void playNextSong(MouseEvent event) throws Exception {
        searchpageController.arrangeSong(Song.random());
        setSongNameText(Song.currentlyPlayingSong);
        setSongArtist(Song.currentlyPlayingSong);
    }

    public void renderPost(MouseEvent event) throws SQLException {
        String text = postText.getText();
        String currentPost = ((Node) event.getSource()).getAccessibleText();
        if (newPost.getImageUrl() == null && newPost.getPlaylistId() < 1 && newPost.getSongId() < 1) {
            Post.createTextPost(text, User.getCurrentUser().getId(), Community.getCurrentCommunityId(),
                    postTitle.getText());
            // render text
            Pane textPane = postText(text);
            postPane.setPrefHeight(postPane.getPrefHeight() + textPane.getPrefHeight());
            postPane.getChildren().add(textPane);
        } else if (newPost.getImageUrl() != null) {
            Post.createImagePost(newPost.getImageUrl(), User.getCurrentUser().getId(),
                    Community.getCurrentCommunityId(), postTitle.getText());
            // render image
            Pane imaPane = postImage(newPost.getImageUrl().toString());
            postPane.setPrefHeight(postPane.getPrefHeight() + imaPane.getPrefHeight());
            postPane.getChildren().add(imaPane);

        } else if (newPost.getPlaylistId() > -1) {
            Post.createPlaylistPost(newPost.getPlaylistId(), User.getCurrentUser().getId(),
                    Community.getCurrentCommunityId(), postTitle.getText());
            // render playlist
            Pane playlistPane = postPlaylist(newPost);
            postPane.setPrefHeight(postPane.getPrefHeight() + playlistPane.getPrefHeight());
            postPane.getChildren().add(playlistPane);

        } else if (newPost.getSongId() > -1) {
            Post.createSongPost(newPost.getSongId(), User.getCurrentUser().getId(), Community.getCurrentCommunityId(),
                    postTitle.getText());
            // render song
            Pane songPane = postSong(newPost);
            postPane.setPrefHeight(postPane.getPrefHeight() + songPane.getPrefHeight());
            postPane.getChildren().add(songPane);

        }
        postText.setText("");
        postTitle.setText("");
        newPost.setImageUrl(null);
        newPost.setText(null);
        newPost.setSongId(-1);
        newPost.setPlaylistId(-1);

    }

    public Pane postPlaylist(Post post) throws SQLException {
        close.setVisible(true);
        VBox playlists = new VBox();
        Text info = new Text(
                "\"" + postTitle.getText() + "\"" + " from the user @" + User.getCurrentUser().getUsername());
        info.setFont(Font.font("Times New Roman", 14));
        info.setFill(Color.WHITE);
        playlists.getChildren().addAll(info);
        Button playlistButton = new Button("" + Playlist.getPlaylistById(post.getPlaylistId()).getPlaylistName());
        playlistButton.setFont(Font.font("Times New Roman", 16));
        playlistButton.setPrefSize(playlistButton.getText().length() * 10, 50);
        playlistButton.setOnMouseClicked(e -> {
            try {
                goToPlaylist(Playlist.getPlaylistById(post.getPlaylistId()), e);
            } catch (IOException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            } catch (SQLException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            }
        });
        playlists.getChildren().add(playlistButton);
        playlists.setAlignment(Pos.CENTER);
        playlists.setStyle("-fx-background-color: #053c75; -fx-padding: 5px; -fx-background-radius: 5px;");
        return playlists;

    }

    public Pane renderPlaylist(Post post) throws SQLException {
        close.setVisible(true);
        VBox playlists = new VBox();
        Text info = new Text(
                "\"" + post.getTitle() + "\"" + " from the user @" + User.getById(post.getId()).getUsername());
        info.setFont(Font.font("Times New Roman", 14));
        info.setFill(Color.WHITE);
        playlists.getChildren().addAll(info);
        Button playlistButton = new Button("" + Playlist.getPlaylistById(post.getPlaylistId()).getPlaylistName());
        playlistButton.setFont(Font.font("Times New Roman", 16));
        playlistButton.setOnMouseClicked(e -> {
            try {
                goToPlaylist(Playlist.getPlaylistById(post.getPlaylistId()), e);
            } catch (IOException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            } catch (SQLException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            }
        });
        playlistButton.setPrefSize(playlistButton.getText().length() * 10, 50);
        playlists.getChildren().add(playlistButton);
        playlists.setAlignment(Pos.CENTER);
        playlists.setStyle("-fx-background-color: #053c75; -fx-padding: 5px; -fx-background-radius: 5px;");
        return playlists;

    }

    public void goToPlaylist(Playlist playlist, MouseEvent event) throws IOException {
        Playlist.setCurrenPlaylistId(playlist.getPlaylistID());
        Parent root = FXMLLoader.load(getClass().getResource("playlistPage.fxml"));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();

    }

    public Pane postSong(Post post) throws SQLException {
        close.setVisible(true);
        VBox songs = new VBox();
        Text info = new Text(
                "\"" + postTitle.getText() + "\"" + " from the user @" + User.getCurrentUser().getUsername());
        info.setFont(Font.font("Times New Roman", 14));
        info.setFill(Color.WHITE);
        songs.getChildren().addAll(info);
        Button songButton = new Button(Song.getSongById(post.getSongId()).getName() + " - "
                + Song.getSongById(post.getSongId()).getArtist());
        songButton.setFont(Font.font("Times New Roman", 16));
        songButton.setOnMouseClicked(e -> {
            try {
                try {
                    searchpageController.arrangeSong(Song.getSongById(post.getSongId()));
                } catch (SQLException e1) {
                    // TODO Auto-generated catch block
                    e1.printStackTrace();
                }
            } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            }
        });
        songButton.setPrefSize(songButton.getText().length() * 10, 50);
        songs.getChildren().add(songButton);
        songs.setAlignment(Pos.CENTER);
        songs.setStyle("-fx-background-color: #053c75; -fx-padding: 5px; -fx-background-radius: 5px;");
        return songs;
    }

    public Pane renderSong(Post post) throws SQLException {
        close.setVisible(true);
        VBox songs = new VBox();
        Text info = new Text(
                "\"" + post.getTitle() + "\"" + " from the user @" + User.getById(post.getId()).getUsername());
        info.setFont(Font.font("Times New Roman", 14));
        info.setFill(Color.WHITE);
        songs.getChildren().addAll(info);
        Button songButton = new Button(Song.getSongById(post.getSongId()).getName() + " - "
                + Song.getSongById(post.getSongId()).getArtist());
        songButton.setOnMouseClicked(e -> {
            try {
                try {
                    searchpageController.arrangeSong(Song.getSongById(post.getSongId()));
                } catch (SQLException e1) {
                    // TODO Auto-generated catch block
                    e1.printStackTrace();
                }
            } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            }
        });
        songButton.setFont(Font.font("Times New Roman", 16));
        songButton.setPrefSize(songButton.getText().length() * 10, 50);
        songs.getChildren().add(songButton);
        songs.setAlignment(Pos.CENTER);
        songs.setStyle("-fx-background-color: #053c75; -fx-padding: 5px; -fx-background-radius: 5px;");
        return songs;
    }
    public void goToCreatePlaylist(MouseEvent event) throws Exception {
        Playlist.setCurrenPlaylistId(Playlist.createPlaylist(User.getCurrentUser(), "Playlist", "efuhjıdfsjjd").getPlaylistID());
        Parent root = FXMLLoader.load(getClass().getResource("createPlaylist2.fxml"));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();

    }

    public Pane postText(String post) {
        VBox postBox = new VBox();
        HBox titleBox = new HBox();
        HBox textBox = new HBox();
        titleBox.setSpacing(5);
        textBox.setSpacing(5);
        titleBox.setAlignment(Pos.CENTER);
        textBox.setAlignment(Pos.CENTER);
        postBox.setSpacing(5);
        postBox.setAlignment(Pos.CENTER);
        Text info = new Text(
                "\"" + postTitle.getText() + "\"" + " from the user @" + User.getCurrentUser().getUsername());
        info.setFont(Font.font("Times New Roman", 14));
        info.setFill(Color.WHITE);
        postBox.getChildren().add(info);

        Text postText = new Text(post);
        postText.setFont(Font.font("Times New Roman", 14));
        postText.setFill(Color.BLACK);
        textBox.getChildren().add(postText);
        textBox.setStyle("-fx-background-color: #b4bfc9; -fx-padding: 5px; -fx-background-radius: 5px;");

        Text title = new Text(postTitle.getText());
        title.setFill(Color.BLACK);
        title.setFont(Font.font("Times New Roman", 16));
        titleBox.getChildren().addAll(title);
        titleBox.setStyle("-fx-background-color: #b4bfc9; -fx-padding: 5px; -fx-background-radius: 5px;");
        Text space = new Text("");
        space.setFont(Font.font("Times New Roman", 5));
        postBox.getChildren().addAll(titleBox, space, textBox);
        postBox.setStyle("-fx-background-color: #053c75; -fx-padding: 5px; -fx-background-radius: 5px;");

        return postBox;
    }

    public Pane postText(Post post) throws Exception {
        VBox postBox = new VBox();
        HBox titleBox = new HBox();
        HBox textBox = new HBox();
        titleBox.setSpacing(5);
        textBox.setSpacing(5);
        titleBox.setAlignment(Pos.CENTER);
        textBox.setAlignment(Pos.CENTER);
        postBox.setSpacing(5);
        postBox.setAlignment(Pos.CENTER);
        Text info = new Text(
                "\"" + post.getTitle() + "\"" + " from the user @" + User.getById(post.getId()).getUsername());
        info.setFont(Font.font("Times New Roman", 14));
        info.setFill(Color.WHITE);
        postBox.getChildren().add(info);

        Text postText = new Text(post.getText());
        postText.setFont(Font.font("Times New Roman", 14));
        postText.setFill(Color.BLACK);
        textBox.getChildren().add(postText);
        textBox.setStyle("-fx-background-color: #b4bfc9; -fx-padding: 5px; -fx-background-radius: 5px;");

        Text title = new Text(post.getTitle());
        title.setFill(Color.BLACK);
        title.setFont(Font.font("Times New Roman", 16));
        titleBox.getChildren().addAll(title);
        titleBox.setStyle("-fx-background-color: #b4bfc9; -fx-padding: 5px; -fx-background-radius: 5px;");
        Text space = new Text("");
        space.setFont(Font.font("Times New Roman", 5));
        postBox.getChildren().addAll(titleBox, space, textBox);
        postBox.setStyle("-fx-background-color: #053c75; -fx-padding: 5px; -fx-background-radius: 5px;");

        return postBox;
    }

    public Pane postImage(String selectedFile) {
        VBox imageBox = new VBox();
        imageBox.setAlignment(Pos.CENTER);
        Text title = new Text("\"" + postTitle.getText() + "\"" + " from @" + User.getCurrentUser().getUsername());
        title.setFill(Color.WHITE);
        title.setFont(Font.font("Times New Roman", 16));
        if (selectedFile != null) {
            Image image = new Image(selectedFile);
            System.out.println(selectedFile);
            ImageView imageView = new ImageView(image);
            imageBox.getChildren().addAll(title, imageView);
            imageBox.setStyle("-fx-background-color: #053c75; -fx-padding: 5px; -fx-background-radius: 5px;");
        }
        return imageBox;
    }

    public Pane postImage(Post post) throws SQLException {
        VBox imageBox = new VBox();
        File selectedFile = new File(post.getImageUrl());
        imageBox.setAlignment(Pos.CENTER);
        Text title = new Text("\"" + post.getTitle() + "\"" + " from @" + User.getById(post.getId()).getUsername());
        title.setFill(Color.WHITE);
        title.setFont(Font.font("Times New Roman", 16));
        if (selectedFile != null) {
            Image image = new Image(selectedFile.toURI().toString());
            System.out.println(selectedFile);
            ImageView imageView = new ImageView(image);
            imageBox.getChildren().addAll(title, imageView);
            imageBox.setStyle("-fx-background-color: #053c75; -fx-padding: 5px; -fx-background-radius: 5px;");
        }
        return imageBox;
    }

    public void closeClose(MouseEvent event) {
        close.setVisible(false);
    }

    public void imageButton(MouseEvent event) throws Exception {
        close.setVisible(true);
        newPost.setText(null);
        newPost.setSongId(-1);
        newPost.setPlaylistId(-1);
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Choose Image");
        File selectedFile = fileChooser.showOpenDialog(stage);
        newPost.setImageUrl(selectedFile.toURI().toString());
    }

    public void playlistButton(MouseEvent event) throws Exception {
        close.setVisible(true);
        openPopup();
    }

    private void openPopup() throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("playlistSearch.fxml"));
        Scene scene = new Scene(root);
        popupStage.setScene(scene);
        popupStage.show();
    }

    public void renderPlaylist(MouseEvent event) throws Exception {

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
            Button shareButton = new Button("Select");
            shareButton.setFont(Font.font("Times New Roman", 16));
            shareButton.setPrefSize(100, 30);
            shareButton.setOnMouseClicked(e -> {
                newPost.setText(null);
                newPost.setSongId(-1);
                newPost.setPlaylistId(playlist.getPlaylistID());
                newPost.setImageUrl(null);
            });
            playlistHBox.getChildren().addAll(playlistName, shareButton);
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

    public void songButton(MouseEvent event) throws Exception {
        close.setVisible(true);
        openPopupSong();
    }

    public void openPopupSong() throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("songsSearch.fxml"));
        Scene scene = new Scene(root);
        popupStageSong.setScene(scene);
        popupStageSong.show();
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
                String url = rs.getString("url");
                Song song = new Song(id, songTitle, artist, url);
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

    public void arrangeSong(Song song) throws UnsupportedAudioFileException, IOException, LineUnavailableException {
        String filePath = song.getUrl().substring(39);
        File file = new File(filePath);
        AudioInputStream audioStream = AudioSystem.getAudioInputStream(file);

        Clip clip = song.getClip();
        if (clip == null) {
            clip = AudioSystem.getClip();
            clip.open(audioStream);
            song.setClip(clip);
        }

        if (Song.getCurrentlyPlayingClip() != null && Song.getCurrentlyPlayingClip().isRunning()
                && Song.getCurrentlyPlayingClip() != clip) {
            Song.getCurrentlyPlayingClip().stop();
            Song.getCurrentlyPlayingsSong().setIsPlaying(false);
        }
        if (song.getIsPlaying()) {
            song.setPausedPosition(clip.getMicrosecondPosition());
            clip.stop();
            song.setIsPlaying(false);
        } else {
            clip.setMicrosecondPosition(song.getPausedPosition());
            clip.start();
            song.setIsPlaying(true);
        }
        Song.currentlyPlayingSong = song;
        Song.currentlyPlayingClip = clip;
    }

    public void renderSongs(MouseEvent event) throws Exception {
        ArrayList<Song> searchedSongs = new ArrayList<>();

        searchedSongs = searchSongsByNameorArtist(searchSongTextField.getText());
        for (Song song : searchedSongs) {
            HBox songHBox = new HBox();
            Text songInfo = new Text(songCounter + "- " + song.getName() + " - " + song.getArtist()
                    + "                                          ");
            Button shareButton = new Button("Select");
            shareButton.setFont(Font.font("Times New Roman", 16));
            shareButton.setPrefSize(30, 30);
            shareButton.setOnMouseClicked(e -> {
                try {
                    searchpageController.arrangeSong(song);
                } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e1) {
                    // TODO Auto-generated catch block
                    e1.printStackTrace();
                }
                newPost.setText(null);
                newPost.setSongId(song.getID());
                System.out.println(song.getID());
                newPost.setPlaylistId(-1);
                newPost.setImageUrl(null);
                popupStageSong.close();
            });

            songHBox.getChildren().addAll(songInfo, shareButton);
            songsFlowPane2.getChildren().add(songHBox);
            songsFlowPane2.setVisible(true);
            songCounter++;
        }

    }
    // ----------------------------------------------------------------------------------------------------------

    public void goToChatroom(MouseEvent event) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("chatroomPage.fxml"));
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

    public void goToProfile(MouseEvent event) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("profilePage.fxml"));
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

        while (rs.next()) {
            playlists.add(Playlist.getPlaylistById(rs.getInt("id")));
        }

        rs.close();
        connection.close();
        for (int i = 0; i < playlists.size(); i++) {
            System.out.println("AAAAAAAAAAAAAAAAAAAAA\n\n");
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

    public void setPostPageName() {
        communityText.setText(Community.getCommunityById(Community.getCurrentCommunityId()).getName()
                + " Community");
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        /* if (Song.currentlyPlayingSong == null) {
            songNameText.setText(" ");
            artistText.setText(" ");
        } else {
            setSongNameText(Song.currentlyPlayingSong);
            setSongArtist(Song.currentlyPlayingSong);
        } */
        try {
            close.setVisible(false);
            newPost = new Post(-1, null, null, null, -1, -1, -1, -1);
            setPostPageName();
            renderPost();
            renderPlaylistsOnSidebar();
            ObservableList<String> options = FXCollections.observableArrayList("User", "Song", "Playlist");
            if (Song.currentlyPlayingSong == null) {
                songNameText.setText(" ");
                artistText.setText(" ");
            } else {
                setSongNameText(Song.currentlyPlayingSong);
                setSongArtist(Song.currentlyPlayingSong);
            }

        } catch (Exception e) {
            System.out.println("Error in rendering messages: SQLException");
            e.printStackTrace();
        }
    }
    public void setSongNameText(Song song) {
        songNameText.setText(song.getSongNameWithSpaces());
    }

    public void setSongArtist(Song song) {
        artistText.setText(song.getArtistNameWithSpaces());
    }
}
