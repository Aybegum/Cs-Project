import javafx.fxml.FXML;
import javafx.event.ActionEvent ;
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

public class backButton {
    private Stage stage;
    private Scene scene;
    private Parent root;
    private Scene preScene;
    public void getPreviousScene(Scene preScene) {
        this.preScene = preScene;
    }

    void clickPreviousScene (MouseEvent event) throws IOException {
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        stage.setScene(preScene);
        stage.show();
    }

}
