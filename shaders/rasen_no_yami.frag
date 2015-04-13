uniform float time;
uniform vec2 resolution;
#define PI 3.1415

mat2 rotate(float theta) {
	return mat2(
		cos(theta), -sin(theta),
		sin(theta), cos(theta)
	);
}

float grid(vec2 pos) {
	vec2 val = sin(pos);
	return max(val.x, val.y);
}

void main () {
	vec2 pos = gl_FragCoord.xy / resolution;
	pos-=.5;
	vec2 spiralPos = rotate(sin(time/1000.)*length(pos))*pos;
	spiralPos*=100.;
	float mul = min(1.+sin(time/500.+length(pos)),1.);
	gl_FragColor = mix(vec4(.5,0,0,0),mul*vec4(0,.5,0,0),grid(spiralPos));
}