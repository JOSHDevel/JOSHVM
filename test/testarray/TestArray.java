import java.nio.*;
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

        String [][][] ar1 = new String[10][200][1];
        testArrayType(ar1);        
        float [][][] ar2 = new float[10][200][5];
        testArrayType(ar2);        
        long [][][] ar3 = new long[10][200][1];
        testArrayType(ar3);        
        boolean [][] ar5 = new boolean[5][200];
        testArrayType(ar5);        
        byte [][][] ar6 = new byte[10][0][10];
        testArrayType(ar6);        
        short [] ar7 = new short[0];
        testArrayType(ar7);        
        int [][][] ar8 = new int[10][200][1];
        testArrayType(ar8);        
    }

    private static void testArrayType(Object ar) {
        System.out.println("Array type:"+dataTypeOf(ar).toStringName());
        System.out.println("Dimension: "+computeNumDimensions(ar));
        System.out.println("Number of elements: "+computeNumElements(computeShapeOf(ar)));
    }

    private static void fail(String s) throws Exception {
        System.out.println(s+" failed");
        throw new Exception();
    }

    static Class getComponentTypeByName(String className) {
        char[] name = className.toCharArray();
        for (int i = 0; i < name.length; i++) {
            if (name[i] != '[') {
                switch (name[i]) {
                    case 'Z': return Boolean.class;
                    case 'C': return Character.class;
                    case 'F': return Float.class;
                    case 'D': return Double.class;
                    case 'B': return Byte.class;
                    case 'S': return Short.class;
                    case 'I': return Integer.class;
                    case 'J': return Long.class;
                    case 'L': 
                        int begin = i + 1;
                    try {
                        for (int end = begin; end < name.length; end++) {
                            if (name[end] == ';') {
                                className = className.substring(begin, end);
                                return Class.forName(className);                                
                            }
                        }
                    } catch (ClassNotFoundException e) {}
                }
            }
        }
        return null;
    }

    static DataType dataTypeOf(Object o) {
        if (o != null) {
          Class c = o.getClass();
          // For arrays, the data elements must be a *primitive* type, e.g., an
          // array of floats is fine, but not an array of Floats.
          if (c.isArray()) {
            //Object arrObj = o;
            // while (c.isArray()) {
            //   //Array.get always get Wrapped value instead of the primitive type value
            //   arrObj = Array.get(arrObj, 0);
            //   c = arrObj.getClass();
            // }
            c = getComponentTypeByName(c.getName());
            if (Float.class.equals(c)) {
              return DataType.FLOAT32;
            } else if (Integer.class.equals(c)) {
              return DataType.INT32;
            } else if (Short.class.equals(c)) {
              return DataType.INT16;
            } else if (Byte.class.equals(c)) {
              return DataType.UINT8;
            } else if (Long.class.equals(c)) {
              return DataType.INT64;
            } else if (Boolean.class.equals(c)) {
              return DataType.BOOL;
            } else if (String.class.equals(c)) {
              return DataType.STRING;
            }
          } else {
            // For scalars, the type will be boxed.
            if (Float.class.equals(c) || o instanceof FloatBuffer) {
              return DataType.FLOAT32;
            } else if (Integer.class.equals(c) || o instanceof IntBuffer) {
              return DataType.INT32;
            } else if (Short.class.equals(c) || o instanceof ShortBuffer) {
              return DataType.INT16;
            } else if (Byte.class.equals(c)) {
              // Note that we don't check for ByteBuffer here; ByteBuffer payloads
              // are allowed to map to any type, and should be handled earlier
              // in the input/output processing pipeline.
              return DataType.UINT8;
            } else if (Long.class.equals(c) || o instanceof LongBuffer) {
              return DataType.INT64;
            } else if (Boolean.class.equals(c)) {
              return DataType.BOOL;
            } else if (String.class.equals(c)) {
              return DataType.STRING;
            }
          }
        }
        throw new IllegalArgumentException(
            "DataType error: cannot resolve DataType of " + o.getClass().getName());
      }
    
      /** Returns the shape of an object as an int array. */
      static int[] computeShapeOf(Object o) {
        int size = computeNumDimensions(o);
        
        int[] dimensions = new int[size];
        fillShape(o, 0, dimensions);
        return dimensions;
      }
    
      /** Returns the number of elements in a flattened (1-D) view of the tensor's shape. */
      static int computeNumElements(int[] shape) {
        int n = 1;
        for (int i = 0; i < shape.length; ++i) {
          n *= shape[i];
        }
        return n;
      }
    
      /** Returns the number of dimensions of a multi-dimensional array, otherwise 0. */
      static int computeNumDimensions(Object o) {
        if (o == null || !o.getClass().isArray()) {
          return 0;
        }
        if (Array.getLength(o) == 0) {
          throw new IllegalArgumentException("Array lengths cannot be 0.");
        }
        return 1 + computeNumDimensions(Array.get(o, 0));
      }
    
      /** Recursively populates the shape dimensions for a given (multi-dimensional) array. */
      static void fillShape(Object o, int dim, int[] shape) {
        if (shape == null || dim == shape.length) {
          return;
        }
        final int len = Array.getLength(o);
        if (shape[dim] == 0) {
          shape[dim] = len;
        } else if (shape[dim] != len) {
          throw new IllegalArgumentException(
                  new StringBuffer().append("Mismatched lengths (")
                  .append(shape[dim])
                  .append(" and ")
                  .append(len)
                  .append(") in dimension ")
                  .append(dim).toString());
        }
        for (int i = 0; i < len; ++i) {
          fillShape(Array.get(o, i), dim + 1, shape);
        }
      }
   
    private static boolean isBuffer(Object o) {
        return o instanceof Buffer;
      }
    
      private static boolean isByteBuffer(Object o) {
        return o instanceof ByteBuffer;
      }
}

