import javafx.event.ActionEvent ;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.fxml.*;
import javafx.geometry.Pos;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;

import javafx.scene.Node;
public class chatroomController {
    private Stage stage;
    private Scene scene;
    private Parent root;
    @FXML
    private FlowPane chatPane;
    @FXML 
    private TextField messageField;
    

   /* public void renderMessages() throws SQLException {

        ArrayList<Message> messages = Message.getMessages();

        for (Message message : messages) {
            if(message.isSentByCurrentUser()) {
                //TODO: Render on the right
            } else {
                //TODO: Render on the left
            }
        }
    }*/
    public void renderMessages() throws SQLException {

    ArrayList<Message> messages = Message.getMessages();
    for (Message message : messages) {
        if (message.isSentByCurrentUser()) {
            // Render on the right
            Pane messagePane = createMessagePane(message.getBody(), "right");
            chatPane.getChildren().add(messagePane);
        } else {
            // Render on the left
            Pane messagePane = createMessagePane(message.getBody(), "left");
            chatPane.getChildren().add(messagePane);
        }
    }
}
private Pane createMessagePane(String text, String alignment) throws SQLException{
    // Create an HBox to hold the text
    HBox messageBox = new HBox();
    messageBox.setSpacing(10); // Set spacing between elements
    messageBox.setAlignment(Pos.CENTER); // Align text to center horizontally

    // Create the text node
    Text messageText = new Text(text);
    messageText.setFont(Font.font("Times New Roman", 14));

    // Add the text to the HBox
    messageBox.getChildren().add(messageText);

    // Create a Pane to contain the HBox
    FlowPane messagePane = new FlowPane();
    messagePane.setPrefWidth(400); // Set preferred width

    // Set alignment of the HBox within the Pane based on the specified alignment
    if ("right".equals(alignment)) {
        messageText.setFill(Color.WHITE);
        messageBox.setStyle("-fx-background-color: #053c75; -fx-padding: 5px; -fx-background-radius: 5px;");
        messagePane.getChildren().add(messageBox); // Add the HBox to the Pane
        messagePane.setAlignment(Pos.CENTER_RIGHT); // Align the HBox to the right
    } else {
        messageText.setFill(Color.BLACK);
        messageBox.setStyle("-fx-background-color: #b4bfc9; -fx-padding: 5px; -fx-background-radius: 5px;");
        messagePane.getChildren().add(messageBox); // Add the HBox to the Pane
        messagePane.setAlignment(Pos.CENTER_LEFT); // Align the HBox to the left
    }

    return messagePane;
}
    public void goToCommunityHub(MouseEvent  event) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("communityhubPage.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
    public void goToPlaylist(MouseEvent event)throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("playlistPage.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
    public void goToProfile(MouseEvent  event) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("profilePage.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
    public void goToCommunityPosts(MouseEvent  event) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("postPage.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
    public void postMessage()throws SQLException{
        String text = messageField.getText(); 
        Message.createMessage(User.getCurrentUser().getId(), Community.getCurrentCommunityId(), text);
        renderMessages();
        messageField.setText("");
    }
}

