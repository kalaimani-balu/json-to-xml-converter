# JSON To XML Converter

This library provides REST API for converting JSON file to XML file with following format specification. Here is an example conversion from JSON to XML,

JSON input:
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
XML output:
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
**Steps to build and run the app**

_Prerequisites: Java 8 and Apache Maven 3.6.0_

> git clone https://github.com/kalaimani-balu/json-to-xml-converter.git

> cd json-to-xml-converter

> chmod +x build.sh

> ./build.sh

> java -jar target/json-to-xml-converter-1.0.jar

After a successful build, You can copy the target jar anywhere and run using above command.

Post your JSON to http:localhost:8080/json-to-xml-converter and you'll get the data back, converted to XML.