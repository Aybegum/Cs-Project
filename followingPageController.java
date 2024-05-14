import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.scene.Node;

public class followingPageController implements Initializable{
    Parent root;
    Stage stage;
    Scene scene;

     @FXML
    private VBox followingVBox;
    
    public void goToProfile(MouseEvent event) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("profilePage.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
    public void renderfollowingpage () throws SQLException {
          ArrayList<User> userArraylist = new ArrayList<>();
        if (User.getCurrentUser().getFollowingList().size() != 0) {
            
        for (int i = 0; i <User.getCurrentUser().getFollowingList().size(); i++) {
            userArraylist.add(User.getById(User.getCurrentUser().getFollowingList().get(i)));
        }     
        System.out.println("bbb");
            for (User user : userArraylist) {
                HBox userHBox = new HBox();
                Label userName = new Label( "- " + user.getUsername() + " - " + user.getUsername());
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
                followingVBox.getChildren().add(userHBox);
                followingVBox.setVisible(true);
			}
        }
    }
    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
        try {
            renderfollowingpage();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
