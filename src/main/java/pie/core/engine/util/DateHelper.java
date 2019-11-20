package pie.core.engine.util;

import pie.core.engine.validation.IsoapExcetion;
import pie.core.engine.validation.Guard;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


public class DateHelper {
	
	private final static Log Logger = LogFactory.getLog(DateHelper.class);

    // order is like this because the SimpleDateFormat.parse does not fail with exception
    // if it can parse a valid date out of a substring of the full string given the mask
    // so we have to check the most complete format first, then it fails with exception
    private static final String[] RFC822_MASKS = {
            "EEE, dd MMM yy HH:mm:ss z",
            "EEE, dd MMM yy HH:mm z",
            "dd MMM yy HH:mm:ss z",
            "dd MMM yy HH:mm z"
        };
	
	  // order is like this because the SimpleDateFormat.parse does not fail with exception
    // if it can parse a valid date out of a substring of the full string given the mask
    // so we have to check the most complete format first, then it fails with exception
    private static final String[] W3CDATETIME_MASKS = {
        "yyyy-MM-dd'T'HH:mm:ss.SSSz",
        "yyyy-MM-dd't'HH:mm:ss.SSSz",
        "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'",
        "yyyy-MM-dd't'HH:mm:ss.SSS'z'",
        "yyyy-MM-dd'T'HH:mm:ssz",
        "yyyy-MM-dd't'HH:mm:ssz",
        "yyyy-MM-dd'T'HH:mm:ss'Z'",
        "yyyy-MM-dd't'HH:mm:ss'z'",
        "yyyy-MM-dd'T'HH:mmz",   // together with logic in the parseW3CDateTime they
        "yyyy-MM'T'HH:mmz",      // handle W3C dates without time forcing them to be GMT
        "yyyy'T'HH:mmz",          
        "yyyy-MM-dd't'HH:mmz", 
        "yyyy-MM-dd'T'HH:mm'Z'", 
        "yyyy-MM-dd't'HH:mm'z'", 
        "yyyy-MM-dd",
        "yyyy-MM",
        "yyyy"
    };
	
	public static Date normalize(Date date) {
		Calendar cal = Calendar.getInstance();
		cal.setTimeInMillis(date.getTime());
		cal.set(Calendar.MILLISECOND, 0);
		return new Date(cal.getTimeInMillis());
	}
	
    /**
     * create a W3C Date Time representation of a date.
     * <p/>
     * Refer to the java.text.SimpleDateFormat javadocs for details on the format of each element.
     * <p/>
     * @param date Date to parse
     * @return the W3C Date Time represented by the given Date
     *         It returns <b>null</b> if it was not possible to parse the date.
     *
     */
    public static String formatW3CDateTime(Date date) {
        SimpleDateFormat dateFormater = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'",Locale.US);
        dateFormater.setTimeZone(TimeZone.getTimeZone("GMT"));
        return dateFormater.format(date);
    }
    
    /**
     * Parses a Date out of a String with a date in W3C date-time format.
     * <p/>
     * It parsers the following formats:
     * <ul>
     *   <li>"yyyy-MM-dd'T'HH:mm:ssz"</li>
     *   <li>"yyyy-MM-dd'T'HH:mmz"</li>
     *   <li>"yyyy-MM-dd"</li>
     *   <li>"yyyy-MM"</li>
     *   <li>"yyyy"</li>
     * </ul>
     * <p/>
     * Refer to the java.text.SimpleDateFormat javadocs for details on the format of each element.
     * <p/>
     * @param sDate string to parse for a date.
     * @return the Date represented by the given W3C date-time string.
     *         It returns <b>null</b> if it was not possible to parse the given string into a Date.
     *
     */
    public static Date parseW3CDateTime(String sDate) {
        // if sDate has time on it, it injects 'GTM' before de TZ displacement to
        // allow the SimpleDateFormat parser to parse it properly
        int tIndex = sDate.indexOf("T");
        if (tIndex>-1) {
            if (sDate.endsWith("Z")) {
                sDate = sDate.substring(0,sDate.length()-1)+"+00:00";
            }
            int tzdIndex = sDate.indexOf("+",tIndex);
            if (tzdIndex==-1) {
                tzdIndex = sDate.indexOf("-",tIndex);
            }
            if (tzdIndex>-1) {
                String pre = sDate.substring(0,tzdIndex);
                int secFraction = pre.indexOf(",");
                if (secFraction>-1) {
                    pre = pre.substring(0,secFraction);
                }
                String post = sDate.substring(tzdIndex);
                sDate = pre + "GMT" + post;
            }
        }
        else {
            sDate += "T00:00GMT";
        }
        return parseUsingMask(W3CDATETIME_MASKS,sDate);
    }
    
