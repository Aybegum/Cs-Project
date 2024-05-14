import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class createplayControl {
    private Stage stage;
    private Scene scene;
    private Parent root;
    private static int songCounter = 1;
	private static int playlistNoCounter = 1;
	private static boolean isPlaying = false;
     @FXML
    private TextField searchBarTextField;
     @FXML
    private VBox songsFlowPane;
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
	
    public void renderSongs(MouseEvent event) throws SQLException {
        ArrayList<Song> searchedSongs = new ArrayList<>();
    
        searchedSongs = searchSongsByNameorArtist(searchBarTextField.getText());
        for (Song song : searchedSongs) {
			HBox songHBox = new HBox();
			Label songInfo = new Label(songCounter + "- " + song.getName() + " - " + song.getArtist() + "                                          ");
			Button playButton = new Button("Play");		
				playButton.setOnAction(new EventHandler<ActionEvent>() {
					@Override
					public void handle(ActionEvent event) {
						try {
							arrangeSong(song);
						} catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
							e.printStackTrace();
						}
					}
				});	
				songHBox.getChildren().addAll(songInfo, playButton);
				songsFlowPane.getChildren().add(songHBox);
				songsFlowPane.setVisible(true);
				songCounter++;
				}

    }
}
