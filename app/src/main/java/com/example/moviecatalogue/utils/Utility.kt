package com.example.moviecatalogue.utils

import java.util.*

object Utility {

    private fun suffixMap(): TreeMap<Long, String> {
        val map = TreeMap<Long, String>()
        map[1_000L] = "k"
        map[1_000_000L] = "M"
        map[1_000_000_000L] = "G"
        map[1_000_000_000_000L] = "T"
        map[1_000_000_000_000_000L] = "P"
        map[1_000_000_000_000_000_000L] = "E"
        return map
    }


    //sumber fungsi: https://stackoverflow.com/questions/4753251/how-to-go-about-formatting-1200-to-1-2k-in-java
    fun longToSuffixes(value: Long): String {
        if (value == java.lang.Long.MIN_VALUE) return longToSuffixes(java.lang.Long.MIN_VALUE + 1)
        if (value < 0) return "-" + longToSuffixes(-value)
        if (value < 1000) return value.toString()

        val entry = suffixMap().floorEntry(value)
        val divideBy = entry?.key
        val suffix = entry?.value

        val truncated = value / (divideBy!! / 10)
        val hasDecimal = truncated < 100 && truncated / 10.0 != (truncated / 10).toDouble()
        return if (hasDecimal) "${(truncated / 10.0)}$suffix" else "${(truncated / 10)}$suffix"
    }

}
