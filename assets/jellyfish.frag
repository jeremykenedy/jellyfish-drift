precision mediump float;
uniform float uLight;
uniform float uSpecies;
uniform float uPhase;
uniform float uDepth;
uniform sampler2D uTissue;
varying vec3 vNormal;
varying vec3 vPosition;
varying vec4 vParam;
const float PI=3.14159265;
float tissueNoise(vec2 p){
 return sin(p.x*137.0+sin(p.y*57.0))*sin(p.y*113.0+sin(p.x*39.0));
}
void main(){
 float moon=1.0-step(0.5,uSpecies);
 float purple=step(1.5,uSpecies);
 float nettle=1.0-moon-purple;
 vec3 n=normalize(vNormal);
 vec3 eye=normalize(vec3(0.0,0.0,5.2)-vPosition);
 float facing=abs(dot(n,eye));
 float fresnel=pow(1.0-facing,2.6);
 vec3 light=normalize(vec3(-0.35,0.8,0.6));
 float diffuse=abs(dot(n,light));
 float spec=pow(abs(dot(reflect(-light,n),eye)),42.0);
 vec3 tint=mix(vec3(0.69,0.83,0.92),vec3(0.72,0.30,0.12),nettle);
 tint=mix(tint,vec3(0.85,0.86,0.93),purple);
 vec3 glint=mix(vec3(0.75,0.89,0.98),vec3(1.0,0.78,0.45),nettle);
 float alpha;
 vec3 color;
 if(vParam.z<0.5){
  float phi=vParam.x*2.0*PI;
  float radial=vParam.y;
  vec2 uv=vec2(cos(phi),sin(phi))*sin(radial*PI*0.5);
  float warp=phi+0.034*sin(radial*17.0+phi*3.0+uPhase)+0.016*sin(phi*7.0-radial*31.0);
  float parentCanal=pow(0.5+0.5*cos(warp*8.0),22.0)*smoothstep(0.15,0.65,radial);
  float branches=pow(0.5+0.5*cos(warp*32.0+sin(radial*29.0+phi*5.0)*0.8),24.0)*smoothstep(0.45,0.95,radial);
  float capillary=pow(0.5+0.5*cos(warp*96.0+sin(radial*41.0)*1.2),13.0)*smoothstep(0.62,1.0,radial);
  float rim=pow(radial,36.0);
  float stripe=pow(0.5+0.5*cos(phi*16.0+0.15*sin(radial*8.0+uPhase)),4.0)*smoothstep(0.12,0.56,radial);
  float tissue=0.0;
  for(int i=0;i<4;i++){
   float a=float(i)*PI*0.5+PI*0.25;
   vec2 center=vec2(cos(a),sin(a))*0.29;
   vec2 delta=uv-center;
   float d=length(delta*vec2(1.0,1.12));
   float opening=smoothstep(-0.035,0.015,dot(delta,normalize(center)));
   tissue+=exp(-pow((d-0.112)*26.0,2.0))*opening;
  }
  tissue*=moon;
  float detail=parentCanal*0.025+branches*0.025+capillary*0.018+rim*0.23;
  vec2 materialUV=vec2(0.50+uv.x*0.19,0.17+uv.y*0.075);
  vec4 material=texture2D(uTissue,materialUV);
  float materialLuma=dot(material.rgb/max(0.15,material.a),vec3(0.333));
  float micro=tissueNoise(uv)*0.010+(materialLuma-0.65)*0.12;
  float gel=0.15+0.19*pow(facing,1.6)+nettle*0.17;
  alpha=gel+fresnel*0.31+detail+stripe*(purple*0.15+nettle*0.09)+tissue*0.18+micro;
  color=tint*(0.76+diffuse*0.36)+glint*(detail+spec*0.62);
  color=mix(color,vec3(0.28,0.12,0.46)*(0.85+diffuse*0.3),stripe*purple*0.82);
  color=mix(color,color*vec3(0.82,0.61,0.50),stripe*nettle*0.40);
  color+=tissue*vec3(0.20,0.14,0.09);
  color*=0.85+materialLuma*0.23;
 }else{
  float oral=step(1.5,vParam.z);
  float edge=pow(max(0.0,1.0-abs(vParam.x)),0.40);
  float fold=0.5+0.5*sin(vParam.y*77.0+vParam.w*35.0+vParam.x*7.0);
  float micro=tissueNoise(vec2(vParam.x*0.4,vParam.y*2.0));
  float lace=0.75+0.25*sin(vParam.y*183.0+vParam.x*19.0+micro*2.0);
  alpha=mix(0.67,0.38+fold*0.21+micro*0.05,oral)*edge*(1.0-vParam.y*0.65);
  alpha*=mix(1.0,lace,oral);
  color=tint*(0.85+diffuse*0.35)+glint*spec*0.34;
  color=mix(color,color*vec3(0.72,0.52,0.80),purple*(0.20+0.25*vParam.y));
 }
 color=mix(color,color*mix(vec3(0.89,0.87,0.84),vec3(0.80,0.66,0.60),nettle),uLight);
 alpha=min(0.92,alpha*uDepth);
 gl_FragColor=vec4(color*alpha,alpha);
}
