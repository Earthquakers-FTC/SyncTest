package nl.djog.arduino;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbManager;
import android.view.View;

public class DeviceOnClickListener implements View.OnClickListener {

    protected UsbDevice device;

    protected UsbManager usbManager;

    public DeviceOnClickListener(UsbDevice device) {
        this.device = device;

        usbManager = (UsbManager) MainActivity.get().getSystemService(Context.USB_SERVICE);
    }

    @Override
    public void onClick(View v) {
        PendingIntent pendingIntent = PendingIntent.getBroadcast(v.getContext(), 0, new Intent("com.android.example.USB_PERMISSION"), 0);

        usbManager.requestPermission(device, pendingIntent);

        Intent intent = new Intent(v.getContext(), SerialActivity.class);

        intent.putExtra("device", device);

        v.getContext().startActivity(intent);
    }
}
