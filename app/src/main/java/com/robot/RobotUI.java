package com.robot;

import android.app.Activity;
import android.app.PrivateManager;
import android.app.Service;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.ArrayMap;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.List;
import java.util.Map;

public class RobotUI extends Activity {

    private static final String TAG = "RobotUI";

    private RecyclerView mComponentPanel;

    private List<App> mAppDatas;
    private AppAdapter mAdapter;
    private PackageManager mManager;
    private ProgressBar mBar;
    private Button mSmartCleanBtn;
    private PrivateManager mPrivMgr;
    private List<String> mBlackPackages;

    private IRobot mRobot = null;
    private List<RobotComponent> mComponents = null;
    private Map<String, View> mComponentUIs = new ArrayMap<>();
    private TextView mRobotMessageView = null;

    private ServiceConnection mServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            mRobot = IRobot.Stub.asInterface(service);
            onRobotConnected();
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            mRobot = null;
            onRobotDisconnected();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.robot_ui);

        // bind robot service firstly
        if (mRobot == null) {
            bindService(new Intent(this, RobotService.class), mServiceConnection, Service.BIND_AUTO_CREATE);
        }

        mComponentPanel = (RecyclerView) findViewById(R.id.component_panel);
        LinearLayoutManager manager = new LinearLayoutManager(this);
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        mComponentPanel.setLayoutManager(manager);
        mComponentPanel.setAdapter(new ComponentAdapter());

        mRobotMessageView = (TextView) findViewById(R.id.robot_message);

