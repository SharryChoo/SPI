package com.sharry.android.spi.plugin.core

class AnnotationEntry {

    var serviceApiPath = ""
    var singleton = true
    var delay = false

    override fun toString(): String {
        return "AnnotationEntry(serviceApiPath='$serviceApiPath', singleton=$singleton, delay=$delay)"
    }

}