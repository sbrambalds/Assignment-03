package org.ass03.part2A.controller;

import org.ass03.part2A.model.User;
import org.ass03.part2A.view.GameDetailsView;
import org.ass03.part2A.view.GridView;
import org.ass03.part2A.view.StartView;

import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class StartController {

    private final StartView startView;
    private final User user;

    public StartController(StartView startView, User user) {
        this.startView = startView;
        this.user = user;
        this.startView.addJoinGameListener(new OpenGridViewListener());
        this.startView.addNewGameListener(new NewGameListener());
    }

    class OpenGridViewListener implements ActionListener {
        @Override
        public void actionPerformed(java.awt.event.ActionEvent e) {
            startView.setVisible(false);
            GridView gridView = new GridView(user.getId());
            new GridController(user, startView, gridView);
            gridView.setVisible(true);
        }
    }

    class NewGameListener implements ActionListener {
        @Override
        public void actionPerformed(java.awt.event.ActionEvent e) {
            try {
                user.createGrid();
            } catch (IOException | TimeoutException ex) {
                throw new RuntimeException(ex);
            }
            GameDetailsView gameDetailsView = new GameDetailsView(user.getId());
            new GameDetailsController(user, gameDetailsView, startView, user.getAllGrids().size() - 1);
            startView.setVisible(false);
            gameDetailsView.setVisible(true);
        }
    }


}
