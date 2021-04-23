// Generated by Dagger (https://dagger.dev).
package info.nightscout.androidaps.queue.commands;

import dagger.MembersInjector;
import dagger.internal.InjectedFieldSignature;
import info.nightscout.androidaps.interfaces.ActivePluginProvider;
import info.nightscout.androidaps.logging.AAPSLogger;
import info.nightscout.androidaps.utils.resources.ResourceHelper;
import javax.inject.Provider;

@SuppressWarnings({
    "unchecked",
    "rawtypes"
})
public final class CommandInsightSetTBROverNotification_MembersInjector implements MembersInjector<CommandInsightSetTBROverNotification> {
  private final Provider<AAPSLogger> aapsLoggerProvider;

  private final Provider<ResourceHelper> resourceHelperProvider;

  private final Provider<ActivePluginProvider> activePluginProvider;

  public CommandInsightSetTBROverNotification_MembersInjector(
      Provider<AAPSLogger> aapsLoggerProvider, Provider<ResourceHelper> resourceHelperProvider,
      Provider<ActivePluginProvider> activePluginProvider) {
    this.aapsLoggerProvider = aapsLoggerProvider;
    this.resourceHelperProvider = resourceHelperProvider;
    this.activePluginProvider = activePluginProvider;
  }

  public static MembersInjector<CommandInsightSetTBROverNotification> create(
      Provider<AAPSLogger> aapsLoggerProvider, Provider<ResourceHelper> resourceHelperProvider,
      Provider<ActivePluginProvider> activePluginProvider) {
    return new CommandInsightSetTBROverNotification_MembersInjector(aapsLoggerProvider, resourceHelperProvider, activePluginProvider);
  }

  @Override
  public void injectMembers(CommandInsightSetTBROverNotification instance) {
    Command_MembersInjector.injectAapsLogger(instance, aapsLoggerProvider.get());
    Command_MembersInjector.injectResourceHelper(instance, resourceHelperProvider.get());
    injectActivePlugin(instance, activePluginProvider.get());
  }

  @InjectedFieldSignature("info.nightscout.androidaps.queue.commands.CommandInsightSetTBROverNotification.activePlugin")
  public static void injectActivePlugin(CommandInsightSetTBROverNotification instance,
      ActivePluginProvider activePlugin) {
    instance.activePlugin = activePlugin;
  }
}
