// Generated by Dagger (https://dagger.dev).
package info.nightscout.androidaps.historyBrowser;

import dagger.android.HasAndroidInjector;
import dagger.internal.Factory;
import info.nightscout.androidaps.interfaces.ActivePluginProvider;
import info.nightscout.androidaps.interfaces.ProfileFunction;
import info.nightscout.androidaps.logging.AAPSLogger;
import info.nightscout.androidaps.plugins.bus.RxBusWrapper;
import info.nightscout.androidaps.plugins.sensitivity.SensitivityAAPSPlugin;
import info.nightscout.androidaps.plugins.sensitivity.SensitivityOref1Plugin;
import info.nightscout.androidaps.plugins.sensitivity.SensitivityWeightedAveragePlugin;
import info.nightscout.androidaps.utils.DateUtil;
import info.nightscout.androidaps.utils.FabricPrivacy;
import info.nightscout.androidaps.utils.resources.ResourceHelper;
import info.nightscout.androidaps.utils.sharedPreferences.SP;
import javax.inject.Provider;

@SuppressWarnings({
    "unchecked",
    "rawtypes"
})
public final class IobCobCalculatorPluginHistory_Factory implements Factory<IobCobCalculatorPluginHistory> {
  private final Provider<HasAndroidInjector> injectorProvider;

  private final Provider<AAPSLogger> aapsLoggerProvider;

  private final Provider<RxBusWrapper> rxBusProvider;

  private final Provider<SP> spProvider;

  private final Provider<ResourceHelper> resourceHelperProvider;

  private final Provider<ProfileFunction> profileFunctionProvider;

  private final Provider<ActivePluginProvider> activePluginProvider;

  private final Provider<TreatmentsPluginHistory> treatmentsPluginHistoryProvider;

  private final Provider<SensitivityOref1Plugin> sensitivityOref1PluginProvider;

  private final Provider<SensitivityAAPSPlugin> sensitivityAAPSPluginProvider;

  private final Provider<SensitivityWeightedAveragePlugin> sensitivityWeightedAveragePluginProvider;

  private final Provider<FabricPrivacy> fabricPrivacyProvider;

  private final Provider<DateUtil> dateUtilProvider;

  public IobCobCalculatorPluginHistory_Factory(Provider<HasAndroidInjector> injectorProvider,
      Provider<AAPSLogger> aapsLoggerProvider, Provider<RxBusWrapper> rxBusProvider,
      Provider<SP> spProvider, Provider<ResourceHelper> resourceHelperProvider,
      Provider<ProfileFunction> profileFunctionProvider,
      Provider<ActivePluginProvider> activePluginProvider,
      Provider<TreatmentsPluginHistory> treatmentsPluginHistoryProvider,
      Provider<SensitivityOref1Plugin> sensitivityOref1PluginProvider,
      Provider<SensitivityAAPSPlugin> sensitivityAAPSPluginProvider,
      Provider<SensitivityWeightedAveragePlugin> sensitivityWeightedAveragePluginProvider,
      Provider<FabricPrivacy> fabricPrivacyProvider, Provider<DateUtil> dateUtilProvider) {
    this.injectorProvider = injectorProvider;
    this.aapsLoggerProvider = aapsLoggerProvider;
    this.rxBusProvider = rxBusProvider;
    this.spProvider = spProvider;
    this.resourceHelperProvider = resourceHelperProvider;
    this.profileFunctionProvider = profileFunctionProvider;
    this.activePluginProvider = activePluginProvider;
    this.treatmentsPluginHistoryProvider = treatmentsPluginHistoryProvider;
    this.sensitivityOref1PluginProvider = sensitivityOref1PluginProvider;
    this.sensitivityAAPSPluginProvider = sensitivityAAPSPluginProvider;
    this.sensitivityWeightedAveragePluginProvider = sensitivityWeightedAveragePluginProvider;
    this.fabricPrivacyProvider = fabricPrivacyProvider;
    this.dateUtilProvider = dateUtilProvider;
  }

  @Override
  public IobCobCalculatorPluginHistory get() {
    return newInstance(injectorProvider.get(), aapsLoggerProvider.get(), rxBusProvider.get(), spProvider.get(), resourceHelperProvider.get(), profileFunctionProvider.get(), activePluginProvider.get(), treatmentsPluginHistoryProvider.get(), sensitivityOref1PluginProvider.get(), sensitivityAAPSPluginProvider.get(), sensitivityWeightedAveragePluginProvider.get(), fabricPrivacyProvider.get(), dateUtilProvider.get());
  }

  public static IobCobCalculatorPluginHistory_Factory create(
      Provider<HasAndroidInjector> injectorProvider, Provider<AAPSLogger> aapsLoggerProvider,
      Provider<RxBusWrapper> rxBusProvider, Provider<SP> spProvider,
      Provider<ResourceHelper> resourceHelperProvider,
      Provider<ProfileFunction> profileFunctionProvider,
      Provider<ActivePluginProvider> activePluginProvider,
      Provider<TreatmentsPluginHistory> treatmentsPluginHistoryProvider,
      Provider<SensitivityOref1Plugin> sensitivityOref1PluginProvider,
      Provider<SensitivityAAPSPlugin> sensitivityAAPSPluginProvider,
      Provider<SensitivityWeightedAveragePlugin> sensitivityWeightedAveragePluginProvider,
      Provider<FabricPrivacy> fabricPrivacyProvider, Provider<DateUtil> dateUtilProvider) {
    return new IobCobCalculatorPluginHistory_Factory(injectorProvider, aapsLoggerProvider, rxBusProvider, spProvider, resourceHelperProvider, profileFunctionProvider, activePluginProvider, treatmentsPluginHistoryProvider, sensitivityOref1PluginProvider, sensitivityAAPSPluginProvider, sensitivityWeightedAveragePluginProvider, fabricPrivacyProvider, dateUtilProvider);
  }

  public static IobCobCalculatorPluginHistory newInstance(HasAndroidInjector injector,
      AAPSLogger aapsLogger, RxBusWrapper rxBus, SP sp, ResourceHelper resourceHelper,
      ProfileFunction profileFunction, ActivePluginProvider activePlugin,
      TreatmentsPluginHistory treatmentsPluginHistory,
      SensitivityOref1Plugin sensitivityOref1Plugin, SensitivityAAPSPlugin sensitivityAAPSPlugin,
      SensitivityWeightedAveragePlugin sensitivityWeightedAveragePlugin,
      FabricPrivacy fabricPrivacy, DateUtil dateUtil) {
    return new IobCobCalculatorPluginHistory(injector, aapsLogger, rxBus, sp, resourceHelper, profileFunction, activePlugin, treatmentsPluginHistory, sensitivityOref1Plugin, sensitivityAAPSPlugin, sensitivityWeightedAveragePlugin, fabricPrivacy, dateUtil);
  }
}