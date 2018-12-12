package com.risksense.converters;


import static org.junit.Assert.*;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.ReflectionUtils;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Collectors;


@RunWith(SpringRunner.class)
@ContextConfiguration(classes = ConverterFactory.class)
public class XMLJSONConverterITest {
    private XMLJSONConverterI converter;

    @Autowired
    public void setConverter(XMLJSONConverterI converter) {
        this.converter = converter;
    }

    @Test
    public void test_convertJsonNodeToXML_and_prettyFormat() throws NoSuchMethodException, IOException {
        Method convertJsonNodeToXML = converter.getClass().getDeclaredMethod("convertJsonNodeToXML", JsonNode.class);
        Method prettyFormat = converter.getClass().getDeclaredMethod("prettyFormat", String.class);
        convertJsonNodeToXML.setAccessible(true);
        prettyFormat.setAccessible(true);

        String json =
                "{" +
                    "\"firstName\":\"John\", " +
                    "\"lastName\":\"Doe\"," +
                    "\"age\":30," +
                    "\"isEmployed\":true," +
                    "\"hobbies\":null," +
                    "\"phones\":[9000012345, 9000012346]" +
                 "}";

        ObjectMapper mapper = new ObjectMapper();
        JsonNode node = mapper.readTree(json);

        Object generatedXML = ReflectionUtils.invokeMethod(convertJsonNodeToXML, converter, node);
        Object formattedXML = ReflectionUtils.invokeMethod(prettyFormat, converter, generatedXML);

        String expectedXML =
                "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                "<object>\n" +
                "   <string name=\"firstName\">John</string>\n" +
                "   <string name=\"lastName\">Doe</string>\n" +
                "   <number name=\"age\">30</number>\n" +
                "   <boolean name=\"isEmployed\">true</boolean>\n" +
                "   <null name=\"hobbies\"/>\n" +
                "   <array name=\"phones\">\n" +
                "      <number>9000012345</number>\n" +
                "      <number>9000012346</number>\n" +
                "   </array>\n" +
                "</object>";

        assertEquals(expectedXML, formattedXML);
    }

    @Test
    public void test_convertJSONtoXML() throws IOException  {
        File jsonFile = new File(XMLJSONConverterITest.class.getResource("/example.json").getFile());
        File expectedXmlFile = new File(XMLJSONConverterITest.class.getResource("/example.xml").getFile());
        File tempXmlFile = File.createTempFile("example-", ".xml");
        converter.convertJSONtoXML(jsonFile, tempXmlFile);

        try(BufferedReader tempXmlReader = Files.newBufferedReader(Paths.get(tempXmlFile.toURI()));
            BufferedReader xmlReader = Files.newBufferedReader(Paths.get(expectedXmlFile.toURI()))) {
            String generatedXml = tempXmlReader.lines().collect(Collectors.joining("\n"));
            String expectedXml = xmlReader.lines().collect(Collectors.joining("\n"));
            assertEquals(expectedXml, generatedXml);
        }

        tempXmlFile.deleteOnExit();
    }

    @Test(expected = JsonParseException.class)
    public void test_convertJSONtoXML_for_bad_JSON() throws IOException {
        File jsonFile = new File(XMLJSONConverterITest.class.getResource("/bad.json").getFile());
        File tempXmlFile = File.createTempFile("example-", ".xml");

        try {
            converter.convertJSONtoXML(jsonFile, tempXmlFile);
        } finally {
            tempXmlFile.deleteOnExit();
        }
    }
}
