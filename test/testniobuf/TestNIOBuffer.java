import java.nio.ByteBuffer;
import java.nio.LongBuffer;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;
import java.nio.IntBuffer;

import java.nio.Buffer;
import java.nio.ByteOrder;

public class TestNIOBuffer {
    ByteBuffer failedBuffer = null;
    final static int BUFFERTYPE_INDIRECT = 0;
    final static int BUFFERTYPE_DIRECT = 1;

    
    final short testShortValues[] = {(short)0x1122, (short)0x01aa, (short)0xaa55, (short)0x55aa, (short)0xffff,
                                     (short)0x0000, (short)0x8000, (short)0x7fff, (short)0xfffe, (short)0x0001};
    final int   testIntValues[] =   {0x11223344, 0xaa559977, 0xffffffff, 0x00000000, 0x8000};
    final long  testLongValues[] =  {0x1122334455667788L, 0xffeeddccbbaa9988L};
    final float testFloatValues[] = {1.23f, -3.24f, 0.0f, 900.2984f, 0.0000001f};
	
	private ByteBuffer createIndirectByteBuffer() {
		byte[] src = new byte[20];
		for (int i = 0; i < 20; i++) {
			src[i] = (byte)(i+1);
		}

        return ByteBuffer.wrap(src);
    }

    private ByteBuffer createDirectByteBuffer() {
        byte[] src = new byte[20];
		for (int i = 0; i < 20; i++) {
			src[i] = (byte)(i+1);
		}
        return (ByteBuffer)ByteBuffer.allocateDirect(20).put(src).flip();
    }

    private void testByteBuffer() {
        
    	ByteBuffer byteBufferDirect;
        ByteBuffer byteBufferInDirect;
        
		byteBufferDirect = createDirectByteBuffer();

        byteBufferInDirect = createIndirectByteBuffer();

        byteBufferDirect.order(ByteOrder.LITTLE_ENDIAN);
        byteBufferDirect.putLong(2, (long)0x1122334455667778L);
        if ((long)0x1122334455667778L != byteBufferDirect.getLong(2)) {
            failed("[TestByteBuffer]: ByteBuffer.getLong()#Direct little-endian failed");
        }
        byteBufferDirect.order(ByteOrder.BIG_ENDIAN);
        byteBufferDirect.putLong(10,(long)0x66778899aabbccddL);
        if ((long)0x66778899aabbccddL != byteBufferDirect.getLong(10)) {
            failed("[TestByteBuffer]: ByteBuffer.getLong()#Direct big-endian failed");
        }
        
        //printByteBufferContent("[TestByteBuffer]:(Direct Buffer)", byteBufferDirect);

        long lOff0 = byteBufferDirect.getLong(0);
        byteBufferDirect.order(ByteOrder.LITTLE_ENDIAN);
        long lOff10 = byteBufferDirect.getLong(10);

        if (lOff0 != 0x0102787766554433L) failed("[TestByteBuffer]:lOff0 failed");
        if (lOff10 != 0xddccbbaa99887766L) failed("[TestByteBuffer]:lOff1 failed");

        byteBufferDirect.order(ByteOrder.nativeOrder());
        byteBufferDirect.putShort(0, (short)0x55aa);
        if (!expectByteBufferOrderWithShort(byteBufferDirect, 0, (short)0x55aa, ByteOrder.nativeOrder())) {
            failed("[TestByteBuffer]: ByteBuffer.putShort()#Direct little-endian failed");
        }

        byteBufferDirect.order(ByteOrder.nativeOrder());
        byteBufferDirect.putInt(0, 0x55aa77cc);
        if (!expectByteBufferOrderWithInt(byteBufferDirect, 0, 0x55aa77cc, ByteOrder.nativeOrder())) {
            failed("[TestByteBuffer]: ByteBuffer.putInt()#Direct little-endian failed");
        }

        byteBufferDirect.order(ByteOrder.BIG_ENDIAN);
        byteBufferDirect.putInt(0, 0x55aa77cc);
        if (!expectByteBufferOrderWithInt(byteBufferDirect, 0, 0x55aa77cc, ByteOrder.BIG_ENDIAN)) {
            failed("[TestByteBuffer]: ByteBuffer.putInt()#Direct big-endian failed");
        }
    }

