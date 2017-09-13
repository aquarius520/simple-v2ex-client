package com.aquarius.simplev2ex;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.View;
import android.widget.TextView;

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
    CheckBoxPreference mNoImagePref;
//    Preference mFileCachePref;
    Preference mAboutPref;

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

        mNoImagePref = (CheckBoxPreference) findPreference("pref_show_img_only_wifi");
        mNoImagePref.setChecked(!AppConfig.isDownloadImageInMobileNetwork());
        mNoImagePref.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                AppConfig.writeToPreference(getActivity(), Constants.KEY_NOT_LOAD_IMG_MOBILE_NETWORK,
                        mNoImagePref.isChecked());
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
                                        GlideUtil.clearDiskCache(V2exApplication.getInstance());
                                        return null;
                                    }

                                    @Override
                                    protected void onPostExecute(Void aVoid) {
                                        mImageCachePref.setSummary("0K");
                                    }
                                }.execute();
                                }
                            })
                        .setCancelable(true)
                        .create();
                dialog.show();
                return true;
            }
        });


//        mFileCachePref = findPreference("pref_clear_file_cache");
//        mFileCachePref.setSummary(FileUtil.getFileSize(FileUtil.getFileCache(getActivity())));
//        mFileCachePref.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
//            @Override
//            public boolean onPreferenceClick(Preference preference) {
//                return true;
//            }
//        });

        mAboutPref = findPreference("about_pref");
        mAboutPref.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                showAbout();
                return true;
            }
        });
    }

    private void showAbout() {
        View contentView = View.inflate(getActivity(), R.layout.setting_about, null);
        TextView about = (TextView) contentView.findViewById(R.id.about_text);
        AlertDialog dialog = new AlertDialog.Builder(getActivity())
                .setView(contentView)
//                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        dialog.dismiss();
//                    }
//                })
                .setCancelable(true)
                .create();

        StringBuilder sb = new StringBuilder();
        String author = "aquarius";
        String authorUrl = sb.append("<a href='")
                            .append("https://github.com/aquarius520")
                            .append("'>")
                            .append("https://github.com/aquarius520")
                            .append("</a>")
                            .append("<br/>").toString();
        String message = getActivity().getString(R.string.app_name) + "<br/>"  + authorUrl +
                "<br/>" + "by@" + author;
        CharSequence charSequence = Html.fromHtml(message);
        about.setText(charSequence);
        about.setMovementMethod(LinkMovementMethod.getInstance());

        dialog.show();
    }

}