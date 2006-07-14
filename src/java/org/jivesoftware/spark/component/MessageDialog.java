/**
 * $Revision: $
 * $Date: $
 *
 * Copyright (C) 2006 Jive Software. All rights reserved.
 *
 * This software is published under the terms of the GNU Lesser Public License (LGPL),
 * a copy of which is included in this distribution.
 */

package org.jivesoftware.spark.component;

import org.jivesoftware.MainWindow;
import org.jivesoftware.resource.SparkRes;
import org.jivesoftware.spark.SparkManager;

import javax.swing.Icon;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Font;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;


/**
 * <code>MessageDialog</code> class is used to easily display the most commonly used dialogs.
 */
public final class MessageDialog {

    private MessageDialog() {
    }

    /**
     * Display a dialog with an exception.
     *
     * @param throwable the throwable object to display.
     */
    public static void showErrorDialog(Throwable throwable) {
        JTextPane textPane;
        final JOptionPane pane;
        final JDialog dlg;

        TitlePanel titlePanel;

        textPane = new JTextPane();
        textPane.setFont(new Font("Dialog", Font.PLAIN, 12));
        textPane.setEditable(false);

        String message = getStackTrace(throwable);
        textPane.setText(message);
        // Create the title panel for this dialog
        titlePanel = new TitlePanel("An error has been detected. Please report to support@jivesoftware.com.", null, SparkRes.getImageIcon(SparkRes.SMALL_DELETE), true);

        // Construct main panel w/ layout.
        final JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());
        mainPanel.add(titlePanel, BorderLayout.NORTH);

        // The user should only be able to close this dialog.
        Object[] options = {"Close"};
        pane = new JOptionPane(new JScrollPane(textPane), JOptionPane.PLAIN_MESSAGE, JOptionPane.OK_CANCEL_OPTION, null, options, options[0]);

        mainPanel.add(pane, BorderLayout.CENTER);

        MainWindow mainWindow = SparkManager.getMainWindow();
        dlg = new JDialog(mainWindow, "Spark Error", false);
        dlg.pack();
        dlg.setSize(600, 400);
        dlg.setContentPane(mainPanel);
        dlg.setLocationRelativeTo(mainWindow);

        PropertyChangeListener changeListener = new PropertyChangeListener() {
            public void propertyChange(PropertyChangeEvent e) {
                String value = (String)pane.getValue();
                if ("Close".equals(value)) {
                    dlg.setVisible(false);
                }
            }
        };

        pane.addPropertyChangeListener(changeListener);

        dlg.setVisible(true);
        dlg.toFront();
        dlg.requestFocus();
    }

    /**
     * Display an alert dialog.
     *
     * @param message the message to display.
     * @param header  the header/title of the dialog.
     */
    public static void showAlert(String message, String header, Icon icon) {
        JTextPane textPane;
        final JOptionPane pane;
        final JDialog dlg;

        TitlePanel titlePanel;

        textPane = new JTextPane();
        textPane.setFont(new Font("Dialog", Font.PLAIN, 12));
        textPane.setEditable(false);
        textPane.setText(message);

        // Create the title panel for this dialog
        titlePanel = new TitlePanel(header, null, icon, true);

        // Construct main panel w/ layout.
        final JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());
        mainPanel.add(titlePanel, BorderLayout.NORTH);

        // The user should only be able to close this dialog.
        Object[] options = {"Close"};
        pane = new JOptionPane(new JScrollPane(textPane), JOptionPane.PLAIN_MESSAGE, JOptionPane.OK_CANCEL_OPTION, null, options, options[0]);

        mainPanel.add(pane, BorderLayout.CENTER);

        MainWindow mainWindow = SparkManager.getMainWindow();
        dlg = new JDialog(mainWindow, "Broadcast Message", false);
        dlg.pack();
        dlg.setSize(300, 300);
        dlg.setContentPane(mainPanel);
        dlg.setLocationRelativeTo(SparkManager.getMainWindow());

        PropertyChangeListener changeListener = new PropertyChangeListener() {
            public void propertyChange(PropertyChangeEvent e) {
                String value = (String)pane.getValue();
                if ("Close".equals(value)) {
                    dlg.setVisible(false);
                }
            }
        };

        pane.addPropertyChangeListener(changeListener);

        dlg.setVisible(true);
        dlg.toFront();
        dlg.requestFocus();
    }

    /**
     * Display a dialog with a specified component.
     *
     * @param title       the title of the dialog.
     * @param description the description to display.
     * @param icon        the icon.
     * @param comp        the component to display.
     * @param parent      the parent of this dialog.
     * @param width       the width of this dialog.
     * @param height      the height of this dialog.
     * @param modal       true if it is modal.
     * @return the <code>JDialog</code> created.
     */
    public static JDialog showComponent(String title, String description, Icon icon, JComponent comp, Component parent, int width, int height, boolean modal) {
        final JOptionPane pane;
        final JDialog dlg;

        TitlePanel titlePanel;

        // Create the title panel for this dialog
        titlePanel = new TitlePanel(title, description, icon, true);

        // Construct main panel w/ layout.
        final JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());
        mainPanel.add(titlePanel, BorderLayout.NORTH);

        // The user should only be able to close this dialog.
        Object[] options = {"Close"};
        pane = new JOptionPane(comp, JOptionPane.PLAIN_MESSAGE, JOptionPane.OK_CANCEL_OPTION, null, options, options[0]);

        mainPanel.add(pane, BorderLayout.CENTER);

        JOptionPane p = new JOptionPane();
        dlg = p.createDialog(parent, title);
        dlg.setModal(modal);

        dlg.pack();
        dlg.setSize(width, height);
        dlg.setResizable(true);
        dlg.setContentPane(mainPanel);
        dlg.setLocationRelativeTo(parent);

        PropertyChangeListener changeListener = new PropertyChangeListener() {
            public void propertyChange(PropertyChangeEvent e) {
                String value = (String)pane.getValue();
                if ("Close".equals(value)) {
                    dlg.setVisible(false);
                }
            }
        };

        pane.addPropertyChangeListener(changeListener);

        dlg.setVisible(true);
        dlg.toFront();
        dlg.requestFocus();
        return dlg;
    }

    /**
     * Returns the String representation of a StackTrace.
     *
     * @param aThrowable the throwable object.
     * @return the string.
     */
    public static String getStackTrace(Throwable aThrowable) {
        final Writer result = new StringWriter();
        final PrintWriter printWriter = new PrintWriter(result);
        aThrowable.printStackTrace(printWriter);
        return result.toString();
    }

    /**
     * Defines a custom format for the stack trace as String.
     *
     * @param heading    the title of the stack trace.
     * @param aThrowable the throwable object.
     * @return the string.
     */
    public static String getCustomStackTrace(String heading, Throwable aThrowable) {
        //add the class name and any message passed to constructor
        final StringBuffer result = new StringBuffer(heading);
        result.append(aThrowable.toString());
        final String lineSeperator = System.getProperty("line.separator");
        result.append(lineSeperator);

        //add each element of the stack trace
        StackTraceElement[] stackTrace = aThrowable.getStackTrace();
        final List traceElements = Arrays.asList(stackTrace);
        final Iterator traceElementsIter = traceElements.iterator();
        while (traceElementsIter.hasNext()) {
            result.append(traceElementsIter.next());
            result.append(lineSeperator);
        }
        return result.toString();
    }


}