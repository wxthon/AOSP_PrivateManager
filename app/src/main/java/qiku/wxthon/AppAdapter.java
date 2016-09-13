package qiku.wxthon;

import android.app.PrivateManager;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;

import java.util.List;

/**
 * Created by huayahng on 9/8/16.
 */
public class AppAdapter extends RecyclerView.Adapter <AppHolder> {

    private List<App> mDatas;
    private Context mContext;
    private PrivateManager mASM;

    public AppAdapter(Context context, PrivateManager asm, List<App> datas) {
        mDatas = datas;
        mContext = context;
        mASM = asm;
    }

    @Override
    public AppHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = View.inflate(mContext, R.layout.item_app, null);
        return new AppHolder(view);
    }

    @Override
    public void onBindViewHolder(final AppHolder holder, final int position) {
        final App app = mDatas.get(position);
        if (app == null) return;
        holder.mIcon.setImageDrawable(app.mAppIcon);
        holder.mName.setText(app.mAppName);
        holder.mSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                //Toast.makeText(mContext, "name " + app.mAppName, Toast.LENGTH_SHORT).show();
                app.mChecked = isChecked;
                holder.mSwitch.setChecked(app.mChecked);

                if (isChecked) {
                    AppUtil.start(app.mPackageName);
                    try {
                        mASM.setAutoStartPackage(app.mPackageName, true);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    AppUtil.stop(app.mPackageName);
                    try {
                        mASM.setAutoStartPackage(app.mPackageName, false);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        holder.mSwitch.setChecked(app.mChecked);
    }

    @Override
    public int getItemCount() {
        return mDatas == null ? 0 : mDatas.size();
    }
}
