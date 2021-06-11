import org.joshvm.util.Array;

public class TestArray {
    
    final static short testShortValues[] = {(short)0x1122, (short)0x01aa, (short)0xaa55, (short)0x55aa, (short)0xffff,
                                     (short)0x0000, (short)0x8000, (short)0x7fff, (short)0xfffe, (short)0x0001};
    final static int   testIntValues[] =   {0x11223344, 0xaa559977, 0xffffffff, 0x00000000, 0x8000};
    final static long  testLongValues[] =  {0x1122334455667788L, 0xffeeddccbbaa9988L};
    final static float testFloatValues[] = {1.23f, -3.24f, 0.0f, 900.2984f, 0.0000001f};
    final static String testObjValues[] = {"hello", "world", "foo", "bar"};
    final static boolean testBooleanValues[] = {true, false};
    final static char testCharValues[] = {'h', 'e', 'l', 'l', 'o', ' ', 'w', 'o', 'r', 'l', 'd'};
    final static double testDoubleValues[] = {1.23, -3.24, 0.0, 900.2984, 0.0000001f};
    final static byte testByteValues[] = {(byte)0x01, (byte)0x02, (byte)0x00, (byte)0xff, (byte)0x80, (byte)0x55, (byte)0xaa};

    public static void main(String[] args) throws Exception {
        if (Array.getLength(testShortValues) != testShortValues.length) fail("Short Array getLength");
        if (Array.getLength(testLongValues) != testLongValues.length) fail("Long Array getLength");
        if (Array.getLength(testIntValues) != testIntValues.length) fail("Int Array getLength");
        if (Array.getLength(testFloatValues) != testFloatValues.length) fail("Float Array getLength");
        if (Array.getLength(testDoubleValues) != testDoubleValues.length) fail("Double Array getLength");
        if (Array.getLength(testCharValues) != testCharValues.length) fail("Char Array getLength");
        if (Array.getLength(testObjValues) != testObjValues.length) fail("Obj Array getLength");
        if (Array.getLength(testByteValues) != testByteValues.length) fail("Byte Array getLength");
        if (Array.getLength(testBooleanValues) != testBooleanValues.length) fail("Boolean Array getLength");

        for (int i = 0; i < testShortValues.length; i++) {
            if (!Array.get(testShortValues, i).equals(new Short(testShortValues[i]))) {
                fail("Short Array get");
            }
        }

        for (int i = 0; i < testLongValues.length; i++) {
            if (!Array.get(testLongValues, i).equals(new Long(testLongValues[i]))) {
                fail("Long Array get");
            }
        }

        for (int i = 0; i < testIntValues.length; i++) {
            if (!Array.get(testIntValues, i).equals(new Integer(testIntValues[i]))) {
                fail("Int Array get");
            }
        }

        for (int i = 0; i < testFloatValues.length; i++) {
            if (!Array.get(testFloatValues, i).equals(new Float(testFloatValues[i]))) {
                fail("Float Array get");
            }
        }

        for (int i = 0; i < testDoubleValues.length; i++) {
            if (!Array.get(testDoubleValues, i).equals(new Double(testDoubleValues[i]))) {
                fail("Double Array get");
            }
        }

        for (int i = 0; i < testByteValues.length; i++) {
            if (!Array.get(testByteValues, i).equals(new Byte(testByteValues[i]))) {
                fail("Byte Array get");
            }
        }

        for (int i = 0; i < testCharValues.length; i++) {
            if (!Array.get(testCharValues, i).equals(new Character(testCharValues[i]))) {
                fail("Char Array get");
            }
        }

        for (int i = 0; i < testBooleanValues.length; i++) {
            if (!Array.get(testBooleanValues, i).equals(new Boolean(testBooleanValues[i]))) {
                fail("Boolean Array get");
            }
        }

        for (int i = 0; i < testObjValues.length; i++) {
            if (!Array.get(testObjValues, i).equals((String)testObjValues[i])) {
                fail("Obj Array get");
            }
        }

        System.out.println("Passed");
    }

    private static void fail(String s) throws Exception {
        System.out.println(s+" failed");
        throw new Exception();
    }
}

