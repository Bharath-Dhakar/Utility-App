package com.example.utilityapp.utils

import android.app.Activity
import android.app.admin.DevicePolicyManager
import android.content.ComponentName
import android.content.Context
import android.util.Log

fun lockPhone(context: Context) {
    Log.d("LockPhone", "Locking phone...")
    val devicePolicyManager = context.getSystemService(Context.DEVICE_POLICY_SERVICE) as DevicePolicyManager
    val componentName = ComponentName(context, MyDeviceAdminReceiver::class.java)
    if (devicePolicyManager.isAdminActive(componentName)) {
        devicePolicyManager.lockNow()
    } else {
        Log.d("LockPhone", "Device admin not active")
    }
}