    private void printByteBufferContent(String info, ByteBuffer buf) {
        System.out.print(info);
        for (int i = 0; i < buf.limit(); i++) {
            System.out.print(Long.toString(buf.get(i), 16)+" ");
        }
        System.out.println("");
	}
	private void testFloatBuffer() {
        final float testValue1 = 1.23f;
        final float testValue2 = 3.24f;
        final float testValue3 = 3.1415f;
        final float testValue4 = 900.2984f;
        final float testValue5 = 100000.0f;

		FloatBuffer fb;
        FloatBuffer fbi;
        ByteBuffer byteBufferDirect = createDirectByteBuffer();
        ByteBuffer byteBufferInDirect = createIndirectByteBuffer();

        float[] fsrc = {testValue1, testValue2, testValue3, testValue4, testValue5}; 
        
		fb = byteBufferDirect.order(ByteOrder.nativeOrder()).asFloatBuffer();
        fbi = byteBufferInDirect.asFloatBuffer();

        fb.put(fsrc);
        fb.flip();
        fbi.put(fsrc);
        fbi.flip();
        if (!expectByteBufferOrderWithFloat(byteBufferDirect, 0, testValue1, ByteOrder.LITTLE_ENDIAN)) {
            failed("[testFloatBuffer]: FloatBuffer.put(float[])#Direct failed: byte little-endian order wrong");
        }
        if (!expectByteBufferOrderWithFloat(byteBufferInDirect, 4, testValue2, ByteOrder.BIG_ENDIAN)) {
            failed("[testFloatBuffer]: FloatBuffer.put(float[])#InDirect failed: byte big-endian order wrong");
        }
        
        byteBufferInDirect = createIndirectByteBuffer();
        fbi = byteBufferInDirect.order(ByteOrder.LITTLE_ENDIAN).asFloatBuffer();
        fbi.put(fsrc);
        fbi.flip();
        if (!expectByteBufferOrderWithFloat(byteBufferInDirect, 8, testValue3, ByteOrder.LITTLE_ENDIAN)) {
            failed("[testFloatBuffer]: FloatBuffer.put(float[])#InDirect failed: byte little-endian order wrong");
        }

        byteBufferDirect = createDirectByteBuffer();
        byteBufferInDirect = createIndirectByteBuffer();
        fb = byteBufferDirect.order(ByteOrder.nativeOrder()).asFloatBuffer();
        
        fbi = byteBufferInDirect.order(ByteOrder.BIG_ENDIAN).asFloatBuffer();
        fbi.put(fsrc, 0, 5).flip();
        
        fb.put(fbi);
        fb.flip();
        if (!expectByteBufferOrderWithFloat(byteBufferDirect, 0, testValue1, ByteOrder.LITTLE_ENDIAN)) {
            failed("[testFloatBuffer]: FloatBuffer.put(FloatBuffer#InDirect#Big-Endian)#Direct failed: byte little-endian order wrong");
        }

        byteBufferDirect = createDirectByteBuffer();
        byteBufferInDirect = createIndirectByteBuffer();
        fbi = byteBufferInDirect.order(ByteOrder.BIG_ENDIAN).asFloatBuffer();
        
        fb = byteBufferDirect.order(ByteOrder.nativeOrder()).asFloatBuffer();
        fb.put(fsrc, 0, 5).flip();
        
        fbi.put(fb);
        fbi.flip();
        if (!expectByteBufferOrderWithFloat(byteBufferInDirect, 4, testValue2, ByteOrder.BIG_ENDIAN)) {
            failed("[testFloatBuffer]: FloatBuffer.put(FloatBuffer#Direct#Little-Endian)#InDirect failed: byte big-endian order wrong");
        }

        byteBufferDirect = createDirectByteBuffer();
        fb = byteBufferDirect.order(ByteOrder.nativeOrder()).asFloatBuffer();
        fb.put(fsrc, 3, 2).flip();
        float[] fdst = new float[2];
        fb.get(fdst);
        
        if (fdst[0] != fsrc[3] || fdst[1] != fsrc[4]) {
            failed("[testFloatBuffer]: FloatBuffer.get(float[])#Direct failed: byte little-endian order wrong");
        }

        byteBufferDirect = createDirectByteBuffer();
        fbi = byteBufferInDirect.order(ByteOrder.BIG_ENDIAN).asFloatBuffer();
        fbi.put(fsrc, 3, 2).flip();
        fdst = new float[2];
        fbi.get(fdst);
        
        if (fdst[0] != fsrc[3] || fdst[1] != fsrc[4]) {
            failed("[testFloatBuffer]: FloatBuffer.get(float[])#InDirect failed: byte big-endian order wrong");
        }
		
	}
	private void testLongBuffer() {
        final long testValue1 = 0x1122334455667788L;
        final long testValue2 = 0x8899aabbccddeeffL;
		LongBuffer lb;
        LongBuffer lbi;
        ByteBuffer byteBufferDirect = createDirectByteBuffer();
        ByteBuffer byteBufferInDirect = createIndirectByteBuffer();

        long[] lsrc = {testValue1, testValue2}; 
        long[] ldst = new long[2];

        lb = byteBufferDirect.order(ByteOrder.BIG_ENDIAN).asLongBuffer();
        lbi = byteBufferInDirect.order(ByteOrder.BIG_ENDIAN).asLongBuffer();

        lb.put(testValue1).flip();
        lbi.put(testValue2).flip();
        if (lb.get() != testValue1) {
            printByteBufferContent("[failedBuffer]:"+byteBufferDirect, byteBufferDirect);
            failed("[testLongBuffer]: LongBuffer.put()/get()#Direct big-endian failed");
        }
        if (lbi.get() != testValue2) {
            failed("[testLongBuffer]: LongBuffer.put()/get()#InDirect big-endian failed");
        }

        lb = byteBufferDirect.order(ByteOrder.LITTLE_ENDIAN).asLongBuffer();
        lbi = byteBufferInDirect.order(ByteOrder.LITTLE_ENDIAN).asLongBuffer();

        lb.put(testValue1).flip();
        lbi.put(testValue2).flip();
        
        if (lb.get() != testValue1) {
            failed("[testLongBuffer]: LongBuffer.put()/get()#Direct little-endian failed");
        }
        if (lbi.get() != testValue2) {
            failed("[testLongBuffer]: LongBuffer.put()/get()#InDirect little-endian failed");
        }

        lb = byteBufferDirect.order(ByteOrder.BIG_ENDIAN).asLongBuffer();
        lbi = byteBufferInDirect.order(ByteOrder.BIG_ENDIAN).asLongBuffer();

        lb.put(lsrc).flip();
        lbi.put(lsrc).flip();
        lb.get(ldst);
        if (ldst[0] != lsrc[0] || ldst[1] != lsrc[1]) {
            printByteBufferContent("[failedBuffer]:"+byteBufferDirect, byteBufferDirect);
            failed("[testLongBuffer]: LongBuffer.put(long[])/get(long[])#Direct big-endian failed");
        }
        lbi.get(ldst);
        if (ldst[0] != lsrc[0] || ldst[1] != lsrc[1]) {
            failed("[testLongBuffer]: LongBuffer.put(long[])/get(long[])#InDirect big-endian failed");
        }
        
		lb = byteBufferDirect.order(ByteOrder.BIG_ENDIAN).asLongBuffer();
        lbi = byteBufferInDirect.order(ByteOrder.BIG_ENDIAN).asLongBuffer();

        lb.put(lsrc);
        lb.flip();
        lbi.put(lsrc);
        lbi.flip();
        if (!expectByteBufferOrderWithLong(byteBufferDirect, 0, testValue1, ByteOrder.BIG_ENDIAN)) {
            failed("[testLongBuffer]: LongBuffer.put(long[])#Direct failed: byte big-endian order wrong");
        }
        if (!expectByteBufferOrderWithLong(byteBufferInDirect, 8, testValue2, ByteOrder.BIG_ENDIAN)) {
            failed("[testLongBuffer]: LongBuffer.put(long[])#InDirect failed: byte big-endian order wrong");
        }

        

        lb = byteBufferDirect.order(ByteOrder.LITTLE_ENDIAN).asLongBuffer();
        lbi = byteBufferInDirect.order(ByteOrder.LITTLE_ENDIAN).asLongBuffer();

        lb.put(lsrc);
        lb.flip();
        lbi.put(lsrc);
        lbi.flip();
        //printByteBufferContent("[testLongBuffer]:(Direct Buffer)", byteBufferDirect);
        //printByteBufferContent("[testLongBuffer]:(InDirect Buffer)", byteBufferInDirect);

        if (!expectByteBufferOrderWithLong(byteBufferDirect, 8, testValue2, ByteOrder.LITTLE_ENDIAN)) {
            failed("[testLongBuffer]: LongBuffer.put(long[])#Direct failed: byte little-endian order wrong");
        }
        if (!expectByteBufferOrderWithLong(byteBufferInDirect, 0, testValue1, ByteOrder.LITTLE_ENDIAN)) {
            failed("[testLongBuffer]: LongBuffer.put(long[])#InDirect failed: buffer byte little-endian order wrong");
        }

        byteBufferDirect = createDirectByteBuffer();
        byteBufferInDirect = createIndirectByteBuffer();
        lb = byteBufferDirect.order(ByteOrder.nativeOrder()).asLongBuffer();
        lbi = byteBufferInDirect.order(ByteOrder.LITTLE_ENDIAN).asLongBuffer();
        lbi.put(lsrc, 1, 1).flip();

        lb.put(lbi);
        lb.flip();
        
        if (!expectByteBufferOrderWithLong(byteBufferDirect, 0, testValue2, ByteOrder.LITTLE_ENDIAN)) {
            failed("[testLongBuffer]: LongBuffer.put(LongBuffer#InDirect#Little-Endian)#Direct failed: byte little-endian order wrong");
        }

        byteBufferDirect = createDirectByteBuffer();
        byteBufferInDirect = createIndirectByteBuffer();
        lb = byteBufferDirect.order(ByteOrder.BIG_ENDIAN).asLongBuffer();
        
        lbi = byteBufferInDirect.order(ByteOrder.BIG_ENDIAN).asLongBuffer();
        lbi.put(lsrc, 1, 1).flip();
        
        lb.put(lbi);
        lb.flip();

        
        if (!expectByteBufferOrderWithLong(byteBufferDirect, 0, testValue2, ByteOrder.BIG_ENDIAN)) {
            failed("[testLongBuffer]: LongBuffer.put(LongBuffer#InDirect#Big-Endian)#Direct failed: byte big-endian order wrong");
        }

        byteBufferDirect = createDirectByteBuffer();
        byteBufferInDirect = createIndirectByteBuffer();
        lb = byteBufferDirect.order(ByteOrder.nativeOrder()).asLongBuffer();
        lbi = byteBufferInDirect.order(ByteOrder.LITTLE_ENDIAN).asLongBuffer();
        lb.put(lsrc, 1, 1).flip();

        lbi.put(lb);
        lbi.flip();
        
        if (!expectByteBufferOrderWithLong(byteBufferInDirect, 0, testValue2, ByteOrder.LITTLE_ENDIAN)) {
            failed("[testLongBuffer]: LongBuffer.put(LongBuffer#Direct#Little-Endian)#InDirect failed: byte little-endian order wrong");
        }

        byteBufferDirect = createDirectByteBuffer();
        byteBufferInDirect = createIndirectByteBuffer();
        lb = byteBufferDirect.order(ByteOrder.nativeOrder()).asLongBuffer();
        lbi = byteBufferInDirect.order(ByteOrder.BIG_ENDIAN).asLongBuffer();
        lb.put(lsrc, 1, 1).flip();

        lbi.put(lb);
        lbi.flip();
        
        if (!expectByteBufferOrderWithLong(byteBufferInDirect, 0, testValue2, ByteOrder.BIG_ENDIAN)) {
            failed("[testLongBuffer]: LongBuffer.put(LongBuffer#Direct#Little-Endian)#InDirect failed: byte big-endian order wrong");
        }

        byteBufferDirect = createDirectByteBuffer();
        lb = byteBufferDirect.order(ByteOrder.nativeOrder()).asLongBuffer();
        lb.put(lsrc, 0, 2).flip();
        lb.get(ldst);
        
        if (ldst[0] != lsrc[0] || ldst[1] != lsrc[1]) {
            failed("[testLongBuffer]: LongBuffer.get(long[])#Direct failed: byte little-endian order wrong");
        }

        byteBufferInDirect = createIndirectByteBuffer();
        lbi = byteBufferInDirect.order(ByteOrder.LITTLE_ENDIAN).asLongBuffer();
        lbi.put(lsrc, 0, 2).flip();
        ldst = new long[2];
        lbi.get(ldst);
        
        if (ldst[0] != lsrc[0] || ldst[1] != lsrc[1]) {
            failed("[testLongBuffer]: LongBuffer.get(long[])#InDirect failed: byte little-endian order wrong");
        }

        byteBufferInDirect = createIndirectByteBuffer();
        lbi = byteBufferInDirect.order(ByteOrder.BIG_ENDIAN).asLongBuffer();
        lbi.put(lsrc, 0, 2).flip();
        ldst = new long[2];
        lbi.get(ldst);
        
        if (ldst[0] != lsrc[0] || ldst[1] != lsrc[1]) {
            failed("[testLongBuffer]: LongBuffer.get(long[])#InDirect failed: byte big-endian order wrong");
        }
	}
	private void testShortBuffer() {
        final short testValue1 = (short)0x1122;
        final short testValue2 = (short)0xaa55;
		ShortBuffer sb;
        ShortBuffer sbi;
        ByteBuffer byteBufferDirect = createDirectByteBuffer();
        ByteBuffer byteBufferInDirect = createIndirectByteBuffer();

        short[] ssrc = {testValue1, testValue2}; 
        
		sb = byteBufferDirect.order(ByteOrder.BIG_ENDIAN).asShortBuffer();
        sbi = byteBufferInDirect.order(ByteOrder.BIG_ENDIAN).asShortBuffer();

        sb.put(ssrc);
        sb.flip();
        sbi.put(ssrc);
        sbi.flip();
        if (!expectByteBufferOrderWithShort(byteBufferDirect, 0, testValue1, ByteOrder.BIG_ENDIAN)) {
            failed("[testShortBuffer]: ShortBuffer.put(short[])#Direct failed: byte big-endian order wrong");
        }
        if (!expectByteBufferOrderWithShort(byteBufferInDirect, 2, testValue2, ByteOrder.BIG_ENDIAN)) {
            failed("[testShortBuffer]: ShortBuffer.put(short[])#InDirect failed: byte big-endian order wrong");
        }

        

        sb = byteBufferDirect.order(ByteOrder.LITTLE_ENDIAN).asShortBuffer();
        sbi = byteBufferInDirect.order(ByteOrder.LITTLE_ENDIAN).asShortBuffer();

        sb.put(ssrc);
        sb.flip();
        sbi.put(ssrc);
        sbi.flip();
        
        if (!expectByteBufferOrderWithShort(byteBufferDirect, 2, testValue2, ByteOrder.LITTLE_ENDIAN)) {
            failed("[testShortBuffer]: ShortBuffer.put(short[])#Direct failed: byte little-endian order wrong");
        }
        if (!expectByteBufferOrderWithShort(byteBufferInDirect, 0, testValue1, ByteOrder.LITTLE_ENDIAN)) {
            failed("[testShortBuffer]: ShortBuffer.put(short[])#InDirect failed: buffer byte little-endian order wrong");
        }

        byteBufferDirect = createDirectByteBuffer();
        byteBufferInDirect = createIndirectByteBuffer();
        sb = byteBufferDirect.order(ByteOrder.nativeOrder()).asShortBuffer();
        sbi = byteBufferInDirect.order(ByteOrder.LITTLE_ENDIAN).asShortBuffer();
        sbi.put(ssrc).flip();

        sb.put(sbi);
        sb.flip();
        
        if (!expectByteBufferOrderWithShort(byteBufferDirect, 0, testValue1, ByteOrder.LITTLE_ENDIAN)) {
            failed("[testShortBuffer]: ShortBuffer.put(ShortBuffer#InDirect#Little-Endian)#Direct failed: byte little-endian order wrong");
        }

        byteBufferDirect = createDirectByteBuffer();
        byteBufferInDirect = createIndirectByteBuffer();
        sb = byteBufferDirect.order(ByteOrder.BIG_ENDIAN).asShortBuffer();
        
        sbi = byteBufferInDirect.order(ByteOrder.LITTLE_ENDIAN).asShortBuffer();
        sbi.put(ssrc, 1, 1).flip();
        
        sb.put(sbi);
        sb.flip();

        if (!expectByteBufferOrderWithShort(byteBufferDirect, 0, testValue2, ByteOrder.BIG_ENDIAN)) {
            failed("[testShortBuffer]: ShortBuffer.put(ShortBuffer#InDirect#Big-Endian)#Direct failed: byte big-endian order wrong");
        }

        byteBufferDirect = createDirectByteBuffer();
        byteBufferInDirect = createIndirectByteBuffer();
        sb = byteBufferDirect.order(ByteOrder.nativeOrder()).asShortBuffer();
        sbi = byteBufferInDirect.order(ByteOrder.LITTLE_ENDIAN).asShortBuffer();
        sb.put(ssrc, 1, 1).flip();

        sbi.put(sb);
        sbi.flip();
        
        if (!expectByteBufferOrderWithShort(byteBufferInDirect, 0, testValue2, ByteOrder.LITTLE_ENDIAN)) {
            failed("[testShortBuffer]: ShortBuffer.put(ShortBuffer#Direct#Little-Endian)#InDirect failed: byte little-endian order wrong");
        }

        byteBufferDirect = createDirectByteBuffer();
        byteBufferInDirect = createIndirectByteBuffer();
        sb = byteBufferDirect.order(ByteOrder.nativeOrder()).asShortBuffer();
        sbi = byteBufferInDirect.order(ByteOrder.BIG_ENDIAN).asShortBuffer();
        sb.put(ssrc, 1, 1).flip();

        sbi.put(sb);
        sbi.flip();
        
        if (!expectByteBufferOrderWithShort(byteBufferInDirect, 0, testValue2, ByteOrder.BIG_ENDIAN)) {
            failed("[testShortBuffer]: ShortBuffer.put(ShortBuffer#Direct#Little-Endian)#InDirect failed: byte big-endian order wrong");
        }

        byteBufferDirect = createDirectByteBuffer();
        sb = byteBufferDirect.order(ByteOrder.nativeOrder()).asShortBuffer();
        sb.put(ssrc, 0, 2).flip();
        short[] sdst = new short[2];
        sb.get(sdst);
        
        if (sdst[0] != ssrc[0] || sdst[1] != ssrc[1]) {
            failed("[testShortBuffer]: ShortBuffer.get(short[])#Direct failed: byte little-endian order wrong");
        }

        byteBufferInDirect = createIndirectByteBuffer();
        sbi = byteBufferInDirect.order(ByteOrder.LITTLE_ENDIAN).asShortBuffer();
        sbi.put(ssrc, 0, 2).flip();
        sdst = new short[2];
        sbi.get(sdst);
        
        if (sdst[0] != ssrc[0] || sdst[1] != ssrc[1]) {
            failed("[testShortBuffer]: ShortBuffer.get(short[])#InDirect failed: byte little-endian order wrong");
        }

        byteBufferInDirect = createIndirectByteBuffer();
        sbi = byteBufferInDirect.order(ByteOrder.BIG_ENDIAN).asShortBuffer();
        sbi.put(ssrc, 0, 2).flip();
        sdst = new short[2];
        sbi.get(sdst);
        
        if (sdst[0] != ssrc[0] || sdst[1] != ssrc[1]) {
            failed("[testShortBuffer]: ShortBuffer.get(short[])#InDirect failed: byte big-endian order wrong");
        }
		
	}

