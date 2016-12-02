package nl.djog.arduino;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbDeviceConnection;
import android.hardware.usb.UsbManager;
import android.util.Log;
import android.widget.ScrollView;
import android.widget.TextView;

import com.felhr.usbserial.UsbSerialDevice;
import com.felhr.usbserial.UsbSerialInterface;

import java.io.UnsupportedEncodingException;

public class ArduinoReceiver extends BroadcastReceiver implements UsbSerialInterface.UsbReadCallback {

    protected TextView textView = (TextView) MainActivity.get().findViewById(R.id.output);

    protected UsbManager usbManager = MainActivity.get().usbManager;

    protected UsbDevice device = MainActivity.get().device;

    public ArduinoReceiver() {
        Log.d("asdf", "Registered Receiver");
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d("asd", "Received shit");

        if(!intent.getAction().equals("com.android.example.USB_PERMISSION") || !intent.getExtras().getBoolean(UsbManager.EXTRA_PERMISSION_GRANTED)) {
            Log.e("Permission", "Permission was not obtained!");
            return;
        }

        UsbDeviceConnection con = usbManager.openDevice(device);
        UsbSerialDevice serial = UsbSerialDevice.createUsbSerialDevice(device, con);

        serial.open();

        serial.setBaudRate(9600);

        serial.setDataBits(UsbSerialInterface.DATA_BITS_8);
        serial.setDataBits(UsbSerialInterface.STOP_BITS_1);

        serial.setParity(UsbSerialInterface.PARITY_NONE);

        serial.setFlowControl(UsbSerialInterface.FLOW_CONTROL_OFF);

        serial.read(this);
    }

    @Override
    public void onReceivedData(byte[] bytes) {
        String data;

        try {
            data = new String(bytes, "UTF-8");
            data.concat("\n");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return;
        }

        Log.d("Found: ", data);

        textView = (TextView) MainActivity.get().findViewById(R.id.output);

        textView.append(data);
    }
}
