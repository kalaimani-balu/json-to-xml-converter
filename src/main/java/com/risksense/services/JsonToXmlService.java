package com.risksense.services;

/**
 * This interface provides method that converts the data from XML to JSON.
 */
public interface JsonToXmlService {
    /**
     * Reads in the JSON from the given string and returns the data, converted to XML.
     * @param json to be converted to XML
     * @return converted XML
     */
    String convertToXML(String json);
}
