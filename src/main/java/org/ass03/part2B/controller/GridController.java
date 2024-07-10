package org.ass03.part2B.controller;

import org.ass03.part2B.model.Grid;
import org.ass03.part2B.model.User;
import org.ass03.part2B.view.GameDetailsView;
import org.ass03.part2B.view.GridView;
import org.ass03.part2B.view.StartView;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.rmi.RemoteException;

public class GridController implements GridUpdateListener {
    private final User user;
    private final GridView gridView;
    private final StartView startView;
    private GameDetailsView detailsView;

    public GridController(User user, StartView startView, GridView gridView) {
        this.user = user;
        this.gridView = gridView;
        this.startView = startView;
        user.addGridUpdateListener(this);
        this.gridView.addBackButtonListener(new BackButtonListener());
        initView();
    }

    private void initView(){
        gridView.displayGrids(user.getAllGrids(), new GridButtonListener());
    }

    @Override
    public void onGridCreated() {
        gridView.displayGrids(user.getAllGrids(), new GridButtonListener());
    }

    @Override
    public void onGridUpdated(int gridIndex) {

    }

    @Override
    public void onCellSelected(int gridId, int row, int col, Color color) {

    }

    @Override
    public void onCellUnselected(int gridId, int row, int col) {

    }

    @Override
    public void onGridCompleted(int gridId, String userId) {
        gridView.displayGrids(user.getAllGrids(), new GridButtonListener());
    }

    class GridButtonListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            int gridIndex = Integer.parseInt(e.getActionCommand().split(" ")[1]) - 1;
            detailsView = new GameDetailsView(user.getId());
            new GameDetailsController(user, detailsView, startView, gridIndex);
            detailsView.setVisible(true);
            startView.setVisible(false);
            gridView.setVisible(false);
        }
    }

    class BackButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            gridView.setVisible(false);

            if (detailsView != null) {
                    detailsView.setVisible(false);
            }

            startView.setVisible(true);
        }
    }
}
