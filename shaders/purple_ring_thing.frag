uniform float time;
uniform vec2 resolution;

void main(void)
{
    float t = time/1000.;
	vec2 uv = gl_FragCoord.xy / resolution;
    uv *= 1.+sin(t);
    vec2 whatEvah = cos(uv);
    vec4 col1 = mix(vec4(1,0,1,1), vec4(1,0,0,1), abs(.5-fract(t)));
    uv -= .4-vec2(.1*sin(t),-.4*abs(.5-fract(t*2.)));
    col1 = mix(.25*(1.+sin(length(uv*40.)+t))*vec4(0,0,1,1), col1, .7);
	gl_FragColor = col1;
}