import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.ResourceBundle;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineEvent;
import javax.sound.sampled.LineListener;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;

public class createplayControl implements Initializable {
    private Stage stage;
    private Scene scene;
	private String name;
    private Parent root;
    private static int songCounter = 1;
	private static int playlistNoCounter = 1;
	private static boolean isPlaying = false;
	@FXML
	private VBox playlistBox;
     @FXML
    private TextField searchBarTextField;
     @FXML
    private VBox songsFlowPane;
	@FXML
	private TextField playlistName;
    public ArrayList<Song> searchSongsByNameorArtist(String searchTerm) throws SQLException {
		ArrayList<Song> resultSongs = new ArrayList<Song>();
		Connection connection = Main.connect();
		PreparedStatement stat = null;
		ResultSet rs = null;
		try {
			String query = "select * from songs where name like ? OR artist like ?";
			stat = connection.prepareStatement(query);
			stat.setString(1, "%" + searchTerm + "%");
			stat.setString(2, "%" + searchTerm + "%");

			rs = stat.executeQuery();
			while (rs.next()) {
				int id = rs.getInt("id");
				String songTitle = rs.getString("name");
				String artist = rs.getString("artist");
				String url = rs.getString("url");
				Song song = new Song(id, songTitle, artist, url);
				resultSongs.add(song);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (rs != null) {
					rs.close();
				}
				if (rs != null) {
					stat.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return resultSongs;
	}
    public void arrangeSong(Song song) throws UnsupportedAudioFileException, IOException, LineUnavailableException {
		String filePath = song.getUrl().substring(39);
		File file = new File(filePath);
		AudioInputStream audioStream = AudioSystem.getAudioInputStream(file);
			Clip clip = song.getClip();
		if (clip == null) {
			clip = AudioSystem.getClip();
			clip.open(audioStream);
			song.setClip(clip);
		}
		
		if (Song.getCurrentlyPlayingClip() != null && Song.getCurrentlyPlayingClip().isRunning() && Song.getCurrentlyPlayingClip() != clip) {
			Song.getCurrentlyPlayingClip().stop();
			Song.getCurrentlyPlayingsSong().setIsPlaying(false);
		}
		if (song.getIsPlaying()) {
			song.setPausedPosition(clip.getMicrosecondPosition());
			clip.stop();
			song.setIsPlaying(false);
		} else {
			clip.setMicrosecondPosition(song.getPausedPosition());
			clip.start();
			song.setIsPlaying(true);
		}
		Song.currentlyPlayingSong = song;
		Song.currentlyPlayingClip = clip;
	}
	public void setName(MouseEvent event){
		name =""+ playlistName.getText();
	}
	public void playNextSong(MouseEvent event)throws Exception{
		Song.queue.remove(0);
		searchpageController.arrangeSong(Song.queue.get(0));

	}
	public void goToCreatePlaylist(MouseEvent event) throws Exception {
		Playlist.setCurrenPlaylistId(Playlist.createPlaylist(User.getCurrentUser(), "Playlist", "efuhjıdfsjjd").getPlaylistID());
		Parent root = FXMLLoader.load(getClass().getResource("createPlaylist2.fxml"));
		stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
		scene = new Scene(root);
		stage.setScene(scene);
		stage.show();

	}
    public void renderSongs(MouseEvent event) throws SQLException {
        ArrayList<Song> searchedSongs = new ArrayList<>();
    
        searchedSongs = searchSongsByNameorArtist(searchBarTextField.getText());
        for (Song song : searchedSongs) {
			BorderPane songBox = new BorderPane();
                  songBox.setStyle("-fx-background-color: #053c75; -fx-padding: 5px; -fx-background-radius: 5px;");
                  songBox.setPrefWidth(400);
                  Text songInfo = new Text(song.getSongNameWithSpaces() + " -" + song.getArtistNameWithSpaces());
                  songInfo.setFont(new Font("Times New Roman", 16));
                  songInfo.setFill(Color.WHITE);
                  songInfo.setTextAlignment(TextAlignment.CENTER);
            
                  Button addButton = new Button("Add");
                  addButton.setFont(Font.font("Times New Roman", 12));
                  addButton.setOnMouseClicked(new EventHandler<MouseEvent>() {
                        @Override
                        public void handle(MouseEvent event) {
                              try {
								if(Playlist.getPlaylistById(Playlist.getCurrenPlaylistId()) !=null){
									if (Playlist.getPlaylistById(Playlist.getCurrenPlaylistId()).isSongInPlaylist(song)) {
										addButton.setText("Remove");
										Playlist.getPlaylistById(Playlist.getCurrenPlaylistId()).removeSong(song.getID());
                                    } else {
                                        addButton.setText("Add");
										Playlist.getPlaylistById(Playlist.getCurrenPlaylistId()).addSong(song.getID());
										addButton.setText("Added");
                                    }
								}else{
									Playlist.getPlaylistById(Playlist.getCurrenPlaylistId()).addSong(song.getID());
									addButton.setText("Remove");

								}
                              } catch (Exception e) {
                                    e.printStackTrace();
                              }
                        }
						
                  });
				songBox.setLeft(songInfo);
				songBox.setRight(addButton);
				songsFlowPane.getChildren().add(songBox);
				songsFlowPane.setVisible(true);
				songCounter++;
				}

    }
	public void renderPlaylistsOnSidebar() throws SQLException {

            ArrayList<Playlist> playlists = new ArrayList<>();
    
            Connection connection = Main.connect();
            Statement statement = connection.createStatement();
            ResultSet rs = statement
                    .executeQuery("select * from playlists where creatorid = '" + User.getCurrentUser().getId() + "'");
    
            while(rs.next()) {
                playlists.add(Playlist.getPlaylistById(rs.getInt("id")));
            }
    
            rs.close();
            connection.close();
            for (int i = 0; i < playlists.size(); i++) {
                System.out.println("AAAAAAAAAAAAAAAAAAAAA\n\n");
                displayPlaylist(playlists.get(i));
            }
        }

        public void displayPlaylist(Playlist playlist) throws SQLException {
            Button newPlaylist = new Button(playlist.getPlaylistName());
            playlistBox.getChildren().add(newPlaylist);
            newPlaylist.setOnMouseClicked(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent arg0) {
                    try {
                        Playlist.setCurrenPlaylistId(playlist.getPlaylistID());
                        goToPlaylist(arg0);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
            newPlaylist.setFont(Font.font("Times New Roman", 16));
            newPlaylist.setPrefSize(153, 30);
    
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
		public void createPlaylist1 (MouseEvent event) throws Exception {
			Playlist.getPlaylistById(Playlist.getCurrenPlaylistId()).updatePlaylistName(playlistName.getText());
			goToPlaylist(event);
        } 
		public static int getSongIndex(Song song){
			for (int i = 0; i < Song.queue.size(); i++) {
				if(Song.queue.get(i).getName().equals(song.getName())){
					return i;
				}
			}return -1;
		}public void goToPlaylist(MouseEvent event) throws Exception {
            Parent root = FXMLLoader.load(getClass().getResource("playlistPage.fxml"));
            stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        }

        public void setName () throws SQLException {
            name = playlistName.getText();
        }
        @Override
      public void initialize(URL location, ResourceBundle resources) {
            try {
				setName();
                  renderPlaylistsOnSidebar();
            } catch (Exception e) {
                  System.out.println("Error");
                  e.printStackTrace();
            }
      }
}
