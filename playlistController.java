public class playlistController {
  public void addSongToPlaylist (String songName) {
    
  }
  public void deleteSongToPlaylist (String songName) {
    
  }
  public void changePlaylistImage (String urlOfImage) {
    
  }
  public void deletePlaylist () {
    
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
