package com.bugfullabs.curvnapse.utils;

import com.bugfullabs.curvnapse.player.PlayerColor;
import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ColorBank {
    private List<PlayerColor> mColors;
    private Map<PlayerColor, Boolean> mTaken;

    public ColorBank() {
        mColors = new ArrayList<>();
        mTaken = new HashMap<>();

        mColors.add(new PlayerColor(Color.RED));
        mColors.add(new PlayerColor(Color.BLUE));
        mColors.add(new PlayerColor(Color.DARKSLATEGRAY));
        mColors.add(new PlayerColor(Color.ORANGE));
        mColors.add(new PlayerColor(Color.DARKMAGENTA));
        mColors.add(new PlayerColor(Color.AQUA));
        mColors.add(new PlayerColor(Color.BLUEVIOLET));
        mColors.add(new PlayerColor(Color.VIOLET));
        mColors.add(new PlayerColor(Color.DARKCYAN));
        mColors.add(new PlayerColor(Color.DARKGOLDENROD));

        mColors.forEach(pPlayerColor -> mTaken.put(pPlayerColor, false));
    }

    public PlayerColor nextColor() {
        PlayerColor color = mTaken.entrySet().stream()
                .filter(entry -> !entry.getValue())
                .findFirst()
                .get()
                .getKey();
        mTaken.replace(color, true);
        return color;
    }

    public void returnColor(PlayerColor pColor) {
        mTaken.replaceAll((color, used) -> pColor.equals(color) ? false : used);
    }
}
