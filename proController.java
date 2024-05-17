import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class proController implements Initializable {
    private Stage stage;
    private Scene scene;
    private Parent root;
    @FXML
    private Rectangle backButtonRectangle;

    @FXML
    private Polygon backButtonTriangle;

    @FXML
    private Text otherUserFollowers;

    @FXML
    private Text otherUserFollowing;

    @FXML
    private Text otherUsername;

    public void setUsername() {
        otherUsername.setText("@" + User.profileUser.getUsername());
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setUsername();
    }

    public void goToSearchPage(MouseEvent event) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("searchpage.fxml"));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
}
