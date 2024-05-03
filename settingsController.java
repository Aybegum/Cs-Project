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
public class settingsController {
    private Stage stage;
    private Scene scene;
    private Parent root;
    @FXML
    private Rectangle arrowPart1;

    @FXML
    private Polygon arrowPart2;

    @FXML
    private Text confirmPaassword;

    @FXML
    private Text logOut;

    @FXML
    private Text oldPasswordText;

    @FXML
    private Circle profilePictureCircle;

    @FXML
    private Button saveChangesButton;

    @FXML
    private Text updatePasswordText;

    @FXML
    private Text updateUsernameText;

}
public void saveChanges () {
  
}
public void logOut() {
  
}
public void goToProfile (ActionEvent event) throws Exception {
  Parent root = FXMLLoader.load(getClass().getResource("profilePage.fxml"));
  stage = (Stage)((Node)event.getSource().getScene.getWindow();
  scene = new Scene (root);
  stage.setScene(scene);
  stage.show();
}

