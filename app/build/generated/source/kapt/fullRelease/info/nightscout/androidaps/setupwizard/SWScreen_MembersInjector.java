// Generated by Dagger (https://dagger.dev).
package info.nightscout.androidaps.setupwizard;

import dagger.MembersInjector;
import dagger.internal.InjectedFieldSignature;
import info.nightscout.androidaps.utils.resources.ResourceHelper;
import javax.inject.Provider;

@SuppressWarnings({
    "unchecked",
    "rawtypes"
})
public final class SWScreen_MembersInjector implements MembersInjector<SWScreen> {
  private final Provider<ResourceHelper> resourceHelperProvider;

  public SWScreen_MembersInjector(Provider<ResourceHelper> resourceHelperProvider) {
    this.resourceHelperProvider = resourceHelperProvider;
  }

  public static MembersInjector<SWScreen> create(Provider<ResourceHelper> resourceHelperProvider) {
    return new SWScreen_MembersInjector(resourceHelperProvider);
  }

  @Override
  public void injectMembers(SWScreen instance) {
    injectResourceHelper(instance, resourceHelperProvider.get());
  }

  @InjectedFieldSignature("info.nightscout.androidaps.setupwizard.SWScreen.resourceHelper")
  public static void injectResourceHelper(SWScreen instance, ResourceHelper resourceHelper) {
    instance.resourceHelper = resourceHelper;
  }
}