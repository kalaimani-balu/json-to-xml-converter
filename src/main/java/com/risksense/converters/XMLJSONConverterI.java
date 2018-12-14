package com.risksense.converters;


import java.io.File;
import java.io.IOException;

/**
 * This interface provides methods that are required for creating a converter from JSON to XML.
 */
public interface XMLJSONConverterI {

    /**
     * Reads in the JSON from the given file and outputs the data, converted to
     * XML, to the given file. Exceptions are thrown by this method so that the
     * caller can clean up the before exiting.
     *
     * @param json {@link File} from which to read JSON data.
     * @param xml {@link File} from which to write XML data.
     * @throws IOException
     */
    void convertJSONtoXML(File json, File xml) throws IOException;
}
