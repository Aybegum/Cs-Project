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
      root = FXMLLoader.load(getClass().getResource("welcomePage.fxml"));
      stage = (Stage)((Node)event.getSource()).getScene().getWindow();
      scene = new Scene(root);
      stage.setScene(scene);
      stage.show();
    }
    public void goToProfile (MouseEvent event) throws Exception {
      root = FXMLLoader.load(getClass().getResource("profilePage.fxml"));
      stage = (Stage)((Node)event.getSource()).getScene().getWindow();
      scene = new Scene(root);
      stage.setScene(scene);
      stage.show();
    }
    
}

