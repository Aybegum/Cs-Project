import javafx.fxml.FXML;

public class backButton {
    public void getPreviousScene(Scene preScene) {
        this.preScene = preScene;
    }


    void clickPreviousScene (ActionEvent event) throws IOException {
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        stage.setScene(preScene);
        stage.show();
    }

}
