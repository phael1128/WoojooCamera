package com.woojoo.woojoocamera.utils

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.provider.Settings

private const val Camera_Permission_Request_Code = 100

fun Activity.checkCameraPermission() {
    val isGrantedCameraPermission = this.checkSelfPermission(Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED

    if (!isGrantedCameraPermission) {
        if (shouldShowRequestPermissionRationale(Manifest.permission.CAMERA)) {
            startActivity(getSettingIntent())
        } else {
            requestCameraPermission()
        }
    }
}

fun Activity.requestCameraPermission() {
    this.requestPermissions(arrayOf(Manifest.permission.CAMERA), Camera_Permission_Request_Code)
}

fun Activity.getSettingIntent() = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
    data = Uri.parse("package:${packageName}")
}
