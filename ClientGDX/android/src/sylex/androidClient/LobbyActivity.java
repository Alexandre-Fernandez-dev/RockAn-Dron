package sylex.androidClient;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import fr.upmc.lexteksylux.radclient.RAD;
import sylex.androidClient.interfaces.RoomEventReceiver;
import sylex.gdx.AndroidLauncher;
import sylex.model.ClientModel;

public class LobbyActivity extends AppCompatActivity implements RoomEventReceiver {

    private ListView gameUserList;
    private ArrayAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lobby);
        gameUserList = (ListView) findViewById(R.id.GameUserList);
        adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, ClientModel.clientsInGame);
        gameUserList.setAdapter(adapter);
        ClientModel.bindRoomEvent(this);
    }

    public void onClickReady(View view) {
        Button readyButton = (Button) findViewById(R.id.buttonReady);
        readyButton.setEnabled(false);
        readyButton.setText("READY OK");
        ClientModel.StartGameOK();
    }

    @Override
    public void onGameUserListChanged() {
        runOnUiThread(new Runnable() {
            public void run() {
                Log.d("D", "UPDATE USER LIST : "+ ClientModel.clientsInGame.toString()+"\n\n");
                adapter.notifyDataSetChanged();
            }
        });
    }

    @Override
    public void onStartGame(int nbSec) {
        Log.d("D", "RECEIVED START GAME :D");
        Intent intent = new Intent(this, AndroidLauncher.class);
        startActivity(intent);
    }

}
