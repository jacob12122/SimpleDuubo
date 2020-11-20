package shanghai.shu.common;

import java.util.concurrent.ThreadLocalRandom;

public class LogIds {
    public static long generate(){
        long time = System.currentTimeMillis();
        long rand = ThreadLocalRandom.current().nextLong();
        return ((time& 0x7ffffffffL)<<28)|(rand&0xfffffff);
    }
    private LogIds(){
    }

}
