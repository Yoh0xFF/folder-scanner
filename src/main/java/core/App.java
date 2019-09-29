package core;

import core.ui.FolderScanner;

import javax.swing.*;
import javax.swing.plaf.nimbus.NimbusLookAndFeel;

public class App {

    public static void main(String[] args) throws Exception {
        UIManager.setLookAndFeel(NimbusLookAndFeel.class.getName());
        new FolderScanner().setVisible(true);
    }
}
