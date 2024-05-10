
import java.sql.*;
import java.util.Scanner;

public class User {

	private int id;
	private String username;
	private String pictureUrl;
	private String email;
	private String password;

	private static User currentUser;

	public User(int id, String username, String password, String pictureUrl, String email) throws SQLException {

		this.id = id;
		this.username = username;
		this.email = email;
		this.password = password;
		this.pictureUrl = pictureUrl;

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
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
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
		if(r.next()) {
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

}
