package org.example;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.Vector;

public class POSWindow extends Frame implements WindowListener {
    //Label list for corresponding item list, price list and double total in camera class
    private Label[] itemLabels;
    private Label[] priceLabels;
    private Label total;

    //separate variables to create camera instance and store
    private Vector<String> itemList;
    private Vector<Double> priceList;
    private Double Dtotal = 0.0;
    //removedPrice for removebutton (mainly used to consistently update total after removing an item)
    private Double removedPrice = 0.0;

    //camera instance
    private Camera cameraInstance;
    //Throws used for UIManager
    public POSWindow(String frameTitle) throws UnsupportedLookAndFeelException, ClassNotFoundException, InstantiationException, IllegalAccessException {
        super(frameTitle);
        setSize(800, 600);
        setLocation(100, 100);
        addWindowListener(this);

        // Components
        itemLabels = new Label[10]; // Initialize item labels
        priceLabels = new Label[10]; // Initialize price labels
        total = new Label("Total: $0.00");
        Button totalBtn = new Button("Pay Now");
        Button rmvBtn = new Button("Remove Item");

        //Updating showDialogMessage windows to match with system UI
        UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());

        // Set layout
        GridBagLayout gBL = new GridBagLayout();
        setLayout(gBL);
        GridBagConstraints gBCon = new GridBagConstraints();

        // Add the instructions label at the top
        Label instructions = new Label("Please Scan Your Items");
        instructions.setFont(new Font("Calibri", Font.BOLD, 25));
        gBCon.weighty = 0.01;  // Small vertical space
        gBCon.weightx = 1;
        gBCon.fill = GridBagConstraints.BOTH;
        gBCon.gridwidth = 3;   // Span across 3 columns
        gBCon.gridy = 0;
        gBCon.gridx = 0;
        gBL.setConstraints(instructions, gBCon);
        add(instructions);

        // Add the filler labels in the first column
        gBCon.weighty = 0.01;   // Take most of the vertical space
        gBCon.weightx = 0.1;   // Filler takes full horizontal space
        gBCon.gridwidth = 1;    // Span 1 column only
        gBCon.gridheight = 1; // Span 11 rows
        gBCon.gridy = 1;        // Start from row 1
        gBCon.gridx = 0;        // First column

        //Initialize every label in itemLables array as blank (will be updated later)
        for (int i = 0; i < itemLabels.length; i++) {
            itemLabels[i] = new Label("");
            gBL.setConstraints(itemLabels[i], gBCon);
            // Cosmetics
            itemLabels[i].setFont(new Font("Calibri", Font.ITALIC, 15));
            itemLabels[i].setBackground(Color.LIGHT_GRAY);
            add(itemLabels[i]);
            gBCon.gridy++; // Move to the next row for each item
        }

        // Reset GridBagConstraints for price labels
        gBCon.weightx = 0.01;    // Filler takes full horizontal space
        gBCon.gridwidth = 1;    // Span 1 column only
        gBCon.gridheight = 1;  // Span 11 rows
        gBCon.gridy = 1;        // Start from row 1
        gBCon.gridx = 1;        // Second column

        // Initialize every label in priceLabels array as blank (will be updated later)
        for (int i = 0; i < priceLabels.length; i++) {
            priceLabels[i] = new Label("");
            gBL.setConstraints(priceLabels[i], gBCon);
            // Cosmetics
            priceLabels[i].setBackground(Color.LIGHT_GRAY);
            priceLabels[i].setFont(new Font("Calibri", Font.PLAIN, 15));
            add(priceLabels[i]);
            gBCon.gridy++; // Move to the next row for each price
        }

        // Add total label
        gBCon.weighty = 1;
        gBCon.weightx = 1;     // Filler takes full horizontal space
        gBCon.gridwidth = 2;   // Span across two columns
        gBCon.gridy = 11;      // Bottom row
        gBCon.gridx = 0;       // First column
        gBL.setConstraints(total, gBCon);
        total.setFont(new Font("Calibri", Font.BOLD,25));
        add(total);

