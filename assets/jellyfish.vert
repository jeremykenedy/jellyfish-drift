precision mediump float;
attribute vec4 aParam;
uniform mat4 uModel;
uniform mat4 uVP;
uniform float uTime;
uniform float uPhase;
uniform float uSpecies;
uniform float uScale;
uniform float uPixelWorld;
uniform vec3 uShape;
varying vec3 vNormal;
varying vec3 vPosition;
varying vec4 vParam;
const float PI=3.14159265;
vec3 oralPoint(float u,float q,float strand,float t){
 float moon=1.0-step(0.5,uSpecies);
 float length=mix(1.12,0.43,moon)*(0.85+0.15*sin(strand*3.0+1.7));
 float envelope=(0.37+0.63*sin(PI*pow(q,0.72)))*(1.0-q*0.58);
 float width=mix(0.145,0.108,moon)*envelope;
 float edge=pow(abs(u),2.0);
 float frill=1.0+edge*(0.28*sin(q*63.0+strand)+0.17*sin(q*111.0-strand*2.0));
 float twist=strand+q*4.5+0.28*sin(q*11.0-t*0.32);
 vec3 p=vec3(0.12*cos(strand)+q*0.15*sin(t*0.61-q*5.1+strand),
  -q*length,0.12*sin(strand)+q*0.13*sin(t*0.47-q*6.0-strand));
 p+=vec3(cos(twist),0.0,sin(twist))*u*width*frill;
 p.y+=edge*0.023*sin(q*72.0+u*3.0+strand)+0.022*sin(u*9.0+q*19.0+t*0.3);
 p.z+=0.025*sin(u*7.0+q*23.0+strand)*sin(PI*q);
 return p;
}
void main(){
 float t=uTime+uPhase;
 float contraction=pow(0.5+0.5*sin(t*1.38*uShape.z),3.0);
 float phi=aParam.x*2.0*PI;
 float theta=aParam.y*PI*0.5;
 float moon=1.0-step(0.5,uSpecies);
 float purple=step(1.5,uSpecies);
 vec3 p;
 vec3 n;
 float filamentWidth=0.0;
 if(aParam.z<0.5){
  float radial=aParam.y;
  float breadth=1.0-contraction*(0.09+0.15*pow(radial,3.0));
  float scallop=1.0+(0.017*cos(phi*16.0+0.25*sin(phi*3.0+uPhase))+0.020*sin(phi*3.0+uPhase))*pow(radial,5.0);
  float radius=0.51*uShape.x*breadth*sin(theta)*scallop;
  float height=mix(0.36,0.185,moon)*mix(1.0,0.85,purple)*uShape.y;
  float profile=pow(max(0.0,cos(theta)),mix(0.94,0.63,moon));
  p=vec3(radius*cos(phi),height*profile*(1.0+contraction*0.22),radius*sin(phi));
  p.y+=0.016*sin(phi*8.0+t*0.35)*pow(radial,7.0)-0.035*contraction*pow(radial,4.0);
  n=normalize(vec3(sin(theta)*cos(phi),mix(1.5,2.1,moon)*cos(theta),sin(theta)*sin(phi)));
 }else if(aParam.z<1.5){
  float strand=aParam.w*2.0*PI;
  strand+=0.025*sin(strand*13.0+uPhase);
  float q=aParam.y;
  float length=mix(1.48,0.17,moon)*(0.76+0.14*sin(strand*11.0+2.0)+0.10*sin(strand*23.0+uPhase));
  float attach=0.50*uShape.x*(1.0-contraction*0.22);
  float delayed=t*0.69-q*4.4+strand*1.7+0.4*sin(strand*9.0);
  float sway=mix(0.14,0.027,moon);
  p=vec3(attach*cos(strand)+sin(delayed)*q*sway,
   -q*length+0.01*sin(strand*11.0),attach*sin(strand)+sin(t*0.48-q*7.2+strand)*q*sway*0.8);
  filamentWidth=max(0.0048*(1.0-q*0.72),uPixelWorld*0.78/uScale);
  n=vec3(0.0,0.0,1.0);
 }else{
  float strand=aParam.w*2.0*PI;
  p=oralPoint(aParam.x,aParam.y,strand,t);
  vec3 tangentU=oralPoint(aParam.x+0.012,aParam.y,strand,t)-p;
  vec3 tangentV=oralPoint(aParam.x,min(1.0,aParam.y+0.005),strand,t)-oralPoint(aParam.x,max(0.0,aParam.y-0.005),strand,t);
  n=normalize(cross(tangentU,tangentV));
 }
 vec4 world=uModel*vec4(p,1.0);
 if(aParam.z>0.5 && aParam.z<1.5){
  world.x+=aParam.x*filamentWidth*uScale;
  vNormal=normalize(vec3(aParam.x*0.65,0.0,0.85));
 }else vNormal=mat3(uModel)*n;
 vPosition=world.xyz;
 vParam=aParam;
 gl_Position=uVP*world;
}
