package org.ass03.part2A.controller;

import org.ass03.part2A.model.Grid;

import java.awt.*;

public interface GridUpdateListener {
    void onGridCreated();
    void onGridUpdated(int gridIndex);
    void onCellSelected(int gridId, int row, int col, Color color, String idUser);
}