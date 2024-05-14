import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.scene.Node;

public class followingPageController {
    Parent root;
    Stage stage;
    Scene scene;
    public void goToProfile(MouseEvent event) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("profilePage.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
}