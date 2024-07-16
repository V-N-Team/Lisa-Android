package ht.lisa.app;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;

import ht.lisa.app.model.Draw;

public class HashMapSortTest {
    @Test
    public void testHashmapSort() {

        HashMap<String, String> tickets = new HashMap<>();
        tickets.put(Draw.MARIAGE, "test");
        tickets.put(Draw.BOLET, "test");
        tickets.put(Draw.LOTTO3, "test");
        tickets.put(Draw.LOTTO5JR, "test");
        tickets.put(Draw.LOTTO4, "test");
        tickets.put(Draw.LOTTO5, "test");
        tickets.put(Draw.BOLOTO, "test");


        ArrayList<String> keys = new ArrayList<>(tickets.keySet());
        Collections.sort(keys, (o1, o2) ->
                Draw.getOrderByName(o2) - Draw.getOrderByName(o1));

        LinkedHashMap<String, String> newHash = new LinkedHashMap<>();
        for (String key : keys) {
            newHash.put(key, tickets.get(key));
        }
        System.out.println(newHash);

    }
}
