package com.example.animaland.chatgpt;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.animaland.Island;
import com.example.animaland.R;
import com.iflytek.cloud.ErrorCode;
import com.iflytek.cloud.InitListener;
import com.iflytek.cloud.RecognizerResult;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.SpeechUtility;
import com.iflytek.cloud.ui.RecognizerDialog;
import com.iflytek.cloud.ui.RecognizerDialogListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.Response;

public class GPTMainActivity extends AppCompatActivity {
    Intent intent1=new Intent(this, Island.class);
    private RecyclerView mRecyclerView;
    private MessageAdapter mAdapter;
    private EditText mEditText;
    private ImageView back_chat;
    private ImageView info_chat;
    private AppCompatImageView mButton;
    private AppCompatImageView speak;

    private String apiUrl = "https://api.openai.com/v1/completions";
    private String accessToken = "Enter your token here";
    private List < Message > mMessages;
    public static final MediaType JSON
            = MediaType.get("application/json; charset=utf-8");
    private static final String TAG = "MainActivity";

    private SpeechRecognizer mIat;// 语音听写对象
    private RecognizerDialog mIatDialog;// 语音听写UI

    // 用HashMap存储听写结果
    private HashMap<String, String> mIatResults = new LinkedHashMap<String, String>();

    private SharedPreferences mSharedPreferences;//缓存

    private String mEngineType = SpeechConstant.TYPE_CLOUD;// 引擎类型
    private String language = "zh_cn";//识别语言


    private String resultType = "json";//结果内容数据格式
    /**
     * Used for receiving notifications from the SpeechRecognizer when the
     * recognition related events occur. All the callbacks are executed on the
     * Application main thread.
     * 值的注意的是，所有的回调都在主线程
     */
    public interface RecognitionListener {

        // 实例准备就绪
        void onReadyForSpeech(Bundle params);

        // 开始语音识别
        void onBeginningOfSpeech();

        // 聆听分贝值 可能会有负数哦
        void onRmsChanged(float rmsdB);

        void onBufferReceived(byte[] buffer);

        // 识别结束
        void onEndOfSpeech();

        // 错误码
        void onError(int error);

        // 识别的结果，在某些国产机上，这个结果会是空
        void onResults(Bundle results);

        // 识别的部分结果 有些过程机上 [onResults] 方法为空，可以在这里拿到结果
        void onPartialResults(Bundle partialResults);

        void onEvent(int eventType, Bundle params);
    }


    OkHttpClient client = new OkHttpClient();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.gpt_activity_main);
        mRecyclerView = findViewById(R.id.recycler_view);
        mEditText = findViewById(R.id.edit_text);
        mButton = findViewById(R.id.sent_image);
        mMessages = new ArrayList < > ();
        back_chat=findViewById(R.id.back_chat);
        mAdapter = new MessageAdapter(mMessages);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setAdapter(mAdapter);
        info_chat=findViewById(R.id.info_chat);
        speak=findViewById(R.id.speak_image);
        initPermission();//权限请求
        SpeechUtility.createUtility(GPTMainActivity.this, "appid=591ba0dd");
        // 使用SpeechRecognizer对象，可根据回调消息自定义界面；
        mIat = SpeechRecognizer.createSpeechRecognizer(GPTMainActivity.this);

        // 使用UI听写功能，请根据sdk文件目录下的notice.txt,放置布局文件和图片资源
        mIatDialog = new RecognizerDialog(GPTMainActivity.this, mInitListener);
        mSharedPreferences = getSharedPreferences("ASR",
                Activity.MODE_PRIVATE);




        final  dialog_chat chat=new dialog_chat(GPTMainActivity.this,R.style.dialogOfShowRoom);
        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                callAPI();
            }
        });
        back_chat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Handler handler1 = new Handler();
                handler1.postDelayed(new Runnable() {
                    @Override
                    public void run() {

                        startActivity(intent1);

                    }

                }, 700);

            }
        });
        info_chat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                chat.show();
            }
        });
        speak.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if( null == mIat ){
                    // 创建单例失败，与 21001 错误为同样原因，参考 http://bbs.xfyun.cn/forum.php?mod=viewthread&tid=9688
                    showMsg( "创建对象失败，请确认 libmsc.so 放置正确，且有调用 createUtility 进行初始化" );
                    return;
                }

                mIatResults.clear();//清除数据
                // 启动服务需要一个 Intent
                Intent mRecognitionIntent;
                mRecognitionIntent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
                mRecognitionIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
                mRecognitionIntent.putExtra(RecognizerIntent.EXTRA_PARTIAL_RESULTS, true);
                mRecognitionIntent.putExtra(RecognizerIntent.EXTRA_MAX_RESULTS, 3);
// mLocale 是一个语音种类，可以根据自己的需求去设置
                boolean mLocale=true;
                mRecognitionIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, mLocale);

// 开始语音识别 结果在 mSpeechRecognizer.setRecognitionListener(this);回调中
                mIat.startListening(mRecognitionIntent);

// 停止监听
                mIat.stopListening();

