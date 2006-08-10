import javax.microedition.lcdui.Alert;
import javax.microedition.lcdui.AlertType;
import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Display;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.Form;
import javax.microedition.lcdui.List;
import javax.microedition.lcdui.TextField;
import javax.microedition.midlet.MIDlet;
import javax.microedition.midlet.MIDletStateChangeException;

import calendar.CivilDate;
import calendar.DateConverter;
import calendar.DayOutOfRangeException;
import calendar.IslamicDate;
import calendar.MonthOutOfRangeException;
import calendar.PersianDate;
import calendar.AbstractDate;

public class PersianCalendar extends MIDlet implements CommandListener {

	private Display display;

	// Screens of this MIDlet

	private Alert aboutAlert;

	private Alert mainAlert;

	private List dateConvList;

	private Form dateConvForm;

	private Alert convDateAlert; // alert to show converted date
	
	private Alert convErrorAlert;

	// Commands of main screen (mainAlert)
	private Command exitCommand;

	private Command aboutCommand;

	private Command dateConvCommand;

	private Command backCommand; // used in many screens
	
	// Constants
	
	private static final int PERSIAN = 0;
	
	private static final int ISLAMIC = 1;

	private static final int CIVIL = 2;

	public PersianCalendar() {
		display = Display.getDisplay(this);

		// main screen
		mainAlert = new Alert("\u0627\u0645\u0631\u0648\u0632");
		mainAlert.setCommandListener(this);
		// mainAlert.setType(AlertType.CONFIRMATION);
		mainAlert.setTimeout(Alert.FOREVER);

		exitCommand = new Command("\u062e\u0631\u0648\u062c", Command.OK, 0);
		mainAlert.addCommand(exitCommand);
		dateConvCommand = new Command(
				"\u062A\u0628\u062F\u064A\u0644",
				"\u062A\u0628\u062F\u064A\u0644\u0020\u062A\u0627\u0631\u064A\u062E",
				Command.SCREEN, 1);
		mainAlert.addCommand(dateConvCommand);
		aboutCommand = new Command("\u062F\u0631\u0628\u0627\u0631\u0647",
				Command.HELP, 1);
		mainAlert.addCommand(aboutCommand);

		// about screen
		aboutAlert = new Alert("\u062F\u0631\u0628\u0627\u0631\u0647");
		aboutAlert.setType(AlertType.INFO);
		aboutAlert.setTimeout(Alert.FOREVER);
		aboutAlert.setString(getAboutString());
		backCommand = new Command("\u0628\u0627\u0632\u06AF\u0634\u062A",
				Command.BACK, 1);
		aboutAlert.addCommand(backCommand);
		aboutAlert.setCommandListener(new CommandListener() {
			public void commandAction(Command c, Displayable d) {
				display.setCurrent(mainAlert);
			}
		});

		// date conversion screen
		dateConvList = new List(
				"\u062A\u0628\u062F\u064A\u0644\u0020\u0627\u0632",
				List.IMPLICIT);
		dateConvList.append("\u0634\u0645\u0633\u064A", null);
		dateConvList.append("\u0642\u0645\u0631\u064A", null);
		dateConvList.append("\u0645\u064A\u0644\u0627\u062F\u064A", null);
		dateConvList.addCommand(backCommand);
		dateConvList.setCommandListener(new CommandListener() {
			public void commandAction(Command c, Displayable d) {
				if (c == List.SELECT_COMMAND) {
					int selIndex = dateConvList.getSelectedIndex();
					// lazy initialization
					if (dateConvForm == null)
						createDateConvScreens(selIndex);					
					customizeDateConvForm(selIndex);
					display.setCurrentItem(dateConvForm.get(0));

				} else { // back command
					display.setCurrent(mainAlert);
				}
			}

		});

	}

	public void startApp() throws MIDletStateChangeException {
		// Today
		CivilDate civil = new CivilDate();
		PersianDate persian = DateConverter.civilToPersian(civil);
		IslamicDate islamic = DateConverter.civilToIslamic(civil);

		String today = " " + civil.getDayOfWeekName() + "\n " + persian + "\n "
				+ islamic + "\n " + civil;

		mainAlert.setString(today);

		display.setCurrent(mainAlert);
	}

	public void pauseApp() {
	}

	public void destroyApp(boolean unconditional) {
	}

	public void commandAction(Command command, Displayable d) {
		if (d == mainAlert) {
			if (command == exitCommand)
				notifyDestroyed();

			if (command == aboutCommand)
				display.setCurrent(aboutAlert);

			if (command == dateConvCommand)
				display.setCurrent(dateConvList);

			return;
		}
	}

