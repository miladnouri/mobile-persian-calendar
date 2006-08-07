import javax.microedition.lcdui.*;
import javax.microedition.midlet.*;
import java.util.*;

public class PersianCalendar extends MIDlet implements CommandListener {

	private static final String[] persianMonthName = { "",
			"\u0641\u0631\u0648\u0631\u062f\u064a\u0646", // Farvardin
			"\u0627\u0631\u062f\u064a\u0628\u0647\u0634\u062a", // Ordibehesht
			"\u062e\u0631\u062f\u0627\u062f", // Khordad
			"\u062a\u064a\u0631", // Tir
			"\u0645\u0631\u062f\u0627\u062f", // Mordad
			"\u0634\u0647\u0631\u064a\u0648\u0631", // Shahrivar
			"\u0645\u0647\u0631", // Mehr
			"\u0622\u0628\u0627\u0646", // Aban
			"\u0622\u0630\u0631", // Azar
			"\u062f\u064a", // Dey
			"\u0628\u0647\u0645\u0646", // Bahman
			"\u0627\u0633\u0641\u0646\u062f" // Esfand
	};

	private static final String[] islamicMonthName = {
			"",
			"\u0645\u062D\u0631\u0645", // Moharram
			"\u0635\u0641\u0631", // Safar
			"\u0631\u0628\u064A\u0639 \u0627\u0644\u0627\u0648\u0644", // Rabi
																		// 1
			"\u0631\u0628\u064A\u0639 \u0627\u0644\u062B\u0627\u0646\u064A", // Rabi
																				// 2
			"\u062C\u0645\u0627\u062F\u064A \u0627\u0644\u0627\u0648\u0644", // Jamdi
																				// 1
			"\u062C\u0645\u0627\u062F\u064A \u0627\u0644\u062B\u0627\u0646\u064A", // Jamadi
																					// 2
			"\u0631\u062C\u0628", // Rajab
			"\u0634\u0639\u0628\u0627\u0646", // Shaban
			"\u0631\u0645\u0636\u0627\u0646", // Ramezan
			"\u0634\u0648\u0627\u0644", // Shavval
			"\u0630\u064A \u0627\u0644\u0642\u0639\u062F\u0647", // Zel Ghade
			"\u0630\u064A \u0627\u0644\u062D\u062C\u0647" // Zel Hajje
	};

	private static final String[] civilMonthName = { "",
			"\u0698\u0627\u0646\u0648\u064A\u0647", // Janvie
			"\u0641\u0648\u0631\u064A\u0647", // Fevrie
			"\u0645\u0627\u0631\u0633", // Mars
			"\u0622\u0648\u0631\u064A\u0644", // Avril
			"\u0645\u0647", // Meh
			"\u0698\u0648\u0646", // Juan
			"\u062C\u0648\u0644\u0627\u064A", // July
			"\u0622\u06AF\u0648\u0633\u062A", // Agost
			"\u0633\u067E\u062A\u0627\u0645\u0628\u0631", // Septambr
			"\u0627\u0643\u062A\u0628\u0631", // Octobr
			"\u0646\u0648\u0627\u0645\u0628\u0631", // Novambr
			"\u062F\u0633\u0627\u0645\u0628\u0631" // Desambr
	};

	private static final String SHANBEH = "\u0634\u0646\u0628\u0647";

	private static final String[] weekdays = { "", "\u064a\u06A9" + SHANBEH, // 1
																				// shanbeh
			"\u062F\u0648" + SHANBEH, // 2 shanbeh
			"\u0633\u0647 " + SHANBEH, // 3 shanbeh
			"\u0686\u0647\u0627\u0631" + SHANBEH, // 4 shanbeh
			"\u067E\u0646\u062C" + SHANBEH, // 5 shanbeh
			"\u062C\u0645\u0639\u0647", // jome
			SHANBEH // shanbe
	};

	private Command exitCommand;

	private Command aboutCommand;

	private Command backCommand;

	private Display display;

	private Alert aboutAlert;

	private Alert mainAlert;

