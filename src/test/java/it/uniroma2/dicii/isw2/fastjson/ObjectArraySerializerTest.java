package it.uniroma2.dicii.isw2.fastjson;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.JSONSerializer;
import com.alibaba.fastjson.serializer.SerializeWriter;
import org.junit.Assume;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Collection;

import static org.junit.Assert.assertEquals;

/* Original class: https://github.com/alibaba/fastjson/tree/1.2.79/src/test/java/com/alibaba/json/bvt/serializer/ObjectArraySerializerTest.java */
@RunWith(value = Parameterized.class)
public class ObjectArraySerializerTest {

    enum Type {WRITE, TO_JSON_STRING};

    private Type type;
    private int initialSize;
    private Object [] objects;
    private String expected;
    private boolean isPrettyFormat;

    public ObjectArraySerializerTest(Type type, int initialSize, Object [] objects, boolean isPrettyFormat, String expected) {
        this.type = type;
        this.initialSize = initialSize;
        this.objects = objects;
        this.isPrettyFormat = isPrettyFormat;
        this.expected = expected;
    }

    @Parameterized.Parameters
    public static Collection<Object[]> configure() {
        /* Tests 0, 1 and 2 do not use the 'isPrettyFormat' parameter */
        final boolean dontCareBoolean = false;
        /* Test 3 does not use the 'initialSize' parameter */
        final int dontCareInteger = 1;

        return Arrays.asList(new Object[][] {
                {Type.WRITE, 1, new Object[] {"a12", "b34"}, dontCareBoolean, "[\"a12\",\"b34\"]"},
                {Type.WRITE, 1, new Object[] {}, dontCareBoolean, "[]"},
                {Type.WRITE, 1,new Object[] { null, null }, dontCareBoolean, "[null,null]"},
                {Type.TO_JSON_STRING, dontCareInteger, new Object[] { null, null }, false, "[null,null]"}
        });
    }

    @Test
    public void test012() {
        Assume.assumeTrue(this.type == Type.WRITE);
        SerializeWriter out = new SerializeWriter(this.initialSize);
        JSONSerializer.write(out, this.objects);
        assertEquals(this.expected, out.toString());
    }

    @Test
    public void test3() {
        Assume.assumeTrue(this.type == Type.TO_JSON_STRING);
        assertEquals(this.expected, JSON.toJSONString(this.objects, this.isPrettyFormat));
    }
}
