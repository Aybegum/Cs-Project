package com.comusicly.main;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class Message {
	
	private int senderId;
	private int communityId;
	private String body;
	
	public Message(int senderId, int communityId, String body) {
		
		this.senderId = senderId;
		this.communityId = communityId;
		this.body = body;
		
	}

	public int getSenderId() {
		return senderId;
	}

	public void setSenderId(int senderId) {
		this.senderId = senderId;
	}

	public int getCommunityId() {
		return communityId;
	}

	public void setCommunityId(int communityId) {
		this.communityId = communityId;
	}

	public String getBody() {
		return body;
	}

	public void setBody(String body) {
		this.body = body;
	}
	
	public static Message createMessage(int senderId, int communityId, String body) throws SQLException {
		
		Connection connection = Main.connect();
		
		Message message = new Message(senderId, communityId, body);
		String query = "insert into messages values (?, ?, ?)";
		PreparedStatement statement = connection.prepareStatement(query);
		statement.setInt(1, senderId);
		statement.setInt(2, communityId);
		statement.setString(3, body);
		
		int count = statement.executeUpdate();
		
		return message;
		
	}
	
	public boolean isSentByCurrentUser() {
		
		if (Main.getCurrentUser().getId() == senderId) {
			return true;
		}
		
		return false;
		
	}
	
	public static void renderMessage() {
		
		
		
	}
	
}
