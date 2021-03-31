package com.manhnguyen.codebase.di.module

import android.util.Log
import com.manhnguyen.codebase.BuildConfig
import com.manhnguyen.codebase.data.api.ApiInterface
import com.manhnguyen.codebase.util.JsonUtil.Companion.instance
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.io.IOException
import java.net.SocketTimeoutException
import java.security.KeyManagementException
import java.security.NoSuchAlgorithmException
import java.security.SecureRandom
import java.security.cert.CertificateException
import java.security.cert.X509Certificate
import java.util.concurrent.TimeUnit
import javax.net.ssl.*

val apiModule = module {
    factory { aipService() }
}

fun aipService(): ApiInterface {
    val okHttpClient: OkHttpClient = headersInjectedHTTPClient
    return Retrofit.Builder()
        .addConverterFactory(
            GsonConverterFactory.create(
                instance.gson
            )
        )
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
        .baseUrl(siteInfoUrl)
        .client(okHttpClient)
        .build().create(ApiInterface::class.java)
}

fun retrofitInstance(): Retrofit {
    val okHttpClient: OkHttpClient = headersInjectedHTTPClient
    return Retrofit.Builder()
        .addConverterFactory(
            GsonConverterFactory.create(
                instance.gson
            )
        )
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
        .baseUrl(siteInfoUrl)
        .client(okHttpClient)
        .build()
}

fun retrofitNoLog(): Retrofit {
    return Retrofit.Builder()
        .addConverterFactory(GsonConverterFactory.create(instance.gson))
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
        .baseUrl(siteInfoUrl)
        .build()
}


/**
 * @return HTTP client with headers injected
 */
private val headersInjectedHTTPClient: OkHttpClient
    get() {
        var builder: OkHttpClient.Builder = OkHttpClient.Builder()
        builder = injectRequestHeadersBuilder(builder)
        if (BuildConfig.DEBUG) {
            builder = injectLoggingBuilder(builder)
        }
        builder = injectUnsafeBuilder(builder)
        builder.interceptors().add(Interceptor { chain -> onOnIntercept(chain) })
        builder.connectTimeout(15, TimeUnit.SECONDS)
        return builder.build()
    }

@Throws(IOException::class)
private fun onOnIntercept(chain: Interceptor.Chain): Response {
    val request = chain.request().newBuilder().header("X-App-Token", getToken()).build()
    try {
        return chain.proceed(request)
    } catch (exception: SocketTimeoutException) {
        exception.printStackTrace()
    }
    return chain.proceed(request)
}

private fun injectRequestHeadersBuilder(builder: OkHttpClient.Builder): OkHttpClient.Builder {
    builder.addInterceptor { chain ->
        val request: Request = chain.request()
        chain.proceed(request)
    }
    return builder
}

private fun injectLoggingBuilder(builder: OkHttpClient.Builder): OkHttpClient.Builder {
    val loggingInterceptor: HttpLoggingInterceptor = HttpLoggingInterceptor()
    loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
    builder.addInterceptor(loggingInterceptor)
    return builder
}

private val unsafeHostnameVerifier: HostnameVerifier
    get() {
        return HostnameVerifier { hostname, session -> true }
    }

private fun injectUnsafeBuilder(builder: OkHttpClient.Builder): OkHttpClient.Builder {
    builder.hostnameVerifier(unsafeHostnameVerifier)
    val unsafeTrustManager: X509TrustManager =
        object : X509TrustManager {
            @Throws(CertificateException::class)
            override fun checkClientTrusted(
                x509Certificates: Array<X509Certificate>,
                s: String
            ) {
            }

            @Throws(CertificateException::class)
            override fun checkServerTrusted(
                x509Certificates: Array<X509Certificate>,
                s: String
            ) {
            }

            override fun getAcceptedIssuers(): Array<X509Certificate?> {
                return arrayOfNulls(0)
            }
        }
    val trustAllCerts: Array<TrustManager> =
        arrayOf(unsafeTrustManager)
    var sslContext: SSLContext? = null
    try {
        sslContext = SSLContext.getInstance("SSL")
    } catch (e: NoSuchAlgorithmException) {
        e.printStackTrace()
    }
    try {
        assert(sslContext != null)
        sslContext!!.init(null, trustAllCerts, SecureRandom())
    } catch (e: KeyManagementException) {
        e.printStackTrace()
    }
    // Create an ssl socket factory with our all-trusting manager
    val sslSocketFactory: SSLSocketFactory = sslContext!!.socketFactory
    builder.sslSocketFactory(sslSocketFactory, unsafeTrustManager)
    return builder
}

private val siteInfoUrl: String
    get() {
        return "https://rth-recruitment.herokuapp.com/"
    }

private fun getToken(): String {
    return "76524a53ee60602ac3528f38"
}

