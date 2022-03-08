package net.njsharpe.infernalmobs.util;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Predicate;

public class ArrayHelper {

    @SuppressWarnings("unchecked")
    public static <E> E[] concat(E[]... arrays) {
        int size = 0;
        for(E[] array : arrays) {
             size += array.length;
        }
        E[] out = (E[]) Array.newInstance(arrays.getClass().getComponentType().getComponentType(), size);
        int copied = 0;
        for(E[] array : arrays) {
            System.arraycopy(array, 0, out, copied, array.length);
            copied += array.length;
        }
        return out;
    }

    public static <E> List<E> fromIterator(Iterator<E> iterator) {
        List<E> list = new ArrayList<>();
        while(iterator.hasNext()) {
            list.add(iterator.next());
        }
        return list;
    }

    public static <E> boolean isIn(E e, E[] array) {
        for(E item : array) {
            if(item.equals(e)) return true;
        }
        return false;
    }

    public static <E> boolean anyMatch(E[] array, Predicate<E> predicate) {
        for(E item : array) {
            if(predicate.test(item)) return true;
        }
        return false;
    }

    public static <E> boolean allMatch(E[] array, Predicate<E> predicate) {
        for(E item : array) {
            if(!predicate.test(item)) return false;
        }
        return true;
    }

}