    void testCreateBufferByArray() throws TestFailedException {
        ByteBuffer byteBuffer;
        ByteOrder testByteOrder;
        
    
        log("Start testCreateBufferByArray...");

        for (int order = 0; order < 2; order++) {
            if (order == 0) testByteOrder = ByteOrder.BIG_ENDIAN;
            else testByteOrder = ByteOrder.LITTLE_ENDIAN;

            for (int bufferType = 0; bufferType < 2; bufferType++) {
                if (bufferType == BUFFERTYPE_INDIRECT) byteBuffer = createIndirectByteBuffer().order(testByteOrder);
                else byteBuffer = createDirectByteBuffer().order(testByteOrder);

                byteBuffer.asShortBuffer().put(testShortValues);
                expectByteBufferOrderWithShorts("ShortBuffer.put(short[])", byteBuffer, testShortValues, testByteOrder, bufferType);        

                byteBuffer.asFloatBuffer().put(testFloatValues);
                expectByteBufferOrderWithFloats("FloatBuffer.put(float[])", byteBuffer, testFloatValues, testByteOrder, bufferType);        

                byteBuffer.asIntBuffer().put(testIntValues);
                expectByteBufferOrderWithInts("IntBuffer.put(int[])", byteBuffer, testIntValues, testByteOrder, bufferType);        

                byteBuffer.asLongBuffer().put(testLongValues);
                expectByteBufferOrderWithLongs("LongBuffer.put(long[])", byteBuffer, testLongValues, testByteOrder, bufferType);        
            }
        }
        log("Start testCreateBufferByArray OK!");
    }

