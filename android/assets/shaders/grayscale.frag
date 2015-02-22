#ifdef GL_ES
#define LOWP lowp
precision mediump float;
#else
#define LOWP 
#endif
//texture 0
uniform sampler2D u_texture;

//our screen resolution, set from Java whenever the display is resized
uniform vec2 resolution;

//"in" attributes from our vertex shader
varying LOWP vec4 vColor;
varying vec2 vTexCoord;

//RADIUS of our vignette, where 0.5 results in a circle fitting the screen
const float RADIUS = 0.75;

//softness of our vignette, between 0.0 and 1.0
const float SOFTNESS = 1.00;

//sepia colour, adjust to taste
const vec3 SEPIA = vec3(0.8, 1.0, 0.8); 

void main() {
	//sample our texture
	vec4 texColor = texture2D(u_texture, vTexCoord);
		
	//1. VIGNETTE
	
	//determine center position
	vec2 position = (gl_FragCoord.xy / resolution.xy) - vec2(0.5);
	
	//determine the vector length of the center position
	float len = length(position);
	
	//use smoothstep to create a smooth vignette
	float vignette = smoothstep(RADIUS, RADIUS-SOFTNESS, len);
	
	//apply the vignette with 50% opacity
	texColor.rgb = mix(texColor.rgb, texColor.rgb * vignette, 0.5);
		
	//2. GRAYSCALE
	
	//convert to grayscale using NTSC conversion weights
	float gray = dot(texColor.rgb, vec3(0.299, 0.587, 0.114));
	
	//3. SEPIA
	
	//create our sepia tone from some constant value
	vec3 sepiaColor = vec3(gray) * SEPIA;
	vec3 grey = vec3(gray);
		
	//again we'll use mix so that the sepia effect is at 75%
	texColor.rgb = mix(texColor.rgb, grey, 1);
		
	//final colour, multiplied by vertex colour
	gl_FragColor = texColor * vColor;
}