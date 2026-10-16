precision mediump float;
uniform float uLight;
uniform float uSpecies;
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
 vec3 n=normalize(vNormal);
 vec3 eye=normalize(vec3(0.0,0.0,5.2)-vPosition);
 float facing=abs(dot(n,eye));
 float fresnel=pow(1.0-facing,2.8);
 vec3 light=normalize(vec3(-0.35,0.8,0.6));
 float diffuse=abs(dot(n,light));
 float spec=pow(abs(dot(reflect(-light,n),eye)),45.0);
 vec3 tint=vec3(0.67,0.83,0.91);
 if(uSpecies>0.5) tint=vec3(0.95,0.64,0.39);
 if(uSpecies>1.5) tint=vec3(0.80,0.75,0.91);
 float alpha;
 vec3 color;
 if(vParam.z<0.5){
  float phi=vParam.x*2.0*PI;
  float radial=vParam.y;
  float branch=sin(radial*23.0+phi*3.0)*0.09;
  float ribs=pow(0.5+0.5*cos(phi*32.0+branch),36.0);
  float canal=ribs*smoothstep(0.16,0.60,radial);
  float capillary=pow(0.5+0.5*cos(phi*128.0+sin(radial*45.0)*0.7),16.0)*smoothstep(0.52,0.95,radial);
  float rim=pow(radial,38.0);
  float stripe=pow(0.5+0.5*cos(phi*16.0),7.0)*smoothstep(0.22,0.65,radial);
  vec2 uv=vec2(cos(phi),sin(phi))*sin(radial*PI*0.5);
  float tissue=0.0;
  for(int i=0;i<4;i++){
   float a=float(i)*PI*0.5+PI*0.25;
   vec2 center=vec2(cos(a),sin(a))*0.29;
   vec2 delta=uv-center;
   float d=length(delta*vec2(1.0,1.12));
   float opening=smoothstep(-0.035,0.015,dot(delta,normalize(center)));
   tissue+=exp(-pow((d-0.112)*26.0,2.0))*opening;
  }
  float detail=canal*0.07+capillary*0.035+rim*0.32;
  float pigment=uSpecies>0.5?stripe*0.16:0.0;
  vec2 materialUV=vec2(0.50+uv.x*0.19,0.17+uv.y*0.075);
  vec4 material=texture2D(uTissue,materialUV);
  float materialLuma=dot(material.rgb/max(0.15,material.a),vec3(0.333));
  float micro=tissueNoise(uv)*0.009+(materialLuma-0.65)*0.12;
  tissue*=1.0-step(0.5,uSpecies);
  float gel=0.14+0.20*pow(facing,1.6);
  alpha=gel+fresnel*0.34+detail+pigment+tissue*0.19+micro;
  color=tint*(0.74+diffuse*0.40)+vec3(0.62,0.85,0.98)*(detail+spec*0.75);
  color=mix(color,color*vec3(0.91,0.67,0.91),pigment*3.0*step(1.5,uSpecies));
  color+=tissue*vec3(0.20,0.14,0.09);
  color*=0.85+materialLuma*0.23;
 }else{
  float oral=step(1.5,vParam.z);
  float edge=pow(max(0.0,1.0-abs(vParam.x)),0.65);
  float folds=0.5+0.5*sin(vParam.y*77.0+vParam.w*35.0+vParam.x*5.0);
  float micro=tissueNoise(vec2(vParam.x*0.3,vParam.y*2.0));
  alpha=mix(0.58,0.32+folds*0.21+micro*0.05,oral)*edge*(1.0-vParam.y*0.75);
  color=tint*(0.91+diffuse*0.26)+spec*0.26;
 }
 // Bright-water tissue absorbs color without turning the whole animal into a dark silhouette.
 color=mix(color,color*vec3(0.82,0.77,0.72),uLight);
 alpha*=uDepth;
 gl_FragColor=vec4(color*alpha,alpha);
}