    /**
     * Parses a Date out of a string using an array of masks.
     * <p/>
     * It uses the masks in order until one of them succedes or all fail.
     * <p/>
     *
     * @param masks array of masks to use for parsing the string
     * @param sDate string to parse for a date.
     * @return the Date represented by the given string using one of the given masks.
     * It returns <b>null</b> if it was not possible to parse the the string with any of the masks.
     *
     */
    private static Date parseUsingMask(String[] masks,String sDate) {
        sDate = (sDate!=null) ? sDate.trim() : null;
        ParsePosition pp = null;
        Date d = null;
        for (int i=0;d==null && i<masks.length;i++) {
            DateFormat df = new SimpleDateFormat(masks[i],Locale.US);
            //df.setLenient(false);
            df.setLenient(true);
            try {
                pp = new ParsePosition(0);
                d = df.parse(sDate,pp);
                if (pp.getIndex()!=sDate.length()) {
                    d = null;
                }
                ////System.out.println("pp["+pp.getIndex()+"] s["+sDate+" m["+masks[i]+"] d["+d+"]");
            }
            catch (Exception ex1) {
            	Logger.error(ex1.getMessage(), ex1);
            }
        }
        return d;
    }
    
    
    /**
     * Parses a Date out of a String with a date in RFC822 format.
     * <p/>
     * It parsers the following formats:
     * <ul>
     *   <li>"EEE, dd MMM yyyy HH:mm:ss z"</li>
     *   <li>"EEE, dd MMM yyyy HH:mm z"</li>
     *   <li>"EEE, dd MMM yy HH:mm:ss z"</li>
     *   <li>"EEE, dd MMM yy HH:mm z"</li>
     *   <li>"dd MMM yyyy HH:mm:ss z"</li>
     *   <li>"dd MMM yyyy HH:mm z"</li>
     *   <li>"dd MMM yy HH:mm:ss z"</li>
     *   <li>"dd MMM yy HH:mm z"</li>
     * </ul>
     * <p/>
     * Refer to the java.text.SimpleDateFormat javadocs for details on the format of each element.
     * <p/>
     * @param sDate string to parse for a date.
     * @return the Date represented by the given RFC822 string.
     *         It returns <b>null</b> if it was not possible to parse the given string into a Date.
     *
     */
    public static Date parseRFC822(String sDate) {
        int utIndex = sDate.indexOf(" UT");
        if (utIndex>-1) {
            String pre = sDate.substring(0,utIndex);
            String post = sDate.substring(utIndex+3);
            sDate = pre + " GMT" + post;
        }
        Date result = parseUsingMask(RFC822_MASKS,sDate);
        if(result == null){
        	result = parseW3CDateTime(sDate);				// For J2me compatibility
        }
        return result;
    }

    /**
     * create a RFC822 representation of a date.
     * <p/>
     * Refer to the java.text.SimpleDateFormat javadocs for details on the format of each element.
     * <p/>
     * @param date Date to parse
     * @return the RFC822 represented by the given Date
     *         It returns <b>null</b> if it was not possible to parse the date.
     *
     */
    public static String formatRFC822(Date date) {
        SimpleDateFormat dateFormater = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss 'GMT'",Locale.US);
        dateFormater.setTimeZone(TimeZone.getTimeZone("GMT"));
        return dateFormater.format(date);
    }
    
