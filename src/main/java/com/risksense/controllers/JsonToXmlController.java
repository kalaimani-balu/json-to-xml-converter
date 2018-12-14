package com.risksense.controllers;

import com.risksense.services.JsonToXmlService;

import org.springframework.http.MediaType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * REST API for JSON to XML conversion.
 */
@RestController
public class JsonToXmlController {
    private final JsonToXmlService service;

    @Autowired
    JsonToXmlController(JsonToXmlService service) {
        this.service = service;
    }

    @RequestMapping(value="/json-to-xml-converter",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = {MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE})
    public String convertToXML(@RequestBody String json) {
        return service.convertToXML(json);
    }
}
