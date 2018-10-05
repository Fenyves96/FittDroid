package com.example.fenyv.fittdroiddrawer.Contents;

import com.example.fenyv.fittdroiddrawer.Entities.Workout;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class WorkoutContent {
    /**
     * An array of sample (dummy) items.
     */
    public static final List<Workout> ITEMS = new ArrayList<Workout>();

    /**
     * A map of sample (dummy) items, by ID.
     */
    public static final Map<String, Workout> ITEM_MAP = new HashMap<String, Workout>();

    private static final int COUNT = 25;

    private static void addItem(Workout item) {
        ITEMS.add(item);
        ITEM_MAP.put(item.toString(), item);
    }

}



