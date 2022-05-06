package it.uniroma2.dicii.isw2.fastjson;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.JSONSerializer;
import com.alibaba.fastjson.serializer.SerializeWriter;
import com.alibaba.fastjson.serializer.SerializerFeature;
import org.junit.Test;
import org.junit.experimental.runners.Enclosed;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;

/* Original class: https://github.com/alibaba/fastjson/tree/1.2.79/src/test/java/com/alibaba/json/bvt/serializer/ObjectArraySerializerTest.java */
@RunWith(value = Enclosed.class)
public class ObjectArraySerializerTest {

    @RunWith(value = Parameterized.class)
    public static class ObjectArraySerializerTest012Tests {
        private SerializeWriter out;
        private Object[] objects;
        private String expected;

        public ObjectArraySerializerTest012Tests(int initialSize, Object[] objects, String expected) {
            configure(initialSize, objects, expected);
        }

        public void configure(int initialSize, Object[] objects, String expected) {
            this.out = new SerializeWriter(initialSize);
            this.expected = expected;
            JSONSerializer.write(this.out, objects);
        }

        @Parameterized.Parameters
        public static Collection<Object[]> testCasesTuples() {
            return Arrays.asList(new Object[][] {
                    {1, new Object[] { "a12", "b34" }, "[\"a12\",\"b34\"]"},
                    {1, new Object[] {}, "[]"},
                    {1, new Object[] {null, null}, "[null,null]"}
            });
        }

        @Test
        public void test012() {
            assertEquals(this.expected, this.out.toString());
        }
    }

    @RunWith(value = Parameterized.class)
    public static class ObjectArraySerializerTest3Tests {
        private boolean prettyFormat;
        private Object[] objects;
        private String expected;

        public ObjectArraySerializerTest3Tests(boolean prettyFormat, Object[] objects, String expected) {
            configure(prettyFormat, objects, expected);
        }

        public void configure(boolean prettyFormat, Object[] objects, String expected) {
            this.prettyFormat = prettyFormat;
            this.objects = objects;
            this.expected = expected;
        }

        @Parameterized.Parameters
        public static Collection<Object[]> testCasesTuples() {
            return Arrays.asList(new Object[][] {
                    {false, new Object[] {null, null}, "[null,null]"}
            });
        }

        @Test
        public void test3() {
            assertEquals(this.expected, JSON.toJSONString(this.objects, this.prettyFormat));
        }
    }
}