//        mBar = (ProgressBar) findViewById(R.id.progressBar);
//        mSmartCleanBtn = (Button) findViewById(R.id.smart_clean_btn);
//        mSmartCleanBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (mPrivMgr != null) {
//                    mPrivMgr.smartClean();
//                    Toast.makeText(RobotUI.this, "Smart Clean Finished", Toast.LENGTH_SHORT).show();
//                }
//            }
//        });
//        mAppDatas = new ArrayList<>();
//        mPrivMgr = (PrivateManager) getSystemService("privsrv");
//        mAdapter = new AppAdapter(this, mPrivMgr, mAppDatas);
//        mAppList.setAdapter(mAdapter);
//        mManager = getPackageManager();
//        try {
//            mBlackPackages = mPrivMgr.getBlackList();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        loadApps(this);
    }

    private void onRobotDisconnected() {
        showRobotStat("robot is missing");
    }

    private void onRobotConnected() {
        try {
            if (!mRobot.isReady()) {
                showRobotStat("not ready");
                mRobotMessageView.setText("Robot is not ready ^-^, must fix it.");
                return;
            }
            mRobotMessageView.setText("Robot is well...");
            mComponents = mRobot.getComponents();
            initComponentUI();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    private void initComponentUI() {
        for (RobotComponent rc : mComponents) {
            if (rc.getLayoutID() == 0) {
                // has no ui
                continue;
            } else if (mComponentUIs.containsKey(rc.getName())) {
                // ignore new ui or just use cache ui
                continue;
            }
            mComponentUIs.put(rc.getName(), getLayoutInflater().inflate(rc.getLayoutID(), null));
        }
        showComponentUI();
    }

    private void showComponentUI() {
         mComponentPanel.getAdapter().notifyDataSetChanged();
    }

    private void showRobotStat(String message) {
        mRobotMessageView.setText(message);
    }

    private void onComponentClicked(RobotComponent rc) {
        try {
            int id = mRobot.getComponentUI(rc);
            Intent intent = new Intent(this, RobotComponentUI.class);
            intent.putExtra("component_ui", id);
            startActivity(intent);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

//    private void loadApps(Context context) {
//
//        new AsyncTask<Void, Void, Void>() {
//
//            @Override
//            protected Void doInBackground(Void... params) {
//                List<PackageInfo> infos = mManager.getInstalledPackages(0);
//                if (infos == null || infos.size() == 0) {
//                    return null;
//                }
//                for (PackageInfo info : infos) {
//                    if ((info.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) != 0) {
//                        continue;
//                    }
//                    App app = new App();
//                    app.mAppIcon = info.applicationInfo.loadIcon(mManager);
//                    CharSequence title = info.applicationInfo.loadLabel(mManager);
//                    if (title != null) {
//                        app.mAppName = (String) title;
//                    }
//                    app.mPackageName = info.packageName;
//                    //app.mChecked = AppUtil.canStart(info.packageName);
//                    app.mChecked = allowed(info.packageName);
//                    Log.e("Checked", "pkg:" + info.packageName + "; allowed :" + app.mChecked);
//                    mAppDatas.add(app);
//                }
//
//                Collections.sort(mAppDatas, new Comparator<App>() {
//                    @Override
//                    public int compare(App lhs, App rhs) {
//                        return lhs.mChecked && rhs.mChecked || !(lhs.mChecked || rhs.mChecked) ? 0 :
//                                lhs.mChecked ?  -1 : 1;
//                    }
//                });
//                return null;
//            }
//
//            @Override
//            protected void onPreExecute() {
//                super.onPreExecute();
//                mBar.setVisibility(View.VISIBLE);
//            }
//
//            @Override
//            protected void onPostExecute(Void aVoid) {
//                super.onPostExecute(aVoid);
//                mBar.setVisibility(View.GONE);
//                mAdapter.notifyDataSetChanged();
//            }
//        }.execute();
//    }
//
//    private boolean allowed(String packageName) {
////        if (mBlackPackages == null || mBlackPackages.size() == 0) {
////            return false;
////        }
////        return mBlackPackages.contains(packageName);
//        try {
//            return mPrivMgr.allowAutoStart(packageName);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return false;
//    }

    private class ComponentAdapter extends RecyclerView.Adapter <ComponentViewHolder>  {

        @Override
        public ComponentViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LinearLayout layout = (LinearLayout) View.inflate(RobotUI.this, R.layout.component_item, null);
            layout.setLayoutParams(new LinearLayout.LayoutParams(RobotTool.getHeight(), RobotTool.getHeight()/3));
            return new ComponentViewHolder(layout);
        }

        @Override
        public void onBindViewHolder(ComponentViewHolder holder, int position) {
            holder.mComponentLeft.removeAllViews();
            holder.mComponentRight.removeAllViews();
            Object[] keys = mComponentUIs.keySet().toArray();
            String name = (String) keys[position * 2];
            RobotComponentLayout rcLayout = (RobotComponentLayout) mComponentUIs.get(name);
            rcLayout.setLayoutParams(new LinearLayout.LayoutParams(RobotTool.getHeight() / 3,
                    RobotTool.getHeight() / 3));
            holder.mComponentLeft.addView(rcLayout);
            holder.mComponentLeft.setTag(name);
            if (position * 2 + 1 < mComponentUIs.size()) {
                name = (String) keys[position * 2 + 1];
                rcLayout = (RobotComponentLayout) mComponentUIs.get(name);
                rcLayout.setLayoutParams(new LinearLayout.LayoutParams(RobotTool.getHeight() / 3,
                        RobotTool.getHeight() / 3));
                holder.mComponentRight.addView(rcLayout);
                holder.mComponentRight.setTag(name);
            }
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public int getItemCount() {
            return (int) Math.round(mComponentUIs.size()/2.0);
        }
    }

    private class ComponentViewHolder extends RecyclerView.ViewHolder {
        private LinearLayout mComponentLeft;
        private LinearLayout mComponentRight;

        public ComponentViewHolder(View itemView) {
            super(itemView);
            mComponentLeft = (LinearLayout) itemView.findViewById(R.id.component_left);
            mComponentRight = (LinearLayout) itemView.findViewById(R.id.component_right);

            View.OnClickListener listener = new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String name = (String) v.getTag();
                    for (RobotComponent rc : mComponents) {
                        if (name.equals(rc.getName())) {
                            onComponentClicked(rc);
                            break;
                        }
                    }
                }
            };
            mComponentLeft.setOnClickListener(listener);
            mComponentRight.setOnClickListener(listener);
        }
    }
}
