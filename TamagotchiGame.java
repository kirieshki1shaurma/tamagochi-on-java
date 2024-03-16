import javax.microedition.midlet.*;
import javax.microedition.lcdui.*;
import java.io.*;

public class TamagotchiGame extends MIDlet implements CommandListener {
    private Display display;
    private Form mainForm;
    private Command exitCommand;
    private Command feedCommand;
    private Command playCommand;
    private Command saveCommand;
    private Command loadCommand;
    private String characterName = "Tamagotchi";
    private int hunger = 50;
    private int happiness = 50;

    public TamagotchiGame() {
        display = Display.getDisplay(this);
        mainForm = new Form("Tamagotchi");
        exitCommand = new Command("Exit", Command.EXIT, 1);
        feedCommand = new Command("Feed", Command.SCREEN, 2);
        playCommand = new Command("Play", Command.SCREEN, 3);
        saveCommand = new Command("Save", Command.SCREEN, 4);
        loadCommand = new Command("Load", Command.SCREEN, 5);
        mainForm.addCommand(exitCommand);
        mainForm.addCommand(feedCommand);
        mainForm.addCommand(playCommand);
        mainForm.addCommand(saveCommand);
        mainForm.addCommand(loadCommand);
        mainForm.setCommandListener(this);
        display.setCurrent(mainForm);
    }

    public void startApp() {
    }

    public void pauseApp() {
    }

    public void destroyApp(boolean unconditional) {
    }

    public void commandAction(Command c, Displayable d) {
        if (c == exitCommand) {
            destroyApp(false);
            notifyDestroyed();
        } else if (c == feedCommand) {
            hunger += 10;
            happiness -= 5;
            updateStatus();
        } else if (c == playCommand) {
            hunger -= 5;
            happiness += 10;
            updateStatus();
        } else if (c == saveCommand) {
            saveState();
        } else if (c == loadCommand) {
            loadState();
        }
    }

    private void updateStatus() {
        mainForm.append(characterName + " status:\n");
        mainForm.append("Hunger: " + hunger + "\n");
        mainForm.append("Happiness: " + happiness + "\n");
    }

    private void saveState() {
        try {
            FileConnection fc = (FileConnection) Connector.open("file:///Tamagotchi.txt");
            if (!fc.exists()) {
                fc.create();
            }
            OutputStream os = fc.openOutputStream();
            os.write((characterName + "," + hunger + "," + happiness).getBytes());
            os.close();
            fc.close();
            mainForm.append("Saved successfully!\n");
        } catch (IOException e) {
            mainForm.append("Error saving state: " + e.getMessage() + "\n");
        }
    }

    private void loadState() {
        try {
            FileConnection fc = (FileConnection) Connector.open("file:///Tamagotchi.txt");
            if (fc.exists()) {
                InputStream is = fc.openInputStream();
                byte[] data = new byte[(int) fc.fileSize()];
                is.read(data);
                String[] values = new String(data).split(",");
                characterName = values[0];
                hunger = Integer.parseInt(values[1]);
                happiness = Integer.parseInt(values[2]);
                is.close();
                mainForm.append("Loaded successfully!\n");
                updateStatus();
            } else {
                mainForm.append("No saved state found.\n");
            }
            fc.close();
        } catch (IOException e) {
            mainForm.append("Error loading state: " + e.getMessage() + "\n");
        }
    }
}

