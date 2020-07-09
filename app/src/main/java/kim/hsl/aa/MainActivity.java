package kim.hsl.aa;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    private MyAsyncTask mMyAsyncTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 创建并执行异步任务
        mMyAsyncTask = new MyAsyncTask();
        mMyAsyncTask.execute();
    }
}
