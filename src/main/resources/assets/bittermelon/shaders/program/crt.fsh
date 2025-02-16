#version 150

uniform sampler2D DiffuseSampler;

in vec2 texCoord;
in vec2 oneTexel;

uniform float Intensity;
uniform float Speed;
uniform float Time;
uniform vec4 Region;

out vec4 fragColor;

void main() {
    vec4 color = texture(DiffuseSampler, texCoord);

    float segments = 5.0;

    // Create a moving scanline using Time, but snap to segments
    float time = Speed * Time;
    float rawPos = mod(time, 1.0);
    // Round to nearest segment position
    float scanlinePos = floor(rawPos * segments) / segments;

    // Calculate distance from current pixel to scanline
    float dist = abs(texCoord.y - scanlinePos);

    // Create a hard cutoff for rectangular look
    float thickness = 0.1;
    float scanline = (dist < thickness) ? 1.0 : 0.0;

    // Apply the scanline effect
    vec4 scanlineColor = vec4(1.0, 1.0, 1.0, 1.0);
    color = mix(color, scanlineColor, scanline * Intensity);

    fragColor = vec4(color.rgb, 1);
}