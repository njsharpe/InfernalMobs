package net.njsharpe.infernalmobs.util;

import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.Iterator;
import java.util.Random;

public class RandomHelper {

    public static int nextIntInclusive(Random random, int min, int max) {
        return random.nextInt((max - min) + 1) + min;
    }

    @Nullable
    public static <E> E getRandomFor(Random random, Collection<E> collection) {
        Iterator<E> iterator = collection.iterator();
        if(collection.size() == 0) return null;
        int choice = random.nextInt(collection.size());
        int i = 0;
        while(iterator.hasNext()) {
            if(i == choice) return iterator.next();
            i++;
        }
        throw new IllegalStateException(String.format("Rolled %s into max of %s.", choice, collection.size()));
    }

    @Nullable
    public static <E> E getRandomFor(Random random, E[] array) {
        if(array.length == 0) return null;
        int choice = random.nextInt(array.length);
        for(int i = 0; i < array.length; i++) {
            if(i == choice) return array[i];
        }
        throw new IllegalStateException(String.format("Rolled %s into max of %s.", choice, array.length));
    }

}
