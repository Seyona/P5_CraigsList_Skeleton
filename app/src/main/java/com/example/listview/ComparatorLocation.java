package com.example.listview;

import java.util.Comparator;

/**
 * Created by David & Jack on 4/7/16.
 */
public class ComparatorLocation implements Comparator<BikeData> {
    @Override
    public int compare(BikeData lhs, BikeData rhs) {
        return (lhs.getLocation().compareTo(rhs.getLocation()));
    }
}

