package info.nightscout.androidaps.plugins.general.tidepool;

import java.lang.System;

@kotlin.Metadata(mv = {1, 4, 1}, bv = {1, 0, 3}, k = 1, d1 = {"\u0000Z\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0004\u0018\u00002\u00020\u0001B\u0005\u00a2\u0006\u0002\u0010\u0002J&\u0010#\u001a\u0004\u0018\u00010$2\u0006\u0010%\u001a\u00020&2\b\u0010\'\u001a\u0004\u0018\u00010(2\b\u0010)\u001a\u0004\u0018\u00010*H\u0016J\b\u0010+\u001a\u00020,H\u0016J\b\u0010-\u001a\u00020,H\u0016J\u001a\u0010.\u001a\u00020,2\u0006\u0010/\u001a\u00020$2\b\u0010)\u001a\u0004\u0018\u00010*H\u0016R\u000e\u0010\u0003\u001a\u00020\u0004X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u001e\u0010\u0005\u001a\u00020\u00068\u0006@\u0006X\u0087.\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u0007\u0010\b\"\u0004\b\t\u0010\nR\u001e\u0010\u000b\u001a\u00020\f8\u0006@\u0006X\u0087.\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\r\u0010\u000e\"\u0004\b\u000f\u0010\u0010R\u001e\u0010\u0011\u001a\u00020\u00128\u0006@\u0006X\u0087.\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u0013\u0010\u0014\"\u0004\b\u0015\u0010\u0016R\u001e\u0010\u0017\u001a\u00020\u00188\u0006@\u0006X\u0087.\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u0019\u0010\u001a\"\u0004\b\u001b\u0010\u001cR\u001e\u0010\u001d\u001a\u00020\u001e8\u0006@\u0006X\u0087.\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u001f\u0010 \"\u0004\b!\u0010\"\u00a8\u00060"}, d2 = {"Linfo/nightscout/androidaps/plugins/general/tidepool/TidepoolFragment;", "Ldagger/android/support/DaggerFragment;", "()V", "disposable", "Lio/reactivex/disposables/CompositeDisposable;", "fabricPrivacy", "Linfo/nightscout/androidaps/utils/FabricPrivacy;", "getFabricPrivacy", "()Linfo/nightscout/androidaps/utils/FabricPrivacy;", "setFabricPrivacy", "(Linfo/nightscout/androidaps/utils/FabricPrivacy;)V", "rxBus", "Linfo/nightscout/androidaps/plugins/bus/RxBusWrapper;", "getRxBus", "()Linfo/nightscout/androidaps/plugins/bus/RxBusWrapper;", "setRxBus", "(Linfo/nightscout/androidaps/plugins/bus/RxBusWrapper;)V", "sp", "Linfo/nightscout/androidaps/utils/sharedPreferences/SP;", "getSp", "()Linfo/nightscout/androidaps/utils/sharedPreferences/SP;", "setSp", "(Linfo/nightscout/androidaps/utils/sharedPreferences/SP;)V", "tidepoolPlugin", "Linfo/nightscout/androidaps/plugins/general/tidepool/TidepoolPlugin;", "getTidepoolPlugin", "()Linfo/nightscout/androidaps/plugins/general/tidepool/TidepoolPlugin;", "setTidepoolPlugin", "(Linfo/nightscout/androidaps/plugins/general/tidepool/TidepoolPlugin;)V", "tidepoolUploader", "Linfo/nightscout/androidaps/plugins/general/tidepool/comm/TidepoolUploader;", "getTidepoolUploader", "()Linfo/nightscout/androidaps/plugins/general/tidepool/comm/TidepoolUploader;", "setTidepoolUploader", "(Linfo/nightscout/androidaps/plugins/general/tidepool/comm/TidepoolUploader;)V", "onCreateView", "Landroid/view/View;", "inflater", "Landroid/view/LayoutInflater;", "container", "Landroid/view/ViewGroup;", "savedInstanceState", "Landroid/os/Bundle;", "onPause", "", "onResume", "onViewCreated", "view", "app_fullRelease"})
public final class TidepoolFragment extends dagger.android.support.DaggerFragment {
    @javax.inject.Inject()
    public info.nightscout.androidaps.plugins.bus.RxBusWrapper rxBus;
    @javax.inject.Inject()
    public info.nightscout.androidaps.plugins.general.tidepool.TidepoolPlugin tidepoolPlugin;
    @javax.inject.Inject()
    public info.nightscout.androidaps.plugins.general.tidepool.comm.TidepoolUploader tidepoolUploader;
    @javax.inject.Inject()
    public info.nightscout.androidaps.utils.sharedPreferences.SP sp;
    @javax.inject.Inject()
    public info.nightscout.androidaps.utils.FabricPrivacy fabricPrivacy;
    private io.reactivex.disposables.CompositeDisposable disposable;
    private java.util.HashMap _$_findViewCache;
    
    @org.jetbrains.annotations.NotNull()
    public final info.nightscout.androidaps.plugins.bus.RxBusWrapper getRxBus() {
        return null;
    }
    
    public final void setRxBus(@org.jetbrains.annotations.NotNull()
    info.nightscout.androidaps.plugins.bus.RxBusWrapper p0) {
    }
    
    @org.jetbrains.annotations.NotNull()
    public final info.nightscout.androidaps.plugins.general.tidepool.TidepoolPlugin getTidepoolPlugin() {
        return null;
    }
    
    public final void setTidepoolPlugin(@org.jetbrains.annotations.NotNull()
    info.nightscout.androidaps.plugins.general.tidepool.TidepoolPlugin p0) {
    }
    
    @org.jetbrains.annotations.NotNull()
    public final info.nightscout.androidaps.plugins.general.tidepool.comm.TidepoolUploader getTidepoolUploader() {
        return null;
    }
    
    public final void setTidepoolUploader(@org.jetbrains.annotations.NotNull()
    info.nightscout.androidaps.plugins.general.tidepool.comm.TidepoolUploader p0) {
    }
    
    @org.jetbrains.annotations.NotNull()
    public final info.nightscout.androidaps.utils.sharedPreferences.SP getSp() {
        return null;
    }
    
    public final void setSp(@org.jetbrains.annotations.NotNull()
    info.nightscout.androidaps.utils.sharedPreferences.SP p0) {
    }
    
    @org.jetbrains.annotations.NotNull()
    public final info.nightscout.androidaps.utils.FabricPrivacy getFabricPrivacy() {
        return null;
    }
    
    public final void setFabricPrivacy(@org.jetbrains.annotations.NotNull()
    info.nightscout.androidaps.utils.FabricPrivacy p0) {
    }
    
    @org.jetbrains.annotations.Nullable()
    @java.lang.Override()
    public android.view.View onCreateView(@org.jetbrains.annotations.NotNull()
    android.view.LayoutInflater inflater, @org.jetbrains.annotations.Nullable()
    android.view.ViewGroup container, @org.jetbrains.annotations.Nullable()
    android.os.Bundle savedInstanceState) {
        return null;
    }
    
    @java.lang.Override()
    public void onViewCreated(@org.jetbrains.annotations.NotNull()
    android.view.View view, @org.jetbrains.annotations.Nullable()
    android.os.Bundle savedInstanceState) {
    }
    
    @java.lang.Override()
    public synchronized void onResume() {
    }
    
    @java.lang.Override()
    public synchronized void onPause() {
    }
    
    public TidepoolFragment() {
        super();
    }
}