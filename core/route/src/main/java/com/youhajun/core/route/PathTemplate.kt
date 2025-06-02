package com.youhajun.core.route

import android.net.Uri

class PathTemplate(template: String) {
    private val segments = template.split("/")

    fun match(uri: Uri): Map<String, String>? {
        val pathSegments = uri.pathSegments
        if (pathSegments.size != segments.size) return null

        val result = mutableMapOf<String, String>()
        for ((seg, pathSeg) in segments.zip(pathSegments)) {
            if (seg.startsWith("{") && seg.endsWith("}")) {
                val key = seg.removePrefix("{").removeSuffix("}")
                result[key] = pathSeg
            } else if (seg != pathSeg) {
                return null
            }
        }
        return result
    }
}
