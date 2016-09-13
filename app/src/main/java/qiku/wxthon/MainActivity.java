package qiku.wxthon;

import android.app.Activity;
import android.app.PrivateManager;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;
import android.widget.Toolbar;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class MainActivity extends Activity {

    private RecyclerView mAppList;
    private List<App> mAppDatas;
    private AppAdapter mAdapter;
    private PackageManager mManager;
    private ProgressBar mBar;
    private Button mSmartCleanBtn;
    private PrivateManager mPrivMgr;
    private List<String> mBlackPackages;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.fragment_perm);


        mAppList = (RecyclerView) findViewById(R.id.id_app_list);
        mBar = (ProgressBar) findViewById(R.id.progressBar);
        mSmartCleanBtn = (Button) findViewById(R.id.smart_clean_btn);
        mSmartCleanBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mPrivMgr != null) {
                    mPrivMgr.smartClean();
                    Toast.makeText(MainActivity.this, "Smart Clean Finished", Toast.LENGTH_SHORT).show();
                }
            }
        });
        LinearLayoutManager manager = new LinearLayoutManager(this);
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        mAppList.setLayoutManager(manager);
        mAppDatas = new ArrayList<>();
        mPrivMgr = (PrivateManager) getSystemService("privsrv");
        mAdapter = new AppAdapter(this, mPrivMgr, mAppDatas);
        mAppList.setAdapter(mAdapter);
        mManager = getPackageManager();
        try {
            mBlackPackages = mPrivMgr.getBlackList();
        } catch (Exception e) {
            e.printStackTrace();
        }
        loadApps(this);
    }

    private void loadApps(Context context) {

        new AsyncTask<Void, Void, Void>() {

            @Override
            protected Void doInBackground(Void... params) {
                List<PackageInfo> infos = mManager.getInstalledPackages(0);
                if (infos == null || infos.size() == 0) {
                    return null;
                }
                for (PackageInfo info : infos) {
                    if ((info.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) != 0) {
                        continue;
                    }
                    App app = new App();
                    app.mAppIcon = info.applicationInfo.loadIcon(mManager);
                    CharSequence title = info.applicationInfo.loadLabel(mManager);
                    if (title != null) {
                        app.mAppName = (String) title;
                    }
                    app.mPackageName = info.packageName;
                    //app.mChecked = AppUtil.canStart(info.packageName);
                    app.mChecked = allowed(info.packageName);
                    Log.e("Checked", "pkg:" + info.packageName + "; allowed :" + app.mChecked);
                    mAppDatas.add(app);
                }

                Collections.sort(mAppDatas, new Comparator<App>() {
                    @Override
                    public int compare(App lhs, App rhs) {
                        return lhs.mChecked && rhs.mChecked || !(lhs.mChecked || rhs.mChecked) ? 0 :
                                lhs.mChecked ?  -1 : 1;
                    }
                });
                return null;
            }

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                mBar.setVisibility(View.VISIBLE);
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                mBar.setVisibility(View.GONE);
                mAdapter.notifyDataSetChanged();
            }
        }.execute();
    }

    private boolean allowed(String packageName) {
//        if (mBlackPackages == null || mBlackPackages.size() == 0) {
//            return false;
//        }
//        return mBlackPackages.contains(packageName);
        try {
            return mPrivMgr.allowAutoStart(packageName);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
}
