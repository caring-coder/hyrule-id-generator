package pro.verron;

import java.util.Collections;
import java.util.Iterator;

public class HyruleId {

    public static Iterator<HyruleId> producer() {
        return Collections.singleton(new HyruleId()).iterator();
    }

    public String representation() {
        return "000000000";
    }
}
