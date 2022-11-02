package com.leandroperez.taf.utils.fileutil;

import lombok.extern.slf4j.Slf4j;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.leandroperez.taf.utils.excelutil.ExcelUtil.getCustomProperties;

@Slf4j
public class FileUtil {
	private LinkedHashMap<String, String> customSettings = new LinkedHashMap<>();
	private static final String INPUT_XML_BASE_PATH = "src/ test/resources/testdata/xml/";
	private static final String INPUT_TXT_BASE_PATH = "src/ test/resources/testdata/txt/";
	private static final String XML_FILENAME = "data.xml";
	private static final String DATA_FILENAME = "data.txt";

	public FileUtil() {
	}

	public Map<String, String> getCustomSettings() {
		String filePath = getInputFilePath();
		try {
			parseXML(filePath, customSettings);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return customSettings;
	}

	public String getCustomValues(String key) {
		String filePath = getInputFilePath();
		String value = "";
		try {
			value = parseFileLines(filePath, key);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return value;
	}

	private String getInputFilePath() {
		if (getCustomProperties().get("isAndroid").equals("true")) {
			return INPUT_XML_BASE_PATH + XML_FILENAME;
		} else {
			return INPUT_TXT_BASE_PATH + DATA_FILENAME;
		}
	}

	/**
	 * Parse XML files. This method takes a
	 * {@code LinkedHashMap<String, String> customProperties}, captures key and
	 * value for each node in xml file, then outputs in the
	 * {@code LinkedHashMap<key, value> customProperties} map.
	 *
	 * @param filePath         with path of file
	 * @param customProperties {@code with map<key, value>}
	 */
	private void parseXML(String filePath, LinkedHashMap<String, String> customProperties) throws Exception {
		File fXmlFile = new File(filePath);
		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
		Document doc = dBuilder.parse(fXmlFile);
		doc.getDocumentElement().normalize();
		log.info("Root element :" + doc.getDocumentElement().getNodeName());

		NodeList nodeList = doc.getElementsByTagName("string");
		for (int i = 0; i < nodeList.getLength(); i++) {
			Node node = nodeList.item(i);
			Element element = (Element) node;
			String key = element.getAttribute("name");
			String value = node.getTextContent();
			customProperties.put(key, value);
		}
	}

	/**
	 * Returns the value for a given key. It could be used for parse txt, xml or
	 * other file types.
	 *
	 * @param filePath with path of file
	 * @param key      with key value
	 *
	 * @return String with value for a given key
	 */
	private String parseFileLines(String filePath, String key) {
		String strLine = "";
		try {
			Stream<String> lines = Files.lines(Paths.get(filePath));
			strLine = lines.filter(s -> s.contains(key)).collect(Collectors.toList()).get(0);
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (getCustomProperties().get("isAndroid").equals("true")) {
			return strLine.substring(strLine.indexOf(">") + 1, strLine.lastIndexOf("<"));
		} else {
			return strLine.substring(strLine.indexOf("=") + 3, strLine.indexOf(";") - 1);
		}
	}
}