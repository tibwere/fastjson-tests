package it.uniroma2.dicii.isw2.fastjson;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.JSONSerializer;
import com.alibaba.fastjson.serializer.SerializeWriter;
import org.junit.Test;
import org.junit.experimental.runners.Enclosed;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Collection;

import static org.junit.Assert.assertEquals;

/* Original class: https://github.com/alibaba/fastjson/tree/1.2.79/src/test/java/com/alibaba/json/bvt/serializer/ObjectArraySerializerTest.java */
@RunWith(value = Enclosed.class)
public class ObjectArraySerializerTest {

    @RunWith(value = Parameterized.class)
    public static class Type012ObjectArraySerializerTest {

        private int initialSize;
        private Object [] objects;
        private String expected;

        public Type012ObjectArraySerializerTest(int initialSize, Object[] objects, String expected) {
            configure(initialSize, objects, expected);
        }

        private void configure(int initialSize, Object[] objects, String expected) {
            this.initialSize = initialSize;
            this.objects = objects;
            this.expected = expected;
        }

        @Parameterized.Parameters
        public static Collection<Object[]> testCasesTuples() {
            return Arrays.asList(new Object[][] {
                    {1, new Object[] {"a12", "b34"}, "[\"a12\",\"b34\"]"},
                    {1, new Object[] {}, "[]"},
                    {1,new Object[] { null, null }, "[null,null]"},
            });
        }

        @Test
        public void test012() {
            SerializeWriter out = new SerializeWriter(this.initialSize);
            JSONSerializer.write(out, this.objects);
            assertEquals(this.expected, out.toString());
        }
    }

    @RunWith(value = Parameterized.class)
    public static class Type3ObjectArraySerializerTest {

        private Object [] objects;
        private boolean isPrettyFormat;
        private String expected;

        public Type3ObjectArraySerializerTest(Object[] objects, boolean isPrettyFormat, String expected) {
            configure(objects, isPrettyFormat, expected);
        }

        private void configure(Object []objects, boolean isPrettyFormat, String expected) {
            this.objects = objects;
            this.isPrettyFormat = isPrettyFormat;
            this.expected = expected;
        }

        @Parameterized.Parameters
        public static Collection<Object[]> testCasesTuples() {
            return Arrays.asList(new Object[][] {
                    {new Object[] { null, null }, false, "[null,null]"}
            });
        }

        @Test
        public void test3() {
            assertEquals(this.expected, JSON.toJSONString(this.objects, this.isPrettyFormat));
        }
    }

}
