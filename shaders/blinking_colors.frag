uniform float time;
uniform vec2 resolution;
#define PI 3.1415
void main() {
	float offset = length(gl_FragCoord.xy/resolution-.5)*(1.5+sin(time/3000.));
	gl_FragColor = (1.-offset)*vec4(.5*(1.+sin(time/500.)),.5*(1.+sin(time/500.+PI/3.)),.5*(1.+sin(time/500.+2.*PI/3.)),0); 
}