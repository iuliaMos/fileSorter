package com.filesorter;

import javax.swing.*;
import java.awt.*;

public class Main extends JFrame {

    public Main() {
        super("File Chooser");
        setLayout(new FlowLayout());
        setSize(400, 200);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JButton button = new JButton("Choose file and sort");
        JPanel p = new JPanel();
        p.add(button);

        FileChooser fc = new FileChooser();
        button.addActionListener(fc);
        add(p);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new Main().setVisible(true));
    }
}
