// Generated by view binder compiler. Do not edit!
package info.nightscout.androidaps.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.viewbinding.ViewBinding;
import info.nightscout.androidaps.R;
import java.lang.NullPointerException;
import java.lang.Override;
import java.lang.String;

public final class ObjectivesItemBinding implements ViewBinding {
  @NonNull
  private final CardView rootView;

  @NonNull
  public final TextView objectiveAccomplished;

  @NonNull
  public final Button objectiveEnterbutton;

  @NonNull
  public final TextView objectiveGate;

  @NonNull
  public final EditText objectiveInput;

  @NonNull
  public final TextView objectiveInputhint;

  @NonNull
  public final TextView objectiveObjective;

  @NonNull
  public final LinearLayout objectiveProgress;

  @NonNull
  public final TextView objectiveRequestcode;

  @NonNull
  public final Button objectiveStart;

  @NonNull
  public final TextView objectiveTitle;

  @NonNull
  public final Button objectiveUnfinish;

  @NonNull
  public final Button objectiveUnstart;

  @NonNull
  public final Button objectiveVerify;

  private ObjectivesItemBinding(@NonNull CardView rootView, @NonNull TextView objectiveAccomplished,
      @NonNull Button objectiveEnterbutton, @NonNull TextView objectiveGate,
      @NonNull EditText objectiveInput, @NonNull TextView objectiveInputhint,
      @NonNull TextView objectiveObjective, @NonNull LinearLayout objectiveProgress,
      @NonNull TextView objectiveRequestcode, @NonNull Button objectiveStart,
      @NonNull TextView objectiveTitle, @NonNull Button objectiveUnfinish,
      @NonNull Button objectiveUnstart, @NonNull Button objectiveVerify) {
    this.rootView = rootView;
    this.objectiveAccomplished = objectiveAccomplished;
    this.objectiveEnterbutton = objectiveEnterbutton;
    this.objectiveGate = objectiveGate;
    this.objectiveInput = objectiveInput;
    this.objectiveInputhint = objectiveInputhint;
    this.objectiveObjective = objectiveObjective;
    this.objectiveProgress = objectiveProgress;
    this.objectiveRequestcode = objectiveRequestcode;
    this.objectiveStart = objectiveStart;
    this.objectiveTitle = objectiveTitle;
    this.objectiveUnfinish = objectiveUnfinish;
    this.objectiveUnstart = objectiveUnstart;
    this.objectiveVerify = objectiveVerify;
  }

  @Override
  @NonNull
  public CardView getRoot() {
    return rootView;
  }

  @NonNull
  public static ObjectivesItemBinding inflate(@NonNull LayoutInflater inflater) {
    return inflate(inflater, null, false);
  }

  @NonNull
  public static ObjectivesItemBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup parent, boolean attachToParent) {
    View root = inflater.inflate(R.layout.objectives_item, parent, false);
    if (attachToParent) {
      parent.addView(root);
    }
    return bind(root);
  }

  @NonNull
  public static ObjectivesItemBinding bind(@NonNull View rootView) {
    // The body of this method is generated in a way you would not otherwise write.
    // This is done to optimize the compiled bytecode for size and performance.
    int id;
    missingId: {
      id = R.id.objective_accomplished;
      TextView objectiveAccomplished = rootView.findViewById(id);
      if (objectiveAccomplished == null) {
        break missingId;
      }

      id = R.id.objective_enterbutton;
      Button objectiveEnterbutton = rootView.findViewById(id);
      if (objectiveEnterbutton == null) {
        break missingId;
      }

      id = R.id.objective_gate;
      TextView objectiveGate = rootView.findViewById(id);
      if (objectiveGate == null) {
        break missingId;
      }

      id = R.id.objective_input;
      EditText objectiveInput = rootView.findViewById(id);
      if (objectiveInput == null) {
        break missingId;
      }

      id = R.id.objective_inputhint;
      TextView objectiveInputhint = rootView.findViewById(id);
      if (objectiveInputhint == null) {
        break missingId;
      }

      id = R.id.objective_objective;
      TextView objectiveObjective = rootView.findViewById(id);
      if (objectiveObjective == null) {
        break missingId;
      }

      id = R.id.objective_progress;
      LinearLayout objectiveProgress = rootView.findViewById(id);
      if (objectiveProgress == null) {
        break missingId;
      }

      id = R.id.objective_requestcode;
      TextView objectiveRequestcode = rootView.findViewById(id);
      if (objectiveRequestcode == null) {
        break missingId;
      }

      id = R.id.objective_start;
      Button objectiveStart = rootView.findViewById(id);
      if (objectiveStart == null) {
        break missingId;
      }

      id = R.id.objective_title;
      TextView objectiveTitle = rootView.findViewById(id);
      if (objectiveTitle == null) {
        break missingId;
      }

      id = R.id.objective_unfinish;
      Button objectiveUnfinish = rootView.findViewById(id);
      if (objectiveUnfinish == null) {
        break missingId;
      }

      id = R.id.objective_unstart;
      Button objectiveUnstart = rootView.findViewById(id);
      if (objectiveUnstart == null) {
        break missingId;
      }

      id = R.id.objective_verify;
      Button objectiveVerify = rootView.findViewById(id);
      if (objectiveVerify == null) {
        break missingId;
      }

      return new ObjectivesItemBinding((CardView) rootView, objectiveAccomplished,
          objectiveEnterbutton, objectiveGate, objectiveInput, objectiveInputhint,
          objectiveObjective, objectiveProgress, objectiveRequestcode, objectiveStart,
          objectiveTitle, objectiveUnfinish, objectiveUnstart, objectiveVerify);
    }
    String missingId = rootView.getResources().getResourceName(id);
    throw new NullPointerException("Missing required view with ID: ".concat(missingId));
  }
}