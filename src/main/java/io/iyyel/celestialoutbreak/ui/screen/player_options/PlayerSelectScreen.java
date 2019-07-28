package io.iyyel.celestialoutbreak.ui.screen.player_options;

import io.iyyel.celestialoutbreak.controller.GameController;
import io.iyyel.celestialoutbreak.dal.dao.contract.IPlayerDAO;
import io.iyyel.celestialoutbreak.ui.screen.AbstractNavigationScreen;
import io.iyyel.celestialoutbreak.ui.screen.component.Button;

import java.awt.*;

public final class PlayerSelectScreen extends AbstractNavigationScreen {

    private Button[] buttons;
    private Color[] playerNameColors;

    private int playerAmount = 0;

    public PlayerSelectScreen(NavStyle navStyle, int btnAmount,
                              int btnWrapAmount, GameController gameController) {
        super(navStyle, btnAmount, btnWrapAmount, gameController);
    }

    @Override
    protected void updateNavUse(int index) {
        if (isButtonUsed(index)) {
            String selectedPlayer = playerDAO.getPlayerList().get(index);

            try {
                if (!selectedPlayer.equalsIgnoreCase(playerDAO.getSelectedPlayer())) {
                    menuUseClip.play(false);
                    playerDAO.selectPlayer(selectedPlayer);
                    try {
                        playerDAO.savePlayerDTO();
                    } catch (IPlayerDAO.PlayerDAOException e) {
                        e.printStackTrace();
                    }
                } else {
                    menuBadActionClip.play(false);
                }
            } catch (IPlayerDAO.PlayerDAOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    protected void updateNavOK(int index) {

    }

    @Override
    public void updateNavCancel(GameController.State state) {
        super.updateNavCancel(state);
    }

    @Override
    public void update() {
        super.update();
        updateNavUp();
        updateNavDown();
        updateNavLeft();
        updateNavRight();
        updateNavOK(selectedIndex);
        updateNavCancel(gameController.getPrevState());
        updateNavUse(selectedIndex);
        updatePlayerColors(selectedIndex);
        updateSelectedButtonColor(buttons);
    }

    @Override
    public void render(Graphics2D g) {
        /*
         * Do this ONCE every time the user is on this screen.
         */
        if (isFirstRender) {
            isFirstRender = false;
            updatePlayerData();
        }

        drawScreenTitles(textHandler.TITLE_SELECT_PLAYER_SCREEN, g);
        renderButtons(buttons, g);
        drawScreenToolTip("Press '" + textHandler.BTN_CONTROL_USE + "' to select a player.", g);
        drawScreenInfoPanel(g);
    }

    private void updatePlayerData() {
        try {
            playerDAO.savePlayerDTO();
            playerDAO.loadPlayerDTO();
        } catch (IPlayerDAO.PlayerDAOException e) {
            e.printStackTrace();
        }

        playerAmount = playerDAO.getPlayerList().size();

        // Update rectangles
        buttons = new Button[playerAmount];
        playerNameColors = new Color[playerAmount];

        int initialX = 240;
        int initialY = 230;
        int x = initialX;
        int y = initialY;
        int xInc = 200;
        int yInc = 80;

        for (int i = 0; i < playerAmount; i++) {
            if (i % 5 == 0 && i != 0) {
                x += xInc;
                y = initialY;
            }

            String player = playerDAO.getPlayerList().get(i);
            buttons[i] = new Button(new Point(x, y), new Dimension(150, 50),
                    player, false, inputBtnFont, screenFontColor, menuBtnColor,
                    new Point(75, 0), new Point(70, -5), gameController);

            y += yInc;
        }
    }

    private void updatePlayerColors(int index) {
        try {
            String player = playerDAO.getPlayerList().get(index);
            String selectedPlayer = playerDAO.getSelectedPlayer();

            if (player.equals(selectedPlayer)) {
                playerNameColors[index] = menuBtnPlayerSelectedColor;
                buttons[index].setTextColor(playerNameColors[index]);
            } else {
                playerNameColors[index] = menuBtnColor;
                buttons[index].setTextColor(playerNameColors[index]);
            }
        } catch (IPlayerDAO.PlayerDAOException e) {
            e.printStackTrace();
        }
    }

}