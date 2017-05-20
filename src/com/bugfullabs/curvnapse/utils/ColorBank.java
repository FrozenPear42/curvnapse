package com.bugfullabs.curvnapse.utils;

import com.bugfullabs.curvnapse.player.PlayerColor;
import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Class that yields available colors for new players
 */
public class ColorBank {
    private Map<PlayerColor, Boolean> mTaken;

    /**
     * Create new ColorBank
     */
    public ColorBank() {

        List<PlayerColor> colors = new ArrayList<>();
        mTaken = new HashMap<>();

        colors.add(new PlayerColor(Color.RED));
        colors.add(new PlayerColor(Color.BLUE));
        colors.add(new PlayerColor(Color.DARKSLATEGRAY));
        colors.add(new PlayerColor(Color.ORANGE));
        colors.add(new PlayerColor(Color.DARKMAGENTA));
        colors.add(new PlayerColor(Color.AQUA));
        colors.add(new PlayerColor(Color.BLUEVIOLET));
        colors.add(new PlayerColor(Color.VIOLET));
        colors.add(new PlayerColor(Color.DARKCYAN));
        colors.add(new PlayerColor(Color.DARKGOLDENROD));

        colors.forEach(pPlayerColor -> mTaken.put(pPlayerColor, false));
    }

    /**
     * Yield next color
     * @return Color
     */
    public PlayerColor nextColor() {
        PlayerColor color = mTaken.entrySet().stream()
                .filter(entry -> !entry.getValue())
                .findFirst()
                .get()
                .getKey();
        mTaken.replace(color, true);
        return color;
    }

    /**
     * Return color to bank
     * @param pColor color to be returned
     */
    public void returnColor(PlayerColor pColor) {
        mTaken.replaceAll((color, used) -> pColor.equals(color) ? false : used);
    }
}
