package shanghai.shu.common.serialize.hessian;

import com.google.common.io.Closer;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class HessianCodecUtil {
    static HessianSerializePool pool=HessianSerializePool.getHessianPoolInstance();
    private static Closer closer=Closer.create();
    public HessianCodecUtil(){
    }
    public static byte[] encode(final Object message)throws IOException{
        try {
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            HessianSerialize hessianSerialization = pool.borrow();
            hessianSerialization.serialize(byteArrayOutputStream,message);
            byte[] body = byteArrayOutputStream.toByteArray();
            pool.restore(hessianSerialization);
            return body;
        } finally {

        }
    }

    public static Object decode(byte[] body) throws IOException{
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(body);
        HessianSerialize hessianSerialization = pool.borrow();
        Object object = hessianSerialization.deserialize(byteArrayInputStream);
        pool.restore(hessianSerialization);
        return object;
    }
}
