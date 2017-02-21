package sylex.gdx;

import android.content.Intent;
import android.os.Bundle;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;

import fr.upmc.lexteksylux.radclient.RAD;
import fr.upmc.lexteksylux.radclient.interfaces.GameEventReceiver;
import sylex.androidClient.ConnectActivity;
import sylex.androidClient.LobbyActivity;
import sylex.model.ClientModel;

public class AndroidLauncher extends AndroidApplication implements GameEventReceiver {

	private RAD rad;

	@Override
	protected void onCreate (Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();
		config.useAccelerometer = false;
		config.useCompass = false;
		RAD rad;
		initialize((rad = new RAD(ClientModel.nbSec, this)), config);
		this.rad = rad;
		ClientModel.bindServerGameEndEvent(rad);
	}

	@Override
	public void onGameScoreUpdate(byte amount) {
		ClientModel.notifScoreTick(amount);
	}

	@Override
	public void onGameClose() {
		rad.dispose();
		Intent intent = new Intent(this, ConnectActivity.class);
		intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
		startActivity(intent);
		this.finish();
		ClientModel.disconnect();
		//Gdx.app.exit();
	}

	@Override
	public void onBackPressed() {
		ClientModel.disconnect();
		this.finish(); 
	}



}
