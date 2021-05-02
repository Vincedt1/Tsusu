// Generated by Dagger (https://dagger.dev).
package info.nightscout.androidaps.dialogs;

import dagger.MembersInjector;
import dagger.android.DispatchingAndroidInjector;
import dagger.android.support.DaggerDialogFragment_MembersInjector;
import dagger.internal.InjectedFieldSignature;
import info.nightscout.androidaps.interfaces.ActivePluginProvider;
import info.nightscout.androidaps.interfaces.ProfileFunction;
import info.nightscout.androidaps.logging.AAPSLogger;
import info.nightscout.androidaps.plugins.treatments.TreatmentsPlugin;
import info.nightscout.androidaps.utils.DateUtil;
import info.nightscout.androidaps.utils.resources.ResourceHelper;
import info.nightscout.androidaps.utils.sharedPreferences.SP;
import javax.inject.Provider;

@SuppressWarnings({
    "unchecked",
    "rawtypes"
})
public final class ProfileSwitchDialog_MembersInjector implements MembersInjector<ProfileSwitchDialog> {
  private final Provider<DispatchingAndroidInjector<Object>> androidInjectorProvider;

  private final Provider<AAPSLogger> aapsLoggerProvider;

  private final Provider<SP> spProvider;

  private final Provider<DateUtil> dateUtilProvider;

  private final Provider<ResourceHelper> resourceHelperProvider;

  private final Provider<ProfileFunction> profileFunctionProvider;

  private final Provider<TreatmentsPlugin> treatmentsPluginProvider;

  private final Provider<ActivePluginProvider> activePluginProvider;

  public ProfileSwitchDialog_MembersInjector(
      Provider<DispatchingAndroidInjector<Object>> androidInjectorProvider,
      Provider<AAPSLogger> aapsLoggerProvider, Provider<SP> spProvider,
      Provider<DateUtil> dateUtilProvider, Provider<ResourceHelper> resourceHelperProvider,
      Provider<ProfileFunction> profileFunctionProvider,
      Provider<TreatmentsPlugin> treatmentsPluginProvider,
      Provider<ActivePluginProvider> activePluginProvider) {
    this.androidInjectorProvider = androidInjectorProvider;
    this.aapsLoggerProvider = aapsLoggerProvider;
    this.spProvider = spProvider;
    this.dateUtilProvider = dateUtilProvider;
    this.resourceHelperProvider = resourceHelperProvider;
    this.profileFunctionProvider = profileFunctionProvider;
    this.treatmentsPluginProvider = treatmentsPluginProvider;
    this.activePluginProvider = activePluginProvider;
  }

  public static MembersInjector<ProfileSwitchDialog> create(
      Provider<DispatchingAndroidInjector<Object>> androidInjectorProvider,
      Provider<AAPSLogger> aapsLoggerProvider, Provider<SP> spProvider,
      Provider<DateUtil> dateUtilProvider, Provider<ResourceHelper> resourceHelperProvider,
      Provider<ProfileFunction> profileFunctionProvider,
      Provider<TreatmentsPlugin> treatmentsPluginProvider,
      Provider<ActivePluginProvider> activePluginProvider) {
    return new ProfileSwitchDialog_MembersInjector(androidInjectorProvider, aapsLoggerProvider, spProvider, dateUtilProvider, resourceHelperProvider, profileFunctionProvider, treatmentsPluginProvider, activePluginProvider);
  }

  @Override
  public void injectMembers(ProfileSwitchDialog instance) {
    DaggerDialogFragment_MembersInjector.injectAndroidInjector(instance, androidInjectorProvider.get());
    DialogFragmentWithDate_MembersInjector.injectAapsLogger(instance, aapsLoggerProvider.get());
    DialogFragmentWithDate_MembersInjector.injectSp(instance, spProvider.get());
    DialogFragmentWithDate_MembersInjector.injectDateUtil(instance, dateUtilProvider.get());
    injectResourceHelper(instance, resourceHelperProvider.get());
    injectProfileFunction(instance, profileFunctionProvider.get());
    injectTreatmentsPlugin(instance, treatmentsPluginProvider.get());
    injectActivePlugin(instance, activePluginProvider.get());
  }

  @InjectedFieldSignature("info.nightscout.androidaps.dialogs.ProfileSwitchDialog.resourceHelper")
  public static void injectResourceHelper(ProfileSwitchDialog instance,
      ResourceHelper resourceHelper) {
    instance.resourceHelper = resourceHelper;
  }

  @InjectedFieldSignature("info.nightscout.androidaps.dialogs.ProfileSwitchDialog.profileFunction")
  public static void injectProfileFunction(ProfileSwitchDialog instance,
      ProfileFunction profileFunction) {
    instance.profileFunction = profileFunction;
  }

  @InjectedFieldSignature("info.nightscout.androidaps.dialogs.ProfileSwitchDialog.treatmentsPlugin")
  public static void injectTreatmentsPlugin(ProfileSwitchDialog instance,
      TreatmentsPlugin treatmentsPlugin) {
    instance.treatmentsPlugin = treatmentsPlugin;
  }

  @InjectedFieldSignature("info.nightscout.androidaps.dialogs.ProfileSwitchDialog.activePlugin")
  public static void injectActivePlugin(ProfileSwitchDialog instance,
      ActivePluginProvider activePlugin) {
    instance.activePlugin = activePlugin;
  }
}