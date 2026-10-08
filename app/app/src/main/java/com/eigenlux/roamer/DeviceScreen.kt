package com.eigenlux.roamer

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.eigenlux.roamer.core.DeviceConfig
import com.eigenlux.roamer.core.DeviceProperty
import com.eigenlux.roamer.core.InstrumentationTrigger
import com.eigenlux.roamer.data.AppLocaleStore
import com.eigenlux.roamer.ui.theme.Spacing
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@Composable
fun DeviceScreen(
    busy: Boolean,
    shizukuGranted: Boolean,
    onBusyChange: (Boolean) -> Unit,
    onLog: (String) -> Unit,
) {
    val ctx = androidx.compose.ui.platform.LocalContext.current
    val scope = rememberCoroutineScope()
    var values by remember { mutableStateOf(DeviceConfig.load(ctx)) }
    val baseline = remember { values }
    val target = remember { AppLocaleStore.enrolled(ctx).firstOrNull().orEmpty() }

    Column(
        modifier = Modifier.fillMaxWidth().verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(Spacing.md),
    ) {
        Text("Device", style = MaterialTheme.typography.headlineSmall)
        Text(
            if (target.isBlank()) "Instrumentation target: no app selected (the current Instrumentation declaration targets Roamer)"
            else "Selected app: $target",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
        values.forEachIndexed { index, item ->
            Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                Checkbox(
                    checked = item.enabled,
                    onCheckedChange = { checked -> values = values.toMutableList().also { it[index] = item.copy(enabled = checked) } },
                    enabled = !busy,
                )
                Spacer(Modifier.width(4.dp))
                OutlinedTextField(
                    value = item.value,
                    onValueChange = { value -> values = values.toMutableList().also { it[index] = item.copy(value = value) } },
                    label = { Text(item.key) },
                    modifier = Modifier.weight(1f),
                    singleLine = true,
                    enabled = !busy && item.enabled,
                )
            }
        }
        Row(horizontalArrangement = Arrangement.spacedBy(Spacing.md)) {
            Button(
                enabled = !busy && shizukuGranted,
                onClick = {
                    DeviceConfig.save(ctx, values)
                    onBusyChange(true)
                    scope.launch {
                        val result = withContext(Dispatchers.IO) {
                            runCatching {
                                val extras = buildMap {
                                    put("action", "device")
                                    put("targetPackage", target)
                                    values.filter { it.enabled }.forEach { put("device.${it.key}", it.value) }
                                }
                                InstrumentationTrigger.trigger(ctx, extras)
                            }
                        }
                        result.onSuccess {
                            onLog("Device instrumentation requested. Check logcat for RoamerPriv and verify the target app's Build values.")
                        }.onFailure { onLog("Device instrumentation failed: ${it.message}") }
                        onBusyChange(false)
                    }
                },
            ) { Text("Save Device") }
            OutlinedButton(
                enabled = !busy,
                onClick = { values = baseline.toList() },
            ) { Text("Cancel") }
        }
    }
}
