package kim.hsl.aa;

import android.os.AsyncTask;

/**
 * AsyncTask<Void, Void, Void> 泛型解析
 * - 1. 异步任务开始时 , execute 方法传入的参数类型
 * - 2. 异步任务执行时 , 进度值类型
 * - 3. 异步任务结束时 , 结果类型
 */
public class MyAsyncTask extends AsyncTask<Void, Void, Void> {

    @Override
    protected void onPreExecute() {
        // doInBackground 之前执行的方法, 一般在该方法中执行初始化操作 ( 主线程, 可以更新 UI )
        super.onPreExecute();
    }

    @Override
    protected Void doInBackground(Void... voids) {
        // 主要的耗时操作是在该方法中执行的 ( 非主线程, 不能更新 UI )
        return null;
    }

    @Override
    protected void onProgressUpdate(Void... values) {
        // 在 doInBackground 中调用了 publishProgress 方法, 就会回调该方法
        // 一般情况下是在该方法中执行更新 UI 的操作 ( 主线程, 可以更新 UI )
        super.onProgressUpdate(values);
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        // doInBackground 执行完毕后 , 调用 return 方法后 , 该方法会被调用 ( 主线程, 可以更新 UI )
        super.onPostExecute(aVoid);
    }

    @Override
    protected void onCancelled() {
        super.onCancelled();
    }

    @Override
    protected void onCancelled(Void aVoid) {
        super.onCancelled(aVoid);
    }
}
