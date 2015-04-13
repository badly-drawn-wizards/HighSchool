uniform float time;
uniform vec2 resolution;

vec2 random(vec2 x)
{
	return fract(sin(vec2(dot(x,vec2(127.1,311.7)),dot(x,vec2(269.5,183.3))))*43758.5453);
}


vec2 randomCircle(vec2 x, float t) {
    vec2 randCenter =  random(x+random(vec2(0)));
    vec2 randRadii = min(randCenter, 1.-randCenter)*random(x+random(vec2(1)));
    float randSpeed = 2.*length(random(x+random(vec2(2))))-1.;
    t *= randSpeed;
    return randCenter+randRadii*vec2(cos(t),sin(t));
}

vec2 gridClosest(vec2 pos, vec2 prev) {
    vec2 center = floor(pos);
    vec2 closest = vec2(2.);
    for(int i=-1; i<=1; i++)
        for(int j=-1; j<=1; j++){
            vec2 point = center+vec2(i,j);
            point += randomCircle(point, time/1000.);
            if(prev==vec2(0) || point != prev && length(pos-prev) <= length(pos-point))
                if(length(pos-point) <= length(pos-closest)) {
                    closest = point;
                }
        }
    return closest;
}

float voronoi(vec2 pos, vec2 first, vec2 second) {
   	return abs(dot(normalize(first-second),pos-0.5*(first+second)));
}

void main(void)
{
    vec2 pos = gl_FragCoord.xy/resolution;
    float sTime = time/10000.;
    pos-=10.+mix(random(vec2(3)),random(vec2(5)), clamp((sTime-2.)/10.,0.,1.))*sTime ;
    pos*=5.;
    vec2 first = gridClosest(pos, vec2(0));
    vec2 second = gridClosest(pos, first);
    float vor = voronoi(pos, first, second);
    float clampDist = abs(min(length(pos-first),1.));
    vec4 center = .2/clampDist*vec4(1,1,0,1);
    float borderPulse = 1.-abs(.5-clamp(5.*(fract(sTime)-.8),0.,1.));
    vec4 border = max(0., 1.-vor)*mix(vec4(1,0,1,1),vec4(1,0,0,1),borderPulse);
    gl_FragColor = mix(center, border, .5);
}