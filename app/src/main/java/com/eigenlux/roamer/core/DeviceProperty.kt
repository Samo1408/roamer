package com.eigenlux.roamer.core

data class DeviceProperty(
    val key: String,
    val defaultValue: String,
    val enabled: Boolean = true,
    val value: String = defaultValue,
)

object DeviceProperties {
    val defaults: List<DeviceProperty> = listOf(
        DeviceProperty("ro.product.manufacturer", "Google"),
        DeviceProperty("ro.product.brand", "google"),
        DeviceProperty("ro.product.model", "Pixel 10"),
        DeviceProperty("ro.product.device", "blazer"),
        DeviceProperty("ro.product.name", "blazer"),
        DeviceProperty("ro.product.board", "blazer"),
        DeviceProperty("ro.hardware", "blazer"),
        DeviceProperty("ro.bootloader", "blazer-1.0-13450100"),
        DeviceProperty("baseband", "g5500a-260218-B-13400050"),
        DeviceProperty("ro.build.id", "AP4A.250405.001"),
        DeviceProperty("ro.build.display.id", "AP4A.250405.001"),
        DeviceProperty("ro.build.fingerprint", "google/blazer/blazer:16/AP4A.250405.001/13500000:user/release-keys"),
        DeviceProperty("ro.system.build.fingerprint", "google/blazer/blazer:16/AP4A.250405.001/13500000:user/release-keys"),
        DeviceProperty("ro.vendor.build.fingerprint", "google/blazer/blazer:16/AP4A.250405.001/13500000:user/release-keys"),
        DeviceProperty("ro.bootimage.build.fingerprint", "google/blazer/blazer:16/AP4A.250405.001/13500000:user/release-keys"),
        DeviceProperty("ro.build.type", "user"),
        DeviceProperty("ro.build.tags", "release-keys"),
        DeviceProperty("ro.build.user", "jenkins"),
        DeviceProperty("ro.build.host", "builder-255.google.com"),
        DeviceProperty("ro.build.version.release", "16"),
        DeviceProperty("ro.build.version.incremental", "13500000"),
        DeviceProperty("ro.build.version.security_patch", "2026-05-05"),
        DeviceProperty("ro.soc.manufacturer", "Google"),
        DeviceProperty("ro.soc.model", "Google Tensor G5"),
        DeviceProperty("ro.csc.sales_code", "ATT"),
        DeviceProperty("serialnumber", "SN-KBRDZSM0AEF1WW"),
        DeviceProperty("ro.product.locale", "en-CA"),
    )
}
