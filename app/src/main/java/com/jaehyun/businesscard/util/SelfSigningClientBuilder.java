package com.jaehyun.businesscard.util;

import android.content.Context;
import android.util.Log;

import com.jaehyun.businesscard.R;

import java.io.IOException;
import java.io.InputStream;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;

import okhttp3.OkHttpClient;

public class SelfSigningClientBuilder {

    public static OkHttpClient.Builder addBuilder(Context context, OkHttpClient.Builder okHttpClientBuilder) {

        CertificateFactory cf;
        Certificate ca;
        SSLContext sslContext;
        InputStream caInput;
        try {
            cf = CertificateFactory.getInstance("X.509");
            caInput = context.getResources().openRawResource(R.raw.my_cert);

            ca = cf.generateCertificate(caInput);
            System.out.println("ca=" + ((X509Certificate) ca).getSubjectDN());

            // Create a KeyStore containing our trusted CAs
            String keyStoreType = KeyStore.getDefaultType();
            KeyStore keyStore = KeyStore.getInstance(keyStoreType);
            keyStore.load(null,null);
            keyStore.setCertificateEntry("ca", ca);

            // Create a TrustManager that trusts the CAs in our KeyStore
            String tmfAlgorithm = TrustManagerFactory.getDefaultAlgorithm();
            TrustManagerFactory tmf = TrustManagerFactory.getInstance(tmfAlgorithm);
            tmf.init(keyStore);


            // Create an SSLContext that uses our TrustManager
            sslContext = SSLContext.getInstance("TLS");
            sslContext.init(null, tmf.getTrustManagers(), null);

            okHttpClientBuilder = okHttpClientBuilder
                    .sslSocketFactory(sslContext.getSocketFactory(), (X509TrustManager) tmf.getTrustManagers()[0]);

            okHttpClientBuilder.hostnameVerifier((hostname, session) -> {
                if (hostname.contentEquals("10.0.2.2")) {
                    Log.d("test", "Approving certificate for host " + hostname);
                    return true;
                }else {
                    Log.d("test", "fail " + hostname);
                    return false;
                }
            });

            caInput.close();
        } catch (KeyStoreException | CertificateException | NoSuchAlgorithmException | IOException | KeyManagementException e) {
            e.printStackTrace();
        }
        return okHttpClientBuilder;
    }

}