    void testCreateBufferByBuffer() throws TestFailedException {
        ByteBuffer byteBufferSrc, byteBufferDst;
        ByteOrder testByteOrderSrc, testByteOrderDst;
        
    
        log("Start testCreateBufferByBuffer...");

        for (int order = 0; order < 2; order++) {
          if (order == 0) testByteOrderSrc = ByteOrder.BIG_ENDIAN;
          else testByteOrderSrc = ByteOrder.LITTLE_ENDIAN;

          for (int order1 = 0; order1 < 2; order1++) {
            if (order1 == 0) testByteOrderDst = ByteOrder.BIG_ENDIAN;
            else testByteOrderDst = ByteOrder.LITTLE_ENDIAN;  

            for (int bufferTypeSrc = 0; bufferTypeSrc < 2; bufferTypeSrc++) {
              if (bufferTypeSrc == BUFFERTYPE_INDIRECT) byteBufferSrc = createIndirectByteBuffer().order(testByteOrderSrc);
              else byteBufferSrc = createDirectByteBuffer().order(testByteOrderSrc);

              for (int bufferTypeDst = 0; bufferTypeDst < 2; bufferTypeDst++) {
                if (bufferTypeDst == BUFFERTYPE_INDIRECT) {
                    byteBufferDst = createIndirectByteBuffer().order(testByteOrderDst);
                } else {
                    byteBufferDst = createDirectByteBuffer().order(testByteOrderDst);
                }

                byteBufferSrc.asShortBuffer().put(testShortValues);
                byteBufferDst.asShortBuffer().put(byteBufferSrc.asShortBuffer());
                expectByteBufferOrderWithShorts("ShortBuffer.put(ShortBuffer#"
                                                + (bufferTypeSrc == BUFFERTYPE_DIRECT?"Direct":"Indirect")
                                                + "@"
                                                + testByteOrderSrc
                                                + ")",
                                        byteBufferDst, testShortValues, testByteOrderDst, bufferTypeDst);        

                byteBufferSrc.asFloatBuffer().put(testFloatValues);
                byteBufferDst.asFloatBuffer().put(byteBufferSrc.asFloatBuffer());
                expectByteBufferOrderWithFloats("FloatBuffer.put(FloatBuffer#"
                                                + (bufferTypeSrc == BUFFERTYPE_DIRECT?"Direct":"Indirect")
                                                + "@"
                                                + testByteOrderSrc
                                                + ")",
                                        byteBufferDst, testFloatValues, testByteOrderDst, bufferTypeDst);        

                byteBufferSrc.asIntBuffer().put(testIntValues);
                byteBufferDst.asIntBuffer().put(byteBufferSrc.asIntBuffer());
                expectByteBufferOrderWithInts("IntBuffer.put(IntBuffer#"
                                                + (bufferTypeSrc == BUFFERTYPE_DIRECT?"Direct":"Indirect")
                                                + "@"
                                                + testByteOrderSrc
                                                + ")", 
                                        byteBufferDst, testIntValues, testByteOrderDst, bufferTypeDst);        

                byteBufferSrc.asLongBuffer().put(testLongValues);
                byteBufferDst.asLongBuffer().put(byteBufferSrc.asLongBuffer());
                expectByteBufferOrderWithLongs("LongBuffer.put(LongBuffer#"
                                                + (bufferTypeSrc == BUFFERTYPE_DIRECT?"Direct":"Indirect")
                                                + "@"
                                                + testByteOrderSrc
                                                + ")", 
                                        byteBufferDst, testLongValues, testByteOrderDst, bufferTypeDst);        
              }
            }
          }
        }
        log("Start testCreateBufferByBuffer OK!");
    }

