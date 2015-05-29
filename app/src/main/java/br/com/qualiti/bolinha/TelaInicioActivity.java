package br.com.qualiti.bolinha;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

public class TelaInicioActivity extends Activity implements OnClickListener {

	RadioGroup radioGroup;
	Button btJogar;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.tela_inicio);

		radioGroup = (RadioGroup) findViewById(R.id.radioGroup1);

		btJogar = (Button) findViewById(R.id.btJogar);
		btJogar.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {

		iniciarJogo();

	}

	private void iniciarJogo() {

		Bundle b = new Bundle();
		int idBtChecado = radioGroup.getCheckedRadioButtonId();

		RadioButton btChecado = (RadioButton) findViewById(idBtChecado);
		EditText editTextNome = (EditText) findViewById(R.id.editTextNome);		
		b.putString("nivel", btChecado.getText().toString().toUpperCase());
		b.putString("nome", editTextNome.getText().toString());

		Intent it = new Intent(this, BolinhaActivity.class);

		it.putExtra("dados", b);
		
		startActivity(it);
	}
	
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		
		menu.add(0, 1, 0, "Records").setIcon(R.drawable.icon);
		
		return super.onCreateOptionsMenu(menu);
	}
	
	@Override
	public boolean onMenuItemSelected(int featureId, MenuItem item) {
		// TODO Auto-generated method stub
		if(item.getItemId() == 1){			
			Toast.makeText(this, "Menu1 Clicado", Toast.LENGTH_SHORT).show();
		}
		return super.onMenuItemSelected(featureId, item);
		
	}
	
}