package io.iyyel.celestialoutbreak.level;

import io.iyyel.celestialoutbreak.controller.GameController;
import io.iyyel.celestialoutbreak.handler.FileHandler;
import io.iyyel.celestialoutbreak.handler.LogHandler;
import io.iyyel.celestialoutbreak.handler.TextHandler;
import io.iyyel.celestialoutbreak.ui.entity.Ball;
import io.iyyel.celestialoutbreak.ui.entity.Block;
import io.iyyel.celestialoutbreak.ui.entity.PowerUp;
import io.iyyel.celestialoutbreak.ui.entity.effects.BallEffect;
import io.iyyel.celestialoutbreak.ui.entity.effects.Effect;
import io.iyyel.celestialoutbreak.ui.entity.effects.PaddleEffect;
import org.apache.commons.lang3.exception.ExceptionUtils;

import java.awt.*;
import java.util.Map;

public final class LevelOptions {

    private final TextHandler textHandler = TextHandler.getInstance();
    private final FileHandler fileHandler = FileHandler.getInstance();
    private final LogHandler logHandler = LogHandler.getInstance();

    /*
     * Level options.
     */
    private String levelName;
    private String levelDesc;
    private int levelPlayerLife;
    private String levelSoundFileName;
    private Color levelColor;

    /*
     * Power up options.
     */
    private Dimension powerUpDim;
    private int powerUpSpeed;
    private int powerUpChance;
    private PowerUp.Style powerUpStyle;

    /*
     * Power up effects.
     */
    private Effect[] effects;

    /*
     * Paddle options.
     */
    private Point paddlePos;
    private Dimension paddleDim;
    private int paddleSpeed;
    private Color paddleColor;

    /*
     * Ball options.
     */
    private Point ballPos;
    private Dimension ballDim;
    private int ballSpeed;
    private Ball.Style ballStyle;
    private Color ballColor;

    /*
     * BlockField options.
     */
    private Point blockPosStart;
    private Point blockPosSpacing;
    private int blockAmount;
    private int blockHitPoints;
    private Dimension blockDim;
    private Block.Style blockStyle;
    private float blockLum;
    private float blockSat;

    /*
     * GamePanel options.
     */
    private Color gamePanelTitleColor;
    private Color gamePanelValueColor;

    /*
     * Required objects.
     */
    private final GameController gameController;

    /*
     * Constructor.
     */
    public LevelOptions(String fileName, GameController gameController) {
        this.gameController = gameController;

        try {
            parseLevelOptions(fileName);
        } catch (Exception e) {
            /*
             * If the level options aren't able to be parsed correctly, any exception that will be
             * thrown will be printed to the console as well as the current log file, and then the
             * gameController will be stopped, since invalid level options has been found which might cause
             * the application to be in an inconsistent state.
             */
            logHandler.log(textHandler.errorParsingPropertiesMsg(fileName, ExceptionUtils.getStackTrace(e)), LogHandler.LogLevel.ERROR, false);
            gameController.stop();
        }
    }

