package de.canitzp.rarmor.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author canitzp
 */
public class JavaUtil {

    public static <E> List<E> newList(E... elements){
        if(elements != null){
            List<E> list = new ArrayList<>();
            Collections.addAll(list, elements);
            return list;
        }
        return new ArrayList<>();
    }

}
