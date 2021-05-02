// Generated by Dagger (https://dagger.dev).
package info.nightscout.androidaps.utils.buildHelper;

import dagger.internal.Factory;
import info.nightscout.androidaps.Config;
import javax.inject.Provider;

@SuppressWarnings({
    "unchecked",
    "rawtypes"
})
public final class BuildHelper_Factory implements Factory<BuildHelper> {
  private final Provider<Config> configProvider;

  public BuildHelper_Factory(Provider<Config> configProvider) {
    this.configProvider = configProvider;
  }

  @Override
  public BuildHelper get() {
    return newInstance(configProvider.get());
  }

  public static BuildHelper_Factory create(Provider<Config> configProvider) {
    return new BuildHelper_Factory(configProvider);
  }

  public static BuildHelper newInstance(Config config) {
    return new BuildHelper(config);
  }
}