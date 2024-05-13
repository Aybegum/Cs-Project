import javafx.collections.ListChangeListener;
import javafx.event.ActionEvent;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent.VerticalTextScrollUnits;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.geometry.VerticalDirection;
import javafx.fxml.*;
import javafx.geometry.Pos;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.ResourceBundle;

import javafx.scene.Node;

public class chatroomController implements Initializable {
    private Stage stage;
    private Scene scene;
    private Parent root;
    @FXML
    private FlowPane chatPane;
    @FXML
    private TextField messageField;

    public void renderMessages() throws SQLException {

        ArrayList<Message> messages = Message.getMessages(Community.getCurrentCommunityId());
        for (Message message : messages) {
            renderMessage(message);
        }
    }

    public void renderMessage(Message message) throws SQLException {
        String name = User.getById(message.getSenderId()).getUsername();
        if (message.isSentByCurrentUser()) {
            Pane messagePane = createMessagePane(name, message.getBody(), "right");
            chatPane.getChildren().add(messagePane);
        } else {
            Pane messagePane = createMessagePane(name, message.getBody(), "left");
            chatPane.getChildren().add(messagePane);
        }
    }
    
    private Pane createMessagePane(String userName, String text, String alignment) throws SQLException {
        // Create an HBox to hold the text
        HBox messageBox = new HBox();
        VBox generalBox = new VBox();
        generalBox.setSpacing(5);
        messageBox.setSpacing(5);
        messageBox.setAlignment(Pos.CENTER);
        generalBox.setAlignment(Pos.CENTER);

        // Create the text node
        Text messageText = new Text(text);
        messageText.setFont(Font.font("Times New Roman", 14));
        messageBox.getChildren().add(messageText);
   /*      if (messageText.getWrappingWidth() > 200) {
            messageText.setWrappingWidth(200);
        } */
        /* Label longText = new Label(messageText.getText());
        longText.setWrapText(true);
 */

        Text name = new Text("@" + userName);
        Text B = new Text("");
        B.setFont(Font.font("Times New Roman", 2));
        name.setFont(Font.font("Times New Roman", 12));
        generalBox.getChildren().addAll(messageBox,name,B);
        

        FlowPane messagePane = new FlowPane();
        FlowPane messageP = new FlowPane();
        messagePane.setPrefWidth(400);
        messageP.setPrefHeight(0);
        messagePane.setPrefHeight(0);
        name.setTextAlignment(TextAlignment.JUSTIFY);

        if ("right".equals(alignment)) {
            generalBox.setAlignment(Pos.CENTER_RIGHT);
            messageText.setFill(Color.WHITE);
            name.setFill(Color.BLACK);
            messageBox.setStyle("-fx-background-color: #053c75; -fx-padding: 5px; -fx-background-radius: 5px;");
            /* messageP.getChildren().add(messageBox);
            messageP.getChildren().add(name);
            messageP.setOrientation(Orientation.VERTICAL);
            messagePane.getChildren().add(messageBox);
            messagePane.getChildren().add(nameBox);
            messagePane.getChildren().add(messageP);
            messageP.setAlignment(Pos.CENTER_RIGHT);
            messagePane.setAlignment(Pos.CENTER_RIGHT); */
            messagePane.getChildren().add(generalBox);
            messagePane.setAlignment(Pos.CENTER_RIGHT); 
        } else {
            generalBox.setAlignment(Pos.CENTER_LEFT);
            messageText.setFill(Color.BLACK);
            name.setFill(Color.BLACK);
            messageBox.setStyle("-fx-background-color: #b4bfc9; -fx-padding: 5px; -fx-background-radius: 5px;");
            /* messageP.getChildren().add(messageBox);
            messageP.getChildren().add(name);
            messageP.setOrientation(Orientation.VERTICAL);
            messagePane.getChildren().add(messageBox);
            // messagePane.getChildren().add(nameBox);
            messagePane.getChildren().add(messageP);
            messagePane.setAlignment(Pos.CENTER_LEFT);
            messageP.setAlignment(Pos.CENTER_LEFT); */
            messagePane.getChildren().add(generalBox);
            messagePane.setAlignment(Pos.CENTER_LEFT);
        }

        return messagePane;
    }

    public void goToCommunityHub(MouseEvent event) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("communityhubPage.fxml"));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    public void goToPlaylist(MouseEvent event) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("playlistPage.fxml"));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    public void goToProfile(MouseEvent event) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("profilePage.fxml"));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    public void goToCommunityPosts(MouseEvent event) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("postPage.fxml"));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    public void postMessage() throws SQLException {
        String text = messageField.getText();
        Message message = Message.createMessage(User.getCurrentUser().getId(), Community.getCurrentCommunityId(), text);
        renderMessage(message);
        messageField.setText("");
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            renderMessages();
        } catch (SQLException e) {
            System.out.println("Error in rendering messages: SQLException");
        }
    }
    public void goToSearchPage(MouseEvent event) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("searchpage.fxml"));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
}