    public static String formatDateTime(Date date){
    	if(date == null){
    		return "";
    	}
    	return String.valueOf(date.getTime());
    }
    
    public static Date parseDateTime(String dateTimeAsString){
    	if(dateTimeAsString == null || dateTimeAsString.length() == 0){
    		return null;
    	}
    	try{
    		return new Date(Long.valueOf(dateTimeAsString));
    	} catch(NumberFormatException e){
    		return null;
    	}
    }
    
	// yyyy-MM-dd'T'HH:mm:ss'Z'
	public static String formatDateYYYYMMDDHHMMSS(Date date, String dateSeparator, String dateTimeSeparator, String endIndicator, TimeZone timeZone) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		if(timeZone != null){
			cal.setTimeZone(timeZone);
		}

		String month = String.valueOf(cal.get(Calendar.MONTH) + 1);
		month = month.length() < 2 ? "0" + month : month;
		String day = String.valueOf(cal.get(Calendar.DAY_OF_MONTH));
		day = day.length() < 2 ? "0" + day : day;
		String hour = String.valueOf(cal.get(Calendar.HOUR_OF_DAY));
		hour = hour.length() < 2 ? "0" + hour : hour;
		String minute = String.valueOf(cal.get(Calendar.MINUTE));
		minute = minute.length() < 2 ? "0" + minute : minute;
		String second = String.valueOf(cal.get(Calendar.SECOND));
		second = second.length() < 2 ? "0" + second : second;

		StringBuffer sb = new StringBuffer();
		sb.append(cal.get(Calendar.YEAR));
		sb.append(dateSeparator);
		sb.append(month);
		sb.append(dateSeparator);
		sb.append(day);
		sb.append(dateTimeSeparator);
		sb.append(hour);
		sb.append(":");
		sb.append(minute);
		sb.append(":");
		sb.append(second);
		if (endIndicator != null) {
			sb.append(endIndicator);
		}
		return sb.toString();
	}

	// y y y y - M M - d d T  H  H  :  m  m  :  s  s  Z
	// 0 1 2 3 4 5 6 7 8 9 10 11 12 13 14 15 16 17 18 19
	@SuppressWarnings("deprecation")
	public static Date parseDateYYYYMMDDHHMMSS(String sDate, TimeZone timeZone) {
		
		try{
			int year = Integer.parseInt(sDate.substring(0, 4));
			int month = Integer.parseInt(sDate.substring(5, 7)) - 1;
			int day = Integer.parseInt(sDate.substring(8, 10));
			int hour = Integer.parseInt(sDate.substring(11, 13));
			int minute = Integer.parseInt(sDate.substring(14, 16)) - 1;
			int second = Integer.parseInt(sDate.substring(17, 19));
			
			Calendar cal = Calendar.getInstance();
			if(timeZone != null){
				cal.setTimeZone(timeZone);
			}
			cal.set(Calendar.YEAR, year);
			cal.set(Calendar.MONTH, month);
			cal.set(Calendar.DAY_OF_MONTH, day);
			cal.set(Calendar.HOUR_OF_DAY, hour);
			cal.set(Calendar.MINUTE, minute);
			cal.set(Calendar.SECOND, second);
			return cal.getTime();
		} catch (Exception e) {
			return new Date(sDate);
		}
	}

	public static Date parseDate(String dateAsString, String format) {
		Guard.argumentNotNull(dateAsString, "dateAsString");

		SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format);
		Date dateAndTime;
		try {
			dateAndTime = simpleDateFormat.parse(dateAsString);
		} catch (ParseException e) {
			throw new IsoapExcetion(e);
		}
		return dateAndTime;
	}
	
}
