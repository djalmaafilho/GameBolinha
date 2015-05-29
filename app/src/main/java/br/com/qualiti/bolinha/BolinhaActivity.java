package br.com.qualiti.bolinha;


import java.io.IOException;
import java.util.Observable;
import java.util.Observer;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.os.Bundle;
import android.os.Handler;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.TextView;
import android.widget.Toast;

public class BolinhaActivity extends Activity implements SensorEventListener,
		Runnable, CustomViewEventoListener{
	private CustomView customView;
	private SensorManager sensorManager;
	private int minuto;
	private int segundo;
	private TextView txtRelogio;
	private TextView txtNome;
	private TextView txtPontos;
	private Handler handler;
	private WakeLock wakeLock;
	private SoundManager sm;
	private boolean customViewPausada;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		setVolumeControlStream(AudioManager.STREAM_MUSIC);
		sm = SoundManager.getInstance(this);
		sm.addSound(R.raw.beep4);

		txtNome = (TextView) findViewById(R.id.textViewNome);
		txtPontos = (TextView) findViewById(R.id.textViewPontos);
		customView = (CustomView) findViewById(R.id.customView1);

		Intent it = getIntent();
		if (it != null) {
			Bundle b = it.getBundleExtra("dados");

			txtNome.setText(b.getString("nome"));
			txtPontos.setText("0");

			// criar niveis

			Toast.makeText(this, "Nivel: " + b.getString("nivel"),
					Toast.LENGTH_SHORT).show();

			if (b.getString("nivel").equals(Nivel.MEDIO.name())) {
				customView.setNivel(Nivel.MEDIO);
			} else if (b.getString("nivel").equals(Nivel.DIFICIL.name())) {
				customView.setNivel(Nivel.DIFICIL);
			} else {
				customView.setNivel(Nivel.FACIL);
			}

			customView.setVelocidadeComida(customView.getNivel()
					.getVelodidade());
			customView.addCustomEventoListener(this);
		}

		txtRelogio = (TextView) findViewById(R.id.textViewRelogio);
		handler = new Handler();
		sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);

		sensorManager.registerListener(this,
				sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
				SensorManager.SENSOR_DELAY_GAME);

		new Handler().post(this);
	}

	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();

		// Obtém a instância do PowerManager
		PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);

		// Liga o display do aparelho
		wakeLock = pm.newWakeLock(
		// Liga a tela
				PowerManager.SCREEN_DIM_WAKE_LOCK |
				// Após liberar a tela para apagar,
				// mantém a tela ligada por um pouco
				// mais de tempo
						PowerManager.ON_AFTER_RELEASE,
				// Tag para debug
				"DJPS");

		// Liga a tela por 10 segundos
		wakeLock.acquire();
	}

	@Override
	protected void onStop() {

		super.onStop();
		if (wakeLock != null) {
			wakeLock.release();
		}

		//para a chamada a runnable
		handler.removeCallbacks(this);
	}

	
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		sensorManager.unregisterListener(this);
	}
	
	@Override
	public void onAccuracyChanged(Sensor sensor, int accuracy) {

	}

	@Override
	public void onSensorChanged(SensorEvent event) {
		String strValores = "";
		float[] valores = event.values;
		
		for (int i = 0; i < valores.length; i++) {
			strValores += "[" + valores[i] + "]";
		}
		Log.i("DJPS", "NOME " + event.sensor.getName() + " TIPO "
				+ event.sensor.getType() + " : " + strValores);

		customView.atualizarJogo(valores[0], valores[1]);
		txtPontos.setText("" + customView.getPontos());
		if (customView.getAcertouComida()) {
			sm.playSound(0);
		}
	}

	@Override
	public void run() {
		if(!customViewPausada){
			atualizarRelogio();
		}
		handler.postDelayed(this, 1000);
	}

	private void atualizarRelogio() {

		if (segundo == 60) {
			minuto++;
			segundo = 0;
		}

		if (minuto == 60) {
			minuto = 0;
			segundo = 0;
		}

		txtRelogio.setText((minuto < 10 ? "0" + minuto : "" + minuto) + ":"
				+ (segundo < 10 ? "0" + segundo : "" + segundo));

		segundo++;
	}

	@Override
	public void customViewPausada(CustomViewEvento e) {
		customViewPausada = (Boolean) e.getData();		
	}
}