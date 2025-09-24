package com.ewd.dto;

import lombok.Data;

@Data
public class MessageSender {
	
	private String[] fromAddress;
	private String[] toAddress;
	private String mailBody;
	private String subject;
	

}
