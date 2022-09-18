package XXE.TEST.XXE.web;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import XXE.TEST.XXE.domain.User;

@RestController
@CrossOrigin
public class XXETest {

	Log logger = LogFactory.getLog(XXETest.class);
	@SuppressWarnings("unchecked")
	@PostMapping("login")
	public User test(HttpServletRequest request) throws UnsupportedEncodingException, IOException, DocumentException {
		        String value = "";
		        User user = new User();
	            // 获取HTTP请求的输入流
	            // 已HTTP请求输入流建立一个BufferedReader对象
	            BufferedReader br = new BufferedReader(new InputStreamReader(
	                    request.getInputStream(), "UTF-8"));

	            String buffer = null;
	            // 存放请求内容
	            StringBuffer xml = new StringBuffer();
	            while ((buffer = br.readLine()) != null) {
	                xml.append(buffer);
	            }
	            Document document = null;
	            SAXReader reader = new SAXReader();
	            ByteArrayInputStream inputStream = new ByteArrayInputStream(xml
	                    .toString().getBytes());
	            InputStreamReader ir = new InputStreamReader(inputStream);
	//document获取xml文件
	           document = reader.read(ir);
	           @SuppressWarnings("unchecked")
			List<Element> element = document.selectNodes("root");
	        List<Element> usernameList = new ArrayList<>();
	        String username = "";
	           for(Element el:element) {
	        	   usernameList = el.selectNodes("username");
	           }
	           for(Element el:usernameList) {
	        	   username = el.getText();
	        	   user.setUsername(username);
	           }
	        return user;
	}
	

	
	@GetMapping("/xxe-param")
	public String getXXEParam() {
		return  "<!ENTITY xxe SYSTEM \"file:///E:/password.txt\" >";
	}
}
