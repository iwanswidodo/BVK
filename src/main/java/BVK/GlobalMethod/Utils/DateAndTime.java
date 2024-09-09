package BVK.GlobalMethod.Utils;


import org.joda.time.DateTime;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import static BVK.GlobalMethod.Assertion.Assertions.assertTrue;


public class DateAndTime 
{
	/*	To get the Current Time */
	public static String getTime() {
		Date date = new Date();
		SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm");
		return dateFormat.format(date);
	}

	/*	To get the Current Date */
	public static String getDate() {
		Date date = new Date();
		SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMMM yyyy");
		return dateFormat.format(date);
	}

	public static String getDate(int dateOffset,String dateFormat) {
		Calendar current = Calendar.getInstance();
		current.add(Calendar.DATE, dateOffset);
		SimpleDateFormat DF = new SimpleDateFormat(dateFormat);
		Date date = new Date(current.getTimeInMillis());
		return DF.format(date);
	}

	/*	To get the Current Date */
	public static String getDate2() {
		Date date = new Date();
		SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMMM ''yy");
		return dateFormat.format(date);
	}




	public static String tanggal = getDate();
	public static String getafterDday(int tgl) throws ParseException {
		DateFormat dateFormat = new SimpleDateFormat("dd MMMM yy");
		Calendar cal = Calendar.getInstance();
		cal.setTime(dateFormat.parse(tanggal));
		cal.add(Calendar.DATE, tgl);
		return dateFormat.format(cal.getTime());
	}

