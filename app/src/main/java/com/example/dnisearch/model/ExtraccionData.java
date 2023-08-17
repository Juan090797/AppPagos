package com.example.dnisearch.model;

import android.annotation.SuppressLint;
import android.os.StrictMode;
import android.util.Log;
import com.google.mlkit.common.MlKitException;
import com.google.mlkit.vision.barcode.common.Barcode;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Locale;

public class ExtraccionData {
    public String nombre,dni,direccion,Razon_Social;
    public String getSuccessfulMessage(Barcode barcode,String TipoDocumento) {
        String barcodeValue;
        if (TipoDocumento.equals("ruc")){
            String valor = barcode.getDisplayValue();
            Log.d("PRUEBA",valor);
            String[] partes = valor.split("\\|");
            //Log.d("PRUEBA",partes[0]);
            for (String parte : partes) {
                Log.d("PRUEBA",parte);
            }
            //Log.d("PRUEBA",valor.substring(0,11));
            String jsonPersona = obtenerDatosByRUC(valor.substring(0,11));
            try {
                JSONObject jsonObj = new JSONObject(jsonPersona);
                Razon_Social = jsonObj.getString("nombre");
                direccion = jsonObj.getString("direccion");
            } catch (JSONException e) {
                Log.d("PRUEBA",e.toString());
            }
            //int total = partes.length;
            //Log.d("PRUEBA", "total: " + total);
            String[] nuevoArray = new String[partes.length];
            System.arraycopy(partes, 0, nuevoArray, 0, partes.length);
            /*System.arraycopy(partes, 0, nuevoArray, 0, partes.length);
            nuevoArray[nuevoArray.length - 2] = Razon_Social;
            nuevoArray[nuevoArray.length - 1] = direccion;*/
            Log.d("PRUEBA","NUEVO ARRAY: " + nuevoArray.toString());
            /*for (String nuevo : nuevoArray) {
                Log.d("PRUEBA",nuevo);
            }*/
            //barcodeValue = partes.toString();
            barcodeValue = String.join("\n", nuevoArray);
        }
        else {
            String jsonPersona = obtenerDatosByDni(barcode.getDisplayValue());
            try {
                JSONObject jsonObj = new JSONObject(jsonPersona);
                nombre = jsonObj.getString("nombre");
                dni = jsonObj.getString("numeroDocumento");
            } catch (JSONException e) {
                Log.d("PRUEBA",e.toString());
            }
            barcodeValue = dni;
        }

        return barcodeValue;
    }
    public String obtenerDatosByDni(String dni){
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        return new HttpService("https://api.apis.net.pe/v1").get("dni", "numero="+dni);
    }

    public String obtenerDatosByRUC(String ruc){
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        return new HttpService("https://api.apis.net.pe/v1/").get("ruc", "numero="+ruc);
    }
    @SuppressLint("SwitchIntDef")
    public String getErrorMessage(Exception e) {
        if (e instanceof MlKitException) {
            switch (((MlKitException) e).getErrorCode()) {
                case MlKitException.CODE_SCANNER_CAMERA_PERMISSION_NOT_GRANTED:
                    return "No se concede el permiso de la cámara.";
                case MlKitException.CODE_SCANNER_APP_NAME_UNAVAILABLE:
                    return "El nombre de la aplicación no está establecido.";
                default:
                    return "No se pudo escanear el código: %1$s";
            }
        } else {
            return e.getMessage();
        }
    }
    class HttpService {
        private static final String TAG = "MYTAG";
        private String urlService;
        public HttpService(String urlService) {
            this.urlService = urlService;
        }
        public String get(String param, String query) {
            StringBuilder result = new StringBuilder();
            try {
                String finalPath = urlService + (param==null?"":"/"+param)+(query==null?"":"?"+query);
                Log.i("PRUEBA",finalPath);
                URL url = new URL(finalPath);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");
                BufferedReader rd = new BufferedReader(new InputStreamReader(connection.getInputStream(),"utf-8"));
                String linea;
                while ((linea = rd.readLine()) != null) {
                    result.append(linea+"\n");
                }
                rd.close();
            } catch (Exception e) {
                Log.e("PRUEBA",  urlService+  e.toString());
            }
            Log.i("PRUEBA",result.toString());
            return result.toString();
        }
        public String post(String param, String query, String body) {
            StringBuilder result = new StringBuilder();
            try {
                String finalPath = urlService + (param==null?"":"/"+param)+(query==null?"":"?"+query);
                Log.i(TAG,finalPath);
                URL url = new URL(finalPath);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("POST");
                connection.setRequestProperty("Content-Type", "application/json");
                connection.setRequestProperty("Accept", "application/json");
                connection.setDoOutput(true);

                OutputStream os = connection.getOutputStream();
                byte[] input = body.getBytes("utf-8");
                os.write(input, 0, input.length);

                BufferedReader rd = new BufferedReader(new InputStreamReader(connection.getInputStream(),"utf-8"));
                String linea;
                while ((linea = rd.readLine()) != null) {
                    result.append(linea);
                }
                rd.close();

            } catch (Exception e) {
                Log.e(TAG ,e.toString());
            }
            Log.i(TAG,result.toString());
            return result.toString();
        }
        }
}
