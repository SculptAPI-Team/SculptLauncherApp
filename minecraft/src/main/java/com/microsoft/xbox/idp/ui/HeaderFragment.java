package com.microsoft.xbox.idp.ui;

import android.app.Activity;
import android.app.LoaderManager;
import android.content.Loader;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.google.gson.GsonBuilder;
import com.microsoft.xbox.idp.compat.BaseFragment;
import com.microsoft.xbox.idp.model.UserAccount;
import com.microsoft.xbox.idp.services.EndpointsFactory;
import com.microsoft.xbox.idp.toolkit.BitmapLoader;
import com.microsoft.xbox.idp.toolkit.ObjectLoader;
import com.microsoft.xbox.idp.util.CacheUtil;
import com.microsoft.xbox.idp.util.FragmentLoaderKey;
import com.microsoft.xbox.idp.util.HttpCall;
import com.microsoft.xbox.idp.util.HttpUtil;
import org.sculptlauncher.minecraft.R;

public class HeaderFragment extends BaseFragment implements View.OnClickListener {
    static final /* synthetic */ boolean $assertionsDisabled = false;
    private static final int LOADER_GET_PROFILE = 1;
    private static final int LOADER_USER_IMAGE_URL = 2;
    private static final Callbacks NO_OP_CALLBACKS = new Callbacks() { // from class: com.microsoft.xbox.idp.ui.HeaderFragment.3
        @Override // com.microsoft.xbox.idp.ui.HeaderFragment.Callbacks
        public void onClickCloseHeader() {
        }
    };
    private static final String TAG = "HeaderFragment";
    private UserAccount userAccount;
    private TextView userEmail;
    private ImageView userImageView;
    private TextView userName;
    private Callbacks callbacks = NO_OP_CALLBACKS;
    LoaderManager.LoaderCallbacks<ObjectLoader.Result<UserAccount>> userAccountCallbacks = new LoaderManager.LoaderCallbacks<ObjectLoader.Result<UserAccount>>() { // from class: com.microsoft.xbox.idp.ui.HeaderFragment.1
        @Override // android.app.LoaderManager.LoaderCallbacks
        public void onLoaderReset(Loader<ObjectLoader.Result<UserAccount>> loader) {
        }

        @Override // android.app.LoaderManager.LoaderCallbacks
        public Loader<ObjectLoader.Result<UserAccount>> onCreateLoader(int i, Bundle bundle) {
            Log.d(HeaderFragment.TAG, "Creating LOADER_GET_PROFILE");
            return new ObjectLoader(HeaderFragment.this.getActivity(), CacheUtil.getObjectLoaderCache(), new FragmentLoaderKey(HeaderFragment.class, 1), UserAccount.class, UserAccount.registerAdapters(new GsonBuilder()).create(), HttpUtil.appendCommonParameters(new HttpCall("GET", EndpointsFactory.get().accounts(), "/users/current/profile"), "4"));
        }

        @Override // android.app.LoaderManager.LoaderCallbacks
        public void onLoadFinished(Loader<ObjectLoader.Result<UserAccount>> loader, ObjectLoader.Result<UserAccount> result) {
            Log.d(HeaderFragment.TAG, "LOADER_GET_PROFILE finished");
            if (!result.hasData()) {
                Log.e(HeaderFragment.TAG, "Error getting UserAccount");
                return;
            }
            HeaderFragment.this.userAccount = result.getData();
            HeaderFragment.this.userEmail.setText(HeaderFragment.this.userAccount.email);
            if (!TextUtils.isEmpty(HeaderFragment.this.userAccount.firstName) || !TextUtils.isEmpty(HeaderFragment.this.userAccount.lastName)) {
                HeaderFragment.this.userName.setVisibility(View.VISIBLE);
                HeaderFragment.this.userName.setText(HeaderFragment.this.getString(R.string.xbid_first_and_last_name_android, new Object[]{HeaderFragment.this.userAccount.firstName, HeaderFragment.this.userAccount.lastName}));
            } else {
                HeaderFragment.this.userName.setVisibility(View.GONE);
            }
            HeaderFragment.this.getLoaderManager().initLoader(2, null, HeaderFragment.this.imageCallbacks);
        }
    };
    private final LoaderManager.LoaderCallbacks<BitmapLoader.Result> imageCallbacks = new LoaderManager.LoaderCallbacks<BitmapLoader.Result>() { // from class: com.microsoft.xbox.idp.ui.HeaderFragment.2
        @Override // android.app.LoaderManager.LoaderCallbacks
        public Loader<BitmapLoader.Result> onCreateLoader(int i, Bundle bundle) {
            Log.d(HeaderFragment.TAG, "Creating LOADER_USER_IMAGE_URL");
            Log.d(HeaderFragment.TAG, "url: " + HeaderFragment.this.userAccount.imageUrl);
            return new BitmapLoader(HeaderFragment.this.getActivity(), CacheUtil.getBitmapCache(), HeaderFragment.this.userAccount.imageUrl, HeaderFragment.this.userAccount.imageUrl);
        }

        @Override // android.app.LoaderManager.LoaderCallbacks
        public void onLoadFinished(Loader<BitmapLoader.Result> loader, BitmapLoader.Result result) {
            Log.d(HeaderFragment.TAG, "LOADER_USER_IMAGE_URL finished");
            if (result.hasData()) {
                HeaderFragment.this.userImageView.setVisibility(View.VISIBLE);
                HeaderFragment.this.userImageView.setImageBitmap(result.getData());
            } else if (result.hasException()) {
                HeaderFragment.this.userImageView.setVisibility(View.GONE);
                Log.w(HeaderFragment.TAG, "Failed to load user image with message: " + result.getException().getMessage());
            }
        }

        @Override // android.app.LoaderManager.LoaderCallbacks
        public void onLoaderReset(Loader<BitmapLoader.Result> loader) {
            HeaderFragment.this.userImageView.setImageBitmap(null);
        }
    };

    public interface Callbacks {
        void onClickCloseHeader();
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // android.app.Fragment
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.callbacks = (Callbacks) activity;
    }

    @Override // android.app.Fragment
    public void onDetach() {
        super.onDetach();
        this.callbacks = NO_OP_CALLBACKS;
    }

    @Override // android.app.Fragment
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
    }

    @Override // android.app.Fragment
    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        return layoutInflater.inflate(R.layout.xbid_fragment_header, viewGroup, false);
    }

    @Override // android.app.Fragment
    public void onViewCreated(View view, Bundle bundle) {
        super.onViewCreated(view, bundle);
        view.findViewById(R.id.xbid_close).setOnClickListener(this);
        this.userImageView = (ImageView) view.findViewById(R.id.xbid_user_image);
        this.userName = (TextView) view.findViewById(R.id.xbid_user_name);
        this.userEmail = (TextView) view.findViewById(R.id.xbid_user_email);
    }

    @Override // android.app.Fragment
    public void onResume() {
        super.onResume();
        Bundle arguments = getArguments();
        if (arguments != null) {
            getLoaderManager().initLoader(1, arguments, this.userAccountCallbacks);
        } else {
            Log.e(TAG, "No arguments provided");
        }
    }

    @Override // android.view.View.OnClickListener
    public void onClick(View view) {
        if (view.getId() == R.id.xbid_close) {
            this.callbacks.onClickCloseHeader();
        }
    }
}
