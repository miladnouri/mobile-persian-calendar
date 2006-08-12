package calendar;

public class PersianDate extends AbstractDate {
	private static final String[] monthName = { "",
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
	
	private int year;

	private int month;

	private int day;

	public PersianDate(int year, int month, int day) {	
		setYear(year);
		// Initialize day, so that we get no exceptions when setting month
		this.day = 1;
		setMonth(month);
		setDayOfMonth(day);
	}

	public int getDayOfMonth() {
		return day;
	}

	public int getMonth() {
		return month;
	}

	public String getMonthName() {
		return monthName[month];
	}

	public int getWeekOfYear() {
		throw new RuntimeException("not implemented yet!");
	}

	public int getYear() {
		return year;
	}

	public void rollDay(int amount, boolean up) {
		throw new RuntimeException("not implemented yet!");
	}

	public void rollMonth(int amount, boolean up) {
		throw new RuntimeException("not implemented yet!");
	}

	public void rollYear(int amount, boolean up) {
		throw new RuntimeException("not implemented yet!");
	}

	public void setDayOfMonth(int day) {
		if(day < 1)
			throw new DayOutOfRangeException("day " + day + " is out of range!");
			
		if(month <= 6 && day > 31)
			throw new DayOutOfRangeException("day " + day + " is out of range!");
		
		if(month > 6 && month <= 12 && day > 30)
			throw new DayOutOfRangeException("day " + day + " is out of range!");
		
		// TODO check for the special case of leap year for Esfand
		this.day = day;
	}

	public void setMonth(int month) {
		if(month < 1 || month > 12)
			throw new MonthOutOfRangeException("month " + month + " is out of range!");

		// Set the day again, so that exceptions are thrown if the
		// day is out of range
		setDayOfMonth(day);
		
		this.month = month;
	}

	public void setYear(int year) {
		if (year == 0)
			throw new YearOutOfRangeException("Year 0 is invalid!");
				
		this.year = year;
	}

	public String getEvent() {
		throw new RuntimeException("not implemented yet!");
	}

	public int getDayOfWeek() {
		throw new RuntimeException("not implemented yet!");
	}
	
	public int getDayOfYear() {
		throw new RuntimeException("not implemented yet!");
	}

	public int getWeekOfMonth() {
		throw new RuntimeException("not implemented yet!");
	}

	public boolean isLeapYear() {
		throw new RuntimeException("not implemented yet!");
	}
	
	public String toString() {
		return day + " " + monthName[month] + " " + year;
	}
}