	// CONSTRUCTOR
	public PersianCalendar() {
		display = Display.getDisplay(this);
		// Emrooz
		mainAlert = new Alert("\u0627\u0645\u0631\u0648\u0632");
		mainAlert.setCommandListener(this);
		mainAlert.setType(AlertType.CONFIRMATION);
		mainAlert.setTimeout(Alert.FOREVER);

		exitCommand = new Command("\u062e\u0631\u0648\u062c", Command.BACK, 0);
		mainAlert.addCommand(exitCommand);

		aboutCommand = new Command("\u062F\u0631\u0628\u0627\u0631\u0647", Command.HELP, 1);
		mainAlert.addCommand(aboutCommand);


		aboutAlert = new Alert("\u062F\u0631\u0628\u0627\u0631\u0647");
		aboutAlert.setCommandListener(this);
		aboutAlert.setType(AlertType.INFO);
		aboutAlert.setTimeout(Alert.FOREVER);
		aboutAlert.setString(getAboutString());

		backCommand = new Command("\u0628\u0627\u0632\u06AF\u0634\u062A", Command.BACK, 0);
		aboutAlert.addCommand(backCommand);

		display.setCurrent(mainAlert);
	}

	protected void startApp() throws MIDletStateChangeException {
		Calendar c = Calendar.getInstance();
		int cy = c.get(Calendar.YEAR);
		int cm = c.get(Calendar.MONTH) + 1;
		int cd = c.get(Calendar.DAY_OF_MONTH);

		MyDate persian = civil_persian(cy, cm, cd);
		MyDate islamic = civil_islamic(cy, cm, cd);

		String today = " " + weekdays[c.get(Calendar.DAY_OF_WEEK)] + "\n "
				+ persian.day + " " + persianMonthName[persian.month] + " "
				+ persian.year + "\n " + islamic.day + " "
				+ islamicMonthName[islamic.month] + " " + islamic.year + "\n "
				+ cd + " " + civilMonthName[cm] + " " + cy;

		mainAlert.setString(today);

	}

	protected void pauseApp() {
	}

	public void destroyApp(boolean unconditional) {
	}

	public void commandAction(Command command, Displayable d) {
		if(d == mainAlert) {
			if (command == exitCommand)
				notifyDestroyed();

			if (command == aboutCommand)
				display.setCurrent(aboutAlert);
		}

		if( d == aboutAlert) {
			if (command == backCommand)
				display.setCurrent(mainAlert);
		}
	}

	// ***** CONVERSION METHODS *****

	private static final MyDate civil_persian(int year, int month, int day) {
		return jdn_persian(civil_jdn(year, month, day));
	}

	// private static final int[] civil_islamic(int year, int month, int day) {
	// return jdn_islamic(civil_jdn(year, month, day));
	// }

	private static final long civil_jdn(int year, int month, int day) {

		long lYear = year;
		long lMonth = month;
		long lDay = day;
		if ((year > 1582) || ((year == 1582) && (month > 10))
				|| ((year == 1582) && (month == 10) && (day > 14))) {
			return ((1461 * (lYear + 4800 + ((lMonth - 14) / 12))) / 4)
					+ ((367 * (lMonth - 2 - 12 * (((lMonth - 14) / 12)))) / 12)
					- ((3 * (((lYear + 4900 + ((lMonth - 14) / 12)) / 100))) / 4)
					+ lDay - 32075;
		} else
			return julian_jdn(lYear, lMonth, lDay);

	}

	private static final long julian_jdn(long lYear, long lMonth, long lDay) {

		return 367 * lYear - ((7 * (lYear + 5001 + ((lMonth - 9) / 7))) / 4)
				+ ((275 * lMonth) / 9) + lDay + 1729777;

	}

	private static final long persian_jdn(int year, int month, int day) {
		final long PERSIAN_EPOCH = 1948321; // The JDN of 1 Farvardin 1

		long epbase;
		if (year >= 0)
			epbase = year - 474;
		else
			epbase = year - 473;

		long epyear = 474 + (epbase % 2820);

		long mdays;
		if (month <= 7)
			mdays = (month - 1) * 31;
		else
			mdays = (month - 1) * 30 + 6;

		return day + mdays + fix(((epyear * 682) - 110) / 2816) + (epyear - 1)
				* 365 + fix(epbase / 2820) * 1029983 + (PERSIAN_EPOCH - 1);
	}

