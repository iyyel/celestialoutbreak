package io.iyyel.celestialoutbreak.ui.screen;

import io.iyyel.celestialoutbreak.controller.GameController;
import io.iyyel.celestialoutbreak.data.dao.PlayerDAO;
import io.iyyel.celestialoutbreak.data.dao.contract.IPlayerDAO;
import io.iyyel.celestialoutbreak.handler.InputHandler;
import io.iyyel.celestialoutbreak.handler.OptionsHandler;
import io.iyyel.celestialoutbreak.handler.SoundHandler;
import io.iyyel.celestialoutbreak.handler.TextHandler;
import io.iyyel.celestialoutbreak.ui.interfaces.IRenderable;
import io.iyyel.celestialoutbreak.ui.interfaces.IUpdatable;
import io.iyyel.celestialoutbreak.ui.screen.component.Button;
import io.iyyel.celestialoutbreak.util.Util;

import java.awt.*;

public abstract class AbstractScreen implements IUpdatable, IRenderable {

    protected final Util util = Util.getInstance();
    protected final OptionsHandler optionsHandler = OptionsHandler.getInstance();
    protected final InputHandler inputHandler = InputHandler.getInstance();
    protected final TextHandler textHandler = TextHandler.getInstance();
    protected final SoundHandler soundHandler = SoundHandler.getInstance();
    protected final IPlayerDAO playerDAO = PlayerDAO.getInstance();

    protected final SoundHandler.SoundClip menuNavClip = soundHandler.getSoundClip(textHandler.SOUND_FILE_NAME_MENU_BTN_NAV);
    protected final SoundHandler.SoundClip menuUseClip = soundHandler.getSoundClip(textHandler.SOUND_FILE_NAME_MENU_BTN_USE);
    protected final SoundHandler.SoundClip menuBadClip = soundHandler.getSoundClip(textHandler.SOUND_FILE_NAME_BAD_ACTION);

    private final Font titleFont = util.getGameFont().deriveFont(52F);
    private final Font subtitleFont = util.getGameFont().deriveFont(36F);
    private final Font tooltipFont = util.getGameFont().deriveFont(18F);
    private final Font infoPanelFont = util.getGameFont().deriveFont(14F);
    protected final Font msgFont = util.getGameFont().deriveFont(26F);
    protected final Font inputBtnFont = util.getGameFont().deriveFont(20F);

    protected Color screenFontColor = optionsHandler.getMenuFontColor();
    protected Color menuBtnColor = optionsHandler.getMenuBtnColor();
    protected Color menuSelectedBtnColor = optionsHandler.getMenuBtnSelectedColor();
    protected Color menuBtnPlayerSelectedColor = optionsHandler.getMenuBtnPlayerSelectedColor();
    protected Color menuBtnPlayerDeletedColor = optionsHandler.getMenuBtnPlayerDeletedColor();

    private final Rectangle versionRect, authorRect;

    private final int INIT_INPUT_TIMER = 12;
    private int inputTimer = INIT_INPUT_TIMER;

    protected final int initialBtnYPos = 230;
    protected final int btnYIncrement = 75;

    protected final GameController gameController;

    protected final int screenWidth;
    protected final int screenHeight;

    private int blockUpdateUpdates = 0;
    private int blockRenderUpdates = 0;

    public AbstractScreen(GameController gameController) {
        this.gameController = gameController;
        this.screenWidth = gameController.getWidth();
        this.screenHeight = gameController.getHeight();
        authorRect = new Rectangle(getHalfWidth() + 3, screenHeight - 22, 70, 18);
        versionRect = new Rectangle(getHalfWidth() - 73, screenHeight - 22, 66, 18);
    }

    @Override
    public void update() {
        decInputTimer();

        if (isUpdateStopped()) {
            blockUpdateUpdates--;
        }

        if (isRenderStopped()) {
            blockRenderUpdates--;
        }
    }

    protected void updateNavOK() {
        if (inputHandler.isOKPressed() && isInputAvailable()) {
            resetInputTimer();
            menuUseClip.play(false);
        }
    }

    protected void updateNavCancel(GameController.State state) {
        if (inputHandler.isCancelPressed() && isInputAvailable()) {
            resetInputTimer();
            menuUseClip.play(false);
            gameController.switchState(state);
        }
    }

    @Override
    public void stopUpdate(int updates) {
        this.blockUpdateUpdates = updates;
    }

    @Override
    public void stopRender(int updates) {
        this.blockRenderUpdates = updates;
    }

    @Override
    public boolean isUpdateStopped() {
        return blockUpdateUpdates != 0;
    }

    @Override
    public boolean isRenderStopped() {
        return blockRenderUpdates != 0;
    }

    protected void drawCenteredText(String text, int y, Font font, Graphics2D g) {
        g.setColor(screenFontColor);
        g.setFont(font);
        FontMetrics metrics = g.getFontMetrics(font);
        int x = (screenWidth - metrics.stringWidth(text)) / 2;
        g.drawString(text, x, y);
    }

    protected void drawCenteredText(String text, int yOffset, Graphics2D g) {
        drawCenteredText(text, getHalfHeight() + yOffset, msgFont, g);
    }

    protected void drawCenteredText(String text, Graphics2D g) {
        drawCenteredText(text, getHalfHeight(), msgFont, g);
    }

    protected void drawInfoPanel(Graphics2D g) {
        g.setColor(screenFontColor);
        g.setFont(infoPanelFont);

        /* Version number */
        g.drawString(textHandler.GAME_VERSION, versionRect.x + 3, versionRect.y + 15);
        g.draw(versionRect);

        /* Email tag */
        g.drawString(textHandler.AUTHOR_WEBSITE, authorRect.x + 2, authorRect.y + 13);
        g.draw(authorRect);
    }

    protected void drawTitle(Graphics2D g) {
        g.setColor(screenFontColor);
        g.setFont(titleFont);
        drawCenteredText(textHandler.GAME_TITLE, 100, titleFont, g);
    }

    protected void drawSubtitle(String subtitle, Graphics2D g) {
        g.setColor(screenFontColor);
        g.setFont(subtitleFont);
        drawCenteredText(subtitle, getHalfHeight() - 170, subtitleFont, g);
    }

    protected void drawToolTip(String tooltip, Graphics2D g) {
        g.setColor(screenFontColor);
        g.setFont(tooltipFont);
        drawCenteredText(tooltip, 665, tooltipFont, g);
    }

    protected void drawScreenTitles(String subtitle, Graphics2D g) {
        drawTitle(g);
        drawSubtitle(subtitle, g);
    }

    protected void resetInputTimer() {
        inputTimer = INIT_INPUT_TIMER;
    }

    protected void decInputTimer() {
        if (inputTimer > 0) {
            inputTimer--;
        }
    }

    protected void renderButtons(Button[] buttons, Graphics2D g) {
        for (Button button : buttons) {
            button.render(g);
        }
    }

    protected boolean isInputAvailable() {
        return inputTimer == 0;
    }

    protected int getHalfWidth() {
        return screenWidth / 2;
    }

    private int getHalfHeight() {
        return screenHeight / 2;
    }

}