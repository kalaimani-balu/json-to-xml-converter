package com.risksense.converters.impl;


import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Collectors;

import javax.xml.transform.*;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import com.risksense.converters.XMLJSONConverterI;

/**
 * This implementation provides methods that are required for creating a converter from XML to JSON.
 */
public class XMLJSONConverterImpl implements XMLJSONConverterI {
    /**
     * {@inheritDoc}
     */
    public void convertJSONtoXML(File json, File xml) throws IOException {
        if (null == json || null == xml)
            throw new IllegalArgumentException("Input files can't be null");

        try(BufferedReader reader = Files.newBufferedReader(Paths.get(json.toURI()));
            BufferedWriter writer = Files.newBufferedWriter(Paths.get(xml.toURI()))) {
            String jsonContent = reader.lines().collect(Collectors.joining());
            ObjectMapper mapper = new ObjectMapper();
            JsonNode node = mapper.readTree(jsonContent);
            writer.write(prettyFormat(convertJsonNodeToXML(node)));
            writer.flush();
        }
    }

    // Converts given JsonNode to XML formatted String
    private String convertJsonNodeToXML(JsonNode node) {
        if (node.isNull())
            return "<null/>";
        if (node.isArray())
            return String.format("<array>%s</array>", format(node));
        if (node.isObject())
            return String.format("<object>%s</object>", format(node));
        if (node.isNumber())
            return String.format("<number>%s</number>", node.asText());
        if (node.isBoolean())
            return String.format("<boolean>%s</boolean>", node.asText());
        if (node.isObject())
            return String.format("<object>%s</object>", node.asText());

        return String.format("<string>%s</string>", node.asText());
    }

    // traverse the node and format every field inside recursively
    private String format(JsonNode node) {
        if (node.isObject()) {
            StringBuilder builder = new StringBuilder();

            node.fields().forEachRemaining(entry -> {
                String field = entry.getKey();
                JsonNode child = entry.getValue();
                if (child.isObject()) {
                    builder.append(String.format("<object name=\"%s\">%s</object>", field, format(child)));
                } else if (child.isArray()) {
                    builder.append(String.format("<array name=\"%s\">%s</array>", field, format(child)));
                } else if (child.isNumber()) {
                    builder.append(String.format("<number name=\"%s\">%s</number>", field, format(child)));
                } else if (child.isBoolean()) {
                    builder.append(String.format("<boolean name=\"%s\">%s</boolean>", field, format(child)));
                } else if (child.isTextual()) {
                    builder.append(String.format("<string name=\"%s\">%s</string>", field, format(child)));
                } else if (child.isNull()) {
                    builder.append(String.format("<null name=\"%s\"/>", field));
                }
            });

            return builder.toString();
        }

        if (node.isArray()) {
            StringBuilder builder = new StringBuilder();
            node.forEach(child -> {
                if (child.isObject()) {
                    builder.append(String.format("<object>%s</object>", format(child)));
                } else if (child.isArray()) {
                    builder.append(String.format("<array>%s</array>", format(child)));
                } else if (child.isNumber()) {
                    builder.append(String.format("<number>%s</number>", format(child)));
                } else if (child.isBoolean()) {
                    builder.append(String.format("<boolean>%s</boolean>", format(child)));
                } else if (child.isTextual()) {
                    builder.append(String.format("<string>%s</string>", format(child)));
                } else if (child.isNull()) {
                    builder.append("<null/>");
                }
            });

            return builder.toString();
        }

        return node.asText();
    }

    // Beautifies the given XML String
    private static String prettyFormat(String input) {
        Source xmlInput = new StreamSource(new StringReader(input));
        StringWriter stringWriter = new StringWriter();

        try {
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.setOutputProperty(OutputKeys.DOCTYPE_PUBLIC, "yes");
            transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "3");
            transformer.transform(xmlInput, new StreamResult(stringWriter));
            return stringWriter.toString().trim();
        } catch (TransformerException e) {
            throw new RuntimeException(e);
        }
    }
}
