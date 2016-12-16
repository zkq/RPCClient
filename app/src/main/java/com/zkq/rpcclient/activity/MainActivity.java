package com.zkq.rpcclient.activity;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.zkq.rpcclient.R;
import com.zkq.rpcclient.RpcBitmap;
import com.zkq.rpcclient.rpcClient;

import org.acplt.oncrpc.OncRpcException;
import org.acplt.oncrpc.OncRpcProtocols;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends Activity {

    ListView imgview;
    TextView jsonview;



    final byte[] SERVER_IP = new byte[]{(byte)192, (byte)168, (byte)0, (byte)106};
    final int SERVER_PORT = 2023;
    final String CLIENT_ERROR = "初始化rpcClient出错";
    final String IVOKE_ERROR = "调用远程方法出错";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        imgview = (ListView) findViewById(R.id.list);
        jsonview = (TextView) findViewById(R.id.json);

        //must invoke remote procedure in un ui thread
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    //initialize rpcClient
                    final rpcClient client;
                    try {
                        InetAddress address = InetAddress.getByAddress(SERVER_IP);
                        client = new rpcClient(address, SERVER_PORT, OncRpcProtocols.ONCRPC_TCP);
                    } catch (UnknownHostException e) {
                        showToast(CLIENT_ERROR);
                        e.printStackTrace();
                        return;
                    } catch (OncRpcException e) {
                        showToast(CLIENT_ERROR);
                        e.printStackTrace();
                        return;
                    } catch (IOException e) {
                        showToast(CLIENT_ERROR);
                        e.printStackTrace();
                        return;
                    }

                    if(client == null){
                        return;
                    }

                    //get file tree
                    String filetreejson = client.getFileTree_1();
                    if(filetreejson == null || filetreejson.equals("")){
                        return;
                    }
                    showJson(filetreejson);

                    //analyze file tree and download files
                    List<String> paths = new ArrayList<>();
                    JSONObject root = new JSONObject(filetreejson);
                    JSONArray files = root.getJSONArray("dir");
                    for(int i =0; i < files.length(); i++){
                        JSONObject filej = files.getJSONObject(i);
                        String path = filej.get("name").toString();
                        long modifytime = Long.valueOf(filej.get("lastModified").toString());
                        paths.add(path);

                        File file = new File(Environment.getExternalStorageDirectory()  + "/" + path);
                        if(file.exists() && file.lastModified() >= modifytime){
                            //存在此文件且未修改则不用下载
                        }else{
                            RpcBitmap rpcBitmap = client.getImg_1(path);
                            file.createNewFile();
                            FileOutputStream fos = new FileOutputStream(file);
                            fos.write(rpcBitmap.value);
                            fos.close();
                        }
                    }
                    //show img to screen
                    showImg(paths);

                } catch (final OncRpcException e) {
                    e.printStackTrace();
                    showToastOnUIThread(IVOKE_ERROR + e.getMessage());
                } catch (final IOException e) {
                    e.printStackTrace();
                    showToastOnUIThread(IVOKE_ERROR + e.getMessage());
                } catch (JSONException e) {
                    e.printStackTrace();
                    showToastOnUIThread("Parse json error" + e.getMessage());
                }
            }
        });

        thread.start();
    }

    private void showToastOnUIThread(final String str){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(MainActivity.this, str, Toast.LENGTH_LONG).show();
            }
        });
    }

    private void showImg(final List<String> paths){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                List<Bitmap> bitmaps = new ArrayList<>();
                for(String path : paths){
                    Bitmap bitmap = BitmapFactory.decodeFile(Environment.getExternalStorageDirectory()  + "/" + path);
                    bitmaps.add(bitmap);
                }
                MyListAdapter adapter = new MyListAdapter(MainActivity.this, bitmaps);
                imgview.setAdapter(adapter);
            }
        });
    }

    private void showJson(final String json){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                jsonview.setText(json);
            }
        });
    }


    private void showToast(final String str){
        Toast.makeText(MainActivity.this, str, Toast.LENGTH_LONG).show();
    }
}
