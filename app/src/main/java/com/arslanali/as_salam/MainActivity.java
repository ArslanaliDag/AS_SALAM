package com.arslanali.as_salam;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


public class MainActivity extends AppCompatActivity {

    Button btnLogon;
    EditText eTUser, eTPassword;
    TextView tVMsg, tVAtemptCount;


    // Количество попыток ввода логина и пароля
    int attemptCounter = 3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        LoginButton();
    }

    public void LoginButton() {

        eTUser = (EditText)findViewById(R.id.eTUser);
        eTPassword = (EditText)findViewById(R.id.eTPassword);

        tVMsg = (TextView)findViewById(R.id.tVMsg);

        btnLogon = (Button)findViewById(R.id.btnLogon);

        btnLogon.setOnClickListener (
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        if (eTUser.getText().toString().equals("1") && eTPassword.getText().toString().equals("1")) {

                            Toast.makeText(MainActivity.this, "Ассаляму алейкум!", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent (MainActivity.this, UserInterface.class));
                        } else {

                            Toast.makeText(MainActivity.this, "Логин или пароль не верны!", Toast.LENGTH_SHORT).show();
                            attemptCounter--;
                            if (attemptCounter == 0) {
                                btnLogon.setEnabled(false);
                            }
                        }
                    }
                }
        );
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
