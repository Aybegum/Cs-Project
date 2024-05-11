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
  
    public void saveChanges () {
  
    }
    public void logOut(MouseEvent event) throws Exception{
      //userı null yapmak lazım :)))
      Parent root = FXMLLoader.load(getClass().getResource("welcomePage.fxml"));
      stage = (Stage)((Node)event.getSource()).getScene().getWindow();
      scene = new Scene(root);
      stage.setScene(scene);
      stage.show();
    }
    public void goToProfile (MouseEvent event) throws Exception {
      Parent root = FXMLLoader.load(getClass().getResource("profilePage.fxml"));
      stage = (Stage)((Node)event.getSource()).getScene().getWindow();
      scene = new Scene(root);
      stage.setScene(scene);
      stage.show();
    }
    
    public static void showInvalidPasswordError() {
      Alert alert = new Alert(AlertType.ERROR);
      alert.setTitle("Error");
      alert.setContentText("Password should at least be 8 characters long and should contain at least 1 number, 1 uppercase letter and 1 special character.");
      alert.showAndWait();
  }
  public static void showInvalidUsernameError() {
      Alert alert = new Alert(AlertType.ERROR);
      alert.setTitle("Error");
      alert.setContentText("Username should at least be 3 characters long and cannot contain any spaces or any uppercase letters.");
      alert.showAndWait();
  }

  public static void showNotUniqueUsernameError() {
      Alert alert = new Alert(AlertType.ERROR);
      alert.setTitle("Error");
      alert.setContentText("This username is already taken.");
      alert.showAndWait();
  }
  public static void showInvalidConfirmPasswordError() {
      Alert alert = new Alert(AlertType.ERROR);
      alert.setTitle("Error");
      alert.setContentText("The passwords do not match.");
      alert.showAndWait();
  }
  public static void showInvalidOldPasswordError() {
    Alert alert = new Alert(AlertType.ERROR);
    alert.setTitle("Error");
    alert.setContentText("The current password you entered is wrong.");
    alert.showAndWait();
}
}

