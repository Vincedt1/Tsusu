// Generated by Dagger (https://dagger.dev).
package info.nightscout.androidaps.plugins.general.nsclient.data;

import dagger.internal.Factory;
import info.nightscout.androidaps.Config;
import info.nightscout.androidaps.logging.AAPSLogger;
import info.nightscout.androidaps.plugins.bus.RxBusWrapper;
import info.nightscout.androidaps.utils.DefaultValueHelper;
import info.nightscout.androidaps.utils.resources.ResourceHelper;
import info.nightscout.androidaps.utils.sharedPreferences.SP;
import javax.inject.Provider;

@SuppressWarnings({
    "unchecked",
    "rawtypes"
})
public final class NSSettingsStatus_Factory implements Factory<NSSettingsStatus> {
  private final Provider<AAPSLogger> aapsLoggerProvider;

  private final Provider<ResourceHelper> resourceHelperProvider;

  private final Provider<RxBusWrapper> rxBusProvider;

  private final Provider<DefaultValueHelper> defaultValueHelperProvider;

  private final Provider<SP> spProvider;

  private final Provider<Config> configProvider;

  public NSSettingsStatus_Factory(Provider<AAPSLogger> aapsLoggerProvider,
      Provider<ResourceHelper> resourceHelperProvider, Provider<RxBusWrapper> rxBusProvider,
      Provider<DefaultValueHelper> defaultValueHelperProvider, Provider<SP> spProvider,
      Provider<Config> configProvider) {
    this.aapsLoggerProvider = aapsLoggerProvider;
    this.resourceHelperProvider = resourceHelperProvider;
    this.rxBusProvider = rxBusProvider;
    this.defaultValueHelperProvider = defaultValueHelperProvider;
    this.spProvider = spProvider;
    this.configProvider = configProvider;
  }

  @Override
  public NSSettingsStatus get() {
    return newInstance(aapsLoggerProvider.get(), resourceHelperProvider.get(), rxBusProvider.get(), defaultValueHelperProvider.get(), spProvider.get(), configProvider.get());
  }

  public static NSSettingsStatus_Factory create(Provider<AAPSLogger> aapsLoggerProvider,
      Provider<ResourceHelper> resourceHelperProvider, Provider<RxBusWrapper> rxBusProvider,
      Provider<DefaultValueHelper> defaultValueHelperProvider, Provider<SP> spProvider,
      Provider<Config> configProvider) {
    return new NSSettingsStatus_Factory(aapsLoggerProvider, resourceHelperProvider, rxBusProvider, defaultValueHelperProvider, spProvider, configProvider);
  }

  public static NSSettingsStatus newInstance(AAPSLogger aapsLogger, ResourceHelper resourceHelper,
      RxBusWrapper rxBus, DefaultValueHelper defaultValueHelper, SP sp, Config config) {
    return new NSSettingsStatus(aapsLogger, resourceHelper, rxBus, defaultValueHelper, sp, config);
  }
}
