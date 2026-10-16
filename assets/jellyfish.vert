precision mediump float;
attribute vec4 aParam;
uniform mat4 uModel;
uniform mat4 uVP;
uniform float uTime;
uniform float uPhase;
uniform float uSpecies;
uniform float uScale;
uniform float uPixelWorld;
varying vec3 vNormal;
varying vec3 vPosition;
varying vec4 vParam;
const float PI=3.14159265;
void main(){
 float t=uTime+uPhase;
 float contraction=pow(0.5+0.5*sin(t*1.45),3.0);
 float breadth=1.0-0.19*contraction;
 float phi=aParam.x*2.0*PI;
 float theta=aParam.y*PI*0.5;
 vec3 p;
 vec3 n;
 if(aParam.z<0.5){
  float scallop=1.0+0.014*cos(phi*32.0)*pow(aParam.y,8.0);
  float radius=0.51*breadth*sin(theta)*scallop;
  float height=mix(0.24,0.37,min(uSpecies,1.0))+0.075*contraction;
  p=vec3(radius*cos(phi),height*cos(theta),radius*sin(phi));
  p.y+=0.013*sin(phi*16.0+t)*pow(aParam.y,6.0);
  n=normalize(vec3(sin(theta)*cos(phi),1.65*cos(theta),sin(theta)*sin(phi)));
 }else{
  float strand=aParam.w*2.0*PI;
  float q=aParam.y;
  float oral=step(1.5,aParam.z);
  float length=mix(mix(0.48,1.28,min(uSpecies,1.0)),0.93,oral);
  length*=0.82+0.18*sin(strand*7.0+2.0);
  float attach=mix(0.495*breadth,0.13,oral);
  float wave=sin(t*0.72-q*4.1+strand*2.0);
  float wave2=sin(t*0.49-q*8.0+strand);
  float width=mix(0.0062,0.068,oral)*(1.0-q*0.78);
  width=max(width,(1.0-oral)*uPixelWorld*0.65/uScale);
  float ruffle=1.0+oral*0.37*sin(q*47.0+t+strand);
  p=vec3(attach*cos(strand)+wave*q*0.11,
    -q*length+0.012*sin(strand*11.0),attach*sin(strand)+wave2*q*0.10);
  p.x+=aParam.x*width*ruffle*cos(strand+q*5.0);
  p.z+=aParam.x*width*ruffle*sin(strand+q*5.0);
  n=normalize(vec3(sin(strand+q*5.0),0.18,cos(strand+q*5.0)));
 }
 vec4 world=uModel*vec4(p,1.0);
 vPosition=world.xyz;
 vNormal=mat3(uModel)*n;
 vParam=aParam;
 gl_Position=uVP*world;
}
