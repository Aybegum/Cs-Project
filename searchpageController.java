import javafx.scene.Node;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineEvent;
import javax.sound.sampled.LineListener;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.shape.Line;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;

public class searchpageController implements Initializable {

	@FXML
	private Rectangle backRect;

	@FXML
	private Polygon backTria;
	@FXML
	private ComboBox<String> comboBoxSearch;

	@FXML
	private Circle enterSongCircle;

	@FXML
	private Line enterSongLine;

	@FXML
	private TextField searchBarTextField;

	@FXML
	private VBox songsFlowPane;

	private Stage stage;
	private Scene scene;
	private Parent root;
	private static int songCounter = 1;
	private static int playlistNoCounter = 1;
	private static boolean isPlaying = false;

	public void goToCommunityHub(MouseEvent event) throws Exception {
		Parent root = FXMLLoader.load(getClass().getResource("communityhubPage.fxml"));
		stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
		scene = new Scene(root);
		stage.setScene(scene);
		stage.show();
	}

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

	public void renderSongs(MouseEvent event) {
		songsFlowPane.getChildren().clear();
		ArrayList<Song> searchedSongs = new ArrayList<>();
		ArrayList<User> searchedUsers = new ArrayList<>();
		ArrayList<Playlist> searchedPlaylists = new ArrayList<>();

		if (!comboBoxSearch.getSelectionModel().isEmpty()) {
			if (comboBoxSearch.getValue().equals("Song")) {
				try {
					searchedSongs = searchSongsByNameorArtist(searchBarTextField.getText());
				} catch (SQLException e) {
					e.printStackTrace();
				}

				for (Song song : searchedSongs) {
					BorderPane songBox = new BorderPane();
					songBox.setStyle("-fx-background-color: #053c75; -fx-padding: 5px; -fx-background-radius: 5px;");
					songBox.setPrefWidth(400);
					Text songInfo = new Text(song.getSongNameWithSpaces() + " -" + song.getArtistNameWithSpaces());
					songInfo.setFont(new Font("Times New Roman", 16));
					songInfo.setFill(Color.WHITE);
					songInfo.setTextAlignment(TextAlignment.CENTER);

					Button playButton = new Button("Play");
					playButton.setFont(Font.font("Times New Roman", 12));

					playButton.setOnAction(new EventHandler<ActionEvent>() {
						@Override
						public void handle(ActionEvent event) {
							try {
								arrangeSong(song);
								if (song.getisPlaying()) {
									playButton.setText("Pause");
								} else {
									playButton.setText("Play");
									Song.queue.clear();
								}
							} catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
								e.printStackTrace();
							}
						}
					});

					songBox.setLeft(songInfo);
					songBox.setRight(playButton);
					songsFlowPane.getChildren().add(songBox);
					songsFlowPane.setPrefHeight(songsFlowPane.getPrefHeight() + songBox.getHeight());
					songsFlowPane.setVisible(true);
					songCounter++;
				}
			} else if (comboBoxSearch.getValue().equals("User")) {
				try {
					searchedUsers = searchUserByName(searchBarTextField.getText());
				} catch (SQLException e) {
					e.printStackTrace();
				}
				for (User user : searchedUsers) {
					BorderPane userHBox = new BorderPane();
					Button view = new Button();
					Text userName = new Text(user.getUsername());
					userName.setFont(new Font("Times New Roman", 16));
					userName.setFill(Color.WHITE);
					userName.setTextAlignment(TextAlignment.CENTER);
					userHBox.setStyle("-fx-background-color: #053c75; -fx-padding: 5px; -fx-background-radius: 5px;");
					userHBox.setPrefWidth(400);
					Button relationButton = new Button();
					if (User.getCurrentUser().isFriendsWith(user)) {
						relationButton.setText("Unfollow");
						relationButton.setFont(Font.font("Times New Roman", 12));
						relationButton.setOnAction(new EventHandler<ActionEvent>() {
							@Override
							public void handle(ActionEvent event) {
								try {
									User.getCurrentUser().unfollow(user);
									relationButton.setText("Follow");
								} catch (SQLException e) {
									e.printStackTrace();
								}

							}
						});

					} else if (User.getCurrentUser().isFollowing(user)) {
						relationButton.setText("Unfollow");
						relationButton.setOnAction(new EventHandler<ActionEvent>() {
							@Override
							public void handle(ActionEvent event) {
								try {
									User.getCurrentUser().unfollow(user);
									relationButton.setText("Follow");
								} catch (SQLException e) {
									e.printStackTrace();
								}
							}
						});
					} else {
						// (user.isFollowing(User.getCurrentUser()))
						relationButton.setText("Follow");
						relationButton.setOnAction(new EventHandler<ActionEvent>() {
							@Override
							public void handle(ActionEvent event) {
								try {
									User.getCurrentUser().follow(user);
									relationButton.setText("Unfollow");
								} catch (SQLException e) {
									e.printStackTrace();
								}

							}
						});
					}
					view.setText("View Profile");
					view.setOnAction(new EventHandler<ActionEvent>() {
						@Override
						public void handle(ActionEvent event) {
							Parent root;
							try {
								User.profileUser = user;
								root = FXMLLoader.load(getClass().getResource("defa.fxml"));
								stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
								scene = new Scene(root);
								stage.setScene(scene);
								stage.show();
							} catch (IOException e) {
								e.printStackTrace();
							}

						}
					});
					userHBox.setLeft(userName);
					userHBox.setRight(relationButton);
					userHBox.setCenter(view);
					songsFlowPane.getChildren().add(userHBox);
					songsFlowPane.setVisible(true);
					if (songsFlowPane == null) {
						songCounter = 1;
					}
				}
			} else if (comboBoxSearch.getValue().equals("Playlist")) {
				try {
					searchedPlaylists = searchPlaylistsByName(searchBarTextField.getText());
				} catch (SQLException e) {
					e.printStackTrace();
				}
				for (Playlist playlist : searchedPlaylists) {
					BorderPane playlistHBox = new BorderPane();
					Text playlistName = new Text();
					playlistName.setFont(new Font("Times New Roman", 16));
					playlistName.setFill(Color.WHITE);
					playlistName.setTextAlignment(TextAlignment.CENTER);
					playlistHBox
							.setStyle("-fx-background-color: #053c75; -fx-padding: 5px; -fx-background-radius: 5px;");
					playlistHBox.setPrefWidth(400);
					try {
						playlistName = new Text(
								playlist.getPlaylistName() + " - " + User.getById(playlist.getCreatorID()));
						playlistName.setFont(new Font("Times New Roman", 16));
						playlistName.setFill(Color.WHITE);
						playlistName.setTextAlignment(TextAlignment.CENTER);
					} catch (SQLException e) {
						e.printStackTrace();
					}
					Button viewButton = new Button("View");
					viewButton.setFont(new Font("Times New Roman", 16));
					playlistHBox.setLeft(playlistName);
					playlistHBox.setRight(viewButton);
					songsFlowPane.getChildren().add(playlistHBox);
					songsFlowPane.setVisible(true);
					playlistNoCounter++;
				}
			}
		}

	}

	public ArrayList<User> searchUserByName(String searchTerm) throws SQLException {
		ArrayList<User> resultUsers = new ArrayList<User>();
		Connection connection = Main.connect();
		PreparedStatement stat = null;
		ResultSet rs = null;

		try {
			String query = "select * from users where username like ?";
			stat = connection.prepareStatement(query);
			stat.setString(1, "%" + searchTerm + "%");

			rs = stat.executeQuery();
			while (rs.next()) {
				int id = rs.getInt("id");
				String username = rs.getString("username");
				String password = rs.getString("password");
				String pictureUrl = rs.getString("pictureURL");
				String email = rs.getString("email");
				User user = new User(id, username, password, pictureUrl, email);
				resultUsers.add(user);
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
		return resultUsers;
	}

	public ArrayList<Playlist> searchPlaylistsByName(String searchTerm) throws SQLException {
		ArrayList<Playlist> resultPlaylists = new ArrayList<Playlist>();
		Connection connection = Main.connect();
		PreparedStatement stat = null;
		ResultSet rs = null;

		try {
			String query = "select * from playlists where name like ?";
			stat = connection.prepareStatement(query);
			stat.setString(1, "%" + searchTerm + "%");

			rs = stat.executeQuery();
			while (rs.next()) {
				int id = rs.getInt("id");
				String name = rs.getString("name");
				String pictureUrl = rs.getString("pictureURL");
				int creatorID = rs.getInt("creatorID");
				Playlist playlist = new Playlist(name, pictureUrl, id, creatorID);
				resultPlaylists.add(playlist);
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
		return resultPlaylists;
	}

	@Override
	public void initialize(java.net.URL location, ResourceBundle resources) {
		songsFlowPane.setStyle("-fx-background-color: 053c75;");
		ObservableList<String> options = FXCollections.observableArrayList("User", "Song", "Playlist");
		comboBoxSearch.setItems(options);
	}

	public void playNextSong(MouseEvent event) throws Exception {
		Song.queue.remove(0);
		arrangeSong(Song.queue.get(0));

	}

	public static void arrangeSong(Song song)
			throws UnsupportedAudioFileException, IOException, LineUnavailableException {
		String filePath = song.getUrl().substring(39);
		File file = new File(filePath);
		System.out.println("File URI: " + file.toURI().toString()); // Print file URI for debugging
		AudioInputStream audioStream = AudioSystem.getAudioInputStream(file);

		Clip clip = song.getClip();
		if (clip == null) {
			clip = AudioSystem.getClip();
			clip.open(audioStream);
			song.setClip(clip);
			clip.addLineListener(new LineListener() {
				@Override
				public void update(LineEvent event) {
					if (event.getType() == LineEvent.Type.STOP && Song.getCurrentlyPlayingsSong().getClip()
							.getFrameLength() == Song.getCurrentlyPlayingsSong().getClip().getFramePosition()) {

						Song.queue.remove(0);
						if (Song.queue.size() == 0) {

						}
						if (1 >= Song.queue.size() - 1) {
							Song.currentlyPlayingSong = Song.queue.get(0);
						} else {
							Song.currentlyPlayingSong = Song.queue.get(getSongIndex(Song.currentlyPlayingSong) + 1);
						}
						try {
							arrangeSong(Song.currentlyPlayingSong);
						} catch (UnsupportedAudioFileException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (LineUnavailableException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						;
					}
				}
			});
		}

		if (Song.getCurrentlyPlayingClip() != null && Song.getCurrentlyPlayingClip().isRunning()
				&& Song.getCurrentlyPlayingClip() != clip) {
			Song.getCurrentlyPlayingClip().stop();
			Song.getCurrentlyPlayingsSong().setIsPlaying(false);
			System.out.println();

		}
		if (song.getIsPlaying() || User.getCurrentUser() == null) {
			song.setPausedPosition(clip.getMicrosecondPosition());
			clip.stop();
			song.setIsPlaying(false);
		} else {
			clip.setMicrosecondPosition(song.getPausedPosition());
			clip.start();

			song.setIsPlaying(true);
			Song.needToSwitch = false;
		}
		Song.currentlyPlayingSong = song;
		Song.currentlyPlayingClip = clip;

	}

	public String getSongNameWithSpaces(Song song) {

		String name = song.getName().replace("_", " ");
		return name;
	}

	/*
	 * public void goToOthersProfilePage(MouseEvent event) throws Exception {
	 * Parent root = FXMLLoader.load(getClass().getResource("emptyprofile.fxml"));
	 * stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
	 * scene = new Scene(root);
	 * stage.setScene(scene);
	 * stage.show();
	 * }
	 */
	/*
	 * public void stopSong(Song song) throws UnsupportedAudioFileException,
	 * IOException, LineUnavailableException{
	 * String a = song.getUrl().substring(39);
	 * File file = new File(a);
	 * AudioInputStream audioStream = AudioSystem.getAudioInputStream(file);
	 * Clip clip = AudioSystem.getClip();
	 * clip.open(audioStream);
	 * clip.stop();
	 * //clip.setMicrosecondPosition(0);
	 * // we'll use these methods
	 * isPlaying = false;
	 * }
	 */
	public static int getSongIndex(Song song) {
		for (int i = 0; i < Song.queue.size(); i++) {
			if (Song.queue.get(i).getName().equals(song.getName())) {
				return i;
			}
		}
		return -1;
	}

	public void setSecondsplus10(MouseEvent event) {
		Song.getCurrentlyPlayingsSong().getClip()
				.setMicrosecondPosition(Song.getCurrentlyPlayingsSong().getClip().getFramePosition() - 100);
	}
}
