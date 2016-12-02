package nl.djog.arduino;

import android.support.annotation.NonNull;

import android.content.Context;
import android.hardware.usb.UsbDevice;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class DevicesArrayAdapter extends ArrayAdapter<UsbDevice> {

    public DevicesArrayAdapter(Context context, List<UsbDevice> devices) {
        super(context, R.layout.usb_device, devices);
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        TextView title, path;
        ImageView image;
        UsbDevice device;

        device = getItem(position);

        if(convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.usb_device, parent, false);
        }

        convertView.setTag(device);

        convertView.setOnClickListener(new DeviceOnClickListener(device));

        image = (ImageView) convertView.findViewById(R.id.device_icon);
        title = (TextView) convertView.findViewById(R.id.device_title);
        path = (TextView) convertView.findViewById(R.id.device_path);

        try {
            title.setText(device.getProductName());
        } catch(NullPointerException e) {
            title.setText("null");
        }

        if(device.getManufacturerName().toLowerCase().contains("arduino") || device.getVendorId() == 0x2341) {
            image.setImageResource(R.drawable.ic_developer_board_black_36dp);
            title.setText("Arduino");
        } else if(device.getProductName().toLowerCase().contains("storage")) {
            image.setImageResource(R.drawable.ic_storage_black_36dp);
        } else {
            image.setImageResource(R.drawable.ic_videogame_asset_black_36dp);
        }

        path.setText(device.getManufacturerName());

        return convertView;
    }
}
