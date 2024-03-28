package uj.wmii.pwj.collections;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


public class JsonFactory implements JsonMapper {

    private StringBuilder space;

    JsonFactory() {
        space = new StringBuilder();
    }

    public String getType(Object obj) {
        if (obj instanceof String) return "String";
        if (obj instanceof Boolean) return "Boolean";
        if (obj instanceof Number) return "Number";
        if (obj instanceof List) return "List";
        return "Map";
    }

    void format() {
        space.replace(space.length() - 2, space.length(), "");
    }

    StringBuilder deleteComma(StringBuilder out) {
        out = new StringBuilder(out.toString().trim());
        out.replace(out.length() - 1, out.length(), "");
        if (out.charAt(out.length() - 1) == '}') out.append("\n").append(space);
        return out;
    }

    StringBuilder menageList(Object obj){
        StringBuilder out = new StringBuilder();
        if (obj == null || ((List<?>) obj).isEmpty()) return out.append("[],\n");
        out.append("[");
        for (Object listElement : (List<?>) obj) out.append(inListElement(listElement));
        out = deleteComma(out).append("],\n");
        return out;
    }

    String inListElement(Object obj) {
        StringBuilder out = new StringBuilder();
        String type = getType(obj);

        switch (type) {
            case "Boolean", "Number" -> out.append(obj.toString() + ",");
            case "String" -> out.append("\"").append(obj.toString().replaceAll("[\\\\\"]", "\\\\$0")).append("\",");
            case "List" -> out.append(menageList(obj));
            case "Map" -> {
                out.append("\n");
                space.append("  ");
                out.append(space).append(toJson((Map<String, ?>) obj)).append(",");
                format();
                out.append(space);
            }
        }
        return out.toString();
    }

    String inMapElement(Object obj) {
        StringBuilder out = new StringBuilder();

        String type = getType(obj);
        switch (type) {
            case "Boolean", "Number" -> out.append(obj.toString() + ",\n");
            case "String" -> out.append("\"").append(obj.toString().replaceAll("[\\\\\"]", "\\\\$0")).append("\",\n");
            case "List" -> out.append(menageList(obj));
            case "Map" -> out.append(toJson((Map<String, ?>) obj)).append(",\n");
        }
        return out.toString();
    }


    @Override
    public String toJson(Map<String, ?> map) {
        if (map == null || map.isEmpty()) return "{}\n";

        StringBuilder out = new StringBuilder();
        out.append("{\n");
        space.append("  ");

        List<String> keysList = new ArrayList<>(map.keySet());
        for (String key : keysList) {
            Object val = map.get(key);
            out.append(space).append("\"").append(key).append("\": ");
            out.append(inMapElement(val));
        }
        out = deleteComma(out).append("\n");
        format();
        out.append(space).append("}");

        return out.toString();
    }


}