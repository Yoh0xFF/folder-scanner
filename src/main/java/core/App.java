package core;

import javax.swing.UIManager;
import javax.swing.plaf.nimbus.NimbusLookAndFeel;

import core.ui.FolderScanner;

public class App {
	
	public static void main(String[] args) {
		try {
            UIManager.setLookAndFeel(NimbusLookAndFeel.class.getName());
        } catch (Exception e) {
            throw new AssertionError(e);
        }
		
		FolderScanner scanner = new FolderScanner();
		scanner.setVisible(true);
	}
}
