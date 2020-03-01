package com.example.androidcpa.core;

public class TownHall {

    public static TownHall INVALID = new TownHall( "", "", 0.0, 0.0, "", "", "");
    private static final String NotApplicable = "N/A";

    public TownHall(String countryCode, String postalCode,   double longitud, double latitud,String placeName, String adminName1,
                    String adminName2 ){


        if ( countryCode == null
                || countryCode.isEmpty() )
        {
            countryCode = NotApplicable;
        }

        if ( placeName == null
                || placeName.isEmpty() )
        {
            placeName = NotApplicable;
        }


        if ( adminName1 == null
                || adminName1.isEmpty() )
        {
            adminName1 = NotApplicable;
        }

        if ( adminName2 == null
                || adminName2.isEmpty() ) {
            adminName2 = NotApplicable;
        }


        this.countryCode = countryCode.trim();
        this.placeName = placeName.trim();
        this.longitud = longitud;
        this.latitud = latitud;
        this.postalCode = postalCode.trim();
        this.adminName1 = adminName1.trim();
        this.adminName2 = adminName2.trim();

    }

    public String toString(){
        return this.adminName1 + " " + this.countryCode + " "+ this.placeName;
    }

    public String toString2(){
        return  this.adminName1 + "\n" + this.adminName2 + "\n"+ "Lugar: " + this.placeName
                + "\n"+ "Codigo de pais: " + this.countryCode
                + "\n"+ "Latitud: " + this.latitud
                + "\n"+ "Longitud: " +this.longitud;
    }

    /** @return the country, as a string. */
    public String getCountry(){
        return this.countryCode;
    }
    /** @return the placeName, as a string. */
    public String getPlaceName(){
        return this.placeName;
    }

    public String getPostalCode(){
        return this.postalCode;
    }
    public double getLatitud(){
        return this.latitud;
    }
    public double getLongitud(){
        return this.longitud;
    }
    public String getAdminName1(){
        return this.adminName1;
    }
    public String getAdminName2(){
        return this.adminName2;
    }
    private String countryCode;
    private String placeName;
    private String postalCode;
    private double longitud;
    private String adminName1;
    private String adminName2;
    private double latitud;
}