// 取消服务
                mIat.cancel();
                mIatDialog.setListener(mRecognizerDialogListener);//设置监听
                mIatDialog.show();// 显示对话框

            }


        });


    }
    /**
     * android 6.0 以上需要动态申请权限
     */
    private void initPermission() {
        String permissions[] = {Manifest.permission.RECORD_AUDIO,
                Manifest.permission.ACCESS_NETWORK_STATE,
                Manifest.permission.INTERNET,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
        };

        ArrayList<String> toApplyList = new ArrayList<String>();

        for (String perm : permissions) {
            if (PackageManager.PERMISSION_GRANTED != ContextCompat.checkSelfPermission(this, perm)) {
                toApplyList.add(perm);
            }
        }
        String tmpList[] = new String[toApplyList.size()];
        if (!toApplyList.isEmpty()) {
            ActivityCompat.requestPermissions(this, toApplyList.toArray(tmpList), 123);
        }

    }

    /**
     * 权限申请回调，可以作进一步处理
     *
     * @param requestCode
     * @param permissions
     * @param grantResults
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        // 此处为android 6.0以上动态授权的回调，用户自行实现。
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
    /**
     * 初始化监听器。
     */
    private InitListener mInitListener = new InitListener() {

        @Override
        public void onInit(int code) {
            Log.d(TAG, "SpeechRecognizer init() code = " + code);
            if (code != ErrorCode.SUCCESS) {
                showMsg("初始化失败，错误码：" + code + ",请点击网址https://www.xfyun.cn/document/error-code查询解决方案");
            }
        }
    };


    /**
     * 听写UI监听器
     */
    private RecognizerDialogListener mRecognizerDialogListener = new RecognizerDialogListener() {
        public void onResult(RecognizerResult results, boolean isLast) {

            printResult(results);//结果数据解析

        }

        /**
         * 识别回调错误.
         */
        public void onError(SpeechError error) {
            showMsg(error.getPlainDescription(true));
        }

    };
    /**
     * 数据解析
     *
     * @param results
     */
    private void printResult(RecognizerResult results) {
        String text = JsonParser.parseIatResult(results.getResultString());

        String sn = null;
        // 读取json结果中的sn字段
        try {
            JSONObject resultJson = new JSONObject(results.getResultString());
            sn = resultJson.optString("sn");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        mIatResults.put(sn, text);

        StringBuffer resultBuffer = new StringBuffer();
        for (String key : mIatResults.keySet()) {
            resultBuffer.append(mIatResults.get(key));
        }

        mEditText.setText(resultBuffer.toString());//听写结果显示

    }


    /**
     * 提示消息
     * @param msg
     */
    private void showMsg(String msg) {
        Toast.makeText(GPTMainActivity.this, msg, Toast.LENGTH_SHORT).show();
    }
    /**
     * 参数设置
     *
     * @return
     */


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case 1: {
                if (resultCode == RESULT_OK && data != null) {
                    ArrayList<String> text = data
                            .getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    //这里集合列表中第一个值为匹配度最高的值
                    mEditText.setText(text.get(0));
                }
                break;
            }
        }
    }

    void addResponse(String response){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mMessages.add(new Message(response,false));
                mAdapter.notifyItemInserted(mMessages.size() - 1);
                mRecyclerView.smoothScrollToPosition(mAdapter.getItemCount());
            }
        });
    }
    private void callAPI() {
        String text = mEditText.getText().toString();
        mMessages.add(new Message(text, true));
        mAdapter.notifyItemInserted(mMessages.size() - 1);
        mEditText.getText().clear();
        JSONObject requestBody = new JSONObject();
        try {
            //requestBody.put("model", "text-davinci-003");
            //requestBody.put("prompt", text);
            //requestBody.put("max_tokens", 4076);
            //requestBody.put("temperature", 1);
            //requestBody.put("top_p", 1);
            //requestBody.put("n", 1);
            //requestBody.put("stream", false);
            //requestBody.put("logprobs", null);
            //requestBody.put("stop", ".");
            requestBody.put("model", "text-davinci-003");
            requestBody.put("prompt", text);
            requestBody.put("max_tokens", 100);
            requestBody.put("temperature", 1);
            requestBody.put("top_p", 1);
            requestBody.put("frequency_penalty", 0.0);
            requestBody.put("presence_penalty", 0.0);
        } catch (JSONException e) {

            e.printStackTrace();
        }

        RequestBody body=  RequestBody.create(requestBody.toString(),JSON);
        okhttp3.Request request= new okhttp3.Request.Builder().url(apiUrl).
                header("Authorization","Bearer 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")
                .post(body).build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
addResponse("Failed to load response due to "+e.getMessage());
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
if(response.isSuccessful()){
    JSONObject jsonObject= null;
    try {
        jsonObject = new JSONObject(response.body().string());
        JSONArray jsonArray= jsonObject.getJSONArray("choices");
        String result= jsonArray.getJSONObject(0).getString("text");
        addResponse(result.trim());
    } catch (JSONException e) {
        throw new RuntimeException(e);
    }

}
else
    addResponse("Failed to load response due to "+response.body().string());
            }
        });


    }
}