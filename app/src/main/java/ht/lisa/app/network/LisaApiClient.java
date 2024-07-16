package ht.lisa.app.network;

import android.annotation.SuppressLint;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import ht.lisa.app.util.SharedPreferencesUtil;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

import static ht.lisa.app.util.Settings.TOKEN_HEADER;
import static ht.lisa.app.util.Settings.TOKEN_MOVILOT;

class LisaApiClient {

    private static final String BASE_URL = "https://haiti.movilot.pro:6464/";

    private static Retrofit retrofit = buildClient();
    private static LisaApiService apiService;


    private static Retrofit buildClient() {
        if (retrofit == null) {
            OkHttpClient client =
                    buildUnsafeOkHttp(
                            new OkHttpClient
                                    .Builder()
                                    .readTimeout(60, TimeUnit.SECONDS)
                                    .connectTimeout(60, TimeUnit.SECONDS)
                                    .writeTimeout(60, TimeUnit.SECONDS)
                                    .retryOnConnectionFailure(true)
                                    .addInterceptor(new MyInterceptor()));

            Gson gson = new GsonBuilder()
                    .setLenient()
                    .create();

            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .client(client)
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .build();
        }
        return retrofit;
    }

    static LisaApiService getApiService() {
        if (apiService == null) {
            apiService = retrofit.create(LisaApiService.class);
        }
        return apiService;
    }

    private static OkHttpClient buildUnsafeOkHttp(OkHttpClient.Builder builder) {
        try {
            final TrustManager[] trustAllCerts = new TrustManager[]{
                    new X509TrustManager() {
                        @SuppressLint("TrustAllX509TrustManager")
                        @Override
                        public void checkClientTrusted(java.security.cert.X509Certificate[] chain, String authType) {
                        }

                        @SuppressLint("TrustAllX509TrustManager")
                        @Override
                        public void checkServerTrusted(java.security.cert.X509Certificate[] chain, String authType) {
                        }

                        @Override
                        public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                            return new java.security.cert.X509Certificate[]{};
                        }
                    }
            };

            final SSLContext sslContext = SSLContext.getInstance("SSL");
            sslContext.init(null, trustAllCerts, new java.security.SecureRandom());
            final SSLSocketFactory sslSocketFactory = sslContext.getSocketFactory();

            builder.sslSocketFactory(sslSocketFactory, (X509TrustManager) trustAllCerts[0]);
            builder.hostnameVerifier((hostname, session) -> true);

            return builder.build();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    static class MyInterceptor implements Interceptor {

        @NonNull
        @Override
        public Response intercept(Interceptor.Chain chain) throws IOException {
            try {
                Thread.sleep(new Random().nextInt(500) + 200);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            Request request = chain.request();
            Request.Builder builder = request.newBuilder();

            if (SharedPreferencesUtil.isAuthorized()) {
                builder.addHeader(TOKEN_HEADER, TOKEN_MOVILOT + " " + SharedPreferencesUtil.getToken());
            }

            Request authorizedRequest = builder.build();

            long t1 = System.nanoTime();
            Log.d("ApiClient",
                    String.format("Sending request %s on %n%s", request.url(), authorizedRequest.headers()));

            Response response = chain.proceed(authorizedRequest);

            long t2 = System.nanoTime();

            Log.d("ApiClient",
                    String.format("Received response for %s in %.1fms%n%s", response.request().url(),
                            (t2 - t1) / 1e6d, response.headers()));
            Log.d("UDBVIRN", request.url().toString());
            /*if (request.url().toString().equals("https://haiti.movilot.pro:6464/api/user_profile")) {
                Log.d("LOGTAGURL", response.body().string());
            }*/

            return response;
        }
    }
}
