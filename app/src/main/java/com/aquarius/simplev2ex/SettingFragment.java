package com.aquarius.simplev2ex;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;

import com.aquarius.simplev2ex.config.AppConfig;
import com.aquarius.simplev2ex.util.Constants;
import com.aquarius.simplev2ex.util.FileUtil;
import com.aquarius.simplev2ex.util.GlideUtil;

/**
 * Created by aquarius on 2017/8/6.
 */
public class SettingFragment extends PreferenceFragment {

    SharedPreferences mPreferences;
    CheckBoxPreference mHttpsPref;
    Preference mImageCachePref;
    Preference mFileCachePref;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.settings);

        mPreferences = AppConfig.getSharedPreference(getActivity());

        mHttpsPref = (CheckBoxPreference)findPreference("pref_use_https");
        mHttpsPref.setChecked(AppConfig.isHttps());
        mHttpsPref.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                AppConfig.writeToPreference(getActivity(), Constants.KEY_USE_HTTPS, mHttpsPref.isChecked());
                return true;
            }
        });

        mImageCachePref = findPreference("pref_clear_img_cache");
        mImageCachePref.setSummary(FileUtil.getFileSize(FileUtil.getCacheSize(getActivity())));
        mImageCachePref.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                AlertDialog dialog = new AlertDialog.Builder(getActivity()).setTitle("提示")
                        .setMessage("确认清除图片缓存吗？")
                        .setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        })
                        .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                new AsyncTask<Void, Void, Void>() {
                                    @Override
                                    protected Void doInBackground(Void... params) {
                                        GlideUtil.clearDiskCache(getActivity());
                                        return null;
                                    }

                                    @Override
                                    protected void onPostExecute(Void aVoid) {
                                        mImageCachePref.setSummary("0K");
                                    }
                                }.execute();
                                }
                            })
                        .create();
                dialog.show();
                return true;
            }
        });


    }

}