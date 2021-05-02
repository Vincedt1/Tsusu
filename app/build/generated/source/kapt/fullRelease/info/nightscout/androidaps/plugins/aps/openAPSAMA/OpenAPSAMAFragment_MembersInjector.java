// Generated by Dagger (https://dagger.dev).
package info.nightscout.androidaps.plugins.aps.openAPSAMA;

import dagger.MembersInjector;
import dagger.android.DispatchingAndroidInjector;
import dagger.android.support.DaggerFragment_MembersInjector;
import dagger.internal.InjectedFieldSignature;
import info.nightscout.androidaps.logging.AAPSLogger;
import info.nightscout.androidaps.plugins.bus.RxBusWrapper;
import info.nightscout.androidaps.utils.DateUtil;
import info.nightscout.androidaps.utils.FabricPrivacy;
import info.nightscout.androidaps.utils.resources.ResourceHelper;
import javax.inject.Provider;

@SuppressWarnings({
    "unchecked",
    "rawtypes"
})
public final class OpenAPSAMAFragment_MembersInjector implements MembersInjector<OpenAPSAMAFragment> {
  private final Provider<DispatchingAndroidInjector<Object>> androidInjectorProvider;

  private final Provider<AAPSLogger> aapsLoggerProvider;

  private final Provider<RxBusWrapper> rxBusProvider;

  private final Provider<ResourceHelper> resourceHelperProvider;

  private final Provider<FabricPrivacy> fabricPrivacyProvider;

  private final Provider<OpenAPSAMAPlugin> openAPSAMAPluginProvider;

  private final Provider<DateUtil> dateUtilProvider;

  public OpenAPSAMAFragment_MembersInjector(
      Provider<DispatchingAndroidInjector<Object>> androidInjectorProvider,
      Provider<AAPSLogger> aapsLoggerProvider, Provider<RxBusWrapper> rxBusProvider,
      Provider<ResourceHelper> resourceHelperProvider,
      Provider<FabricPrivacy> fabricPrivacyProvider,
      Provider<OpenAPSAMAPlugin> openAPSAMAPluginProvider, Provider<DateUtil> dateUtilProvider) {
    this.androidInjectorProvider = androidInjectorProvider;
    this.aapsLoggerProvider = aapsLoggerProvider;
    this.rxBusProvider = rxBusProvider;
    this.resourceHelperProvider = resourceHelperProvider;
    this.fabricPrivacyProvider = fabricPrivacyProvider;
    this.openAPSAMAPluginProvider = openAPSAMAPluginProvider;
    this.dateUtilProvider = dateUtilProvider;
  }

  public static MembersInjector<OpenAPSAMAFragment> create(
      Provider<DispatchingAndroidInjector<Object>> androidInjectorProvider,
      Provider<AAPSLogger> aapsLoggerProvider, Provider<RxBusWrapper> rxBusProvider,
      Provider<ResourceHelper> resourceHelperProvider,
      Provider<FabricPrivacy> fabricPrivacyProvider,
      Provider<OpenAPSAMAPlugin> openAPSAMAPluginProvider, Provider<DateUtil> dateUtilProvider) {
    return new OpenAPSAMAFragment_MembersInjector(androidInjectorProvider, aapsLoggerProvider, rxBusProvider, resourceHelperProvider, fabricPrivacyProvider, openAPSAMAPluginProvider, dateUtilProvider);
  }

  @Override
  public void injectMembers(OpenAPSAMAFragment instance) {
    DaggerFragment_MembersInjector.injectAndroidInjector(instance, androidInjectorProvider.get());
    injectAapsLogger(instance, aapsLoggerProvider.get());
    injectRxBus(instance, rxBusProvider.get());
    injectResourceHelper(instance, resourceHelperProvider.get());
    injectFabricPrivacy(instance, fabricPrivacyProvider.get());
    injectOpenAPSAMAPlugin(instance, openAPSAMAPluginProvider.get());
    injectDateUtil(instance, dateUtilProvider.get());
  }

  @InjectedFieldSignature("info.nightscout.androidaps.plugins.aps.openAPSAMA.OpenAPSAMAFragment.aapsLogger")
  public static void injectAapsLogger(OpenAPSAMAFragment instance, AAPSLogger aapsLogger) {
    instance.aapsLogger = aapsLogger;
  }

  @InjectedFieldSignature("info.nightscout.androidaps.plugins.aps.openAPSAMA.OpenAPSAMAFragment.rxBus")
  public static void injectRxBus(OpenAPSAMAFragment instance, RxBusWrapper rxBus) {
    instance.rxBus = rxBus;
  }

  @InjectedFieldSignature("info.nightscout.androidaps.plugins.aps.openAPSAMA.OpenAPSAMAFragment.resourceHelper")
  public static void injectResourceHelper(OpenAPSAMAFragment instance,
      ResourceHelper resourceHelper) {
    instance.resourceHelper = resourceHelper;
  }

  @InjectedFieldSignature("info.nightscout.androidaps.plugins.aps.openAPSAMA.OpenAPSAMAFragment.fabricPrivacy")
  public static void injectFabricPrivacy(OpenAPSAMAFragment instance, FabricPrivacy fabricPrivacy) {
    instance.fabricPrivacy = fabricPrivacy;
  }

  @InjectedFieldSignature("info.nightscout.androidaps.plugins.aps.openAPSAMA.OpenAPSAMAFragment.openAPSAMAPlugin")
  public static void injectOpenAPSAMAPlugin(OpenAPSAMAFragment instance,
      OpenAPSAMAPlugin openAPSAMAPlugin) {
    instance.openAPSAMAPlugin = openAPSAMAPlugin;
  }

  @InjectedFieldSignature("info.nightscout.androidaps.plugins.aps.openAPSAMA.OpenAPSAMAFragment.dateUtil")
  public static void injectDateUtil(OpenAPSAMAFragment instance, DateUtil dateUtil) {
    instance.dateUtil = dateUtil;
  }
}