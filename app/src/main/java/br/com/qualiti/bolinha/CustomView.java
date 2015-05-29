package br.com.qualiti.bolinha;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Observable;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.Toast;

public class CustomView extends View implements OnTouchListener {
	private boolean comidaParaBottom, comidaParaTop, comidaParaEsq,
			comidaParaDir;
	private static final int RAIO = 40;
	private float x, y;
	private int xComida, yComida;
	private Drawable bola, piso, comida;
	private int pontos, acertos;
	private Nivel nivel;
	private int velocidadeComida;
	private boolean acertouComida, jogoPausado;
	private Collection<CustomViewEventoListener> customVeiwlisteners;
	
	public CustomView(Context context, AttributeSet attrs) {
		super(context, attrs);
		bola = this.getResources().getDrawable(R.drawable.bola_verm);
		bola.setBounds(new Rect(0, 0, RAIO, RAIO));
		piso = this.getResources().getDrawable(R.drawable.piso);
		comida = this.getResources().getDrawable(R.drawable.comida);
		customVeiwlisteners =  new ArrayList<CustomViewEventoListener>();
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		piso.setBounds(canvas.getClipBounds());
		piso.draw(canvas);

		comida = recuperarComidaAtualizada(comida, canvas);
		comida.draw(canvas);

		bola = recuperarBolaAtualizada(bola, canvas);
		bola.draw(canvas);
		
	}

	private void atualizarStatus() {

		acertouComida = false;
		boolean pontuar = false;
		
		if (nivel.equals(Nivel.FACIL)
				&& bola.getBounds().contains(xComida, yComida)) {
			acertouComida = pontuar = true;
		} else if (nivel.equals(Nivel.MEDIO)
				&& bola.getBounds().contains(xComida, yComida)) {
			acertouComida = pontuar = true;

		} else if (nivel.equals(Nivel.DIFICIL)
				&& bola.getBounds().contains(xComida, yComida)) {
			
			acertouComida = true;
			if (acertos == 5) {
				pontuar = true;
				acertos = 0;
			} else {
				acertos++;
			}
		}

		if (acertouComida) {
			xComida = 0;
			yComida = 0;
			comidaParaDir = false;
			comidaParaEsq = false;
			comidaParaTop = false;
			comidaParaBottom = false;
		}
		
		if(pontuar){
			pontos++;
		}
		
	}

	private Drawable recuperarComidaAtualizada(Drawable comida, Canvas canvas) {

		if (xComida == canvas.getClipBounds().left
				&& yComida == canvas.getClipBounds().top) {
			xComida = canvas.getClipBounds().left
					+ ((int) (Math.random() * 1000) - (int) (Math.random() * 1000));
			yComida = canvas.getClipBounds().top
					+ ((int) (Math.random() * 1000) - (int) (Math.random() * 1000));
		}

		if (xComida < canvas.getClipBounds().left) {
			xComida = canvas.getClipBounds().left;
			comidaParaDir = true;
			comidaParaEsq = false;
		}

		if (yComida < canvas.getClipBounds().top) {
			yComida = canvas.getClipBounds().top;
			comidaParaTop = false;
			comidaParaBottom = true;

		}

		if (xComida > canvas.getClipBounds().right - comida.getBounds().width()) {
			xComida = canvas.getClipBounds().right - comida.getBounds().width();
			comidaParaDir = false;
			comidaParaEsq = true;

		}

		if (yComida > canvas.getClipBounds().bottom
				- comida.getBounds().width()) {
			yComida = canvas.getClipBounds().bottom
					- comida.getBounds().width();
			comidaParaTop = true;
			comidaParaBottom = false;
		}

		comida.setBounds(xComida, yComida, xComida + RAIO, yComida + RAIO);

		return comida;
	}

	private Drawable recuperarBolaAtualizada(Drawable bola, Canvas canvas) {

		if (x < canvas.getClipBounds().left) {
			x = canvas.getClipBounds().left;
		}

		if (y < canvas.getClipBounds().top) {
			y = canvas.getClipBounds().top;

		}

		if (x > canvas.getClipBounds().right - bola.getBounds().width()) {
			x = canvas.getClipBounds().right - bola.getBounds().width();
		}

		if (y > canvas.getClipBounds().bottom - bola.getBounds().width()) {
			y = canvas.getClipBounds().bottom - bola.getBounds().width();
		}

		bola.setBounds((int) x, (int) y, (int) x + bola.getBounds().width(),
				(int) y + bola.getBounds().height());
		return bola;
	}

	public void atualizarJogo(float aceleracaoX, float aceleracaoY) {

		if(jogoPausado == true){
			return;
		}
		
		this.x -= aceleracaoX;
		this.y += aceleracaoY;

		if (comidaParaTop == true) {
			yComida -= velocidadeComida;
		}

		if (comidaParaBottom == true) {
			yComida += velocidadeComida;
		}
		if (comidaParaDir == true) {
			xComida += velocidadeComida;
		}

		if (comidaParaEsq == true) {
			xComida -= velocidadeComida;
		}

		invalidate();
		atualizarStatus();		
	}

	public int getPontos() {
		return pontos;
	}

	public void setNivel(Nivel nivel) {
		this.nivel = nivel;
	}

	public void setVelocidadeComida(int velocidadeComida) {
		this.velocidadeComida = velocidadeComida;
	}

	public Nivel getNivel() {
		return nivel;
	}
	
	public boolean getAcertouComida(){
		
		return acertouComida;
	}
	

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		
		if(jogoPausado == false){
			jogoPausado = true;
			Toast.makeText(getContext(), "Jogo Pausado", Toast.LENGTH_SHORT).show();
		}else{
			jogoPausado = false;
		}
		
		 
		notificarPausaToListeners(new Boolean(jogoPausado));
		
		return super.onTouchEvent(event);
	}
	
	private void notificarPausaToListeners(Boolean statusPausa) {

		Collection<CustomViewEventoListener> customViewListener;
		synchronized (this) {
			customViewListener = (Collection) (((ArrayList) customVeiwlisteners).clone());
		}
		CustomViewEvento evento = new CustomViewEvento(this, statusPausa);
		
		for (CustomViewEventoListener customViewEventoListener : customViewListener) {
			customViewEventoListener.customViewPausada(evento);
		}		
	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		return true;
	}

	public void addCustomEventoListener(CustomViewEventoListener listenner){
		if(!this.customVeiwlisteners.contains(listenner)){
			this.customVeiwlisteners.add(listenner);
		}
	}
}