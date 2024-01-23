package com.example.animaland.oral;

import static com.example.animaland.tool.ThreadHelper.cachedThreadPool;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.View;
import android.view.Window;
import android.widget.CheckedTextView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.animaland.R;
import com.example.animaland.School.Language;
import com.example.animaland.tool.DatabaseHelper;
import com.example.animaland.tool.User;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class dialog_language extends Dialog {

    private Context mContext;
    private TextView name;
    private RecyclerView mRVmother,mRVgood,mRVlearn;
    private OralGridViewAdapter mAdapter1,mAdapter2,mAdapter3;
    private EditText et0,et1,et2,et3;
    private ImageView close;
    private String  string1="",string2="",string3="";
    private View.OnClickListener listener;
    List<Language[]> languages=new ArrayList<>();
    private DatabaseHelper db = new DatabaseHelper();
    private List<GridLayoutManager> mGlms=new ArrayList<>();
    private User user = new User();

    public dialog_language(@NonNull Context context, int themeResId) {
        super(context, themeResId);
        this.mContext=context;
    }
    int[] oral=new int[4];
    private Handler handler=new Handler(Looper.myLooper()){
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);

            if(msg.what==0){
                initData(oral[0],oral[1],oral[2],oral[3],null,oral[3],null,oral[3],null);//暂时是由是不是其他的选项
                et0.setText((String)msg.obj);
            }else if(msg.what==1){
                name.setText((String)msg.obj);
            };
        }
    };

    public void setOnCloseListener(View.OnClickListener listener){
        this.listener=listener;
    }

    //0：不选 1：母语 2：学习 3：擅长
    int eng = 0;
    int fra = 0;
    int jan = 0;
    int other = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);//去除标题栏
        setContentView(R.layout.oral_dialog_language);

        //找到控件
        mRVmother =findViewById(R.id.rv_motherLanguage);
        mRVgood=findViewById(R.id.rv_LanguageGoodAt);
        mRVlearn=findViewById(R.id.rv_LanguageLearn);
        et0=findViewById(R.id.introduction);
        et1=findViewById(R.id.etOther_mother);
        et2=findViewById(R.id.etOther_good);
        et3=findViewById(R.id.etOther_learn);
        close=findViewById(R.id.close);
        name=findViewById(R.id.name);

        //设置复选框布局
        GridLayoutManager glm1 = new GridLayoutManager(mContext,3);
        GridLayoutManager glm2 = new GridLayoutManager(mContext,3);
        GridLayoutManager glm3 = new GridLayoutManager(mContext,3);

        mRVmother.setLayoutManager(glm1);
        mRVgood.setLayoutManager(glm2);
        mRVlearn.setLayoutManager(glm3);

        mGlms.add(glm1);
        mGlms.add(glm2);
        mGlms.add(glm3);

        mAdapter1 = new OralGridViewAdapter(mContext,4);
        mAdapter2 = new OralGridViewAdapter(mContext,4);
        mAdapter3 = new OralGridViewAdapter(mContext,4);

        mRVmother.setAdapter(mAdapter1);
        mRVgood.setAdapter(mAdapter2);
        mRVlearn.setAdapter(mAdapter3);

        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                try {
                    for(int i =0;i<4;i++) {
                        oral[i] = db.getOral()[i];
                        System.out.println(oral[i]);
                    }
                    Message msg=new Message();
                    msg.what=0;
                    msg.obj=db.getOralIntro();
                    handler.sendMessage(msg);
                    Message msg1=new Message();
                    msg1.what = 1;
                    msg1.obj=db.getName();
                    handler.sendMessage(msg1);
                } catch (SQLException e) {
                    e.printStackTrace();
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (InstantiationException e) {
                    e.printStackTrace();
                }
            }
        };
        cachedThreadPool.execute(runnable);

        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Runnable runnable = new Runnable() {
                    @Override
                    public void run() {
                        try {
                            db.setOral(eng,fra,jan,other);
                            db.setOralIntro(et0.getText().toString());
                        } catch (SQLException e) {
                            e.printStackTrace();
                        } catch (ClassNotFoundException e) {
                            e.printStackTrace();
                        } catch (IllegalAccessException e) {
                            e.printStackTrace();
                        } catch (InstantiationException e) {
                            e.printStackTrace();
                        }
                    }
                };
                cachedThreadPool.execute(runnable);
                dismiss();
            }

        });

        mAdapter1.setOnItemClickListener(new OralGridViewAdapter.onGridItemClickListener() {//母语
                    @Override
                    public void onGridItemClick(int position) {
                        switch (position) {
                            case 0://英
                                setOthersUnClickable(glm2, 0, mAdapter1.getCheckedStates().get(position) != null ? false : true);
                                setOthersUnClickable(glm3, 0, mAdapter1.getCheckedStates().get(position) != null ? false : true);
                                eng = 1;
                                break;
                            case 1://fa
                                setOthersUnClickable(glm2, 1, mAdapter1.getCheckedStates().get(position) != null ? false : true);
                                setOthersUnClickable(glm3, 1, mAdapter1.getCheckedStates().get(position) != null ? false : true);
                                fra = 1;
                                break;
                            case 2://ri
                                setOthersUnClickable(glm2, 2, mAdapter1.getCheckedStates().get(position) != null ? false : true);
                                setOthersUnClickable(glm3, 2, mAdapter1.getCheckedStates().get(position) != null ? false : true);
                                jan = 1;
                                break;
                            case 3://qita
                                et1.setVisibility(mAdapter1.getCheckedStates().get(3) != null ? View.VISIBLE : View.INVISIBLE);
                                other = 1;
                                break;
                        }

                    }
                });

        mAdapter2.setOnItemClickListener(new OralGridViewAdapter.onGridItemClickListener() {//擅长
            @Override
            public void onGridItemClick(int position) {
                switch (position){
                    case 0:
                        setOthersUnClickable(glm1,0,mAdapter2.getCheckedStates().get(position)!=null?false:true);
                        setOthersUnClickable(glm3,0,mAdapter2.getCheckedStates().get(position)!=null?false:true);
                        eng = 2;
                        break;
                    case 1:
                        setOthersUnClickable(glm1,1,mAdapter2.getCheckedStates().get(position)!=null?false:true);
                        setOthersUnClickable(glm3,1,mAdapter2.getCheckedStates().get(position)!=null?false:true);
                        fra = 2;
                        break;
                    case 2:
                        setOthersUnClickable(glm1,2,mAdapter2.getCheckedStates().get(position)!=null?false:true);
                        setOthersUnClickable(glm3,2,mAdapter2.getCheckedStates().get(position)!=null?false:true);
                        jan = 2;
                        break;
                    case 3:
                        et2.setVisibility(mAdapter2.getCheckedStates().get(3)!=null?View.VISIBLE:View.INVISIBLE);
                        other = 2;
                        break;
                }

            }
        });

        mAdapter3.setOnItemClickListener(new OralGridViewAdapter.onGridItemClickListener() {//学习
            @Override
            public void onGridItemClick(int position) {
                switch (position){
                    case 0:
                        setOthersUnClickable(glm1,0,mAdapter3.getCheckedStates().get(position)!=null?false:true);
                        setOthersUnClickable(glm2,0,mAdapter3.getCheckedStates().get(position)!=null?false:true);
                        eng = 3;
                        break;
                    case 1:
                        setOthersUnClickable(glm1,1,mAdapter3.getCheckedStates().get(position)!=null?false:true);
                        setOthersUnClickable(glm2,1,mAdapter3.getCheckedStates().get(position)!=null?false:true);
                        fra = 3;
                        break;
                    case 2:
                        setOthersUnClickable(glm1,2,mAdapter3.getCheckedStates().get(position)!=null?false:true);
                        setOthersUnClickable(glm2,2,mAdapter3.getCheckedStates().get(position)!=null?false:true);
                        jan = 3;
                        break;
                    case 3:
                        et3.setVisibility(mAdapter3.getCheckedStates().get(3)!=null?View.VISIBLE:View.INVISIBLE);
                        other = 3;
                        break;
                }

            }
        });
    }

    private void setOthersUnClickable(GridLayoutManager manager,int position,boolean toClick){
        CheckedTextView checkedTextView =manager.findViewByPosition(position).findViewById(R.id.ctv);
        checkedTextView.setClickable(toClick);
        if(toClick)
            checkedTextView.setTextColor(-7637147);
        else
            checkedTextView.setTextColor(-6710887);

    }

    private String setStrings(OralGridViewAdapter adapter, String string, EditText et){
        string="";
        //3是语言数量（不包括其他
        for(int i=0;i<3;i++){
            Language l=adapter.getCheckedStates().get(i);
            if(l!=null){
                string+=l.getText();
                string+=" ";
            }
        }

        if(adapter.getCheckedStates().get(3)!=null){
            //选择了“其他”
            string+=et.getText();
        }

        return string;
    }

    private Language[] setLanguages(OralGridViewAdapter adapter){
        Language[] language = new Language[4];
        int currentLength = 0;

        //4是语言数量（包括其他
        for(int i=0;i<4;i++){
            Language l=adapter.getCheckedStates().get(i);
            if(l!=null){
                language[currentLength++]=l;
            }
        }
        return language;
    }

    public Language[] getLanguages(int index){
        return languages.get(index);
    }

    public String getStrings(int index){

        string1= setStrings(mAdapter1,string1,et1);
        string2= setStrings(mAdapter2,string2,et2);
        string3= setStrings(mAdapter3,string3,et3);

        switch (index){
            case 0:
                return et0.getText().toString();
            case 1:
                return string1;
            case 2:
                return string2;
            case 3:
                return string3;
        }
        return null;
    }

    public void Update(){
        languages.clear();
        languages.add(setLanguages(mAdapter1));
        languages.add(setLanguages(mAdapter2));
        languages.add(setLanguages(mAdapter3));
    }

    public void initData(int English,int French,int Japan,int Other1,String other1,int Other2,String other2,int Other3,String other3){
        setData(English,Language.ENGLISH);
        setData(French,Language.FRENCH);
        setData(Japan,Language.JAPANESE);

        //设置其他
        setOther(Other1,other1);
        setOther(Other2,other2);
        setOther(Other3,other3);


    }

    private void setData(int position,Language name){
        if(position!=0){
            CheckedTextView ctv= mGlms.get(position-1).findViewByPosition(name.getCode()-1).findViewById(R.id.ctv);
            ctv.setChecked(true);
            for(int i=0;i<3;i++){
                if(i!=position-1)
                    setOthersUnClickable(mGlms.get(i),name.getCode()-1,false);
            }
        }
    }

    private void setOther(int position,String string){
        if(position!=0){
            CheckedTextView ctv= mGlms.get(position-1).findViewByPosition(3).findViewById(R.id.ctv);
            ctv.setChecked(true);

            switch (position){
                case 1:
                    et1.setVisibility(View.VISIBLE);
                    et1.setText(string);
                    break;
                case 2:
                    et2.setVisibility(View.VISIBLE);
                    et2.setText(string);
                    break;
                case 3:
                    et3.setVisibility(View.VISIBLE);
                    et3.setText(string);
                    break;
                default:
                    break;
            }
        }
    }
}