package training.salvador.practica.salvador.ejemplowebservice;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import training.salvador.practica.salvador.ejemplowebservice.PARSER.UsuarioJSONparser;
import training.salvador.practica.salvador.ejemplowebservice.PARSER.usuarioXMLparser;
import training.salvador.practica.salvador.ejemplowebservice.POJO.Usuario;

public class MainActivity extends AppCompatActivity {

    Button boton;
    TextView text;
    ProgressBar progressbar;
    List<myTask> taskList; //puedes guardar estados de los hilos
    List<Usuario> usuarioList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        text=(TextView)findViewById(R.id.text);
        text.setMovementMethod(new ScrollingMovementMethod()); //para hacer scroll en el texto u objeto
        boton=(Button)findViewById(R.id.boton);
        progressbar=(ProgressBar)findViewById(R.id.progressbar);
        progressbar.setVisibility(View.INVISIBLE); //pon la progressbar invisible
        taskList = new ArrayList<>(); //instancia la listtask
        boton.setOnClickListener(new View.OnClickListener() {    //se pone un onclic listener en el boton para ver si lo han presionado
            @Override
            public void onClick(View v) {
                if (isOnline())
                    pedirDatos("http://maloschistes.com/maloschistes.com/jose/webservice.php");
                    //pedirDatos("http://maloschistes.com/maloschistes.com/jose/usuarios.xml");
                else
                    Toast.makeText(getApplicationContext(), "Sin conexxion a Internet", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private class myTask extends AsyncTask<String,String,String>{  //asi se crea un objeto o hilo asincrono
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            taskList.add(this);
            if (taskList.size()>0)
                progressbar.setVisibility(View.VISIBLE);
        }

        @Override
        protected String doInBackground(String... params) {
            /*for (int a=0;a<=10;a++) {
                publishProgress("Numero:" + a);
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            */
            String content=HttpManager.getData(params[0]);
            return content;
        }

        @Override
        protected void onProgressUpdate(String... values) {
           // cargarDatos(values[0]);
        }

        @Override
        protected void onPostExecute(String s){
            super.onPostExecute(s);
            usuarioList= UsuarioJSONparser.parser(s);
            cargarDatos();
            taskList.remove(this);
            if (taskList.size()==0)
                progressbar.setVisibility(View.INVISIBLE);
        }
    }

    public void pedirDatos(String uri){
        myTask tarea = new myTask(); //instancia myTask
       // tarea.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR); //para hacer hilos paralelos
        tarea.execute(uri);
    }

    public void cargarDatos(){

        if(usuarioList!=null){
            for (Usuario user:usuarioList) {
                text.append(user.getNombre()+"\n");
                text.append(user.getTwitter()+"\n");
                text.append("\n");
            }
        }

    }

    public boolean isOnline(){  //verifica si tenemos salida a internet
        ConnectivityManager connectivityManager= (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo=connectivityManager.getActiveNetworkInfo();
        if(networkInfo!=null && networkInfo.isConnectedOrConnecting()){
            return true;
        }
        else{
            return false;
        }
    }
}
