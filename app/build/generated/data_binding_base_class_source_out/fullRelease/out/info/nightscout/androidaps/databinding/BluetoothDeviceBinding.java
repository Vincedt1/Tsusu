// Generated by view binder compiler. Do not edit!
package info.nightscout.androidaps.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewbinding.ViewBinding;
import info.nightscout.androidaps.R;
import java.lang.NullPointerException;
import java.lang.Override;

public final class BluetoothDeviceBinding implements ViewBinding {
  @NonNull
  private final TextView rootView;

  private BluetoothDeviceBinding(@NonNull TextView rootView) {
    this.rootView = rootView;
  }

  @Override
  @NonNull
  public TextView getRoot() {
    return rootView;
  }

  @NonNull
  public static BluetoothDeviceBinding inflate(@NonNull LayoutInflater inflater) {
    return inflate(inflater, null, false);
  }

  @NonNull
  public static BluetoothDeviceBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup parent, boolean attachToParent) {
    View root = inflater.inflate(R.layout.bluetooth_device, parent, false);
    if (attachToParent) {
      parent.addView(root);
    }
    return bind(root);
  }

  @NonNull
  public static BluetoothDeviceBinding bind(@NonNull View rootView) {
    if (rootView == null) {
      throw new NullPointerException("rootView");
    }

    return new BluetoothDeviceBinding((TextView) rootView);
  }
}