	private static final String getAboutString() {
		String s = "\u062A\u0642\u0648\u064A\u0645\u0020\u0641\u0627\u0631\u0633\u064A\n"
				+ "\u062A\u0648\u0633\u0639\u0647\u0020\u062F\u0647\u0646\u062F\u0647\u003A\u0020\u0627\u0645\u064A\u0631\u0631\u0636\u0627\u0020\u062E\u0633\u0631\u0648\u0634\u0627\u0647\u064A\n"
				+ "khosroshahi@\n"
				+ "ce.sharif.edu\n"
				+ "\u0627\u064A\u0646\u0020\u064A\u0643\u0020\u0646\u0631\u0645\u0020\u0627\u0641\u0632\u0627\u0631\u0020\u0622\u0632\u0627\u062F\u0020\u0627\u0633\u062A\u0021\n";
		return s;
	}

	private final void createDateConvScreens(int selIndex) {
		// Make date conversion form
		dateConvForm = new Form("");

		TextField yearField = new TextField("\u0633\u0627\u0644", "", 5,
				TextField.NUMERIC);
		dateConvForm.append(yearField);

		TextField monthField = new TextField("\u0645\u0627\u0647", "", 2,
				TextField.NUMERIC);
		dateConvForm.append(monthField);

		TextField dayField = new TextField("\u0631\u0648\u0632", "", 2,
				TextField.NUMERIC);
		dateConvForm.append(dayField);

		final Command convertCommand = new Command(
				"\u062A\u0628\u062F\u064A\u0644", Command.OK, 1);
		dateConvForm.addCommand(backCommand);
		dateConvForm.addCommand(convertCommand);

		dateConvForm.setCommandListener(new CommandListener() {
			public void commandAction(Command c, Displayable d) {
				if (c == backCommand)
					display.setCurrent(dateConvList);
				else if (c == convertCommand)
					convertDate();
			}
		});
		
		// Prefill the text fields with the date of today
		AbstractDate today = null;
		switch(selIndex) {
		case PERSIAN:
			today = DateConverter.civilToPersian(new CivilDate());
			yearField.setString(today.getYear() + "");
			monthField.setString(today.getMonth() + "");
			dayField.setString(today.getDayOfMonth() + "");
			break;
		case ISLAMIC:
			today = DateConverter.civilToIslamic(new CivilDate());
			yearField.setString(today.getYear() + "");
			monthField.setString(today.getMonth() + "");
			dayField.setString(today.getDayOfMonth() + "");
			break;
		case CIVIL:
			today = new CivilDate();
			yearField.setString(today.getYear() + "");
			monthField.setString(today.getMonth() + "");
			dayField.setString(today.getDayOfMonth() + "");
			break;
		}
		
		// Make the alert that shows the converted date
		convDateAlert = new Alert("\u062A\u0628\u062F\u064A\u0644");
		final Command againCommand = new Command(
				"\u062F\u0648\u0628\u0627\u0631\u0647", Command.SCREEN, 1);
		final Command finishCommand = new Command(
				"\u067E\u0627\u064A\u0627\u0646", Command.OK, 1);
		convDateAlert.addCommand(againCommand);
		convDateAlert.addCommand(finishCommand);
		convDateAlert.setCommandListener(new CommandListener() {
			public void commandAction(Command c, Displayable d) {
				if (c == againCommand)
					display.setCurrent(dateConvList);
				else if (c == finishCommand)
					display.setCurrent(mainAlert);
			}
		});
		convDateAlert.setTimeout(Alert.FOREVER);
		
		// Make the warning alert displayed when entering
		// invalid dates
		convErrorAlert = new Alert("\u062E\u0637\u0627"/*khata*/);
		convErrorAlert.setType(AlertType.ERROR);
		
	}

