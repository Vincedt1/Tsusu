// Generated by Dagger (https://dagger.dev).
package info.nightscout.androidaps.plugins.constraints.versionChecker;

import dagger.android.HasAndroidInjector;
import dagger.internal.Factory;
import info.nightscout.androidaps.logging.AAPSLogger;
import info.nightscout.androidaps.plugins.bus.RxBusWrapper;
import info.nightscout.androidaps.utils.resources.ResourceHelper;
import info.nightscout.androidaps.utils.sharedPreferences.SP;
import javax.inject.Provider;

@SuppressWarnings({
    "unchecked",
    "rawtypes"
})
public final class VersionCheckerPlugin_Factory implements Factory<VersionCheckerPlugin> {
  private final Provider<HasAndroidInjector> injectorProvider;

  private final Provider<SP> spProvider;

  private final Provider<ResourceHelper> resourceHelperProvider;

  private final Provider<VersionCheckerUtils> versionCheckerUtilsProvider;

  private final Provider<RxBusWrapper> rxBusProvider;

  private final Provider<AAPSLogger> aapsLoggerProvider;

  public VersionCheckerPlugin_Factory(Provider<HasAndroidInjector> injectorProvider,
      Provider<SP> spProvider, Provider<ResourceHelper> resourceHelperProvider,
      Provider<VersionCheckerUtils> versionCheckerUtilsProvider,
      Provider<RxBusWrapper> rxBusProvider, Provider<AAPSLogger> aapsLoggerProvider) {
    this.injectorProvider = injectorProvider;
    this.spProvider = spProvider;
    this.resourceHelperProvider = resourceHelperProvider;
    this.versionCheckerUtilsProvider = versionCheckerUtilsProvider;
    this.rxBusProvider = rxBusProvider;
    this.aapsLoggerProvider = aapsLoggerProvider;
  }

  @Override
  public VersionCheckerPlugin get() {
    return newInstance(injectorProvider.get(), spProvider.get(), resourceHelperProvider.get(), versionCheckerUtilsProvider.get(), rxBusProvider.get(), aapsLoggerProvider.get());
  }

  public static VersionCheckerPlugin_Factory create(Provider<HasAndroidInjector> injectorProvider,
      Provider<SP> spProvider, Provider<ResourceHelper> resourceHelperProvider,
      Provider<VersionCheckerUtils> versionCheckerUtilsProvider,
      Provider<RxBusWrapper> rxBusProvider, Provider<AAPSLogger> aapsLoggerProvider) {
    return new VersionCheckerPlugin_Factory(injectorProvider, spProvider, resourceHelperProvider, versionCheckerUtilsProvider, rxBusProvider, aapsLoggerProvider);
  }

  public static VersionCheckerPlugin newInstance(HasAndroidInjector injector, SP sp,
      ResourceHelper resourceHelper, VersionCheckerUtils versionCheckerUtils, RxBusWrapper rxBus,
      AAPSLogger aapsLogger) {
    return new VersionCheckerPlugin(injector, sp, resourceHelper, versionCheckerUtils, rxBus, aapsLogger);
  }
}