package nl.djog.arduino;

import android.content.Context;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener {
    public UsbManager usbManager;

    protected ListView devicesListView;

    public LayoutInflater factory;

    public DevicesArrayAdapter devicesArrayAdapter;

    public SwipeRefreshLayout swipeRefreshLayout;

    protected static MainActivity i;

    protected UsbDevice device;

    public static MainActivity get() {
        return i;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        factory = LayoutInflater.from(this);

        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.activity_main);
        swipeRefreshLayout.setOnRefreshListener(this);

        usbManager = (UsbManager) getSystemService(Context.USB_SERVICE);

        devicesListView = (ListView) findViewById(R.id.devices_view);

        devicesArrayAdapter = new DevicesArrayAdapter(this, getUsbDevices());

        devicesListView.setAdapter(devicesArrayAdapter);

        swipeRefreshLayout.setColorSchemeColors(
                getResources().getColor(R.color.colorAccent)
        );

        i = this;
    }

    private List<UsbDevice> getUsbDevices() {
        return new ArrayList<>(usbManager.getDeviceList().values());
    }

    @Override
    public void onRefresh() {
        devicesArrayAdapter.clear();

        devicesArrayAdapter.addAll(getUsbDevices());

        swipeRefreshLayout.setRefreshing(false);
    }
}
