package sylex.androidClient;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import sylex.androidClient.interfaces.RoomEventReceiver;
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
        ClientModel.StartGameOK();
    }

    @Override
    public void onGameUserListChanged() {
        runOnUiThread(new Runnable() {
            public void run() {
                Log.d("D", "UPDATE USER LIST : "+ClientModel.clientsInGame.toString()+"\n\n");
                adapter.clear();
                adapter.addAll(ClientModel.clientsInGame);
                adapter.notifyDataSetChanged();
            }
        });
    }

        @Override
    public void onJoinGameOk() {

    }

    @Override
    public void onJoinGameBad() {

    }
}
