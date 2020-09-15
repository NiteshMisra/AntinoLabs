@file:Suppress("DEPRECATION")

package `in`.antinolabs.activity

import `in`.antinolabs.R
import `in`.antinolabs.extras.Constants
import `in`.antinolabs.extras.GoogleApiHelper
import `in`.antinolabs.extras.TaskApp
import `in`.antinolabs.rest.Coroutines
import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.content.IntentSender
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.common.api.PendingResult
import com.google.android.gms.location.*
import com.gun0912.tedpermission.PermissionListener
import com.gun0912.tedpermission.TedPermission
import kotlinx.coroutines.delay

class SplashActivity : AppCompatActivity() {

    private var longitude: Double = 20.0
    private var latitude: Double = 20.0
    private lateinit var locationRequest: LocationRequest
    private lateinit var pendingResult: PendingResult<LocationSettingsResult>
    private lateinit var googleApiHelper: GoogleApiHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        googleApiHelper = TaskApp.getGoogleApiHelper()
        TedPermission.with(this)
            .setPermissionListener(permissionListener)
            .setDeniedMessage("If you reject permission, you cannot able to show your task\n Please turn on permissions at [Setting] > [Permission]")
            .setPermissions(Manifest.permission.ACCESS_FINE_LOCATION)
            .check()
    }

    private val permissionListener: PermissionListener = object : PermissionListener {

        override fun onPermissionGranted() {
            getUserLocation()
        }

        override fun onPermissionDenied(deniedPermissions: List<String>) {
            finish()
        }
    }

    @SuppressLint("MissingPermission")
    private fun getUserLocation() {
        if (googleApiHelper.isConnected) {
            val client = googleApiHelper.googleApiClient
            client?.let {
                val location = LocationServices.FusedLocationApi.getLastLocation(client);
                location?.let {
                    longitude = location.longitude
                    latitude = location.latitude
                    moveToNextActivity()
                }
            }
        } else {
            locationRequest = LocationRequest.create()
            locationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
            locationRequest.interval = 30 * 1000
            locationRequest.fastestInterval = 5 * 1000

            val builder = LocationSettingsRequest.Builder()
                .addLocationRequest(locationRequest)
                .setAlwaysShow(true)

            pendingResult = LocationServices.SettingsApi.checkLocationSettings(googleApiHelper.googleApiClient,builder.build())

            pendingResult.setResultCallback {
                try {
                    when (it.status.statusCode) {
                        LocationSettingsStatusCodes.SUCCESS -> {
                            getUserLocation()
                        }
                        LocationSettingsStatusCodes.RESOLUTION_REQUIRED -> {
                            it.status.startResolutionForResult(this,0)
                        }
                        else -> {
                            Toast.makeText(this,"Enable GPS Location",Toast.LENGTH_SHORT).show()
                        }
                    }
                }catch (e : IntentSender.SendIntentException){
                    e.printStackTrace()
                }
            }

        }
    }

    private fun moveToNextActivity() {
        Coroutines.main {
            delay(2000)
            val intent = Intent(this, MainActivity::class.java)
            intent.putExtra(Constants.longitude, longitude)
            intent.putExtra(Constants.latitude, latitude)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 0 && resultCode == Activity.RESULT_OK){
            getUserLocation()
        }else{
            finish()
        }
    }
}