package com.eigenlux.roamer.core

import android.content.Context
import android.util.Log

object DeviceConfigController {
    private const val TAG = "RoamerDevice"
    private const val PREFS = "device_profile"
    private const val VALUES = "values"
    private const val ENABLED = "enabled"

    data class Result(val ok: Boolean, val output: String)

    fun load(ctx: Context): List<DeviceProperty> {
        val p = ctx.getSharedPreferences(PREFS, Context.MODE_PRIVATE)
        return DeviceProperties.defaults.mapIndexed { index, d ->
            val value = p.getString("$VALUES.$index", d.defaultValue) ?: d.defaultValue
            val enabled = p.getBoolean("$ENABLED.$index", true)
            d.copy(value = value, enabled = enabled)
        }
    }

    fun save(ctx: Context, properties: List<DeviceProperty>): Result {
        if (properties.size != DeviceProperties.defaults.size) {
            return Result(false, "Invalid Device property set")
        }
        val clean = properties.map { it.copy(value = it.value.trim()) }
        val editor = ctx.getSharedPreferences(PREFS, Context.MODE_PRIVATE).edit()
        clean.forEachIndexed { index, d ->
            editor.putString("$VALUES.$index", d.value)
            editor.putBoolean("$ENABLED.$index", d.enabled)
        }
        editor.apply()
        return runCatching {
            InstrumentationTrigger.trigger(
                ctx,
                buildMap {
                    put("action", "device")
                    clean.forEachIndexed { i, d ->
                        put("device.$i.key", d.key)
                        put("device.$i.value", d.value)
                        put("device.$i.enabled", d.enabled.toString())
                    }
                },
            )
            Result(true, "Device profile saved and hook dispatched")
        }.getOrElse {
            Log.e(TAG, "Device hook failed", it)
            Result(false, "Device hook failed: ${it.message ?: it.javaClass.simpleName}")
        }
    }

    fun cancel(ctx: Context): List<DeviceProperty> = load(ctx)
}
