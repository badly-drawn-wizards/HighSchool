uniform vec2 resolution;
void main() {
	// normalize the coordinates from pixel dimensions to unit dimensions
	vec2 position = gl_FragCoord.xy/resolution; 
	// GLSL colors are represented as 4-dimensional vectors containing red, blue, green and opacity values ranging from 0.0 to 1.0
	gl_FragColor = vec4(position.x);
}