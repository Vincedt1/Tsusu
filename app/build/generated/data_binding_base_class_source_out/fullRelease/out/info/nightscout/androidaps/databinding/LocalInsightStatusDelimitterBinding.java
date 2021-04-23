// Generated by view binder compiler. Do not edit!
package info.nightscout.androidaps.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewbinding.ViewBinding;
import info.nightscout.androidaps.R;
import java.lang.NullPointerException;
import java.lang.Override;

public final class LocalInsightStatusDelimitterBinding implements ViewBinding {
  @NonNull
  private final View rootView;

  private LocalInsightStatusDelimitterBinding(@NonNull View rootView) {
    this.rootView = rootView;
  }

  @Override
  @NonNull
  public View getRoot() {
    return rootView;
  }

  @NonNull
  public static LocalInsightStatusDelimitterBinding inflate(@NonNull LayoutInflater inflater) {
    return inflate(inflater, null, false);
  }

  @NonNull
  public static LocalInsightStatusDelimitterBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup parent, boolean attachToParent) {
    View root = inflater.inflate(R.layout.local_insight_status_delimitter, parent, false);
    if (attachToParent) {
      parent.addView(root);
    }
    return bind(root);
  }

  @NonNull
  public static LocalInsightStatusDelimitterBinding bind(@NonNull View rootView) {
    if (rootView == null) {
      throw new NullPointerException("rootView");
    }

    return new LocalInsightStatusDelimitterBinding(rootView);
  }
}
