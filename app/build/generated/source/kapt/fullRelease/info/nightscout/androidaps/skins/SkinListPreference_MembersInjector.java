// Generated by Dagger (https://dagger.dev).
package info.nightscout.androidaps.skins;

import dagger.MembersInjector;
import dagger.internal.InjectedFieldSignature;
import javax.inject.Provider;

@SuppressWarnings({
    "unchecked",
    "rawtypes"
})
public final class SkinListPreference_MembersInjector implements MembersInjector<SkinListPreference> {
  private final Provider<SkinProvider> skinProvider;

  public SkinListPreference_MembersInjector(Provider<SkinProvider> skinProvider) {
    this.skinProvider = skinProvider;
  }

  public static MembersInjector<SkinListPreference> create(Provider<SkinProvider> skinProvider) {
    return new SkinListPreference_MembersInjector(skinProvider);
  }

  @Override
  public void injectMembers(SkinListPreference instance) {
    injectSkinProvider(instance, skinProvider.get());
  }

  @InjectedFieldSignature("info.nightscout.androidaps.skins.SkinListPreference.skinProvider")
  public static void injectSkinProvider(SkinListPreference instance, SkinProvider skinProvider) {
    instance.skinProvider = skinProvider;
  }
}