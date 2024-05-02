import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.input.MouseEvent;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.fxml.FXMLLoader;
import java.io.IOException;
import org.w3c.dom.Text;
import javafx.scene.Node;

public class welcomePageController {
    private Stage stage;
    private Scene scene;
    private Parent root;
    @FXML
    private TextField singUpUsername;
    @FXML
    private TextField singUpPassword;
    @FXML
    private TextField singUpEMail;
    @FXML
    private TextField logInUsername;
    @FXML
    private TextField logInPassword;

    public void logIn (MouseEvent event) throws Exception{
        String userName = logInUsername.getText();
        String passWord = logInPassword.getText();
        System.out.println("Username: "+userName+" Password: "+passWord);
        goToCommunittyHub(event);
    }
    public void singUp (MouseEvent event) throws Exception{
        String userName = singUpUsername.getText();
        String passWord = singUpPassword.getText();
        String eMail = singUpEMail.getText();
        System.out.println("Username: "+userName+"\nPassword: "+passWord + "\nE-mail: "+eMail);
        goToCommunittyHub(event);
    }
    public void goToSingUpPage(MouseEvent event) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("singUpPage.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
    public void goToLogInPage(MouseEvent event) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("logInPage.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    public void goToCommunittyHub(MouseEvent event) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("communityhubPage.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();

    }


 
}