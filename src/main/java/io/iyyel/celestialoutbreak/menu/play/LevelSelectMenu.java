package io.iyyel.celestialoutbreak.menu.play;

import io.iyyel.celestialoutbreak.controller.GameController;
import io.iyyel.celestialoutbreak.handler.LevelHandler;
import io.iyyel.celestialoutbreak.menu.AbstractMenu;

import java.awt.*;

public class LevelSelectMenu extends AbstractMenu {

    private final LevelHandler levelHandler = LevelHandler.getInstance();

    private Rectangle[] levelRects;
    private Color[] levelRectColors;
    private Color[] levelNameColors;

    private Font levelInfoFont = utils.getGameFont().deriveFont(15F);

    private int levelAmount = levelHandler.getLevelAmount();
    private int selected = 0;

    public LevelSelectMenu(GameController gameController) {
        super(gameController);
        levelRects = new Rectangle[levelAmount];
        levelRectColors = new Color[levelAmount];
        levelNameColors = new Color[levelAmount];

        int initialX = 130;
        int initialY = 240;
        int x = initialX;
        int y = initialY;
        int xInc = 260;
        int yInc = 100;

        for (int i = 0; i < levelAmount; i++) {
            levelRectColors[i] = menuBtnColor;
            levelNameColors[i] = levelHandler.getLevel(i).getColor();

            if (i % 4 == 0 && i != 0) {
                x += xInc;
                y = initialY;
            }
            levelRects[i] = new Rectangle(x, y, 240, 80);
            y += yInc;
        }
    }

    @Override
    public void update() {
        decInputTimer();

        if (inputHandler.isCancelPressed() && isInputAvailable()) {
            resetInputTimer();
            menuUseClip.play(false);
            gameController.switchState(GameController.State.MAIN_MENU);
        }

        if (inputHandler.isDownPressed() && (selected + 1) % 4 != 0 && (selected + 1) < levelAmount && isInputAvailable()) {
            resetInputTimer();
            selected++;
            menuNavClip.play(false);
        }

        if (inputHandler.isUpPressed() && selected % 4 != 0 && isInputAvailable()) {
            resetInputTimer();
            selected--;
            menuNavClip.play(false);
        }

        if (inputHandler.isLeftPressed() && selected > 3 && isInputAvailable()) {
            resetInputTimer();
            selected -= 4;
            menuNavClip.play(false);
        }

        if (inputHandler.isRightPressed() && selected < 16 && (selected + 4) < levelAmount && isInputAvailable()) {
            resetInputTimer();
            selected += 4;
            menuNavClip.play(false);
        }

        for (int i = 0, n = levelHandler.getLevelAmount(); i < n; i++) {
            if (selected == i) {
                updateLevelColors(i);

                if (inputHandler.isUsePressed() && isInputAvailable()) {
                    resetInputTimer();
                    levelHandler.setActiveLevelIndex(i);
                    gameController.switchState(GameController.State.PLAY_SCREEN);
                }

            } else {
                updateLevelColors(i);
            }
        }

    }

    @Override
    public void render(Graphics2D g) {
        /* Render game title */
        drawMenuTitle(g);

        /* Show sub menu */
        drawSubmenuTitle("SELECT LEVEL", g);

        /* Render buttons  */

        for (int i = 0; i < levelHandler.getLevelAmount(); i++) {
            g.setFont(inputBtnFont);
            g.setColor(levelNameColors[i]);
            g.drawString(levelHandler.getLevel(i).getName(), levelRects[i].x + 5, levelRects[i].y + 32);

            g.setFont(levelInfoFont);
            g.setColor(menuBtnColor);
            g.drawString("Blocks: " + levelHandler.getLevel(i).getBlockAmount(), levelRects[i].x + 5, levelRects[i].y + 55);
            g.drawString("Score: 1234", levelRects[i].x + 5, levelRects[i].y + 75);

            g.setColor(levelRectColors[i]);
            g.draw(levelRects[i]);
        }

        drawMenuToolTip("Press '" + textHandler.BTN_CONTROL_USE + "' to play a level.", g);
        drawInfoPanel(g);
    }

    private void updateLevelColors(int index) {
        if (selected == index) {
            levelRectColors[index] = menuSelectedBtnColor;
        } else {
            levelRectColors[index] = menuBtnColor;
        }
    }

}