	/*
	 * private static final MyDate jdn_civil(long jdn) {
	 *
	 * if (jdn > 2299160) { long l = jdn + 68569; long n = ((4 * l) / 146097); l =
	 * l - ((146097 * n + 3) / 4); long i = ((4000 * (l + 1)) / 1461001); l = l -
	 * ((1461 * i) / 4) + 31; long j = ((80 * l) / 2447); int day = (int)(l -
	 * ((2447 * j) / 80)); l = (j / 11); int month = (int)(j + 2 - 12 * l); int
	 * year = (int)(100 * (n - 49) + i + l); return new MyDate(year, month,
	 * day); } else return jdn_julian(jdn);
	 *  }
	 *
	 * private static final MyDate jdn_julian(long jdn) { long j = jdn + 1402;
	 * long k = ((j - 1) / 1461); long l = j - 1461 * k; long n = ((l - 1) /
	 * 365) - (l / 1461); long i = l - 365 * n + 30; j = ((80 * i) / 2447); int
	 * day = (int)(i - ((2447 * j) / 80)); i = (j / 11); int month = (int)(j + 2 -
	 * 12 * i); int year = (int)(4 * k + n + i - 4716);
	 *
	 * return new MyDate(year, month, day); }
	 */

	private static final MyDate jdn_persian(long jdn) {

		long depoch = jdn - persian_jdn(475, 1, 1);
		long cycle = fix(depoch / 1029983);
		long cyear = depoch % 1029983;
		long ycycle;
		long aux1, aux2;

		if (cyear == 1029982)
			ycycle = 2820;
		else {
			aux1 = fix(cyear / 366);
			aux2 = cyear % 366;
			ycycle = (((2134 * aux1) + (2816 * aux2) + 2815) / 1028522) + aux1
					+ 1;
		}

		int year, month, day; // to be returned in an array
		year = (int) (ycycle + (2820 * cycle) + 474);
		if (year <= 0)
			year = year - 1;

		long yday = (jdn - persian_jdn(year, 1, 1)) + 1;
		if (yday <= 186)
			month = (int) Math.ceil(yday / 31d);
		else
			month = (int) Math.ceil((yday - 6) / 30d);

		day = (int) (jdn - persian_jdn(year, month, 1)) + 1;

		return new MyDate(year, month, day);
	}

	private static final int fix(double d) {
		if (d >= 0)
			return (int) Math.floor(d);
		else
			return (int) Math.floor(d) + 1;
	}

	private static final MyDate civil_islamic(int year, int month, int day) {

		long k = (long) (0.6 + (year + (month % 2 == 0 ? month : month - 1)
				/ 12d + day / 365f - 1900) * 12.3685);
		long jd = civil_jdn(year, month, day);

		double mjd;
		do {
			mjd = visibility(k);
			k = k - 1;
		} while (mjd > (jd - 0.5));

		k = k + 1;
		long hm = k - 1048;

		year = 1405 + fix(hm / 12);
		month = (int) (hm % 12) + 1;

		if (hm != 0 && month <= 0) {
			month = month + 12;
			year = year - 1;
		}

		if (year <= 0)
			year = year - 1;

		day = (int) (jd - mjd + 0.5);

		return new MyDate(year, month, day);
	}

	private static final double visibility(long n) {

		// parameters for Makkah: for a new moon to be visible after sunset on
		// a the same day in which it started, it has to have started before
		// (SUNSET-MINAGE)-TIMZ=3 A.M. local time.
		final float TIMZ = 3f, MINAGE = 13.5f, SUNSET = 19.5f, // approximate
		TIMDIF = (SUNSET - MINAGE);

		double jd = tmoonphase(n, 0);
		long d = (long) jd;

		double tf = (jd - d);

		if (tf <= 0.5) // new moon starts in the afternoon
			return (jd + 1f);
		else { // new moon starts before noon
			tf = (tf - 0.5) * 24 + TIMZ; // local time
			if (tf > TIMDIF)
				return (jd + 1d); // age at sunset < min for visiblity
			else
				return jd;
		}

	}

