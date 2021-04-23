// Generated by Dagger (https://dagger.dev).
package info.nightscout.androidaps.plugins.general.wear;

import dagger.Lazy;
import dagger.android.HasAndroidInjector;
import dagger.internal.DoubleCheck;
import dagger.internal.Factory;
import info.nightscout.androidaps.MainApp;
import info.nightscout.androidaps.logging.AAPSLogger;
import info.nightscout.androidaps.plugins.aps.loop.LoopPlugin;
import info.nightscout.androidaps.plugins.bus.RxBusWrapper;
import info.nightscout.androidaps.utils.FabricPrivacy;
import info.nightscout.androidaps.utils.resources.ResourceHelper;
import info.nightscout.androidaps.utils.sharedPreferences.SP;
import javax.inject.Provider;

@SuppressWarnings({
    "unchecked",
    "rawtypes"
})
public final class WearPlugin_Factory implements Factory<WearPlugin> {
  private final Provider<HasAndroidInjector> injectorProvider;

  private final Provider<AAPSLogger> aapsLoggerProvider;

  private final Provider<ResourceHelper> resourceHelperProvider;

  private final Provider<SP> spProvider;

  private final Provider<MainApp> mainAppProvider;

  private final Provider<FabricPrivacy> fabricPrivacyProvider;

  private final Provider<LoopPlugin> loopPluginProvider;

  private final Provider<RxBusWrapper> rxBusProvider;

  public WearPlugin_Factory(Provider<HasAndroidInjector> injectorProvider,
      Provider<AAPSLogger> aapsLoggerProvider, Provider<ResourceHelper> resourceHelperProvider,
      Provider<SP> spProvider, Provider<MainApp> mainAppProvider,
      Provider<FabricPrivacy> fabricPrivacyProvider, Provider<LoopPlugin> loopPluginProvider,
      Provider<RxBusWrapper> rxBusProvider) {
    this.injectorProvider = injectorProvider;
    this.aapsLoggerProvider = aapsLoggerProvider;
    this.resourceHelperProvider = resourceHelperProvider;
    this.spProvider = spProvider;
    this.mainAppProvider = mainAppProvider;
    this.fabricPrivacyProvider = fabricPrivacyProvider;
    this.loopPluginProvider = loopPluginProvider;
    this.rxBusProvider = rxBusProvider;
  }

  @Override
  public WearPlugin get() {
    return newInstance(injectorProvider.get(), aapsLoggerProvider.get(), resourceHelperProvider.get(), spProvider.get(), mainAppProvider.get(), fabricPrivacyProvider.get(), DoubleCheck.lazy(loopPluginProvider), rxBusProvider.get());
  }

  public static WearPlugin_Factory create(Provider<HasAndroidInjector> injectorProvider,
      Provider<AAPSLogger> aapsLoggerProvider, Provider<ResourceHelper> resourceHelperProvider,
      Provider<SP> spProvider, Provider<MainApp> mainAppProvider,
      Provider<FabricPrivacy> fabricPrivacyProvider, Provider<LoopPlugin> loopPluginProvider,
      Provider<RxBusWrapper> rxBusProvider) {
    return new WearPlugin_Factory(injectorProvider, aapsLoggerProvider, resourceHelperProvider, spProvider, mainAppProvider, fabricPrivacyProvider, loopPluginProvider, rxBusProvider);
  }

  public static WearPlugin newInstance(HasAndroidInjector injector, AAPSLogger aapsLogger,
      ResourceHelper resourceHelper, SP sp, MainApp mainApp, FabricPrivacy fabricPrivacy,
      Lazy<LoopPlugin> loopPlugin, RxBusWrapper rxBus) {
    return new WearPlugin(injector, aapsLogger, resourceHelper, sp, mainApp, fabricPrivacy, loopPlugin, rxBus);
  }
}
