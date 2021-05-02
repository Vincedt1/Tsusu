// Generated by view binder compiler. Do not edit!
package info.nightscout.androidaps.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.ScrollView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewbinding.ViewBinding;
import info.nightscout.androidaps.R;
import info.nightscout.androidaps.utils.ui.MinutesNumberPicker;
import info.nightscout.androidaps.utils.ui.NumberPicker;
import java.lang.NullPointerException;
import java.lang.Override;
import java.lang.String;

public final class DialogCareBinding implements ViewBinding {
  @NonNull
  private final ScrollView rootView;

  @NonNull
  public final NumberPicker bg;

  @NonNull
  public final LinearLayout bgLayout;

  @NonNull
  public final RadioGroup bgsource;

  @NonNull
  public final TextView bgunits;

  @NonNull
  public final DatetimeBinding datetime;

  @NonNull
  public final MinutesNumberPicker duration;

  @NonNull
  public final LinearLayout durationLayout;

  @NonNull
  public final ImageView icon;

  @NonNull
  public final RadioButton meter;

  @NonNull
  public final NotesBinding notesLayout;

  @NonNull
  public final OkcancelBinding okcancel;

  @NonNull
  public final RadioButton other;

  @NonNull
  public final RadioButton sensor;

  @NonNull
  public final LinearLayout spacer;

  @NonNull
  public final TextView title;

  private DialogCareBinding(@NonNull ScrollView rootView, @NonNull NumberPicker bg,
      @NonNull LinearLayout bgLayout, @NonNull RadioGroup bgsource, @NonNull TextView bgunits,
      @NonNull DatetimeBinding datetime, @NonNull MinutesNumberPicker duration,
      @NonNull LinearLayout durationLayout, @NonNull ImageView icon, @NonNull RadioButton meter,
      @NonNull NotesBinding notesLayout, @NonNull OkcancelBinding okcancel,
      @NonNull RadioButton other, @NonNull RadioButton sensor, @NonNull LinearLayout spacer,
      @NonNull TextView title) {
    this.rootView = rootView;
    this.bg = bg;
    this.bgLayout = bgLayout;
    this.bgsource = bgsource;
    this.bgunits = bgunits;
    this.datetime = datetime;
    this.duration = duration;
    this.durationLayout = durationLayout;
    this.icon = icon;
    this.meter = meter;
    this.notesLayout = notesLayout;
    this.okcancel = okcancel;
    this.other = other;
    this.sensor = sensor;
    this.spacer = spacer;
    this.title = title;
  }

  @Override
  @NonNull
  public ScrollView getRoot() {
    return rootView;
  }

  @NonNull
  public static DialogCareBinding inflate(@NonNull LayoutInflater inflater) {
    return inflate(inflater, null, false);
  }

  @NonNull
  public static DialogCareBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup parent, boolean attachToParent) {
    View root = inflater.inflate(R.layout.dialog_care, parent, false);
    if (attachToParent) {
      parent.addView(root);
    }
    return bind(root);
  }

  @NonNull
  public static DialogCareBinding bind(@NonNull View rootView) {
    // The body of this method is generated in a way you would not otherwise write.
    // This is done to optimize the compiled bytecode for size and performance.
    int id;
    missingId: {
      id = R.id.bg;
      NumberPicker bg = rootView.findViewById(id);
      if (bg == null) {
        break missingId;
      }

      id = R.id.bg_layout;
      LinearLayout bgLayout = rootView.findViewById(id);
      if (bgLayout == null) {
        break missingId;
      }

      id = R.id.bgsource;
      RadioGroup bgsource = rootView.findViewById(id);
      if (bgsource == null) {
        break missingId;
      }

      id = R.id.bgunits;
      TextView bgunits = rootView.findViewById(id);
      if (bgunits == null) {
        break missingId;
      }

      id = R.id.datetime;
      View datetime = rootView.findViewById(id);
      if (datetime == null) {
        break missingId;
      }
      DatetimeBinding binding_datetime = DatetimeBinding.bind(datetime);

      id = R.id.duration;
      MinutesNumberPicker duration = rootView.findViewById(id);
      if (duration == null) {
        break missingId;
      }

      id = R.id.duration_layout;
      LinearLayout durationLayout = rootView.findViewById(id);
      if (durationLayout == null) {
        break missingId;
      }

      id = R.id.icon;
      ImageView icon = rootView.findViewById(id);
      if (icon == null) {
        break missingId;
      }

      id = R.id.meter;
      RadioButton meter = rootView.findViewById(id);
      if (meter == null) {
        break missingId;
      }

      id = R.id.notes_layout;
      View notesLayout = rootView.findViewById(id);
      if (notesLayout == null) {
        break missingId;
      }
      NotesBinding binding_notesLayout = NotesBinding.bind(notesLayout);

      id = R.id.okcancel;
      View okcancel = rootView.findViewById(id);
      if (okcancel == null) {
        break missingId;
      }
      OkcancelBinding binding_okcancel = OkcancelBinding.bind(okcancel);

      id = R.id.other;
      RadioButton other = rootView.findViewById(id);
      if (other == null) {
        break missingId;
      }

      id = R.id.sensor;
      RadioButton sensor = rootView.findViewById(id);
      if (sensor == null) {
        break missingId;
      }

      id = R.id.spacer;
      LinearLayout spacer = rootView.findViewById(id);
      if (spacer == null) {
        break missingId;
      }

      id = R.id.title;
      TextView title = rootView.findViewById(id);
      if (title == null) {
        break missingId;
      }

      return new DialogCareBinding((ScrollView) rootView, bg, bgLayout, bgsource, bgunits,
          binding_datetime, duration, durationLayout, icon, meter, binding_notesLayout,
          binding_okcancel, other, sensor, spacer, title);
    }
    String missingId = rootView.getResources().getResourceName(id);
    throw new NullPointerException("Missing required view with ID: ".concat(missingId));
  }
}