
import java.sql.*;
import java.util.ArrayList;
import java.util.Scanner;

import javax.management.loading.PrivateClassLoader;

public class User {

	private int id;
	private String username;
	private String pictureUrl;
	private String email;
	private String password;
	private ArrayList<Integer> followingList;
	private ArrayList<Integer> followerList;

	private static User currentUser;

	public User(int id, String username, String password, String pictureUrl, String email) throws SQLException {

		this.id = id;
		this.username = username;
		this.email = email;
		this.password = password;
		this.pictureUrl = pictureUrl;
		this.followerList = new ArrayList<Integer>();
		this.followingList = new ArrayList<Integer>();
		setFollowingList();
		setFollowerList();

	}

	// Getters and setters
	public static User getCurrentUser() {
		return currentUser;
	}

	public static void setCurrentUser(User currentUser) {
		User.currentUser = currentUser;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {

		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPictureUrl() {
		return pictureUrl;
	}

	public void setPictureUrl(String pictureUrl) {
		this.pictureUrl = pictureUrl;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return this.password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public void setFollowingList() throws SQLException {
		Connection connection = Main.connect();
		Statement statement = connection.createStatement();
		ResultSet result = statement.executeQuery("select * from follower_following where followerid ='" + id + "'");
		
		for (int i = 0; result.next(); i++) {
			followingList.add(result.getInt("followingid"));
		}

		statement.close();
		connection.close();

	}

	public void setFollowerList() throws SQLException {
		Connection connection = Main.connect();
		Statement statement = connection.createStatement();
		ResultSet result = statement.executeQuery("select * from follower_following where followingid ='" + id + "'");

		for (int i = 0; result.next(); i++) {
			followerList.add(result.getInt("followerid"));
		}

		statement.close();
		connection.close();

	}

	public ArrayList<Integer> getFollowerList() {
		return followerList;
	}

	public ArrayList<Integer> getFollowingList() {
		return followingList;
	}

	// Methods

	public static ResultSet getByUsername(String username) throws SQLException {

		Connection connection = Main.connect();
		String query = "select * from users where username = ?";
		PreparedStatement stat = connection.prepareStatement(query);
		stat.setString(1, username);
		ResultSet r = stat.executeQuery();

		return r;

	}

	public static User getById(int id) throws SQLException {

		Connection connection = Main.connect();
		String query = "select * from users where id = ?";
		PreparedStatement stat = connection.prepareStatement(query);
		stat.setInt(1, id);
		ResultSet r = stat.executeQuery();
		if (r.next()) {
			String username = r.getString("username");
			String password = r.getString("password");
			String email = r.getString("email");
			String pictureUrl = r.getString("pictureUrl");
			stat.close();
			connection.close();
			return new User(id, username, password, pictureUrl, email);
		}

		stat.close();
		connection.close();
		return null;

	}
	public void addFollowers (User user) {
		followerList.add(user.id);
	}
	
	public static ResultSet getByEmail(String email) throws SQLException {

		Connection connection = Main.connect();
		String query = "select * from users where email = ?";
		PreparedStatement stat = connection.prepareStatement(query);
		stat.setString(1, email);
		ResultSet r = stat.executeQuery();

		stat.close();
		connection.close();

		return r;

	}

	public static User signUp(String username, String password, String pictureUrl, String email) throws SQLException {

		Scanner scanner = new Scanner(System.in);

		Connection connection = DriverManager.getConnection(Main.getMySqlUrl(), Main.getMySqlUsername(),
				Main.getMySqlPassword());
		Statement idStatement = connection.createStatement();
		ResultSet r = idStatement.executeQuery("select id from users order by id desc");
		r.next();
		int id = r.getInt(1) + 1;

		idStatement.close();
		connection.close();
		scanner.close();

		if (!isValidUsername(username)) {
			welcomePage.showInvalidUsernameError();
			return null;
		}

		if (!isUsernameUnique(username)) {
			welcomePage.showNotUniqueUsernameError();
			return null;
		}

		if (!isValidPassword(password)) {
			welcomePage.showInvalidPasswordError();
			return null;
		}

		if (!isValidEmail(email)) {
			welcomePage.showInvalidEmailError();
			return null;
		}

		return addUser(id, username, password, pictureUrl, email);

	}

	private static boolean isValidEmail(String email) {
		if (email.lastIndexOf('@') == -1 || email.lastIndexOf('.') == -1) {
			return false;
		}
		int count = 0;
		for (int i = 0; i < email.length(); ++i) {
			if (email.charAt(i) == '@') {
				count++;
			}
		}
		if (count > 1 || count == 0) {
			return false;
		}
		return true;
	}

	private static boolean isValidUsername(String username) throws SQLException {
		if (!username.trim().equals(username) || username.length() < 3 || !username.toLowerCase().equals(username)) {
			return false;
		}
		return true;
	} 

	private static boolean isUsernameUnique(String username) throws SQLException {
		Connection connection = Main.connect();
		Statement statement = connection.createStatement();
		ResultSet resultSet = statement.executeQuery("SELECT * FROM users WHERE username='" + username + "';");
		if (resultSet.next()) {
			return false;
		}
		statement.close();
		connection.close();
		return true;
		
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

	public static User logIn(String username, String password) throws SQLException {

		ResultSet r = getByUsername(username);
		if (r.next() && r.getString("password").equals(password)) {

			int id = r.getInt("id");
			String pictureUrl = r.getString("pictureurl");
			String email = r.getString("email");

			return new User(id, username, pictureUrl, password, email);

		}

		r.close();
		welcomePage.showInvalidLoginError();
		return null;

	}

	public static User addUser(int id, String username, String password, String pictureUrl, String email)
			throws SQLException {

		Connection connection = DriverManager.getConnection(Main.getMySqlUrl(), Main.getMySqlUsername(),
				Main.getMySqlPassword());

		User user = new User(id, username, password, pictureUrl, email);
		String query = "insert into users values (?, ?, ?, ?, ?)";
		PreparedStatement statement = connection.prepareStatement(query);
		statement.setInt(1, user.id);
		statement.setString(5, username);
		statement.setString(3, password);
		statement.setString(2, pictureUrl);
		statement.setString(4, email);
		statement.executeUpdate();

		statement.close();
		connection.close();

		return user;
	}

	public static void deleteAccount(User currentUser) throws SQLException {

		Connection connection = Main.connect();
		int currentUserId = currentUser.getId();

		String query = "DELETE FROM users WHERE userid = ?";
		PreparedStatement statement = connection.prepareStatement(query);
		statement.setInt(1, currentUserId);
		statement.executeUpdate();
	}

	public ArrayList<Integer> follow(User user) throws SQLException {

		Connection connection = null;
		Statement statement = null;
		try {
			connection = Main.connect();
			String query = "INSERT INTO follower_following VALUES('" + currentUser.id + "', '" + user.id + "')";
			statement = connection.createStatement();
			statement.executeUpdate(query);

			followingList.add(user.id);
		} finally {
			if (statement != null) {
				statement.close();
			}
			if (connection != null) {
				connection.close();
			}
		}
		return followingList;
	}

	public ArrayList<Integer> unfollow(User user) throws SQLException {

		Connection connection = null;
		Statement statement = null;

		try {
			connection = Main.connect();
			String query = "DELETE FROM follower_following VALUES('" + currentUser.id + "', '" + user.id + "')";
			statement = connection.createStatement();
			statement.executeUpdate(query);

			followingList.remove(user.id);
		} finally {
			if (statement != null) {
				statement.close();
			}
			if (connection != null) {
				connection.close();
			}
		}
		return followingList;
	}

	public boolean isFriendsWith(User user) {

		if (followerList.indexOf(user.id) == -1 ) {
			return false;
		}
		if (followingList.indexOf(user.id) == -1 ) {
			return false;
		}

		boolean isFriendsWith = followerList.get(followerList.indexOf(user.id)) == followingList
				.get(followingList.indexOf(user.id));
		return isFriendsWith;
	}

	/*public void updateUsername (String username) throws SQLException {

		if(!isValidUsername(username)) {
			settingsController.showInvalidUsernameError();
			return;
		}

		if(!isUsernameUnique(username)) {
			settingsController.showNotUniqueUsernameError();
			return;
		}

		Connection connection = Main.connect();
		Statement statement = connection.createStatement();
		int count = statement.executeUpdate("update users set username = '" + username + "' where id = '" + this.id + "'");
		
		statement.close();
		connection.close();
	}*/

	public boolean isPasswordConfirmed(String enterPassword, String confirmPassword) {
		return enterPassword.equals(confirmPassword);
	}

	/*public void updatePassword (String enterPassword, String confirmPassword, String oldPassword) throws SQLException {

		if(!oldPassword.equals(password)) {
			settingsController.showInvalidOldPasswordError();
			return;
		}

		if(!isValidPassword(enterPassword)) {
			settingsController.showInvalidPasswordError();
			return;
		}

		if(!isPasswordConfirmed(enterPassword, confirmPassword)) {
			settingsController.showInvalidConfirmPasswordError();
			return;
		}

		Connection connection = Main.connect();
		Statement statement = connection.createStatement();
		int count = statement.executeUpdate("update users set password = '" + password + "' where id = '" + this.id + "'");
		
		statement.close();
		connection.close();
	} */

	public int getNoOfFollowers (User user) {
		return User.getCurrentUser().getFollowerList().size();
	}
}
