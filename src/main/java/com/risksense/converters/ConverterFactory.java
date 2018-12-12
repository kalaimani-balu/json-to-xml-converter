package com.risksense.converters;


import com.risksense.converters.impl.XMLJSONConverterImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Factory class for creating instances of {@link XMLJSONConverterI}.
 */
@Configuration
public class ConverterFactory {

    /**
     * @return an instance of {@link com.risksense.converters.impl.XMLJSONConverterImpl}.
     */
    @Bean
    public static XMLJSONConverterI createXMLJSONConverter() {
       return new XMLJSONConverterImpl();
    }
}
