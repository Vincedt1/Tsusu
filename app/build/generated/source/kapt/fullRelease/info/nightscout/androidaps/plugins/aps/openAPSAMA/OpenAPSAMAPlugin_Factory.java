// Generated by Dagger (https://dagger.dev).
package info.nightscout.androidaps.plugins.aps.openAPSAMA;

import android.content.Context;
import dagger.android.HasAndroidInjector;
import dagger.internal.Factory;
import info.nightscout.androidaps.interfaces.ActivePluginProvider;
import info.nightscout.androidaps.interfaces.ProfileFunction;
import info.nightscout.androidaps.logging.AAPSLogger;
import info.nightscout.androidaps.plugins.bus.RxBusWrapper;
import info.nightscout.androidaps.plugins.configBuilder.ConstraintChecker;
import info.nightscout.androidaps.plugins.iob.iobCobCalculator.IobCobCalculatorPlugin;
import info.nightscout.androidaps.plugins.treatments.TreatmentsPlugin;
import info.nightscout.androidaps.utils.FabricPrivacy;
import info.nightscout.androidaps.utils.HardLimits;
import info.nightscout.androidaps.utils.Profiler;
import info.nightscout.androidaps.utils.resources.ResourceHelper;
import javax.inject.Provider;

@SuppressWarnings({
    "unchecked",
    "rawtypes"
})
public final class OpenAPSAMAPlugin_Factory implements Factory<OpenAPSAMAPlugin> {
  private final Provider<HasAndroidInjector> injectorProvider;

  private final Provider<AAPSLogger> aapsLoggerProvider;

  private final Provider<RxBusWrapper> rxBusProvider;

  private final Provider<ConstraintChecker> constraintCheckerProvider;

  private final Provider<ResourceHelper> resourceHelperProvider;

  private final Provider<ProfileFunction> profileFunctionProvider;

  private final Provider<Context> contextProvider;

  private final Provider<ActivePluginProvider> activePluginProvider;

  private final Provider<TreatmentsPlugin> treatmentsPluginProvider;

  private final Provider<IobCobCalculatorPlugin> iobCobCalculatorPluginProvider;

  private final Provider<HardLimits> hardLimitsProvider;

  private final Provider<Profiler> profilerProvider;

  private final Provider<FabricPrivacy> fabricPrivacyProvider;

  public OpenAPSAMAPlugin_Factory(Provider<HasAndroidInjector> injectorProvider,
      Provider<AAPSLogger> aapsLoggerProvider, Provider<RxBusWrapper> rxBusProvider,
      Provider<ConstraintChecker> constraintCheckerProvider,
      Provider<ResourceHelper> resourceHelperProvider,
      Provider<ProfileFunction> profileFunctionProvider, Provider<Context> contextProvider,
      Provider<ActivePluginProvider> activePluginProvider,
      Provider<TreatmentsPlugin> treatmentsPluginProvider,
      Provider<IobCobCalculatorPlugin> iobCobCalculatorPluginProvider,
      Provider<HardLimits> hardLimitsProvider, Provider<Profiler> profilerProvider,
      Provider<FabricPrivacy> fabricPrivacyProvider) {
    this.injectorProvider = injectorProvider;
    this.aapsLoggerProvider = aapsLoggerProvider;
    this.rxBusProvider = rxBusProvider;
    this.constraintCheckerProvider = constraintCheckerProvider;
    this.resourceHelperProvider = resourceHelperProvider;
    this.profileFunctionProvider = profileFunctionProvider;
    this.contextProvider = contextProvider;
    this.activePluginProvider = activePluginProvider;
    this.treatmentsPluginProvider = treatmentsPluginProvider;
    this.iobCobCalculatorPluginProvider = iobCobCalculatorPluginProvider;
    this.hardLimitsProvider = hardLimitsProvider;
    this.profilerProvider = profilerProvider;
    this.fabricPrivacyProvider = fabricPrivacyProvider;
  }

  @Override
  public OpenAPSAMAPlugin get() {
    return newInstance(injectorProvider.get(), aapsLoggerProvider.get(), rxBusProvider.get(), constraintCheckerProvider.get(), resourceHelperProvider.get(), profileFunctionProvider.get(), contextProvider.get(), activePluginProvider.get(), treatmentsPluginProvider.get(), iobCobCalculatorPluginProvider.get(), hardLimitsProvider.get(), profilerProvider.get(), fabricPrivacyProvider.get());
  }

  public static OpenAPSAMAPlugin_Factory create(Provider<HasAndroidInjector> injectorProvider,
      Provider<AAPSLogger> aapsLoggerProvider, Provider<RxBusWrapper> rxBusProvider,
      Provider<ConstraintChecker> constraintCheckerProvider,
      Provider<ResourceHelper> resourceHelperProvider,
      Provider<ProfileFunction> profileFunctionProvider, Provider<Context> contextProvider,
      Provider<ActivePluginProvider> activePluginProvider,
      Provider<TreatmentsPlugin> treatmentsPluginProvider,
      Provider<IobCobCalculatorPlugin> iobCobCalculatorPluginProvider,
      Provider<HardLimits> hardLimitsProvider, Provider<Profiler> profilerProvider,
      Provider<FabricPrivacy> fabricPrivacyProvider) {
    return new OpenAPSAMAPlugin_Factory(injectorProvider, aapsLoggerProvider, rxBusProvider, constraintCheckerProvider, resourceHelperProvider, profileFunctionProvider, contextProvider, activePluginProvider, treatmentsPluginProvider, iobCobCalculatorPluginProvider, hardLimitsProvider, profilerProvider, fabricPrivacyProvider);
  }

  public static OpenAPSAMAPlugin newInstance(HasAndroidInjector injector, AAPSLogger aapsLogger,
      RxBusWrapper rxBus, ConstraintChecker constraintChecker, ResourceHelper resourceHelper,
      ProfileFunction profileFunction, Context context, ActivePluginProvider activePlugin,
      TreatmentsPlugin treatmentsPlugin, IobCobCalculatorPlugin iobCobCalculatorPlugin,
      HardLimits hardLimits, Profiler profiler, FabricPrivacy fabricPrivacy) {
    return new OpenAPSAMAPlugin(injector, aapsLogger, rxBus, constraintChecker, resourceHelper, profileFunction, context, activePlugin, treatmentsPlugin, iobCobCalculatorPlugin, hardLimits, profiler, fabricPrivacy);
  }
}
