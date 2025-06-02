package com.steve.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.*;

public class Json {
    private static ObjectMapper myObjectMapper =defaultObjectMapper();

    public static ObjectMapper defaultObjectMapper(){
        ObjectMapper objectMapper= new ObjectMapper();
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES,false); // Ensures that it does not crash if properties are missing.
        return objectMapper;
    }
    public static JsonNode parse(String jsonSrc) throws JsonProcessingException {
        return myObjectMapper.readTree(jsonSrc);
    }
    public static <A>A fromJson(JsonNode node, Class<A> className) throws JsonProcessingException {
        return myObjectMapper.treeToValue(node,
                className);
    }
    public static JsonNode toJson(Object object){
        return myObjectMapper.valueToTree(object);
    }
    private static String generateJson(Object obj, boolean pretty) throws JsonProcessingException {
        ObjectWriter objectWriter = pretty ?myObjectMapper.writer().with(SerializationFeature.INDENT_OUTPUT): myObjectMapper.writer();
        return objectWriter.writeValueAsString(obj);

    }
    public static String stringify(JsonNode node) throws JsonProcessingException {
return generateJson(node, false);
    }
    public static String stringifyPretty(JsonNode node) throws JsonProcessingException {
        return generateJson(node, true);
    }
}
