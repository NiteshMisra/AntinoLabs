package `in`.antinolabs.extras

import android.content.Context
import android.os.Bundle
import android.util.Log
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener
import com.google.android.gms.location.LocationServices


class GoogleApiHelper(context: Context) : ConnectionCallbacks, OnConnectionFailedListener {

    private val context: Context = context
    var googleApiClient: GoogleApiClient? = null
        private set
    private var connectionListener: ConnectionListener? = null
    private var connectionBundle: Bundle? = null

    fun setConnectionListener(connectionListener: ConnectionListener) {
        this.connectionListener = connectionListener
        if (this.connectionListener != null && isConnected) {
            connectionListener.onConnected(connectionBundle)
        }
    }

    fun connect() {
        if (googleApiClient != null) {
            googleApiClient!!.connect()
        }
    }

    fun disconnect() {
        if (googleApiClient != null && googleApiClient!!.isConnected) {
            googleApiClient!!.disconnect()
        }
    }

    val isConnected: Boolean
        get() = googleApiClient != null && googleApiClient!!.isConnected

    private fun buildGoogleApiClient() {
        googleApiClient = GoogleApiClient.Builder(context)
            .addConnectionCallbacks(this)
            .addOnConnectionFailedListener(this)
            .addApi(LocationServices.API).build()
    }

    override fun onConnected(bundle: Bundle?) {
        connectionBundle = bundle
        if (connectionListener != null) {
            connectionListener!!.onConnected(bundle)
        }
    }

    override fun onConnectionSuspended(i: Int) {
        Log.d(TAG, "onConnectionSuspended: googleApiClient.connect()")
        googleApiClient!!.connect()
        if (connectionListener != null) {
            connectionListener!!.onConnectionSuspended(i)
        }
    }

    override fun onConnectionFailed(connectionResult: ConnectionResult) {
        Log.d(TAG, "onConnectionFailed: connectionResult = $connectionResult")
        if (connectionListener != null) {
            connectionListener!!.onConnectionFailed(connectionResult)
        }
    }

    interface ConnectionListener {
        fun onConnectionFailed(connectionResult: ConnectionResult)
        fun onConnectionSuspended(i: Int)
        fun onConnected(bundle: Bundle?)
    }

    companion object {
        private val TAG = GoogleApiHelper::class.java.simpleName
    }

    init {
        buildGoogleApiClient()
        connect()
    }
}