package com.ebe.employeemonitorapp.utils

import android.annotation.SuppressLint
import android.location.Geocoder
import android.util.Log
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import java.security.KeyStore
import java.security.SecureRandom
import java.security.cert.X509Certificate
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit
import javax.net.ssl.*
import javax.security.cert.CertificateException

@SuppressLint("TrustAllX509TrustManager")
fun getUnsafeOkHttpClient(): OkHttpClient? {
    return try {
        // Create a trust manager that does not validate certificate chains
        val trustAllCerts = arrayOf<TrustManager>(
            @SuppressLint("CustomX509TrustManager")
            object : X509TrustManager {
                @SuppressLint("TrustAllX509TrustManager")
                @Throws(CertificateException::class)
                override fun checkClientTrusted(
                    chain: Array<X509Certificate?>?,
                    authType: String?
                ) {
                }

                @Throws(CertificateException::class)
                override fun checkServerTrusted(
                    chain: Array<X509Certificate?>?,
                    authType: String?
                ) {
                }

                override fun getAcceptedIssuers(): Array<X509Certificate?>? {
                    return arrayOf()
                }
            }
        )

        // Install the all-trusting trust manager
        val sslContext = SSLContext.getInstance("SSL")
        sslContext.init(null, trustAllCerts, SecureRandom())
        // Create an ssl socket factory with our all-trusting manager
        val sslSocketFactory = sslContext.socketFactory
        val builder: OkHttpClient.Builder = OkHttpClient.Builder()
        val trustManagerFactory =
            TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm())
        trustManagerFactory.init(null as KeyStore?)
        val trustManagers = trustManagerFactory.trustManagers
        check(!(trustManagers.size != 1 || trustManagers[0] !is X509TrustManager)) {
            "Unexpected default trust managers:" + Arrays.toString(
                trustManagers
            )
        }
        val trustManager = trustManagers[0] as X509TrustManager
        builder.sslSocketFactory(sslSocketFactory, trustManager)
        builder.hostnameVerifier(HostnameVerifier { hostname, session -> true })
        val loggingInterceptor: HttpLoggingInterceptor = HttpLoggingInterceptor()
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY)
        builder.callTimeout(30000, TimeUnit.MILLISECONDS)
            .connectTimeout(30000, TimeUnit.MILLISECONDS)
            .addInterceptor(loggingInterceptor)
        builder.build()
    } catch (e: java.lang.Exception) {
        throw RuntimeException(e)
    }
}

fun getTime(time: String): String {
    val sdf = SimpleDateFormat("hh:mm:ss")
    val sdfs = SimpleDateFormat("hh:mm a")
    val dt = sdf.parse(time);

    return sdfs.format(dt!!)
}


fun getAddress(lat: Double, long: Double, geocoder: Geocoder): String {

    try {
        val addresses = geocoder?.getFromLocation(lat, long, 10)?.filter {
            it.thoroughfare != null
        }

        return addresses?.get(0)?.getAddressLine(0)!!


    } catch (e: Exception) {

        Log.d("adressList", "onLocationSelected: $e.localizedMessage")
        e.localizedMessage!!
        return ""
    }


}