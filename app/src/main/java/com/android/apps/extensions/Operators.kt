package com.android.apps.extensions

import org.json.JSONArray
import org.json.JSONObject

/**
 * JSONArray
 */

operator fun JSONArray.iterator(): Iterator<JSONObject> = (0 until length()).asSequence().map { get(it) as JSONObject }.iterator()