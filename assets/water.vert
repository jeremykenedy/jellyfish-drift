attribute vec4 aParam;
varying vec2 vUV;
void main(){vUV=aParam.xy*0.5+0.5;gl_Position=vec4(aParam.xy,0.99,1.0);}
