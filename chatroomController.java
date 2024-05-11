import javafx.event.ActionEvent ;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent.VerticalTextScrollUnits;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.scene.control.Button;
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
import java.sql.SQLException;
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
        renderMessage(message);
    }
}

    public void renderMessage(Message message) throws SQLException {
        String name = User.getById(message.getSenderId()).getUsername();
        if (message.isSentByCurrentUser()) {
            Pane messagePane = createMessagePane(name,message.getBody(), "right");
            chatPane.getChildren().add(messagePane);
        } else {
            Pane messagePane = createMessagePane(name,message.getBody(), "left");
            chatPane.getChildren().add(messagePane);
        }
    }
private Pane createMessagePane(String userName, String text, String alignment) throws SQLException{
    // Create an HBox to hold the text
    HBox messageBox = new HBox();
    messageBox.setSpacing(5); 
    messageBox.setAlignment(Pos.CENTER); 


    // Create the text node
    Text messageText = new Text(text);
    messageText.setFont(Font.font("Times New Roman", 14));
    messageText.setWrappingWidth(0);

    Text name = new Text("@"+userName);
    name.setFont(Font.font("Times New Roman", 12));
    //nameBox.getChildren().add(name);

    messageBox.getChildren().add(messageText);
    FlowPane messagePane = new FlowPane();
    FlowPane messageP = new FlowPane();
    messagePane.setPrefWidth(400); 
    messageP.setPrefHeight(52); 
    messagePane.setPrefHeight(0); 

    if ("right".equals(alignment)) {
        messageText.setFill(Color.WHITE);
        name.setFill(Color.BLACK);
        messageBox.setStyle("-fx-background-color: #053c75; -fx-padding: 5px; -fx-background-radius: 5px;");
        messageP.getChildren().add(messageBox); 
        messageP.getChildren().add(name);
        messageP.setOrientation(Orientation.VERTICAL);
        messagePane.getChildren().add(messageP);
        messageP.setAlignment(Pos.CENTER_RIGHT); 
        messagePane.setAlignment(Pos.CENTER_RIGHT); 
    } else {
        messageText.setFill(Color.BLACK);
        name.setFill(Color.BLACK);
        messageBox.setStyle("-fx-background-color: #b4bfc9; -fx-padding: 5px; -fx-background-radius: 5px;");
        messageP.getChildren().add(messageBox); 
        messageP.getChildren().add(name);
        messageP.setOrientation(Orientation.VERTICAL);
        messagePane.getChildren().add(messageBox);
        messagePane.getChildren().add(messageP);
        messagePane.setAlignment(Pos.CENTER_LEFT);
        messageP.setAlignment(Pos.CENTER_LEFT);  
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
}