	private final void convertDate() {
		// TODO try/catch convertion method calls
		final int YEAR = 0, MONTH = 1, DAY = 2, ERROR = 3;
		
		int year = 1, month = 1, day = 1;
		
		// validate year
		try {
			year = Integer.parseInt(((TextField) dateConvForm.get(0))
					.getString());
		} catch(NumberFormatException e) {
			showConvErrorAlert(YEAR);
			return;
		}
		
		// validate month
		try {
			month = Integer.parseInt(((TextField) dateConvForm.get(1))
					.getString());			
		} catch (NumberFormatException e) {
			showConvErrorAlert(MONTH);
			return;
		}
		
		// validate day
		try {
			day = Integer.parseInt(((TextField) dateConvForm.get(2))
					.getString());			
		} catch (Exception e) {
			showConvErrorAlert(DAY);
			return;
		}

		CivilDate civil = null;
		IslamicDate islamic = null;
		PersianDate persian = null;
		String alertString = null;

		switch (dateConvList.getSelectedIndex()) {
		case PERSIAN:
			try {
				persian = new PersianDate(year, month, day);				
			} catch (MonthOutOfRangeException e) {
				showConvErrorAlert(MONTH);
				return;
			} catch (DayOutOfRangeException e) {
				showConvErrorAlert(DAY);
				return;
			}
			
			try {
				civil = DateConverter.persianToCivil(persian);
				islamic = DateConverter.persianToIslamic(persian);				
			} catch (Exception e) {
				showConvErrorAlert(ERROR);
			}
			
			alertString = "\u062A\u0627\u0631\u064A\u062E\u0020\u0634\u0645\u0633\u064A:\n"
					+ persian
					+ "\n"
					+ "\u0628\u0631\u0627\u0628\u0631\u0020\u0642\u0645\u0631\u064A:\n"
					+ islamic
					+ "\n"
					+ "\u0628\u0631\u0627\u0628\u0631\u0020\u0645\u064A\u0644\u0627\u062F\u064A:\n"
					+ civil;
			break;
		case ISLAMIC:
			try {
				islamic = new IslamicDate(year, month, day);
			} catch (MonthOutOfRangeException e) {
				showConvErrorAlert(MONTH);
				return;
			} catch (DayOutOfRangeException e) {
				showConvErrorAlert(DAY);
				return;
			}

			try {
				persian = DateConverter.islamicToPersian(islamic);
				civil = DateConverter.islamicToCivil(islamic);				
			} catch (Exception e) {
				showConvErrorAlert(ERROR);
			}
			
			alertString = "\u062A\u0627\u0631\u064A\u062E\u0020\u0645\u064A\u0644\u0627\u062F\u064A:\n"
					+ islamic
					+ "\n"
					+ "\u0628\u0631\u0627\u0628\u0631\u0020\u0634\u0645\u0633\u064A:\n"
					+ persian
					+ "\n"
					+ "\u0628\u0631\u0627\u0628\u0631\u0020\u0645\u064A\u0644\u0627\u062F\u064A:\n"
					+ civil;
			break;
		case CIVIL:
			try {
				civil = new CivilDate(year, month, day);
			} catch (MonthOutOfRangeException e) {
				showConvErrorAlert(MONTH);
				return;
			} catch (DayOutOfRangeException e) {
				showConvErrorAlert(DAY);
				return;
			}

			try {
				persian = DateConverter.civilToPersian(civil);
				islamic = DateConverter.civilToIslamic(civil);				
			} catch (Exception e) {
				showConvErrorAlert(ERROR);
			}
			
			alertString = "\u062A\u0627\u0631\u064A\u062E\u0020\u0645\u064A\u0644\u0627\u062F\u064A:\n"
					+ civil
					+ "\n"
					+ "\u0628\u0631\u0627\u0628\u0631\u0020\u0634\u0645\u0633\u064A:\n"
					+ persian
					+ "\n"
					+ "\u0628\u0631\u0627\u0628\u0631\u0020\u0642\u0645\u0631\u064A:\n"
					+ islamic;
			break;
		}
		
		alertString += "\n\u0631\u0648\u0632\u0020\u0647\u0641\u062A\u0647:\n"
				+ civil.getDayOfWeekName();
		convDateAlert.setString(alertString);
		display.setCurrent(convDateAlert);
	}
	
	private final void showConvErrorAlert(int type) {
		if( type < 3) {
			// Invalid year, month or day
			// = "vared shode mo'taber nist"
			String strInvalid = "\u0648\u0627\u0631\u062F\u0020\u0634\u062F\u0647\u0020\u0645\u0639\u062A\u0628\u0631\u0020\u0646\u064A\u0633\u062A\u0021";
			// = "saal", "maah", "rooz"
			String[] YMD = {"\u0633\u0627\u0644 ",
					"\u0645\u0627\u0647 ", "\u0631\u0648\u0632 "};
			convErrorAlert.setString(YMD[type] + strInvalid);
			display.setCurrent(convErrorAlert);
			display.setCurrentItem(dateConvForm.get(type));
		} else { // exception in conversion
			convErrorAlert.setString("\u062E\u0637\u0627\u0020\u062F\u0631\u0020\u062A\u0628\u062F\u064A\u0644\u0021");
			display.setCurrent(convErrorAlert, dateConvForm);
		}
	}

	private final void customizeDateConvForm(int selectedIndex) {
		switch (selectedIndex) {
		case PERSIAN:
			dateConvForm
					.setTitle("\u062A\u0627\u0631\u064A\u062E\u0020\u0634\u0645\u0633\u064A");
			break;
		case ISLAMIC:
			dateConvForm
					.setTitle("\u062A\u0627\u0631\u064A\u062E\u0020\u0642\u0645\u0631\u064A");
			break;
		case CIVIL:
			dateConvForm
					.setTitle("\u062A\u0627\u0631\u064A\u062E\u0020\u0645\u064A\u0644\u0627\u062F\u064A");
			break;
		}
	}

}
