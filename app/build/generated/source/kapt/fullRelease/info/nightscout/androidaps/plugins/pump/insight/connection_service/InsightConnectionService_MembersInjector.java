// Generated by Dagger (https://dagger.dev).
package info.nightscout.androidaps.plugins.pump.insight.connection_service;

import dagger.MembersInjector;
import dagger.internal.InjectedFieldSignature;
import info.nightscout.androidaps.logging.AAPSLogger;
import info.nightscout.androidaps.utils.sharedPreferences.SP;
import javax.inject.Provider;

@SuppressWarnings({
    "unchecked",
    "rawtypes"
})
public final class InsightConnectionService_MembersInjector implements MembersInjector<InsightConnectionService> {
  private final Provider<AAPSLogger> aapsLoggerProvider;

  private final Provider<SP> spProvider;

  public InsightConnectionService_MembersInjector(Provider<AAPSLogger> aapsLoggerProvider,
      Provider<SP> spProvider) {
    this.aapsLoggerProvider = aapsLoggerProvider;
    this.spProvider = spProvider;
  }

  public static MembersInjector<InsightConnectionService> create(
      Provider<AAPSLogger> aapsLoggerProvider, Provider<SP> spProvider) {
    return new InsightConnectionService_MembersInjector(aapsLoggerProvider, spProvider);
  }

  @Override
  public void injectMembers(InsightConnectionService instance) {
    injectAapsLogger(instance, aapsLoggerProvider.get());
    injectSp(instance, spProvider.get());
  }

  @InjectedFieldSignature("info.nightscout.androidaps.plugins.pump.insight.connection_service.InsightConnectionService.aapsLogger")
  public static void injectAapsLogger(InsightConnectionService instance, AAPSLogger aapsLogger) {
    instance.aapsLogger = aapsLogger;
  }

  @InjectedFieldSignature("info.nightscout.androidaps.plugins.pump.insight.connection_service.InsightConnectionService.sp")
  public static void injectSp(InsightConnectionService instance, SP sp) {
    instance.sp = sp;
  }
}
