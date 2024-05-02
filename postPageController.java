import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.input.MouseEvent;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.fxml.FXMLLoader;
import java.io.IOException;
import org.w3c.dom.Text;
import javafx.scene.Node;

public class postPageController {
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
    private Text communityText;

    @FXML
    private Rectangle createPlaylist;

    @FXML
    private Circle playMusic;

    @FXML
    private Circle playMusic1;

    @FXML
    private Circle playMusic11;

    @FXML
    private Rectangle postsBox;
    public void goToChatroom(MouseEvent event) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("logInPage.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
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
}
