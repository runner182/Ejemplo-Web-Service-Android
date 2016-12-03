package training.salvador.practica.salvador.ejemplowebservice.PARSER;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import training.salvador.practica.salvador.ejemplowebservice.POJO.Usuario;

/**
 * Created by Salvador on 27/10/2016.
 */

public class UsuarioJSONparser {
    public static List<Usuario> parser(String content){
        try {
            JSONArray jsonArray=new JSONArray(content);
            List<Usuario> usuarioList=new ArrayList<>();
            for(int a=0;a<jsonArray.length();a++){
                JSONObject jsonObject = jsonArray.getJSONObject(a);
                Usuario usuario= new Usuario();
                usuario.setId(jsonObject.getInt("usuarioid"));
                usuario.setNombre(jsonObject.getString("nombre"));
                usuario.setTwitter(jsonObject.getString("twitter"));
                usuarioList.add(usuario);
            }
            return usuarioList;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }
}
