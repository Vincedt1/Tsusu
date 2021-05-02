// Generated by Dagger (https://dagger.dev).
package info.nightscout.androidaps.plugins.constraints.storage;

import dagger.android.HasAndroidInjector;
import dagger.internal.Factory;
import info.nightscout.androidaps.logging.AAPSLogger;
import info.nightscout.androidaps.plugins.bus.RxBusWrapper;
import info.nightscout.androidaps.utils.resources.ResourceHelper;
import javax.inject.Provider;

@SuppressWarnings({
    "unchecked",
    "rawtypes"
})
public final class StorageConstraintPlugin_Factory implements Factory<StorageConstraintPlugin> {
  private final Provider<HasAndroidInjector> injectorProvider;

  private final Provider<AAPSLogger> aapsLoggerProvider;

  private final Provider<ResourceHelper> resourceHelperProvider;

  private final Provider<RxBusWrapper> rxBusProvider;

  public StorageConstraintPlugin_Factory(Provider<HasAndroidInjector> injectorProvider,
      Provider<AAPSLogger> aapsLoggerProvider, Provider<ResourceHelper> resourceHelperProvider,
      Provider<RxBusWrapper> rxBusProvider) {
    this.injectorProvider = injectorProvider;
    this.aapsLoggerProvider = aapsLoggerProvider;
    this.resourceHelperProvider = resourceHelperProvider;
    this.rxBusProvider = rxBusProvider;
  }

  @Override
  public StorageConstraintPlugin get() {
    return newInstance(injectorProvider.get(), aapsLoggerProvider.get(), resourceHelperProvider.get(), rxBusProvider.get());
  }

  public static StorageConstraintPlugin_Factory create(
      Provider<HasAndroidInjector> injectorProvider, Provider<AAPSLogger> aapsLoggerProvider,
      Provider<ResourceHelper> resourceHelperProvider, Provider<RxBusWrapper> rxBusProvider) {
    return new StorageConstraintPlugin_Factory(injectorProvider, aapsLoggerProvider, resourceHelperProvider, rxBusProvider);
  }

  public static StorageConstraintPlugin newInstance(HasAndroidInjector injector,
      AAPSLogger aapsLogger, ResourceHelper resourceHelper, RxBusWrapper rxBus) {
    return new StorageConstraintPlugin(injector, aapsLogger, resourceHelper, rxBus);
  }
}