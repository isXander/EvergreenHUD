/*
 * EvergreenHUD - A mod to improve on your heads-up-display.
 * Copyright (C) isXander [2019 - 2021]
 *
 * This program comes with ABSOLUTELY NO WARRANTY
 * This is free software, and you are welcome to redistribute it
 * under the certain conditions that can be found here
 * https://www.gnu.org/licenses/lgpl-2.1.en.html
 *
 * If you have any questions or concerns, please create
 * an issue on the github page that can be found here
 * https://github.com/isXander/EvergreenHUD
 *
 * If you have a private concern, please contact
 * isXander @ business.isxander@gmail.com
 */

package dev.isxander.evergreenhud.utils

import dev.isxander.evergreenhud.api.MCVersion
import dev.isxander.evergreenhud.api.mcVersion

@Suppress("UNUSED")
enum class Input(val glfw: Int, val lwjgl2: Int, val type: Type) {
    KEY_NONE(-1, 0x00, Type.Keyboard),
    KEY_0(48, 0x0B, Type.Keyboard),
    KEY_1(49, 0x02, Type.Keyboard),
    KEY_2(50, 0x03, Type.Keyboard),
    KEY_3(51, 0x04, Type.Keyboard),
    KEY_4(52, 0x05, Type.Keyboard),
    KEY_5(53, 0x06, Type.Keyboard),
    KEY_6(54, 0x07, Type.Keyboard),
    KEY_7(55, 0x08, Type.Keyboard),
    KEY_8(56, 0x09, Type.Keyboard),
    KEY_9(57, 0x0A, Type.Keyboard),
    KEY_MINUS(45, 0x0C, Type.Keyboard),
    KEY_EQUALS(61, 0x0D, Type.Keyboard),
    KEY_BACKSPACE(259, 0x0E, Type.Keyboard),
    KEY_TAB(258, 0x0F, Type.Keyboard),
    KEY_Q(81, 0x10, Type.Keyboard),
    KEY_W(87, 0x11, Type.Keyboard),
    KEY_E(69, 0x12, Type.Keyboard), // nice
    KEY_R(82, 0x13, Type.Keyboard),
    KEY_T(84, 0x14, Type.Keyboard),
    KEY_Y(89, 0x15, Type.Keyboard),
    KEY_U(85, 0x16, Type.Keyboard),
    KEY_I(73, 0x17, Type.Keyboard),
    KEY_O(79, 0x18, Type.Keyboard),
    KEY_P(80, 0x19, Type.Keyboard),
    KEY_LEFT_BRACKET(91, 0x1A, Type.Keyboard),
    KEY_RIGHT_BRACKET(93, 0x1B, Type.Keyboard),
    KEY_ENTER(257, 0x1C, Type.Keyboard),
    KEY_LEFT_CONTROL(341, 0x1D, Type.Keyboard),
    KEY_A(65, 0x1E, Type.Keyboard),
    KEY_S(83, 0x1F, Type.Keyboard),
    KEY_D(68, 0x20, Type.Keyboard),
    KEY_F(70, 0x21, Type.Keyboard),
    KEY_G(71, 0x22, Type.Keyboard),
    KEY_H(72, 0x23, Type.Keyboard),
    KEY_J(74, 0x24, Type.Keyboard),
    KEY_K(75, 0x25, Type.Keyboard),
    KEY_L(76, 0x26, Type.Keyboard),
    KEY_SEMICOLON(59, 0x27, Type.Keyboard),
    KEY_APOSTROPHE(39, 0x28, Type.Keyboard),
    KEY_GRAVE(96, 0x29, Type.Keyboard),
    KEY_LEFT_SHIFT(340, 0x2A, Type.Keyboard),
    KEY_BACKSLASH(92, 0x2B, Type.Keyboard),
    KEY_Z(90, 0x2C, Type.Keyboard),
    KEY_X(88, 0x2D, Type.Keyboard),
    KEY_C(67, 0x2E, Type.Keyboard),
    KEY_V(86, 0x2F, Type.Keyboard),
    KEY_B(66, 0x30, Type.Keyboard),
    KEY_N(78, 0x31, Type.Keyboard),
    KEY_M(77, 0x32, Type.Keyboard),
    KEY_COMMA(44, 0x33, Type.Keyboard),
    KEY_PERIOD(46, 0x34, Type.Keyboard),
    KEY_SLASH(47, 0x35, Type.Keyboard),
    KEY_RIGHT_SHIFT(344, 0x36, Type.Keyboard),
    KEY_NUMPAD_MULTIPLY(332, 0x37, Type.Keyboard),
    KEY_LEFT_ALT(342, 0x38, Type.Keyboard),
    KEY_SPACE(32, 0x39, Type.Keyboard),
    KEY_CAPS_LOCK(280, 0x3A, Type.Keyboard),
    KEY_F1(290, 0x3B, Type.Keyboard),
    KEY_F2(291, 0x3C, Type.Keyboard),
    KEY_F3(292, 0x3D, Type.Keyboard),
    KEY_F4(293, 0x3E, Type.Keyboard),
    KEY_F5(294, 0x3F, Type.Keyboard),
    KEY_F6(295, 0x40, Type.Keyboard),
    KEY_F7(296, 0x41, Type.Keyboard),
    KEY_F8(297, 0x42, Type.Keyboard),
    KEY_F9(298, 0x43, Type.Keyboard),
    KEY_F10(299, 0x44, Type.Keyboard),
    KEY_NUM_LOCK(282, 0x45, Type.Keyboard),
    KEY_SCROLL_LOCK(281, 0x46, Type.Keyboard),
    KEY_NUMPAD_7(327, 0x47, Type.Keyboard),
    KEY_NUMPAD_8(328, 0x48, Type.Keyboard),
    KEY_NUMPAD_9(329, 0x49, Type.Keyboard),
    KEY_NUMPAD_SUBTRACT(333, 0x4A, Type.Keyboard),
    KEY_NUMPAD_4(324, 0x4B, Type.Keyboard),
    KEY_NUMPAD_5(325, 0x4C, Type.Keyboard),
    KEY_NUMPAD_6(326, 0x4D, Type.Keyboard),
    KEY_NUMPAD_ADD(334, 0x4E, Type.Keyboard),
    KEY_NUMPAD_1(321, 0x4F, Type.Keyboard),
    KEY_NUMPAD_2(322, 0x50, Type.Keyboard),
    KEY_NUMPAD_3(333, 0x51, Type.Keyboard),
    KEY_NUMPAD_0(320, 0x52, Type.Keyboard),
    KEY_NUMPAD_DECIMAL(330, 0x53, Type.Keyboard),
    KEY_F11(300, 0x57, Type.Keyboard),
    KEY_F12(301, 0x58, Type.Keyboard),
    KEY_F13(302, 0x64, Type.Keyboard),
    KEY_F14(303, 0x65, Type.Keyboard),
    KEY_F15(304, 0x66, Type.Keyboard),
    KEY_F16(305, 0x67, Type.Keyboard),
    KEY_F17(306, 0x68, Type.Keyboard),
    KEY_F18(307, 0x69, Type.Keyboard), // nice
    KEY_F19(308, 0x71, Type.Keyboard),
    KEY_NUMPAD_EQUALS(336, 0x8D, Type.Keyboard),
    KEY_RIGHT_CONTROL(345, 0x9D, Type.Keyboard),
    KEY_NUMPAD_DIVIDE(331, 0xB5, Type.Keyboard),
    KEY_RIGHT_ALT(346, 0xB8, Type.Keyboard),
    KEY_PAUSE(284, 0xC5, Type.Keyboard),
    KEY_HOME(268, 0xC7, Type.Keyboard),
    KEY_ARROW_UP(265, 0xC8, Type.Keyboard),
    KEY_PAGE_UP(266, 0xC9, Type.Keyboard),
    KEY_ARROW_LEFT(263, 0xCB, Type.Keyboard),
    KEY_ARROW_RIGHT(262, 0xCD, Type.Keyboard),
    KEY_END(269, 0xCF, Type.Keyboard),
    KEY_ARROW_DOWN(264, 0xD0, Type.Keyboard),
    KEY_PAGE_DOWN(267, 0xD1, Type.Keyboard),
    KEY_INSERT(260, 0xD2, Type.Keyboard),
    KEY_DELETE(261, 0xD3, Type.Keyboard),
    KEY_LEFT_META(343, 0xDB, Type.Keyboard),
    KEY_RIGHT_META(347, 0xDC, Type.Keyboard),

    MOUSE_LEFT(0, 0, Type.Mouse),
    MOUSE_RIGHT(1, 1, Type.Mouse),
    MOUSE_MIDDLE(2, 2, Type.Mouse),
    MOUSE_4(3, 3, Type.Mouse),
    MOUSE_5(4, 4, Type.Mouse),
    MOUSE_6(5, 5, Type.Mouse),
    MOUSE_7(6, 6, Type.Mouse),
    MOUSE_8(7, 7, Type.Mouse);

    fun get(): Int {
        return when (mcVersion) {
            MCVersion.FORGE_1_8_9 -> lwjgl2
            MCVersion.FABRIC_1_17_1 -> glfw
        }
    }
    
    enum class Type {
        Keyboard,
        Mouse
    }

    companion object {
        fun find(code: Int): Input {
            for (key in values()) if (key.get() == code) return key
            return KEY_NONE
        }
    }
}