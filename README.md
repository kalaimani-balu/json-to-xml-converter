# converters

This project provides API for converting JSON file to XML file with following format specification. Here is an example conversion from JSON to XML,

JSON File:
```json
{
  "organization" : {
    "name" : "RiskSense",
    "type" : "Inc",
    "building_number" : 4,
    "floating" : -17.4,
    "null_test": null
  },
  "security_related" : true,
  "array_example0" : ["red", "green", "blue", "black"],
  "array_example1" : [1, "red", [{ "nested" : true}], { "obj" : false}]
}
``` 
XML file:
```xml
<?xml version="1.0" encoding="UTF-8"?>
<object>
   <object name="organization">
      <string name="name">RiskSense</string>
      <string name="type">Inc</string>
      <number name="building_number">4</number>
      <number name="floating">-17.4</number>
      <null name="null_test"/>
   </object>
   <boolean name="security_related">true</boolean>
   <array name="array_example0">
      <string>red</string>
      <string>green</string>
      <string>blue</string>
      <string>black</string>
   </array>
   <array name="array_example1">
      <number>1</number>
      <string>red</string>
      <array>
         <object>
            <boolean name="nested">true</boolean>
         </object>
      </array>
      <object>
         <boolean name="obj">false</boolean>
      </object>
   </array>
</object>
```
#####Steps to build

> git clone https://github.com/kalaimani-balu/converters.git

> cd converters

> chmod +x build

> ./build

After a successful build, the target jar and pom files will be placed into local maven directory.
Then you can use this library in your project as maven dependency like below,
```xml
<dependency>
    <groupId>com.risksense</groupId>
    <artifactId>converters</artifactId>
    <version>1.0</version>
</dependency>
```
Here is a trivial example of how you can use this library with spring boot application,
 ```java
import com.risksense.converters.XMLJSONConverterI;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Service;

import java.io.File;

@Service
@ComponentScan("com.risksense.converters")
public class SomeService {
    private XMLJSONConverterI converter;

    @Autowired
    public void setConverter(XMLJSONConverterI converter) {
        this.converter = converter;
    }

    public void doConversion(String jsonPath, String xmlPath) throws IOException {
        converter.convertJSONtoXML(new File(jsonPath), new File(xmlPath));
    }
}
```
