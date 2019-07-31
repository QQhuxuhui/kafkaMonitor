package com.common.email;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;


@Component
@ConfigurationProperties(prefix = "email")
public class EmailRecever {

	private String[] recever;

	public String[] getRecever() {
		return recever;
	}

	public void setRecever(String[] recever) {
		this.recever = recever;
	}
	
}
