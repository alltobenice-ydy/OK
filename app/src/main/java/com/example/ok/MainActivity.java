package com.example.ok;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;


import androidx.appcompat.app.AppCompatActivity;

/**
 * 因为此类 implements OnClickListener
 * 所以可以将 onClick() 方法写到 onCreate() 方法之外
 * 避免 onCreate()方法过于冗长
 * 同时，将声明与初始化View的操作都封装成方法，写到onCreate() 方法之外，
 * 进一步避免 onCreate()方法过于冗长
 */
public class MainActivity extends AppCompatActivity
        implements OnClickListener {

    /**
     * 在全局声明的对象
     * 这里注意区分*声明* 和 *初始化*
     * 仅仅声明是没有值的，即为 null
     * 只有初始化之后，此对象才会有值
     * 常见的NullPointException很多情况都是只进行了声明，没有初始化
     */
    private EditText etInsert;
    private EditText etDelete;
    private EditText etBeforeUpdate;
    private EditText etAfterUpdate;
    private EditText etquerryone;
    private TextView tvQueryResult;
    private TextView tvQueryResult_one;
    private SQLiteDatabase db;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 这一句话指定了可以去哪个布局文件中找id
        setContentView(R.layout.activity_main);

        //依靠DatabaseHelper的构造函数创建数据库
        DatabaseHelper database = new DatabaseHelper(MainActivity.this, "db1", null, 1);
        db = database.getWritableDatabase();
//        createTable(db);
        initView();
    }
    private void createTable(SQLiteDatabase db){
        //创建表SQL语句
        String sql = "create table test7(name varchar(20) not null , password varchar(60) not null );";
        db.execSQL(sql);
    }

    private void initView() {
        // 根据setContentView(R.layout.activity_main)方法指定的布局中的id初始化对象
        // 8个按钮
        Button btInsert = findViewById(R.id.bt_insert);
        Button btClearInsert = findViewById(R.id.bt_clear_insert);
        Button btUpdate = findViewById(R.id.bt_update);
        Button btClearUpdate = findViewById(R.id.bt_clear_update);
        Button btDelete = findViewById(R.id.bt_delete);
        Button btClearDelete = findViewById(R.id.bt_clear_delete);
        Button btQuery = findViewById(R.id.bt_query);
        Button btClearQueryResult = findViewById(R.id.bt_clear_query);

        Button btQueryOne = findViewById(R.id.bt_querry_one);

        // 5个输入框（4个输入框需要在下面的onClick()方法中用去获取输入的文本，所以在全局进行声明）


        etInsert = findViewById(R.id.et_inset);
        etDelete = findViewById(R.id.et_delete);
        etBeforeUpdate = findViewById(R.id.et_before_update);
        etAfterUpdate = findViewById(R.id.et_after_update);

        // 1个查询结果展示文本
        tvQueryResult = findViewById(R.id.tv_query_result);

        // 为8个按钮对象设置监听器
        btInsert.setOnClickListener(this);
        btClearInsert.setOnClickListener(this);

        btUpdate.setOnClickListener(this);
        btClearUpdate.setOnClickListener(this);

        btDelete.setOnClickListener(this);
        btClearDelete.setOnClickListener(this);

        btQuery.setOnClickListener(this);
        btClearQueryResult.setOnClickListener(this);

        btQueryOne.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        // 获取输入框的数据
        String insertData = etInsert.getText().toString();
        String deleteData = etDelete.getText().toString();
        String beforeUpdateData = etBeforeUpdate.getText().toString();
        String afterUpdateData = etAfterUpdate.getText().toString();

        //根据响应Click的按钮id进行选择操作
        switch (v.getId()) {
            //插入数据按钮
            case R.id.bt_insert:
                //创建存放数据的ContentValues对象
                ContentValues values = new ContentValues();

                values.put("name", insertData);
                values.put("password", beforeUpdateData);

                //数据库执行插入命令
                db.insert("test7", null, values);
                ;


                break;
            //插入数据按钮后面的清除按钮
            case R.id.bt_clear_insert:
                etInsert.setText("");
                break;

            //删除数据按钮
            case R.id.bt_delete:
                db.delete("user1", "name=?", new String[]{deleteData});
                break;
            //删除数据按钮后面的清除按钮
            case R.id.bt_clear_delete:
                etDelete.setText("");
                break;

            //更新数据按钮
            case R.id.bt_update:
                ContentValues values2 = new ContentValues();
                values2.put("name", afterUpdateData);
                db.update("user1", values2, "name = ?", new String[]{beforeUpdateData});
                break;
            //更新数据按钮后面的清除按钮
            case R.id.bt_clear_update:
                etBeforeUpdate.setText("");
                etAfterUpdate.setText("");
                break;

            case R.id.bt_querry_one:
                String NAME= "name=?";
                String PASSWORD= "password=?";

                Cursor cursor = db.query("test7", new String[]{"name","password"}, PASSWORD, new String[]{"158"}, null, null, null);
                while(cursor.moveToNext()){
                    String name = cursor.getString(cursor.getColumnIndex("name"));
                    String password = cursor.getString(cursor.getColumnIndex("password"));;
                    System.out.println("query------->" + name+" "+password);
                }

                    break;

                    //查询全部按钮
//            case R.id.bt_query:
//                //创建游标对象
//                Cursor cursor = db.query("user1", new String[]{"name"}, null, null, null, null, null);
//                //利用游标遍历所有数据对象（for循环中，建议使用StringBuilder替代String）
//                //为了显示全部，把所有对象连接起来，放到TextView中
//                StringBuilder tvData = new StringBuilder();
//                while (cursor.moveToNext()) {
//                    String name = cursor.getString(cursor.getColumnIndex("name"));
//
//                    tvData.append("\n").append(name);
//                }


//
//                tvQueryResult.setText(tvData.toString());
//                cursor.close(); // 关闭游标，释放资源
//                break;
               case R.id.bt_clear_query:
                        tvQueryResult.setText("");
                        tvQueryResult.setHint("查询内容为空");
                        break;

                    default:
                        break;

        }
    }

}