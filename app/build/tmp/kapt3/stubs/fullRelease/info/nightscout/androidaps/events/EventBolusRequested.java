package info.nightscout.androidaps.events;

import java.lang.System;

@kotlin.Metadata(mv = {1, 4, 1}, bv = {1, 0, 3}, k = 1, d1 = {"\u0000\u0012\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0006\n\u0002\b\u0005\u0018\u00002\u00020\u0001B\r\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004R\u001a\u0010\u0002\u001a\u00020\u0003X\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u0005\u0010\u0006\"\u0004\b\u0007\u0010\u0004\u00a8\u0006\b"}, d2 = {"Linfo/nightscout/androidaps/events/EventBolusRequested;", "Linfo/nightscout/androidaps/events/Event;", "amount", "", "(D)V", "getAmount", "()D", "setAmount", "app_fullRelease"})
public final class EventBolusRequested extends info.nightscout.androidaps.events.Event {
    private double amount;
    
    public final double getAmount() {
        return 0.0;
    }
    
    public final void setAmount(double p0) {
    }
    
    public EventBolusRequested(double amount) {
        super();
    }
}