// Generated by view binder compiler. Do not edit!
package info.nightscout.androidaps.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewbinding.ViewBinding;
import info.nightscout.androidaps.R;
import java.lang.NullPointerException;
import java.lang.Override;
import java.lang.String;

public final class ActivitySetupwizardBinding implements ViewBinding {
  @NonNull
  private final LinearLayout rootView;

  @NonNull
  public final Button finishButton;

  @NonNull
  public final Button nextButton;

  @NonNull
  public final Button previousButton;

  @NonNull
  public final TextView swContent;

  @NonNull
  public final LinearLayout swContentFields;

  @NonNull
  public final ImageButton swExit;

  @NonNull
  public final ScrollView swScrollview;

  private ActivitySetupwizardBinding(@NonNull LinearLayout rootView, @NonNull Button finishButton,
      @NonNull Button nextButton, @NonNull Button previousButton, @NonNull TextView swContent,
      @NonNull LinearLayout swContentFields, @NonNull ImageButton swExit,
      @NonNull ScrollView swScrollview) {
    this.rootView = rootView;
    this.finishButton = finishButton;
    this.nextButton = nextButton;
    this.previousButton = previousButton;
    this.swContent = swContent;
    this.swContentFields = swContentFields;
    this.swExit = swExit;
    this.swScrollview = swScrollview;
  }

  @Override
  @NonNull
  public LinearLayout getRoot() {
    return rootView;
  }

  @NonNull
  public static ActivitySetupwizardBinding inflate(@NonNull LayoutInflater inflater) {
    return inflate(inflater, null, false);
  }

  @NonNull
  public static ActivitySetupwizardBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup parent, boolean attachToParent) {
    View root = inflater.inflate(R.layout.activity_setupwizard, parent, false);
    if (attachToParent) {
      parent.addView(root);
    }
    return bind(root);
  }

  @NonNull
  public static ActivitySetupwizardBinding bind(@NonNull View rootView) {
    // The body of this method is generated in a way you would not otherwise write.
    // This is done to optimize the compiled bytecode for size and performance.
    int id;
    missingId: {
      id = R.id.finish_button;
      Button finishButton = rootView.findViewById(id);
      if (finishButton == null) {
        break missingId;
      }

      id = R.id.next_button;
      Button nextButton = rootView.findViewById(id);
      if (nextButton == null) {
        break missingId;
      }

      id = R.id.previous_button;
      Button previousButton = rootView.findViewById(id);
      if (previousButton == null) {
        break missingId;
      }

      id = R.id.sw_content;
      TextView swContent = rootView.findViewById(id);
      if (swContent == null) {
        break missingId;
      }

      id = R.id.sw_content_fields;
      LinearLayout swContentFields = rootView.findViewById(id);
      if (swContentFields == null) {
        break missingId;
      }

      id = R.id.sw_exit;
      ImageButton swExit = rootView.findViewById(id);
      if (swExit == null) {
        break missingId;
      }

      id = R.id.sw_scrollview;
      ScrollView swScrollview = rootView.findViewById(id);
      if (swScrollview == null) {
        break missingId;
      }

      return new ActivitySetupwizardBinding((LinearLayout) rootView, finishButton, nextButton,
          previousButton, swContent, swContentFields, swExit, swScrollview);
    }
    String missingId = rootView.getResources().getResourceName(id);
    throw new NullPointerException("Missing required view with ID: ".concat(missingId));
  }
}
