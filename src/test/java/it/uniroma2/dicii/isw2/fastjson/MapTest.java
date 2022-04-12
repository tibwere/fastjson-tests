package it.uniroma2.dicii.isw2.fastjson;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.annotation.JSONField;
import com.alibaba.fastjson.serializer.JSONSerializer;
import com.alibaba.fastjson.serializer.SerializeWriter;
import com.alibaba.fastjson.serializer.SerializerFeature;
import org.junit.Assume;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;

/* Original class: https://github.com/alibaba/fastjson/tree/1.2.79/src/test/java/com/alibaba/json/bvt/serializer/MapTest.java */
@RunWith(value = Parameterized.class)
public class MapTest {

    enum Type {TEST_NO_SORT, TEST_NULL, TEST_JSON_FIELD};

    private static Map<SerializerFeature, Boolean> configs;
    private static String exceptionMsg;

    private Type type;
    private boolean ordered;
    private Map<String, Object> elements;
    private String expected;
    private SerializerFeature [] features;

    public MapTest(Type type, boolean ordered, Map<String, Object> elements, SerializerFeature [] features, String expected) {
        this.type = type;
        this.ordered = ordered;
        this.elements = elements;
        this.features = features;
        this.expected = expected;
    }

    @BeforeClass
    public static void setupEnvironment() {
        configs = new HashMap<>();
        configs.put(SerializerFeature.SortField, false);
        configs.put(SerializerFeature.UseSingleQuotes, true);
        exceptionMsg = "maybe circular references";
    }

    @Parameterized.Parameters
    public static Collection<Object[]> configure() {
        Map<String, Object> noSortMap = new HashMap<>();
        noSortMap.put("name", "jobs");
        noSortMap.put("id", 33);

        Map<String, Object> nullMap = new HashMap<>();
        nullMap.put("name", null);

        Map<String, Object> jsonFieldMap = new HashMap<>();
        jsonFieldMap.put("Ariston", null);

        return Arrays.asList(new Object[][] {
                {Type.TEST_NO_SORT, true, noSortMap, null, "{'name':'jobs','id':33}"},
                {Type.TEST_NULL, true, nullMap, new SerializerFeature[] {SerializerFeature.WriteMapNullValue}, "{\"name\":null}"},
                {Type.TEST_JSON_FIELD, true, jsonFieldMap, null, "{\"map\":{\"Ariston\":null}}"}
        });
    }

    @Test
    public void testNoSort() {
        Assume.assumeTrue(this.type == Type.TEST_NO_SORT);

        JSONObject obj = new JSONObject(this.ordered);
        obj.putAll(this.elements);
        assertEquals(this.expected, toJSONString(obj));
    }

    @Test
    public void testNull() throws Exception {
        Assume.assumeTrue(this.type == Type.TEST_NULL);

        JSONObject obj = new JSONObject(this.ordered);
        obj.putAll(this.elements);
        String text = JSON.toJSONString(obj, this.features);
        assertEquals(this.expected, text);
    }

    public static final String toJSONString(Object object) {
        SerializeWriter out = new SerializeWriter();

        try {
            JSONSerializer serializer = new JSONSerializer(out);
            configs.forEach(serializer::config);
            serializer.write(object);
            return out.toString();
        } catch (StackOverflowError e) {
            throw new JSONException(exceptionMsg, e);
        } finally {
            out.close();
        }
    }

    @Test
    public void testOnJSONField() {
        Assume.assumeTrue(this.type == Type.TEST_JSON_FIELD);

        MapNullValue mapNullValue = new MapNullValue();
        mapNullValue.setMap(this.elements);
        String json = JSON.toJSONString(mapNullValue);
        assertEquals(this.expected, json);
    }

    class MapNullValue {
        @JSONField(serialzeFeatures = {SerializerFeature.WriteMapNullValue})
        private Map map;

        public Map getMap() {
            return map;
        }

        public void setMap( Map map ) {
            this.map = map;
        }
    }

}