    private void parseLevelOptions(String fileName) {
        Map<String, String> map = fileHandler.readPropertiesFromFile(fileName);

        /* Level options */
        levelName = map.get(textHandler.PROP_KEY_LEVEL_NAME);
        levelDesc = map.get(textHandler.PROP_KEY_LEVEL_DESC);
        levelPlayerLife = Integer.parseInt(map.get(textHandler.PROP_KEY_LEVEL_PLAYER_LIFE));
        levelSoundFileName = map.get(textHandler.PROP_KEY_LEVEL_SOUND_FILE_NAME);
        int levelColorValue = Integer.decode(map.get(textHandler.PROP_KEY_LEVEL_COLOR));
        levelColor = new Color(levelColorValue);

        /* Power up options */
        int powerUpWidth = Integer.parseInt(map.get(textHandler.PROP_KEY_POWERUP_WIDTH));
        int powerUpHeight = Integer.parseInt(map.get(textHandler.PROP_KEY_POWERUP_HEIGHT));
        powerUpDim = new Dimension(powerUpWidth, powerUpHeight);
        powerUpSpeed = Integer.parseInt(map.get(textHandler.PROP_KEY_POWERUP_SPEED));
        powerUpChance = Integer.parseInt(map.get(textHandler.PROP_KEY_POWERUP_CHANCE));
        String powerUpStyleStr = map.get(textHandler.PROP_KEY_POWERUP_STYLE);
        powerUpStyle = PowerUp.Style.valueOf(powerUpStyleStr);

        int powerUpAmount = Integer.parseInt(map.get(textHandler.PROP_KEY_POWERUP_AMOUNT));
        effects = new Effect[powerUpAmount];

        for (int i = 0; i < powerUpAmount; i++) {
            String pKeyType = textHandler.powerUpPropNumbered(textHandler.PROP_KEY_POWERUP_EFFECT_TYPE, i);
            String effectType = fileHandler.readPropertyFromFile(pKeyType, textHandler.LEVEL_FILE_CLIENT_PATH_NEPTUNE);

            String pKeyDuration = textHandler.powerUpPropNumbered(textHandler.PROP_KEY_POWERUP_EFFECT_DURATION, i);
            int effectDuration = Integer.parseInt(fileHandler.readPropertyFromFile(pKeyDuration, textHandler.LEVEL_FILE_CLIENT_PATH_NEPTUNE));

            String pKeyWidth = textHandler.powerUpPropNumbered(textHandler.PROP_KEY_POWERUP_EFFECT_WIDTH, i);
            int effectWidth = Integer.parseInt(fileHandler.readPropertyFromFile(pKeyWidth, textHandler.LEVEL_FILE_CLIENT_PATH_NEPTUNE));

            String pKeyHeight = textHandler.powerUpPropNumbered(textHandler.PROP_KEY_POWERUP_EFFECT_HEIGHT, i);
            int effectHeight = Integer.parseInt(fileHandler.readPropertyFromFile(pKeyHeight, textHandler.LEVEL_FILE_CLIENT_PATH_NEPTUNE));

            Dimension effectDim = new Dimension(effectWidth, effectHeight);

            String pKeyColor = textHandler.powerUpPropNumbered(textHandler.PROP_KEY_POWERUP_EFFECT_COLOR, i);
            Color effectColor = new Color(Integer.decode(fileHandler.readPropertyFromFile(pKeyColor, textHandler.LEVEL_FILE_CLIENT_PATH_NEPTUNE)));

            String pKeySpeed = textHandler.powerUpPropNumbered(textHandler.PROP_KEY_POWERUP_EFFECT_SPEED, i);
            int effectSpeed = Integer.parseInt(fileHandler.readPropertyFromFile(pKeySpeed, textHandler.LEVEL_FILE_CLIENT_PATH_NEPTUNE));

            if (effectType.equals("Paddle")) {
                effects[i] = new PaddleEffect(effectDuration, effectDim, effectColor, effectSpeed);
            } else {
                effects[i] = new BallEffect(effectDuration, effectDim, effectColor, effectSpeed);
            }
        }

        /* Paddle options */
        int paddlePosXOffset = Integer.parseInt(map.get(textHandler.PROP_KEY_PADDLE_POS_X_OFFSET));
        int paddlePosYOffset = Integer.parseInt(map.get(textHandler.PROP_KEY_PADDLE_POS_Y_OFFSET));
        paddlePos = new Point((gameController.getWidth() / 2) - paddlePosXOffset, gameController.getHeight() - paddlePosYOffset);
        int paddleWidth = Integer.parseInt(map.get(textHandler.PROP_KEY_PADDLE_WIDTH));
        int paddleHeight = Integer.parseInt(map.get(textHandler.PROP_KEY_PADDLE_HEIGHT));
        paddleDim = new Dimension(paddleWidth, paddleHeight);
        paddleSpeed = Integer.parseInt(map.get(textHandler.PROP_KEY_PADDLE_SPEED));
        int paddleCol = Integer.decode(map.get(textHandler.PROP_KEY_PADDLE_COLOR));
        paddleColor = new Color(paddleCol);

        /* Ball options */
        int ballWidth = Integer.parseInt(map.get(textHandler.PROP_KEY_BALL_WIDTH));
        int ballHeight = Integer.parseInt(map.get(textHandler.PROP_KEY_BALL_HEIGHT));
        ballDim = new Dimension(ballWidth, ballHeight);
        ballPos = new Point(paddlePos.x + (paddleWidth / 2) - (ballWidth / 2), paddlePos.y - (ballHeight));
        ballSpeed = Integer.parseInt(map.get(textHandler.PROP_KEY_BALL_SPEED));
        String ballStyleStr = map.get(textHandler.PROP_KEY_BALL_STYLE);
        ballStyle = Ball.Style.valueOf(ballStyleStr);
        int ballColorHex = Integer.decode(map.get(textHandler.PROP_KEY_BALL_COLOR));
        ballColor = new Color(ballColorHex);

        /* Block options */
        int blockPosXStart = Integer.parseInt(map.get(textHandler.PROP_KEY_BLOCK_POS_X_START));
        int blockPosYStart = Integer.parseInt(map.get(textHandler.PROP_KEY_BLOCK_POS_Y_START));
        blockPosStart = new Point(blockPosXStart, blockPosYStart);
        int blockPosXSpacing = Integer.parseInt(map.get(textHandler.PROP_KEY_BLOCK_POS_X_SPACING));
        int blockPosYSpacing = Integer.parseInt(map.get(textHandler.PROP_KEY_BLOCK_POS_Y_SPACING));
        blockPosSpacing = new Point(blockPosXSpacing, blockPosYSpacing);
        blockAmount = Integer.parseInt(map.get(textHandler.PROP_KEY_BLOCK_AMOUNT));
        blockHitPoints = Integer.parseInt(map.get(textHandler.PROP_KEY_BLOCK_HEALTH));
        int blockWidth = Integer.parseInt(map.get(textHandler.PROP_KEY_BLOCK_WIDTH));
        int blockHeight = Integer.parseInt(map.get(textHandler.PROP_KEY_BLOCK_HEIGHT));
        String blockStyleStr = map.get(textHandler.PROP_KEY_BLOCK_STYLE);
        blockStyle = Block.Style.valueOf(blockStyleStr);
        blockDim = new Dimension(blockWidth, blockHeight);
        blockLum = Float.parseFloat(map.get(textHandler.PROP_KEY_BLOCK_LUMINANCE));
        blockSat = Float.parseFloat(map.get(textHandler.PROP_KEY_BLOCK_SATURATION));

        /* GamePanel options */
        int gamePanelTitleColorInt = Integer.decode(map.get(textHandler.PROP_KEY_GAME_PANEL_TITLE_COLOR));
        int gamePanelValueColorInt = Integer.decode(map.get(textHandler.PROP_KEY_GAME_PANEL_VALUE_COLOR));
        gamePanelTitleColor = new Color(gamePanelTitleColorInt);
        gamePanelValueColor = new Color(gamePanelValueColorInt);
    }

