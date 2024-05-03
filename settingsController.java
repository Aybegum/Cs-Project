import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;

public class PleaseProvideControllerClassName {

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

