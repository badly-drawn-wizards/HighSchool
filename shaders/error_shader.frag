// This shader runs whenever there's an error like with the following nonsensical code
asdfasdf
// You can comment out the garbage and use this valid code instead
// uniform float time; uniform vec2 resolution; void main() { vec2 pos = gl_FragCoord.xy/resolution-.5; pos*=10.; float mul = .5*(1.+sin(length(pos)-time/1000.)); gl_FragColor = mul*vec4(1,0,0,0); }