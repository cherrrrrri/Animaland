package com.example.animaland.selfroom;

import static com.example.animaland.tool.ThreadHelper.cachedThreadPool;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager.widget.ViewPager;

import com.app.hubert.guide.NewbieGuide;
import com.app.hubert.guide.model.GuidePage;
import com.example.animaland.Adapters.FragmentAdapter;
import com.example.animaland.R;
import com.example.animaland.dress.animalFragment;
import com.example.animaland.dress.head1Fragment;
import com.example.animaland.dress.head2Fragment;
import com.example.animaland.dress.head3Fragment;
import com.example.animaland.dress.head4Fragment;
import com.example.animaland.dress.moleFragment;
import com.example.animaland.dress.neck1Fragment;
import com.example.animaland.dress.neck2Fragment;
import com.example.animaland.dress.octopusFragment;
import com.example.animaland.dress.squirrelFragment;
import com.example.animaland.tool.DatabaseHelper;
import com.example.animaland.tool.User;

import java.sql.SQLException;
import java.util.ArrayList;

public class Dress extends AppCompatActivity implements View.OnClickListener,ViewPager.OnPageChangeListener {

    private View view_status;
    private ImageView iv_title_head;
    private ImageView iv_title_neck;
    private ImageView iv_title_ornament;
    private ImageView iv_title_animal;
    private ViewPager vp_content;
    private Toolbar toolbar;
    private ImageView back;
    private ImageView back2;

    private ImageView plant;
    private ImageView clock;
    private ImageView squirrel;
    private ImageView mole;
    private ImageView octopus;
    private ImageView headImage1;
    private ImageView headImage2;
    private ImageView headImage3;
    private ImageView headImage4;
    private ImageView neckImage1;
    private ImageView neckImage2;
    private ImageView neckImage3;
    private ImageView neckImage4;

    private TextView money;


    int isShowingO1=0;//0：装饰物
    int isShowingO2=0;
    int isShowing11=0;//1：头饰
    int isShowing12=0;
    int isShowing13=0;
    int isShowing14=0;
    int isShowing21=0;//2:颈饰
    int isShowing22=0;
    int isShowing23=0;
    int isShowing24=0;
    int isShowing31=0;//3：动物
    int isShowing32=0;
    int isShowing33=0;

    int coin;

    boolean isBought11;
    boolean isBought12;
    boolean isBought13;
    boolean isBought14;
    boolean isBought21;
    boolean isBought22;
    boolean isBought23;
    boolean isBought24;
    boolean isBought31;
    boolean isBought32;
    boolean isBought33;
    boolean isBought34;

    FragmentManager fm = getSupportFragmentManager();


    plantFragment plantFragment;
    clockFragment clockFragment;
    com.example.animaland.dress.squirrelFragment squirrelFragment;
    com.example.animaland.dress.moleFragment moleFragment;
    com.example.animaland.dress.octopusFragment octopusFragment;

    com.example.animaland.dress.head1Fragment head1Fragment;
    com.example.animaland.dress.head2Fragment head2Fragment;
    com.example.animaland.dress.head3Fragment head3Fragment;
    com.example.animaland.dress.head4Fragment head4Fragment;

    com.example.animaland.dress.neck1Fragment neck1Fragment;
    com.example.animaland.dress.neck2Fragment neck2Fragment;

    RelativeLayout rl;

    DatabaseHelper db = new DatabaseHelper();



    static String head = null;
    static String neck = null;
    static String animal = null;
    static String ornament = null;

    User u = new User();


