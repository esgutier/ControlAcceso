package cl.rticket.controlacceso;

import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;

import org.json.JSONArray;
import org.json.JSONException;

import cl.rticket.utils.VolleyS;

//import static android.R.attr.label;

public class DatosActivity extends AppCompatActivity {



    private TextView textView;
    private EditText partidoInput;
    private EditText sectorInput;

    private TextView totalNormales;
    private TextView totalNominativas;
    private TextView totalListaNegra;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d("TEST", "0.");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_datos);

        totalNormales    = (TextView) findViewById(R.id.totalNormales);
        totalNominativas = (TextView) findViewById(R.id.totalNominativas);
        totalListaNegra  = (TextView) findViewById(R.id.totalListaNegra);
        final AccesoSQLiteHelper helper = new AccesoSQLiteHelper(this,"AccesoBD",null,1);
        final SQLiteDatabase bd = helper.getWritableDatabase();
        totalNormales.setText(""+ helper.getCount("Normales"));
        totalNominativas.setText(""+helper.getCount("Nominativas"));
        totalListaNegra.setText("0");
    }

    public void getNormales(View view) {

        final AccesoSQLiteHelper helper = new AccesoSQLiteHelper(this,"AccesoBD",null,1);
        final SQLiteDatabase bd = helper.getWritableDatabase();
        bd.execSQL("delete from Normales");
        Log.d("TEST", "1.");
        textView = (TextView) findViewById(R.id.labelTotalNormales);
        partidoInput = (EditText) findViewById(R.id.partidoInput);
        sectorInput  = (EditText) findViewById(R.id.sectorInput);
        Log.d("TEST", "2.");
        Log.d("TEST", "partidoInput="+partidoInput.getText());
        Log.d("TEST", "sectorInput="+sectorInput.getText());
        RequestQueue queue = VolleyS.getInstance(this).getRequestQueue();
        Log.d("TEST", "3.");
        String url = "http://172.16.187.144:8080/rticket/rest/normales/partido/"+partidoInput.getText()+"/sector/"+sectorInput.getText();
        Log.d("TEST", "4.");
        JsonArrayRequest request = new JsonArrayRequest(url, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray jsonArray) {
                Log.d("TEST", "5.");
                try {
                    for (int i = 0; i < jsonArray.length(); i++) {
                        String value = jsonArray.getString(i);
                        Log.d("TEST", "value:" + value);

                        bd.execSQL("INSERT INTO Normales(id) VALUES('"+value+"')");

                    }
                    bd.close();
                    int total = helper.getCount("Normales");
                    Toast.makeText(getApplicationContext(), "Total Normales:"+total, Toast.LENGTH_SHORT).show();


                } catch(JSONException e) {
                    Toast.makeText(getApplicationContext(), "Error: Al leer JSONArray", Toast.LENGTH_SHORT).show();
                }
                //textView.setText(jsonArray.toString());
               // onConnectionFinished();

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Log.d("TEST", "6."+volleyError.getMessage());
                Toast.makeText(getApplicationContext(), "Ocurrio un error", Toast.LENGTH_SHORT).show();
               // onConnectionFailed(volleyError.toString());
            }
        });
        Log.d("TEST", "7.");

        queue.add(request);


        Log.d("TEST", "8.");
    }


    /*public void onConnectionFailed(Context String error) {
        getActivity().setProgressBarIndeterminateVisibility(false);
        Toast.makeText(getActivity(), error, Toast.LENGTH_SHORT).show();
    }*/

    /*
    public void onPreStartConnection() {
        getApplicationContext().set
    }*/
}
