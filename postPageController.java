import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.sql.SQLException;
import javafx.scene.Node;

public class postPageController implements Initializable{
    private Stage stage;
    private Scene scene;
    private Parent root;
    @FXML
    private TextField postText;
    @FXML
    private FlowPane postPane;
    @FXML
    private VBox playlistBox;
    public void renderPost(MouseEvent event){
        String text = postText.getText();
        String currentPost = ((Node) event.getSource()).getAccessibleText();
        if(currentPost == null){
            postPane.getChildren().add(postText(text));
            
        }else if(currentPost.equals("image")){
            postPane.getChildren().add(postImage());
            
        }else if(currentPost.equals("playlist")){
        
        }else {

        }
    }

    //to do 
    public void renderPost()throws SQLException{
    }
    //Implemet this method when the post's data come
    public Pane postImage(){
        HBox imageBox = new HBox();
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Choose Image");
        File selectedFile = fileChooser.showOpenDialog(stage);
        if(selectedFile != null){
            Image image = new Image(selectedFile.toURI().toString());
            ImageView imageView = new ImageView(image);
            imageBox.getChildren().add(imageView);
        }
        return imageBox;

    }
    public Pane postPlaylist(){
        return null;
    }
    public Pane postSong(){
        return null;
    }
    public Pane postText(String post){
        HBox postBox = new HBox();
        postBox.setSpacing(5);
        postBox.setAlignment(Pos.CENTER);
        Text postText = new Text(post);
        postText.setFont(Font.font("Times New Roman", 14));
        postText.setFill(Color.WHITE);
        postBox.getChildren().add(postText);
        postBox.setStyle("-fx-background-color: #053c75; -fx-padding: 5px; -fx-background-radius: 5px;");
        return postBox;
    }
    
    public void goToChatroom(MouseEvent event) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("chatroomPage.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
        
    }
    public void goToCommunityHub(MouseEvent  event) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("communityhubPage.fxml"));
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
    public void goToPlaylist(MouseEvent event)throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("playlistPage.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
    public void goToSearchPage(MouseEvent event)throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("searchpage.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
    public void renderPlaylistsOnSidebar() throws SQLException{
        ArrayList<Playlist> playlists = new ArrayList<>();
        Connection connection = Main.connect();
        Statement statement = connection.createStatement();
        ResultSet rs = statement.executeQuery("select * from playlists where creatorid = '" + User.getCurrentUser().getId() + "' order by id desc");
        int count;
        if (rs.next()) {
            count = rs.getInt("id");
            for (int i = count - 1; i >= 0; i--) {
                playlists.add(Playlist.getPlaylistByIdAndUser(i, User.getCurrentUser()));
            }
        }
        for (int i = 0; i < playlists.size(); i++) {     
            displayPlaylist();
        }
    }
    
      public void displayPlaylist()throws SQLException{
            Button newPlaylist = new Button("Playlist");
            newPlaylist.setFont(Font.font("Times New Roman", 16));
            newPlaylist.setPrefSize(153,30);
            playlistBox.getChildren().add(newPlaylist);
      }
       @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            renderPost();
            renderPlaylistsOnSidebar();
        } catch (SQLException e) {
            System.out.println("Error in rendering messages: SQLException");
        }
    }
}
