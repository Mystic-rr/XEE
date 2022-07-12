package XXE.TEST.XXE.domain;

import javax.xml.bind.annotation.XmlElement;

public class User {
	@XmlElement
	String name;

	@XmlElement
	String password;

	public String getName() {

	return name;

	}

	public void setName(String name) {

	this.name = name;

	}

	public String getPassword() {

	return password;

	}

	public void setPassword(String password) {

	this.password = password;

	}

}
