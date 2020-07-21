package kim.hsl.aa;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;

import java.util.Timer;
import java.util.TimerTask;
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


        // 手写 AsyncTask
        future();

        // 定时器使用
        timer();
    }

    private void timer(){
        // Timer 可用于执行延迟任务或循环任务
        Timer timer = new Timer();

        /*
            如果提交多个 TimerTask 定时器任务
            需要等待之前的 定时器任务 执行完成 , 才能执行后面的任务

            TimerTask 实现了 Runnable 接口

            延迟 1 秒执行任务 1 ( 任务 1 时长 5 秒 )
            延迟 2 秒执行任务 2 ( 任务 2 时长 5 秒 )

            Timer 执行任务是串行执行的 , 同一时间只能执行一个任务
            任务 1 在 1 秒之后执行 , 在第 6 秒执行完毕
            任务 2 在第 6 秒 , 任务 1 执行完毕后 , 才开始执行 , 在第 11 秒执行完毕
         */

        // 延迟 1 秒执行任务 1
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                Log.i(TAG, "延迟 1 秒执行 5 秒的任务 1 开始执行");
                try {
                    Thread.sleep(5_000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                Log.i(TAG, "延迟 1 秒执行 5 秒的任务 1 执行完毕");
            }
        }, 1_000);

        // 延迟 2 秒执行任务 2
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                Log.i(TAG, "延迟 2 秒执行 5 秒的任务 2 开始执行");
                try {
                    Thread.sleep(5_000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                Log.i(TAG, "延迟 2 秒执行 5 秒的任务 2执行完毕");
            }
        }, 2_000);
    }

    private void future(){

        /*
            FutureTask 间接实现了 Runnable 和 Future 接口 ,
            可以得到子线程耗时操作的执行结果 , AsyncTask 异步任务就是使用了该机制 ;

            需要开发者传入 Callable 或者 Runnable 实现类对象 , 在该对象中定义要在子线程中执行的操作
         */
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
