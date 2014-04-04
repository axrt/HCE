package base;

import java.util.HashMap;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: alext
 * Date: 10/6/13
 * Time: 3:29 PM
 * To change this template use File | Settings | File Templates.
 */
public class RDPOutputSet extends HashMap<String,String>{

    public String getName() {
        return name;
    }

    protected final String name;

    public RDPOutputSet(int initialCapacity, float loadFactor, String name) {
        super(initialCapacity, loadFactor);
        this.name = name;
    }

    public RDPOutputSet(int initialCapacity, String name) {
        super(initialCapacity);
        this.name = name;
    }

    public RDPOutputSet(String name) {
        this.name = name;
    }

    public RDPOutputSet(Map<? extends String, ? extends String> m, String name) {
        super(m);
        this.name = name;
    }


}
