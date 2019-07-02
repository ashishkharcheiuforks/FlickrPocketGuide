package com.piotrek1543.example.flickrpocketguide.utils

import android.app.Activity
import android.app.AlertDialog
import android.content.ContentValues.TAG
import android.content.Context
import android.content.Intent
import android.content.IntentSender
import android.location.LocationManager
import android.util.Log
import android.widget.Toast
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.*
import com.piotrek1543.example.flickrpocketguide.Constants
import com.piotrek1543.example.flickrpocketguide.R
import timber.log.Timber


class GpsUtils(private val context: Context) {
    private val mLocationSettingsRequest: LocationSettingsRequest
    private val mSettingsClient: SettingsClient = LocationServices.getSettingsClient(context)
    private val locationManager: LocationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
    private val locationRequest: LocationRequest = LocationRequest.create()

    init {

        locationRequest.apply {
            priority = LocationRequest.PRIORITY_HIGH_ACCURACY
            interval = (10 * 1000).toLong()
            fastestInterval = (2 * 1000).toLong()
        }
        val builder = LocationSettingsRequest.Builder()
            .addLocationRequest(locationRequest)
        mLocationSettingsRequest = builder.build()
        builder.setAlwaysShow(true)
    }

    // method for turn on GPS
    fun turnGPSOn(cxt: Context = context) {

        if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            buildAlertMessageNoGps(cxt)
        } else {
            mSettingsClient
                .checkLocationSettings(mLocationSettingsRequest)
                .addOnSuccessListener(context as Activity) {
                    //  GPS is already enable, callback GPS status through listener
                    Timber.d("GPS is already enabled")
                }
                .addOnFailureListener(context) { e ->
                    Timber.d("GPS is already disabled")

                    when ((e as ApiException).statusCode) {
                        LocationSettingsStatusCodes.RESOLUTION_REQUIRED ->

                            try {
                                // Show the dialog by calling startResolutionForResult(), and check the
                                // result in onActivityResult().
                                val rae = e as ResolvableApiException
                                rae.startResolutionForResult(context, Constants.GPS_REQUEST)
                            } catch (sie: IntentSender.SendIntentException) {
                                Log.i(TAG, "PendingIntent unable to execute request.")
                            }

                        LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE -> {
                            val errorMessage =
                                "Location settings are inadequate, and cannot be fixed here. Fix in Settings."
                            Log.e(TAG, errorMessage)

                            Toast.makeText(context, errorMessage, Toast.LENGTH_LONG).show()
                        }
                    }
                }
        }
    }

    private fun buildAlertMessageNoGps(context: Context) {
        val builder = AlertDialog.Builder(context)
        builder.setMessage(context.getString(R.string.alert_gps_disabled))
            .setCancelable(false)
            .setPositiveButton(android.R.string.yes) { dialog, _ ->
                dialog.dismiss()
                context.startActivity(Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS))
            }
            .setNegativeButton(android.R.string.no) { dialog, _ -> dialog.cancel() }
        val alert = builder.create()
        alert.show()
    }
}