    void testCreateBufferByWrap() throws TestFailedException {
            ShortBuffer shortBuffer;
            LongBuffer longBuffer;
            FloatBuffer floatBuffer;
            IntBuffer intBuffer;
            
            ByteOrder testByteOrder;
            short[] dstShortValues = new short[10];
            int[] dstIntValues = new int[5];
            float[] dstFloatValues = new float[5];
            long[] dstLongValues = new long[2];
        
            log("Start testCreateBufferByWrap...");
            
    
            shortBuffer = ShortBuffer.wrap(testShortValues);
            shortBuffer.get(dstShortValues);
            if (!areEqual(testShortValues, dstShortValues)) {
                log(Long.toString((long)dstShortValues[0], 16));
                throw new TestFailedException("ShortBuffer.wrap(short[]) failed");
            }

            floatBuffer = FloatBuffer.wrap(testFloatValues);
            floatBuffer.get(dstFloatValues);
            if (!areEqual(testFloatValues, dstFloatValues)) {
                throw new TestFailedException("FloatBuffer.wrap(float[]) failed");
            }

            intBuffer = IntBuffer.wrap(testIntValues);
            intBuffer.get(dstIntValues);
            if (!areEqual(testIntValues, dstIntValues)) {
                throw new TestFailedException("IntBuffer.wrap(int[]) failed");
            }

            longBuffer = LongBuffer.wrap(testLongValues);
            longBuffer.get(dstLongValues);
            if (!areEqual(testLongValues, dstLongValues)) {
                throw new TestFailedException("LongBuffer.wrap(long[]) failed");
            }
    
            log("Start testCreateBufferByWrap OK!");
        }

