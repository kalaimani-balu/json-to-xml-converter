package com.risksense.controllers;

import com.risksense.Application;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK, classes = Application.class)
@AutoConfigureMockMvc
public class JsonToXmlControllerTest {
    private MockMvc mvc;

    @Autowired
    public void setMvc(MockMvc mvc) {
        this.mvc = mvc;
    }

    @Test
    public void test_convertToXML_for_200() throws Exception {
        String json =
                "{" +
                    "\"firstName\":\"John\", " +
                    "\"lastName\":\"Doe\"," +
                    "\"age\":30," +
                    "\"isEmployed\":true," +
                    "\"hobbies\":null," +
                    "\"phones\":[9000012345, 9000012346]" +
                "}";

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

        mvc.perform(post("/json-to-xml-converter")
                .content(json)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().xml(expectedXML));
    }

    @Test
    public void test_convertToXML_for_400() throws Exception {
        mvc.perform(post("/json-to-xml-converter")
                .content("{\"BAD JSON\"}")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.status").value(400))
                .andExpect(MockMvcResultMatchers.jsonPath("$.error").value("Invalid JSON"));

    }
}
