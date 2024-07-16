package ht.lisa.app.model;

import android.util.Log;

import java.io.Serializable;
import java.text.DecimalFormat;

public class Poi implements Serializable {

    private String loc;
    private String addr;
    private double lat;
    private double lng;

    public Poi() {
    }

    public Poi(double lat, double lng) {
        this.lat = lat;
        this.lng = lng;
    }

    public String getLoc() {
        return loc;
    }

    public String getAddr() {
        return addr;
    }

    public double getLat() {
        return lat;
    }

    public double getLng() {
        return lng;
    }

    public static double calculateByDistance(Poi StartP, Poi EndP) {
        if (StartP == null || EndP == null) return 0;
        int Radius = 6371;
        double lat1 = StartP.getLat();
        double lat2 = EndP.getLat();
        double lon1 = StartP.getLng();
        double lon2 = EndP.getLng();
        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2)
                + Math.cos(Math.toRadians(lat1))
                * Math.cos(Math.toRadians(lat2)) * Math.sin(dLon / 2)
                * Math.sin(dLon / 2);
        double c = 2 * Math.asin(Math.sqrt(a));
        double valueResult = Radius * c;
        double km = valueResult / 1;
        DecimalFormat newFormat = new DecimalFormat("####");
        int kmInDec = Integer.parseInt(newFormat.format(km));
        double meter = valueResult % 1000;
        int meterInDec = Integer.parseInt(newFormat.format(meter));
        Log.i("Radius Value", "" + valueResult + "   KM  " + kmInDec
                + " Meter   " + meterInDec);

        return Radius * c;
    }
}
