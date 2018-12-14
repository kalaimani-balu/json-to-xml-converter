package com.risksense.services.impl;

import com.risksense.converters.XMLJSONConverterI;
import com.risksense.services.JsonToXmlService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Collectors;

/**
 * {@inheritDoc}
 */
@Service
public class JsonToXmlServiceImpl implements JsonToXmlService {
    private XMLJSONConverterI converter;

    @Autowired
    public void setConverter(XMLJSONConverterI converter) {
        this.converter = converter;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String convertToXML(String json) {
        if (null == json || "".equals(json))
            throw new IllegalArgumentException("Input JSON can't be null or empty");
        try {
            return convert(json);
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }

    // do the conversion and clean up
    private String convert(String json) throws IOException {
        File jsonFile = File.createTempFile("tmp", ".json");
        File xmlFile = File.createTempFile("tmp", ".xml");

        try(BufferedWriter writer = Files.newBufferedWriter(Paths.get(jsonFile.toURI()));
            BufferedReader reader = Files.newBufferedReader(Paths.get(xmlFile.toURI()))) {
            writer.write(json);
            writer.flush();
            converter.convertJSONtoXML(jsonFile, xmlFile);
            return reader.lines().collect(Collectors.joining("\n"));
        } finally {
            Files.delete(Paths.get(jsonFile.toURI()));
            Files.delete(Paths.get(xmlFile.toURI()));
        }
    }
}
