package core.ui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JPanel;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class FolderScanner extends JFrame {

    private static final Logger logger = Logger.getLogger(FolderScanner.class.getName());
    private static final int WIDTH = 500;
    private static final int HEIGHT = 200;

    public FolderScanner() {
        // set general options
        Toolkit tk = Toolkit.getDefaultToolkit();
        Dimension screenSize = tk.getScreenSize();

        int x = (screenSize.width - WIDTH) / 2, y = (screenSize.height - HEIGHT) / 2;

        this.setSize(WIDTH, HEIGHT);
        this.setLocation(x, y);

        this.setResizable(false);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // create panel
        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new GridLayout(3, 3));

        for (int i = 0; i < 3; ++i) {
            for (int j = 0; j < 3; ++j) {
                JPanel panel = new JPanel();
                panel.setLayout(new BorderLayout());

                if (i == 1 && j == 1) {
                    JButton selectButton = new JButton("Choose directory");

                    selectButton.addActionListener(e -> {
                        JFileChooser fileChooser = new JFileChooser(System.getProperty("user.home"));
                        fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);

                        if (fileChooser.showOpenDialog(this) != JFileChooser.APPROVE_OPTION) {
                            return;
                        }

                        File selectedDirectory = fileChooser.getSelectedFile();
                        List<String> nameList = new ArrayList<>();
                        for (File subFile : selectedDirectory.listFiles()) {
                            String name = subFile.getName();

                            if (name.lastIndexOf('.') > 0) {
                                name = name.substring(0, subFile.getName().lastIndexOf('.'));
                            }

                            nameList.add(name);
                        }
                        Collections.sort(nameList);
                        createExcelFile(nameList);

                        this.dispatchEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING));
                    });

                    panel.add(selectButton);
                }

                inputPanel.add(panel);
            }
        }

        this.add(inputPanel);
    }

    private void createExcelFile(List<String> nameList) {
        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet();
            sheet.setColumnWidth(0, 15000);

            int rowCount = -1;
            for (String name : nameList) {
                ++rowCount;
                Row row = sheet.createRow(rowCount);

                Cell cell = row.createCell(0);
                cell.setCellValue(name);
            }

            File excelFile = new File(System.getProperty("user.home") + "/excel.xlsx");
            try (FileOutputStream os = new FileOutputStream(excelFile)) {
                workbook.write(os);
            }
        } catch (IOException ex) {
            logger.log(Level.SEVERE, "createExcelFile", ex);
        }
    }
}