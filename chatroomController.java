import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollBar;
import javafx.scene.control.TextField;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;

public class PleaseProvideControllerClassName {

    @FXML
    private Rectangle backButtonRectangle;

    @FXML
    private Polygon backButtonTriangle;

    @FXML
    private Rectangle chatroomBox;

    @FXML
    private Text chatroomText;

    @FXML
    private Text communityText;

    @FXML
    private Rectangle createPlaylist;

    @FXML
    private Text createPlaylistRectangle;

    @FXML
    private TextField enterYourMessageTextField;

    @FXML
    private Rectangle likedSongs;

    @FXML
    private Text likedSongsPlaylistRectangle;

    @FXML
    private Polygon nextSongTriangle;

    @FXML
    private Circle playMusicCircle;

    @FXML
    private Polygon playMusicTriangle;

    @FXML
    private ScrollBar playlistScrollBarr;

    @FXML
    private Text playlistsText;

    @FXML
    private Rectangle postsBox;

    @FXML
    private Text postsText;

    @FXML
    private Polygon previousSongTriangle;

    @FXML
    private Button sendChatButton;
    public void goToCommunityHub(ActionEvent event) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("communityhub.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
    public void goToProfile(ActionEvent event) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("profilePage.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
    public void goToCommunityPosts(ActionEvent event) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("postPage.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
}
