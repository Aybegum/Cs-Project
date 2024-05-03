public class playlistController {
  private Stage stage;
    private Scene scene;
    private Parent root;
  public void addSongToPlaylist (String songName) {
    
  }
  public void deleteSongToPlaylist (String songName) {
    
  }
  public void changePlaylistImage (String urlOfImage) {
    
  }
  public void deletePlaylist () {
    
  }
  public void createPlaylist (String playlistName, String coverURL, Song song) {
        
  }
  public void goToProfile(MouseEvent event) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("profilePage.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
  }
  public void goToCommunityHub(MouseEvent event) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("communityhubPage.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
  }
 
} 