	public static String getMonth(int interval) {
		DateFormat dateFormat = new SimpleDateFormat("dd MMMM yy");
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.MONTH, interval);
		String result = dateFormat.format(cal.getTime());
		return result;
	}

	public static String getMonth2(int intervalDays) {
		DateFormat dateFormat = new SimpleDateFormat("MMM yyyy");
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DATE, intervalDays);
		String result = dateFormat.format(cal.getTime());
		return result;
	}

	public static String getYear(int intervalyear) {
		DateFormat dateFormat = new SimpleDateFormat("yyyy");
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.YEAR, intervalyear);
		String result = dateFormat.format(cal.getTime());
		return result;
	}
	/**
	 *
	 * To get the Date in n days in the future
	 * Format is dd MMMM yyyy
	 *
	*/
	public static String getFutureDate1(int n) {
		Calendar current = Calendar.getInstance();
		current.add(Calendar.DATE, n);
		SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMMM yyyy");
		Date date = new Date(current.getTimeInMillis());
		return dateFormat.format(date);
	}

	/**
	 *
	 * To get the Date in n days in the future
	 * Format is dd MMMM 'yy
	 *
	 */
	public static String getFutureDate2(int n) {
		Calendar current = Calendar.getInstance();
		current.add(Calendar.DATE, n);
		SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMMM ''yy");
		Date date = new Date(current.getTimeInMillis());
		return dateFormat.format(date);
	}

	public static String getFutureDate3(int n) {
		Calendar current = Calendar.getInstance();
		current.add(Calendar.DATE, n);
		SimpleDateFormat dateFormat = new SimpleDateFormat("MMMM dd, yyyy");
		Date date = new Date(current.getTimeInMillis());
		return dateFormat.format(date);
	}

	public static String getFutureDate4(int n) {
		Calendar current = Calendar.getInstance();
		current.add(Calendar.DATE, n);
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		Date date = new Date(current.getTimeInMillis());
		return dateFormat.format(date);
	}

	public static String getFutureDate5(int n) {
		Calendar current = Calendar.getInstance();
		current.add(Calendar.DATE, n);
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
		Date date = new Date(current.getTimeInMillis());
		return dateFormat.format(date);
	}

	public static String getFutureDate6(int n) {
		Calendar current = Calendar.getInstance();
		current.add(Calendar.DATE, n);
		SimpleDateFormat dateFormat = new SimpleDateFormat("d");
		Date date = new Date(current.getTimeInMillis());
		return dateFormat.format(date);
	}

	public static String getFutureDate7(int n) {
		Calendar current = Calendar.getInstance();
		current.add(Calendar.DATE, n);
		SimpleDateFormat dateFormat = new SimpleDateFormat("d MMM yyyy");
		Date date = new Date(current.getTimeInMillis());
		return dateFormat.format(date);
	}

	public static String getFutureDate8(int n) {
		Calendar current = Calendar.getInstance();
		current.add(Calendar.DATE, n);
	/*	dd/MM/yyy HH:mm:ss
		yyyy-MM-dd HH:mm:ss.SSSSSS*/
		SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
		Date date = new Date(current.getTimeInMillis());
		return dateFormat.format(date);
	}

	public static String getFutureDate9(int n) {
		Calendar current = Calendar.getInstance();
		current.add(Calendar.DATE, n);
		SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyy HH:mm:ss");
		Date date = new Date(current.getTimeInMillis());
		return dateFormat.format(date);
	}

	/*	To get the Current Month in Integer */
	public static int getMonth_Integer() {
		DateTime datetime = DateTime.now();
		return datetime.getMonthOfYear();
	}
	
	/*	To get the Current Month Text as Full in String*/
	public static String getMonth_Full() {
		DateTime datetime = DateTime.now();
		return datetime.monthOfYear().getAsText();
	}
	
	/*	To get the Current Month Text as Short in String*/
	public static String getMonth_Short() {
		DateTime datetime = DateTime.now();
		return datetime.monthOfYear().getAsShortText();
	}
	
	/*	To get the Current Day of the Month */
	public static String getDayOfTheMonth() {
		DateTime datetime = DateTime.now();
		return datetime.dayOfMonth().getAsText();
	}
	
	/*	To get the Current Day Count in the Year */
	public static String getDayCount() {
		DateTime datetime = DateTime.now();
		return datetime.dayOfYear().getAsText();
	}
	
	/*	To get the Current Minute of the Hour in String */
	public static String getMinuteOfTheHourAsString() {
		DateTime datetime = DateTime.now();
		return datetime.minuteOfHour().getAsText();
	}
	
	/*	To get the Current Year as Integer */
	public static int getYear() {
		DateTime datetime = DateTime.now();
		return datetime.getYear();
	}
	
	/*	To get the Current Hour of the Day in String */
	public static String getHourOfTheDay() {
		DateTime datetime = DateTime.now();
		return datetime.hourOfDay().getAsShortText();
	}
	
	/*	To get the Current Week Count */
	public static String getWeekCount() {
		DateTime datetime = DateTime.now();
		return datetime.weekOfWeekyear().getAsText();
	}

	//convert from eproc API
	public static String convertDate(String date) {
		DateTimeFormatter dateFormatter
				= DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSSSS'Z'");
		DateTimeFormatter dateFormatterNew
				= DateTimeFormatter.ofPattern("dd MMM yyyy',' HH:mm 'WIB'");
		// string to LocalDateTime
		LocalDateTime ldateTime = LocalDateTime.parse(date, dateFormatter);
		return dateFormatterNew.format(ldateTime.plusHours(7));
	}

	//convert from raw DB date
	public static String convertDate2(String date) {
		DateTimeFormatter dateFormatter
				= DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSSSSS");
		DateTimeFormatter dateFormatterNew
				= DateTimeFormatter.ofPattern("dd MMM yyyy',' HH:mm 'WIB'");
		// string to LocalDateTime
		LocalDateTime ldateTime = LocalDateTime.parse(date, dateFormatter);
		return dateFormatterNew.format(ldateTime.plusHours(7));
	}

	public static String getFutureDate8Plan(int n) {
		Calendar current = Calendar.getInstance();
		current.add(Calendar.DATE, n);
		SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss.SSSSSS");
		/*yyyy-MM-dd HH:mm:ss.SSSSSS
		dd/MM/yyyy HH:mm:ss.SSSSSS*/
		Date date = new Date(current.getTimeInMillis());
		return dateFormat.format(date);
	}
	public static void assertDateFormatSlash(String date){

		String day = date.substring(0,2);
		String dayMonthSeparator = date.substring(2,3);
		String month = date.substring(3,5);
		String monthYearSeparator = date.substring(5,6);
		String year = date.substring(6,10);

		assertTrue(day.matches("0[1-9]|[12][0-9]|3[01]"));
		assertTrue(dayMonthSeparator.equals("/"));
		assertTrue(month.matches("0[1-9]|1[0-2]"));
		assertTrue(monthYearSeparator.equals("/"));
		assertTrue(year.matches("(19|20)\\d\\d"));
	}
	public static void assertDateFormatMins(String date){

		String day = date.substring(0,2);
		String dayMonthSeparator = date.substring(2,3);
		String month = date.substring(3,5);
		String monthYearSeparator = date.substring(5,6);
		String year = date.substring(6,10);

		assertTrue(day.matches("0[1-9]|[12][0-9]|3[01]"));
		assertTrue(dayMonthSeparator.equals("-"));
		assertTrue(month.matches("0[1-9]|1[0-2]"));
		assertTrue(monthYearSeparator.equals("-"));
		assertTrue(year.matches("(19|20)\\d\\d"));
	}
}
