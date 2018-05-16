package com.example;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class UIActionsHandler {

    //Start of tool bar actions
    public static class SettingOpenHandler implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            JFrame settingFrame = new JFrame("Settings");
            settingFrame.setSize(500,500);
            settingFrame.setVisible(true);
        }
    }

    public static class AddOpenHandler implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            JFrame settingFrame = new JFrame("Add a New Download");
            settingFrame.setSize(200,200);
            settingFrame.setVisible(true);
        }
    }

    public static class playHandler implements ActionListener { //todo:add action
        @Override
        public void actionPerformed(ActionEvent e) {

        }
    }
    public static class pauseHandler implements ActionListener {//todo:add action
        @Override
        public void actionPerformed(ActionEvent e) {

        }
    }

    public static class removeHandler implements ActionListener {//todo:add action
        @Override
        public void actionPerformed(ActionEvent e) {

        }
    }
    //end of tool bar actions
}
