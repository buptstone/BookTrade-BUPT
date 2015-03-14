package com.bupt.booktrade.fragment;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;

import com.bupt.booktrade.R;
import com.bupt.booktrade.utils.TimeFormat;
import com.dss886.nForumSDK.NForumSDK;
import com.dss886.nForumSDK.http.NForumException;
import com.dss886.nForumSDK.model.User;
import com.dss886.nForumSDK.service.UserService;
import com.dss886.nForumSDK.util.Host;

import org.json.JSONException;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import fr.castorflex.android.circularprogressbar.CircularProgressDrawable;

public class PersonalHomeFragment extends Fragment {

    private final static String TAG = "  PersonalHomeFragment: ";
    private final boolean D = true;
    private User user = new User();
    private Bitmap bitmap;
    private View rootView;
    private ProgressBar mProgressBar;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //getUserInfo();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
            getUserInfo();
            rootView = inflater.inflate(R.layout.fragment_personal_home, container, false);
            mProgressBar = (ProgressBar) rootView.findViewById(R.id.progressbar_circular_person);
            mProgressBar.setIndeterminateDrawable(new CircularProgressDrawable
                    .Builder(getActivity())
                    .colors(getResources().getIntArray(R.array.gplus_colors))
                    .sweepSpeed(1f)
                    .strokeWidth(4)
                    .style(CircularProgressDrawable.Style.ROUNDED)
                    .build());
        return rootView;
    }

    private void getUserInfo() {
        GetUserInfoTask testAsyncTask = new GetUserInfoTask(new FragmentCallback() {

            @Override
            public void onTaskDone() {
                //ToastUtils.showToast(getActivity(), userName.getText().toString(), Toast.LENGTH_SHORT);
                mProgressBar.setVisibility(View.GONE);
                ScrollView userInfo = (ScrollView) rootView.findViewById(R.id.user_info);
                userInfo.setVisibility(View.VISIBLE);
                setUserInfo();

            }
        });
        try {
            testAsyncTask.execute().get(0, TimeUnit.MILLISECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (TimeoutException e) {
            e.printStackTrace();
        }
    }

    private void setUserInfo() {
        TextView userName = (TextView) rootView.findViewById(R.id.person_home_username);
        TextView nickName = (TextView) rootView.findViewById(R.id.nick_name);
        TextView gender = (TextView) rootView.findViewById(R.id.gender);
        TextView constellation = (TextView) rootView.findViewById(R.id.constellation);
        TextView qq = (TextView) rootView.findViewById(R.id.qq);
        TextView msn = (TextView) rootView.findViewById(R.id.msn);
        TextView homePage = (TextView) rootView.findViewById(R.id.home_page);
        TextView level = (TextView) rootView.findViewById(R.id.level);
        TextView score = (TextView) rootView.findViewById(R.id.score);
        TextView isOnline = (TextView) rootView.findViewById(R.id.is_online);
        TextView firstLoginTime = (TextView) rootView.findViewById(R.id.first_login_time);
        TextView lastLoginTime = (TextView) rootView.findViewById(R.id.last_login_time);
        TextView lastLoginIp = (TextView) rootView.findViewById(R.id.last_login_ip);
        ImageView avatar = (ImageView) rootView.findViewById(R.id.person_home_avatar);

        userName.setText(user.id);

        if (user.user_name.isEmpty()) {
            nickName.setText("未填写");
        } else {
            nickName.setText(user.user_name);
        }

        if (user.gender.equals("m")) {
            gender.setText("男生");
        } else if (user.gender.equals("f")) {
            gender.setText("女生");
        } else {
            gender.setText("隐藏");
        }

        if (user.astro.isEmpty()) {
            constellation.setText("隐藏");
        } else {
            constellation.setText(user.astro);
        }

        if (user.user_name.isEmpty()) {
            qq.setText("未填写");
        } else {
            qq.setText(user.qq);
        }

        if (user.user_name.isEmpty()) {
            msn.setText("未填写");
        } else {
            msn.setText(user.msn);
        }

        if (user.user_name.isEmpty()) {
            homePage.setText("未填写");
        } else {
            homePage.setText(user.home_page);
        }

        level.setText(user.level);
        score.setText(user.score);
        if (user.is_online) {
            isOnline.setText("在线");
        } else {
            isOnline.setText("离线");
        }
        firstLoginTime.setText(TimeFormat.transTime(user.first_login_time * 1000l));
        lastLoginTime.setText(TimeFormat.transTime(user.last_login_time * 1000l));
        lastLoginIp.setText(user.last_login_ip);

        avatar.setImageBitmap(bitmap);
    }

    private interface FragmentCallback {
        public void onTaskDone();
    }

    /**
     * 在后台加载用户信息，加载完成后更新UI
     */
    class GetUserInfoTask extends AsyncTask<Void, Void, Void> {
        private FragmentCallback mFragmentCallback;

        public GetUserInfoTask(FragmentCallback fragmentCallback) {
            mFragmentCallback = fragmentCallback;
        }

        @Override
        protected Void doInBackground(Void... params) {
        /* Do your thing. */
            NForumSDK nForumSDK = new NForumSDK(Host.HOST_BYR, Host.APP_KEY, "Demievil", "liuyan1206");
            UserService userService = nForumSDK.getUserService();
            try {
                user = userService.queryById("Demievil");
            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (NForumException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            try {
                bitmap = BitmapFactory.decodeStream((InputStream) new URL(user.face_url).getContent());
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            mFragmentCallback.onTaskDone();
        }
    }
}
