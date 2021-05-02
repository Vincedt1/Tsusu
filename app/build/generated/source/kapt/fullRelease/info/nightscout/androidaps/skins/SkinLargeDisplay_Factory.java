// Generated by Dagger (https://dagger.dev).
package info.nightscout.androidaps.skins;

import dagger.internal.Factory;
import info.nightscout.androidaps.Config;
import javax.inject.Provider;

@SuppressWarnings({
    "unchecked",
    "rawtypes"
})
public final class SkinLargeDisplay_Factory implements Factory<SkinLargeDisplay> {
  private final Provider<Config> configProvider;

  public SkinLargeDisplay_Factory(Provider<Config> configProvider) {
    this.configProvider = configProvider;
  }

  @Override
  public SkinLargeDisplay get() {
    return newInstance(configProvider.get());
  }

  public static SkinLargeDisplay_Factory create(Provider<Config> configProvider) {
    return new SkinLargeDisplay_Factory(configProvider);
  }

  public static SkinLargeDisplay newInstance(Config config) {
    return new SkinLargeDisplay(config);
  }
}