package io.iyyel.celestialoutbreak.menu.main_menu;

import io.iyyel.celestialoutbreak.controller.GameController;
import io.iyyel.celestialoutbreak.menu.AbstractMenu;

import java.awt.*;

public final class ExitMenu extends AbstractMenu {

    public ExitMenu(GameController gameController) {
        super(gameController);
    }

    @Override
    public void update() {
        decInputTimer();

        if (inputHandler.isOKPressed() && isInputAvailable()) {
            resetInputTimer();
            gameController.stop();
        }

        if (inputHandler.isCancelPressed() && isInputAvailable()) {
            resetInputTimer();
            menuUseClip.play(false);
            if (gameController.getPrevState() != GameController.State.NONE &&
                    gameController.getPrevState() == GameController.State.WELCOME_MENU) {
                gameController.switchState(GameController.State.WELCOME_MENU);
            } else {
                gameController.switchState(GameController.State.MAIN_MENU);
            }
        }

    }

    @Override
    public void render(Graphics2D g) {
        g.setColor(menuFontColor);
        drawMenuTitle(g);
        drawSubmenuTitle("Exit", g);
        drawCenterString("Exit " + textHandler.GAME_TITLE + "?", gameController.getHeight() / 2, g, msgFont);
        drawInformationPanel(g);
    }

}