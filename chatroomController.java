import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.input.MouseEvent;
import javafx.scene.shape.Circle;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.fxml.FXMLLoader;
import java.io.IOException;
import org.w3c.dom.Text;
import javafx.scene.Node;
public class chatroomController {
    private Stage stage;
    private Scene scene;
    private Parent root;
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
    public void postMessage(){
        
    }
    public void deleteMessage(){
        
    }
}
