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
import java.sql.SQLException;
import java.util.ArrayList;

import org.w3c.dom.Text;
import javafx.scene.Node;
public class chatroomController {
    private Stage stage;
    private Scene scene;
    private Parent root;
    

    public void renderMessages() throws SQLException {

        ArrayList<Message> messages = Message.getMessages();

        for (Message message : messages) {
            if(message.isSentByCurrentUser()) {
                //TODO: Render on the right
            } else {
                //TODO: Render on the left
            }
        }
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
    public void postMessage(){
        String text = messageField.getText(); //TODO: Add a reference to the text field
        Message.createMessage(User.getCurrentUser().getId(), , text)
    }
    public void deleteMessage(){
        
    }
}

