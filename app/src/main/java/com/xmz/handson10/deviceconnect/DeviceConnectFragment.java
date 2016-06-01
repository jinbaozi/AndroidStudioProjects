package com.xmz.handson10.deviceconnect;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.NestedScrollView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.xmz.handson10.R;
import com.xmz.handson10.data.DeviceAvailable;
import com.xmz.handson10.data.DeviceDescription;
import com.xmz.handson10.data.DeviceSocket;

import java.util.List;

/**
 * Created by xmz on 2016/5/30.
 */
public class DeviceConnectFragment extends Fragment implements DeviceConnectContract.View {

    private DeviceConnectContract.Presenter mPresenter;

    private DrawerLayout mDrawerLayout;

    private NestedScrollView mNestedScrollView;

    private LinearLayout mDeviceLL;

    private FrameLayout mFrameLayout;

    private RelativeLayout mCntentLL;

    private FloatingActionButton fabOpenDrawer;

    private int mSocketX;

    private int mSocketY;



    public DeviceConnectFragment() {

    }

    public static DeviceConnectFragment newInstance() { return new DeviceConnectFragment(); }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public void onResume() {
        super.onResume();
        mPresenter.start();
    }

    @Override
    public void setPresenter(DeviceConnectContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.connect_frag, container, false);

        mDrawerLayout = (DrawerLayout) root.findViewById(R.id.drawer_layout_frag);

        mNestedScrollView = (NestedScrollView) root.findViewById(R.id.devices_scroll_view);
        mDeviceLL = (LinearLayout) root.findViewById(R.id.devices_linear_layout);
        mFrameLayout = (FrameLayout) root.findViewById(R.id.contentFrame);
        mCntentLL = (RelativeLayout) root.findViewById(R.id.contentLL);

        fabOpenDrawer =
                (FloatingActionButton) root.findViewById(R.id.fab_open_drawer);
        fabOpenDrawer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDeviceDescriptionsDrawer();
            }
        });

        return root;
    }

    @Override
    public void showDeviceDescriptions(List<DeviceDescription> deviceDescriptions) {

        mDeviceLL.removeAllViews();
        for (DeviceDescription deviceDescription : deviceDescriptions) {
            LinearLayout compLL = (LinearLayout) getActivity().getLayoutInflater().inflate(R.layout.device_description_comp, null);
            TextView deviceName = (TextView) compLL.getChildAt(0);
            ImageView devicePic = (ImageView) compLL.getChildAt(1);
            TextView deviceFeature = (TextView) compLL.getChildAt(2);

            deviceName.setText(deviceDescription.getDeviceName());
            devicePic.setImageResource(deviceDescription.getDevicePicSrcId());
            deviceFeature.setText(deviceDescription.getTypeFeatureId());

            int newId = View.generateViewId();
            compLL.setId(newId);
            compLL.setTag(deviceDescription.getTypeId());

            mDeviceLL.addView(compLL);
        }
    }

    @Override
    public void showDeviceSockets(List<DeviceSocket> deviceSockets) {
        mCntentLL.removeAllViews();
        for (DeviceSocket deviceSocket : deviceSockets) {
            LinearLayout compLL = (LinearLayout) getActivity().getLayoutInflater().inflate(R.layout.device_socket_comp, null);
            ImageView socketPic = (ImageView) compLL.getChildAt(0);
            TextView socketTypeName = (TextView) compLL.getChildAt(1);
            socketPic.setImageResource(deviceSocket.getPicSrcId());
            socketTypeName.setText(deviceSocket.getSocketId());

            int newId = View.generateViewId();
            compLL.setId(newId);
            compLL.setTag(deviceSocket.getSocketId());

            compLL.setOnTouchListener(new DeviceSocketTouchListener());

            mCntentLL.addView(compLL);
        }
    }
    private class DeviceSocketTouchListener implements View.OnTouchListener {
        RelativeLayout.LayoutParams layoutParams;
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            final int X = (int) event.getRawX();
            final int Y = (int) event.getRawY();
            switch ( event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    layoutParams =
                            (RelativeLayout.LayoutParams) v.getLayoutParams();
                    mSocketX = X - layoutParams.leftMargin;
                    mSocketY = Y - layoutParams.topMargin;
                    break;
                case MotionEvent.ACTION_UP:
                    Log.d("TouchEvent",String.valueOf(v.getTag()));
                    break;
                case MotionEvent.ACTION_MOVE:
                    layoutParams =
                            (RelativeLayout.LayoutParams) v.getLayoutParams();
                    layoutParams.leftMargin = X - mSocketX;
                    layoutParams.topMargin = Y - mSocketY;
                    layoutParams.rightMargin = 0;
                    layoutParams.bottomMargin = 0;
                    v.setLayoutParams(layoutParams);
                    break;

            }
            mCntentLL.invalidate();
            return true;
        }
    }

    @Override
    public void showAvailableDevices(List<DeviceAvailable> availableDevices) {

    }

    @Override
    public void showEditControl() {

    }

    @Override
    public void showDeviceDescriptionsDrawer() {
        mDrawerLayout.openDrawer(GravityCompat.START);
    }
}
