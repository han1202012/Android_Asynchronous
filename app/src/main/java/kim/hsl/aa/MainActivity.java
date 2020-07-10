package kim.hsl.aa;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.FutureTask;

public class MainActivity extends AppCompatActivity {
    public static final String TAG = "MainActivity";

    private MyAsyncTask mMyAsyncTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 创建并执行异步任务
        mMyAsyncTask = new MyAsyncTask();
        mMyAsyncTask.execute();



    }

    private void future(){

        // 需要开发者传入 Callable 或者 Runnable 实现类对象
        FutureTask<String> futureTask = new FutureTask<String>(new MyCallable()){
            /**
             * 该方法在 MyCallable 的 call() 方法执行完毕后
             * 自动回调
              */
            @Override
            protected void done() {
                try {
                    /*
                        获取 MyCallable 的 call 方法耗时操作的结果
                        注意 FutureTask 对象的 get() 最好在 done 中调用 , 可以立刻得到异步操作的执行结果
                        如果调用 get() 方法时 , Callable 的 call() 方法还没有执行完毕 ,
                        此时调用线程就会一直阻塞 , 直到 call() 方法是调用完毕 ,
                        返回执行结果 , 此时才会解除阻塞 , 返回执行结果 ;
                     */
                    String callableResult = get();
                    Log.i(TAG, "执行结果 : " + callableResult);
                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };

        // 创建线程池 , 通过该线程池执行
        ExecutorService executorService = Executors.newCachedThreadPool();
        // 执行 futureTask 耗时操作
        executorService.execute(futureTask);

    }

    /**
     * 自定义 Callable 类型
     * 实际的异步操作在该方法中执行
     */
    class MyCallable implements Callable<String>{
        @Override
        public String call() throws Exception {
            Log.i(TAG, "MyCallable call() 耗时操作");
            return "Success";
        }
    }

}
