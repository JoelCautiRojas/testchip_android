package com.gkn.testchip.domain;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.net.Uri;
import android.telephony.TelephonyManager;

public class APNConfig {

    public final Context context;
    private static final Uri APN_TABLE_URI = Uri.parse("content://telephony/carriers");
    private static final Uri PREFERRED_APN_URI = Uri.parse("content://telephony/carriers/preferapn");

    public APNConfig(Context context) {
        this.context = context;
    }

    public String getAPNSelect(){
        String APN = "";
        Cursor c = context.getContentResolver().query(
                PREFERRED_APN_URI,
                null,
                null,
                null,
                null
        );

        if (c.moveToFirst()) {
            String cadena = "";
            do {
                String data = c.getString(c.getColumnIndex("name"));
                cadena = cadena + data + " ";
            } while (c.moveToNext());

            APN = cadena;
        }
        c.close();
        return APN.trim();
    }

    public int insertAPN(String name, String apn_addr) {
        try {
        int id = -1;
        ContentResolver resolver = context.getContentResolver();
        ContentValues values = new ContentValues();
        values.put("name", name);
        values.put("apn", apn_addr);
        TelephonyManager tel = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        String networkOperator = tel.getNetworkOperator();
        int mcc = 0;
        int mnc = 0;
        if (networkOperator != null) {
            mcc = Integer.valueOf(networkOperator.substring(0, 3));
            mnc = Integer.valueOf(networkOperator.substring(3));
        }
        values.put("mcc", mcc);
        values.put("mnc", mnc);
        values.put("numeric", networkOperator);
        Cursor c = null;
            Uri newRow = resolver.insert(APN_TABLE_URI, values);
            if (newRow != null) {
                c = resolver.query(newRow, null, null, null, null);
                //printAllData(c) //Print the entire result set
                // Obtain the apn id
                int idindex = c.getColumnIndex("_id");
                c.moveToFirst();
                id = c.getShort(idindex);
            }
            c.close();
            return id;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return -1;
    }

    public Boolean setDefaultAPN(int id) {
        boolean res = false;
        ContentResolver resolver = context.getContentResolver();
        ContentValues values = new ContentValues();
        values.put("apn_id", id);
        try {
            resolver.update(PREFERRED_APN_URI, values, null, null);
            Cursor c = resolver.query(
                    PREFERRED_APN_URI, new String[]{"name", "apn"},
                    "_id=$id",
                    null,
                    null
            );
            if (c != null) {
                res = true;
                c.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return res;
    }
}
