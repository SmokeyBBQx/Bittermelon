#version 150

uniform sampler2D DiffuseSampler;
uniform float Time;
uniform vec2 OutSize;

in vec2 texCoord;
in vec2 oneTexel;

out vec4 fragColor;

precision highp float;

// https://www.shadertoy.com/view/WcVXRy

mat2 rotate2D(float angle) {
    float s = sin(angle);
    float c = cos(angle);
    return mat2(c, -s, s, c);
}

float random(vec2 st) {
    return fract(sin(dot(st.xy, vec2(12.9898, 78.233))) * 43758.5453123);
}

float noise(vec2 st) {
    vec2 i = floor(st);
    vec2 f = fract(st);

    float a = random(i);
    float b = random(i + vec2(1.0, 0.0));
    float c = random(i + vec2(0.0, 1.0));
    float d = random(i + vec2(1.0, 1.0));

    vec2 u = f * f * (3.0 - 2.0 * f);

    return mix(a, b, u.x) + (c - a) * u.y * (1.0 - u.x) + (d - b) * u.x * u.y;
}

float fbm(vec2 st) {
    float value = 0.0;
    float amplitude = 0.5;
    for (int i = 0; i < 6; i++) {
        value += amplitude * noise(st);
        st *= 2.0;
        amplitude *= 0.5;
    }
    return value;
}

vec3 palette(float t) {
    vec3 a = vec3(0.5, 0.5, 0.5);
    vec3 b = vec3(0.5, 0.5, 0.5);
    vec3 c = vec3(1.0, 1.0, 1.0);
    vec3 d = vec3(0.0, 0.33, 0.67);

    return a + b * cos(6.28318 * (c * t + d));
}

void main() {
    vec2 fragCoord = texCoord * OutSize;
    vec2 uv = (fragCoord.xy * 2.0 - OutSize.xy) / OutSize.y;

    vec2 uv_offset1 = uv;
    uv_offset1 += vec2(sin(Time * 0.1), cos(Time * 0.15)) * 0.5;
    uv_offset1 *= rotate2D(Time * 0.2);

    vec2 uv_offset2 = uv;
    uv_offset2 += vec2(cos(Time * 0.08), sin(Time * 0.12)) * 0.7;
    uv_offset2 *= rotate2D(Time * -0.3);

    float noise1 = fbm(uv_offset1 * 5.0 + Time * 0.5);
    float noise2 = fbm(uv_offset2 * 4.0 + Time * 0.3);

    float combined_noise = (noise1 + noise2) * 0.5;
    combined_noise = pow(combined_noise, 1.5);

    vec3 color1 = palette(combined_noise + Time * 0.1);
    vec3 color2 = palette(combined_noise + Time * 0.1 + 0.33);
    vec3 color3 = palette(combined_noise + Time * 0.1 + 0.66);

    vec3 final_color = mix(color1, color2, combined_noise);
    final_color = mix(final_color, color3, sin(Time * 0.5) * 0.5 + 0.5);

    final_color = pow(final_color, vec3(1.2));
    final_color *= (0.8 + 0.2 * sin(Time * 3.0));

    fragColor = vec4(final_color, 1.0);
}