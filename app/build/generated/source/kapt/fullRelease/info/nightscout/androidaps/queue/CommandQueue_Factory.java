// Generated by Dagger (https://dagger.dev).
package info.nightscout.androidaps.queue;

import android.content.Context;
import dagger.Lazy;
import dagger.android.HasAndroidInjector;
import dagger.internal.DoubleCheck;
import dagger.internal.Factory;
import info.nightscout.androidaps.interfaces.ActivePluginProvider;
import info.nightscout.androidaps.interfaces.ProfileFunction;
import info.nightscout.androidaps.logging.AAPSLogger;
import info.nightscout.androidaps.plugins.bus.RxBusWrapper;
import info.nightscout.androidaps.plugins.configBuilder.ConstraintChecker;
import info.nightscout.androidaps.utils.FabricPrivacy;
import info.nightscout.androidaps.utils.buildHelper.BuildHelper;
import info.nightscout.androidaps.utils.resources.ResourceHelper;
import info.nightscout.androidaps.utils.sharedPreferences.SP;
import javax.inject.Provider;

@SuppressWarnings({
    "unchecked",
    "rawtypes"
})
public final class CommandQueue_Factory implements Factory<CommandQueue> {
  private final Provider<HasAndroidInjector> injectorProvider;

  private final Provider<AAPSLogger> aapsLoggerProvider;

  private final Provider<RxBusWrapper> rxBusProvider;

  private final Provider<ResourceHelper> resourceHelperProvider;

  private final Provider<ConstraintChecker> constraintCheckerProvider;

  private final Provider<ProfileFunction> profileFunctionProvider;

  private final Provider<ActivePluginProvider> activePluginProvider;

  private final Provider<Context> contextProvider;

  private final Provider<SP> spProvider;

  private final Provider<BuildHelper> buildHelperProvider;

  private final Provider<FabricPrivacy> fabricPrivacyProvider;

  public CommandQueue_Factory(Provider<HasAndroidInjector> injectorProvider,
      Provider<AAPSLogger> aapsLoggerProvider, Provider<RxBusWrapper> rxBusProvider,
      Provider<ResourceHelper> resourceHelperProvider,
      Provider<ConstraintChecker> constraintCheckerProvider,
      Provider<ProfileFunction> profileFunctionProvider,
      Provider<ActivePluginProvider> activePluginProvider, Provider<Context> contextProvider,
      Provider<SP> spProvider, Provider<BuildHelper> buildHelperProvider,
      Provider<FabricPrivacy> fabricPrivacyProvider) {
    this.injectorProvider = injectorProvider;
    this.aapsLoggerProvider = aapsLoggerProvider;
    this.rxBusProvider = rxBusProvider;
    this.resourceHelperProvider = resourceHelperProvider;
    this.constraintCheckerProvider = constraintCheckerProvider;
    this.profileFunctionProvider = profileFunctionProvider;
    this.activePluginProvider = activePluginProvider;
    this.contextProvider = contextProvider;
    this.spProvider = spProvider;
    this.buildHelperProvider = buildHelperProvider;
    this.fabricPrivacyProvider = fabricPrivacyProvider;
  }

  @Override
  public CommandQueue get() {
    return newInstance(injectorProvider.get(), aapsLoggerProvider.get(), rxBusProvider.get(), resourceHelperProvider.get(), constraintCheckerProvider.get(), profileFunctionProvider.get(), DoubleCheck.lazy(activePluginProvider), contextProvider.get(), spProvider.get(), buildHelperProvider.get(), fabricPrivacyProvider.get());
  }

  public static CommandQueue_Factory create(Provider<HasAndroidInjector> injectorProvider,
      Provider<AAPSLogger> aapsLoggerProvider, Provider<RxBusWrapper> rxBusProvider,
      Provider<ResourceHelper> resourceHelperProvider,
      Provider<ConstraintChecker> constraintCheckerProvider,
      Provider<ProfileFunction> profileFunctionProvider,
      Provider<ActivePluginProvider> activePluginProvider, Provider<Context> contextProvider,
      Provider<SP> spProvider, Provider<BuildHelper> buildHelperProvider,
      Provider<FabricPrivacy> fabricPrivacyProvider) {
    return new CommandQueue_Factory(injectorProvider, aapsLoggerProvider, rxBusProvider, resourceHelperProvider, constraintCheckerProvider, profileFunctionProvider, activePluginProvider, contextProvider, spProvider, buildHelperProvider, fabricPrivacyProvider);
  }

  public static CommandQueue newInstance(HasAndroidInjector injector, AAPSLogger aapsLogger,
      RxBusWrapper rxBus, ResourceHelper resourceHelper, ConstraintChecker constraintChecker,
      ProfileFunction profileFunction, Lazy<ActivePluginProvider> activePlugin, Context context,
      SP sp, BuildHelper buildHelper, FabricPrivacy fabricPrivacy) {
    return new CommandQueue(injector, aapsLogger, rxBus, resourceHelper, constraintChecker, profileFunction, activePlugin, context, sp, buildHelper, fabricPrivacy);
  }
}
