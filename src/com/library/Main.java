package com.library;

import com.library.ui.MainFrame;

public class Main {
    public static void main(String[] args) {
        // Initialize database connection
        try {
            // Test database connection
            Class.forName("com.mysql.cj.jdbc.Driver");

            // Launch the main application window
            javax.swing.SwingUtilities.invokeLater(new Runnable() {
                @Override
                public void run() {
                    MainFrame mainFrame = new MainFrame();
                    mainFrame.setVisible(true);
                }
            });

        } catch (ClassNotFoundException e) {
            System.out.println("MySQL JDBC Driver not found!");
            e.printStackTrace();
        }
    }
}