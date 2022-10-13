package XXE.TEST.XXE.web;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.dom4j.DocumentException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

@RestController
public class TestCrossController {
    @GetMapping("/hello")
    public String hello() {
        return "CORS TEST";
    }

    public static Map xmlToMap(String strXML) throws Exception {
        try {
            Map data = new HashMap();
            DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
            InputStream stream = new ByteArrayInputStream(strXML.getBytes("UTF-8"));
            org.w3c.dom.Document doc = documentBuilder.parse(stream);
            doc.getDocumentElement().normalize();
            NodeList nodeList = doc.getDocumentElement().getChildNodes();
            for (int idx = 0; idx < nodeList.getLength(); ++idx) {
                Node node = nodeList.item(idx);
                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    org.w3c.dom.Element element = (org.w3c.dom.Element)node;
                    data.put(element.getNodeName(), element.getTextContent());
                }
            }
            try {
                stream.close();
            } catch (Exception ex) {
                // do nothing
            }
            return data;
        } catch (Exception ex) {
            WXPayUtil.getLogger().warn("Invalid XML, can not convert to map. Error message: {}. XML content: {}",
                ex.getMessage(), strXML);
            throw ex;
        }
    }

    @SuppressWarnings("unchecked")
    @PostMapping("test-xxe")
    public void test(HttpServletRequest request) throws UnsupportedEncodingException, IOException, DocumentException,
        ParserConfigurationException, SAXException {
        String value = "";
        Map data = new HashMap();
        // 获取HTTP请求的输入流
        // 已HTTP请求输入流建立一个BufferedReader对象
        BufferedReader br = new BufferedReader(new InputStreamReader(request.getInputStream(), "UTF-8"));
        DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder documentBuilder = newDocumentBuilder();
        String buffer = null;
        // 存放请求内容
        StringBuffer xml = new StringBuffer();
        while ((buffer = br.readLine()) != null) {
            xml.append(buffer);
        }
        // reader.setEntityResolver(new IgnoreDTDEntityResolver());
        ByteArrayInputStream inputStream = new ByteArrayInputStream(xml.toString().getBytes());
        org.w3c.dom.Document doc = documentBuilder.parse(inputStream);
        doc.getDocumentElement().normalize();
        NodeList nodeList = doc.getDocumentElement().getChildNodes();
        for (int idx = 0; idx < nodeList.getLength(); ++idx) {
            Node node = nodeList.item(idx);
            if (node.getNodeType() == Node.ELEMENT_NODE) {
                org.w3c.dom.Element element = (org.w3c.dom.Element)node;
                data.put(element.getNodeName(), element.getTextContent());
            }
        }
        try {
            inputStream.close();
        } catch (Exception ex) {
            // do nothing
        }
        System.out.println(data);
    }

    public static DocumentBuilder newDocumentBuilder() throws ParserConfigurationException {

        DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
        // documentBuilderFactory.setFeature("http://apache.org/xml/features/disallow-doctype-decl", true);
        // documentBuilderFactory.setFeature("http://xml.org/sax/features/external-general-entities", false);
        // documentBuilderFactory.setFeature("http://xml.org/sax/features/external-parameter-entities", false);
        // documentBuilderFactory.setFeature("http://apache.org/xml/features/nonvalidating/load-external-dtd", false);
        // documentBuilderFactory.setXIncludeAware(false);
        documentBuilderFactory.setExpandEntityReferences(false);
        return documentBuilderFactory.newDocumentBuilder();
    }

}
