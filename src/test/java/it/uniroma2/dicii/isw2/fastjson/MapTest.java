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
    public static class MapTestNoSortTests {

        private static SerializeWriter out;
        private static String exceptionString;
        private static Map<SerializerFeature, Boolean> features;

        private JSONObject obj;
        private String expected;

        public MapTestNoSortTests(boolean ordered, Map<String, Object> objects, String expected) {
            configure(ordered, objects, expected);
        }

        public void configure(boolean ordered, Map<String, Object> objects, String expected) {
            this.obj = new JSONObject(ordered);
            objects.forEach((k,v) -> obj.put(k,v));
            this.expected = expected;
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

        @BeforeClass
        public static void setupEnvironment() {
            out = new SerializeWriter();
            exceptionString = "maybe circular references";

            features = new HashMap<>();
            features.put(SerializerFeature.SortField, false);
            features.put(SerializerFeature.UseSingleQuotes, true);
        }

        public static String toJSONString(Object object) {
            try {
                JSONSerializer serializer = new JSONSerializer(out);
                features.forEach(serializer::config);
                serializer.write(object);

                return out.toString();
            } catch (StackOverflowError e) {
                throw new JSONException(exceptionString, e);
            } finally {
                out.close();
            }
        }

        @Test
        public void testNSort() {
            assertEquals(this.expected, toJSONString(this.obj));
        }
    }

    @RunWith(value = Parameterized.class)
    public static class MapTestNullTests {
        private JSONObject obj;
        private String expected;

        public MapTestNullTests(boolean ordered, Map<String, Object> objects, String expected) {
            configure(ordered, objects, expected);
        }

        public void configure(boolean ordered, Map<String, Object> objects, String expected) {
            this.obj = new JSONObject(ordered);
            objects.forEach((k,v) -> obj.put(k,v));
            this.expected = expected;
        }

        @Parameterized.Parameters
        public static Collection<Object[]> testCasesTuples() {
            Map<String, Object> objs = new HashMap<>();
            objs.put("name", null);

            return Arrays.asList(new Object[][] {
                    {true, objs, "{\"name\":null}"}
            });
        }

        @Test
        public void testNull() {
            /*
             * WriteMapNullValue is not a parameter of the test because the test itself
             * requires to test null elements inside the JSONObject
             */
            String actual = JSON.toJSONString(this.obj, SerializerFeature.WriteMapNullValue);
            assertEquals(this.expected, actual);
        }
    }

    @RunWith(value = Parameterized.class)
    public static class MapTestOnJsonFieldTests {

        private MapNullValue map;
        private String expected;

        public MapTestOnJsonFieldTests(Map<String, Object> objects, String expected) {
            configure(objects, expected);
        }

        public void configure(Map<String, Object> objects, String expected) {
            this.map = new MapNullValue();
            this.map.setMap(objects);
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
            assertEquals(this.expected, JSON.toJSONString(this.map));
        }
    }

    static class MapNullValue {
        @JSONField(serialzeFeatures = {SerializerFeature.WriteMapNullValue})
        private Map<String, Object> map;

        public Map<String, Object> getMap() {
            return map;
        }

        public void setMap(Map<String, Object> map) {
            this.map = map;
        }
    }
}
