package mx.edu.ittepic.themickyebmo.tpdm_u4_practica2_maciasurzuadelia;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.CountDownTimer;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

import java.util.Random;

public class Lienzo extends View {
    int width,height;
    boolean activo, gano, perdio;
    Bitmap mosquita;
    Random posi;
    CountDownTimer decometro;
    Moscas[] arrayMosca,arrayMoscaJefe;
    int c_muerta,segundos,arreglo,jefe_muerte;


    public Lienzo(Context context) {
        super(context);
        activo=true;
        gano=perdio=false;
        arrayMosca = new Moscas[30];
        arrayMoscaJefe = new Moscas[5];
        posi = new Random();
        c_muerta =0;
        segundos = 60;
        arreglo=0;jefe_muerte=0;
        width = getResources().getSystem().getDisplayMetrics().widthPixels;
        height = getResources().getSystem().getDisplayMetrics().heightPixels-200;
        for(int i=0;i<arrayMosca.length;i++){
            arrayMosca[i] = new Moscas(this, R.drawable.mosca,(1+posi.nextInt(width-190)),(200+posi.nextInt(height-350)));
        }
        for (int y=0;y<arrayMoscaJefe.length;y++){
            arrayMoscaJefe[y] = new Moscas(this,R.drawable.mosquita,(1+posi.nextInt(width-730)),100+posi.nextInt(height-900));
        }

        decometro = new CountDownTimer(1000,1000) {
            @Override
            public void onTick(long millisUntilFinished) {

            }

            @Override
            public void onFinish() {
                if (!perdio) {
                    if (segundos < 1) {
                        perdio = true;
                        invalidate();
                    } else {
                        segundos--;
                        invalidate();
                        start();
                    }
                }
            }
        };
    }
    @Override
    protected void onDraw(Canvas canvas) {

            Paint p = new Paint();
            if (perdio && !gano) {
                p.setColor(Color.BLACK);
                p.setTextSize(100);
                canvas.drawText("LO SIENTO, PERDISTE!!",width/2-525,height/2,p);
            }else{
                if(gano){
                    p.setColor(Color.BLACK);
                    p.setTextSize(100);
                    canvas.drawText("Felicidades, GANASTE!!",width/2-525,height/2,p);
                }else {
                    if (activo) {
                        canvas.drawBitmap(arrayMosca[arreglo].imagen, arrayMosca[arreglo].x, arrayMosca[arreglo].y, p);
                        arrayMosca[arreglo].pintar(canvas, p);

                    } else {
                        canvas.drawBitmap(arrayMoscaJefe[jefe_muerte].imagen, arrayMoscaJefe[jefe_muerte].x, arrayMoscaJefe[jefe_muerte].y, p);
                        arrayMoscaJefe[jefe_muerte].pintar(canvas, p);
                        if (jefe_muerte == arrayMoscaJefe.length - 1) {
                            gano = true;
                        }
                    }
                }
            }



        //canvas.drawPaint(p);
        p.setColor(Color.BLACK);
        p.setTextSize(100);
        canvas.drawText(segundos+"",width/2,100,p);
        decometro.start();
    }

    public boolean onTouchEvent( MotionEvent me){
        int accion = me.getAction();
        int posx = (int)me.getX();
        int posy = (int)me.getY();

        switch(accion){
            case MotionEvent.ACTION_DOWN: //presiono
                if (!gano) {
                    if (activo && arrayMosca[arreglo].estaEnArea(posx, posy)) {
                        if (arreglo < arrayMosca.length) {
                            if (arreglo == arrayMosca.length - 1) {
                                activo = false;
                                segundos = 10;
                            }
                            arreglo++;
                        }
                    }
                    if (!activo && arrayMoscaJefe[jefe_muerte].estaEnArea(posx, posy)) {
                        if (jefe_muerte < arrayMoscaJefe.length) {
                            jefe_muerte++;
                        }
                    }
                }
                break;
            case MotionEvent.ACTION_MOVE:
                break;
            case MotionEvent.ACTION_UP:
                break;
        }

        invalidate();
        return true;
    }
}