    public String getLevelName() {
        return levelName;
    }

    public String getLevelDesc() {
        return levelDesc;
    }

    public int getLevelPlayerLife() {
        return levelPlayerLife;
    }

    public String getLevelSoundFileName() {
        return levelSoundFileName;
    }

    public Color getLevelColor() {
        return levelColor;
    }

    public Dimension getPowerUpDim() {
        return powerUpDim;
    }

    public int getPowerUpSpeed() {
        return powerUpSpeed;
    }

    public int getPowerUpChance() {
        return powerUpChance;
    }

    public Effect[] getEffects() {
        return effects;
    }

    public Point getPaddlePos() {
        return paddlePos;
    }

    public Dimension getPaddleDim() {
        return paddleDim;
    }

    public int getPaddleSpeed() {
        return paddleSpeed;
    }

    public Color getPaddleColor() {
        return paddleColor;
    }

    public Point getBallPos() {
        return ballPos;
    }

    public Dimension getBallDim() {
        return ballDim;
    }

    public int getBallSpeed() {
        return ballSpeed;
    }

    public Color getBallColor() {
        return ballColor;
    }

    public Point getBlockPosStart() {
        return blockPosStart;
    }

    public Point getBlockPosSpacing() {
        return blockPosSpacing;
    }

    public int getBlockAmount() {
        return blockAmount;
    }

    public int getBlockHitPoints() {
        return blockHitPoints;
    }

    public Dimension getBlockDim() {
        return blockDim;
    }

    public float getBlockLum() {
        return blockLum;
    }

    public float getBlockSat() {
        return blockSat;
    }

    public Color getGamePanelTitleColor() {
        return gamePanelTitleColor;
    }

    public Color getGamePanelValueColor() {
        return gamePanelValueColor;
    }

    public PowerUp.Style getPowerUpStyle() {
        return powerUpStyle;
    }

    public Ball.Style getBallStyle() {
        return ballStyle;
    }

    public Block.Style getBlockStyle() {
        return blockStyle;
    }

}