    void testGetArrayFromBuffer() throws TestFailedException {
            ByteBuffer byteBuffer;
            ByteOrder testByteOrder;
            short[] dstShortValues = new short[10];
            int[] dstIntValues = new int[5];
            float[] dstFloatValues = new float[5];
            long[] dstLongValues = new long[2];
        
            log("Start testGetArrayFromBuffer...");
            
    
            for (int order = 0; order < 2; order++) {
                if (order == 0) testByteOrder = ByteOrder.BIG_ENDIAN;
                else testByteOrder = ByteOrder.LITTLE_ENDIAN;
    
                for (int bufferType = 0; bufferType < 2; bufferType++) {
                    if (bufferType == BUFFERTYPE_INDIRECT) byteBuffer = createIndirectByteBuffer().order(testByteOrder);
                    else byteBuffer = createDirectByteBuffer().order(testByteOrder);
    
                    byteBuffer.asShortBuffer().put(testShortValues);
                    byteBuffer.asShortBuffer().get(dstShortValues);
                    if (!areEqual(testShortValues, dstShortValues)) {
                        log(Long.toString((long)dstShortValues[0], 16));
                        throw new TestFailedException((bufferType == BUFFERTYPE_DIRECT?"Direct":"Indirect")
                                                        +" ShortBuffer.get(short[]) failed at "+testByteOrder);
                    }
    
                    byteBuffer.asFloatBuffer().put(testFloatValues);
                    byteBuffer.asFloatBuffer().get(dstFloatValues);
                    if (!areEqual(testFloatValues, dstFloatValues)) {
                        throw new TestFailedException("FloatBuffer.get(float[]) failed");
                    }

                    byteBuffer.asIntBuffer().put(testIntValues);
                    byteBuffer.asIntBuffer().get(dstIntValues);
                    if (!areEqual(testIntValues, dstIntValues)) {
                        throw new TestFailedException("IntBuffer.get(int[]) failed");
                    }

                    byteBuffer.asLongBuffer().put(testLongValues);
                    byteBuffer.asLongBuffer().get(dstLongValues);
                    if (!areEqual(testLongValues, dstLongValues)) {
                        throw new TestFailedException("LongBuffer.get(long[]) failed");
                    }
                }
            }
            log("Start testGetArrayFromBuffer OK!");
        }

