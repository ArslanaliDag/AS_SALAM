package com.arslanali.as_salam;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;


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

        eTUser = (EditText)findViewById(R.id.eTUser);
        eTPassword = (EditText)findViewById(R.id.eTPassword);
        tVMsg = (TextView)findViewById(R.id.tVMsg);
        btnLogon = (Button)findViewById(R.id.btnLogon);

        LoginButton();
    }

    public void LoginButton() {

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

    protected class subscriberAsyncTask extends AsyncTask<Void, Void, Void> {

        // Получаем введенные логин и пароль пользователем
        String urlParameters  = "user="+ eTUser.getText() + "&pass=" + eTPassword.getText();
        byte[] postData = urlParameters.getBytes();
        int postDataLength = postData.length;
        // Ссылка на сервер который после авторизации высылает JSON обьект
        String request = "http://itair.esy.es/login.php";

        StringBuilder sb = new StringBuilder();

        HttpURLConnection urlConnection = null;

        @Override
        protected Void doInBackground(Void... params) {

            try {
                URL url = new URL(request);
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setDoOutput(true);
                // Отправляем POST запрос на сервер
                urlConnection.setRequestMethod("POST");
                urlConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                urlConnection.setRequestProperty("Content-Length", Integer.toString(postDataLength));
                urlConnection.setUseCaches(false);
                urlConnection.setConnectTimeout(10000);
                urlConnection.setReadTimeout(10000);
                // Подключаемся, отправляем все данные на сервер.
                urlConnection.connect();

                int HttpResult = urlConnection.getResponseCode();
                // Если ответ от сервера пришел 200 (все ок) то идем далее
                if(HttpResult == HttpURLConnection.HTTP_OK) {
                    BufferedReader br = new BufferedReader(new InputStreamReader(
                            urlConnection.getInputStream()));
                    String line = null;
                    while ((line = br.readLine()) != null) {
                        sb.append(line + "\n");
                    }
                    br.close();

                    JSONObject jsonRoot = new JSONObject(sb.toString());
                    JSONArray jsonSubscriberArray = jsonRoot.optJSONArray("subscriber");
                    // 0 - значит первая запись в файле.
                    // В JSON файле пользователь только один - это пользователь который вошел в программу.
                    // Поэтому нет смысла проходиться по "subscriber" циклом.
                    // Циклом надо проходиться по подписчикам.
                    JSONObject jsonSubscriber = jsonSubscriberArray.getJSONObject(0);

                    // Получение основных данных
//                     headName = jsonSubscriber.optString("headName").toString();
//                     paperboyName = jsonSubscriber.optString("paperboyName").toString();

                    // Получение данных о подписках
                    for (int i = 0; i < jsonSubscriberArray.length(); i++) {

                        JSONObject friend = jsonSubscriberArray.getJSONObject(i);
                        JSONArray contacts = friend.getJSONArray("subscriberData");

                        for (int j = 0; j < contacts.length(); j++) {

                            JSONObject contacts1 = contacts.getJSONObject(j);

//                            subscrFirstName = contacts1.getString("subscrFirstName");
//                            subscrLastName = contacts1.getString("subscrLastName");
//                            subscrNumber = contacts1.getString("subscrNumber");
//                            subscrAddress = contacts1.getString("subscrAddress");
//                            subscrNewspaperEdition = contacts1.getString("subscrNewspaperEdition");
//                            subscrNumberRevision = contacts1.getString("subscrNumberRevision");
//                            subscrStatus = contacts1.getString("subscrStatus");

//                            SubscriberData += "\n Мой старший = " + headName
//                                    + "\n Логин = " + paperboyName
//                            SubscriberData = "\n Имя подписчика = " + subscrFirstName
//                                    + "\n Фамилия подписчика = " + subscrLastName
//                                    + "\n Номер подписчика = " + subscrNumber
//                                    + "\n Адрес доставки = " + subscrAddress
//                                    + "\n Издание = " + subscrNewspaperEdition
//                                    + "\n Номер издания = " + subscrNumberRevision
//                                    + "\n Статус доставки = " + subscrStatus;

                           // Log.v("MYLOG_SubscriberData", SubscriberData);
                        }
                    }
                } else {
                    Log.v("MYLOG", urlConnection.getResponseMessage());
                }
            }
            catch (Exception e) {
                e.printStackTrace();
            }
            finally{
                if(urlConnection != null)

                    urlConnection.disconnect();
            }
            return null;
        }
    }
}