	private static final double tmoonphase(long n, int nph) {

		final double RPD = (1.74532925199433E-02); // radians per degree
													// (pi/180)

		double xtra = 0;

		double k = n + nph / 4d;
		double T = k / 1236.85;
		double t2 = T * T;
		double t3 = t2 * T;
		double jd = 2415020.75933 + 29.53058868 * k - 0.0001178 * t2
				- 0.000000155 * t3 + 0.00033
				* Math.sin(RPD * (166.56 + 132.87 * T - 0.009173 * t2));

		// Sun's mean anomaly
		double sa = RPD
				* (359.2242 + 29.10535608 * k - 0.0000333 * t2 - 0.00000347 * t3);

		// Moon's mean anomaly
		double ma = RPD
				* (306.0253 + 385.81691806 * k + 0.0107306 * t2 + 0.00001236 * t3);

		// Moon's argument of latitude
		double tf = RPD
				* 2d
				* (21.2964 + 390.67050646 * k - 0.0016528 * t2 - 0.00000239 * t3);

		// should reduce to interval 0-1.0 before calculating further
		switch (nph) {
		case 0:
		case 2:
			xtra = (0.1734 - 0.000393 * T) * Math.sin(sa) + 0.0021
					* Math.sin(sa * 2) - 0.4068 * Math.sin(ma) + 0.0161
					* Math.sin(2 * ma) - 0.0004 * Math.sin(3 * ma) + 0.0104
					* Math.sin(tf) - 0.0051 * Math.sin(sa + ma) - 0.0074
					* Math.sin(sa - ma) + 0.0004 * Math.sin(tf + sa) - 0.0004
					* Math.sin(tf - sa) - 0.0006 * Math.sin(tf + ma) + 0.001
					* Math.sin(tf - ma) + 0.0005 * Math.sin(sa + 2 * ma);
			break;
		case 1:
		case 3:
			xtra = (0.1721 - 0.0004 * T) * Math.sin(sa) + 0.0021
					* Math.sin(sa * 2) - 0.628 * Math.sin(ma) + 0.0089
					* Math.sin(2 * ma) - 0.0004 * Math.sin(3 * ma) + 0.0079
					* Math.sin(tf) - 0.0119 * Math.sin(sa + ma) - 0.0047
					* Math.sin(sa - ma) + 0.0003 * Math.sin(tf + sa) - 0.0004
					* Math.sin(tf - sa) - 0.0006 * Math.sin(tf + ma) + 0.0021
					* Math.sin(tf - ma) + 0.0003 * Math.sin(sa + 2 * ma)
					+ 0.0004 * Math.sin(sa - 2 * ma) - 0.0003
					* Math.sin(2 * sa + ma);
			if (nph == 1)
				xtra = xtra + 0.0028 - 0.0004 * Math.cos(sa) + 0.0003
						* Math.cos(ma);
			else
				xtra = xtra - 0.0028 + 0.0004 * Math.cos(sa) - 0.0003
						* Math.cos(ma);

			break;
		default:
			return 0;
		}
		// convert from Ephemeris Time (ET) to (approximate)Universal Time (UT)
		return jd + xtra - (0.41 + 1.2053 * T + 0.4992 * t2) / 1440;
	}

	private static final String getAboutString() {
		String s = "\u062A\u0642\u0648\u064A\u0645\u0020\u0641\u0627\u0631\u0633\u064A\n" +
		"\u062A\u0648\u0633\u0639\u0647\u0020\u062F\u0647\u0646\u062F\u0647\u003A\u0020\u0627\u0645\u064A\u0631\u0631\u0636\u0627\u0020\u062E\u0633\u0631\u0648\u0634\u0627\u0647\u064A\n" +
		"khosroshahi@\n" +
		"ce.sharif.edu\n" +
		"\u0627\u064A\u0646\u0020\u064A\u0643\u0020\u0646\u0631\u0645\u0020\u0627\u0641\u0632\u0627\u0631\u0020\u0622\u0632\u0627\u062F\u0020\u0627\u0633\u062A\u0021\n";
		return s;
	}

}

class MyDate {
	int year;

	int month;

	int day;

	public MyDate(int year, int month, int day) {
		this.year = year;
		this.month = month;
		this.day = day;
	}

}
