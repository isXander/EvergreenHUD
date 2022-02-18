#version 120

varying vec2 texcoord;

void main() {
    gl_Position = gl_ModelViewProjectionMatrix * gl_Vertex;
    texcoord = gl_Position.xy;
    gl_FrontColor = gl_Color;
}
