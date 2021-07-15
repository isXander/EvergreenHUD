package dev.isxander.evergreenhud.compatibility.universal.impl.render

import dev.isxander.evergreenhud.compatibility.universal.RESOLUTION

abstract class AIGL11 {

    /* ---------------------------------- */
    /* Render Stuff                 BEGIN */
    /* ---------------------------------- */
    abstract fun push()
    abstract fun pop()

    abstract fun scale(x: Float, y: Float, z: Float)
    abstract fun translate(x: Double, y: Double, z: Double)
    abstract fun color(r: Float, g: Float, b: Float, a: Float = 1f)
    abstract fun rotate(angle: Float, x: Float, y: Float, z: Float)

    abstract fun bindTexture(texture: Int)

    abstract fun scissor(x: Int, y: Int, width: Int, height: Int)
    abstract fun enableScissor()
    abstract fun disableScissor()

    abstract fun lineWidth(width: Float)

    abstract fun enable(target: Int)
    abstract fun disable(target: Int)

    abstract fun begin(mode: Int)
    abstract fun end()

    abstract fun enableBlend()
    abstract fun disableBlend()

    abstract fun enableTexture()
    abstract fun disableTexture()

    abstract fun enableAlpha()
    abstract fun disableAlpha()

    abstract fun enableDepth()
    abstract fun disableDepth()

    abstract fun blendFuncSeparate(srcFactorRGB: Int, dstFactorRGB: Int, srcFactorAlpha: Int, dstFactorAlpha: Int)
    abstract fun blendFunc(srcFactor: Int, dstFactor: Int)

    /* ---------------------------------- */
    /* Utility Stuff                BEGIN */
    /* ---------------------------------- */
    fun defaultBlendFunc() = blendFuncSeparate(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA, GL_ONE, GL_ZERO)

    abstract fun rect(x: Float, y: Float, width: Float, height: Float, color: Int)
    abstract fun roundedRect(x: Float, y: Float, width: Float, height: Float, color: Int, angle: Float)

    open fun scissorStart(x: Int, y: Int, width: Int, height: Int) {
        enableScissor()
        scissor(
            (x * RESOLUTION.getDisplayWidth()
                    / RESOLUTION.getScaledWidth()),
            (((RESOLUTION.getScaledHeight() - (y + height))
                    * RESOLUTION.getDisplayHeight())
                    / RESOLUTION.getScaledHeight()),
            (width * RESOLUTION.getDisplayWidth() / RESOLUTION.getScaledWidth()),
            (height * RESOLUTION.getDisplayHeight() / RESOLUTION.getScaledHeight())
        )
    }
    open fun scissorEnd() = disableScissor()

    abstract fun circle(x: Float, y: Float, radius: Float, color: Int)

