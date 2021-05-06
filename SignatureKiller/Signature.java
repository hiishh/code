package com.android.patch;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.util.Base64;
import com.pittvandewitt.viperfx.ViPER4Android; //Optional
import java.io.ByteArrayInputStream; 
import java.io.DataInputStream;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
//Extends Viper4Android , replace this with the original Application class name
public class Signature extends ViPER4Android implements InvocationHandler {
    private static final int GET_SIGNATURES = 64;
    private String appPkgName = "";
    private Object base;
    private byte[][] sign;

    public void attachBaseContext(Context base2) {
        hook(base2);
        super.attachBaseContext(base2);
    }

    @Override // java.lang.reflect.InvocationHandler
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        if ("getPackageInfo".equals(method.getName())) {
            String pkgName = (String) args[0];
            if ((((Integer) args[1]).intValue() & 64) != 0 && this.appPkgName.equals(pkgName)) {
                PackageInfo info = (PackageInfo) method.invoke(this.base, args);
                info.signatures = new android.content.pm.Signature[this.sign.length];
                for (int i = 0; i < info.signatures.length; i++) {
                    info.signatures[i] = new android.content.pm.Signature(this.sign[i]);
                }
                return info; //returning false sig
            }
        }
        return method.invoke(this.base, args);
    }

    private void hook(Context context) {
        try {
          //  Below b64 is the signature.
            DataInputStream is = new DataInputStream(new ByteArrayInputStream(Base64.decode("AQAAA38wggN7MIICY6ADAgECAgQL8MeNMA0GCSqGSIb3DQEBCwUAMG4xCzAJBgNVBAYTAk5MMQww\nCgYDVQQIEwNHTEQxETAPBgNVBAcTCE5pam1lZ2VuMRUwEwYDVQQKEwxkZSBXaXR0IEluYy4xFTAT\nBgNVBAsTDEhlYWRxdWFydGVyczEQMA4GA1UEAxMHZGUgV2l0dDAeFw0xODAzMzExNTI3MzdaFw00\nMzAzMjUxNTI3MzdaMG4xCzAJBgNVBAYTAk5MMQwwCgYDVQQIEwNHTEQxETAPBgNVBAcTCE5pam1l\nZ2VuMRUwEwYDVQQKEwxkZSBXaXR0IEluYy4xFTATBgNVBAsTDEhlYWRxdWFydGVyczEQMA4GA1UE\nAxMHZGUgV2l0dDCCASIwDQYJKoZIhvcNAQEBBQADggEPADCCAQoCggEBAJUVwBZC106y6xbLlXPk\nMrn1+iFgCqFLbj9Sv/LV6O8UMyQrmxS1inEt6VNF7PpGGybpojzp9EPm4e0MkdNCE2GCopfGdOvM\n3s4mfs/CisgGs58C7opVTnULdG+U1GVCGpblboywxbnglK48lVs8aKNuXm+ZCRox8PIRaoU1aAya\nFooJVx5aOGTttQuwBTGgBvv8IoM3NLdE7NZcW4Sxe5NFhyiLk5O5d5kjfqr+sPPS2jG/XVjrKdSl\ns+d/LS5SOx3wRwl2mVku3ywAOS/rvbaKKRGun3aQ6VQY7pSJz2hJZCZ14a8W9Botbfz0qi3KVoqr\nBPw4izqXjFlnDUx9NDkCAwEAAaMhMB8wHQYDVR0OBBYEFHrxzkQR5piBTSHBVxsMRGwNZ+TVMA0G\nCSqGSIb3DQEBCwUAA4IBAQAn76WQ5blBWOqAquKTUTnxiBWPCnpGah84WnXpn7EOGp5nCSkyJowi\nyrX/pcxukYubjK785FtXj2NyAHR+z+U6xhpMz4eB/K0vcc9AIREGmcmZgS/uHDyOhKQzFtoThTxn\nzEWda5utWdihN7v95ymp7w1Qo2A9/dHEkDtvgz+WhPjTUBGC2KejBoVD/Urq14sGIsIPoV6uaF3F\nNQeO+ge3pE6D+Pw+DtIwbyjosQGjElKNUbSHK0wwa6rPGzBRj3/NUG2s6YX9s0nwl1Yt/qt9XCzv\ne9f2osWPQ/1t0kICf0mwp03VnqAKmCXOUbt7IMjrBF3vp8lrL6FykAdJeMit\n", 0)));
            byte[][] sign2 = new byte[(is.read() & 255)][];
            for (int i = 0; i < sign2.length; i++) {
                sign2[i] = new byte[is.readInt()];
                is.readFully(sign2[i]);
            }
            Class<?> activityThreadClass = Class.forName("android.app.ActivityThread");
            Object currentActivityThread = activityThreadClass.getDeclaredMethod("currentActivityThread", new Class[0]).invoke(null, new Object[0]);
            Field sPackageManagerField = activityThreadClass.getDeclaredField("sPackageManager");
            sPackageManagerField.setAccessible(true); //setting packagemanager class accessible (overrides private void))
            Object sPackageManager = sPackageManagerField.get(currentActivityThread);
            Class<?> iPackageManagerInterface = Class.forName("android.content.pm.IPackageManager");
            this.base = sPackageManager;
            this.sign = sign2;
            this.appPkgName = context.getPackageName();
            Object proxy = Proxy.newProxyInstance(iPackageManagerInterface.getClassLoader(), new Class[]{iPackageManagerInterface}, this); //Proxies original class to this fake one, to return false sig.
            sPackageManagerField.set(currentActivityThread, proxy);
            PackageManager pm = context.getPackageManager();
            Field mPmField = pm.getClass().getDeclaredField("mPM");
            mPmField.setAccessible(true);
            mPmField.set(pm, proxy);
            System.out.println("Hook success.");
        } catch (Exception e) {
            System.err.println("Hook failed.");
            e.printStackTrace();
        }
    }
}
