package com.example.listview;

import java.util.Comparator;

/**
 * Created by Home on 4/7/16.
 */
public class ComparatorComp implements Comparator<BikeData> {
    @Override
    public int compare(BikeData lhs, BikeData rhs) {
        return (lhs.getCompany().compareTo(rhs.getCompany()));
    }
}
