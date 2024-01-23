package com.example.animaland.oral;

import static android.view.Gravity.CENTER;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Point;
import android.os.Bundle;
import android.view.Display;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.animaland.R;

import org.w3c.dom.Text;

public class dialog_pay extends Dialog {
    private Button yes,no;
    private TextView text;
    private int fee;
    private View.OnClickListener listenerOfYes,listenerOfNo;

    public dialog_pay(@NonNull Context context) {
        super(context);
    }

    public dialog_pay(@NonNull Context context, int themeResId,int fee) {
        super(context, themeResId);
        this.fee=fee;
    }

    public dialog_pay setYes(View.OnClickListener listener) {
        this.listenerOfYes=listener;
        return this;
    }

    public dialog_pay setNo(View.OnClickListener listener) {
        this.listenerOfNo=listener;
        return this;
    }

    public int getFee() {
        return fee;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);//去掉标题栏（及其所占用的空间）
        setContentView(R.layout.dialog_roomexit);

        //找到控件
        yes=findViewById(R.id.room_exitYes);
        no=findViewById(R.id.room_exitNo);
        text=findViewById(R.id.roomshow_name1);

        text.setText("需支付"+fee+"金币，请确认~");

        //设置监听
        yes.setOnClickListener(listenerOfYes);
        no.setOnClickListener(listenerOfNo);
    }

}