    void expectByteBufferOrderWithFloats(String testName, ByteBuffer buf, float[] v, ByteOrder endian, int bufferType)
                                                             throws TestFailedException {
        for (int i = 0; i < v.length; i++) {
            if (!expectByteBufferOrderWithFloat(buf, i << 2, v[i], endian)) {
                failed(testName);
                throw new TestFailedException(testName+" failed in "+endian+" for "+(bufferType == BUFFERTYPE_DIRECT?"Direct":"Indirect"));
            }
        }

    }
    
    boolean expectByteBufferOrderWithFloat(ByteBuffer buf, int off, float f, ByteOrder endian) {
        failedBuffer = buf;
        
        int v = Float.floatToIntBits(f);
        byte v1L, v2, v3, v4;
        v1L = (byte)(v & 0xff);
        v2 = (byte)((v >>> 8)&0xff);
        v3 = (byte)((v >>> 16)&0xff);
        v4 = (byte)((v >>> 24)&0xff);

        byte[] testbytes = new byte[4];
        for (int i = off; i < off + 4; i++) {
            testbytes[i-off] = buf.get(i);
        }

        if (endian == ByteOrder.BIG_ENDIAN) {
            if (v1L != testbytes[3] ||
                v2  != testbytes[2] ||
                v3  != testbytes[1] ||
                v4  != testbytes[0]) {
                return false;
            }
        } else {
            if (v1L != testbytes[0] ||
                v2  != testbytes[1] ||
                v3  != testbytes[2] ||
                v4  != testbytes[3]) {
                return false;
            }
        }

        failedBuffer = null;
        return true;
        
    }

    void expectByteBufferOrderWithInts(String testName, ByteBuffer buf, int[] v, ByteOrder endian, int bufferType)
                                                                 throws TestFailedException {
        for (int i = 0; i < v.length; i++) {
            if (!expectByteBufferOrderWithInt(buf, i << 2, v[i], endian)) {
                failed(testName);
                throw new TestFailedException(testName+" failed in "+endian+" for "+(bufferType == BUFFERTYPE_DIRECT?"Direct":"Indirect"));
            }
        }    
    }

    boolean expectByteBufferOrderWithInt(ByteBuffer buf, int off, int v, ByteOrder endian) {
        failedBuffer = buf;
        
        byte v1L, v2, v3, v4;
        v1L = (byte)(v & 0xff);
        v2 = (byte)((v >>> 8)&0xff);
        v3 = (byte)((v >>> 16)&0xff);
        v4 = (byte)((v >>> 24)&0xff);

        byte[] testbytes = new byte[4];
        for (int i = off; i < off + 4; i++) {
            testbytes[i-off] = buf.get(i);
        }

        if (endian == ByteOrder.BIG_ENDIAN) {
            if (v1L != testbytes[3] ||
                v2  != testbytes[2] ||
                v3  != testbytes[1] ||
                v4  != testbytes[0]) {
                return false;
            }
        } else {
            if (v1L != testbytes[0] ||
                v2  != testbytes[1] ||
                v3  != testbytes[2] ||
                v4  != testbytes[3]) {
                return false;
            }
        }
        failedBuffer = null;
        return true;
        
    }

    void expectByteBufferOrderWithShorts(String testName, ByteBuffer buf, short[] v, ByteOrder endian, int bufferType)
                                                             throws TestFailedException {
        for (int i = 0; i < v.length; i++) {
            if (!expectByteBufferOrderWithShort(buf, i << 1, v[i], endian)) {
                failed(testName);
                throw new TestFailedException("Endian: "+endian+" for "+(bufferType == BUFFERTYPE_DIRECT?"Direct":"Indirect"));
            }
        }
    }

