package com.risksense.services;

import com.risksense.converters.ConverterFactory;
import com.risksense.services.impl.JsonToXmlServiceImpl;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.junit.Assert.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = {JsonToXmlServiceImpl.class, ConverterFactory.class})
public class JsonToXmlServiceTest {
    private JsonToXmlService service;

    @Autowired
    public void setService(JsonToXmlService service) {
        this.service = service;
    }

    @Test(expected = IllegalArgumentException.class)
    public void test_convertToXML_for_null() {
        service.convertToXML(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void test_convertToXML_for_empty() {
        service.convertToXML("");
    }

    @Test
    public void test_convertToXML_for_bad_JSON() {
        try {
            service.convertToXML("{{}}");
        } catch (Exception ex) {
            assertTrue(ex.getCause() instanceof com.fasterxml.jackson.core.JsonParseException);
        }
    }

    @Test
    public void test_convertToXML_for_good_JSON() {
        String expectedXml =
                "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                "<object>\n" +
                "   <string name=\"name\">John Doe</string>\n" +
                "</object>";

        String xml = service.convertToXML("{\"name\": \"John Doe\"}");
        
        assertEquals(expectedXml, xml);
    }
}
