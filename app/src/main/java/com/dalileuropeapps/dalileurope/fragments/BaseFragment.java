package com.dalileuropeapps.dalileurope.fragments;

import android.app.Activity;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import com.dalileuropeapps.dalileurope.R;
import com.dalileuropeapps.dalileurope.utils.Constants;
import com.dalileuropeapps.dalileurope.utils.SharedPreference;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import java.util.Locale;

public class BaseFragment extends Fragment {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStatusBarColor(getActivity().getResources().getColor(R.color.white));

        String lang = SharedPreference.getAppLanguage(getContext().getApplicationContext());
//        Toast.makeText(getApplicationContext(), lang, Toast.LENGTH_SHORT).show();
        if (lang.equalsIgnoreCase(Constants.english)) {
            changeLanguageDefault(Constants.english);
        } else if (lang.equalsIgnoreCase(Constants.swedish)) {
            changeLanguageDefault(Constants.swedish);
        } else if (lang.equalsIgnoreCase(Constants.germany)) {
            changeLanguageDefault(Constants.germany);
        } else if (lang.equalsIgnoreCase(Constants.arabic)) {
            changeLanguageDefault(Constants.arabic);
        }
    }

    private void changeLanguageDefault(String language) {
        Locale locale = new Locale(language);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        getResources().updateConfiguration(config, null);
    }

    public void setStatusBarColor(int statusBarColor) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {

            Window window = getActivity().getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(statusBarColor);
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);

        }
    }

    public void statusBarTextColorWhite() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getActivity().getWindow();
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);
        }

    }

    public void removeStatusBar() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window w = getActivity().getWindow();
            w.addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        }
    }

    public void transparentStatusBar() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            //        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            getActivity().getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR | View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
            getActivity().getWindow().setStatusBarColor(Color.TRANSPARENT);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public static final void setStatusBarTextColorWithTransparentBG(final Activity activity, final boolean pIsDark) {
        // Fetch the current flags.
        final int lFlags = activity.getWindow().getDecorView().getSystemUiVisibility();
        // Update the SystemUiVisibility dependening on whether we want a Light or Dark theme.
        activity.getWindow().getDecorView().setSystemUiVisibility(pIsDark ? (lFlags & ~View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR) : (lFlags | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR));
    }


/*    private ViewTreeObserver.OnGlobalLayoutListener keyboardLayoutListener = new ViewTreeObserver.OnGlobalLayoutListener() {
        @Override
        public void onGlobalLayout() {
            if(rootLayout!=null) {
                int heightDiff = rootLayout.getRootView().getHeight() - rootLayout.getHeight();
                int contentViewTop = getActivity().getWindow().findViewById(Window.ID_ANDROID_CONTENT).getTop();

                if (heightDiff <= contentViewTop) {
                    onHideKeyboard();
                } else {
                    int keyboardHeight = heightDiff - contentViewTop;
                    onShowKeyboard(keyboardHeight);
                }
            }
        }
    };*/

  /*  private boolean keyboardListenersAttached = false;
    private ViewGroup rootLayout;*/

    protected void onShowKeyboard(int keyboardHeight) {
    }

    protected void onHideKeyboard() {
    }

    protected void attachKeyboardListeners(ViewGroup rootLayout) {
/*        if (keyboardListenersAttached) {
            return;
        }
        this.rootLayout = rootLayout;
        rootLayout.getViewTreeObserver().addOnGlobalLayoutListener(keyboardLayoutListener);
        keyboardListenersAttached = true;*/
    }

}
