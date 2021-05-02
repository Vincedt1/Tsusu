// Generated by view binder compiler. Do not edit!
package info.nightscout.androidaps.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewbinding.ViewBinding;
import info.nightscout.androidaps.R;
import java.lang.NullPointerException;
import java.lang.Override;
import java.lang.String;

public final class ActivityInsightPairingBinding implements ViewBinding {
  @NonNull
  private final FrameLayout rootView;

  @NonNull
  public final TextView code;

  @NonNull
  public final LinearLayout codeCompareSection;

  @NonNull
  public final RecyclerView deviceList;

  @NonNull
  public final LinearLayout deviceSearchSection;

  @NonNull
  public final Button exit;

  @NonNull
  public final Button no;

  @NonNull
  public final LinearLayout pairingCompletedSection;

  @NonNull
  public final TextView pleaseWaitSection;

  @NonNull
  public final Button yes;

  private ActivityInsightPairingBinding(@NonNull FrameLayout rootView, @NonNull TextView code,
      @NonNull LinearLayout codeCompareSection, @NonNull RecyclerView deviceList,
      @NonNull LinearLayout deviceSearchSection, @NonNull Button exit, @NonNull Button no,
      @NonNull LinearLayout pairingCompletedSection, @NonNull TextView pleaseWaitSection,
      @NonNull Button yes) {
    this.rootView = rootView;
    this.code = code;
    this.codeCompareSection = codeCompareSection;
    this.deviceList = deviceList;
    this.deviceSearchSection = deviceSearchSection;
    this.exit = exit;
    this.no = no;
    this.pairingCompletedSection = pairingCompletedSection;
    this.pleaseWaitSection = pleaseWaitSection;
    this.yes = yes;
  }

  @Override
  @NonNull
  public FrameLayout getRoot() {
    return rootView;
  }

  @NonNull
  public static ActivityInsightPairingBinding inflate(@NonNull LayoutInflater inflater) {
    return inflate(inflater, null, false);
  }

  @NonNull
  public static ActivityInsightPairingBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup parent, boolean attachToParent) {
    View root = inflater.inflate(R.layout.activity_insight_pairing, parent, false);
    if (attachToParent) {
      parent.addView(root);
    }
    return bind(root);
  }

  @NonNull
  public static ActivityInsightPairingBinding bind(@NonNull View rootView) {
    // The body of this method is generated in a way you would not otherwise write.
    // This is done to optimize the compiled bytecode for size and performance.
    int id;
    missingId: {
      id = R.id.code;
      TextView code = rootView.findViewById(id);
      if (code == null) {
        break missingId;
      }

      id = R.id.code_compare_section;
      LinearLayout codeCompareSection = rootView.findViewById(id);
      if (codeCompareSection == null) {
        break missingId;
      }

      id = R.id.device_list;
      RecyclerView deviceList = rootView.findViewById(id);
      if (deviceList == null) {
        break missingId;
      }

      id = R.id.device_search_section;
      LinearLayout deviceSearchSection = rootView.findViewById(id);
      if (deviceSearchSection == null) {
        break missingId;
      }

      id = R.id.exit;
      Button exit = rootView.findViewById(id);
      if (exit == null) {
        break missingId;
      }

      id = R.id.no;
      Button no = rootView.findViewById(id);
      if (no == null) {
        break missingId;
      }

      id = R.id.pairing_completed_section;
      LinearLayout pairingCompletedSection = rootView.findViewById(id);
      if (pairingCompletedSection == null) {
        break missingId;
      }

      id = R.id.please_wait_section;
      TextView pleaseWaitSection = rootView.findViewById(id);
      if (pleaseWaitSection == null) {
        break missingId;
      }

      id = R.id.yes;
      Button yes = rootView.findViewById(id);
      if (yes == null) {
        break missingId;
      }

      return new ActivityInsightPairingBinding((FrameLayout) rootView, code, codeCompareSection,
          deviceList, deviceSearchSection, exit, no, pairingCompletedSection, pleaseWaitSection,
          yes);
    }
    String missingId = rootView.getResources().getResourceName(id);
    throw new NullPointerException("Missing required view with ID: ".concat(missingId));
  }
}