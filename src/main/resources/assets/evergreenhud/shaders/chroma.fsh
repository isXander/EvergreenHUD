#version 120

uniform int u_time;
uniform float u_speed;
uniform float u_frequency;

varying vec2 texcoord;

vec3 hsb2rgb(vec3 c) {
    vec3 rgb = clamp(abs(mod(c.x*6.0+vec3(0.0,4.0,2.0), 6.0)-3.0)-1.0, 0.0, 1.0);
    rgb = rgb*rgb*(3.0-2.0*rgb);
    return c.z * mix(vec3(1.0), rgb, c.y);
}

void main() {
    float c = mod((u_frequency * (-2.0 * texcoord.x + texcoord.y)) / 10.0, 1.0);//clamp(, 0.0, 1.0);

    vec3 rgb = hsb2rgb(vec3(mod(c + (u_time / u_speed), 1.0), 0.8, 0.8));

    vec4 color = vec4(rgb.xyz, 1.0);

    gl_FragColor = gl_Color * color;
}
