// Generated by Dagger (https://dagger.dev).
package info.nightscout.androidaps.plugins.profile.ns;

import dagger.MembersInjector;
import dagger.android.DispatchingAndroidInjector;
import dagger.android.support.DaggerFragment_MembersInjector;
import dagger.internal.InjectedFieldSignature;
import info.nightscout.androidaps.interfaces.ProfileFunction;
import info.nightscout.androidaps.plugins.bus.RxBusWrapper;
import info.nightscout.androidaps.plugins.treatments.TreatmentsPlugin;
import info.nightscout.androidaps.utils.FabricPrivacy;
import info.nightscout.androidaps.utils.resources.ResourceHelper;
import javax.inject.Provider;

@SuppressWarnings({
    "unchecked",
    "rawtypes"
})
public final class NSProfileFragment_MembersInjector implements MembersInjector<NSProfileFragment> {
  private final Provider<DispatchingAndroidInjector<Object>> androidInjectorProvider;

  private final Provider<TreatmentsPlugin> treatmentsPluginProvider;

  private final Provider<RxBusWrapper> rxBusProvider;

  private final Provider<ResourceHelper> resourceHelperProvider;

  private final Provider<FabricPrivacy> fabricPrivacyProvider;

  private final Provider<ProfileFunction> profileFunctionProvider;

  private final Provider<NSProfilePlugin> nsProfilePluginProvider;

  public NSProfileFragment_MembersInjector(
      Provider<DispatchingAndroidInjector<Object>> androidInjectorProvider,
      Provider<TreatmentsPlugin> treatmentsPluginProvider, Provider<RxBusWrapper> rxBusProvider,
      Provider<ResourceHelper> resourceHelperProvider,
      Provider<FabricPrivacy> fabricPrivacyProvider,
      Provider<ProfileFunction> profileFunctionProvider,
      Provider<NSProfilePlugin> nsProfilePluginProvider) {
    this.androidInjectorProvider = androidInjectorProvider;
    this.treatmentsPluginProvider = treatmentsPluginProvider;
    this.rxBusProvider = rxBusProvider;
    this.resourceHelperProvider = resourceHelperProvider;
    this.fabricPrivacyProvider = fabricPrivacyProvider;
    this.profileFunctionProvider = profileFunctionProvider;
    this.nsProfilePluginProvider = nsProfilePluginProvider;
  }

  public static MembersInjector<NSProfileFragment> create(
      Provider<DispatchingAndroidInjector<Object>> androidInjectorProvider,
      Provider<TreatmentsPlugin> treatmentsPluginProvider, Provider<RxBusWrapper> rxBusProvider,
      Provider<ResourceHelper> resourceHelperProvider,
      Provider<FabricPrivacy> fabricPrivacyProvider,
      Provider<ProfileFunction> profileFunctionProvider,
      Provider<NSProfilePlugin> nsProfilePluginProvider) {
    return new NSProfileFragment_MembersInjector(androidInjectorProvider, treatmentsPluginProvider, rxBusProvider, resourceHelperProvider, fabricPrivacyProvider, profileFunctionProvider, nsProfilePluginProvider);
  }

  @Override
  public void injectMembers(NSProfileFragment instance) {
    DaggerFragment_MembersInjector.injectAndroidInjector(instance, androidInjectorProvider.get());
    injectTreatmentsPlugin(instance, treatmentsPluginProvider.get());
    injectRxBus(instance, rxBusProvider.get());
    injectResourceHelper(instance, resourceHelperProvider.get());
    injectFabricPrivacy(instance, fabricPrivacyProvider.get());
    injectProfileFunction(instance, profileFunctionProvider.get());
    injectNsProfilePlugin(instance, nsProfilePluginProvider.get());
  }

  @InjectedFieldSignature("info.nightscout.androidaps.plugins.profile.ns.NSProfileFragment.treatmentsPlugin")
  public static void injectTreatmentsPlugin(NSProfileFragment instance,
      TreatmentsPlugin treatmentsPlugin) {
    instance.treatmentsPlugin = treatmentsPlugin;
  }

  @InjectedFieldSignature("info.nightscout.androidaps.plugins.profile.ns.NSProfileFragment.rxBus")
  public static void injectRxBus(NSProfileFragment instance, RxBusWrapper rxBus) {
    instance.rxBus = rxBus;
  }

  @InjectedFieldSignature("info.nightscout.androidaps.plugins.profile.ns.NSProfileFragment.resourceHelper")
  public static void injectResourceHelper(NSProfileFragment instance,
      ResourceHelper resourceHelper) {
    instance.resourceHelper = resourceHelper;
  }

  @InjectedFieldSignature("info.nightscout.androidaps.plugins.profile.ns.NSProfileFragment.fabricPrivacy")
  public static void injectFabricPrivacy(NSProfileFragment instance, FabricPrivacy fabricPrivacy) {
    instance.fabricPrivacy = fabricPrivacy;
  }

  @InjectedFieldSignature("info.nightscout.androidaps.plugins.profile.ns.NSProfileFragment.profileFunction")
  public static void injectProfileFunction(NSProfileFragment instance,
      ProfileFunction profileFunction) {
    instance.profileFunction = profileFunction;
  }

  @InjectedFieldSignature("info.nightscout.androidaps.plugins.profile.ns.NSProfileFragment.nsProfilePlugin")
  public static void injectNsProfilePlugin(NSProfileFragment instance,
      NSProfilePlugin nsProfilePlugin) {
    instance.nsProfilePlugin = nsProfilePlugin;
  }
}