        // Add the "Pay Now" button in the right column
        gBCon.weightx = 0.3;
        gBCon.weighty = 1;
        gBCon.gridwidth = 1;   // Span 1 column
        gBCon.gridheight = 5;  // Span 5 rows
        gBCon.gridy = 6;       // Same row as filler
        gBCon.gridx = 2;       // Third column
        gBL.setConstraints(totalBtn, gBCon);
        // Cosmetics
        totalBtn.setBackground(Color.DARK_GRAY);
        totalBtn.setForeground(Color.WHITE);
        totalBtn.setFont(new Font("Calibri", Font.BOLD, 25));
        add(totalBtn);
        //Shows total as a separate window (showMessageDialog)
        totalBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent E){

                JOptionPane.showMessageDialog(null, String.format("Your Total is: $%.2f",Dtotal-removedPrice));

            }
        });

        // Add the "Remove Item" button below the "Pay Now" button
        gBCon.gridy = 1;       // Next row
        gBCon.gridx = 2;       // Third column
        gBL.setConstraints(rmvBtn, gBCon);
        // Cosmetics
        rmvBtn.setBackground(Color.DARK_GRAY);
        rmvBtn.setForeground(Color.WHITE);
        rmvBtn.setFont(new Font("Calibri", Font.BOLD, 25));
        add(rmvBtn);

        //
        rmvBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!itemList.isEmpty() && !priceList.isEmpty()) {

                    int lastIndex = itemList.size() - 1;

                    // Remove the last item and price
                    String removedItem = itemList.remove(lastIndex);
                    removedPrice += priceList.remove(lastIndex);

                    // Subtract the removed price from the total
                    Dtotal -= removedPrice;
                    total.setText(String.format("Total: $%.2f", Dtotal));

                    // Update the labels on the UI
                    itemLabels[lastIndex].setText("");  // Clear the last item label
                    priceLabels[lastIndex].setText(""); // Clear the last price label

                    // Show confirmation dialog
                    JOptionPane.showMessageDialog(null, "Removed: " + removedItem);
                } else {
                    JOptionPane.showMessageDialog(null, "No items to remove.");
                }
            }
        });


        // Initialize the Camera instance and start detecting
        cameraInstance = new Camera();

        // Start updating the itemList in a separate thread
        new Thread(() -> updateListAndUI()).start();
    }

    //
    private void updateListAndUI() {
        while (true) {
            // Fetch the items from the Camera instance
            itemList = Camera.items;
            priceList = Camera.prices;
            Dtotal = Camera.total;

            if (!itemList.isEmpty()) { // Update only if the itemList is not empty
                for (int i = 0; i < Math.min(itemLabels.length, itemList.size()); i++) {
                    final int index = i;
                    SwingUtilities.invokeLater(() -> {
                        //dynamically updates items and prices
                        itemLabels[index].setText(itemList.get(index));
                        priceLabels[index].setText(String.format("%.2f", priceList.get(index)));
                    });
                }
                // Update total label
                SwingUtilities.invokeLater(() -> total.setText(String.format("Total: %.2f", Dtotal-removedPrice)));
            }

            // Simulate a short delay for checking the itemList continuously
            try {
                Thread.sleep(1000); // Check every second for updates
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void windowOpened(WindowEvent e) {}

    @Override
    public void windowClosing(WindowEvent e) {
        System.exit(0);
    }

    @Override
    public void windowClosed(WindowEvent e) {}

    @Override
    public void windowIconified(WindowEvent e) {}

    @Override
    public void windowDeiconified(WindowEvent e) {}

    @Override
    public void windowActivated(WindowEvent e) {}

    @Override
    public void windowDeactivated(WindowEvent e) {}

    // Main method to run the application
    public static void main(String[] args) throws UnsupportedLookAndFeelException, ClassNotFoundException, InstantiationException, IllegalAccessException {
        POSWindow frame = new POSWindow("POS System");
        frame.setVisible(true);
    }
}


