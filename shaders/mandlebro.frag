uniform float time;
uniform vec2 resolution;
#define ITER 50
#define PI 3.1415


float mandlebrot(vec2 pos) {
    vec2 z = vec2(0);
    for(int i=1; i<ITER; i++) {
        z = vec2(z.x*z.x-z.y*z.y, 2.*z.x*z.y) + pos;
        if(length(z) > 2.) {
            return float(i);
        }
    }
    return float(ITER);
}

void main(void)
{
	vec2 pos = gl_FragCoord.xy / resolution;
    pos -= .5;
    pos *= 2.;
    float rev = time/2000.;
    float posiOsc = .5*(1.-cos(PI*fract(rev)));
    float posiOsc2 = .5*(1.-cos(PI*rev));
    float thres = float(ITER)*posiOsc;
    float mandy = mandlebrot(pos);
    float mandyWrap = 1./max(1., thres-mandy);
    float mandyActual = float(ITER)-mandy < .1 ? 1. : 0.;
    vec4 col1 = vec4(1,0,0,1);
    vec4 col2 = vec4(1,1,0,1);
    vec4 actualColor = mix(col2, col1, posiOsc2);
    vec4 backgroundColor = mod(rev, 2.) < 1. ? col2 : col1;
    vec4 wrapColor = mod(rev, 2.) < 1. ? col1 : col2;
    gl_FragColor = mix(mix(backgroundColor, wrapColor, mandyWrap), actualColor, mandyActual);
}