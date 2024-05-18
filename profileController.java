import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.ResourceBundle;

import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

import org.w3c.dom.Text;
import javafx.scene.Node;

public class profileController implements Initializable {
    private Stage stage;
    private Scene scene;
    private Parent root;
    private int playlistNoCounter = 1;
    @FXML
    private FlowPane imagePane;

    public void renderProfilePicture() throws Exception {
        VBox imageBox = new VBox();
        File selectedFile = new File(User.getCurrentUser().getPictureUrl());
        imageBox.setAlignment(Pos.CENTER);

        if (selectedFile != null) {
            Image image = new Image(selectedFile.toURI().toString());
            System.out.println(selectedFile);
            ImageView imageView = new ImageView(image);
            imageBox.getChildren().add(imageView);
            imageView.setFitHeight(150);
            imageView.setFitWidth(150);
        }
        imagePane.getChildren().add(imageBox);

    }

    public void goToCommunityHub(MouseEvent event) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("communityhubPage.fxml"));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    public void goToSettings(MouseEvent event) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("settings.fxml"));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    public void goToFollowers(MouseEvent event) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("followersPage.fxml"));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    public void goToFollowing(MouseEvent event) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("followingPage.fxml"));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            renderProfilePicture();
            ObservableList<String> options = FXCollections.observableArrayList("User", "Song", "Playlist");

        } catch (Exception e) {
            System.out.println("Error in rendering messages: SQLException");
            e.printStackTrace();
        }
    }
    public void playNextSong()
            throws UnsupportedAudioFileException, IOException, LineUnavailableException, SQLException {
        searchpageController.arrangeSong(Song.random());
    }
}
