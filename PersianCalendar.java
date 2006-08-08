import javax.microedition.lcdui.*;
import javax.microedition.midlet.*;
import java.util.*;

public class PersianCalendar extends MIDlet implements CommandListener {

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

		aboutCommand = new Command("\u062F\u0631\u0628\u0627\u0631\u0647",
				Command.HELP, 1);
		mainAlert.addCommand(aboutCommand);

		aboutAlert = new Alert("\u062F\u0631\u0628\u0627\u0631\u0647");
		aboutAlert.setCommandListener(this);
		aboutAlert.setType(AlertType.INFO);
		aboutAlert.setTimeout(Alert.FOREVER);
		aboutAlert.setString(getAboutString());

		backCommand = new Command("\u0628\u0627\u0632\u06AF\u0634\u062A",
				Command.BACK, 0);
		aboutAlert.addCommand(backCommand);

		display.setCurrent(mainAlert);
	}

	protected void startApp() throws MIDletStateChangeException {
		// Today
		CivilDate civil = new CivilDate();
		PersianDate persian = DateConverter.civilToPersian(civil);
		IslamicDate islamic = DateConverter.civilToIslamic(civil);

		String today = " " + civil.getDayOfWeekName() + "\n "
				+ persian.getDayOfMonth() + " " + persian.getMonthName() + " "
				+ persian.getYear() + "\n " + islamic.getDayOfMonth() + " "
				+ islamic.getMonthName() + " " + islamic.getYear() + "\n "
				+ civil.getDayOfMonth() + " " + civil.getMonthName() + " " + civil.getYear();

		mainAlert.setString(today);

	}

	protected void pauseApp() {
	}

	public void destroyApp(boolean unconditional) {
	}

	public void commandAction(Command command, Displayable d) {
		if (d == mainAlert) {
			if (command == exitCommand)
				notifyDestroyed();

			if (command == aboutCommand)
				display.setCurrent(aboutAlert);
		}

		if (d == aboutAlert) {
			if (command == backCommand)
				display.setCurrent(mainAlert);
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

}
