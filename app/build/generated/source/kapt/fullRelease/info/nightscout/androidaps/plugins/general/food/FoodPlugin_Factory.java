// Generated by Dagger (https://dagger.dev).
package info.nightscout.androidaps.plugins.general.food;

import dagger.android.HasAndroidInjector;
import dagger.internal.Factory;
import info.nightscout.androidaps.logging.AAPSLogger;
import info.nightscout.androidaps.utils.resources.ResourceHelper;
import javax.inject.Provider;

@SuppressWarnings({
    "unchecked",
    "rawtypes"
})
public final class FoodPlugin_Factory implements Factory<FoodPlugin> {
  private final Provider<HasAndroidInjector> injectorProvider;

  private final Provider<AAPSLogger> aapsLoggerProvider;

  private final Provider<ResourceHelper> resourceHelperProvider;

  public FoodPlugin_Factory(Provider<HasAndroidInjector> injectorProvider,
      Provider<AAPSLogger> aapsLoggerProvider, Provider<ResourceHelper> resourceHelperProvider) {
    this.injectorProvider = injectorProvider;
    this.aapsLoggerProvider = aapsLoggerProvider;
    this.resourceHelperProvider = resourceHelperProvider;
  }

  @Override
  public FoodPlugin get() {
    return newInstance(injectorProvider.get(), aapsLoggerProvider.get(), resourceHelperProvider.get());
  }

  public static FoodPlugin_Factory create(Provider<HasAndroidInjector> injectorProvider,
      Provider<AAPSLogger> aapsLoggerProvider, Provider<ResourceHelper> resourceHelperProvider) {
    return new FoodPlugin_Factory(injectorProvider, aapsLoggerProvider, resourceHelperProvider);
  }

  public static FoodPlugin newInstance(HasAndroidInjector injector, AAPSLogger aapsLogger,
      ResourceHelper resourceHelper) {
    return new FoodPlugin(injector, aapsLogger, resourceHelper);
  }
}
