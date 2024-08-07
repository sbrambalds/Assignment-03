package org.ass03.part2B.view;

import org.ass03.part2B.model.Cell;
import org.ass03.part2B.model.User;
import org.ass03.part2B.model.Grid;
import org.ass03.part2B.utils.Utils;

import javax.swing.*;
import javax.swing.border.MatteBorder;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;

public class GameDetailsView extends JFrame {
    private final JPanel gamePanel;
    private final JButton backButton;
    private final JTextField[][] cellTextFields;
    private final JButton submitButton;
    private Color myColor;
    private int gridId;
    private int currentSelectedRow = 0;
    private int currentSelectedCol = 0;

    public GameDetailsView(String title) {
        setTitle("Player-" + title + " - Sudoku Grid Details");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        setLayout(new BorderLayout());

        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        backButton = new JButton("Back");
        topPanel.add(backButton);

        add(topPanel, BorderLayout.NORTH);

        gamePanel = new JPanel();
        gamePanel.setLayout(new GridLayout(9, 9));
        add(gamePanel, BorderLayout.CENTER);

        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        submitButton = new JButton("Submit solution");
        bottomPanel.add(submitButton);

        add(bottomPanel, BorderLayout.SOUTH);

        cellTextFields = new JTextField[9][9]; // Initialize the JTextField array
    }


    public void addBackButtonListener(ActionListener listener) {
        backButton.addActionListener(listener);
    }

    public void addSubmitButtonListener(ActionListener listener) {
        submitButton.addActionListener(listener);
    }

    public void displayGrid(Grid grid, User user){
        this.gridId = grid.getId();
        this.myColor = Utils.getColorByName(user.getColor());
        this.gamePanel.removeAll();

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                try {
                    user.unselectCell(gridId, currentSelectedRow, currentSelectedCol);
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
                dispose();
            }
        });

        Cell[][] cells = grid.getGrid();
        for (int row = 0; row < 9; row++) {
            for (int col = 0; col < 9; col++) {
                final int currentRow = row; // final variables for inner class
                final int currentCol = col; // final variables for inner class
                JTextField cellTextField = new JTextField(cells[currentRow][currentCol].getValue() == 0 ? "" : String.valueOf(cells[currentRow][currentCol].getValue()));
                cellTextField.setHorizontalAlignment(JTextField.CENTER); // Align text to the center
                cellTextField.setPreferredSize(new Dimension(50, 50));

                // Set border for 3x3 grid
                int top = (currentRow % 3 == 0) ? 4 : 1;
                int left = (currentCol % 3 == 0) ? 4 : 1;
                int bottom = (currentRow == 8) ? 4 : 0;
                int right = (currentCol == 8) ? 4 : 0;
                cellTextField.setBorder(new MatteBorder(top, left, bottom, right, Color.BLACK));

                if(cells[currentRow][currentCol].isInitialSet()) {
                    cellTextField.setBackground(Color.LIGHT_GRAY);
                    cellTextField.setEditable(false);
                    cellTextField.setFocusable(false);
                } else if(grid.isCompleted()){
                    cellTextField.setFocusable(false);
                    cellTextField.setEditable(false);
                } else {
                    cellTextField.setEditable(true);

                    cellTextField.addActionListener(e -> {
                        try {
                            updateCellValue(grid, currentRow, currentCol, cellTextField, user);
                        } catch (IOException ex) {
                            throw new RuntimeException(ex);
                        }
                    });

                    cellTextField.addFocusListener(new FocusAdapter() {
                        @Override
                        public void focusGained(FocusEvent e) {
                            try {
                                user.selectCell(grid.getId(), currentRow, currentCol);
                                currentSelectedRow = currentRow;
                                currentSelectedCol = currentCol;
                            } catch (IOException ex) {
                                throw new RuntimeException(ex);
                            }
                        }

                        @Override
                        public void focusLost(FocusEvent e) {
                            try {
                                updateCellValue(grid, currentRow, currentCol, cellTextField, user);
                                user.unselectCell(grid.getId(), currentRow, currentCol);
                            } catch (IOException ex) {
                                throw new RuntimeException(ex);
                            }
                        }
                    });
                }
                gamePanel.add(cellTextField);
                cellTextFields[row][col] = cellTextField;
            }
        }
        gamePanel.repaint();
        gamePanel.revalidate();
    }

    private void updateCellValue(Grid grid, int row, int col, JTextField cellTextField, User user) throws IOException {
        try {
            if(cellTextField.getText().isEmpty()){
                user.updateGrid(grid.getId(), row, col, Integer.parseInt(cellTextField.getText()));
                return;
            }
            int newValue = Integer.parseInt(cellTextField.getText());
            if (newValue >= 1 && newValue <= 9) {
                user.updateGrid(grid.getId(), row, col, Integer.parseInt(cellTextField.getText()));
            } else {
                JOptionPane.showMessageDialog(this, "Please enter a number between 1 and 9.");
                cellTextField.setText(""); // Clear the text field if input is invalid
            }
        } catch (NumberFormatException e) {
            if (!cellTextField.getText().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Invalid input. Please enter a number between 1 and 9.");
                cellTextField.setText(""); // Clear the text field if input is invalid
            }
        }
        gamePanel.revalidate();
        gamePanel.repaint();
    }

    public void updateGrid(Grid grid) {
        Cell[][] cells = grid.getGrid();
        for (int row = 0; row < 9; row++) {
            for (int col = 0; col < 9; col++) {
                JTextField cellTextField = cellTextFields[row][col];
                if (cellTextField != null) {
                    if(grid.isCompleted()){
                        cellTextField.setFocusable(false);
                        cellTextField.setEditable(false);
                    }
                    int cellValue = cells[row][col].getValue();
                    cellTextField.setText(cellValue == 0 ? "" : String.valueOf(cellValue));
                }
            }
        }
        gamePanel.repaint();
        gamePanel.revalidate();
    }

    public void colorCell(int gridId, int row, int col, Color color){
        if(this.gridId == gridId){
            cellTextFields[row][col].setBackground(color);
            cellTextFields[row][col].setEditable(myColor.equals(color));
        }
    }

    public void uncoloredCell(int row, int col){
        cellTextFields[row][col].setBackground(Color.WHITE);
        cellTextFields[row][col].setEditable(true);
    }

    public void displayMessage(String s) {
        JOptionPane.showMessageDialog(this, s);
    }
}
