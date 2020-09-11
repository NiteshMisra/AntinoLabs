package `in`.antinolabs.activity

import `in`.antinolabs.R
import `in`.antinolabs.extras.Constants
import `in`.antinolabs.rest.Coroutines
import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.gun0912.tedpermission.PermissionListener
import com.gun0912.tedpermission.TedPermission
import kotlinx.coroutines.delay

class SplashActivity : AppCompatActivity() {

    private var longitude : Double = 20.0
    private var latitude : Double = 20.0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        TedPermission.with(this)
            .setPermissionListener(permissionListener)
            .setDeniedMessage("If you reject permission, you cannot able to show your task\n Please turn on permissions at [Setting] > [Permission]")
            .setPermissions(Manifest.permission.ACCESS_FINE_LOCATION)
            .check()
    }

    private val locationListener = object : LocationListener{

        override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {
        }

        override fun onProviderEnabled(provider: String) {
        }

        override fun onLocationChanged(location : Location) {
            longitude = location.longitude
            latitude = location.latitude
            moveToNextActivity()
        }

    }

    @SuppressLint("MissingPermission")
    private val permissionListener: PermissionListener = object : PermissionListener {

        override fun onPermissionGranted() {
            val lm : LocationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
            val location = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER)
            if (location != null){
                longitude = location.longitude
                latitude = location.latitude
                moveToNextActivity()
            }else{
                lm.requestLocationUpdates(LocationManager.GPS_PROVIDER,1000L,500.0f,locationListener)
            }
        }

        override fun onPermissionDenied(deniedPermissions: List<String>) {}
    }

    private fun moveToNextActivity() {
        Coroutines.main {
            delay(2000)
            val intent = Intent(this,MainActivity::class.java)
            intent.putExtra(Constants.longitude,longitude)
            intent.putExtra(Constants.latitude,latitude)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
        }
    }
}