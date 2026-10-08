package com.eigenlux.roamer.core

import android.content.Context

/** Persistent Device profile. Kept completely separate from SIM settings. */
object DeviceConfig {
    private const val PREFS = "device_profile"
    private const val SEP = "\u0001"

    val defaults = listOf(
        "ro.product.manufacturer" to "Google", "ro.product.brand" to "google", "ro.product.model" to "Pixel 10",
        "ro.product.device" to "blazer", "ro.product.name" to "blazer", "ro.product.board" to "blazer",
        "ro.hardware" to "blazer", "ro.bootloader" to "blazer-1.0-13450100", "baseband" to "g5500a-260218-B-13400050",
        "ro.build.id" to "AP4A.250405.001", "ro.build.display.id" to "AP4A.250405.001",
        "ro.build.fingerprint" to "google/blazer/blazer:16/AP4A.250405.001/13500000:user/release-keys",
        "ro.system.build.fingerprint" to "google/blazer/blazer:16/AP4A.250405.001/13500000:user/release-keys",
        "ro.vendor.build.fingerprint" to "google/blazer/blazer:16/AP4A.250405.001/13500000:user/release-keys",
        "ro.bootimage.build.fingerprint" to "google/blazer/blazer:16/AP4A.250405.001/13500000:user/release-keys",
        "ro.build.type" to "user", "ro.build.tags" to "release-keys", "ro.build.user" to "jenkins",
        "ro.build.host" to "builder-255.google.com", "ro.build.version.release" to "16",
        "ro.build.version.incremental" to "13500000", "ro.build.version.security_patch" to "2026-05-05",
        "ro.soc.manufacturer" to "Google", "ro.soc.model" to "Google Tensor G5", "ro.csc.sales_code" to "ATT",
        "serialnumber" to "SN-KBRDZSM0AEF1WW", "ro.product.locale" to "en-CA",
    ).map { DeviceProperty(it.first, it.second) }

    private fun prefs(ctx: Context) = ctx.getSharedPreferences(PREFS, Context.MODE_PRIVATE)
    fun load(ctx: Context): List<DeviceProperty> = defaults.map { d ->
        val raw = prefs(ctx).getString(d.key, null)
        if (raw == null) d else raw.split(SEP, limit = 2).let { DeviceProperty(d.key, d.defaultValue, it.firstOrNull() == "1", it.getOrElse(1) { d.defaultValue }) }
    }
    fun save(ctx: Context, values: List<DeviceProperty>) {
        prefs(ctx).edit().apply { values.forEach { putString(it.key, (if (it.enabled) "1" else "0") + SEP + it.value) } }.apply()
    }
}
