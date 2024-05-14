import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Circle;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.fxml.FXMLLoader;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

import org.w3c.dom.Text;
import javafx.scene.Node;
public class profileController {
    private Stage stage;
    private Scene scene;
    private Parent root;
    private int playlistNoCounter = 1;

     public void goToCommunityHub(MouseEvent event) throws Exception{
          Parent root = FXMLLoader.load(getClass().getResource("communityhubPage.fxml"));
          stage = (Stage)((Node)event.getSource()).getScene().getWindow();
          scene = new Scene(root);
          stage.setScene(scene);
          stage.show();
   }
   public void goToSettings(MouseEvent event)throws Exception{
          Parent root = FXMLLoader.load(getClass().getResource("settings.fxml"));
          stage = (Stage)((Node)event.getSource()).getScene().getWindow();
          scene = new Scene(root);
          stage.setScene(scene);
          stage.show();
  }
    public void goToFollowers(MouseEvent event) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("followersPage.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
        try {
            for (User user : User.getCurrentUser().getFollowerList()) {
                HBox userHBox = new HBox();
                Label userName = new Label(playlistNoCounter + "- " + user.getUsername() + " - " + user.getUsername());
                Button relationButton = new Button();
                if(User.getCurrentUser().isFriendsWith(user)){
                    relationButton.setText("  Friends  ");
                    relationButton.setOnAction(new EventHandler<ActionEvent>() {
						@Override
						public void handle(ActionEvent event) {
							try {
                                User.getCurrentUser().unfollow(user);
                            } catch (SQLException e) {
                                e.printStackTrace();
                            }
                            relationButton.setText("  Follow  ");
						}
					});
                }
                else{
                    relationButton.setText("  Follow  ");
                    relationButton.setOnAction(new EventHandler<ActionEvent>() {
						@Override
						public void handle(ActionEvent event) {
							try {
                                User.getCurrentUser().follow(user);
                            } catch (SQLException e) {
                                e.printStackTrace();
                            }
                            relationButton.setText("  Friends  ");
						}
					});
                }
        
                Button viewProfile = new Button("  View Profile");
                userHBox.getChildren().addAll(userName, relationButton, viewProfile);
                followingPageController.followingVBox.getChildren().add(userHBox);
                followingPageController.followingVBox.setVisible(true);
                playlistNoCounter++;
			}
        }   
        catch (SQLException e) {
        e.printStackTrace();
    }
}
    public void goToFollowing(MouseEvent event) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("followingPage.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
        try {
            for (User user : User.getCurrentUser().getFollowingList()) {
                HBox userHBox = new HBox();
                Label userName = new Label(playlistNoCounter + "- " + user.getUsername() + " - " + user.getUsername());
                Button relationButton = new Button();
                if(User.getCurrentUser().isFriendsWith(user)){
                    relationButton.setText("  Friends  ");
                    relationButton.setOnAction(new EventHandler<ActionEvent>() {
						@Override
						public void handle(ActionEvent event) {
							try {
                                User.getCurrentUser().unfollow(user);
                                relationButton.setText("  Follow  ");
                            } catch (SQLException e) {
                                e.printStackTrace();
                            }
						}
					});
                }
                else{
                    relationButton.setText("  Unfollow  ");
                    relationButton.setOnAction(new EventHandler<ActionEvent>() {
						@Override
						public void handle(ActionEvent event) {
							try {
                                User.getCurrentUser().follow(user);
                                relationButton.setText("  Friends  ");
                            } catch (SQLException e) {
                                e.printStackTrace();
                            }
						}
					});
                }
        
                Button viewProfile = new Button("  View Profile");
                userHBox.getChildren().addAll(userName, relationButton, viewProfile);
                followingPageController.followingVBox.getChildren().add(userHBox);
                followingPageController.followingVBox.setVisible(true);
                playlistNoCounter++;
			}
        }   
        catch (SQLException e) {
        e.printStackTrace();
    }
    }
}



