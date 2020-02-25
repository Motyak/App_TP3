package com.ceri.tp3;

import android.app.AlertDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

//import fr.uavignon.shuet.tp3.data.Team;

public class NewTeamActivity extends AppCompatActivity {

    private EditText textTeam, textLeague;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_team);

        textTeam = (EditText) findViewById(R.id.editNewName);
        textLeague = (EditText) findViewById(R.id.editNewLeague);

        final Button but = (Button) findViewById(R.id.button);

        but.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                Team team = new Team(textTeam.getText().toString(),
                        textLeague.getText().toString()
                );
                if (NewTeamActivity.this.textTeam.getText().toString().isEmpty()) {
                    new AlertDialog.Builder(NewTeamActivity.this)
                            .setTitle("Sauvegarde impossible")
                            .setMessage("Le nom de l'équipe doit être non vide.")
                            .show();
                } else {
                    intent.putExtra(Team.TAG, team);
                    setResult(RESULT_OK, intent);
                    finish();
                }
            }
        });
    }


}
