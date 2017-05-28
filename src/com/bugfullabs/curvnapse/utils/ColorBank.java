package com.bugfullabs.curvnapse.utils;

import javafx.scene.paint.Color;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Class that yields available colors for new players
 */
public class ColorBank {
    private Map<SerializableColor, Boolean> mTaken;

    /**
     * Create new ColorBank
     */
    public ColorBank() {

        List<SerializableColor> colors = new ArrayList<>();
        mTaken = new HashMap<>();

        colors.add(new SerializableColor(Color.RED));
        colors.add(new SerializableColor(Color.BLUE));
        colors.add(new SerializableColor(Color.DARKSLATEGRAY));
        colors.add(new SerializableColor(Color.ORANGE));
        colors.add(new SerializableColor(Color.DARKMAGENTA));
        colors.add(new SerializableColor(Color.AQUA));
        colors.add(new SerializableColor(Color.BLUEVIOLET));
        colors.add(new SerializableColor(Color.VIOLET));
        colors.add(new SerializableColor(Color.DARKCYAN));
        colors.add(new SerializableColor(Color.DARKSALMON));

        colors.forEach(pPlayerColor -> mTaken.put(pPlayerColor, false));
    }

    /**
     * Yield next color
     *
     * @return Color
     */
    public SerializableColor nextColor() {
        List<SerializableColor> colors = mTaken.entrySet().stream()
                .filter(entry -> !entry.getValue()).map(Map.Entry::getKey).collect(Collectors.toList());

        if (colors.size() == 0)
            return null;

        SerializableColor color = colors.get(new Random().nextInt(colors.size()));

        mTaken.replace(color, true);
        return color;
    }

    /**
     * Return color to bank
     *
     * @param pColor color to be returned
     */
    public void returnColor(SerializableColor pColor) {
        mTaken.replaceAll((color, used) -> pColor.equals(color) ? false : used);
    }
}
