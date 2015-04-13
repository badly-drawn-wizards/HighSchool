uniform vec2 resolution;
uniform float time;

#define E vec2(0,0.01)
#define THICKNESS .1
float lr (vec2 c) {
	c*=100.;
	//Enter your expression here
	float theta = atan(c.y, c.x);
	float len = length(c);
	
	return sin(3.*c.x+time/100.)*cos(c.x)-c.y; // = 0
}

vec2 dlr (vec2 c) {
	return vec2(lr(c+E.yx)-lr(c-E.yx), lr(c+E.xy)-lr(c-E.xy));
}

float dist (vec2 c) {
	float d = length(dlr(c));
	return abs(lr(c)/d);
}

void main() {
vec2 pos = gl_FragCoord.xy/resolution-.5;
gl_FragColor = dist(pos)<THICKNESS ? vec4(1,0,0,1) : vec4(1);
}
