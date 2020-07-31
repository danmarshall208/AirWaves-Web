package com.airwaves.airwavesweb.util

import com.airwaves.airwavesweb.datastore.User
import org.apache.commons.math3.ml.clustering.Clusterable

class LocationWrapper(val user: User) : Clusterable {

    private val points: DoubleArray = doubleArrayOf(user.latitude, user.longitude)

    override fun getPoint(): DoubleArray = points

}