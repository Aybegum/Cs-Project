import javafx.collections.ListChangeListener;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent.VerticalTextScrollUnits;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.geometry.VerticalDirection;
import javafx.fxml.*;
import javafx.geometry.Pos;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
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

public class chatroomController implements Initializable {
    private Stage stage;
    private Scene scene;
    private Parent root;
    @FXML
    private FlowPane chatPane;
    @FXML
    private TextField messageField;
    @FXML
    private Text communityText;
    @FXML
    private VBox playlistBox;
    @FXML
    private Text artistText;
    @FXML
    private Text songNameText;

    public void playNextSong()
            throws UnsupportedAudioFileException, IOException, LineUnavailableException, SQLException {
        searchpageController.arrangeSong(Song.random());
        setSongNameText(Song.currentlyPlayingSong);
        setSongArtist(Song.currentlyPlayingSong);
    }

    public void renderMessages() throws SQLException {

        ArrayList<Message> messages = Message.getMessages(Community.getCurrentCommunityId());
        for (Message message : messages) {
            renderMessage(message);
        }
    }

    public void renderMessage(Message message) throws SQLException {
        String name = User.getById(message.getSenderId()).getUsername();
        if (message.isSentByCurrentUser()) {
            Pane messagePane = createMessagePane(name, message.getBody(), "right");
            chatPane.getChildren().add(messagePane);
        } else {
            Pane messagePane = createMessagePane(name, message.getBody(), "left");
            chatPane.getChildren().add(messagePane);
        }
    }

    private Pane createMessagePane(String userName, String text, String alignment) throws SQLException {
        // Create an HBox to hold the text
        HBox messageBox = new HBox();
        HBox nameBox = new HBox();
        nameBox.setSpacing(5);
        messageBox.setSpacing(5);
        messageBox.setAlignment(Pos.CENTER);

        // Create the text node
        Text messageText = new Text(text);
        messageText.setFont(Font.font("Times New Roman", 14));
        if (messageText.getText().length() > 100) {
            messageText.setWrappingWidth(200);
        }
        

        Text name = new Text("@" + userName);
        name.setFont(Font.font("Times New Roman", 12));
        // nameBox.getChildren().add(name);
        messageBox.getChildren().add(messageText);
        FlowPane messagePane = new FlowPane();
        FlowPane messageP = new FlowPane();
        messagePane.setPrefWidth(400);
        messageP.setPrefHeight(0);
        messagePane.setPrefHeight(0);
        name.setTextAlignment(TextAlignment.JUSTIFY);

        if ("right".equals(alignment)) {
            messageText.setFill(Color.WHITE);
            name.setFill(Color.BLACK);
            messageBox.setStyle("-fx-background-color: #053c75; -fx-padding: 5px; -fx-background-radius: 5px;");
            messageP.getChildren().add(messageBox);
            messageP.getChildren().add(name);
            messageP.setOrientation(Orientation.VERTICAL);
            messagePane.getChildren().add(messageBox);
            // messagePane.getChildren().add(nameBox);
            messagePane.getChildren().add(messageP);
            messageP.setAlignment(Pos.CENTER_RIGHT);
            messagePane.setAlignment(Pos.CENTER_RIGHT);
        } else {
            messageText.setFill(Color.BLACK);
            name.setFill(Color.BLACK);
            messageBox.setStyle("-fx-background-color: #b4bfc9; -fx-padding: 5px; -fx-background-radius: 5px;");
            messageP.getChildren().add(messageBox);
            messageP.getChildren().add(name);
            messageP.setOrientation(Orientation.VERTICAL);
            messagePane.getChildren().add(messageBox);
            // messagePane.getChildren().add(nameBox);
            messagePane.getChildren().add(messageP);
            messagePane.setAlignment(Pos.CENTER_LEFT);
            messageP.setAlignment(Pos.CENTER_LEFT);
        }

        return messagePane;
    }

    public void goToCommunityHub(MouseEvent event) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("communityhubPage.fxml"));
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

    public void goToProfile(MouseEvent event) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("profilePage.fxml"));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    public void goToCommunityPosts(MouseEvent event) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("postPage.fxml"));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    public void postMessage() throws SQLException {
        String text = messageField.getText();
        Message message = Message.createMessage(User.getCurrentUser().getId(), Community.getCurrentCommunityId(), text);
        renderMessage(message);
        messageField.setText("");
    }

    public void setChatroomName() {
        communityText.setText(Community.getCommunityById(Community.getCurrentCommunityId()).getName() + " Community");
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

    public void goToCreatePlaylist(MouseEvent event) throws Exception {
        Playlist.setCurrenPlaylistId(
                Playlist.createPlaylist(User.getCurrentUser(), "Playlist", "efuhjıdfsjjd").getPlaylistID());
        Parent root = FXMLLoader.load(getClass().getResource("createPlaylist2.fxml"));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();

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
            setChatroomName();
            renderMessages();
            renderPlaylistsOnSidebar();
        } catch (SQLException e) {
            System.out.println("Error in rendering messages: SQLException");
        }
    }

    public void goToSearchPage(MouseEvent event) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("searchpage.fxml"));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
    public void setSongNameText(Song song) {
        songNameText.setText(song.getSongNameWithSpaces());
    }

    public void setSongArtist(Song song) {
        artistText.setText(song.getArtistNameWithSpaces());
    }
}
