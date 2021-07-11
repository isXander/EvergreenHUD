/*
 * Copyright (C) isXander [2019 - 2021]
 * This program comes with ABSOLUTELY NO WARRANTY
 * This is free software, and you are welcome to redistribute it
 * under the certain conditions that can be found here
 * https://www.gnu.org/licenses/gpl-3.0.en.html
 *
 * If you have any questions or concerns, please create
 * an issue on the github page that can be found here
 * https://github.com/isXander/EvergreenHUD
 *
 * If you have a private concern, please contact
 * isXander @ business.isxander@gmail.com
 */

package co.uk.isxander.evergreenhud.elements.impl;

import co.uk.isxander.evergreenhud.elements.type.SimpleTextElement;
import co.uk.isxander.evergreenhud.settings.impl.ArraySetting;
import co.uk.isxander.evergreenhud.elements.ElementData;
import co.uk.isxander.evergreenhud.settings.impl.EnumSetting;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import org.lwjgl.input.Mouse;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

public class ElementCps extends SimpleTextElement {

    private final Deque<Long> left = new ArrayDeque<>();
    private boolean leftPressed;
    private final Deque<Long> right = new ArrayDeque<>();
    private boolean rightPressed;

    public ArraySetting button;
    public EnumSetting<InputMode> inputMode;

    @Override
    public void initialise() {
        addSettings(button = new ArraySetting("Button", "Display", "Which button to display.", "Both", new String[]{"Left", "Right", "Both"}));
        addSettings(inputMode = new EnumSetting<>("Input Mode", "Display", "How the element should receive input.", InputMode.CONTROLS));
    }

    @Override
    public ElementData metadata() {
        return new ElementData("CPS Mod", "Shows how many times you can click in a second.", "Combat");
    }

    @Override
    protected String getValue() {
        String text = "";
        switch (button.get().toLowerCase()) {
            case "left":
                text = Integer.toString(left.size());
                break;
            case "right":
                text = Integer.toString(right.size());
                break;
            case "both":
                text = left.size() + " | " + right.size();
                break;
        }
        return text;
    }

    @Override
    public String getDefaultDisplayTitle() {
        return "CPS";
    }

    @Override
    public void onRenderTick(TickEvent.RenderTickEvent event) {
        if (event.phase == TickEvent.Phase.START) return;

        boolean pressed;
        if (inputMode.get() == InputMode.RAW)
            pressed = Mouse.isButtonDown(0);
        else
            pressed = mc.gameSettings.keyBindAttack.isKeyDown();

        if (pressed != leftPressed) {
            leftPressed = pressed;
            if (pressed) left.add(System.currentTimeMillis());
        }

        if (inputMode.get() == InputMode.RAW)
            pressed = Mouse.isButtonDown(1);
        else
            pressed = mc.gameSettings.keyBindUseItem.isKeyDown();

        if (pressed != rightPressed) {
            rightPressed = pressed;
            if (pressed) right.add(System.currentTimeMillis());
        }

        final long currentTime = System.currentTimeMillis();
        if (!left.isEmpty()) {
            while ((currentTime - left.getFirst()) > 1000) {
                left.removeFirst();
                if (left.isEmpty()) break;
            }
        }
        if (!right.isEmpty()) {
            while ((currentTime - right.getFirst()) > 1000) {
                right.removeFirst();
                if (right.isEmpty()) break;
            }
        }
    }

    public enum InputMode {
        CONTROLS,
        RAW
    }

}
