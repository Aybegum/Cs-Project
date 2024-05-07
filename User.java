
import java.sql.*;
import java.util.Scanner;

public class User {
	
	private int id;
	private String username;
	private String pictureUrl;
	private String email;
	private String password;
	
	public User(int id, String username, String password, String pictureUrl, String email) throws SQLException {
		
		this.id = id;
		this.username = username;
		this.email = email;
		this.password = password;
		this.pictureUrl = pictureUrl;
		
	}
	
	//Getters and setters
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
	
	//Methods
	
	public static ResultSet getByUsername(String username) throws SQLException {
		
		Connection connection = Main.connect();
		String query = "select * from users where username = ?";
		PreparedStatement stat = connection.prepareStatement(query);
		stat.setString(1, username);
		ResultSet r = stat.executeQuery();
		
		return r;

	}
	
	public static ResultSet getById(int id) throws SQLException {
		
		Connection connection = Main.connect();
		String query = "select * from users where id = ?";
		PreparedStatement stat = connection.prepareStatement(query);
		stat.setInt(1, id);
		ResultSet r = stat.executeQuery();
		
		stat.close();
		connection.close();
		
		return r;

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
	
	public static User signUp() throws SQLException {
		
		Scanner scanner = new Scanner(System.in);
		
		System.out.print("Enter username: ");
		String username = scanner.nextLine();
		
		System.out.print("Enter password: ");
		String password = scanner.nextLine();
		
		System.out.print("Enter picture URL: ");
		String pictureUrl = scanner.nextLine();
		
		System.out.print("Enter E-mail: ");
		String email = scanner.nextLine();
		
		Connection connection = DriverManager.getConnection(Main.getMySqlUrl(), Main.getMySqlUsername(), Main.getMySqlPassword());
		Statement idStatement = connection.createStatement();
		ResultSet r = idStatement.executeQuery("select id from users order by id desc");
		r.next();
		int id = r.getInt(1) + 1;
				
		idStatement.close();
		connection.close();
		scanner.close();
		
		return addUser(id, username, password, pictureUrl, email);

	}
	
	public static User logIn() throws SQLException { 
		
		Scanner scanner = new Scanner(System.in);
		System.out.print("Enter username: ");
		String username = scanner.nextLine();
		
		System.out.print("Enter password: ");
		String password = scanner.nextLine();
		
		
		ResultSet r = getByUsername(username);
		if (r.next() && r.getString("password").equals(password)) {
			
			int id = r.getInt("id");
			String pictureUrl = r.getString("pictureurl");
			String email = r.getString("email");
			
			return new User(id, username, pictureUrl, password, email);
			
		}
		
		System.out.println("The username or password you provided is wrong");
		return null;
		
	}
	
	public static User addUser(int id, String username, String password, String pictureUrl, String email) throws SQLException {
		
		Connection connection = DriverManager.getConnection(Main.getMySqlUrl(), Main.getMySqlUsername(), Main.getMySqlPassword());

		User user = new User(id, username, password, pictureUrl, email);
		String query = "insert into users values (?, ?, ?, ?, ?)";
		PreparedStatement statement = connection.prepareStatement(query);
		statement.setInt(1, user.id);
		statement.setString(5, username);
		statement.setString(3, password);
		statement.setString(2, pictureUrl);
		statement.setString(4, email);
		int count = statement.executeUpdate();
		
		statement.close();
		connection.close();
		
		return user;
	}
	
}
