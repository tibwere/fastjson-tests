package it.uniroma2.dicii.isw2.fastjson;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.annotation.JSONField;
import com.alibaba.fastjson.serializer.JSONSerializer;
import com.alibaba.fastjson.serializer.SerializeWriter;
import com.alibaba.fastjson.serializer.SerializerFeature;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.experimental.runners.Enclosed;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;

/* Original class: https://github.com/alibaba/fastjson/blob/1.2.79/src/test/java/com/alibaba/json/bvt/serializer/MapTest.java */
@RunWith(value = Enclosed.class)
public class MapTest {

    @RunWith(value = Parameterized.class)
    public static class TypeNoSortMapTest {

        private static Map<SerializerFeature, Boolean> features;
        private static String exceptionMsg;

        private boolean ordered;
        private Map<String, Object> objects;
        private String expected;

        public TypeNoSortMapTest(boolean ordered, Map<String, Object> objects, String expected) {
            configure(ordered, objects, expected);
        }

        private void configure(boolean ordered, Map<String, Object> objects, String expected) {
            this.ordered = ordered;
            this.objects = objects;
            this.expected = expected;
        }

        @BeforeClass
        public static void setupEnvironment() {
            features = new HashMap<>();
            features.put(SerializerFeature.SortField, false);
            features.put(SerializerFeature.UseSingleQuotes, true);
            exceptionMsg = "maybe circular references";
        }

        @Parameterized.Parameters
        public static Collection<Object[]> testCasesTuples() {
            Map<String, Object> objs = new HashMap<>();
            objs.put("name", "jobs");
            objs.put("id", 33);

            return Arrays.asList(new Object[][] {
                    {true, objs, "{'name':'jobs','id':33}"}
            });
        }

        @Test
        public void testNoSort(){
            JSONObject obj = new JSONObject(this.ordered);
            obj.putAll(this.objects);
            String text = toJSONString(obj);
            assertEquals(this.expected, text);
        }

        public static String toJSONString(Object object) {
            SerializeWriter out = new SerializeWriter();

            try {
                JSONSerializer serializer = new JSONSerializer(out);
                features.forEach(serializer::config);
                serializer.write(object);

                return out.toString();
            } catch (StackOverflowError e) {
                throw new JSONException(exceptionMsg, e);
            } finally {
                out.close();
            }
        }
    }

    @RunWith(value = Parameterized.class)
    public static class TypeNullMapTest {

        private boolean ordered;
        private Map<String, Object> objects;
        private SerializerFeature[] features;
        private String expected;

        public TypeNullMapTest(boolean ordered, Map<String, Object> objects, SerializerFeature []features, String expected) {
            configure(ordered, objects, features, expected);
        }

        private void configure(boolean ordered, Map<String, Object> objects, SerializerFeature []features, String expected) {
            this.ordered = ordered;
            this.objects = objects;
            this.features = features;
            this.expected = expected;
        }

        @Parameterized.Parameters
        public static Collection<Object[]> testCasesTuples() {
            Map<String, Object> objs = new HashMap<>();
            objs.put("name", null);

            SerializerFeature []features = new SerializerFeature[] {
                    SerializerFeature.WriteMapNullValue
            };

            return Arrays.asList(new Object[][] {
                    {true, objs, features, "{\"name\":null}"}
            });
        }

        @Test
        public void testNull() {
            JSONObject obj = new JSONObject(this.ordered);
            obj.putAll(this.objects);
            String text = JSON.toJSONString(obj, this.features);
            assertEquals(this.expected, text);
        }
    }

    @RunWith(value = Parameterized.class)
    public static class OnJSONFieldTypeMapTest {

        private Map<String, Object> objects;
        private String expected;

        public OnJSONFieldTypeMapTest(Map<String, Object> objects, String expected) {
            configure(objects, expected);
        }

        private void configure(Map<String, Object> objects, String expected) {
            this.objects = objects;
            this.expected = expected;
        }

        @Parameterized.Parameters
        public static Collection<Object[]> testCasesTuples() {
            Map<String, Object> objs = new HashMap<>();
            objs.put("Ariston", null);

            return Arrays.asList(new Object[][] {
                    {objs, "{\"map\":{\"Ariston\":null}}"}
            });
        }

        @Test
        public void testOnJSONField() {
            MapNullValue mapNullValue = new MapNullValue();
            mapNullValue.setMap(this.objects);
            String json = JSON.toJSONString(mapNullValue);
            assertEquals(this.expected, json);
        }
    }

    static class MapNullValue {
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
