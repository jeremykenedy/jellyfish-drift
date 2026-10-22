precision mediump float;
uniform float uTime;
uniform float uLight;
uniform float uRays;
uniform float uAspect;
varying vec2 vUV;
float hash(vec2 p){return fract(sin(dot(p,vec2(127.1,311.7)))*43758.5453);}
void main(){
 vec2 uv=vUV;
 vec3 dark=mix(vec3(0.003,0.013,0.028),vec3(0.018,0.095,0.14),pow(uv.y,2.3));
 vec3 light=mix(vec3(0.20,0.43,0.48),vec3(0.67,0.84,0.82),uv.y);
 vec3 col=mix(dark,light,uLight);
 float beam=0.0;
 for(int i=0;i<5;i++){
  float x=0.1+float(i)*0.24+sin(uTime*0.04+float(i))*0.025;
  float d=abs(uv.x-x-(1.0-uv.y)*0.12);
  beam+=exp(-d*d*350.0)*pow(uv.y,2.0);
 }
 col+=vec3(0.08,0.16,0.18)*beam*uRays;
 vec2 cell=vec2(uv.x*uAspect,uv.y+uTime*0.004)*55.0;
 vec2 id=floor(cell);
 vec2 p=fract(cell)-vec2(hash(id),hash(id+9.0));
 float speck=exp(-dot(p,p)*230.0)*step(0.93,hash(id+3.0));
 col+=speck*mix(vec3(0.09,0.16,0.18),vec3(-0.07),uLight);
 float vignette=1.0-0.22*length((uv-0.5)*vec2(1.0,0.65));
 gl_FragColor=vec4(col*vignette,1.0);
}
