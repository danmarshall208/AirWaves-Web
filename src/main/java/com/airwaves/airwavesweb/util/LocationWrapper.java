package com.airwaves.airwavesweb.util;

import com.airwaves.airwavesweb.datastore.User;
import org.apache.commons.math3.ml.clustering.Clusterable;

public class LocationWrapper implements Clusterable {
    private double[] points;
    private User user;

    public LocationWrapper(User user) {
        this.user = user;
        this.points = new double[]{user.getLatitude(), user.getLongitude()};
    }

    @Override
    public double[] getPoint() {
        return this.points;
    }

    public User getUser() {
        return this.user;
    }
}