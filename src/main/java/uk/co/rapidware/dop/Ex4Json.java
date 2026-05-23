package uk.co.rapidware.dop;

import java.util.List;
import java.util.Map;

/**
 * Exercise 4: JSON value model — a realistic worked example
 *
 * Goal: model the full JSON value space as a sealed type hierarchy, then write
 * serialisation and querying functions over it — no external libraries.
 *
 * This mirrors how real libraries like Gson/Jackson represent parsed JSON internally,
 * but expressed cleanly with modern Java data-oriented style.
 *
 * Key concepts:
 *  - combining primitive records with collection-based records
 *  - pattern matching over heterogeneous recursive structures
 *  - writing a "fold" / "visitor" without the Visitor design pattern
 *
 * Tasks:
 *  1. Run main() — study how the sample document is constructed and serialised.
 *  2. Implement query(JsonValue root, String path) where path is dot-separated keys,
 *     e.g. "user.address.city" returns the nested JsonValue or JsonNull if missing.
 *  3. Implement merge(JsonObject a, JsonObject b) — b's keys win on conflict.
 *  4. Implement transform(JsonValue v, Function<JsonValue,JsonValue> fn) that applies
 *     fn to every leaf value recursively.
 *  5. Write a prettyPrint(JsonValue v, int indent) that produces indented JSON output.
 */
public class Ex4Json {

    sealed interface JsonValue permits
        Ex4Json.JsonNull, Ex4Json.JsonBool, Ex4Json.JsonNumber,
        Ex4Json.JsonString, Ex4Json.JsonArray, Ex4Json.JsonObject {}

    record JsonNull()                          implements JsonValue {}
    record JsonBool(boolean value)             implements JsonValue {}
    record JsonNumber(double value)            implements JsonValue {}
    record JsonString(String value)            implements JsonValue {}
    record JsonArray(List<JsonValue> elements) implements JsonValue {}
    record JsonObject(Map<String, JsonValue> fields) implements JsonValue {}

    static String serialize(JsonValue v) {
        return switch (v) {
            case JsonNull   _           -> "null";
            case JsonBool(var b)        -> String.valueOf(b);
            case JsonNumber(var n)      -> n % 1 == 0 ? String.valueOf((long) n) : String.valueOf(n);
            case JsonString(var s)      -> "\"" + s.replace("\"", "\\\"") + "\"";
            case JsonArray(var elems)   -> {
                var sb = new StringBuilder("[");
                for (int i = 0; i < elems.size(); i++) {
                    if (i > 0) sb.append(",");
                    sb.append(serialize(elems.get(i)));
                }
                yield sb.append("]").toString();
            }
            case JsonObject(var fields) -> {
                var sb = new StringBuilder("{");
                var iter = fields.entrySet().iterator();
                while (iter.hasNext()) {
                    var entry = iter.next();
                    sb.append("\"").append(entry.getKey()).append("\":")
                      .append(serialize(entry.getValue()));
                    if (iter.hasNext()) sb.append(",");
                }
                yield sb.append("}").toString();
            }
        };
    }

    // TODO Exercise task 2: implement query(JsonValue root, String dotPath)
    static JsonValue query(JsonValue root, String path) {
        throw new UnsupportedOperationException("implement me");
    }

    public static void main(String[] args) {
        JsonValue doc = new JsonObject(Map.of(
            "name",    new JsonString("Alice"),
            "age",     new JsonNumber(30),
            "active",  new JsonBool(true),
            "scores",  new JsonArray(List.of(new JsonNumber(95), new JsonNumber(87), new JsonNumber(92))),
            "address", new JsonObject(Map.of(
                "city",    new JsonString("London"),
                "postcode", new JsonString("EC1A 1BB")
            ))
        ));

        System.out.println(serialize(doc));
    }
}
