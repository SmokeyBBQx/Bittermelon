#version 150

uniform sampler2D DiffuseSampler;
uniform float Time;
uniform vec2 InSize;

in vec2 texCoord;
out vec4 fragColor;

float noise(vec2 p) {
    return sin(p.x*10.0) * sin(p.y*(3.0 + sin(Time/11.0))) + 0.2;
}

mat2 rotate(float angle) {
    return mat2(cos(angle), -sin(angle), sin(angle), cos(angle));
}

float fbm(vec2 p) {
    p *= 1.1;
    float f = 0.0;
    float amp = 0.5;
    for(int i = 0; i < 3; i++) {
        mat2 modify = rotate(Time/50.0 * float(i*i));
        f += amp*noise(p);
        p = modify * p;
        p *= 2.0;
        amp /= 2.2;
    }
    return f;
}

float pattern(vec2 p, out vec2 q, out vec2 r) {
    q = vec2(fbm(p + vec2(1.0)), fbm(rotate(0.1*Time)*p + vec2(1.0)));
    r = vec2(fbm(rotate(0.1)*q + vec2(0.0)), fbm(q + vec2(0.0)));
    return fbm(p + 1.0*r);
}

float onOff(float a, float b, float c) {
    return step(c, sin(Time + a*cos(Time*b)));
}

float displace(vec2 look) {
    float y = (look.y-mod(Time/4.0,1.0));
    float window = 1.0/(1.0+50.0*y*y);
    return sin(look.y*20.0 + Time)/80.0*onOff(4.0,2.0,0.8)*(1.0+cos(Time*60.0))*window;
}

void main() {
    vec2 p = texCoord;
    float bar = mod(p.y + Time*20.0, 1.0) < 0.2 ? 1.4 : 1.0;
    p.x += displace(p);

    vec2 q, r;
    float intensity = pattern(p/10.0, q, r)*1.3 - 0.03;
    vec3 col = vec3(intensity) * bar;

    // Mix with original texture
    vec4 texColor = texture(DiffuseSampler, p);
    fragColor = vec4(mix(texColor.rgb, col, 0.5), 1.0);
}