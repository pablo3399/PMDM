package com.example.androidtimemodificado.core;

/** Represents the date and time info retrieved from server. */
public class DateTime {
    public static DateTime INVALID = new DateTime( "", "", "", "", "", 0.0 );
    private static final String NotApplicable = "N/A";

    /** Creates a date and time object.
     * @param dateTime the date and time info, as a string.
     * @param timeInfo info related to the time, as a string.
     * @param gmtInfo the gmt zone info, as a string.
     */

    public DateTime(String dateTime, String timeInfo, String gmtInfo, String sunrise, String sunset, double latitud)
    {
        if ( dateTime == null
                || dateTime.isEmpty() )
        {
            dateTime = NotApplicable;
        }

        if ( timeInfo == null
                || timeInfo.isEmpty() )
        {
            timeInfo = NotApplicable;
        }

        if ( gmtInfo == null
                || gmtInfo.isEmpty() )
        {
            gmtInfo = NotApplicable;
        }

        if ( sunrise == null
                || sunrise.isEmpty() )
        {
            sunrise = NotApplicable;
        }

        if ( sunset == null
                || sunset.isEmpty() )
        {
            sunset = NotApplicable;
        }


        this.dateTime = dateTime.trim();
        this.timeInfo = timeInfo.trim();
        this.gmtInfo = gmtInfo.trim();
        this.sunrise = sunrise.trim();
        this.sunset = sunset.trim();
        this.latitud = latitud;
    }

    /** @return the date and time info, as a string. */
    public String getDateTime()
    {
        return this.dateTime;
    }

    /** @return the time info, as a string. */
    public String getTimeInfo()
    {
        return this.timeInfo;
    }

    /** @return the gmt info, as a string. */
    public String getGmtInfo()
    {
        return this.gmtInfo;
    }

    /** @return the sunrise info, as a string. */
    public String getSunrise()
    {
        return this.sunrise;
    }
    /** @return the sunset info, as a string. */
    public String getSunset()
    {
        return this.sunset;
    }

    /** @return the sunset info, as a string. */
    public double getLatitud()
    {
        return this.latitud;
    }

    private String dateTime;
    private String timeInfo;
    private String gmtInfo;
    private String sunrise;
    private String sunset;
    private double latitud;
}
