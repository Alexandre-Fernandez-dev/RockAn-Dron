package sylex.androidClient;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import sylex.androidClient.interfaces.RoomEventReceiver;
import sylex.controller.ServerHandler;
import sylex.model.ClientModel;

public class LobbyActivity extends AppCompatActivity implements RoomEventReceiver {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lobby);
        ClientModel.bindRoomEvent(this);
    }

    @Override
    public void onGameListChanged() {
        final LobbyActivity thisclass = this;
        runOnUiThread(new Runnable() {
            public void run() {
                ListView gameList = (ListView) findViewById(R.id.GameList);
                ArrayAdapter listAdapter = new ArrayAdapter<String>(thisclass, R.layout.activity_lobby, ClientModel.games);
                gameList.setAdapter(listAdapter);
            }
        });
    }

    public void onClickNewGame(View view) {
        Intent intent = new Intent(this, NewGameActivity.class);
        startActivity(intent);
    }


        @Override
    public void onJoinGameOk() {

    }

    @Override
    public void onJoinGameBad() {

    }
}
