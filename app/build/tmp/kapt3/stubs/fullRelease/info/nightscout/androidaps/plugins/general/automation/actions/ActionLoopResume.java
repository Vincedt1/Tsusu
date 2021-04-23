package info.nightscout.androidaps.plugins.general.automation.actions;

import java.lang.System;

@kotlin.Metadata(mv = {1, 4, 1}, bv = {1, 0, 3}, k = 1, d1 = {"\u0000L\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0000\u0018\u00002\u00020\u0001B\r\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004J\u0010\u0010\u001d\u001a\u00020\u001e2\u0006\u0010\u001f\u001a\u00020 H\u0016J\b\u0010!\u001a\u00020\"H\u0016J\b\u0010#\u001a\u00020\"H\u0017J\b\u0010$\u001a\u00020%H\u0016R\u001e\u0010\u0005\u001a\u00020\u00068\u0006@\u0006X\u0087.\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u0007\u0010\b\"\u0004\b\t\u0010\nR\u001e\u0010\u000b\u001a\u00020\f8\u0006@\u0006X\u0087.\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\r\u0010\u000e\"\u0004\b\u000f\u0010\u0010R\u001e\u0010\u0011\u001a\u00020\u00128\u0006@\u0006X\u0087.\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u0013\u0010\u0014\"\u0004\b\u0015\u0010\u0016R\u001e\u0010\u0017\u001a\u00020\u00188\u0006@\u0006X\u0087.\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u0019\u0010\u001a\"\u0004\b\u001b\u0010\u001c\u00a8\u0006&"}, d2 = {"Linfo/nightscout/androidaps/plugins/general/automation/actions/ActionLoopResume;", "Linfo/nightscout/androidaps/plugins/general/automation/actions/Action;", "injector", "Ldagger/android/HasAndroidInjector;", "(Ldagger/android/HasAndroidInjector;)V", "configBuilderPlugin", "Linfo/nightscout/androidaps/plugins/configBuilder/ConfigBuilderPlugin;", "getConfigBuilderPlugin", "()Linfo/nightscout/androidaps/plugins/configBuilder/ConfigBuilderPlugin;", "setConfigBuilderPlugin", "(Linfo/nightscout/androidaps/plugins/configBuilder/ConfigBuilderPlugin;)V", "loopPlugin", "Linfo/nightscout/androidaps/plugins/aps/loop/LoopPlugin;", "getLoopPlugin", "()Linfo/nightscout/androidaps/plugins/aps/loop/LoopPlugin;", "setLoopPlugin", "(Linfo/nightscout/androidaps/plugins/aps/loop/LoopPlugin;)V", "resourceHelper", "Linfo/nightscout/androidaps/utils/resources/ResourceHelper;", "getResourceHelper", "()Linfo/nightscout/androidaps/utils/resources/ResourceHelper;", "setResourceHelper", "(Linfo/nightscout/androidaps/utils/resources/ResourceHelper;)V", "rxBus", "Linfo/nightscout/androidaps/plugins/bus/RxBusWrapper;", "getRxBus", "()Linfo/nightscout/androidaps/plugins/bus/RxBusWrapper;", "setRxBus", "(Linfo/nightscout/androidaps/plugins/bus/RxBusWrapper;)V", "doAction", "", "callback", "Linfo/nightscout/androidaps/queue/Callback;", "friendlyName", "", "icon", "shortDescription", "", "app_fullRelease"})
public final class ActionLoopResume extends info.nightscout.androidaps.plugins.general.automation.actions.Action {
    @javax.inject.Inject()
    public info.nightscout.androidaps.utils.resources.ResourceHelper resourceHelper;
    @javax.inject.Inject()
    public info.nightscout.androidaps.plugins.aps.loop.LoopPlugin loopPlugin;
    @javax.inject.Inject()
    public info.nightscout.androidaps.plugins.configBuilder.ConfigBuilderPlugin configBuilderPlugin;
    @javax.inject.Inject()
    public info.nightscout.androidaps.plugins.bus.RxBusWrapper rxBus;
    
    @org.jetbrains.annotations.NotNull()
    public final info.nightscout.androidaps.utils.resources.ResourceHelper getResourceHelper() {
        return null;
    }
    
    public final void setResourceHelper(@org.jetbrains.annotations.NotNull()
    info.nightscout.androidaps.utils.resources.ResourceHelper p0) {
    }
    
    @org.jetbrains.annotations.NotNull()
    public final info.nightscout.androidaps.plugins.aps.loop.LoopPlugin getLoopPlugin() {
        return null;
    }
    
    public final void setLoopPlugin(@org.jetbrains.annotations.NotNull()
    info.nightscout.androidaps.plugins.aps.loop.LoopPlugin p0) {
    }
    
    @org.jetbrains.annotations.NotNull()
    public final info.nightscout.androidaps.plugins.configBuilder.ConfigBuilderPlugin getConfigBuilderPlugin() {
        return null;
    }
    
    public final void setConfigBuilderPlugin(@org.jetbrains.annotations.NotNull()
    info.nightscout.androidaps.plugins.configBuilder.ConfigBuilderPlugin p0) {
    }
    
    @org.jetbrains.annotations.NotNull()
    public final info.nightscout.androidaps.plugins.bus.RxBusWrapper getRxBus() {
        return null;
    }
    
    public final void setRxBus(@org.jetbrains.annotations.NotNull()
    info.nightscout.androidaps.plugins.bus.RxBusWrapper p0) {
    }
    
    @java.lang.Override()
    public int friendlyName() {
        return 0;
    }
    
    @org.jetbrains.annotations.NotNull()
    @java.lang.Override()
    public java.lang.String shortDescription() {
        return null;
    }
    
    @androidx.annotation.DrawableRes()
    @java.lang.Override()
    public int icon() {
        return 0;
    }
    
    @java.lang.Override()
    public void doAction(@org.jetbrains.annotations.NotNull()
    info.nightscout.androidaps.queue.Callback callback) {
    }
    
    public ActionLoopResume(@org.jetbrains.annotations.NotNull()
    dagger.android.HasAndroidInjector injector) {
        super(null);
    }
}