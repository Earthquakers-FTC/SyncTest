package nl.djog.arduino;

import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbDeviceConnection;
import android.hardware.usb.UsbManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.felhr.usbserial.UsbSerialDevice;
import com.felhr.usbserial.UsbSerialInterface;

import org.w3c.dom.Text;

import java.io.UnsupportedEncodingException;

public class SerialActivity extends AppCompatActivity implements UsbSerialDevice.UsbReadCallback {

    protected UsbDevice device;

    protected TextView textView;

    protected UsbManager manager = MainActivity.get().usbManager;

    protected RelativeLayout scrollView;

    protected final int BAUD_RATE = 9600;

    // Byte storage
    protected byte[] bytes = new byte[8];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_serial);

        scrollView = (RelativeLayout) findViewById(R.id.activity_serial);

        textView = (TextView) findViewById(R.id.output);

        device = (UsbDevice) getIntent().getExtras().get("device");

        UsbDeviceConnection connection = manager.openDevice(device);

        UsbSerialDevice serial = UsbSerialDevice.createUsbSerialDevice(device, connection);

        if(serial == null)
            return;

        serial.open();
        serial.setBaudRate(BAUD_RATE);

        serial.setDataBits(UsbSerialInterface.DATA_BITS_8);
        serial.setStopBits(UsbSerialInterface.STOP_BITS_1);
        serial.setParity(UsbSerialInterface.PARITY_NONE);
        serial.setFlowControl(UsbSerialInterface.FLOW_CONTROL_OFF);

        serial.read(this);

        Log.d("swag", "started reading");
    }

    private String bytesToString(byte[] b) {
        StringBuilder builder;

        builder = new StringBuilder();

        for (byte aByte : b) {
            builder.append(aByte);
            builder.append(", ");
        }

        return builder.toString();
    }

    private void updateDisplay() {
        final TextView ftv = textView;
        final String text = bytesToString(this.bytes);

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                ftv.setText(text);
            }
        });
    }

    @Override
    public void onReceivedData(byte[] b) {
        Log.d("test", bytesToString(b));

        if(b.length != 2)
            return;

        int index = b[0];

        if(this.bytes.length < index)
            return;

        this.bytes[index] = b[1];

        updateDisplay();
    }
}