    @Suppress("UNUSED")
    companion object {
        const val GL_ACCUM = 0x100
        const val GL_LOAD = 0x101
        const val GL_RETURN = 0x102
        const val GL_MULT = 0x103
        const val GL_ADD = 0x104
        const val GL_NEVER = 0x200
        const val GL_LESS = 0x201
        const val GL_EQUAL = 0x202
        const val GL_LEQUAL = 0x203
        const val GL_GREATER = 0x204
        const val GL_NOTEQUAL = 0x205
        const val GL_GEQUAL = 0x206
        const val GL_ALWAYS = 0x207
        const val GL_CURRENT_BIT = 0x1
        const val GL_POINT_BIT = 0x2
        const val GL_LINE_BIT = 0x4
        const val GL_POLYGON_BIT = 0x8
        const val GL_POLYGON_STIPPLE_BIT = 0x10
        const val GL_PIXEL_MODE_BIT = 0x20
        const val GL_LIGHTING_BIT = 0x40
        const val GL_FOG_BIT = 0x80
        const val GL_DEPTH_BUFFER_BIT = 0x100
        const val GL_ACCUM_BUFFER_BIT = 0x200
        const val GL_STENCIL_BUFFER_BIT = 0x400
        const val GL_VIEWPORT_BIT = 0x800
        const val GL_TRANSFORM_BIT = 0x1000
        const val GL_ENABLE_BIT = 0x2000
        const val GL_COLOR_BUFFER_BIT = 0x4000
        const val GL_HINT_BIT = 0x8000
        const val GL_EVAL_BIT = 0x10000
        const val GL_LIST_BIT = 0x20000
        const val GL_TEXTURE_BIT = 0x40000
        const val GL_SCISSOR_BIT = 0x80000
        const val GL_ALL_ATTRIB_BITS = 0xFFFFF
        const val GL_POINTS = 0x0
        const val GL_LINES = 0x1
        const val GL_LINE_LOOP = 0x2
        const val GL_LINE_STRIP = 0x3
        const val GL_TRIANGLES = 0x4
        const val GL_TRIANGLE_STRIP = 0x5
        const val GL_TRIANGLE_FAN = 0x6
        const val GL_QUADS = 0x7
        const val GL_QUAD_STRIP = 0x8
        const val GL_POLYGON = 0x9
        const val GL_ZERO = 0x0
        const val GL_ONE = 0x1
        const val GL_SRC_COLOR = 0x300
        const val GL_ONE_MINUS_SRC_COLOR = 0x301
        const val GL_SRC_ALPHA = 0x302
        const val GL_ONE_MINUS_SRC_ALPHA = 0x303
        const val GL_DST_ALPHA = 0x304
        const val GL_ONE_MINUS_DST_ALPHA = 0x305
        const val GL_DST_COLOR = 0x306
        const val GL_ONE_MINUS_DST_COLOR = 0x307
        const val GL_SRC_ALPHA_SATURATE = 0x308
        const val GL_CONSTANT_COLOR = 0x8001
        const val GL_ONE_MINUS_CONSTANT_COLOR = 0x8002
        const val GL_CONSTANT_ALPHA = 0x8003
        const val GL_ONE_MINUS_CONSTANT_ALPHA = 0x8004
        const val GL_TRUE = 0x1
        const val GL_FALSE = 0x0
        const val GL_CLIP_PLANE0 = 0x3000
        const val GL_CLIP_PLANE1 = 0x3001
        const val GL_CLIP_PLANE2 = 0x3002
        const val GL_CLIP_PLANE3 = 0x3003
        const val GL_CLIP_PLANE4 = 0x3004
        const val GL_CLIP_PLANE5 = 0x3005
        const val GL_BYTE = 0x1400
        const val GL_UNSIGNED_BYTE = 0x1401
        const val GL_SHORT = 0x1402
        const val GL_UNSIGNED_SHORT = 0x1403
        const val GL_INT = 0x1404
        const val GL_UNSIGNED_INT = 0x1405
        const val GL_FLOAT = 0x1406
        const val GL_2_BYTES = 0x1407
        const val GL_3_BYTES = 0x1408
        const val GL_4_BYTES = 0x1409
        const val GL_DOUBLE = 0x140A
        const val GL_NONE = 0x0
        const val GL_FRONT_LEFT = 0x400
        const val GL_FRONT_RIGHT = 0x401
        const val GL_BACK_LEFT = 0x402
        const val GL_BACK_RIGHT = 0x403
        const val GL_FRONT = 0x404
        const val GL_BACK = 0x405
        const val GL_LEFT = 0x406
        const val GL_RIGHT = 0x407
        const val GL_FRONT_AND_BACK = 0x408
        const val GL_AUX0 = 0x409
        const val GL_AUX1 = 0x40A
        const val GL_AUX2 = 0x40B
        const val GL_AUX3 = 0x40C
        const val GL_NO_ERROR = 0x0
        const val GL_INVALID_ENUM = 0x500
        const val GL_INVALID_VALUE = 0x501
        const val GL_INVALID_OPERATION = 0x502
        const val GL_STACK_OVERFLOW = 0x503
        const val GL_STACK_UNDERFLOW = 0x504
        const val GL_OUT_OF_MEMORY = 0x505
        const val GL_2D = 0x600
        const val GL_3D = 0x601
        const val GL_3D_COLOR = 0x602
        const val GL_3D_COLOR_TEXTURE = 0x603
        const val GL_4D_COLOR_TEXTURE = 0x604
        const val GL_PASS_THROUGH_TOKEN = 0x700
        const val GL_POINT_TOKEN = 0x701
        const val GL_LINE_TOKEN = 0x702
        const val GL_POLYGON_TOKEN = 0x703
        const val GL_BITMAP_TOKEN = 0x704
        const val GL_DRAW_PIXEL_TOKEN = 0x705
        const val GL_COPY_PIXEL_TOKEN = 0x706
        const val GL_LINE_RESET_TOKEN = 0x707
        const val GL_EXP = 0x800
        const val GL_EXP2 = 0x801
        const val GL_CW = 0x900
        const val GL_CCW = 0x901
        const val GL_COEFF = 0xA00
        const val GL_ORDER = 0xA01
        const val GL_DOMAIN = 0xA02
        const val GL_CURRENT_COLOR = 0xB00
        const val GL_CURRENT_INDEX = 0xB01
        const val GL_CURRENT_NORMAL = 0xB02
        const val GL_CURRENT_TEXTURE_COORDS = 0xB03
        const val GL_CURRENT_RASTER_COLOR = 0xB04
        const val GL_CURRENT_RASTER_INDEX = 0xB05
        const val GL_CURRENT_RASTER_TEXTURE_COORDS = 0xB06
        const val GL_CURRENT_RASTER_POSITION = 0xB07
        const val GL_CURRENT_RASTER_POSITION_VALID = 0xB08
        const val GL_CURRENT_RASTER_DISTANCE = 0xB09
        const val GL_POINT_SMOOTH = 0xB10
        const val GL_POINT_SIZE = 0xB11
        const val GL_POINT_SIZE_RANGE = 0xB12
        const val GL_POINT_SIZE_GRANULARITY = 0xB13
        const val GL_LINE_SMOOTH = 0xB20
        const val GL_LINE_WIDTH = 0xB21
        const val GL_LINE_WIDTH_RANGE = 0xB22
        const val GL_LINE_WIDTH_GRANULARITY = 0xB23
        const val GL_LINE_STIPPLE = 0xB24
        const val GL_LINE_STIPPLE_PATTERN = 0xB25
        const val GL_LINE_STIPPLE_REPEAT = 0xB26
        const val GL_LIST_MODE = 0xB30
        const val GL_MAX_LIST_NESTING = 0xB31
        const val GL_LIST_BASE = 0xB32
        const val GL_LIST_INDEX = 0xB33
        const val GL_POLYGON_MODE = 0xB40
        const val GL_POLYGON_SMOOTH = 0xB41
        const val GL_POLYGON_STIPPLE = 0xB42
        const val GL_EDGE_FLAG = 0xB43
        const val GL_CULL_FACE = 0xB44
        const val GL_CULL_FACE_MODE = 0xB45
        const val GL_FRONT_FACE = 0xB46
        const val GL_LIGHTING = 0xB50
        const val GL_LIGHT_MODEL_LOCAL_VIEWER = 0xB51
        const val GL_LIGHT_MODEL_TWO_SIDE = 0xB52
        const val GL_LIGHT_MODEL_AMBIENT = 0xB53
        const val GL_SHADE_MODEL = 0xB54
        const val GL_COLOR_MATERIAL_FACE = 0xB55
        const val GL_COLOR_MATERIAL_PARAMETER = 0xB56
        const val GL_COLOR_MATERIAL = 0xB57
        const val GL_FOG = 0xB60
        const val GL_FOG_INDEX = 0xB61
        const val GL_FOG_DENSITY = 0xB62
        const val GL_FOG_START = 0xB63
        const val GL_FOG_END = 0xB64
        const val GL_FOG_MODE = 0xB65
        const val GL_FOG_COLOR = 0xB66
        const val GL_DEPTH_RANGE = 0xB70
        const val GL_DEPTH_TEST = 0xB71
        const val GL_DEPTH_WRITEMASK = 0xB72
        const val GL_DEPTH_CLEAR_VALUE = 0xB73
        const val GL_DEPTH_FUNC = 0xB74
        const val GL_ACCUM_CLEAR_VALUE = 0xB80
        const val GL_STENCIL_TEST = 0xB90
        const val GL_STENCIL_CLEAR_VALUE = 0xB91
        const val GL_STENCIL_FUNC = 0xB92
        const val GL_STENCIL_VALUE_MASK = 0xB93
        const val GL_STENCIL_FAIL = 0xB94
        const val GL_STENCIL_PASS_DEPTH_FAIL = 0xB95
        const val GL_STENCIL_PASS_DEPTH_PASS = 0xB96
        const val GL_STENCIL_REF = 0xB97
        const val GL_STENCIL_WRITEMASK = 0xB98
        const val GL_MATRIX_MODE = 0xBA0
        const val GL_NORMALIZE = 0xBA1
        const val GL_VIEWPORT = 0xBA2
        const val GL_MODELVIEW_STACK_DEPTH = 0xBA3
        const val GL_PROJECTION_STACK_DEPTH = 0xBA4
        const val GL_TEXTURE_STACK_DEPTH = 0xBA5
        const val GL_MODELVIEW_MATRIX = 0xBA6
        const val GL_PROJECTION_MATRIX = 0xBA7
        const val GL_TEXTURE_MATRIX = 0xBA8
        const val GL_ATTRIB_STACK_DEPTH = 0xBB0
        const val GL_CLIENT_ATTRIB_STACK_DEPTH = 0xBB1
        const val GL_ALPHA_TEST = 0xBC0
        const val GL_ALPHA_TEST_FUNC = 0xBC1
        const val GL_ALPHA_TEST_REF = 0xBC2
        const val GL_DITHER = 0xBD0
        const val GL_BLEND_DST = 0xBE0
        const val GL_BLEND_SRC = 0xBE1
        const val GL_BLEND = 0xBE2
        const val GL_LOGIC_OP_MODE = 0xBF0
        const val GL_INDEX_LOGIC_OP = 0xBF1
        const val GL_COLOR_LOGIC_OP = 0xBF2
        const val GL_AUX_BUFFERS = 0xC00
        const val GL_DRAW_BUFFER = 0xC01
        const val GL_READ_BUFFER = 0xC02
        const val GL_SCISSOR_BOX = 0xC10
        const val GL_SCISSOR_TEST = 0xC11
        const val GL_INDEX_CLEAR_VALUE = 0xC20
        const val GL_INDEX_WRITEMASK = 0xC21
        const val GL_COLOR_CLEAR_VALUE = 0xC22
        const val GL_COLOR_WRITEMASK = 0xC23
        const val GL_INDEX_MODE = 0xC30
        const val GL_RGBA_MODE = 0xC31
        const val GL_DOUBLEBUFFER = 0xC32
        const val GL_STEREO = 0xC33
        const val GL_RENDER_MODE = 0xC40
        const val GL_PERSPECTIVE_CORRECTION_HINT = 0xC50
        const val GL_POINT_SMOOTH_HINT = 0xC51
        const val GL_LINE_SMOOTH_HINT = 0xC52
        const val GL_POLYGON_SMOOTH_HINT = 0xC53
        const val GL_FOG_HINT = 0xC54
        const val GL_TEXTURE_GEN_S = 0xC60
        const val GL_TEXTURE_GEN_T = 0xC61
        const val GL_TEXTURE_GEN_R = 0xC62
        const val GL_TEXTURE_GEN_Q = 0xC63
        const val GL_PIXEL_MAP_I_TO_I = 0xC70
        const val GL_PIXEL_MAP_S_TO_S = 0xC71
        const val GL_PIXEL_MAP_I_TO_R = 0xC72
        const val GL_PIXEL_MAP_I_TO_G = 0xC73
        const val GL_PIXEL_MAP_I_TO_B = 0xC74
        const val GL_PIXEL_MAP_I_TO_A = 0xC75
        const val GL_PIXEL_MAP_R_TO_R = 0xC76
        const val GL_PIXEL_MAP_G_TO_G = 0xC77
        const val GL_PIXEL_MAP_B_TO_B = 0xC78
        const val GL_PIXEL_MAP_A_TO_A = 0xC79
        const val GL_PIXEL_MAP_I_TO_I_SIZE = 0xCB0
        const val GL_PIXEL_MAP_S_TO_S_SIZE = 0xCB1
        const val GL_PIXEL_MAP_I_TO_R_SIZE = 0xCB2
        const val GL_PIXEL_MAP_I_TO_G_SIZE = 0xCB3
        const val GL_PIXEL_MAP_I_TO_B_SIZE = 0xCB4
        const val GL_PIXEL_MAP_I_TO_A_SIZE = 0xCB5
        const val GL_PIXEL_MAP_R_TO_R_SIZE = 0xCB6
        const val GL_PIXEL_MAP_G_TO_G_SIZE = 0xCB7
        const val GL_PIXEL_MAP_B_TO_B_SIZE = 0xCB8
        const val GL_PIXEL_MAP_A_TO_A_SIZE = 0xCB9
        const val GL_UNPACK_SWAP_BYTES = 0xCF0
        const val GL_UNPACK_LSB_FIRST = 0xCF1
        const val GL_UNPACK_ROW_LENGTH = 0xCF2
        const val GL_UNPACK_SKIP_ROWS = 0xCF3
        const val GL_UNPACK_SKIP_PIXELS = 0xCF4
        const val GL_UNPACK_ALIGNMENT = 0xCF5
        const val GL_PACK_SWAP_BYTES = 0xD00
        const val GL_PACK_LSB_FIRST = 0xD01
        const val GL_PACK_ROW_LENGTH = 0xD02
        const val GL_PACK_SKIP_ROWS = 0xD03
        const val GL_PACK_SKIP_PIXELS = 0xD04
        const val GL_PACK_ALIGNMENT = 0xD05
        const val GL_MAP_COLOR = 0xD10
        const val GL_MAP_STENCIL = 0xD11
        const val GL_INDEX_SHIFT = 0xD12
        const val GL_INDEX_OFFSET = 0xD13
        const val GL_RED_SCALE = 0xD14
        const val GL_RED_BIAS = 0xD15
        const val GL_ZOOM_X = 0xD16
        const val GL_ZOOM_Y = 0xD17
        const val GL_GREEN_SCALE = 0xD18
        const val GL_GREEN_BIAS = 0xD19
        const val GL_BLUE_SCALE = 0xD1A
        const val GL_BLUE_BIAS = 0xD1B
        const val GL_ALPHA_SCALE = 0xD1C
        const val GL_ALPHA_BIAS = 0xD1D
        const val GL_DEPTH_SCALE = 0xD1E
        const val GL_DEPTH_BIAS = 0xD1F
        const val GL_MAX_EVAL_ORDER = 0xD30
        const val GL_MAX_LIGHTS = 0xD31
        const val GL_MAX_CLIP_PLANES = 0xD32
        const val GL_MAX_TEXTURE_SIZE = 0xD33
        const val GL_MAX_PIXEL_MAP_TABLE = 0xD34
        const val GL_MAX_ATTRIB_STACK_DEPTH = 0xD35
        const val GL_MAX_MODELVIEW_STACK_DEPTH = 0xD36
        const val GL_MAX_NAME_STACK_DEPTH = 0xD37
        const val GL_MAX_PROJECTION_STACK_DEPTH = 0xD38
        const val GL_MAX_TEXTURE_STACK_DEPTH = 0xD39
        const val GL_MAX_VIEWPORT_DIMS = 0xD3A
        const val GL_MAX_CLIENT_ATTRIB_STACK_DEPTH = 0xD3B
        const val GL_SUBPIXEL_BITS = 0xD50
        const val GL_INDEX_BITS = 0xD51
        const val GL_RED_BITS = 0xD52
        const val GL_GREEN_BITS = 0xD53
        const val GL_BLUE_BITS = 0xD54
        const val GL_ALPHA_BITS = 0xD55
        const val GL_DEPTH_BITS = 0xD56
        const val GL_STENCIL_BITS = 0xD57
        const val GL_ACCUM_RED_BITS = 0xD58
        const val GL_ACCUM_GREEN_BITS = 0xD59
        const val GL_ACCUM_BLUE_BITS = 0xD5A
        const val GL_ACCUM_ALPHA_BITS = 0xD5B
        const val GL_NAME_STACK_DEPTH = 0xD70
        const val GL_AUTO_NORMAL = 0xD80
        const val GL_MAP1_COLOR_4 = 0xD90
        const val GL_MAP1_INDEX = 0xD91
        const val GL_MAP1_NORMAL = 0xD92
        const val GL_MAP1_TEXTURE_COORD_1 = 0xD93
        const val GL_MAP1_TEXTURE_COORD_2 = 0xD94
        const val GL_MAP1_TEXTURE_COORD_3 = 0xD95
        const val GL_MAP1_TEXTURE_COORD_4 = 0xD96
        const val GL_MAP1_VERTEX_3 = 0xD97
        const val GL_MAP1_VERTEX_4 = 0xD98
        const val GL_MAP2_COLOR_4 = 0xDB0
        const val GL_MAP2_INDEX = 0xDB1
        const val GL_MAP2_NORMAL = 0xDB2
        const val GL_MAP2_TEXTURE_COORD_1 = 0xDB3
        const val GL_MAP2_TEXTURE_COORD_2 = 0xDB4
        const val GL_MAP2_TEXTURE_COORD_3 = 0xDB5
        const val GL_MAP2_TEXTURE_COORD_4 = 0xDB6
        const val GL_MAP2_VERTEX_3 = 0xDB7
        const val GL_MAP2_VERTEX_4 = 0xDB8
        const val GL_MAP1_GRID_DOMAIN = 0xDD0
        const val GL_MAP1_GRID_SEGMENTS = 0xDD1
        const val GL_MAP2_GRID_DOMAIN = 0xDD2
        const val GL_MAP2_GRID_SEGMENTS = 0xDD3
        const val GL_TEXTURE_1D = 0xDE0
        const val GL_TEXTURE_2D = 0xDE1
        const val GL_FEEDBACK_BUFFER_POINTER = 0xDF0
        const val GL_FEEDBACK_BUFFER_SIZE = 0xDF1
        const val GL_FEEDBACK_BUFFER_TYPE = 0xDF2
        const val GL_SELECTION_BUFFER_POINTER = 0xDF3
        const val GL_SELECTION_BUFFER_SIZE = 0xDF4
        const val GL_TEXTURE_WIDTH = 0x1000
        const val GL_TEXTURE_HEIGHT = 0x1001
        const val GL_TEXTURE_INTERNAL_FORMAT = 0x1003
        const val GL_TEXTURE_BORDER_COLOR = 0x1004
        const val GL_TEXTURE_BORDER = 0x1005
        const val GL_DONT_CARE = 0x1100
        const val GL_FASTEST = 0x1101
        const val GL_NICEST = 0x1102
        const val GL_LIGHT0 = 0x4000
        const val GL_LIGHT1 = 0x4001
        const val GL_LIGHT2 = 0x4002
        const val GL_LIGHT3 = 0x4003
        const val GL_LIGHT4 = 0x4004
        const val GL_LIGHT5 = 0x4005
        const val GL_LIGHT6 = 0x4006
        const val GL_LIGHT7 = 0x4007
        const val GL_AMBIENT = 0x1200
        const val GL_DIFFUSE = 0x1201
        const val GL_SPECULAR = 0x1202
        const val GL_POSITION = 0x1203
        const val GL_SPOT_DIRECTION = 0x1204
        const val GL_SPOT_EXPONENT = 0x1205
        const val GL_SPOT_CUTOFF = 0x1206
        const val GL_CONSTANT_ATTENUATION = 0x1207
        const val GL_LINEAR_ATTENUATION = 0x1208
        const val GL_QUADRATIC_ATTENUATION = 0x1209
        const val GL_COMPILE = 0x1300
        const val GL_COMPILE_AND_EXECUTE = 0x1301
        const val GL_CLEAR = 0x1500
        const val GL_AND = 0x1501
        const val GL_AND_REVERSE = 0x1502
        const val GL_COPY = 0x1503
        const val GL_AND_INVERTED = 0x1504
        const val GL_NOOP = 0x1505
        const val GL_XOR = 0x1506
        const val GL_OR = 0x1507
        const val GL_NOR = 0x1508
        const val GL_EQUIV = 0x1509
        const val GL_INVERT = 0x150A
        const val GL_OR_REVERSE = 0x150B
        const val GL_COPY_INVERTED = 0x150C
        const val GL_OR_INVERTED = 0x150D
        const val GL_NAND = 0x150E
        const val GL_SET = 0x150F
        const val GL_EMISSION = 0x1600
        const val GL_SHININESS = 0x1601
        const val GL_AMBIENT_AND_DIFFUSE = 0x1602
        const val GL_COLOR_INDEXES = 0x1603
        const val GL_MODELVIEW = 0x1700
        const val GL_PROJECTION = 0x1701
        const val GL_TEXTURE = 0x1702
        const val GL_COLOR = 0x1800
        const val GL_DEPTH = 0x1801
        const val GL_STENCIL = 0x1802
        const val GL_COLOR_INDEX = 0x1900
        const val GL_STENCIL_INDEX = 0x1901
        const val GL_DEPTH_COMPONENT = 0x1902
        const val GL_RED = 0x1903
        const val GL_GREEN = 0x1904
        const val GL_BLUE = 0x1905
        const val GL_ALPHA = 0x1906
        const val GL_RGB = 0x1907
        const val GL_RGBA = 0x1908
        const val GL_LUMINANCE = 0x1909
        const val GL_LUMINANCE_ALPHA = 0x190A
        const val GL_BITMAP = 0x1A00
        const val GL_POINT = 0x1B00
        const val GL_LINE = 0x1B01
        const val GL_FILL = 0x1B02
        const val GL_RENDER = 0x1C00
        const val GL_FEEDBACK = 0x1C01
        const val GL_SELECT = 0x1C02
        const val GL_FLAT = 0x1D00
        const val GL_SMOOTH = 0x1D01
        const val GL_KEEP = 0x1E00
        const val GL_REPLACE = 0x1E01
        const val GL_INCR = 0x1E02
        const val GL_DECR = 0x1E03
        const val GL_VENDOR = 0x1F00
        const val GL_RENDERER = 0x1F01
        const val GL_VERSION = 0x1F02
        const val GL_EXTENSIONS = 0x1F03
        const val GL_S = 0x2000
        const val GL_T = 0x2001
        const val GL_R = 0x2002
        const val GL_Q = 0x2003
        const val GL_MODULATE = 0x2100
        const val GL_DECAL = 0x2101
        const val GL_TEXTURE_ENV_MODE = 0x2200
        const val GL_TEXTURE_ENV_COLOR = 0x2201
        const val GL_TEXTURE_ENV = 0x2300
        const val GL_EYE_LINEAR = 0x2400
        const val GL_OBJECT_LINEAR = 0x2401
        const val GL_SPHERE_MAP = 0x2402
        const val GL_TEXTURE_GEN_MODE = 0x2500
        const val GL_OBJECT_PLANE = 0x2501
        const val GL_EYE_PLANE = 0x2502
        const val GL_NEAREST = 0x2600
        const val GL_LINEAR = 0x2601
        const val GL_NEAREST_MIPMAP_NEAREST = 0x2700
        const val GL_LINEAR_MIPMAP_NEAREST = 0x2701
        const val GL_NEAREST_MIPMAP_LINEAR = 0x2702
        const val GL_LINEAR_MIPMAP_LINEAR = 0x2703
        const val GL_TEXTURE_MAG_FILTER = 0x2800
        const val GL_TEXTURE_MIN_FILTER = 0x2801
        const val GL_TEXTURE_WRAP_S = 0x2802
        const val GL_TEXTURE_WRAP_T = 0x2803
        const val GL_CLAMP = 0x2900
        const val GL_REPEAT = 0x2901
        const val GL_CLIENT_PIXEL_STORE_BIT = 0x1
        const val GL_CLIENT_VERTEX_ARRAY_BIT = 0x2
        const val GL_ALL_CLIENT_ATTRIB_BITS = -0x1
        const val GL_POLYGON_OFFSET_FACTOR = 0x8038
        const val GL_POLYGON_OFFSET_UNITS = 0x2A00
        const val GL_POLYGON_OFFSET_POINT = 0x2A01
        const val GL_POLYGON_OFFSET_LINE = 0x2A02
        const val GL_POLYGON_OFFSET_FILL = 0x8037
        const val GL_ALPHA4 = 0x803B
        const val GL_ALPHA8 = 0x803C
        const val GL_ALPHA12 = 0x803D
        const val GL_ALPHA16 = 0x803E
        const val GL_LUMINANCE4 = 0x803F
        const val GL_LUMINANCE8 = 0x8040
        const val GL_LUMINANCE12 = 0x8041
        const val GL_LUMINANCE16 = 0x8042
        const val GL_LUMINANCE4_ALPHA4 = 0x8043
        const val GL_LUMINANCE6_ALPHA2 = 0x8044
        const val GL_LUMINANCE8_ALPHA8 = 0x8045
        const val GL_LUMINANCE12_ALPHA4 = 0x8046
        const val GL_LUMINANCE12_ALPHA12 = 0x8047
        const val GL_LUMINANCE16_ALPHA16 = 0x8048
        const val GL_INTENSITY = 0x8049
        const val GL_INTENSITY4 = 0x804A
        const val GL_INTENSITY8 = 0x804B
        const val GL_INTENSITY12 = 0x804C
        const val GL_INTENSITY16 = 0x804D
        const val GL_R3_G3_B2 = 0x2A10
        const val GL_RGB4 = 0x804F
        const val GL_RGB5 = 0x8050
        const val GL_RGB8 = 0x8051
        const val GL_RGB10 = 0x8052
        const val GL_RGB12 = 0x8053
        const val GL_RGB16 = 0x8054
        const val GL_RGBA2 = 0x8055
        const val GL_RGBA4 = 0x8056
        const val GL_RGB5_A1 = 0x8057
        const val GL_RGBA8 = 0x8058
        const val GL_RGB10_A2 = 0x8059
        const val GL_RGBA12 = 0x805A
        const val GL_RGBA16 = 0x805B
        const val GL_TEXTURE_RED_SIZE = 0x805C
        const val GL_TEXTURE_GREEN_SIZE = 0x805D
        const val GL_TEXTURE_BLUE_SIZE = 0x805E
        const val GL_TEXTURE_ALPHA_SIZE = 0x805F
        const val GL_TEXTURE_LUMINANCE_SIZE = 0x8060
        const val GL_TEXTURE_INTENSITY_SIZE = 0x8061
        const val GL_PROXY_TEXTURE_1D = 0x8063
        const val GL_PROXY_TEXTURE_2D = 0x8064
        const val GL_TEXTURE_PRIORITY = 0x8066
        const val GL_TEXTURE_RESIDENT = 0x8067
        const val GL_TEXTURE_BINDING_1D = 0x8068
        const val GL_TEXTURE_BINDING_2D = 0x8069
        const val GL_VERTEX_ARRAY = 0x8074
        const val GL_NORMAL_ARRAY = 0x8075
        const val GL_COLOR_ARRAY = 0x8076
        const val GL_INDEX_ARRAY = 0x8077
        const val GL_TEXTURE_COORD_ARRAY = 0x8078
        const val GL_EDGE_FLAG_ARRAY = 0x8079
        const val GL_VERTEX_ARRAY_SIZE = 0x807A
        const val GL_VERTEX_ARRAY_TYPE = 0x807B
        const val GL_VERTEX_ARRAY_STRIDE = 0x807C
        const val GL_NORMAL_ARRAY_TYPE = 0x807E
        const val GL_NORMAL_ARRAY_STRIDE = 0x807F
        const val GL_COLOR_ARRAY_SIZE = 0x8081
        const val GL_COLOR_ARRAY_TYPE = 0x8082
        const val GL_COLOR_ARRAY_STRIDE = 0x8083
        const val GL_INDEX_ARRAY_TYPE = 0x8085
        const val GL_INDEX_ARRAY_STRIDE = 0x8086
        const val GL_TEXTURE_COORD_ARRAY_SIZE = 0x8088
        const val GL_TEXTURE_COORD_ARRAY_TYPE = 0x8089
        const val GL_TEXTURE_COORD_ARRAY_STRIDE = 0x808A
        const val GL_EDGE_FLAG_ARRAY_STRIDE = 0x808C
        const val GL_VERTEX_ARRAY_POINTER = 0x808E
        const val GL_NORMAL_ARRAY_POINTER = 0x808F
        const val GL_COLOR_ARRAY_POINTER = 0x8090
        const val GL_INDEX_ARRAY_POINTER = 0x8091
        const val GL_TEXTURE_COORD_ARRAY_POINTER = 0x8092
        const val GL_EDGE_FLAG_ARRAY_POINTER = 0x8093
        const val GL_V2F = 0x2A20
        const val GL_V3F = 0x2A21
        const val GL_C4UB_V2F = 0x2A22
        const val GL_C4UB_V3F = 0x2A23
        const val GL_C3F_V3F = 0x2A24
        const val GL_N3F_V3F = 0x2A25
        const val GL_C4F_N3F_V3F = 0x2A26
        const val GL_T2F_V3F = 0x2A27
        const val GL_T4F_V4F = 0x2A28
        const val GL_T2F_C4UB_V3F = 0x2A29
        const val GL_T2F_C3F_V3F = 0x2A2A
        const val GL_T2F_N3F_V3F = 0x2A2B
        const val GL_T2F_C4F_N3F_V3F = 0x2A2C
        const val GL_T4F_C4F_N3F_V4F = 0x2A2D
        const val GL_LOGIC_OP = 0xBF1
        const val GL_TEXTURE_COMPONENTS = 0x1003
    }

}