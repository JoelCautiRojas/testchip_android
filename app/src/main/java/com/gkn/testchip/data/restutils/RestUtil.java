package com.gkn.testchip.data.restutils;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gkn.testchip.BuildConfig;

import java.io.IOException;
import java.io.InputStream;
import java.security.GeneralSecurityException;
import java.security.KeyStore;
import java.security.cert.Certificate;
import java.security.cert.CertificateFactory;
import java.util.Arrays;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;

import okhttp3.CipherSuite;
import okhttp3.ConnectionSpec;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.TlsVersion;
import okhttp3.logging.HttpLoggingInterceptor;
import okio.Buffer;

public class RestUtil {

    private static final Integer timeoutHTTP = 60;

    //
    //
    // METODOS EXPUESTOS
    //
    //

    public static OkHttpClient getHttpClient(String token) {
        X509TrustManager trustManager = null;
        SSLSocketFactory sslSocketFactory = null;
        try {
            trustManager = trustManagerForCertificates(trustedCertificatesInputStream());
            SSLContext sslContext = SSLContext.getInstance("TLSv1.2");
            sslContext.init(null, new TrustManager[]{trustManager}, null);
            sslSocketFactory = sslContext.getSocketFactory();
        } catch (GeneralSecurityException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        //httpClient.connectionPool(new ConnectionPool(1,1,TimeUnit.SECONDS));
        httpClient.readTimeout(timeoutHTTP, TimeUnit.SECONDS);
        httpClient.connectTimeout(timeoutHTTP, TimeUnit.SECONDS);
        //httpClient.retryOnConnectionFailure(false);
        //httpClient.sslSocketFactory(sslSocketFactory, trustManager);
        httpClient.connectionSpecs(Arrays.asList(getConnectionSpecModernTLS(), ConnectionSpec.COMPATIBLE_TLS,ConnectionSpec.CLEARTEXT));
        if(!token.equals("")){
            httpClient.addInterceptor(getHttpTokenInterceptor(token));
        }
        if (BuildConfig.DEBUG) {
            httpClient.addInterceptor(getHttpLoggingInterceptor());
        }
        httpClient.cache(null);
        return httpClient.build();
    }

    //
    //
    // METODOS AUXILIARES
    //
    //

    public static InputStream trustedCertificatesInputStream() {
        String CertificationAuthority = getInputStreamDEV();
        return new Buffer()
                .writeUtf8(CertificationAuthority)
                .inputStream();
    }

    public static X509TrustManager trustManagerForCertificates(InputStream in) throws GeneralSecurityException, IOException {
        CertificateFactory cf = CertificateFactory.getInstance("X.509");
        Certificate ca;
        try {
            ca = cf.generateCertificate(in);
        } finally {
            in.close();
        }
        // creating a KeyStore containing our trusted CAs
        String keyStoreType = KeyStore.getDefaultType();
        KeyStore keyStore = KeyStore.getInstance(keyStoreType);
        keyStore.load(null, null);
        keyStore.setCertificateEntry("ca", ca);
        // creating a TrustManager that trusts the CAs in our KeyStore
        String tmfAlgorithm = TrustManagerFactory.getDefaultAlgorithm();
        TrustManagerFactory tmf = TrustManagerFactory.getInstance(tmfAlgorithm);
        tmf.init(keyStore);
        return (X509TrustManager) tmf.getTrustManagers()[0];
    }

    private static String getInputStreamDEV() {
        String CertificationAuthority = "" +
                "-----BEGIN CERTIFICATE-----\n"+
                "MIIFFjCCAv6gAwIBAgIRAJErCErPDBinU/bWLiWnX1owDQYJKoZIhvcNAQELBQAw\n"+
                "TzELMAkGA1UEBhMCVVMxKTAnBgNVBAoTIEludGVybmV0IFNlY3VyaXR5IFJlc2Vh\n"+
                "cmNoIEdyb3VwMRUwEwYDVQQDEwxJU1JHIFJvb3QgWDEwHhcNMjAwOTA0MDAwMDAw\n"+
                "WhcNMjUwOTE1MTYwMDAwWjAyMQswCQYDVQQGEwJVUzEWMBQGA1UEChMNTGV0J3Mg\n"+
                "RW5jcnlwdDELMAkGA1UEAxMCUjMwggEiMA0GCSqGSIb3DQEBAQUAA4IBDwAwggEK\n"+
                "AoIBAQC7AhUozPaglNMPEuyNVZLD+ILxmaZ6QoinXSaqtSu5xUyxr45r+XXIo9cP\n"+
                "R5QUVTVXjJ6oojkZ9YI8QqlObvU7wy7bjcCwXPNZOOftz2nwWgsbvsCUJCWH+jdx\n"+
                "sxPnHKzhm+/b5DtFUkWWqcFTzjTIUu61ru2P3mBw4qVUq7ZtDpelQDRrK9O8Zutm\n"+
                "NHz6a4uPVymZ+DAXXbpyb/uBxa3Shlg9F8fnCbvxK/eG3MHacV3URuPMrSXBiLxg\n"+
                "Z3Vms/EY96Jc5lP/Ooi2R6X/ExjqmAl3P51T+c8B5fWmcBcUr2Ok/5mzk53cU6cG\n"+
                "/kiFHaFpriV1uxPMUgP17VGhi9sVAgMBAAGjggEIMIIBBDAOBgNVHQ8BAf8EBAMC\n"+
                "AYYwHQYDVR0lBBYwFAYIKwYBBQUHAwIGCCsGAQUFBwMBMBIGA1UdEwEB/wQIMAYB\n"+
                "Af8CAQAwHQYDVR0OBBYEFBQusxe3WFbLrlAJQOYfr52LFMLGMB8GA1UdIwQYMBaA\n"+
                "FHm0WeZ7tuXkAXOACIjIGlj26ZtuMDIGCCsGAQUFBwEBBCYwJDAiBggrBgEFBQcw\n"+
                "AoYWaHR0cDovL3gxLmkubGVuY3Iub3JnLzAnBgNVHR8EIDAeMBygGqAYhhZodHRw\n"+
                "Oi8veDEuYy5sZW5jci5vcmcvMCIGA1UdIAQbMBkwCAYGZ4EMAQIBMA0GCysGAQQB\n"+
                "gt8TAQEBMA0GCSqGSIb3DQEBCwUAA4ICAQCFyk5HPqP3hUSFvNVneLKYY611TR6W\n"+
                "PTNlclQtgaDqw+34IL9fzLdwALduO/ZelN7kIJ+m74uyA+eitRY8kc607TkC53wl\n"+
                "ikfmZW4/RvTZ8M6UK+5UzhK8jCdLuMGYL6KvzXGRSgi3yLgjewQtCPkIVz6D2QQz\n"+
                "CkcheAmCJ8MqyJu5zlzyZMjAvnnAT45tRAxekrsu94sQ4egdRCnbWSDtY7kh+BIm\n"+
                "lJNXoB1lBMEKIq4QDUOXoRgffuDghje1WrG9ML+Hbisq/yFOGwXD9RiX8F6sw6W4\n"+
                "avAuvDszue5L3sz85K+EC4Y/wFVDNvZo4TYXao6Z0f+lQKc0t8DQYzk1OXVu8rp2\n"+
                "yJMC6alLbBfODALZvYH7n7do1AZls4I9d1P4jnkDrQoxB3UqQ9hVl3LEKQ73xF1O\n"+
                "yK5GhDDX8oVfGKF5u+decIsH4YaTw7mP3GFxJSqv3+0lUFJoi5Lc5da149p90Ids\n"+
                "hCExroL1+7mryIkXPeFM5TgO9r0rvZaBFOvV2z0gp35Z0+L4WPlbuEjN/lxPFin+\n"+
                "HlUjr8gRsI3qfJOQFy/9rKIJR0Y/8Omwt/8oTWgy1mdeHmmjk7j1nYsvC9JSQ6Zv\n"+
                "MldlTTKB3zhThV1+XWYp6rjd5JW1zbVWEkLNxE7GJThEUG3szgBVGP7pSWTUTsqX\n"+
                "nLRbwHOoq7hHwg==\n"+
                "-----END CERTIFICATE-----\n";
                /*"-----BEGIN CERTIFICATE-----\n" +
                "MIIF1TCCA72gAwIBAgIQOdises9OM56u+G8g1Xy4czANBgkqhkiG9w0BAQsFADB0\n" +
                "MQswCQYDVQQGEwJQRTENMAsGA1UECBMETGltYTENMAsGA1UEBxMETGltYTERMA8G\n" +
                "A1UEChMIR2xvYm9rYXMxCzAJBgNVBAsTAklUMScwJQYDVQQDEx5HbG9ib2thcyBD\n" +
                "ZXJ0aWZpY2F0ZSBBdXRob3JpdHkwHhcNMTkwNzMwMTYwNTU4WhcNMjkwNzI3MTYw\n" +
                "NTU0WjB0MQswCQYDVQQGEwJQRTENMAsGA1UECBMETGltYTENMAsGA1UEBxMETGlt\n" +
                "YTERMA8GA1UEChMIR2xvYm9rYXMxCzAJBgNVBAsTAklUMScwJQYDVQQDEx5HbG9i\n" +
                "b2thcyBDZXJ0aWZpY2F0ZSBBdXRob3JpdHkwggIiMA0GCSqGSIb3DQEBAQUAA4IC\n" +
                "DwAwggIKAoICAQDSfYqlw+yL2w48Bd6eBpRR5TNR+eW07c779GKdLEzeeoOYKsVd\n" +
                "ZHLHJJg1QJgZZTk88oLqYH/bqgRsGbOgmTBnly8xxkh6A4T3jUWXA3j13OFFbA1c\n" +
                "kwcrFxD8QfzhKoRJDQrdizCko/UKlEKsmaVwanYWC84Xqfoav0pXNRn7ULN6m4hl\n" +
                "7E0eEpW9aWyLgdtmd2W9lGj2FScn2LD0nVpopzDIcqDRxIPtPlz62Hcl6SmOGfYs\n" +
                "LRj23LFdtEbYwNzcGs/Nuix5quEDkMEWZNsRUa02jXEaFofedXJuS/0A+KzmMoEk\n" +
                "nIYFXjIde/YLwE6NjKjjWTPtbIBUboac6Uq1G1K/bAmV2W9LeoPDVd7Y9q2djiZx\n" +
                "Q2BuMfwrq2usptkJEFZv+3Qrk/TP+FTi7BPcHaclc3gEMMlMWpuw+PGcf+5F55hn\n" +
                "EGokETj0mIxID5rnNe1AglxBIkYlJjc2Q+coSY6qKvIGiJVQAIkox5+mh/ksPS3R\n" +
                "4uqUJvtBWOtIgnx7J52/Fx/7piAdrDT5As9OK8cJ3+rrOUpP5bzCjWbTOeI3DbNQ\n" +
                "Q2QOHRas3KuxhjEaZdwZEC2FtGyUXPTC4AAs5+r46chMsN1pRsu28rtxJXQydGi3\n" +
                "MQMDEpc+BjKowr0jXbzCFyYmzKkzRL07MGJRnuq0mGJbE3/gcfnSgcRBdQIDAQAB\n" +
                "o2MwYTAOBgNVHQ8BAf8EBAMCAYYwDwYDVR0TAQH/BAUwAwEB/zAdBgNVHQ4EFgQU\n" +
                "E3zKnF4GeRQwu0AFFcVNfOFNeL4wHwYDVR0jBBgwFoAUE3zKnF4GeRQwu0AFFcVN\n" +
                "fOFNeL4wDQYJKoZIhvcNAQELBQADggIBAHuiJAWKeC26iklLEG9iEgh9igHyoFNp\n" +
                "cbtCEZbME1ACv+l0EAk71aP3OL1vC04o1/CcmWsP6qiyGBx+80IdlW4jOc+I7gmB\n" +
                "KW1Qnn2wkjjrJqmQtsZsslPHsPeZFktgFEPpfYNiV8ZiOHfGrLTqBQT7bs3CMUjs\n" +
                "1eeJUWF5bIQQqcJm22bYveptZlTPy5CdobgQsPd4fNktwO+RojssOV0T1W60SrEt\n" +
                "PjYIi2pc8cCkaGeE+ljfN8H6hKUJ+gjF/mqr0zgxGEqU/e9DSix86mVFMhJt5z+Y\n" +
                "AHm37KT2d2GBtiGu7G4kqamz+dNlMdb7DbDZ53VQyGQSS0EnyQBtQq3oaDKKgPma\n" +
                "d8INE7hmhLo0/o7wEPlkayW2sd+Dm93pLUs6UyWXeBE22R/PpJkP8/dQVgyg50Wf\n" +
                "m3pkve30lcTGxmgzN7diS3VZLZjvnRN2Xd6e0VSA6eWAqubFBxMAYoeNt/O2KlKj\n" +
                "HQHSV5V5od4y6DrJ/vxaKuFHnIJTU2zbn5IJQDSR5uPl0uca7tWku6cbRypCNZti\n" +
                "j/jBj4xk2nV7tqC6ZS4uI18CpIiqQ1Weaa7t0mSruzIRyUIiQNjCicRHLJCwkivY\n" +
                "gSMBnU49MruyL1WPjtRWsr/Jcrrkpa3QST29w8t6VMWL+h1x/pNyhc1LUwC9mO5a\n" +
                "pz8KGngoYNN7\n" +
                "-----END CERTIFICATE-----\n";*/
        return CertificationAuthority;
    }

    private static Interceptor getHttpTokenInterceptor(final String token){
        return new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request request = chain.request().newBuilder().addHeader("Authorization", "Bearer " + token).build();
                return chain.proceed(request);
            }
        };
    }

    private static Interceptor getHttpTenantInterceptor(final String tenant){
        return new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request request = chain.request().newBuilder().addHeader("tenant", tenant).build();
                return chain.proceed(request);
            }
        };
    }

    private static Interceptor getHttpImeiInterceptor(final String imei){
        return new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request request = chain.request().newBuilder().addHeader("imei", imei).build();
                return chain.proceed(request);
            }
        };
    }

    private static ConnectionSpec getConnectionSpecModernTLS(){
        ConnectionSpec spec = new
                ConnectionSpec.Builder(ConnectionSpec.MODERN_TLS)
                .tlsVersions(TlsVersion.TLS_1_2)
                .cipherSuites(
                        CipherSuite.TLS_ECDHE_ECDSA_WITH_AES_128_GCM_SHA256,
                        CipherSuite.TLS_ECDHE_RSA_WITH_AES_128_GCM_SHA256,
                        CipherSuite.TLS_DHE_RSA_WITH_AES_128_GCM_SHA256)
                .build();
        return spec;
    }

    private static HttpLoggingInterceptor getHttpLoggingInterceptor(){
        HttpLoggingInterceptor logInterceptor = new HttpLoggingInterceptor();
        logInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        return logInterceptor;
    }

    public static ObjectMapper getObjectMapperConfiguration() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.enable(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY);
        mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        return mapper;
    }
}
