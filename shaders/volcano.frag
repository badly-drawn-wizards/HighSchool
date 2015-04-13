uniform float time;
uniform vec2 resolution;
//#define time iGlobalTime*1000.
//#define resolution iResolution.xy
#define PI 3.1415

float smin( float a, float b, float k )
{
    float res = exp( -k*a ) + exp( -k*b );
    return -log( res )/k;
}
float smax( float a, float b, float k ) {
    return -smin(-a,-b,k);
}

vec2 r(vec2 x)
{
	return fract(sin(vec2(dot(x,vec2(127.1,311.7)),dot(x,vec2(269.5,183.3))))*43758.5453);
}

vec2 sr2(vec2 x) {
    vec2 floored = floor(x);
    vec2 fracted = sin(.5*PI*fract(x));
    return mix(mix(r(floored), r(floored+vec2(1,0)), fracted.x),
              mix(r(floored+vec2(0,1)), r(floored+vec2(1,1)), fracted.x),
              fracted.y);       
}

vec2 sr1(float x) {
    return sr2(vec2(0,x));
}

float volcano (vec2 pos) {
   	return smax(pos.y+.1*length(sr1(25.*pos.x))-3.,pos.y-1./pow(pos.x,2.), 1.);
}

float volcanoLava (vec2 pos) {
    return abs(pos.x + .01*pos.y*(.5-length(sr1(pos.y-time/100.))))-.4;
}

float lavaBall (vec2 pos) {
    pos -= vec2(0, -time/200.);
    vec2 mods = mod(pos, vec2(2.,3.));
    vec2 divs = floor(pos/vec2(2.,3.));
    pos = mods;
    pos-=vec2(.1,.1)+vec2(1.8,2.8)*sr2(divs);
    return length(pos)-.1;
}



void main(void)
{
	vec2 uv = gl_FragCoord.xy / resolution;
    uv-=vec2(.5,0.);
    vec4 backgroundCol = mix(vec4(1,.4,0,1), mix(vec4(.8,.5,0.,1.), vec4(.1,0,0,1), min(1.,uv.y)), min(1.,4.*uv.y));
    uv*=5.;
    uv+=0.1*sr1(time/100.);
    vec4 lavaBallCol = vec4(1,.5,0,1);
    float lavaBallMul = lavaBall(uv)<0. ? 1. : 0.;
    vec4 volcanoLavaCol = mix(vec4(1,1,0,1), vec4(1,0,0,1), length(sr1(uv.x*25.-uv.y+0.1*sin(5.*uv.y)+time/200.)));
    float volcanoLavaMul = float(volcanoLava(uv)<0.);
    vec4 volcanoCol = .25*(3.+length(sr2(uv*10.)))*vec4(171./256.,106./256.,32./256., 1.);
    float volcanoMul = float(volcano(uv) < 0.);
	gl_FragColor = mix(
        mix(mix(backgroundCol,
                lavaBallCol, lavaBallMul),
            volcanoLavaCol, volcanoLavaMul)
        ,volcanoCol, volcanoMul);
}