    boolean expectByteBufferOrderWithShort(ByteBuffer buf, int off, short v, ByteOrder endian) {
        failedBuffer = buf;
        
        byte v1L, v2;
        v1L = (byte)(v & 0xff);
        v2 = (byte)((v >>> 8)&0xff);

        byte[] testbytes = new byte[2];
        for (int i = off; i < off + 2; i++) {
            testbytes[i-off] = buf.get(i);
        }

        if (endian == ByteOrder.BIG_ENDIAN) {
            if (v1L != testbytes[1] ||
                v2  != testbytes[0]) {
                return false;
            }
        } else {
            if (v1L != testbytes[0] ||
                v2  != testbytes[1]) {
                return false;
            }
        }
        failedBuffer = null;
        return true;
        
    }

    void expectByteBufferOrderWithLongs(String testName, ByteBuffer buf, long[] v, ByteOrder endian, int bufferType)
                                                             throws TestFailedException {
        for (int i = 0; i < v.length; i++) {
            if (!expectByteBufferOrderWithLong(buf, i << 3, v[i], endian)) {
                failed(testName);
                throw new TestFailedException(testName+" failed in "+endian+" for "+(bufferType == BUFFERTYPE_DIRECT?"Direct":"Indirect"));
            }
        }

    }

    boolean expectByteBufferOrderWithLong(ByteBuffer buf, int off, long v, ByteOrder endian) {
        failedBuffer = buf;
        
        byte v1L, v2, v3, v4, v5, v6, v7, v8H;
        v1L = (byte)(v & 0xff);
        v2 = (byte)((v >>> 8)&0xff);
        v3 = (byte)((v >>> 16)&0xff);
        v4 = (byte)((v >>> 24)&0xff);
        v5 = (byte)((v >>> 32)&0xff);
        v6 = (byte)((v >>> 40)&0xff);
        v7 = (byte)((v >>> 48)&0xff);
        v8H = (byte)((v >>> 56)&0xff);

        byte[] testbytes = new byte[8];
        for (int i = off; i < off + 8; i++) {
            testbytes[i-off] = buf.get(i);
        }

        if (endian == ByteOrder.BIG_ENDIAN) {
            if (v1L != testbytes[7] ||
                v2  != testbytes[6] ||
                v3  != testbytes[5] ||
                v4  != testbytes[4] ||
                v5  != testbytes[3] ||
                v6  != testbytes[2] ||
                v7  != testbytes[1] ||
                v8H  != testbytes[0]) {
                return false;
            }
        } else {
            if (v1L != testbytes[0] ||
                v2  != testbytes[1] ||
                v3  != testbytes[2] ||
                v4  != testbytes[3] ||
                v5  != testbytes[4] ||
                v6  != testbytes[5] ||
                v7  != testbytes[6] ||
                v8H  != testbytes[7]) {
                return false;
            }
        }
        failedBuffer = null;
        return true;
        
    }

    private void failed(String info) {
        System.out.println("Test failed:");
        System.out.println(info);
        if (failedBuffer != null) {
            printByteBufferContent("[failedBuffer]:"+failedBuffer, failedBuffer);
        }
    }

    private void log(String s) {
        System.out.println(s);
    }

    public static boolean areEqual(
        int[] a,
        int[] b)
    {
        if (a == b)
        {
            return true;
        }

        if (a == null || b == null)
        {
            return false;
        }

        if (a.length != b.length)
        {
            return false;
        }

        for (int i = 0; i != a.length; i++)
        {
            if (a[i] != b[i])
            {
                return false;
            }
        }

        return true;
    }

    public static boolean areEqual(
        float[] a,
        float[] b)
    {
        if (a == b)
        {
            return true;
        }

        if (a == null || b == null)
        {
            return false;
        }

        if (a.length != b.length)
        {
            return false;
        }

        for (int i = 0; i != a.length; i++)
        {
            if (a[i] != b[i])
            {
                return false;
            }
        }

        return true;
    }

    public static boolean areEqual(
        short[] a,
        short[] b)
    {
        if (a == b)
        {
            return true;
        }

        if (a == null || b == null)
        {
            return false;
        }

        if (a.length != b.length)
        {
            return false;
        }

        for (int i = 0; i != a.length; i++)
        {
            if (a[i] != b[i])
            {
                return false;
            }
        }

        return true;
    }

    public static boolean areEqual(
        long[] a,
        long[] b)
    {
        if (a == b)
        {
            return true;
        }

        if (a == null || b == null)
        {
            return false;
        }

        if (a.length != b.length)
        {
            return false;
        }

        for (int i = 0; i != a.length; i++)
        {
            if (a[i] != b[i])
            {
                return false;
            }
        }

        return true;
    }

    
	public static void main(String[] args) {
        TestNIOBuffer test = new TestNIOBuffer();
        try {    		
    		test.testByteBuffer();
    		test.testShortBuffer();
    		test.testLongBuffer();
    		test.testFloatBuffer();
            test.testCreateBufferByArray();
            test.testCreateBufferByBuffer();
            test.testGetArrayFromBuffer();
            test.testCreateBufferByWrap();
        } catch (TestFailedException tfe) {
            tfe.printStackTrace();
        } catch (Exception e) {
            test.failed(e.getMessage());
            e.printStackTrace();
        }
	}

    class TestFailedException extends Exception {
        public TestFailedException(String s) {
            super(s);
        }
    }
}

