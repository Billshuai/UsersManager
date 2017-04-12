package com.billshuai.testapplication;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.Toast;

public class MainActivity extends Activity implements OnClickListener {

    private ImageView select;
    private PopupWindow pw;
    private int width;
    private LinearLayout parent, option;
    private EditText et_user, et_pwd;
    private ListView listView;
    private Button login;
    private CheckBox checkBox;
    List<String> userList;
    List<String> passList;
    UsersAdapter usersAdapter;

    MyDBOpenHelper databaseHelper;
    private SQLiteDatabase db;
    //数据库名称
    private static final String DATABASE_NAME = "billshuai.db";
    //数据库版本号
    private static final int DATABASE_VERSION = 1;
    //表名
    private static final String TABLE_NAME = "users";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        databaseHelper = new MyDBOpenHelper(this, DATABASE_NAME, null, DATABASE_VERSION);
        db = databaseHelper.getReadableDatabase();
        init();
    }

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        switch (v.getId()) {
            case R.id.select:
                pw.showAsDropDown(parent, 15, -4);
                listView.deferNotifyDataSetChanged();
                break;
            case R.id.login:
                login();
                break;
        }
    }

    private void init() {

        et_user = (EditText) findViewById(R.id.et_user);
        et_pwd = (EditText) findViewById(R.id.et_pwd);
        parent = (LinearLayout) findViewById(R.id.llayout);

        select = (ImageView) findViewById(R.id.select);
        login = (Button) findViewById(R.id.login);
        checkBox = (CheckBox) findViewById(R.id.checkBox1);


        select.setOnClickListener(this);
        login.setOnClickListener(this);

        //读取已经记住的用户名与密码

        userList = new ArrayList<String>();
        passList = new ArrayList<String>();
        query(db, userList, passList);
        usersAdapter = new UsersAdapter(this);
        usersAdapter.setItemsUser(userList);
        usersAdapter.setItemsPass(passList);
        option = (LinearLayout) getLayoutInflater().inflate(R.layout.option,
                null);

        listView = (ListView) option.findViewById(R.id.op);

        listView.setAdapter(usersAdapter);

        usersAdapter.setOnDeleteListener(new UsersAdapter.OnDeleteListener()

        {
            @Override
            public void onDelete(String name) {
                //得到可写的SQLiteDatabase对象
                SQLiteDatabase dbDelete = databaseHelper.getWritableDatabase();
                String whereClauses = "name=?";
                String[] whereArgs = {name};
                //调用delete方法，删除数据
                dbDelete.delete(TABLE_NAME, whereClauses, whereArgs);
                dbDelete.close();
            }
        });

        //获取屏幕的宽度并设置popupwindow的宽度为width,我这里是根据布局控件所占的权重
        WindowManager wManager = (WindowManager) getSystemService(this.WINDOW_SERVICE);
        width = wManager.getDefaultDisplay().

                getWidth() * 4 / 5;
        //实例化一个popupwindow对象
        pw = new
                PopupWindow(option, width, LayoutParams.WRAP_CONTENT, true);
        ColorDrawable dw = new ColorDrawable(00000);
        pw.setBackgroundDrawable(dw);
        pw.setOutsideTouchable(true);

        listView.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view,
                                    int position, long id) {

                // 获取选中项内容及从sharePreferences中获取对应的密码
                String username = userList.get(position);
                String pwd = passList.get(position);

                et_user.setText(username);
                et_pwd.setText(pwd);

                // 选择后，popupwindow自动消失
                pw.dismiss();
            }
        });
    }


    private void login() {
        db = databaseHelper.getReadableDatabase();
        if (checkBox.isChecked()) {

            String name = et_user.getText().toString().trim();
            String pwd = et_pwd.getText().toString().trim();
            if (!"".equals(name) && !"".equals(pwd)) {
                db.execSQL("insert into users (name,password) values(?,?)", new String[]{name, pwd});
                Toast.makeText(this, "用户名与密码已经记住，下次进入软件时将生效！", Toast.LENGTH_SHORT).show();

            }
        }
        db.close();

    }

    private void query(SQLiteDatabase db, List<String> userList2, List<String> passList2) {

        //查询获得游标
        Cursor cursor = db.query(TABLE_NAME, null, null, null, null, null, null);

        //判断游标是否为空
        while (cursor.moveToNext()) {
            String username = cursor.getString(cursor.getColumnIndex("name"));
            userList2.add(username);
            //获得密码
            String password = cursor.getString(cursor.getColumnIndex("password"));
            passList2.add(password);
        }
        cursor.close();
        //关闭数据库
        db.close();
    }
}
