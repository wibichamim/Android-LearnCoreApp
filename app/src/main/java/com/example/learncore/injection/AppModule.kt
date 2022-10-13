package com.example.learncore.injection

import android.content.Context
import android.os.Build
import com.crocodic.core.data.CoreSession
import com.crocodic.core.helper.okhttp.SSLTrust
import com.example.learncore.BuildConfig
import com.example.learncore.api.ApiService
import com.google.gson.FieldNamingPolicy
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.scalars.ScalarsConverterFactory
import java.util.concurrent.TimeUnit
import javax.net.ssl.SSLContext

@InstallIn(SingletonComponent::class)
@Module
class AppModule {

    @Provides
    fun provideSession(@ApplicationContext context: Context) = CoreSession(context)

    @Provides
    fun provideGson() = GsonBuilder().setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES).create()

    @Provides
    fun provideOkHttpClient(session: CoreSession): OkHttpClient {

        val deviceModel = "Android-${Build.VERSION.SDK_INT}-${Build.VERSION.CODENAME}-${Build.MANUFACTURER}-${Build.MODEL}-${Build.DEVICE}"

        val unsafeTrustManager = SSLTrust().createUnsafeTrustManager()
        val sslContext = SSLContext.getInstance("SSL")
        sslContext.init(null, arrayOf(unsafeTrustManager), null)

        val okHttpClient = OkHttpClient().newBuilder()
            .sslSocketFactory(sslContext.socketFactory,  unsafeTrustManager)
            .connectTimeout(90, TimeUnit.SECONDS)
            .readTimeout(90, TimeUnit.SECONDS)
            .writeTimeout(90, TimeUnit.SECONDS)
            .addInterceptor { chain ->
                val original = chain.request()

                val requestBuilder = original.newBuilder()
                    //.header("Accept-Encoding", "identity")
                    .header("User-Agent", "${BuildConfig.APPLICATION_ID} ${BuildConfig.VERSION_NAME} $deviceModel")
                    .header("App-Version", "${BuildConfig.VERSION_CODE}")
                    .method(original.method, original.body)

//                val token = session.getString(Const.SESSION.PREF_TOKEN)
//
//                if (token.isNotEmpty()) {
//                    requestBuilder.header("Authorization", "Bearer $token")
//                } else {
//                    requestBuilder.header("Authorization", "Basic ${getBasicAuth()}")
//                }

                val fcmId = session.getString(CoreSession.PREF_FCMID)

                if (fcmId.isNotEmpty()) {
                    requestBuilder.header("regid", fcmId)
                }

                val request = requestBuilder.build()
                chain.proceed(request)
            }

        if (BuildConfig.DEBUG) {
            val interceptors = HttpLoggingInterceptor()
            interceptors.level = HttpLoggingInterceptor.Level.BODY
            okHttpClient.addInterceptor(interceptors)
        }

        return okHttpClient.build()
    }

    @Provides
    fun provideApiService(okHttpClient: OkHttpClient): ApiService {
        return Retrofit.Builder()
            .baseUrl(BuildConfig.BASE_URL)
            .addConverterFactory(ScalarsConverterFactory.create())
            .client(okHttpClient)
            .build().create(ApiService::class.java)
    }

}