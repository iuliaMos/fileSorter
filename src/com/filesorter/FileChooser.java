package com.filesorter;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.filechooser.FileSystemView;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class FileChooser extends JFrame implements ActionListener {

    public FileChooser() {
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        JFileChooser fileChooser = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());
        fileChooser.setDialogTitle("Select a file for sorting");
        fileChooser.setAcceptAllFileFilterUsed(false);
        FileNameExtensionFilter filter = new FileNameExtensionFilter("TXT", "txt");
        fileChooser.addChoosableFileFilter(filter);

        int returnValue = fileChooser.showSaveDialog(null);
        if (returnValue == JFileChooser.APPROVE_OPTION) {
            String fileName = fileChooser.getSelectedFile().getPath();
            TestSorter ts = new TestSorter(fileName);
            ts.sortFile();
            ts.writeToFile();
        }
    }
}
