package com.demo.Utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.*;

public class Json {
    private static ObjectMapper myObjMapper = getDefaultObjMapper();

    private static ObjectMapper getDefaultObjMapper(){
        ObjectMapper om =  new ObjectMapper();
        om.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        // It will stop parsing from crashing even if some property is missing
        return om;
    }

    // To convert a json source to JsonNode Type : Deserialization
    public static JsonNode parse(String jsonSrc) throws JsonProcessingException {
        return myObjMapper.readTree(jsonSrc);
    }

    // Convert a JsonNode to Key Value Tree
    public static <A> A fromJson(JsonNode node , Class<A> clazz) throws JsonProcessingException {
        return myObjMapper.treeToValue(node, clazz);
    }

    // To convert a string object to JsonNode : Serialization
    public static JsonNode serialize(Object obj) {
        return myObjMapper.valueToTree(obj);
    }

    //Convert a JsonNode to string to print
    public static String stringify(JsonNode node) throws JsonProcessingException {
        return generateJson(node, false);
    }

    //Convert a JsonNode to string to print with indentation
    public static String stringifyPretty(JsonNode node) throws JsonProcessingException {
        return generateJson(node, true);
    }

    //Main function to perform JsonNode to String !!
    private static String generateJson(Object o, boolean pretty) throws JsonProcessingException {
        ObjectWriter objWriter = myObjMapper.writer();
        if (pretty) {
            objWriter = objWriter.with(SerializationFeature.INDENT_OUTPUT);
        }
        return objWriter.writeValueAsString(o);
    }
}
