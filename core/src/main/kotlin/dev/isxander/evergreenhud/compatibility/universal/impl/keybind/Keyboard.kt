package dev.isxander.evergreenhud.compatibility.universal.impl.keybind

import dev.isxander.evergreenhud.compatibility.universal.MCVersion
import dev.isxander.evergreenhud.compatibility.universal.MC_VERSION

enum class Keyboard(val glfw: Int, val lwjgl2: Int) {
    KEY_NONE(-1, 0x00),

    KEY_0(48, 0x0B),
    KEY_1(49, 0x02),
    KEY_2(50, 0x03),
    KEY_3(51, 0x04),
    KEY_4(52, 0x05),
    KEY_5(53, 0x06),
    KEY_6(54, 0x07),
    KEY_7(55, 0x08),
    KEY_8(56, 0x09),
    KEY_9(57, 0x0A),
    KEY_MINUS(45, 0x0C),
    KEY_EQUALS(61, 0x0D),
    KEY_BACKSPACE(259, 0x0E),
    KEY_TAB(258, 0x0F),
    KEY_Q(81, 0x10),
    KEY_W(87, 0x11),
    KEY_E(69, 0x12), // nice
    KEY_R(82, 0x13),
    KEY_T(84, 0x14),
    KEY_Y(89, 0x15),
    KEY_U(85, 0x16),
    KEY_I(73, 0x17),
    KEY_O(79, 0x18),
    KEY_P(80, 0x19),
    KEY_LEFT_BRACKET(91, 0x1A),
    KEY_RIGHT_BRACKET(93, 0x1B),
    KEY_ENTER(257, 0x1C),
    KEY_LEFT_CONTROL(341, 0x1D),
    KEY_A(65, 0x1E),
    KEY_S(83, 0x1F),
    KEY_D(68, 0x20),
    KEY_F(70, 0x21),
    KEY_G(71, 0x22),
    KEY_H(72, 0x23),
    KEY_J(74, 0x24),
    KEY_K(75, 0x25),
    KEY_L(76, 0x26),
    KEY_SEMICOLON(59, 0x27),
    KEY_APOSTROPHE(39, 0x28),
    KEY_GRAVE(96, 0x29),
    KEY_LEFT_SHIFT(340, 0x2A),
    KEY_BACKSLASH(92, 0x2B),
    KEY_Z(90, 0x2C),
    KEY_X(88, 0x2D),
    KEY_C(67, 0x2E),
    KEY_V(86, 0x2F),
    KEY_B(66, 0x30),
    KEY_N(78, 0x31),
    KEY_M(77, 0x32),
    KEY_COMMA(44, 0x33),
    KEY_PERIOD(46, 0x34),
    KEY_SLASH(47, 0x35),
    KEY_RIGHT_SHIFT(344, 0x36),
    KEY_NUMPAD_MULTIPLY(332, 0x37),
    KEY_LEFT_ALT(342, 0x38),
    KEY_SPACE(32, 0x39),
    KEY_CAPS_LOCK(280, 0x3A),
    KEY_F1(290, 0x3B),
    KEY_F2(291, 0x3C),
    KEY_F3(292, 0x3D),
    KEY_F4(293, 0x3E),
    KEY_F5(294, 0x3F),
    KEY_F6(295, 0x40),
    KEY_F7(296, 0x41),
    KEY_F8(297, 0x42),
    KEY_F9(298, 0x43),
    KEY_F10(299, 0x44),
    KEY_NUM_LOCK(282, 0x45),
    KEY_SCROLL_LOCK(281, 0x46),
    KEY_NUMPAD_7(327, 0x47),
    KEY_NUMPAD_8(328, 0x48),
    KEY_NUMPAD_9(329, 0x49),
    KEY_NUMPAD_SUBTRACT(333, 0x4A),
    KEY_NUMPAD_4(324, 0x4B),
    KEY_NUMPAD_5(325, 0x4C),
    KEY_NUMPAD_6(326, 0x4D),
    KEY_NUMPAD_ADD(334, 0x4E),
    KEY_NUMPAD_1(321, 0x4F),
    KEY_NUMPAD_2(322, 0x50),
    KEY_NUMPAD_3(333, 0x51),
    KEY_NUMPAD_0(320, 0x52),
    KEY_NUMPAD_DECIMAL(330, 0x53),
    KEY_F11(300, 0x57),
    KEY_F12(301, 0x58),
    KEY_F13(302, 0x64),
    KEY_F14(303, 0x65),
    KEY_F15(304, 0x66),
    KEY_F16(305, 0x67),
    KEY_F17(306, 0x68),
    KEY_F18(307, 0x69), // nice
    KEY_F19(308, 0x71),
    KEY_NUMPAD_EQUALS(336, 0x8D),
    KEY_RIGHT_CONTROL(345, 0x9D),
    KEY_NUMPAD_DIVIDE(331, 0xB5),
    KEY_RIGHT_ALT(346, 0xB8),
    KEY_PAUSE(284, 0xC5),
    KEY_HOME(268, 0xC7),
    KEY_ARROW_UP(265, 0xC8),
    KEY_PAGE_UP(266, 0xC9),
    KEY_ARROW_LEFT(263, 0xCB),
    KEY_ARROW_RIGHT(262, 0xCD),
    KEY_END(269, 0xCF),
    KEY_ARROW_DOWN(264, 0xD0),
    KEY_PAGE_DOWN(267, 0xD1),
    KEY_INSERT(260, 0xD2),
    KEY_DELETE(261, 0xD3),
    KEY_LEFT_META(343, 0xDB),
    KEY_RIGHT_META(347, 0xDC);

    fun get(): Int {
        return when (MC_VERSION) {
            MCVersion.FORGE_1_8_9 -> lwjgl2
            MCVersion.FABRIC_1_17_1 -> glfw
        }
    }

    companion object {
        fun getGlfw(code: Int): Keyboard {
            for (key in values()) if (key.glfw == code) return key
            return KEY_NONE
        }

        fun getLwjgl2(code: Int): Keyboard {
            for (key in values()) if (key.lwjgl2 == code) return key
            return KEY_NONE
        }
    }
}