    Handler handler = new Handler(Looper.myLooper()){
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            if(msg.what==0){
                head = (String) msg.obj;


            }else if(msg.what == 1){
                neck = (String) msg.obj;
            }else if(msg.what == 2){
                animal = (String)  msg.obj;
            }else if(msg.what == 3){
                ornament = (String) msg.obj;
            }else if(msg.what == 4){
                money.setText(coin+"");
            }

        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        setContentView(R.layout.dress);
        Intent intent3=new Intent(this,selfRoomMainActivity.class);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
            getWindow().setStatusBarColor(Color.parseColor("#ffce3d3a"));

        initView();
        initContentFragment();


        // init();//初始化
        //获取数据库中的编号
        //  Thread thread = new Thread(new Runnable() {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                try {
                    head = db.getHead();
                    neck = db.getNeck();
                    animal = db.getAnimal();
                    ornament = db.getOrnament();
                    coin = db.getCoin();
                    System.out.println(head+" "+neck+" "+ animal + " " + ornament);
                    initData();
                    init();
                    handler.sendEmptyMessage(4);
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
        back.setOnClickListener(new View.OnClickListener() {//返回
            @Override
            public void onClick(View view) {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        startActivity(intent3);
                        overridePendingTransition(android.R.anim.fade_in,
                                android.R.anim.fade_out);/*或许能用其它更炫酷的动画特效*/

                         Runnable runnable1 = new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    if( isShowing31==0 && isShowing32==0 && isShowing33==0){//没选动物
                                        db.setAnimal(null);
                                        db.setOrnament(null);
                                        db.setHead(null);
                                        db.setNeck(null);
                                    }else {//有动物
                                        if(isShowing31==1)
                                            db.setAnimal(1+"");
                                        if(isShowing32==1)
                                            db.setAnimal(2+"");
                                        if(isShowing33==1)
                                            db.setAnimal(3+"");

                                        //头饰
                                        if (isShowing11 == 0 && isShowing12 == 0 && isShowing13 == 0 && isShowing14 == 0) {//没有头饰
                                            db.setHead(null);
                                        } else {//有头饰
                                            if (isShowing11 == 1) {//头饰
                                                db.setHead(1 + "");
                                            }
                                            if (isShowing12 == 1) {
                                                db.setHead(2 + "");
                                            }
                                            if (isShowing13 == 1) {
                                                db.setHead(3 + "");
                                            }
                                            if (isShowing14 == 1) {
                                                db.setHead(4 + "");
                                            }
                                        }

                                        if (isShowingO1 == 0 && isShowingO2 == 0) {//没有摆件
                                            db.setOrnament(null);
                                        } else {
                                            if (isShowingO1 == 1) {//植物
                                                db.setOrnament(1 + "");
                                            }
                                            if (isShowingO2 == 1) {//咖啡机
                                                db.setOrnament(2 + "");
                                            }
                                        }

                                        if (isShowing21 == 0 && isShowing22 == 0) {//没有颈饰
                                            db.setNeck(null);
                                        } else {
                                            if (isShowing21 == 1) {//颈饰
                                                db.setNeck(1 + "");
                                            }
                                            if (isShowing22 == 1) {
                                                db.setNeck(2 + "");
                                            }
                                        }
                                    }
                                    buy();
                                    db.setCoin(coin);//保存金币
                                }catch (SQLException e) {
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
                         cachedThreadPool.execute(runnable1);
//                        thread.start();
                    }

                }, 400);
            }
        });


        SharedPreferences preferences = getSharedPreferences("count4", Context.MODE_PRIVATE);
        boolean count4 = preferences.getBoolean("count4",true);
        if(count4==true) {
            NewbieGuide.with(this)
                    .setLabel("guide1")
                    .alwaysShow(true)
                    .addGuidePage(GuidePage.newInstance()
                            .setLayoutRes(R.layout.dress_guide))
                    .addGuidePage(GuidePage.newInstance()
                            .addHighLight(back2)
                            .setLayoutRes(R.layout.dress_guide2))
                    .addGuidePage(GuidePage.newInstance()
                            .addHighLight(back2)
                            .setLayoutRes(R.layout.dress_guide3))
                    .show();
        }
        SharedPreferences.Editor editor=preferences.edit();
        editor.putBoolean("count4",false);
        editor.commit();


    }


    private void initView() {


        LayoutInflater factory = LayoutInflater.from(Dress.this);
        View layout1 = factory.inflate(R.layout.head_fragment, null);
        View layout2 = factory.inflate(R.layout.neck_fragment, null);
        View layout3 = factory.inflate(R.layout.ornament_fragment, null);
        View layout4 = factory.inflate(R.layout.animal_fragment, null);
        plant=(ImageView) layout3.findViewById(R.id.ornamentImage1);
        clock=(ImageView) layout3.findViewById(R.id.ornamentImage2);
        squirrel=(ImageView) layout4.findViewById(R.id.squirrel);
        mole=(ImageView) layout4.findViewById(R.id.mole);
        octopus=(ImageView) layout4.findViewById(R.id.octopus);
        headImage1=(ImageView) layout1.findViewById(R.id.headImage1);
        headImage2=(ImageView) layout1.findViewById(R.id.headImage2);
        headImage3=(ImageView) layout1.findViewById(R.id.headImage3);
        headImage4=(ImageView) layout1.findViewById(R.id.headImage4);
        neckImage1=(ImageView) layout2.findViewById(R.id.neckImage1);
        neckImage2=(ImageView) layout2.findViewById(R.id.neckImage2);



        view_status = (View) findViewById(R.id.view_status);
        iv_title_head = (ImageView) findViewById(R.id.iv_title_head);
        iv_title_neck = (ImageView) findViewById(R.id.iv_title_neck);
        iv_title_ornament = (ImageView) findViewById(R.id.iv_title_ornament);
        iv_title_animal = (ImageView) findViewById(R.id.iv_title_animal);
        vp_content = (ViewPager) findViewById(R.id.vp_content);
        toolbar = (Toolbar) findViewById(R.id.toolbars);
        back=(ImageView) findViewById(R.id.back);
        back2=(ImageView) findViewById(R.id.back2);
        money=(TextView) findViewById(R.id.moneyAmount) ;

        iv_title_head.setOnClickListener(this);
        iv_title_neck.setOnClickListener(this);
        iv_title_ornament.setOnClickListener(this);
        iv_title_animal.setOnClickListener(this);

        plant.setOnClickListener(this);
        clock.setOnClickListener(this);
        squirrel.setOnClickListener(this);
        mole.setOnClickListener(this);
        octopus.setOnClickListener(this);
        headImage1.setOnClickListener(this);
        headImage2.setOnClickListener(this);
        headImage3.setOnClickListener(this);
        headImage4.setOnClickListener(this);
        neckImage1.setOnClickListener(this);
        neckImage2.setOnClickListener(this);




    }

    private void initContentFragment() {
        ArrayList<Fragment> mFragmentList = new ArrayList<>();
        mFragmentList.add(new headFragment());
        mFragmentList.add(new neckFragment());
        mFragmentList.add(new ornamentFragment());
        mFragmentList.add(new animalFragment());
        FragmentAdapter adapter = new FragmentAdapter(getSupportFragmentManager(), mFragmentList);
        vp_content.setAdapter(adapter);
        vp_content.setOffscreenPageLimit(4);
        vp_content.addOnPageChangeListener(this);
        vp_content.setPageTransformer(true,new DepthPageTransformer());

        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null)
            actionBar.setDisplayShowTitleEnabled(false);

        setCurrentItem(0);

    }

    //初始化装扮
    private void init(){
        //动物初始化
        if(animal!=null) {
            FragmentTransaction ft = fm.beginTransaction();
            System.out.println("ini");


            if (animal.equals("1")) {
                isShowing31 = 1;
                if (squirrelFragment == null) {
                    squirrelFragment = new squirrelFragment();
                    ft.add(R.id.animal_container, squirrelFragment);
                }
                System.out.println(animal);
                ft.show(squirrelFragment);
            } else if (animal.equals("2")) {
                isShowing32 = 1;
                if (moleFragment == null) {
                    moleFragment = new moleFragment();
                    ft.add(R.id.animal_container, moleFragment);
                }
                System.out.println(animal);

                ft.show(moleFragment);

            } else if (animal.equals("3")) {
                isShowing33 = 1;
                if (octopusFragment == null) {
                    octopusFragment = new octopusFragment();
                    ft.add(R.id.animal_container, octopusFragment);
                }
                System.out.println(animal);

                ft.show(octopusFragment);
            } else {

            }

            if (isShowing33 == 1 || isShowing32 == 1 || isShowing31 == 1) {
                //头饰初始化
                if (head != null) {
                    if (head.equals("1")) {
                        isShowing11 = 1;
                        if (head1Fragment == null) {
                            head1Fragment = new head1Fragment();
                            ft.add(R.id.head_container, head1Fragment);
                        }
                        ft.show(head1Fragment);

                    } else if (head.equals("2")) {
                        isShowing12 = 1;
                        if (head2Fragment == null) {
                            head2Fragment = new head2Fragment();
                            ft.add(R.id.head_container, head2Fragment);
                        }
                        ft.show(head2Fragment);

                    } else if (head.equals("3")) {
                        isShowing13 = 1;
                        if (head3Fragment == null) {
                            head3Fragment = new head3Fragment();
                            ft.add(R.id.head_container, head3Fragment);
                        }
                        ft.show(head3Fragment);

                    } else if (head.equals("4")) {
                        isShowing14 = 1;
                        if (head4Fragment == null) {
                            head4Fragment = new head4Fragment();
                            ft.add(R.id.head_container, head4Fragment);
                        }
                        ft.show(head4Fragment);

                    }
                }

                if (neck != null) {
                    //颈饰
                    if (neck.equals("1")) {
                        isShowing21 = 1;
                        if (neck1Fragment == null) {
                            neck1Fragment = new neck1Fragment();
                            ft.add(R.id.neck_container, neck1Fragment);
                        }
                        ft.show(neck1Fragment);
                    } else if (neck.equals("2")) {
                        isShowing22 = 1;
                        if (neck2Fragment == null) {
                            neck2Fragment = new neck2Fragment();
                            ft.add(R.id.neck_container, neck2Fragment);
                        }
                        ft.show(neck2Fragment);
                    }
                }

                if (ornament != null) {
                    //摆件
                    if (ornament.equals("1")) {
                        isShowingO1 = 1;

                        if (plantFragment == null) {
                            plantFragment = new plantFragment();
                            ft.add(R.id.ornament_container, plantFragment);
                        }
                        ft.show(plantFragment);

                    } else if (ornament.equals("2")) {
                        isShowingO2 = 1;
                        if (clockFragment == null) {
                            clockFragment = new clockFragment();
                            ft.add(R.id.ornament_container, clockFragment);
                        }
                        ft.show(clockFragment);

                    }
                }
            }
            ft.commit();
        }
    }
    private void setCurrentItem(int i) {
        vp_content.setCurrentItem(i);
        iv_title_head.setSelected(false);
        iv_title_neck.setSelected(false);
        iv_title_ornament.setSelected(false);
        iv_title_animal.setSelected(false);
        switch (i) {
            case 0:
                iv_title_head.setSelected(true);
                break;
            case 1:
                iv_title_neck.setSelected(true);
                break;
            case 2:
                iv_title_ornament.setSelected(true);
                break;
            case 3:
                iv_title_animal.setSelected(true);
                break;
        }


    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        setCurrentItem(position);
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.iv_title_head:
                if(vp_content.getCurrentItem()!=0)
                    setCurrentItem(0);
                break;
            case R.id.iv_title_neck:
                if(vp_content.getCurrentItem()!=1)
                    setCurrentItem(1);
                break;
            case R.id.iv_title_ornament:
                if(vp_content.getCurrentItem()!=2)
                    setCurrentItem(2);
                break;
            case R.id.iv_title_animal:
                if(vp_content.getCurrentItem()!=3)
                    setCurrentItem(3);
                break;
            case R.id.neckImage1:
                setTabSelection(11);
                break;
            case R.id.neckImage2:
                setTabSelection(12);
                break;


            case R.id.headImage1:
                setTabSelection(01);
                break;
            case R.id.headImage2:
                setTabSelection(02);
                break;
            case R.id.headImage3:
                setTabSelection(03);
                break;
            case R.id.headImage4:
                setTabSelection(04);
                break;
            case R.id.ornamentImage1:
                setTabSelection(21);
                break;

            case R.id.ornamentImage2:
                setTabSelection(22);
                break;
            case R.id.squirrel:
                setTabSelection(31);
                break;
            case R.id.mole:
                setTabSelection(32);
                break;
            case R.id.octopus:
                setTabSelection(33);
                break;


        }
    }

    private void setTabSelection(int index) {


        FragmentTransaction ft = fm.beginTransaction();
        switch (index) {
                    case 01://头饰1
                        if (isShowing31 == 1 || isShowing32 == 1 || isShowing33 == 1) {//选了动物的情况下
                            if (isShowing11 == 0) {
                                    //未买
                                    if(!isBought11){
                                        if(coin-10>=0){
                                            coin-=10;//已买：直接执行
                                            isBought11=true;
                                            Toast.makeText(Dress.this,"买入头饰，金币-10",Toast.LENGTH_SHORT).show();
                                            handler.sendEmptyMessage(4);
                                            isShowing11 = 1;
                                            if (head1Fragment == null) {
                                                head1Fragment = new head1Fragment();
                                                ft.add(R.id.head_container, head1Fragment);
                                            } else {
                                                ft.show(head1Fragment);
                                            }
                                            if (isShowing12 == 1) {
                                                ft.hide(head2Fragment);
                                                isShowing12 = 0;
                                            }
                                            if (isShowing13 == 1) {
                                                ft.hide(head3Fragment);
                                                isShowing13 = 0;
                                            }
                                            if (isShowing14 == 1) {
                                                ft.hide(head4Fragment);
                                                isShowing14 = 0;
                                            }
                                            if (isShowing21 == 1) {
                                                ft.hide(neck1Fragment);
                                                isShowing21 = 0;
                                            }
                                            if (isShowing22 == 1) {
                                                ft.hide(neck2Fragment);
                                                isShowing22 = 0;
                                            }
                                            if (isShowingO1 == 1) {
                                                ft.hide(plantFragment);
                                                isShowingO1 = 0;
                                            }
                                            if (isShowingO2 == 1) {
                                                ft.hide(clockFragment);
                                                isShowingO2 = 0;
                                            }
                                        }else{
                                            Toast.makeText(Dress.this,"金币不足，请先去挣金币哦~~",Toast.LENGTH_SHORT).show();
                                        }
                                    }else {
                                        //已买：直接执行
                                        isShowing11 = 1;
                                        if (head1Fragment == null) {
                                            head1Fragment = new head1Fragment();
                                            ft.add(R.id.head_container, head1Fragment);
                                        } else {
                                            ft.show(head1Fragment);
                                        }
                                        if (isShowing12 == 1) {
                                            ft.hide(head2Fragment);
                                            isShowing12 = 0;
                                        }
                                        if (isShowing13 == 1) {
                                            ft.hide(head3Fragment);
                                            isShowing13 = 0;
                                        }
                                        if (isShowing14 == 1) {
                                            ft.hide(head4Fragment);
                                            isShowing14 = 0;
                                        }
                                        if (isShowing21 == 1) {
                                            ft.hide(neck1Fragment);
                                            isShowing21 = 0;
                                        }
                                        if (isShowing22 == 1) {
                                            ft.hide(neck2Fragment);
                                            isShowing22 = 0;
                                        }
                                        if (isShowingO1 == 1) {
                                            ft.hide(plantFragment);
                                            isShowingO1 = 0;
                                        }
                                        if (isShowingO2 == 1) {
                                            ft.hide(clockFragment);
                                            isShowingO2 = 0;
                                        }
                                    }
                            } else {
                                ft.hide(head1Fragment);
                                isShowing11 = 0;
                            }
                        }
                        break;
                    case 02://头饰2
                        if (isShowing31 == 1 || isShowing32 == 1 || isShowing33 == 1) {
                            if (isShowing12 == 0) {
                                //未买
                                if(!isBought12) {
                                    if (coin - 10 >= 0) {
                                        coin -= 10;//已买：直接执行
                                        isBought12 = true;
                                        Toast.makeText(Dress.this, "买入头饰，金币-10", Toast.LENGTH_SHORT).show();
                                        handler.sendEmptyMessage(4);

                                        isShowing12 = 1;
                                        if (head2Fragment == null) {
                                            head2Fragment = new head2Fragment();
                                            ft.add(R.id.head_container, head2Fragment);
                                        } else {
                                            ft.show(head2Fragment);
                                        }
                                        if (isShowing11 == 1) {
                                            ft.hide(head1Fragment);
                                            isShowing11 = 0;
                                        }
                                        if (isShowing13 == 1) {
                                            ft.hide(head3Fragment);
                                            isShowing13 = 0;
                                        }
                                        if (isShowing14 == 1) {
                                            ft.hide(head4Fragment);
                                            isShowing14 = 0;
                                        }
                                        if (isShowing21 == 1) {
                                            ft.hide(neck1Fragment);
                                            isShowing21 = 0;
                                        }
                                        if (isShowing22 == 1) {
                                            ft.hide(neck2Fragment);
                                            isShowing22 = 0;
                                        }
                                        if (isShowingO1 == 1) {
                                            ft.hide(plantFragment);
                                            isShowingO1 = 0;
                                        }
                                        if (isShowingO2 == 1) {
                                            ft.hide(clockFragment);
                                            isShowingO2 = 0;
                                        }
                                    }
                                    else{
                                        Toast.makeText(Dress.this,"金币不足，请先去挣金币哦~~",Toast.LENGTH_SHORT).show();
                                    }
                                }else {
                                    isShowing12 = 1;
                                    if (head2Fragment == null) {
                                        head2Fragment = new head2Fragment();
                                        ft.add(R.id.head_container, head2Fragment);
                                    } else {
                                        ft.show(head2Fragment);
                                    }
                                    if (isShowing11 == 1) {
                                        ft.hide(head1Fragment);
                                        isShowing11 = 0;
                                    }
                                    if (isShowing13 == 1) {
                                        ft.hide(head3Fragment);
                                        isShowing13 = 0;
                                    }
                                    if (isShowing14 == 1) {
                                        ft.hide(head4Fragment);
                                        isShowing14 = 0;
                                    }
                                    if (isShowing21 == 1) {
                                        ft.hide(neck1Fragment);
                                        isShowing21 = 0;
                                    }
                                    if (isShowing22 == 1) {
                                        ft.hide(neck2Fragment);
                                        isShowing22 = 0;
                                    }
                                    if (isShowingO1 == 1) {
                                        ft.hide(plantFragment);
                                        isShowingO1 = 0;
                                    }
                                    if (isShowingO2 == 1) {
                                        ft.hide(clockFragment);
                                        isShowingO2 = 0;
                                    }
                                }

                            } else {
                                ft.hide(head2Fragment);
                                isShowing12 = 0;
                            }
                        }
                        break;
                    case 03://头饰3
                        if (isShowing31 == 1 || isShowing32 == 1 || isShowing33 == 1) {
                            if (isShowing13 == 0) {
                                if(!isBought13) {
                                    if (coin - 10 >= 0) {
                                        coin -= 10;//已买：直接执行
                                        isBought13 = true;
                                        Toast.makeText(Dress.this, "买入头饰，金币-10", Toast.LENGTH_SHORT).show();
                                        handler.sendEmptyMessage(4);

                                        isShowing13 = 1;
                                        if (head3Fragment == null) {
                                            head3Fragment = new head3Fragment();
                                            ft.add(R.id.head_container, head3Fragment);
                                        } else {
                                            ft.show(head3Fragment);
                                        }
                                        if (isShowing11 == 1) {
                                            ft.hide(head1Fragment);
                                            isShowing11 = 0;
                                        }
                                        if (isShowing12 == 1) {
                                            ft.hide(head2Fragment);
                                            isShowing12 = 0;
                                        }
                                        if (isShowing14 == 1) {
                                            ft.hide(head4Fragment);
                                            isShowing14 = 0;
                                        }
                                        if (isShowing21 == 1) {
                                            ft.hide(neck1Fragment);
                                            isShowing21 = 0;
                                        }
                                        if (isShowing22 == 1) {
                                            ft.hide(neck2Fragment);
                                            isShowing22 = 0;
                                        }
                                        if (isShowingO1 == 1) {
                                            ft.hide(plantFragment);
                                            isShowingO1 = 0;
                                        }
                                        if (isShowingO2 == 1) {
                                            ft.hide(clockFragment);
                                            isShowingO2 = 0;
                                        }
                                    } else {
                                        Toast.makeText(Dress.this,"金币不足，请先去挣金币哦~~",Toast.LENGTH_SHORT).show();

                                    }
                                }else {
                                    isShowing13 = 1;
                                    if (head3Fragment == null) {
                                        head3Fragment = new head3Fragment();
                                        ft.add(R.id.head_container, head3Fragment);
                                    } else {
                                        ft.show(head3Fragment);
                                    }
                                    if (isShowing11 == 1) {
                                        ft.hide(head1Fragment);
                                        isShowing11 = 0;
                                    }
                                    if (isShowing12 == 1) {
                                        ft.hide(head2Fragment);
                                        isShowing12 = 0;
                                    }
                                    if (isShowing14 == 1) {
                                        ft.hide(head4Fragment);
                                        isShowing14 = 0;
                                    }
                                    if (isShowing21 == 1) {
                                        ft.hide(neck1Fragment);
                                        isShowing21 = 0;
                                    }
                                    if (isShowing22 == 1) {
                                        ft.hide(neck2Fragment);
                                        isShowing22 = 0;
                                    }
                                    if (isShowingO1 == 1) {
                                        ft.hide(plantFragment);
                                        isShowingO1 = 0;
                                    }
                                    if (isShowingO2 == 1) {
                                        ft.hide(clockFragment);
                                        isShowingO2 = 0;
                                    }
                                }
                            } else {
                                ft.hide(head3Fragment);
                                isShowing13 = 0;
                            }
                        }
                        break;
                    case 04://头饰4
                        if (isShowing31 == 1 || isShowing32 == 1 || isShowing33 == 1) {
                            if (isShowing14 == 0) {
                                if(!isBought14) {
                                    if (coin - 10 >= 0) {
                                        coin -= 10;//已买：直接执行
                                        isBought14 = true;
                                        Toast.makeText(Dress.this, "买入头饰，金币-10", Toast.LENGTH_SHORT).show();
                                        handler.sendEmptyMessage(4);

                                        isShowing14 = 1;
                                        if (head4Fragment == null) {
                                            head4Fragment = new head4Fragment();
                                            ft.add(R.id.head_container, head4Fragment);
                                        } else {
                                            ft.show(head4Fragment);
                                        }
                                        if (isShowing11 == 1) {
                                            ft.hide(head1Fragment);
                                            isShowing11 = 0;
                                        }
                                        if (isShowing12 == 1) {
                                            ft.hide(head2Fragment);
                                            isShowing12 = 0;
                                        }
                                        if (isShowing13 == 1) {
                                            ft.hide(head3Fragment);
                                            isShowing13 = 0;
                                        }
                                        if (isShowing21 == 1) {
                                            ft.hide(neck1Fragment);
                                            isShowing21 = 0;
                                        }
                                        if (isShowing22 == 1) {
                                            ft.hide(neck2Fragment);
                                            isShowing22 = 0;
                                        }
                                        if (isShowingO1 == 1) {
                                            ft.hide(plantFragment);
                                            isShowingO1 = 0;
                                        }
                                        if (isShowingO2 == 1) {
                                            ft.hide(clockFragment);
                                            isShowingO2 = 0;
                                        }
                                    } else {
                                        Toast.makeText(Dress.this,"金币不足，请先去挣金币哦~~",Toast.LENGTH_SHORT).show();

                                    }
                                }else {
                                    isShowing14 = 1;
                                    if (head4Fragment == null) {
                                        head4Fragment = new head4Fragment();
                                        ft.add(R.id.head_container, head4Fragment);
                                    } else {
                                        ft.show(head4Fragment);
                                    }
                                    if (isShowing11 == 1) {
                                        ft.hide(head1Fragment);
                                        isShowing11 = 0;
                                    }
                                    if (isShowing12 == 1) {
                                        ft.hide(head2Fragment);
                                        isShowing12 = 0;
                                    }
                                    if (isShowing13 == 1) {
                                        ft.hide(head3Fragment);
                                        isShowing13 = 0;
                                    }
                                    if (isShowing21 == 1) {
                                        ft.hide(neck1Fragment);
                                        isShowing21 = 0;
                                    }
                                    if (isShowing22 == 1) {
                                        ft.hide(neck2Fragment);
                                        isShowing22 = 0;
                                    }
                                    if (isShowingO1 == 1) {
                                        ft.hide(plantFragment);
                                        isShowingO1 = 0;
                                    }
                                    if (isShowingO2 == 1) {
                                        ft.hide(clockFragment);
                                        isShowingO2 = 0;
                                    }
                                }
                            } else {
                                ft.hide(head4Fragment);
                                isShowing14 = 0;
                            }
                        }
                        break;
                    case 11://颈饰1
                        if (isShowing31 == 1 || isShowing32 == 1 || isShowing33 == 1) {
                            if (isShowing21 == 0) {
                                if (!isBought21) {
                                    if (coin - 10 >= 0) {
                                        coin -= 10;//已买：直接执行
                                        isBought21 = true;
                                        Toast.makeText(Dress.this, "买入颈饰，金币-10", Toast.LENGTH_SHORT).show();
                                        handler.sendEmptyMessage(4);

                                        isShowing21 = 1;
                                        if (neck1Fragment == null) {
                                            neck1Fragment = new neck1Fragment();
                                            ft.add(R.id.neck_container, neck1Fragment);
                                        } else {
                                            ft.show(neck1Fragment);
                                        }
                                        if (isShowing22 == 1) {
                                            ft.hide(neck2Fragment);
                                            isShowing22 = 0;
                                        }
                                        if (isShowing11 == 1) {
                                            ft.hide(head1Fragment);
                                            isShowing11 = 0;
                                        }
                                        if (isShowing12 == 1) {
                                            ft.hide(head2Fragment);
                                            isShowing12 = 0;
                                        }
                                        if (isShowing13 == 1) {
                                            ft.hide(head3Fragment);
                                            isShowing13 = 0;
                                        }
                                        if (isShowing14 == 1) {
                                            ft.hide(head4Fragment);
                                            isShowing14 = 0;
                                        }
                                        if (isShowingO1 == 1) {
                                            ft.hide(plantFragment);
                                            isShowingO1 = 0;
                                        }
                                        if (isShowingO2 == 1) {
                                            ft.hide(clockFragment);
                                            isShowingO2 = 0;
                                        }
                                    } else {
                                        Toast.makeText(Dress.this,"金币不足，请先去挣金币哦~~",Toast.LENGTH_SHORT).show();

                                    }
                                } else{
                                    isShowing21 = 1;
                                    if (neck1Fragment == null) {
                                        neck1Fragment = new neck1Fragment();
                                        ft.add(R.id.neck_container, neck1Fragment);
                                    } else {
                                        ft.show(neck1Fragment);
                                    }
                                    if (isShowing22 == 1) {
                                        ft.hide(neck2Fragment);
                                        isShowing22 = 0;
                                    }
                                    if (isShowing11 == 1) {
                                        ft.hide(head1Fragment);
                                        isShowing11 = 0;
                                    }
                                    if (isShowing12 == 1) {
                                        ft.hide(head2Fragment);
                                        isShowing12 = 0;
                                    }
                                    if (isShowing13 == 1) {
                                        ft.hide(head3Fragment);
                                        isShowing13 = 0;
                                    }
                                    if (isShowing14 == 1) {
                                        ft.hide(head4Fragment);
                                        isShowing14 = 0;
                                    }
                                    if (isShowingO1 == 1) {
                                        ft.hide(plantFragment);
                                        isShowingO1 = 0;
                                    }
                                    if (isShowingO2 == 1) {
                                        ft.hide(clockFragment);
                                        isShowingO2 = 0;
                                    }
                                }

                            } else {
                                ft.hide(neck1Fragment);
                                isShowing21 = 0;
                            }
                        }
                        break;
                    case 12://颈饰2
                        if (isShowing31 == 1 || isShowing32 == 1 || isShowing33 == 1) {
                            if (isShowing22 == 0) {
                                if (!isBought22) {
                                    if (coin - 10 >= 0) {
                                        coin -= 10;//已买：直接执行
                                        isBought22 = true;
                                        Toast.makeText(Dress.this, "买入颈饰，金币-10", Toast.LENGTH_SHORT).show();
                                        handler.sendEmptyMessage(4);

                                        isShowing22 = 1;
                                        if (neck2Fragment == null) {
                                            neck2Fragment = new neck2Fragment();
                                            ft.add(R.id.neck_container, neck2Fragment);
                                        } else {
                                            ft.show(neck2Fragment);
                                        }
                                        if (isShowing21 == 1) {
                                            ft.hide(neck1Fragment);
                                            isShowing21 = 0;
                                        }
                                        if (isShowing11 == 1) {
                                            ft.hide(head1Fragment);
                                            isShowing11 = 0;
                                        }
                                        if (isShowing12 == 1) {
                                            ft.hide(head2Fragment);
                                            isShowing12 = 0;
                                        }
                                        if (isShowing13 == 1) {
                                            ft.hide(head3Fragment);
                                            isShowing13 = 0;
                                        }
                                        if (isShowing14 == 1) {
                                            ft.hide(head4Fragment);
                                            isShowing14 = 0;
                                        }
                                        if (isShowingO1 == 1) {
                                            ft.hide(plantFragment);
                                            isShowingO1 = 0;
                                        }
                                        if (isShowingO2 == 1) {
                                            ft.hide(clockFragment);
                                            isShowingO2 = 0;
                                        }
                                    } else {
                                        Toast.makeText(Dress.this,"金币不足，请先去挣金币哦~~",Toast.LENGTH_SHORT).show();

                                    }
                                }else {
                                    isShowing22 = 1;
                                    if (neck2Fragment == null) {
                                        neck2Fragment = new neck2Fragment();
                                        ft.add(R.id.neck_container, neck2Fragment);
                                    } else {
                                        ft.show(neck2Fragment);
                                    }
                                    if (isShowing21 == 1) {
                                        ft.hide(neck1Fragment);
                                        isShowing21 = 0;
                                    }
                                    if (isShowing11 == 1) {
                                        ft.hide(head1Fragment);
                                        isShowing11 = 0;
                                    }
                                    if (isShowing12 == 1) {
                                        ft.hide(head2Fragment);
                                        isShowing12 = 0;
                                    }
                                    if (isShowing13 == 1) {
                                        ft.hide(head3Fragment);
                                        isShowing13 = 0;
                                    }
                                    if (isShowing14 == 1) {
                                        ft.hide(head4Fragment);
                                        isShowing14 = 0;
                                    }
                                    if (isShowingO1 == 1) {
                                        ft.hide(plantFragment);
                                        isShowingO1 = 0;
                                    }
                                    if (isShowingO2 == 1) {
                                        ft.hide(clockFragment);
                                        isShowingO2 = 0;
                                    }
                                }
                            } else {
                                ft.hide(neck2Fragment);
                                isShowing22 = 0;
                            }
                        }
                        break;

                    case 21://植物
                        if (isShowingO1 == 0) {
                            if (!isBought31) {
                                if (coin - 10 >= 0) {
                                    coin -= 10;//已买：直接执行
                                    isBought31 = true;
                                    Toast.makeText(Dress.this, "买入摆件，金币-10", Toast.LENGTH_SHORT).show();
                                    handler.sendEmptyMessage(4);

                                    isShowingO1 = 1;
                                    if (plantFragment == null) {
                                        plantFragment = new plantFragment();
                                        ft.add(R.id.ornament_container, plantFragment);
                                    } else {
                                        ft.show(plantFragment);
                                    }
                                    if (isShowingO2 == 1) {
                                        ft.hide(clockFragment);
                                        isShowingO2 = 0;
                                    }
                                    if (isShowing11 == 1) {
                                        ft.hide(head1Fragment);
                                        isShowing11 = 0;
                                    }
                                    if (isShowing12 == 1) {
                                        ft.hide(head2Fragment);
                                        isShowing12 = 0;
                                    }
                                    if (isShowing13 == 1) {
                                        ft.hide(head3Fragment);
                                        isShowing13 = 0;
                                    }
                                    if (isShowing14 == 1) {
                                        ft.hide(head4Fragment);
                                        isShowing14 = 0;
                                    }
                                    if (isShowing21 == 1) {
                                        ft.hide(neck1Fragment);
                                        isShowing21 = 0;
                                    }
                                    if (isShowing22 == 1) {
                                        ft.hide(neck2Fragment);
                                        isShowing22 = 0;
                                    }
                                } else {
                                    Toast.makeText(Dress.this,"金币不足，请先去挣金币哦~~",Toast.LENGTH_SHORT).show();

                                }
                            }else {
                                isShowingO1 = 1;
                                if (plantFragment == null) {
                                    plantFragment = new plantFragment();
                                    ft.add(R.id.ornament_container, plantFragment);
                                } else {
                                    ft.show(plantFragment);
                                }
                                if (isShowingO2 == 1) {
                                    ft.hide(clockFragment);
                                    isShowingO2 = 0;
                                }
                                if (isShowing11 == 1) {
                                    ft.hide(head1Fragment);
                                    isShowing11 = 0;
                                }
                                if (isShowing12 == 1) {
                                    ft.hide(head2Fragment);
                                    isShowing12 = 0;
                                }
                                if (isShowing13 == 1) {
                                    ft.hide(head3Fragment);
                                    isShowing13 = 0;
                                }
                                if (isShowing14 == 1) {
                                    ft.hide(head4Fragment);
                                    isShowing14 = 0;
                                }
                                if (isShowing21 == 1) {
                                    ft.hide(neck1Fragment);
                                    isShowing21 = 0;
                                }
                                if (isShowing22 == 1) {
                                    ft.hide(neck2Fragment);
                                    isShowing22 = 0;
                                }
                            }


                        } else {
                            ft.hide(plantFragment);
                            isShowingO1 = 0;
                        }
                        break;
                    case 22://咖啡机
                        if (isShowingO2 == 0) {
                            if (!isBought32) {
                                if (coin - 10 >= 0) {
                                    coin -= 10;//已买：直接执行
                                    isBought32 = true;
                                    Toast.makeText(Dress.this, "买入摆件，金币-10", Toast.LENGTH_SHORT).show();
                                    handler.sendEmptyMessage(4);

                                    isShowingO2 = 1;
                                    if (clockFragment == null) {
                                        clockFragment = new clockFragment();
                                        ft.add(R.id.ornament_container, clockFragment);
                                    } else {
                                        ft.show(clockFragment);
                                    }
                                    if (isShowingO1 == 1) {
                                        ft.hide(plantFragment);
                                        isShowingO1 = 0;
                                    }
                                    if (isShowing11 == 1) {
                                        ft.hide(head1Fragment);
                                        isShowing11 = 0;
                                    }
                                    if (isShowing12 == 1) {
                                        ft.hide(head2Fragment);
                                        isShowing12 = 0;
                                    }
                                    if (isShowing13 == 1) {
                                        ft.hide(head3Fragment);
                                        isShowing13 = 0;
                                    }
                                    if (isShowing14 == 1) {
                                        ft.hide(head4Fragment);
                                        isShowing14 = 0;
                                    }
                                    if (isShowing21 == 1) {
                                        ft.hide(neck1Fragment);
                                        isShowing21 = 0;
                                    }
                                    if (isShowing22 == 1) {
                                        ft.hide(neck2Fragment);
                                        isShowing22 = 0;
                                    }
                                } else {
                                    Toast.makeText(Dress.this,"金币不足，请先去挣金币哦~~",Toast.LENGTH_SHORT).show();

                                }
                            }else {
                                isShowingO2 = 1;
                                if (clockFragment == null) {
                                    clockFragment = new clockFragment();
                                    ft.add(R.id.ornament_container, clockFragment);
                                } else {
                                    ft.show(clockFragment);
                                }
                                if (isShowingO1 == 1) {
                                    ft.hide(plantFragment);
                                    isShowingO1 = 0;
                                }
                                if (isShowing11 == 1) {
                                    ft.hide(head1Fragment);
                                    isShowing11 = 0;
                                }
                                if (isShowing12 == 1) {
                                    ft.hide(head2Fragment);
                                    isShowing12 = 0;
                                }
                                if (isShowing13 == 1) {
                                    ft.hide(head3Fragment);
                                    isShowing13 = 0;
                                }
                                if (isShowing14 == 1) {
                                    ft.hide(head4Fragment);
                                    isShowing14 = 0;
                                }
                                if (isShowing21 == 1) {
                                    ft.hide(neck1Fragment);
                                    isShowing21 = 0;
                                }
                                if (isShowing22 == 1) {
                                    ft.hide(neck2Fragment);
                                    isShowing22 = 0;
                                }
                            }
                        } else {
                            ft.hide(clockFragment);
                            isShowingO2 = 0;
                        }

                        break;
                    case 31://松鼠
                        if (isShowing31 == 0) {
                            isShowing31 = 1;
                            if (squirrelFragment == null) {
                                squirrelFragment = new squirrelFragment();
                                ft.add(R.id.animal_container, squirrelFragment);
                            } else {
                                ft.show(squirrelFragment);
                            }
                            if (isShowing32 == 1) {
                                ft.hide(moleFragment);
                                isShowing32 = 0;
                            }
                            if (isShowing33 == 1) {
                                ft.hide(octopusFragment);
                                isShowing33 = 0;
                            }

                        } else {
                            if (squirrelFragment == null) {
                                squirrelFragment = new squirrelFragment();
                                ft.add(R.id.animal_container, squirrelFragment);
                            }
                        }

                        break;
                    case 32://鼹鼠
                        isShowing32 = 1;
                        if (moleFragment == null) {
                            moleFragment = new moleFragment();
                            ft.add(R.id.animal_container, moleFragment);
                        } else {
                            ft.show(moleFragment);
                        }
                        if (isShowing31 == 1) {
                            ft.hide(squirrelFragment);
                            isShowing31 = 0;
                        }
                        if (isShowing33 == 1) {
                            ft.hide(octopusFragment);
                            isShowing33 = 0;
                        }
                        break;
                    case 33://章鱼
                        if (isShowing33 == 0) {
                            isShowing33 = 1;
                            if (octopusFragment == null) {
                                octopusFragment = new octopusFragment();
                                ft.add(R.id.animal_container, octopusFragment);
                            } else {
                                ft.show(octopusFragment);
                            }
                            if (isShowing32 == 1) {
                                ft.hide(moleFragment);
                                isShowing32 = 0;
                            }
                            if (isShowing31 == 1) {
                                ft.hide(squirrelFragment);
                                isShowing31 = 0;
                            }

                        }


                        break;


                }

        ft.commit();
    }

    private void initData(){
                try {
                    isBought11 = db.isBought(11);
                    isBought12 = db.isBought(12);
                    isBought13 = db.isBought(13);
                    isBought14 = db.isBought(14);
                    isBought21 = db.isBought(21);
                    isBought22 = db.isBought(22);
                    isBought23 = db.isBought(23);
                    isBought24 = db.isBought(24);
                    isBought31 = db.isBought(31);
                    isBought32 = db.isBought(32);
                    isBought33 = db.isBought(33);
                    isBought34 = db.isBought(34);
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

    private void buy(){

            try {
                if(isBought11==true) {
                    db.buy(11);
                }
                if(isBought12==true)
                    db.buy(12);
                if(isBought13==true)
                    db.buy(13);
                if(isBought14==true)
                    db.buy(14);
                if(isBought21==true)
                    db.buy(21);
                if(isBought22==true)
                    db.buy(22);
                if(isBought23==true)
                    db.buy(23);
                if(isBought24==true)
                    db.buy(24);
                if(isBought31==true)
                    db.buy(31);
                if(isBought32==true)
                    db.buy(32);
                if(isBought33==true)
                    db.buy(33);
                if(isBought34==true)
                    db.buy(34);

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

    //用于隐藏fragment
    private void hideFragment(FragmentTransaction ft){
        if(plantFragment!=null){
            ft.hide(plantFragment);
        }
    }

    public void onStop() {
        super.onStop();
    }
}






