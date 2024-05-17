import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;

import java.io.File;
import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ResourceBundle;

import javafx.scene.Node;

public class settingsController implements Initializable {
  private Stage stage;
  private Scene scene;
  private Parent root;

  @FXML
  private PasswordField confirmPassword;

  @FXML
  private Button savePasswordButton;

  @FXML
  private Button saveUsernameButton;

  @FXML
  private PasswordField updatePassword;

  @FXML
  private TextField updateUsername;

  @FXML
  private Pane updatePasswordPane;

  @FXML
  private Pane updateUsernamePane;
  @FXML
  private FlowPane imagePane;

  public void renderProfilePicture() throws Exception {
    VBox imageBox = new VBox();
    imageBox.setAlignment(Pos.CENTER);
    File selectedFile = new File(User.getCurrentUser().getPictureUrl());
    if (selectedFile != null) {
      Image image = new Image(selectedFile.toURI().toString());
      System.out.println(selectedFile);
      ImageView imageView = new ImageView(image);
      imageBox.getChildren().add(imageView);
      imageBox.setStyle("-fx-background-color: #053c75; -fx-padding: 5px; -fx-background-radius: 5px;");
    }
    imagePane.getChildren().add(imageBox);

  }

  public void deleteAccount(MouseEvent event) throws SQLException {
    User.getCurrentUser().deleteAccount(User.getCurrentUser());
  }

  public void saveChanges() {

  }

  public void logOut(MouseEvent event) throws Exception {
    // userı null yapmak lazım :)))
    Parent root = FXMLLoader.load(getClass().getResource("welcomePage.fxml"));
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

  public static void showInvalidPasswordError() {
    Alert alert = new Alert(AlertType.ERROR);
    alert.setTitle("Error");
    alert.setContentText(
        "Password should at least be 8 characters long and should contain at least 1 number, 1 uppercase letter and 1 special character.");
    alert.showAndWait();
  }

  public static void showInvalidUsernameError() {
    Alert alert = new Alert(AlertType.ERROR);
    alert.setTitle("Error");
    alert.setContentText(
        "Username should at least be 3 characters long and cannot contain any spaces or any uppercase letters.");
    alert.showAndWait();
  }

  public static void showNotUniqueUsernameError() {
    Alert alert = new Alert(AlertType.ERROR);
    alert.setTitle("Error");
    alert.setContentText("This username is already taken.");
    alert.showAndWait();
  }

  public static void showInvalidConfirmPasswordError() {
    Alert alert = new Alert(AlertType.ERROR);
    alert.setTitle("Error");
    alert.setContentText("The passwords do not match.");
    alert.showAndWait();
  }

  public static void showInvalidOldPasswordError() {
    Alert alert = new Alert(AlertType.ERROR);
    alert.setTitle("Error");
    alert.setContentText("The current password you entered is wrong.");
    alert.showAndWait();
  }

  private static boolean isValidUsername(String username) throws SQLException {
    if (!username.trim().equals(username) || username.length() < 3 || !username.toLowerCase().equals(username)) {
      return false;
    }
    return true;
  }

  public void updateUsername(MouseEvent event) throws SQLException {

    if (!isValidUsername(updateUsername.getText())) {
      settingsController.showInvalidUsernameError();
      return;
    }

    if (!isUsernameUnique(updateUsername.getText())) {
      settingsController.showNotUniqueUsernameError();
      return;
    }

    Connection connection = Main.connect();
    java.sql.Statement statement = connection.createStatement();
    int count = statement.executeUpdate("update users set username = '" + updateUsername.getText() + "' where id = '"
        + User.getCurrentUser().getId() + "'");

    statement.close();
    connection.close();
    Label updatedUsername = new Label("Username is updated!");
    updatedUsername.setFont(Font.font("Times New Roman", 12));
    updatedUsername.setStyle("-fx-background-color: #ffffff");
    updateUsernamePane.getChildren().add(updatedUsername);

  }

  public void updatePassword(MouseEvent event) throws SQLException {
    if (!isValidPassword(updatePassword.getText())) {
      settingsController.showInvalidPasswordError();
      return;
    }

    if (!isPasswordConfirmed(updatePassword.getText(), confirmPassword.getText())) {
      settingsController.showInvalidConfirmPasswordError();
      return;
    }

    Connection connection = Main.connect();
    Statement statement = connection.createStatement();
    int count = statement.executeUpdate("update users set password = '" + updatePassword.getText() + "' where id = '"
        + User.getCurrentUser().getId() + "'");

    statement.close();
    connection.close();
    Label updatedPassword = new Label("Password is updated!");
    updatedPassword.setFont(Font.font("Times New Roman", 12));
    updatedPassword.setStyle("-fx-background-color: #ffffff");
    updatePasswordPane.getChildren().add(updatedPassword);

  }

  public static boolean isValidPassword(String password) {

    if (password.length() <= 8 || password.toLowerCase().equals(password)) {
      return false;
    }

    int count = 0;

    for (int i = 0; i < password.length(); i++) {
      if (password.charAt(i) < '/' && password.charAt(i) > ':') {
        count++;
      }
    }

    if (count == password.length()) {
      return false;
    }

    count = 0;

    for (int i = 0; i < password.length(); i++) {
      if (password.charAt(i) < '0' && password.charAt(i) > ' ') {
        count++;
      }
    }

    if (count == password.length()) {
      return false;
    }

    return true;

  }

  public boolean isPasswordConfirmed(String enterPassword, String confirmPassword) {
    return enterPassword.equals(confirmPassword);
  }

  private static boolean isUsernameUnique(String username) throws SQLException {
    Connection connection = Main.connect();
    Statement statement = connection.createStatement();
    ResultSet resultSet = statement.executeQuery("SELECT * FROM users WHERE username='" + username + "';");
    if (resultSet.next()) {
      return false;
    }
    return true;
  }

  public void setProfilePicture(MouseEvent event) throws Exception {
    VBox imageBox = new VBox();
    FileChooser fileChooser = new FileChooser();
    fileChooser.setTitle("Choose Image");
    File selectedFile = fileChooser.showOpenDialog(stage);
    if (selectedFile != null) {
      Image image = new Image(selectedFile.toURI().toString());
      User.getCurrentUser().savePictureUrl(selectedFile.toURI().toString());
      User.getCurrentUser().setPictureUrl(selectedFile.toURI().toString());
      ImageView imageView = new ImageView(image);
      System.out.println(selectedFile.toURI().toString());
      imageView.setFitHeight(165);
      imageView.setFitWidth(165);
      imageBox.getChildren().add(imageView);
    }
    imagePane.getChildren().add(imageBox);
  }

  @Override
  public void initialize(URL location, ResourceBundle resources) {
    try {
      if (User.getCurrentUser().getPictureUrl() != null) {
        renderProfilePicture();
      }
    } catch (Exception e) {
      System.out.println("Error in rendering messages: SQLException");
      e.printStackTrace();
    }
  }

}
