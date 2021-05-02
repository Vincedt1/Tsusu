// Generated by view binder compiler. Do not edit!
package info.nightscout.androidaps.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewbinding.ViewBinding;
import info.nightscout.androidaps.R;
import java.lang.NullPointerException;
import java.lang.Override;
import java.lang.String;

public final class WearFragmentBinding implements ViewBinding {
  @NonNull
  private final FrameLayout rootView;

  @NonNull
  public final Button wearOpensettings;

  @NonNull
  public final Button wearResend;

  private WearFragmentBinding(@NonNull FrameLayout rootView, @NonNull Button wearOpensettings,
      @NonNull Button wearResend) {
    this.rootView = rootView;
    this.wearOpensettings = wearOpensettings;
    this.wearResend = wearResend;
  }

  @Override
  @NonNull
  public FrameLayout getRoot() {
    return rootView;
  }

  @NonNull
  public static WearFragmentBinding inflate(@NonNull LayoutInflater inflater) {
    return inflate(inflater, null, false);
  }

  @NonNull
  public static WearFragmentBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup parent, boolean attachToParent) {
    View root = inflater.inflate(R.layout.wear_fragment, parent, false);
    if (attachToParent) {
      parent.addView(root);
    }
    return bind(root);
  }

  @NonNull
  public static WearFragmentBinding bind(@NonNull View rootView) {
    // The body of this method is generated in a way you would not otherwise write.
    // This is done to optimize the compiled bytecode for size and performance.
    int id;
    missingId: {
      id = R.id.wear_opensettings;
      Button wearOpensettings = rootView.findViewById(id);
      if (wearOpensettings == null) {
        break missingId;
      }

      id = R.id.wear_resend;
      Button wearResend = rootView.findViewById(id);
      if (wearResend == null) {
        break missingId;
      }

      return new WearFragmentBinding((FrameLayout) rootView, wearOpensettings, wearResend);
    }
    String missingId = rootView.getResources().getResourceName(id);
    throw new NullPointerException("Missing required view with ID: ".concat(missingId));
  }
}