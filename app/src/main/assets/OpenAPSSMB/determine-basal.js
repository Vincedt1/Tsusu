/*
  Determine Basal

  Released under MIT license. See the accompanying LICENSE.txt file for
  full terms and conditions

  THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
  IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
  FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
  AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
  LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
  OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
  THE SOFTWARE.
*/


var round_basal = require('../round-basal')

// Rounds value to 'digits' decimal places
function round(value, digits)
{
    if (! digits) { digits = 0; }
    var scale = Math.pow(10, digits);
    return Math.round(value * scale) / scale;
}

// we expect BG to rise or fall at the rate of BGI,
// adjusted by the rate at which BG would need to rise /
// fall to get eventualBG to target over 2 hours
function calculate_expected_delta(target_bg, eventual_bg, bgi) {
    // (hours * mins_per_hour) / 5 = how many 5 minute periods in 2h = 24
    var five_min_blocks = (2 * 60) / 5;
    var target_delta = target_bg - eventual_bg;
    return /* expectedDelta */ round(bgi + (target_delta / five_min_blocks), 1);
}


function convert_bg(value, profile)
{
    if (profile.out_units === "mmol/L")
    {
        return round(value / 18, 1).toFixed(1);
    }
    else
    {
        return Math.round(value);
    }
}

function enable_smb(
    profile,
    microBolusAllowed,
    meal_data,
    target_bg
) {
    // disable SMB when a high temptarget is set
    if (! microBolusAllowed) {
        console.error("SMB disabled (!microBolusAllowed)");
        return false;
    } else if (! profile.allowSMB_with_high_temptarget && profile.temptargetSet && target_bg > 100) {
        console.error("SMB disabled due to high temptarget of",target_bg);
        return false;
    } else if (meal_data.bwFound === true && profile.A52_risk_enable === false) {
        console.error("SMB disabled due to Bolus Wizard activity in the last 6 hours.");
        return false;
    }

    // enable SMB/UAM if always-on (unless previously disabled for high temptarget)
    if (profile.enableSMB_always === true) {
        if (meal_data.bwFound) {
            console.error("Warning: SMB enabled within 6h of using Bolus Wizard: be sure to easy bolus 30s before using Bolus Wizard");
        } else {
            console.error("SMB enabled due to enableSMB_always");
        }
        return true;
    }

    // enable SMB/UAM (if enabled in preferences) while we have COB
    if (profile.enableSMB_with_COB === true && meal_data.mealCOB) {
        if (meal_data.bwCarbs) {
            console.error("Warning: SMB enabled with Bolus Wizard carbs: be sure to easy bolus 30s before using Bolus Wizard");
        } else {
            console.error("SMB enabled for COB of",meal_data.mealCOB);
        }
        return true;
    }

    // enable SMB/UAM (if enabled in preferences) for a full 6 hours after any carb entry
    // (6 hours is defined in carbWindow in lib/meal/total.js)
    if (profile.enableSMB_after_carbs === true && meal_data.carbs ) {
        if (meal_data.bwCarbs) {
            console.error("Warning: SMB enabled with Bolus Wizard carbs: be sure to easy bolus 30s before using Bolus Wizard");
        } else {
            console.error("SMB enabled for 6h after carb entry");
        }
        return true;
    }

    // enable SMB/UAM (if enabled in preferences) if a low temptarget is set
    if (profile.enableSMB_with_temptarget === true && (profile.temptargetSet && target_bg < 100)) {
        if (meal_data.bwFound) {
            console.error("Warning: SMB enabled within 6h of using Bolus Wizard: be sure to easy bolus 30s before using Bolus Wizard");
        } else {
            console.error("SMB enabled for temptarget of",convert_bg(target_bg, profile));
        }
        return true;
    }

    console.error("SMB disabled (no enableSMB preferences active or no condition satisfied)");
    return false;
}

// auto ISF === START
function autoISF(sens, target_bg, profile, glucose_status, meal_data, autosens_data, sensitivityRatio)
{   // #### mod 7e: added switch fr autoISF ON/OFF
    if ( !profile.use_autoisf ) {
        console.error("autoISF disabled in Preferences");
        return sens;
    }
    // #### mod 7:  dynamic ISF strengthening based on duration and width of 5% BG band
    // #### mod 7b: misuse autosens_min to get the scale factor
    // #### mod 7d: use standalone variables for autopISF
    var dura05 = glucose_status.autoISF_duration;           // mod 7d
    var avg05  = glucose_status.autoISF_average;            // mod 7d
    //r weightISF = (1 - profile.autosens_min)*2;           // mod 7b: use 0.6 to get factor 0.8; use 1 to get factor 0, i.e. OFF
    var weightISF = profile.autoisf_hourlychange;           // mod 7d: specify factor directly; use factor 0 to shut autoISF OFF
    if (meal_data.mealCOB==0 && dura05>=0) {
        if (avg05 > target_bg) {
            // # fight the resistance at high levels
            var maxISFReduction = profile.autoisf_max;      // mod 7d
            var dura05_weight = dura05 / 10;
            var avg05_weight = weightISF / target_bg;       // mod gz7b: provide access from AAPS
            var levelISF = 1 + dura05_weight*avg05_weight*(avg05-target_bg);
            var liftISF = Math.max(Math.min(maxISFReduction, levelISF), sensitivityRatio);  // corrected logic on 30.Jan.2021
            /*console.error("autoISF reports", sens, "did not do it for", dura05,"m; go more aggressive by", round(levelISF,2));
            if (maxISFReduction < levelISF) {
                console.error("autoISF reduction", round(levelISF,2), "limited by autoisf_max", maxISFReduction);
            }*/
            sens = round(sens / liftISF, 1);
        } else {
            console.error("autoISF by-passed; avg. glucose", avg05, "below target", target_bg);
        }
    } /*else if (meal_data.mealCOB>0) {
        console.error("autoISF by-passed; mealCOB of "+round(meal_data.mealCOB,1));
    } */else {
        console.error("autoISF by-passed; BG is only "+dura05+"m at level "+avg05);
    }
    return sens;
}
// auto ISF === END

var determine_basal = function determine_basal(glucose_status, currenttemp, iob_data, profile, autosens_data, meal_data, tempBasalFunctions, microBolusAllowed, reservoir_data, currentTime, isSaveCgmSource) {
    var rT = {}; //short for requestedTemp

    var deliverAt = new Date();
    if (currentTime) {
        deliverAt = new Date(currentTime);
    }

    if (typeof profile === 'undefined' || typeof profile.current_basal === 'undefined') {
        rT.error ='Error: could not get current basal rate';
        return rT;
    }
    var profile_current_basal = round_basal(profile.current_basal, profile);
    var basal = profile_current_basal;

    var systemTime = new Date();
    if (currentTime) {
        systemTime = currentTime;
    }
    var bgTime = new Date(glucose_status.date);
    var minAgo = round( (systemTime - bgTime) / 60 / 1000 ,1);

    var bg = glucose_status.glucose;
    var noise = glucose_status.noise;
    // 38 is an xDrip error state that usually indicates sensor failure
    // all other BG values between 11 and 37 mg/dL reflect non-error-code BG values, so we should zero temp for those
    if (bg <= 10 || bg === 38 || noise >= 3) {  //Dexcom is in ??? mode or calibrating, or xDrip reports high noise
        rT.reason = "CGM is calibrating, in ??? state, or noise is high";
    }
    if (minAgo > 12 || minAgo < -5) { // Dexcom data is too old, or way in the future
        rT.reason = "If current system time "+systemTime+" is correct, then BG data is too old. The last BG data was read "+minAgo+"m ago at "+bgTime;
    // if BG is too old/noisy, or is changing less than 1 mg/dL/5m for 45m, cancel any high temps and shorten any long zero temps
    //cherry pick from oref upstream dev cb8e94990301277fb1016c778b4e9efa55a6edbc
    } else if ( bg > 60 && glucose_status.delta == 0 && glucose_status.short_avgdelta > -1 && glucose_status.short_avgdelta < 1 && glucose_status.long_avgdelta > -1 && glucose_status.long_avgdelta < 1 && !isSaveCgmSource) {
        if ( glucose_status.last_cal && glucose_status.last_cal < 3 ) {
            rT.reason = "CGM was just calibrated";
        }/* else {
            rT.reason = "Error: CGM data is unchanged for the past ~45m";
        }*/
    }
    //cherry pick from oref upstream dev cb8e94990301277fb1016c778b4e9efa55a6edbc
    if (bg <= 10 || bg === 38 || noise >= 3 || minAgo > 12 || minAgo < -5 || ( bg > 60 && glucose_status.delta == 0 && glucose_status.short_avgdelta > -1 && glucose_status.short_avgdelta < 1 && glucose_status.long_avgdelta > -1 && glucose_status.long_avgdelta < 1 ) && !isSaveCgmSource ) {
        if (currenttemp.rate > basal) { // high temp is running
            rT.reason += ". Replacing high temp basal of "+currenttemp.rate+" with neutral temp of "+basal;
            rT.deliverAt = deliverAt;
            rT.temp = 'absolute';
            rT.duration = 30;
            rT.rate = basal;
            return rT;
            //return tempBasalFunctions.setTempBasal(basal, 30, profile, rT, currenttemp);
        } else if ( currenttemp.rate === 0 && currenttemp.duration > 30 ) { //shorten long zero temps to 30m
            rT.reason += ". Shortening " + currenttemp.duration + "m long zero temp to 30m. ";
            rT.deliverAt = deliverAt;
            rT.temp = 'absolute';
            rT.duration = 30;
            rT.rate = 0;
            return rT;
            //return tempBasalFunctions.setTempBasal(0, 30, profile, rT, currenttemp);
        } else { //do nothing.
            rT.reason += ". Temp " + currenttemp.rate + " <= current basal " + basal + "U/hr; doing nothing. ";
            return rT;
        }
    }

    var max_iob = profile.max_iob; // maximum amount of non-bolus IOB OpenAPS will ever deliver

    // if min and max are set, then set target to their average
    var target_bg;
    var min_bg;
    var max_bg;
    if (typeof profile.min_bg !== 'undefined') {
            min_bg = profile.min_bg;
    }
    if (typeof profile.max_bg !== 'undefined') {
            max_bg = profile.max_bg;
    }
    if (typeof profile.min_bg !== 'undefined' && typeof profile.max_bg !== 'undefined') {
        target_bg = (profile.min_bg + profile.max_bg) / 2;
    } else {
        rT.error ='Error: could not determine target_bg. ';
        return rT;
    }

    var sensitivityRatio;
    var high_temptarget_raises_sensitivity = profile.exercise_mode || profile.high_temptarget_raises_sensitivity;
    var normalTarget = 100; // evaluate high/low temptarget against 100, not scheduled target (which might change)
    if ( profile.half_basal_exercise_target ) {
        var halfBasalTarget = profile.half_basal_exercise_target;
    } else {
        halfBasalTarget = 160; // when temptarget is 160 mg/dL, run 50% basal (120 = 75%; 140 = 60%)
        // 80 mg/dL with low_temptarget_lowers_sensitivity would give 1.5x basal, but is limited to autosens_max (1.2x by default)
    }
    if ( high_temptarget_raises_sensitivity && profile.temptargetSet && target_bg > normalTarget
        || profile.low_temptarget_lowers_sensitivity && profile.temptargetSet && target_bg < normalTarget ) {
        // w/ target 100, temp target 110 = .89, 120 = 0.8, 140 = 0.67, 160 = .57, and 200 = .44
        // e.g.: Sensitivity ratio set to 0.8 based on temp target of 120; Adjusting basal from 1.65 to 1.35; ISF from 58.9 to 73.6
        //sensitivityRatio = 2/(2+(target_bg-normalTarget)/40);
        var c = halfBasalTarget - normalTarget;
        sensitivityRatio = c/(c+target_bg-normalTarget);
        // limit sensitivityRatio to profile.autosens_max (1.2x by default)
        sensitivityRatio = Math.min(sensitivityRatio, profile.autosens_max);
        sensitivityRatio = round(sensitivityRatio,2);
        console.log("Sensitivity ratio set to "+sensitivityRatio+" based on temp target of "+target_bg+"; ");
    } else if (typeof autosens_data !== 'undefined' && autosens_data) {
        sensitivityRatio = autosens_data.ratio;
        console.log("Autosens ratio: "+sensitivityRatio+"; ");
    }
    if (sensitivityRatio) {
        basal = profile.current_basal * sensitivityRatio;
        basal = round_basal(basal, profile);
        if (basal !== profile_current_basal) {
            console.log("Adjusting basal from "+profile_current_basal+" to "+basal+"; ");
        } else {
            console.log("Basal unchanged: "+basal+"; ");
        }
    }

    // adjust min, max, and target BG for sensitivity, such that 50% increase in ISF raises target from 100 to 120
    //MP: Below code block was copy/pasted below ISF scaling code to adjust the target for w-zero mode
/*    if (profile.temptargetSet) {
        //console.log("Temp Target set, not adjusting with autosens; ");
    } else if (typeof autosens_data !== 'undefined' && autosens_data) {
        if ( profile.sensitivity_raises_target && autosens_data.ratio < 1 || profile.resistance_lowers_target && autosens_data.ratio > 1 ) {
            // with a target of 100, default 0.7-1.2 autosens min/max range would allow a 93-117 target range
            min_bg = round((min_bg - 60) / autosens_data.ratio) + 60;
            max_bg = round((max_bg - 60) / autosens_data.ratio) + 60;
            var new_target_bg = round((target_bg - 60) / autosens_data.ratio) + 60;
            // don't allow target_bg below 80
            new_target_bg = Math.max(80, new_target_bg);
            if (target_bg === new_target_bg) {
                console.log("target_bg unchanged: "+new_target_bg+"; ");
            } else {
                console.log("target_bg from "+target_bg+" to "+new_target_bg+"; ");
            }
            target_bg = new_target_bg;
        }
    }*/

    if (typeof iob_data === 'undefined' ) {
        rT.error ='Error: iob_data undefined. ';
        return rT;
    }

    var iobArray = iob_data;
    if (typeof(iob_data.length) && iob_data.length > 1) {
        iob_data = iobArray[0];
        //console.error(JSON.stringify(iob_data[0]));
    }

    if (typeof iob_data.activity === 'undefined' || typeof iob_data.iob === 'undefined' ) {
        rT.error ='Error: iob_data missing some property. ';
        return rT;
    }

    var tick;

    if (glucose_status.delta > -0.5) {
        tick = "+" + round(glucose_status.delta,0);
    } else {
        tick = round(glucose_status.delta,0);
    }
    //var minDelta = Math.min(glucose_status.delta, glucose_status.short_avgdelta, glucose_status.long_avgdelta);
    var minDelta = Math.min(glucose_status.delta, glucose_status.short_avgdelta);
    var minAvgDelta = Math.min(glucose_status.short_avgdelta, glucose_status.long_avgdelta);
    var maxDelta = Math.max(glucose_status.delta, glucose_status.short_avgdelta, glucose_status.long_avgdelta);

    var tae_start = profile.tae_start;
    var tae_end = profile.tae_end;
    var profile_sens = round(profile.sens,1)
    var sens = profile.sens;
    if (typeof autosens_data !== 'undefined' && autosens_data) {
        sens = profile.sens / sensitivityRatio;
        sens = round(sens, 1);
        if (sens !== profile_sens) {
            console.log("ISF from "+profile_sens+" to "+sens);
        } else {
            console.log("ISF unchanged: "+sens);
        }
        //console.log(" (autosens ratio "+sensitivityRatio+")");
    }
    console.error("CR:",profile.carb_ratio);

var HypoPredBG = round( bg - (iob_data.iob * sens) ) + round( 60 / 5 * ( minDelta - round(( -iob_data.activity * sens * 5 ), 2)));
var HyperPredBG = round( bg - (iob_data.iob * sens) ) + round( 60 / 5 * ( minDelta - round(( -iob_data.activity * sens * 5 ), 2)));

//############################## MP
//### ISF SCALING CODE START ### MP
//############################## MP
var now = new Date().getHours();
//var scale_ISF_ID; //MP: identifier variable. Each of the different ISF scaling codes below is assigned a number to simplify coupling other blocks of code to the type of ISF scaling applied
//var scale_min = profile.scale_min/100;
//var scale_max = profile.scale_max/100;
var boluscap = profile.UAM_boluscap * profile.percentage/100; //MP: Adjust boluscap with profile percentage;
//MP Allow SMBs that are 70% of boluscap or more to absorb before delivering another large SMB.
if (round(( new Date(systemTime).getTime() - iob_data.lastBolusTime ) / 60000,1) <= 9 && meal_data.lastBolus >= 0.70 * boluscap && profile.enable_w_zero) {
    boluscap = profile.UAM_boluscap * profile.percentage/100 - meal_data.lastBolus;
}

//------------------------------- MP
// TSUNAMI ACTIVITY ENGINE START  MP
//------------------------------- MP

var activity_controller = false;
var extremum_bg_broad = glucose_status.broadfit_a * Math.pow(glucose_status.broad_extremum, 2) + glucose_status.broadfit_b * glucose_status.broad_extremum + glucose_status.broadfit_c;
var hypodetect = false;
//MP BG and delta used by tae will switch between sensor data and smoothed data, depending on which is lower - for added safety
//var tae_bg = Math.min(glucose_status.bg_supersmooth_now, glucose_status.glucose);
var tae_bg = glucose_status.bg_supersmooth_now;
var tae_delta = Math.min(glucose_status.delta_supersmooth_now, glucose_status.delta);
var insulinReqPCT = profile.insulinreqPCT/100; //MP Moved up in the code since tsunami 0.8, to modify it

if (glucose_status.broad_extremum >= -29 && glucose_status.broad_extremum <= 0 && (extremum_bg_broad <= 85 || bg <= 85)  && glucose_status.glucose < target_bg * 1.1) { //MP: Checks if an extremum within the last 25-29 min was a minimum with a value of less than 85 mg/dl. If current bg is less than 1.1 * target_bg, then recognise a current rise as hypo correction and don't scale ISF.
    hypodetect = true;
}

if (profile.enable_tae && hypodetect == false && now >= tae_start && now <= tae_end && tae_delta >= 0 && tae_bg >= target_bg && iob_data.iob > 0.1 && meal_data.mealCOB = 0) {
//if (profile.enable_tae) { //MP debugging
    activity_controller = true; //MP Use UAM tsunami for SMB sizing

    var boluscap = profile.UAM_boluscap * profile.percentage/100; //MP: Adjust boluscap with profile percentage;

    var current_activity = glucose_status.sensorlagactivity; //MP Current delta value, due to sensor lag, is more likely to represent situation from about 10 minutes ago - therefore activity from 10 minutes ago is used for activity calculations.
    var futureactivity = glucose_status.futureactivity;

    //MP Allow SMBs that are 70% of boluscap or more to absorb before delivering another large SMB.
    if (round(( new Date(systemTime).getTime() - iob_data.lastBolusTime ) / 60000,1) <= 9 && meal_data.lastBolus >= 0.70 * boluscap) {
        boluscap = Math.max(boluscap - meal_data.lastBolus, 0);
    }
    var mealscore = Math.min(1, Math.max(glucose_status.mealscore_smooth, 0));

    var pure_delta = round(tae_delta + Math.max(current_activity * profile_sens, 0), 1); //MP 5-minute-delta value if insulin activity was zero; Divide sensor data by 5 to get per-minute-delta
    var activity_to_zero = pure_delta / profile_sens; //MP Insulin activity at which delta should be zero (if delta remains unchanged)

    if (tae_delta <= 4.1 && current_activity > 0) { //MP Adjust activity target to 75% of current activity if glucose is near constant / delta is low.
        //var activity_missing_future = (glucose_status.historicactivity - glucose_status.futureactivity)/5; //MP Use activity from 20 minutes ago as target activity in the future; Divide by 5 to get per-minute activity
        var activity_missing_future = round((current_activity * 0.75 - Math.max(futureactivity, 0))/5, 4); //MP Use 75% of current activity as target activity in the future; Divide by 5 to get per-minute activity
        mealscore = Math.min(1, Math.max((tae_bg - target_bg)/100, 0)); //MP redefines mealscore as it otherwise would be near-zero (low deltas). The higher the bg, the larger mealscore
    } else if (current_activity > 0) { //Escalate activity at medium to high delta
        var activity_missing_future = round((activity_to_zero - Math.max(futureactivity, 0))/5, 4); //MP Calculate required activity to end a rise in t minutes; Divide by 5 to get per-minute activity
    } else {
        activity_controller = false; //MP Disable tsunami handling if current activity is negative and let oref1 take over.
        console.log("------------------------------");
        console.log("TSUNAMI STATUS");
        console.log("------------------------------");
        console.log("TAE bypassed - insulin activity is negative or 0.");
        console.log("------------------------------");
    }

    if (activity_controller) {
        var td = profile.dia * 60; //MP Duration of insulin activity, in min
        var tp = profile.peaktime; //MP Insulin peak time as stated in InsulinOrefFreePeakPlugin. Doesn't work with insulin presets. Should be same value as used for futureactivity calculation (see glucoseStatus.java)
        var t = tp; //MP time at which activity of insulin should outcompete current delta
        var tau = tp * (1 - tp / td) / (1 - 2 * tp / td);
        var a = 2 * tau / td;
        var S = 1 / (1 - a + (1 + a) * Math.exp(-td / tau));

        var tsunami_insreq = round(activity_missing_future / ((S / Math.pow(tau, 2.0)) * t * (1 - t / td) * Math.exp(-t / tau)), 2);

        if (tsunami_insreq + iob_data.iob < (tae_bg - target_bg)/profile_sens) { //MP If the calculated insulin req, together with the remaining IOB, is not enough to theoretically bring bg down to target_bg, disable tsunami and used oref1 instead. A possible cause of this situation is a phase where delta is positive but small, triggering low delta mode, while current insulin activity is too small to effectively halt
            activity_controller = false;
            console.log("------------------------------");
            console.log("TSUNAMI STATUS");
            console.log("------------------------------");
            console.log("TAE bypassed - incompatible insulin & glucose status. Let oref1 take over for now.");
            console.log("------------------------------");
        }
        insulinReqPCT = round(insulinReqPCT * mealscore, 3);
        //MP BG SCORE SAFETY PENALTY start
        var bgscore_upper_threshold = 140; //MP BG above which no penalty will be given
        var bgscore_lower_threshold = 80; //MP BG below which tae will not deliver SMBs
        var bgscore = round(Math.min((tae_bg - bgscore_lower_threshold)/(bgscore_upper_threshold-bgscore_lower_threshold), 1), 3); //MP Penalty at low or near-target bg values. Modifies boluscap.
        boluscap = round(boluscap * bgscore, 2);
        //MP BG SCORE SAFETY PENALTY end
/*
        var ncoeff_a = glucose_status.narrowsmoothedfit_a;
        var ncoeff_b = glucose_status.narrowsmoothedfit_b;
        var ncoeff_c = glucose_status.narrowsmoothedfit_c;
        var nR2 = glucose_status.nsR2;
*/
    }
    if (activity_controller) {
        console.log("------------------------------");
        console.log("TSUNAMI STATUS");
        console.log("------------------------------");
        console.log("act. lag: "+glucose_status.sensorlagactivity);
        console.log("act. now: "+current_activity+" ("+glucose_status.currentactivity+")");
        console.log("act. future: "+glucose_status.futureactivity);
        console.log("miss./act. future: "+activity_missing_future);
        console.log("-------------");
        console.log("delta: "+glucose_status.delta);
        console.log("smoothed delta: "+glucose_status.delta_supersmooth_now);
        console.log("used delta: "+tae_delta);
        console.log("pure delta: "+pure_delta);
        console.log("used bg: "+tae_bg);
        console.log("-------------");
        console.log("mealscore_live: "+round(mealscore,3));
        console.log("bgscore_live: "+bgscore);
        console.log("insulinreqPCT_live: "+insulinReqPCT);
        console.log("boluscap_live: "+boluscap);
        console.log("tsunami_insreq: "+tsunami_insreq);
        if (tae_delta <= 4.1 && current_activity > 0) {
            console.log("Mode: Near-constant activity.");
        } else if (current_activity > 0) {
            console.log("Mode: Building up activity.");        
        }
        console.log("------------------------------");
    }
} else {
        console.log("------------------------------");
        console.log("TSUNAMI STATUS");
        console.log("------------------------------");
        console.log("TAE bypassed - reasons:");
        if (!profile.enable_tae)                {console.log("TAE disabled in settings.");}
        if (hypodetect == true)                 {console.log("Recent low glucose event.");}
        if (now < tae_start || now > tae_end)   {console.log("Outside active hours.");}
        if (tae_delta < 0)                      {console.log("Negative delta reported. ("+tae_delta+")");}
        if (tae_bg < target_bg)                 {console.log("Glucose is below target.");}
        if (iob_data.iob <= 0.1)                {console.log("IOB is below minimum of 0.1 U.");}
        if (meal_data.mealCOB > 0)              {console.log("COB is more than 0 g.");}
        console.log("------------------------------");
}

if (profile.use_autoisf && now < tae_start || now > tae_end) { //MP Enables autoISF outside of TAE hours
    sens = autoISF(sens, target_bg, profile, glucose_status, meal_data, autosens_data, sensitivityRatio); //autoISF
}

//----------------------------- MP
// TSUNAMI ACTIVITY ENGINE END  MP
//----------------------------- MP

//#################### MP
//### W-ZERO START ### MP
//#################### MP
/*
Desired features:

- bg/target_bg determines SMB cap size, but doesn't increase aggressiveness. This also means that at smaller bgs below or slightly above target, bolus sizes should be small
- if a rise has been going for a while, even if bg is only slightly above target, allow for larger SMB sizes, depending on how sure we are about a rise. Two positive deltas mean nothing, three mean more, four mean even more and at five, it is definitely a rise.
- W2 modifier could be used as a break to realise point 2: The surer we are about a rise, the smaller it is. This means the code would dynamically transition from W2 aggressiveness towards W1 aggressiveness
- scale_min is another modifier: If we're sure about a rise, opt it in to increase aggressiveness; but scale_min should be turned off if a rise isn't fast enough, or if curve is decelerating
- Explore if curve fittings and R^2 values could be used to identify rises and model/data quality, to then decide if we can go aggressive or not
*/
//todo: Add dynamic hyper target adjustment; Make it so that target decreases dynamically if curve is accelerating (or other conditions are true, explore options)
//W2 modifier to dynamically increase aggressiveness if necessary
// Changes:
// Removed bg/target_bg scale_50 modifier

// if a = negative & b = negative: Negative acceleration (glucose is dropping, maximum lies in the past)
// if a = negative & b = positive: Deceleration (glucose is rising and likely to peak soon - extremum can be used to predict maximum in the future)
// if a = positive & b = positive: Accelerating (glucose is rising at increasing rates, minimum lies in the past) -- Useful for meal detection
// if a = positive & b = negative: Deceleration (glucose is dropping and likely to reach a minimum soon. Minimum is in the future)



/*if (glucose_status.nR2 > glucose_status.nsR2 && glucose_status.nR2 > 0.95 && profile.enable_w_zero) { //MP: If narrow sensor data model fits better than narrow smoothed model or if sensor model fits extremely well (more than 99% accuracy), then use sensor model;
    ncoeff_a = glucose_status.narrowfit_a;
    ncoeff_b = glucose_status.narrowfit_b;
    ncoeff_c = glucose_status.narrowfit_c;
    nR2 = glucose_status.nR2;
    mealscore = Math.min(1, glucose_status.mealscore_raw);
} else*//* if (profile.enable_w_zero) {
    var ncoeff_a;
    var ncoeff_b;
    var ncoeff_c;
    var nR2;
    var insulinReqPCT = profile.insulinreqPCT/100; //MP Moved up in the code since tsunami 0.8, to modify it
    var mealscore;
    //MP: Use smoothed bg model otherwise
    ncoeff_a = glucose_status.narrowsmoothedfit_a;
    ncoeff_b = glucose_status.narrowsmoothedfit_b;
    ncoeff_c = glucose_status.narrowsmoothedfit_c;
    nR2 = glucose_status.nsR2;
    mealscore = Math.min(1, glucose_status.mealscore_smooth);
    var hypodetect = false;
}

var scaleISF_bg = glucose_status.glucose;
var scaleISF_delta = glucose_status.delta;

if (profile.enable_datasmoothing && profile.enable_w_zero && !glucose_status.insufficientdata) { //MP use smoothed glucose data for w-zero if data smoothing is enabled
    //console.log("Using smoothed bg ("+glucose_status.bg_supersmooth_now+") and delta ("+glucose_status.delta_supersmooth_now+").");
    scaleISF_bg = glucose_status.bg_supersmooth_now;
    scaleISF_delta = glucose_status.delta_supersmooth_now;
}*/


//MP penalties
//MP dynamic calculation of scale_min for W-zero
//scale_min = 1 - ((glucose_status.narrowfit_b/10)*glucose_status.nR2) //MP 1 - (slope of linear term / 10) * coefficient of determination for narrow fit

//TODO: CONSIDER: hypo detection likely requires narrowfit first, and broadfit later to find true peaks, where broadfit confirms
/*
var extremum_bg_broad = glucose_status.broadfit_a * Math.pow(glucose_status.broad_extremum, 2) + glucose_status.broadfit_b * glucose_status.broad_extremum + glucose_status.broadfit_c;

if (glucose_status.broad_extremum >= -29 && glucose_status.broad_extremum <= 0 && (extremum_bg_broad <= 85 || bg <= 85)  && glucose_status.glucose < target_bg * 1.1) { //MP: Checks if an extremum within the last 25-29 min was a minimum with a value of less than 85 mg/dl. If current bg is less than 1.1 * target_bg, then recognise a current rise as hypo correction and don't scale ISF.
    hypodetect = true;
}
*/
//TODO: CONSIDER: If curve is accelerating when BG is above target, increase SMB delivery percentage up to a cap

//scale_ISF_ID codes:
// 0.1 = Curve acceleration
// 0.2 = linearity
// 0.3 = Curve deceleration

//MP sens equations for w-zero

//if (profile.enable_w_zero && hypodetect == false && now >= tae_start && now <= tae_end && glucose_status.delta >= 0 && bg >= 80 && iob_data.iob > 0.1) {//MP: W-zero mode must be turned on in the user settings
    //scale_min = Math.pow(target_bg/bg, ncoeff_b)/glucose_status.nR2;
    //scale_min = 1 - ((ncoeff_b/10)*nR2) //MP: dynamic adjustment of scale_min based on an empirical formula using steepness of linear term and its coefficient of determination.
    /*scale_min = 1 - (1 - scale_min) * mealscore; //TODO: CONSIDER: Is using raw mealscore good? Can lead to very large SMBs; Consider dividing mealscore by a threshold value, so scale_min is at 100% at slopes larger than 1.0
    if (scale_min < profile.scale_min/100) { //MP: User setting of scale_min defines the minimum value
        scale_min = profile.scale_min/100;
    } else if (scale_min > 1) {
        scale_min = 1;
    }*/
    /*
    if (ncoeff_a <= -0.015) { //MP: In deceleration mode, scale_min is not used and scale_max is raised for a weaker response.
        //MP Do not use scale_min if curve is decelerating
        scale_ISF_ID = 0.3;
        scale_max = scale_max * 1.2;
        sens = profile.sens - (((profile.sens - (profile.sens * scale_max)) * scaleISF_delta) / (profile.scale_50 + scaleISF_delta));
    } else if (ncoeff_a >= 0.03 && mealscore >= 0.2) { //MP: In acceleration mode, scale_min is used
        scale_ISF_ID = 0.1;
        insulinReqPCT = insulinReqPCT * mealscore; //MP: adjust insulinReqPCT by likelihood that a rise is due to a meal to deliver less insulin in case
        scale_min = Math.max(profile.scale_min/100, 1 - (1 - (profile.scale_min/100)) * mealscore); //MP: Dynamically adjust scale_min with meal score for a slow ramp up of aggressiveness
        sens = scale_min * profile.sens - (((scale_min * profile.sens - (profile.sens * scale_max)) * scaleISF_delta) / (profile.scale_50 + scaleISF_delta)); //MP: Fixed model based on Michaelis-Menten kinetic, self limiting at higher values. v0.3: Curve steepness increases with increasing bg.
    } else if (mealscore >= 0.2) {
        scale_ISF_ID = 0.2;
        scale_min = Math.max(profile.scale_min/100, 1 - ((1 - (profile.scale_min/100)) * mealscore) * Math.min(ncoeff_b / 1.6 , 1)); //MP: Dynamically adjust scale_min with meal score & slope for a slow and delta controlled ramp up of aggressiveness, reaching full aggressiveness only at higher deltas
        insulinReqPCT = insulinReqPCT * mealscore; //MP: adjust insulinReqPCT by likelihood that a rise is due to a meal to deliver less insulin in case
        sens = profile.sens - (((profile.sens - (profile.sens * scale_max)) * scaleISF_delta) / (profile.scale_50 + scaleISF_delta));
    } else {
            if (bg >= 180 && glucose_status.delta >= 0 && insulinReq >= round( 3 * profile.current_basal * profile.maxUAMSMBBasalMinutes / 60 ,1) && boluscap > round(profile.current_basal * profile.maxUAMSMBBasalMinutes / 60 ,2) && now >= tae_start && now <= tae_end) {
                    scale_ISF_ID = 2.1; //MP: Allows use of UAM_boluscap (weakened or original, as defined below) if an array of safety conditions is met
                    sens = autoISF(sens, target_bg, profile, glucose_status, meal_data, autosens_data, sensitivityRatio); //autoISF
            } else {
                    sens = autoISF(sens, target_bg, profile, glucose_status, meal_data, autosens_data, sensitivityRatio); //autoISF
                    scale_ISF_ID = 2;
            }
    }
} else if (profile.enable_w_zero && now >= tae_start && now <= tae_end && bg > target_bg) {
            if (bg >= 180 && glucose_status.delta >= 0 && insulinReq >= round( 3 * profile.current_basal * profile.maxUAMSMBBasalMinutes / 60 ,1) && boluscap > round(profile.current_basal * profile.maxUAMSMBBasalMinutes / 60 ,2) && now >= tae_start && now <= tae_end) {
                    scale_ISF_ID = 2.1; //MP: Allows use of UAM_boluscap (weakened or original, as defined below) if an array of safety conditions is met
                    sens = autoISF(sens, target_bg, profile, glucose_status, meal_data, autosens_data, sensitivityRatio); //autoISF
            } else {
                    sens = autoISF(sens, target_bg, profile, glucose_status, meal_data, autosens_data, sensitivityRatio); //autoISF
                    scale_ISF_ID = 2;
            }
} else if (profile.enable_w_zero && bg > target_bg*1.4) {
              if (bg >= 180 && glucose_status.delta >= 0 && insulinReq >= round( 3 * profile.current_basal * profile.maxUAMSMBBasalMinutes / 60 ,1) && boluscap > round(profile.current_basal * profile.maxUAMSMBBasalMinutes / 60 ,2) && now >= tae_start && now <= tae_end) {
                      scale_ISF_ID = 2.1; //MP: Allows use of UAM_boluscap (weakened or original, as defined below) if an array of safety conditions is met
                      sens = autoISF(sens, target_bg, profile, glucose_status, meal_data, autosens_data, sensitivityRatio); //autoISF
              } else {
                      sens = autoISF(sens, target_bg, profile, glucose_status, meal_data, autosens_data, sensitivityRatio); //autoISF
                      scale_ISF_ID = 2;
              }
}
//MP W-zero debug logs below
if (profile.enable_w_zero && hypodetect == false && now >= tae_start && now <= tae_end && glucose_status.delta >= 0 && bg >= 80 && iob_data.iob > 0.1) {
    console.log("-------------");
    console.log("W-ZERO STATUS");
    console.log("-------------");
    console.log("scale_ISF_ID: "+scale_ISF_ID+";");
    if (glucose_status.nR2 > glucose_status.nsR2 && glucose_status.nR2 > 0.95 && profile.enable_w_zero) {
        console.log("Using sensor data model.");
    } else { //MP: Use smoothed bg model otherwise
        console.log("Using smoothed data model.");
    }
    if (scale_ISF_ID == 0.1) {
        console.log("Curve status: accelerating;");
        console.log("n: ("+ncoeff_a+" / "+ncoeff_b+" / "+ncoeff_c+" / "+nR2+");"); //MP: narrow window fit of form f(x) = ax^2 + bx + c & coefficient of determination R^2;
        console.log("b: ("+glucose_status.broadfit_a+" / "+glucose_status.broadfit_b+" / "+round(glucose_status.broadfit_c,1)+" / "+glucose_status.bR2+");"); //MP: broad window fit of form f(x) = ax^2 + bx + c & coefficient of determination R^2;
        console.log("peak: "+round(glucose_status.broad_extremum, 0)+" min ("+round(extremum_bg_broad, 0)+" mg/dl);");
    } else if (scale_ISF_ID == 0.2) {
        console.log("Curve status: steady;");
        console.log("n: ("+ncoeff_a+" / "+ncoeff_b+" / "+ncoeff_c+" / "+nR2+");"); //MP: narrow window fit of form f(x) = ax^2 + bx + c & coefficient of determination R^2;
        console.log("b: ("+glucose_status.broadfit_a+" / "+glucose_status.broadfit_b+" / "+round(glucose_status.broadfit_c,1)+" / "+glucose_status.bR2+");"); //MP: broad window fit of form f(x) = ax^2 + bx + c & coefficient of determination R^2;
        console.log("peak: "+round(glucose_status.broad_extremum, 0)+" min ("+round(extremum_bg_broad, 0)+" mg/dl);");
    } else if (scale_ISF_ID == 0.3) {
        console.log("Curve status: decelerating;");
        console.log("n: ("+ncoeff_a+" / "+ncoeff_b+" / "+ncoeff_c+" / "+nR2+");"); //MP: narrow window fit of form f(x) = ax^2 + bx + c & coefficient of determination R^2;
        console.log("b: ("+glucose_status.broadfit_a+" / "+glucose_status.broadfit_b+" / "+round(glucose_status.broadfit_c,1)+" / "+glucose_status.bR2+");"); //MP: broad window fit of form f(x) = ax^2 + bx + c & coefficient of determination R^2;
        console.log("peak: "+round(glucose_status.broad_extremum, 0)+" min ("+round(extremum_bg_broad, 0)+" mg/dl);");
    } else {
        console.log("W-zero bypassed - running autoISF.");
    }
    console.log("BG: "+scaleISF_bg+" (raw: "+bg+");");
    console.log("delta: "+scaleISF_delta+" (raw: "+glucose_status.delta+");");
    if (scale_ISF_ID < 1) {
        if (scale_ISF_ID == 0.1 || scale_ISF_ID == 0.2) {
        console.log("scale_min: "+round(scale_min * 100, 1)+"% (raw: "+profile.scale_min+"%);");
        } else {
        console.log("scale_min: not used;");
        }
        console.log("scale_50: "+profile.scale_50+";");
        console.log("scale_max: "+round(scale_max * 100, 1)+"% (raw: "+profile.scale_max+"%);");
    } else if (scale_ISF_ID == 2.1) {
        console.log("autoISF (bolus capped) is active;");
    } else if (scale_ISF_ID == 2) {
        console.log("autoISF (minutes capped) is active;");
    }
    console.log("ISF: "+round(sens,0)+" (raw: "+profile_sens+");");
    if (scale_ISF_ID < 1) {
        console.log("power: "+ round(100*(1 - ((sens - profile_sens*(profile.scale_max/100)) / (profile_sens - profile_sens*(profile.scale_max/100)))), 1) +" %;");//MP: Scaling power is the percentage of scale_max; at 100%, sens = scale_max/100 * profile_sens;
    }
    console.log("mealscore: "+mealscore+";");
    console.log("ins.req.PCT: "+round(insulinReqPCT*100,1)+"% (raw: "+profile.insulinreqPCT+"%);");
    console.log("boluscap: "+boluscap+" (raw: "+profile.UAM_boluscap+");");
    console.log("-------------");
} else if (profile.enable_w_zero && now >= tae_start && now <= tae_end && bg > target_bg) {
    console.log("-------------");
    console.log("W-ZERO STATUS");
    console.log("-------------");
    console.log("scale_ISF_ID: "+scale_ISF_ID+";");
    if (hypodetect == true) {
    console.log("Hypo-mode, W-zero is inactive.");
    }
    if (scale_ISF_ID == 2.1) {
            console.log("autoISF (bolus capped) is active;");
            console.log("boluscap: "+boluscap+" (raw: "+profile.UAM_boluscap+");");
    } else if (scale_ISF_ID == 2) {
            console.log("Auto-ISF (minutes capped) is active;");
    }
    console.log("-------------");
} else {
    console.log("W-zero & autoISF bypassed - conditions were not met.");
}

//################## MP
//### W-ZERO END ### MP
//################## MP
*/
    // adjust min, max, and target BG for sensitivity, such that 50% increase in ISF raises target from 100 to 120
    var new_target_bg;
    if (profile.temptargetSet) {
        console.log("Temp Target set, no dynamic target adjustment; ");
    } else if (typeof autosens_data !== 'undefined' && autosens_data) {
        if ( profile.sensitivity_raises_target && autosens_data.ratio < 1 || profile.resistance_lowers_target && autosens_data.ratio > 1 ) {
            // with a target of 100, default 0.7-1.2 autosens min/max range would allow a 93-117 target range
            min_bg = round((min_bg - 60) / autosens_data.ratio) + 60;
            max_bg = round((max_bg - 60) / autosens_data.ratio) + 60;
            new_target_bg = round((target_bg - 60) / autosens_data.ratio) + 60;
            // don't allow target_bg below 80
            new_target_bg = Math.max(80, new_target_bg);
            if (target_bg === new_target_bg) {
                console.log("target_bg unchanged: "+new_target_bg+"; ");
            } else {
                console.log("target_bg from "+target_bg+" to "+new_target_bg+"; ");
            }
            target_bg = new_target_bg;
        }
    }
    //MP: If curve is accelerating/decelerating, raise or lower the target as defined below instead of using autosens data
    /*
    if (profile.enable_w_zero && scale_ISF_ID == 0.1 && !profile.temptargetSet) {//MP new target adj
        new_target_bg = target_bg - mealscore * (target_bg - (target_bg/profile.adjtarget)); //MP lower target if bg accelerating, adjusted by mealscore to avoid too damage in case of sensor issues
        console.log("W-zero: Target lowered to "+new_target_bg+"; ");
        target_bg = new_target_bg;
    } else if (profile.enable_w_zero && scale_ISF_ID == 0.3 && !profile.temptargetSet) {
        new_target_bg = target_bg*profile.adjtarget;
        console.log("W-zero: Target increased to "+new_target_bg+"; ");
        target_bg = new_target_bg;
    }*/
//MP target adjustment code END

    // compare currenttemp to iob_data.lastTemp and cancel temp if they don't match
    var lastTempAge;
    if (typeof iob_data.lastTemp !== 'undefined' ) {
        lastTempAge = round(( new Date(systemTime).getTime() - iob_data.lastTemp.date ) / 60000); // in minutes
    } else {
        lastTempAge = 0;
    }
    //console.error("currenttemp:",currenttemp,"lastTemp:",JSON.stringify(iob_data.lastTemp),"lastTempAge:",lastTempAge,"m");
    var tempModulus = (lastTempAge + currenttemp.duration) % 30;
    console.error("currenttemp:",currenttemp,"lastTempAge:",lastTempAge,"m","tempModulus:",tempModulus,"m");
    rT.temp = 'absolute';
    rT.deliverAt = deliverAt;
    if ( microBolusAllowed && currenttemp && iob_data.lastTemp && currenttemp.rate !== iob_data.lastTemp.rate && lastTempAge > 10 && currenttemp.duration ) {
        rT.reason = "Warning: currenttemp rate "+currenttemp.rate+" != lastTemp rate "+iob_data.lastTemp.rate+" from pumphistory; canceling temp";
        console.log("Checkpoint 0");
        return tempBasalFunctions.setTempBasal(0, 0, profile, rT, currenttemp);
    }
    if ( currenttemp && iob_data.lastTemp && currenttemp.duration > 0 ) {
        // TODO: fix this (lastTemp.duration is how long it has run; currenttemp.duration is time left
        //if ( currenttemp.duration < iob_data.lastTemp.duration - 2) {
            //rT.reason = "Warning: currenttemp duration "+currenttemp.duration+" << lastTemp duration "+round(iob_data.lastTemp.duration,1)+" from pumphistory; setting neutral temp of "+basal+".";
            //return tempBasalFunctions.setTempBasal(basal, 30, profile, rT, currenttemp);
        //}
        //console.error(lastTempAge, round(iob_data.lastTemp.duration,1), round(lastTempAge - iob_data.lastTemp.duration,1));
        var lastTempEnded = lastTempAge - iob_data.lastTemp.duration
        if ( lastTempEnded > 5 && lastTempAge > 10 ) {
            rT.reason = "Warning: currenttemp running but lastTemp from pumphistory ended "+lastTempEnded+"m ago; canceling temp";
            //console.error(currenttemp, round(iob_data.lastTemp,1), round(lastTempAge,1));
            console.log("Checkpoint 1");
            return tempBasalFunctions.setTempBasal(0, 0, profile, rT, currenttemp);
        }
        // TODO: figure out a way to do this check that doesn't fail across basal schedule boundaries
        //if ( tempModulus < 25 && tempModulus > 5 ) {
            //rT.reason = "Warning: currenttemp duration "+currenttemp.duration+" + lastTempAge "+lastTempAge+" isn't a multiple of 30m; setting neutral temp of "+basal+".";
            //console.error(rT.reason);
            //return tempBasalFunctions.setTempBasal(basal, 30, profile, rT, currenttemp);
        //}
    }

    //calculate BG impact: the amount BG "should" be rising or falling based on insulin activity alone
    var bgi = round(( -iob_data.activity * sens * 5 ), 2);
    // project deviations for 30 minutes
    var deviation = round( 30 / 5 * ( minDelta - bgi ) );
    // don't overreact to a big negative delta: use minAvgDelta if deviation is negative
    if (deviation < 0) {
        deviation = round( (30 / 5) * ( minAvgDelta - bgi ) );
        // and if deviation is still negative, use long_avgdelta
        if (deviation < 0) {
            deviation = round( (30 / 5) * ( glucose_status.long_avgdelta - bgi ) );
        }
    }

    // calculate the naive (bolus calculator math) eventual BG based on net IOB and sensitivity
    if (iob_data.iob > 0) {
        var naive_eventualBG = round( bg - (iob_data.iob * sens) );
    } else { // if IOB is negative, be more conservative and use the lower of sens, profile.sens
        naive_eventualBG = round( bg - (iob_data.iob * Math.min(sens, profile.sens) ) );
    }
    // and adjust it for the deviation above
    var eventualBG = naive_eventualBG + deviation;

    // raise target for noisy / raw CGM data
    if (glucose_status.noise >= 2) {
        // increase target at least 10% (default 30%) for raw / noisy data
        var noisyCGMTargetMultiplier = Math.max( 1.1, profile.noisyCGMTargetMultiplier );
        // don't allow maxRaw above 250
        var maxRaw = Math.min( 250, profile.maxRaw );
        var adjustedMinBG = round(Math.min(200, min_bg * noisyCGMTargetMultiplier ));
        var adjustedTargetBG = round(Math.min(200, target_bg * noisyCGMTargetMultiplier ));
        var adjustedMaxBG = round(Math.min(200, max_bg * noisyCGMTargetMultiplier ));
        console.log("Raising target_bg for noisy / raw CGM data, from "+target_bg+" to "+adjustedTargetBG+"; ");
        min_bg = adjustedMinBG;
        target_bg = adjustedTargetBG;
        max_bg = adjustedMaxBG;
    // adjust target BG range if configured to bring down high BG faster
    } else if ( bg > max_bg && profile.adv_target_adjustments && ! profile.temptargetSet ) {
        // with target=100, as BG rises from 100 to 160, adjustedTarget drops from 100 to 80
        adjustedMinBG = round(Math.max(80, min_bg - (bg - min_bg)/3 ),0);
        adjustedTargetBG =round( Math.max(80, target_bg - (bg - target_bg)/3 ),0);
        adjustedMaxBG = round(Math.max(80, max_bg - (bg - max_bg)/3 ),0);
        // if eventualBG, naive_eventualBG, and target_bg aren't all above adjustedMinBG, don’t use it
        //console.error("naive_eventualBG:",naive_eventualBG+", eventualBG:",eventualBG);
        if (eventualBG > adjustedMinBG && naive_eventualBG > adjustedMinBG && min_bg > adjustedMinBG) {
            console.log("Adjusting targets for high BG: min_bg from "+min_bg+" to "+adjustedMinBG+"; ");
            min_bg = adjustedMinBG;
        } else {
            console.log("min_bg unchanged: "+min_bg+"; ");
        }
        // if eventualBG, naive_eventualBG, and target_bg aren't all above adjustedTargetBG, don’t use it
        if (eventualBG > adjustedTargetBG && naive_eventualBG > adjustedTargetBG && target_bg > adjustedTargetBG) {
            console.log("target_bg from "+target_bg+" to "+adjustedTargetBG+"; ");
            target_bg = adjustedTargetBG;
        } else {
            console.log("target_bg unchanged: "+target_bg+"; ");
        }
        // if eventualBG, naive_eventualBG, and max_bg aren't all above adjustedMaxBG, don’t use it
        if (eventualBG > adjustedMaxBG && naive_eventualBG > adjustedMaxBG && max_bg > adjustedMaxBG) {
            console.error("max_bg from "+max_bg+" to "+adjustedMaxBG);
            max_bg = adjustedMaxBG;
        } else {
            console.error("max_bg unchanged: "+max_bg);
        }
    }

    var expectedDelta = calculate_expected_delta(target_bg, eventualBG, bgi);
    if (typeof eventualBG === 'undefined' || isNaN(eventualBG)) {
        rT.error ='Error: could not calculate eventualBG. ';
        console.log("Checkpoint 2");
        return rT;
    }

    // min_bg of 90 -> threshold of 65, 100 -> 70 110 -> 75, and 130 -> 85
    var threshold = min_bg - 0.5*(min_bg-40);

    //console.error(reservoir_data);

    rT = {
        'temp': 'absolute'
        , 'bg': bg
        , 'tick': tick
        , 'eventualBG': eventualBG
        , 'targetBG': target_bg
        , 'insulinReq': 0
        , 'reservoir' : reservoir_data // The expected reservoir volume at which to deliver the microbolus (the reservoir volume from right before the last pumphistory run)
        , 'deliverAt' : deliverAt // The time at which the microbolus should be delivered
        , 'sensitivityRatio' : sensitivityRatio // autosens ratio (fraction of normal basal)
    };

    //MP temporary placement of curve analysis logs, to have them in NS data logging
    rT.reason += "; n_fit: f(x) = "+ncoeff_a+"x^2 + "+ncoeff_b+"x + "+ncoeff_c+"; nR2 = "+nR2;
    rT.reason += "; b_fit: f(x) = "+glucose_status.broadfit_a+"x^2 + "+glucose_status.broadfit_b+"x + "+glucose_status.broadfit_c+"; bR2 = "+glucose_status.bR2;
    rT.reason += "; peak time: "+glucose_status.broad_extremum+" min";
    //MP temporary rT.reason for analysis mod end

    // generate predicted future BGs based on IOB, COB, and current absorption rate

    var COBpredBGs = [];
    var aCOBpredBGs = [];
    var IOBpredBGs = [];
    var UAMpredBGs = [];
    var ZTpredBGs = [];
    COBpredBGs.push(bg);
    aCOBpredBGs.push(bg);
    IOBpredBGs.push(bg);
    ZTpredBGs.push(bg);
    UAMpredBGs.push(bg);

    var enableSMB = enable_smb(
        profile,
        microBolusAllowed,
        meal_data,
        target_bg
    );

    // enable UAM (if enabled in preferences)
    var enableUAM=(profile.enableUAM);


    //console.error(meal_data);
    // carb impact and duration are 0 unless changed below
    var ci = 0;
    var cid = 0;
    // calculate current carb absorption rate, and how long to absorb all carbs
    // CI = current carb impact on BG in mg/dL/5m
    ci = round((minDelta - bgi),1);
    var uci = round((minDelta - bgi),1);
    // ISF (mg/dL/U) / CR (g/U) = CSF (mg/dL/g)

    // TODO: remove commented-out code for old behavior
    //if (profile.temptargetSet) {
        // if temptargetSet, use unadjusted profile.sens to allow activity mode sensitivityRatio to adjust CR
        //var csf = profile.sens / profile.carb_ratio;
    //} else {
        // otherwise, use autosens-adjusted sens to counteract autosens meal insulin dosing adjustments
        // so that autotuned CR is still in effect even when basals and ISF are being adjusted by autosens
        //var csf = sens / profile.carb_ratio;
    //}
    // use autosens-adjusted sens to counteract autosens meal insulin dosing adjustments so that
    // autotuned CR is still in effect even when basals and ISF are being adjusted by TT or autosens
    // this avoids overdosing insulin for large meals when low temp targets are active
    csf = sens / profile.carb_ratio;
    console.error("profile.sens:",profile.sens,"; sens:", round(sens, 0),"CSF:",round (csf, 0));

    var maxCarbAbsorptionRate = 30; // g/h; maximum rate to assume carbs will absorb if no CI observed
    // limit Carb Impact to maxCarbAbsorptionRate * csf in mg/dL per 5m
    var maxCI = round(maxCarbAbsorptionRate*csf*5/60,1)
    if (ci > maxCI) {
        console.error("Limiting carb impact from",ci,"to",maxCI,"mg/dL/5m (",maxCarbAbsorptionRate,"g/h )");
        ci = maxCI;
    }
    var remainingCATimeMin = 3; // h; duration of expected not-yet-observed carb absorption
    // adjust remainingCATime (instead of CR) for autosens if sensitivityRatio defined
    if (sensitivityRatio){
        remainingCATimeMin = remainingCATimeMin / sensitivityRatio;
    }
    // 20 g/h means that anything <= 60g will get a remainingCATimeMin, 80g will get 4h, and 120g 6h
    // when actual absorption ramps up it will take over from remainingCATime
    var assumedCarbAbsorptionRate = 20; // g/h; maximum rate to assume carbs will absorb if no CI observed
    var remainingCATime = remainingCATimeMin;
    if (meal_data.carbs) {
        // if carbs * assumedCarbAbsorptionRate > remainingCATimeMin, raise it
        // so <= 90g is assumed to take 3h, and 120g=4h
        remainingCATimeMin = Math.max(remainingCATimeMin, meal_data.mealCOB/assumedCarbAbsorptionRate);
        var lastCarbAge = round(( new Date(systemTime).getTime() - meal_data.lastCarbTime ) / 60000);
        //console.error(meal_data.lastCarbTime, lastCarbAge);

        var fractionCOBAbsorbed = ( meal_data.carbs - meal_data.mealCOB ) / meal_data.carbs;
        remainingCATime = remainingCATimeMin + 1.5 * lastCarbAge/60;
        remainingCATime = round(remainingCATime,1);
        //console.error(fractionCOBAbsorbed, remainingCATimeAdjustment, remainingCATime)
        console.error("Last carbs",lastCarbAge,"minutes ago; remainingCATime:",remainingCATime,"hours;",round(fractionCOBAbsorbed*100)+"% carbs absorbed");
    }

    // calculate the number of carbs absorbed over remainingCATime hours at current CI
    // CI (mg/dL/5m) * (5m)/5 (m) * 60 (min/hr) * 4 (h) / 2 (linear decay factor) = total carb impact (mg/dL)
    var totalCI = Math.max(0, ci / 5 * 60 * remainingCATime / 2);
    // totalCI (mg/dL) / CSF (mg/dL/g) = total carbs absorbed (g)
    var totalCA = totalCI / csf;
    var remainingCarbsCap = 90; // default to 90
    var remainingCarbsFraction = 1;
    if (profile.remainingCarbsCap) { remainingCarbsCap = Math.min(90,profile.remainingCarbsCap); }
    if (profile.remainingCarbsFraction) { remainingCarbsFraction = Math.min(1,profile.remainingCarbsFraction); }
    var remainingCarbsIgnore = 1 - remainingCarbsFraction;
    var remainingCarbs = Math.max(0, meal_data.mealCOB - totalCA - meal_data.carbs*remainingCarbsIgnore);
    remainingCarbs = Math.min(remainingCarbsCap,remainingCarbs);
    // assume remainingCarbs will absorb in a /\ shaped bilinear curve
    // peaking at remainingCATime / 2 and ending at remainingCATime hours
    // area of the /\ triangle is the same as a remainingCIpeak-height rectangle out to remainingCATime/2
    // remainingCIpeak (mg/dL/5m) = remainingCarbs (g) * CSF (mg/dL/g) * 5 (m/5m) * 1h/60m / (remainingCATime/2) (h)
    var remainingCIpeak = remainingCarbs * csf * 5 / 60 / (remainingCATime/2);
    //console.error(profile.min_5m_carbimpact,ci,totalCI,totalCA,remainingCarbs,remainingCI,remainingCATime);

    // calculate peak deviation in last hour, and slope from that to current deviation
    var slopeFromMaxDeviation = round(meal_data.slopeFromMaxDeviation,2);
    // calculate lowest deviation in last hour, and slope from that to current deviation
    var slopeFromMinDeviation = round(meal_data.slopeFromMinDeviation,2);
    // assume deviations will drop back down at least at 1/3 the rate they ramped up
    var slopeFromDeviations = Math.min(slopeFromMaxDeviation,-slopeFromMinDeviation/3);
    //console.error(slopeFromMaxDeviation);

    var aci = 10;
    //5m data points = g * (1U/10g) * (40mg/dL/1U) / (mg/dL/5m)
    // duration (in 5m data points) = COB (g) * CSF (mg/dL/g) / ci (mg/dL/5m)
    // limit cid to remainingCATime hours: the reset goes to remainingCI
    if (ci === 0) {
        // avoid divide by zero
        cid = 0;
    } else {
        cid = Math.min(remainingCATime*60/5/2,Math.max(0, meal_data.mealCOB * csf / ci ));
    }
    var acid = Math.max(0, meal_data.mealCOB * csf / aci );
    // duration (hours) = duration (5m) * 5 / 60 * 2 (to account for linear decay)
    console.error("Carb Impact:",ci,"mg/dL per 5m; CI Duration:",round(cid*5/60*2,1),"hours; remaining CI (~2h peak):",round(remainingCIpeak,1),"mg/dL per 5m");
    //console.error("Accel. Carb Impact:",aci,"mg/dL per 5m; ACI Duration:",round(acid*5/60*2,1),"hours");
    var minIOBPredBG = 999;
    var minCOBPredBG = 999;
    var minUAMPredBG = 999;
    var minGuardBG = bg;
    var minCOBGuardBG = 999;
    var minUAMGuardBG = 999;
    var minIOBGuardBG = 999;
    var minZTGuardBG = 999;
    var minPredBG;
    var avgPredBG;
    var IOBpredBG = eventualBG;
    var maxIOBPredBG = bg;
    var maxCOBPredBG = bg;
    var maxUAMPredBG = bg;
    //var maxPredBG = bg;
    var eventualPredBG = bg;
    var lastIOBpredBG;
    var lastCOBpredBG;
    var lastUAMpredBG;
    var lastZTpredBG;
    var UAMduration = 0;
    var remainingCItotal = 0;
    var remainingCIs = [];
    var predCIs = [];
    try {
        iobArray.forEach(function(iobTick) {
            //console.error(iobTick);
            var predBGI = round(( -iobTick.activity * sens * 5 ), 2);
            var predZTBGI = round(( -iobTick.iobWithZeroTemp.activity * sens * 5 ), 2);
            // for IOBpredBGs, predicted deviation impact drops linearly from current deviation down to zero
            // over 60 minutes (data points every 5m)
            var predDev = ci * ( 1 - Math.min(1,IOBpredBGs.length/(60/5)) );
            IOBpredBG = IOBpredBGs[IOBpredBGs.length-1] + predBGI + predDev;
            // calculate predBGs with long zero temp without deviations
            var ZTpredBG = ZTpredBGs[ZTpredBGs.length-1] + predZTBGI;
            // for COBpredBGs, predicted carb impact drops linearly from current carb impact down to zero
            // eventually accounting for all carbs (if they can be absorbed over DIA)
            var predCI = Math.max(0, Math.max(0,ci) * ( 1 - COBpredBGs.length/Math.max(cid*2,1) ) );
            var predACI = Math.max(0, Math.max(0,aci) * ( 1 - COBpredBGs.length/Math.max(acid*2,1) ) );
            // if any carbs aren't absorbed after remainingCATime hours, assume they'll absorb in a /\ shaped
            // bilinear curve peaking at remainingCIpeak at remainingCATime/2 hours (remainingCATime/2*12 * 5m)
            // and ending at remainingCATime h (remainingCATime*12 * 5m intervals)
            var intervals = Math.min( COBpredBGs.length, (remainingCATime*12)-COBpredBGs.length );
            var remainingCI = Math.max(0, intervals / (remainingCATime/2*12) * remainingCIpeak );
            remainingCItotal += predCI+remainingCI;
            remainingCIs.push(round(remainingCI,0));
            predCIs.push(round(predCI,0));
            //console.log(round(predCI,1)+"+"+round(remainingCI,1)+" ");
            COBpredBG = COBpredBGs[COBpredBGs.length-1] + predBGI + Math.min(0,predDev) + predCI + remainingCI;
            var aCOBpredBG = aCOBpredBGs[aCOBpredBGs.length-1] + predBGI + Math.min(0,predDev) + predACI;
            // for UAMpredBGs, predicted carb impact drops at slopeFromDeviations
            // calculate predicted CI from UAM based on slopeFromDeviations
            var predUCIslope = Math.max(0, uci + ( UAMpredBGs.length*slopeFromDeviations ) );
            // if slopeFromDeviations is too flat, predicted deviation impact drops linearly from
            // current deviation down to zero over 3h (data points every 5m)
            var predUCImax = Math.max(0, uci * ( 1 - UAMpredBGs.length/Math.max(3*60/5,1) ) );
            //console.error(predUCIslope, predUCImax);
            // predicted CI from UAM is the lesser of CI based on deviationSlope or DIA
            var predUCI = Math.min(predUCIslope, predUCImax);
            if(predUCI>0) {
                //console.error(UAMpredBGs.length,slopeFromDeviations, predUCI);
                UAMduration=round((UAMpredBGs.length+1)*5/60,1);
            }
            UAMpredBG = UAMpredBGs[UAMpredBGs.length-1] + predBGI + Math.min(0, predDev) + predUCI;
            //console.error(predBGI, predCI, predUCI);
            // truncate all BG predictions at 4 hours
            if ( IOBpredBGs.length < 48) { IOBpredBGs.push(IOBpredBG); }
            if ( COBpredBGs.length < 48) { COBpredBGs.push(COBpredBG); }
            if ( aCOBpredBGs.length < 48) { aCOBpredBGs.push(aCOBpredBG); }
            if ( UAMpredBGs.length < 48) { UAMpredBGs.push(UAMpredBG); }
            if ( ZTpredBGs.length < 48) { ZTpredBGs.push(ZTpredBG); }
            // calculate minGuardBGs without a wait from COB, UAM, IOB predBGs
            if ( COBpredBG < minCOBGuardBG ) { minCOBGuardBG = round(COBpredBG); }
            if ( UAMpredBG < minUAMGuardBG ) { minUAMGuardBG = round(UAMpredBG); }
            if ( IOBpredBG < minIOBGuardBG ) { minIOBGuardBG = round(IOBpredBG); }
            if ( ZTpredBG < minZTGuardBG ) { minZTGuardBG = round(ZTpredBG); }


            //MP Below peak variables have been changed from original
            // set minPredBGs starting when currently-dosed insulin activity will peak
            // look ahead 60m (regardless of insulin type) so as to be less aggressive on slower insulins
            var insulinPeakTime = 40;
            // add 30m to allow for insulin delivery (SMBs or temps)
            //insulinPeakTime = 90;
            var insulinPeak5m = (insulinPeakTime/60)*12;
            //console.error(insulinPeakTime, insulinPeak5m, profile.insulinPeakTime, profile.curve);

            // wait 90m before setting minIOBPredBG
            if ( IOBpredBGs.length > insulinPeak5m && (IOBpredBG < minIOBPredBG) ) { minIOBPredBG = round(IOBpredBG); }
            if ( IOBpredBG > maxIOBPredBG ) { maxIOBPredBG = IOBpredBG; }
            // wait 85-105m before setting COB and 60m for UAM minPredBGs
            if ( (cid || remainingCIpeak > 0) && COBpredBGs.length > insulinPeak5m && (COBpredBG < minCOBPredBG) ) { minCOBPredBG = round(COBpredBG); }
            if ( (cid || remainingCIpeak > 0) && COBpredBG > maxIOBPredBG ) { maxCOBPredBG = COBpredBG; }
            if ( enableUAM && UAMpredBGs.length > 12 && (UAMpredBG < minUAMPredBG) ) { minUAMPredBG = round(UAMpredBG); }
            if ( enableUAM && UAMpredBG > maxIOBPredBG ) { maxUAMPredBG = UAMpredBG; }
        });
        // set eventualBG to include effect of carbs
        //console.error("PredBGs:",JSON.stringify(predBGs));
    } catch (e) {
        console.error("Problem with iobArray.  Optional feature Advanced Meal Assist disabled");
    }
    if (meal_data.mealCOB) {
        console.error("predCIs (mg/dL/5m):",predCIs.join(" "));
        console.error("remainingCIs:      ",remainingCIs.join(" "));
    }
    rT.predBGs = {};
    IOBpredBGs.forEach(function(p, i, theArray) {
        theArray[i] = round(Math.min(401,Math.max(39,p)));
    });
    for (var i=IOBpredBGs.length-1; i > 12; i--) {
        if (IOBpredBGs[i-1] !== IOBpredBGs[i]) { break; }
        else { IOBpredBGs.pop(); }
    }
    rT.predBGs.IOB = IOBpredBGs;
    lastIOBpredBG=round(IOBpredBGs[IOBpredBGs.length-1]);
    ZTpredBGs.forEach(function(p, i, theArray) {
        theArray[i] = round(Math.min(401,Math.max(39,p)));
    });
    for (i=ZTpredBGs.length-1; i > 6; i--) {
        // stop displaying ZTpredBGs once they're rising and above target
        if (ZTpredBGs[i-1] >= ZTpredBGs[i] || ZTpredBGs[i] <= target_bg) { break; }
        else { ZTpredBGs.pop(); }
    }
    rT.predBGs.ZT = ZTpredBGs;
    lastZTpredBG=round(ZTpredBGs[ZTpredBGs.length-1]);
    if (meal_data.mealCOB > 0) {
        aCOBpredBGs.forEach(function(p, i, theArray) {
            theArray[i] = round(Math.min(401,Math.max(39,p)));
        });
        for (i=aCOBpredBGs.length-1; i > 12; i--) {
            if (aCOBpredBGs[i-1] !== aCOBpredBGs[i]) { break; }
            else { aCOBpredBGs.pop(); }
        }
    }
    if (meal_data.mealCOB > 0 && ( ci > 0 || remainingCIpeak > 0 )) {
        COBpredBGs.forEach(function(p, i, theArray) {
            theArray[i] = round(Math.min(401,Math.max(39,p)));
        });
        for (i=COBpredBGs.length-1; i > 12; i--) {
            if (COBpredBGs[i-1] !== COBpredBGs[i]) { break; }
            else { COBpredBGs.pop(); }
        }
        rT.predBGs.COB = COBpredBGs;
        lastCOBpredBG=round(COBpredBGs[COBpredBGs.length-1]);
        eventualBG = Math.max(eventualBG, round(COBpredBGs[COBpredBGs.length-1]) );
    }
    if (ci > 0 || remainingCIpeak > 0) {
        if (enableUAM) {
            UAMpredBGs.forEach(function(p, i, theArray) {
                theArray[i] = round(Math.min(401,Math.max(39,p)));
            });
            for (i=UAMpredBGs.length-1; i > 12; i--) {
                if (UAMpredBGs[i-1] !== UAMpredBGs[i]) { break; }
                else { UAMpredBGs.pop(); }
            }
            rT.predBGs.UAM = UAMpredBGs;
            lastUAMpredBG=round(UAMpredBGs[UAMpredBGs.length-1]);
            if (UAMpredBGs[UAMpredBGs.length-1]) {
                eventualBG = Math.max(eventualBG, round(UAMpredBGs[UAMpredBGs.length-1]) );
            }
        }

        // set eventualBG based on COB or UAM predBGs
        rT.eventualBG = eventualBG;
    }

    console.error("UAM Impact:",uci,"mg/dL per 5m; UAM Duration:",UAMduration,"hours");


    minIOBPredBG = Math.max(39,minIOBPredBG);
    minCOBPredBG = Math.max(39,minCOBPredBG);
    minUAMPredBG = Math.max(39,minUAMPredBG);
    minPredBG = round(minIOBPredBG);

    var fractionCarbsLeft = meal_data.mealCOB/meal_data.carbs;
    // if we have COB and UAM is enabled, average both
    if ( minUAMPredBG < 999 && minCOBPredBG < 999 ) {
        // weight COBpredBG vs. UAMpredBG based on how many carbs remain as COB
        avgPredBG = round( (1-fractionCarbsLeft)*UAMpredBG + fractionCarbsLeft*COBpredBG );
    // if UAM is disabled, average IOB and COB
    } else if ( minCOBPredBG < 999 ) {
        avgPredBG = round( (IOBpredBG + COBpredBG)/2 );
    // if we have UAM but no COB, average IOB and UAM
    } else if ( minUAMPredBG < 999 ) {
        avgPredBG = round( (IOBpredBG + UAMpredBG)/2 );
    } else {
        avgPredBG = round( IOBpredBG );
    }
    // if avgPredBG is below minZTGuardBG, bring it up to that level
    if ( minZTGuardBG > avgPredBG ) {
        avgPredBG = minZTGuardBG;
    }

    // if we have both minCOBGuardBG and minUAMGuardBG, blend according to fractionCarbsLeft
    if ( (cid || remainingCIpeak > 0) ) {
        if ( enableUAM ) {
            minGuardBG = fractionCarbsLeft*minCOBGuardBG + (1-fractionCarbsLeft)*minUAMGuardBG;
        } else {
            minGuardBG = minCOBGuardBG;
        }
    } else if ( enableUAM ) {
        minGuardBG = minUAMGuardBG;
    } else {
        minGuardBG = minIOBGuardBG;
    }
    minGuardBG = round(minGuardBG);
    //console.error(minCOBGuardBG, minUAMGuardBG, minIOBGuardBG, minGuardBG);

    var minZTUAMPredBG = minUAMPredBG;
    // if minZTGuardBG is below threshold, bring down any super-high minUAMPredBG by averaging
    // this helps prevent UAM from giving too much insulin in case absorption falls off suddenly
    if ( minZTGuardBG < threshold ) {
        minZTUAMPredBG = (minUAMPredBG + minZTGuardBG) / 2;
    // if minZTGuardBG is between threshold and target, blend in the averaging
    } else if ( minZTGuardBG < target_bg ) {
        // target 100, threshold 70, minZTGuardBG 85 gives 50%: (85-70) / (100-70)
        var blendPct = (minZTGuardBG-threshold) / (target_bg-threshold);
        var blendedMinZTGuardBG = minUAMPredBG*blendPct + minZTGuardBG*(1-blendPct);
        minZTUAMPredBG = (minUAMPredBG + blendedMinZTGuardBG) / 2;
        //minZTUAMPredBG = minUAMPredBG - target_bg + minZTGuardBG;
    // if minUAMPredBG is below minZTGuardBG, bring minUAMPredBG up by averaging
    // this allows more insulin if lastUAMPredBG is below target, but minZTGuardBG is still high
    } else if ( minZTGuardBG > minUAMPredBG ) {
        minZTUAMPredBG = (minUAMPredBG + minZTGuardBG) / 2;
    }
    minZTUAMPredBG = round(minZTUAMPredBG);
    //console.error("minUAMPredBG:",minUAMPredBG,"minZTGuardBG:",minZTGuardBG,"minZTUAMPredBG:",minZTUAMPredBG);
    // if any carbs have been entered recently
    if (meal_data.carbs) {

        // if UAM is disabled, use max of minIOBPredBG, minCOBPredBG
        if ( ! enableUAM && minCOBPredBG < 999 ) {
            minPredBG = round(Math.max(minIOBPredBG, minCOBPredBG));
        // if we have COB, use minCOBPredBG, or blendedMinPredBG if it's higher
        } else if ( minCOBPredBG < 999 ) {
            // calculate blendedMinPredBG based on how many carbs remain as COB
            var blendedMinPredBG = fractionCarbsLeft*minCOBPredBG + (1-fractionCarbsLeft)*minZTUAMPredBG;
            // if blendedMinPredBG > minCOBPredBG, use that instead
            minPredBG = round(Math.max(minIOBPredBG, minCOBPredBG, blendedMinPredBG));
        // if carbs have been entered, but have expired, use minUAMPredBG
        } else if ( enableUAM ) {
            minPredBG = minZTUAMPredBG;
        } else {
            minPredBG = minGuardBG;
        }
    // in pure UAM mode, use the higher of minIOBPredBG,minUAMPredBG
    } else if ( enableUAM ) {
        minPredBG = round(Math.max(minIOBPredBG,minZTUAMPredBG));
    }

    // make sure minPredBG isn't higher than avgPredBG
    minPredBG = Math.min( minPredBG, avgPredBG );

    console.log("minPredBG: "+minPredBG+" minIOBPredBG: "+minIOBPredBG+" minZTGuardBG: "+minZTGuardBG);
    if (minCOBPredBG < 999) {
        console.log(" minCOBPredBG: "+minCOBPredBG);
    }
    if (minUAMPredBG < 999) {
        console.log(" minUAMPredBG: "+minUAMPredBG);
    }
    console.error(" avgPredBG:",avgPredBG,"COB:",meal_data.mealCOB,"/",meal_data.carbs);
    // But if the COB line falls off a cliff, don't trust UAM too much:
    // use maxCOBPredBG if it's been set and lower than minPredBG
    if ( maxCOBPredBG > bg ) {
        minPredBG = Math.min(minPredBG, maxCOBPredBG);
    }

    rT.COB=meal_data.mealCOB;
    rT.IOB=iob_data.iob;
    rT.reason="COB: " + round(meal_data.mealCOB, 1) + ", Dev: " + convert_bg(deviation, profile) + ", BGI: " + convert_bg(bgi, profile) + ", ISF: " + convert_bg(sens, profile) + ", CR: " + round(profile.carb_ratio, 2) + ", Target: " + convert_bg(target_bg, profile) + ", minPredBG " + convert_bg(minPredBG, profile) + ", minGuardBG " + convert_bg(minGuardBG, profile) + ", IOBpredBG " + convert_bg(lastIOBpredBG, profile);
    if (lastCOBpredBG > 0) {
        rT.reason += ", COBpredBG " + convert_bg(lastCOBpredBG, profile);
    }
    if (lastUAMpredBG > 0) {
        rT.reason += ", UAMpredBG " + convert_bg(lastUAMpredBG, profile)
    }
    rT.reason += "; ";
    // use naive_eventualBG if above 40, but switch to minGuardBG if both eventualBGs hit floor of 39
    var carbsReqBG = naive_eventualBG;
    if ( carbsReqBG < 40 ) {
        carbsReqBG = Math.min( minGuardBG, carbsReqBG );
    }
    var bgUndershoot = threshold - carbsReqBG;
    // calculate how long until COB (or IOB) predBGs drop below min_bg
    var minutesAboveMinBG = 240;
    var minutesAboveThreshold = 240;
    if (meal_data.mealCOB > 0 && ( ci > 0 || remainingCIpeak > 0 )) {
        for (i=0; i<COBpredBGs.length; i++) {
            //console.error(COBpredBGs[i], min_bg);
            if ( COBpredBGs[i] < min_bg ) {
                minutesAboveMinBG = 5*i;
                break;
            }
        }
        for (i=0; i<COBpredBGs.length; i++) {
            //console.error(COBpredBGs[i], threshold);
            if ( COBpredBGs[i] < threshold ) {
                minutesAboveThreshold = 5*i;
                break;
            }
        }
    } else {
        for (i=0; i<IOBpredBGs.length; i++) {
            //console.error(IOBpredBGs[i], min_bg);
            if ( IOBpredBGs[i] < min_bg ) {
                minutesAboveMinBG = 5*i;
                break;
            }
        }
        for (i=0; i<IOBpredBGs.length; i++) {
            //console.error(IOBpredBGs[i], threshold);
            if ( IOBpredBGs[i] < threshold ) {
                minutesAboveThreshold = 5*i;
                break;
            }
        }
    }
//MP Disable the block below if tae is active - BG predictions are mostly wrong during UAM and can result in persistent highs - other safety features are in place
if (!activity_controller) {
    if (enableSMB && minGuardBG < threshold) {
        console.error("minGuardBG",convert_bg(minGuardBG, profile),"projected below", convert_bg(threshold, profile) ,"- disabling SMB");
        //rT.reason += "minGuardBG "+minGuardBG+"<"+threshold+": SMB disabled; ";
        enableSMB = false;
    }
}
    if ( maxDelta > 0.30 * bg ) {
        console.error("maxDelta",convert_bg(maxDelta, profile),"> 30% of BG",convert_bg(bg, profile),"- disabling SMB");
        rT.reason += "maxDelta "+convert_bg(maxDelta, profile)+" > 30% of BG "+convert_bg(bg, profile)+": SMB disabled; ";
        enableSMB = false;
    }

    console.error("BG projected to remain above",convert_bg(min_bg, profile),"for",minutesAboveMinBG,"minutes");
    if ( minutesAboveThreshold < 240 || minutesAboveMinBG < 60 ) {
        console.error("BG projected to remain above",convert_bg(threshold,profile),"for",minutesAboveThreshold,"minutes");
    }
    // include at least minutesAboveThreshold worth of zero temps in calculating carbsReq
    // always include at least 30m worth of zero temp (carbs to 80, low temp up to target)
    var zeroTempDuration = minutesAboveThreshold;
    // BG undershoot, minus effect of zero temps until hitting min_bg, converted to grams, minus COB
    var zeroTempEffect = profile.current_basal*sens*zeroTempDuration/60;
    // don't count the last 25% of COB against carbsReq
    var COBforCarbsReq = Math.max(0, meal_data.mealCOB - 0.25*meal_data.carbs);
    var carbsReq = (bgUndershoot - zeroTempEffect) / csf - COBforCarbsReq;
    zeroTempEffect = round(zeroTempEffect);
    carbsReq = round(carbsReq);
    console.error("naive_eventualBG:",naive_eventualBG,"bgUndershoot:",bgUndershoot,"zeroTempDuration:",zeroTempDuration,"zeroTempEffect:",zeroTempEffect,"carbsReq:",carbsReq);
    if ( carbsReq >= profile.carbsReqThreshold && minutesAboveThreshold <= 45 ) {
        rT.carbsReq = carbsReq;
        rT.carbsReqWithin = minutesAboveThreshold;
        rT.reason += carbsReq + " add'l carbs req w/in " + minutesAboveThreshold + "m; ";
    }
    // don't low glucose suspend if IOB is already super negative and BG is rising faster than predicted
    if (bg < threshold && iob_data.iob < -profile.current_basal*20/60 && minDelta > 0 && minDelta > expectedDelta) {
        rT.reason += "IOB "+iob_data.iob+" < " + round(-profile.current_basal*20/60,2);
        rT.reason += " and minDelta " + convert_bg(minDelta, profile) + " > " + "expectedDelta " + convert_bg(expectedDelta, profile) + "; ";
    // predictive low glucose suspend mode: BG is / is projected to be < threshold
    //MP Disable the block below if tae is active - BG predictions are mostly wrong during UAM and can result in persistent highs - other safety features are in place
    } else if (!activity_controller && (bg < threshold || minGuardBG < threshold )) { //MP modified to include !activity_controller - default behaviour if tae is off
        rT.reason += "minGuardBG " + convert_bg(minGuardBG, profile) + "<" + convert_bg(threshold, profile);
        bgUndershoot = target_bg - minGuardBG;
        var worstCaseInsulinReq = bgUndershoot / sens;
        var durationReq = round(60*worstCaseInsulinReq / profile.current_basal);
        durationReq = round(durationReq/30)*30;
        // always set a 30-120m zero temp (oref0-pump-loop will let any longer SMB zero temp run)
        durationReq = Math.min(120,Math.max(30,durationReq));
        console.log("Checkpoint 3A");
        return tempBasalFunctions.setTempBasal(0, durationReq, profile, rT, currenttemp);
    } else if (activity_controller && bg < threshold) { //MP added to have default behaviour also if tae is on, but prediction condition was removed
        rT.reason += "minGuardBG " + convert_bg(minGuardBG, profile) + "<" + convert_bg(threshold, profile);
        bgUndershoot = target_bg - minGuardBG;
        var worstCaseInsulinReq = bgUndershoot / sens;
        var durationReq = round(60*worstCaseInsulinReq / profile.current_basal);
        durationReq = round(durationReq/30)*30;
        // always set a 30-120m zero temp (oref0-pump-loop will let any longer SMB zero temp run)
        durationReq = Math.min(120,Math.max(30,durationReq));
        console.log("Checkpoint 3B");
        return tempBasalFunctions.setTempBasal(0, durationReq, profile, rT, currenttemp);
    }

    // if not in LGS mode, cancel temps before the top of the hour to reduce beeping/vibration
    // console.error(profile.skip_neutral_temps, rT.deliverAt.getMinutes());
    if ( profile.skip_neutral_temps && rT.deliverAt.getMinutes() >= 55 ) {
        rT.reason += "; Canceling temp at " + rT.deliverAt.getMinutes() + "m past the hour. ";
        console.log("Checkpoint 4");
        return tempBasalFunctions.setTempBasal(0, 0, profile, rT, currenttemp);
    }
if (!activity_controller) { //MP Bypass oref1 block below if TAE is active
    if (eventualBG < min_bg) { // if eventual BG is below target:
        rT.reason += "Eventual BG " + convert_bg(eventualBG, profile) + " < " + convert_bg(min_bg, profile);
        // if 5m or 30m avg BG is rising faster than expected delta
        if ( minDelta > expectedDelta && minDelta > 0 && !carbsReq ) {
            // if naive_eventualBG < 40, set a 30m zero temp (oref0-pump-loop will let any longer SMB zero temp run)
            if (naive_eventualBG < 40) {
                rT.reason += ", naive_eventualBG < 40. ";
                console.log("Checkpoint 5");
                return tempBasalFunctions.setTempBasal(0, 30, profile, rT, currenttemp);
            }
            if (glucose_status.delta > minDelta) {
                rT.reason += ", but Delta " + convert_bg(tick, profile) + " > expectedDelta " + convert_bg(expectedDelta, profile);
            } else {
                rT.reason += ", but Min. Delta " + minDelta.toFixed(2) + " > Exp. Delta " + convert_bg(expectedDelta, profile);
            }
            if (currenttemp.duration > 15 && (round_basal(basal, profile) === round_basal(currenttemp.rate, profile))) {
                rT.reason += ", temp " + currenttemp.rate + " ~ req " + basal + "U/hr. ";
                console.log("Checkpoint 6A");
                return rT;
            } else {
                rT.reason += "; setting current basal of " + basal + " as temp. ";
                console.log("Checkpoint 6B");
                return tempBasalFunctions.setTempBasal(basal, 30, profile, rT, currenttemp);
            }
        }

        // calculate 30m low-temp required to get projected BG up to target
        // multiply by 2 to low-temp faster for increased hypo safety
        var insulinReq = 2 * Math.min(0, (eventualBG - target_bg) / sens);
        insulinReq = round( insulinReq , 2);
        // calculate naiveInsulinReq based on naive_eventualBG
        var naiveInsulinReq = Math.min(0, (naive_eventualBG - target_bg) / sens);
        naiveInsulinReq = round( naiveInsulinReq , 2);
        if (minDelta < 0 && minDelta > expectedDelta) {
            // if we're barely falling, newinsulinReq should be barely negative
            var newinsulinReq = round(( insulinReq * (minDelta / expectedDelta) ), 2);
            //console.error("Increasing insulinReq from " + insulinReq + " to " + newinsulinReq);
            insulinReq = newinsulinReq;
            console.log("Checkpoint 4");
        }
        // rate required to deliver insulinReq less insulin over 30m:
        var rate = basal + (2 * insulinReq);
        rate = round_basal(rate, profile);

        // if required temp < existing temp basal
        var insulinScheduled = currenttemp.duration * (currenttemp.rate - basal) / 60;
        // if current temp would deliver a lot (30% of basal) less than the required insulin,
        // by both normal and naive calculations, then raise the rate
        var minInsulinReq = Math.min(insulinReq,naiveInsulinReq);
        if (insulinScheduled < minInsulinReq - basal*0.3) {
            rT.reason += ", "+currenttemp.duration + "m@" + (currenttemp.rate).toFixed(2) + " is a lot less than needed. ";
            console.log("Checkpoint 7");
            return tempBasalFunctions.setTempBasal(rate, 30, profile, rT, currenttemp);
        }
        if (typeof currenttemp.rate !== 'undefined' && (currenttemp.duration > 5 && rate >= currenttemp.rate * 0.8)) {
            rT.reason += ", temp " + currenttemp.rate + " ~< req " + rate + "U/hr. ";
            console.log("Checkpoint 8A");
            return rT;
        } else {
            // calculate a long enough zero temp to eventually correct back up to target
            if ( rate <=0 ) {
                bgUndershoot = target_bg - naive_eventualBG;
                worstCaseInsulinReq = bgUndershoot / sens;
                durationReq = round(60*worstCaseInsulinReq / profile.current_basal);
                if (durationReq < 0) {
                    durationReq = 0;
                // don't set a temp longer than 120 minutes
                } else {
                    durationReq = round(durationReq/30)*30;
                    durationReq = Math.min(120,Math.max(0,durationReq));
                }
                //console.error(durationReq);
                if (durationReq > 0) {
                    rT.reason += ", setting " + durationReq + "m zero temp. ";
                    console.log("Checkpoint 8B");
                    return tempBasalFunctions.setTempBasal(rate, durationReq, profile, rT, currenttemp);
                }
            } else {
                rT.reason += ", setting " + rate + "U/hr. ";
            }
            console.log("Checkpoint 8C");
            return tempBasalFunctions.setTempBasal(rate, 30, profile, rT, currenttemp);
        }
    }
}
    // if eventual BG is above min but BG is falling faster than expected Delta
    if (minDelta < expectedDelta) {
        // if in SMB mode, don't cancel SMB zero temp
        if (! (microBolusAllowed && enableSMB)) {
            if (glucose_status.delta < minDelta) {
                rT.reason += "Eventual BG " + convert_bg(eventualBG, profile) + " > " + convert_bg(min_bg, profile) + " but Delta " + convert_bg(tick, profile) + " < Exp. Delta " + convert_bg(expectedDelta, profile);
            } else {
                rT.reason += "Eventual BG " + convert_bg(eventualBG, profile) + " > " + convert_bg(min_bg, profile) + " but Min. Delta " + minDelta.toFixed(2) + " < Exp. Delta " + convert_bg(expectedDelta, profile);
            }
            if (currenttemp.duration > 15 && (round_basal(basal, profile) === round_basal(currenttemp.rate, profile))) {
                rT.reason += ", temp " + currenttemp.rate + " ~ req " + basal + "U/hr. ";
                console.log("Checkpoint 9A");
                return rT;
            } else {
                rT.reason += "; setting current basal of " + basal + " as temp. ";
                console.log("Checkpoint 9B");
                return tempBasalFunctions.setTempBasal(basal, 30, profile, rT, currenttemp);
            }
        }
    }
    // eventualBG or minPredBG is below max_bg
    if (Math.min(eventualBG,minPredBG) < max_bg) {
        // if in SMB mode, don't cancel SMB zero temp
        if (! (microBolusAllowed && enableSMB )) {
            rT.reason += convert_bg(eventualBG, profile)+"-"+convert_bg(minPredBG, profile)+" in range: no temp required";
            if (currenttemp.duration > 15 && (round_basal(basal, profile) === round_basal(currenttemp.rate, profile))) {
                rT.reason += ", temp " + currenttemp.rate + " ~ req " + basal + "U/hr. ";
                console.log("Checkpoint 10A");
                return rT;
            } else {
                rT.reason += "; setting current basal of " + basal + " as temp. ";
                console.log("Checkpoint 10B");
                return tempBasalFunctions.setTempBasal(basal, 30, profile, rT, currenttemp);
            }
        }
    }

    // eventual BG is at/above target
    // if iob is over max, just cancel any temps
    if ( eventualBG >= max_bg ) {
        rT.reason += "Eventual BG " + convert_bg(eventualBG, profile) + " >= " +  convert_bg(max_bg, profile) + ", ";
    }
    if (iob_data.iob > max_iob) {
        rT.reason += "IOB " + round(iob_data.iob,2) + " > max_iob " + max_iob;
        if (currenttemp.duration > 15 && (round_basal(basal, profile) === round_basal(currenttemp.rate, profile))) {
            rT.reason += ", temp " + currenttemp.rate + " ~ req " + basal + "U/hr. ";
            console.log("Checkpoint 11A");
            return rT;
        } else {
            rT.reason += "; setting current basal of " + basal + " as temp. ";
            console.log("Checkpoint 11B");
            return tempBasalFunctions.setTempBasal(basal, 30, profile, rT, currenttemp);
        }
    } else { // otherwise, calculate 30m high-temp required to get projected BG down to target

        // insulinReq is the additional insulin required to get minPredBG down to target_bg
        //console.error(minPredBG,eventualBG);
        //MP Activity control start
        if (!activity_controller) {
            insulinReq = round( (Math.min(minPredBG,eventualBG) - target_bg) / sens, 2);
        } else {
            insulinReq = tsunami_insreq;
        }
        //MP Activity control end
        //MP uncomment InsulinReq line below for standard behaviour and delete statement above
        //insulinReq = round( (Math.min(minPredBG,eventualBG) - target_bg) / sens, 2);
        // if that would put us over max_iob, then reduce accordingly
        if (insulinReq > max_iob-iob_data.iob) {
            rT.reason += "max_iob " + max_iob + ", ";
            insulinReq = max_iob-iob_data.iob;
            console.log("InsulinReq adjusted for max_iob!")
        }

        // rate required to deliver insulinReq more insulin over 30m:
        rate = basal + (2 * insulinReq);
        rate = round_basal(rate, profile);
        insulinReq = round(insulinReq,3);
        rT.insulinReq = insulinReq;
        //console.error(iob_data.lastBolusTime);
        // minutes since last bolus
        var lastBolusAge = round(( new Date(systemTime).getTime() - iob_data.lastBolusTime ) / 60000,1);
        //console.error(lastBolusAge);
        //console.error(profile.temptargetSet, target_bg, rT.COB);
        // only allow microboluses with COB or low temp targets, or within DIA hours of a bolus
        if (microBolusAllowed && enableSMB && bg > threshold) {
            // never bolus more than maxSMBBasalMinutes worth of basal
            var mealInsulinReq = round( meal_data.mealCOB / profile.carb_ratio ,3);
            if (typeof profile.maxSMBBasalMinutes === 'undefined' ) {
                var maxBolus = round( profile.current_basal * 30 / 60 ,1);
                console.error("profile.maxSMBBasalMinutes undefined: defaulting to 30m");
            // if IOB covers more than COB, limit maxBolus to 30m of basal
            } else if ( iob_data.iob > mealInsulinReq && iob_data.iob > 0 ) {
                console.error("IOB",iob_data.iob,"> COB",meal_data.mealCOB+"; mealInsulinReq =",mealInsulinReq);
           //     if (profile.maxUAMSMBBasalMinutes) {
           //         console.error("profile.maxUAMSMBBasalMinutes:",profile.maxUAMSMBBasalMinutes,"profile.current_basal:",profile.current_basal);
           //         maxBolus = round( profile.current_basal * profile.maxUAMSMBBasalMinutes / 60 ,1);
             //MP: UAM_boluscap limiter: Use UAM minutes outside TT (default lines: above)
                 if (profile.enable_tae && activity_controller) {
                    maxBolus = boluscap;
                 } else {
                    console.error("profile.maxUAMSMBBasalMinutes undefined: defaulting to 30m");
                    maxBolus = round( profile.current_basal * 30 / 60 ,1);
                 }
                /*if (profile.maxUAMSMBBasalMinutes && scale_ISF_ID !== 0.1 && scale_ISF_ID !== 0.2 && scale_ISF_ID !== 0.3 && scale_ISF_ID !== 0 && scale_ISF_ID !== 1) {
                    if (scale_ISF_ID == 2.1) { //MP: Cap maxbolus for autoISF at modified UAM_boluscap
                        //console.error("autoISF_BC: maxbolus capped at ",0.75 * boluscap," U.");
                        maxBolus = 0.75 * boluscap;
                    } else { //MP: Cap maxbolus for autoISF at UAM minutes limit
                    //console.error("autoISF_MC: maxbolus capped at ",round( basal * profile.maxUAMSMBBasalMinutes / 60 ,1)," U.");
                    maxBolus = round( basal * profile.maxUAMSMBBasalMinutes / 60 ,1);
                    }
             //MP: UAM_boluscap limiter: USE UAM_boluscap within TT
                } else if (scale_ISF_ID == 0 || scale_ISF_ID == 0.1 || scale_ISF_ID == 0.2 || scale_ISF_ID == 0.3 || scale_ISF_ID == 1) {
                    //console.error("boluscap (adj.):",boluscap," U;");
                    maxBolus = boluscap;
                } else {
                    console.error("profile.maxUAMSMBBasalMinutes undefined: defaulting to 30m");
                    maxBolus = round( profile.current_basal * 30 / 60 ,1);
                }*/
            } else {
                console.error("profile.maxSMBBasalMinutes:",profile.maxSMBBasalMinutes,"profile.current_basal:",profile.current_basal);
                maxBolus = round( profile.current_basal * profile.maxSMBBasalMinutes / 60 ,1);
            }
            // bolus 1/2 the insulinReq, up to maxBolus, rounding down to nearest bolus increment
            var roundSMBTo = 1 / profile.bolus_increment;
            var microBolus = Math.floor(Math.min(insulinReq * insulinReqPCT,maxBolus)*roundSMBTo)/roundSMBTo;
            // calculate a long enough zero temp to eventually correct back up to target
            var smbTarget = target_bg;
            worstCaseInsulinReq = (smbTarget - (naive_eventualBG + minIOBPredBG)/2 ) / sens;
            durationReq = round(60*worstCaseInsulinReq / profile.current_basal);

            // if insulinReq > 0 but not enough for a microBolus, don't set an SMB zero temp
            if (insulinReq > 0 && microBolus < profile.bolus_increment) {
                durationReq = 0;
            }

            var smbLowTempReq = 0;
            if (durationReq <= 0) {
                durationReq = 0;
            // don't set an SMB zero temp longer than 60 minutes
            } else if (durationReq >= 30) {
                durationReq = round(durationReq/30)*30;
                durationReq = Math.min(30,Math.max(0,durationReq));
            } else {
                // if SMB durationReq is less than 30m, set a nonzero low temp
                smbLowTempReq = round( basal * durationReq/30 ,2);
                durationReq = 30;
            }
            //MP rT.reason for UAM mods start
            if (activity_controller) {
                rt.reason += "mealscore: "+mealscore;
                rt.reason += "bgscore: "+bgscore;
                rt.reason += "insulinreqPCT_live: "+profile.insulinreqPCT * mealscore;
                rt.reason += "boluscap_live: "+boluscap;
                rt.reason += "tsunami_insreq: "+tsunami_insreq;
                rt.reason += "tae_delta: "+tae_delta;
                rt.reason += "tae_bg: "+tae_bg;
            }
            /*
            rT.reason += " insulinReq: " + insulinReq + " U; InsulinReqPCT: " + insulinReqPCT*100 + "%; scale_ISF_ID: " + scale_ISF_ID + "; scale_min (adj.): " + scale_min + "; scale_max (adj:): " + scale_max + "; scale_50: " + profile.scale_50 + ";";
            if (scale_ISF_ID == 0) {
                rT.reason += " W1, scaling power: " + round((1 - ((sens - scale_max*profile_sens)/(profile_sens*scale_min - scale_max*profile_sens)))*100, 1) + "%";
            } else if (scale_ISF_ID == 1) {
                rT.reason += " W2, scaling power: " + round((1 - ((sens - scale_max*profile_sens)/(profile_sens - scale_max*profile_sens)))*100, 1) + "%";
            } else if (scale_ISF_ID == 2) {
                rT.reason += " autoISF_MC: ISF from "+profile_sens+" to "+sens;
            } else if (scale_ISF_ID == 2.1) {
                rT.reason += " autoISF_BC: ISF from "+profile_sens+" to "+sens;
            } else if (scale_ISF_ID == 0.1) {
                rT.reason += " W-zero (accel.): ISF from "+profile_sens+" to "+sens+"; Power: "+ round(100*(1 - ((sens - profile_sens*(profile.scale_max/100)) / (profile_sens - profile_sens*(profile.scale_max/100)))), 1) + "%";
            } else if (scale_ISF_ID == 0.2) {
                rT.reason += " W-zero (steady): ISF from "+profile_sens+" to "+sens+"; Power: "+ round(100*(1 - ((sens - profile_sens*(profile.scale_max/100)) / (profile_sens - profile_sens*(profile.scale_max/100)))), 1) + "%";
            } else if (scale_ISF_ID == 0.3) {
                rT.reason += " W-zero (decel.): ISF from "+profile_sens+" to "+sens+"; Power: "+ round(100*(1 - ((sens - profile_sens*(profile.scale_max/100)) / (profile_sens - profile_sens*(profile.scale_max/100)))), 1) + "%";
            }
            if (scale_ISF_ID > 0.0 && scale_ISF_ID < 1.0) {
                rT.reason += "; mealscore: "+mealscore;
            }
            //MP curve analysis logs
            rT.reason += "; n_fit: f(x) = "+ncoeff_a+"x^2 + "+ncoeff_b+"x + "+ncoeff_c+"; nR2 = "+nR2;
            rT.reason += "; b_fit: f(x) = "+glucose_status.broadfit_a+"x^2 + "+glucose_status.broadfit_b+"x + "+glucose_status.broadfit_c+"; bR2 = "+glucose_status.bR2;
            rT.reason += "; peak: "+glucose_status.broad_extremum+" min / "+extremum_bg_broad+" mg/dl ";
            */
            //MP rT.reason for UAM mods end
            if (microBolus >= maxBolus) {
                rT.reason +=  "; maxBolus " + maxBolus;
            }
            if (durationReq > 0) {
                rT.reason += "; setting " + durationReq + "m low temp of " + smbLowTempReq + "U/h";
            }
            rT.reason += ". ";

            //allow SMBs every 3 minutes by default
            var SMBInterval = 3;
            if (profile.SMBInterval) {
                // allow SMBIntervals between 1 and 10 minutes
                SMBInterval = Math.min(10,Math.max(1,profile.SMBInterval));
            }
            var nextBolusMins = round(SMBInterval-lastBolusAge,0);
            var nextBolusSeconds = round((SMBInterval - lastBolusAge) * 60, 0) % 60;
            //console.error(naive_eventualBG, insulinReq, worstCaseInsulinReq, durationReq);
            console.error("naive_eventualBG",naive_eventualBG+",",durationReq+"m "+smbLowTempReq+"U/h temp needed; last bolus",lastBolusAge+"m ago; maxBolus: "+maxBolus);
            if (lastBolusAge > SMBInterval) {
                if (microBolus > 0) {
                    rT.units = microBolus;
                    rT.reason += "Microbolusing " + microBolus + "U. ";
                }
            } else {
                rT.reason += "Waiting " + nextBolusMins + "m " + nextBolusSeconds + "s to microbolus again. ";
            }
            //rT.reason += ". ";

            // if no zero temp is required, don't return yet; allow later code to set a high temp
            if (durationReq > 0) {
                rT.rate = smbLowTempReq;
                rT.duration = durationReq;
                console.log("Checkpoint 12");
                return rT;
            }

        }

        var maxSafeBasal = tempBasalFunctions.getMaxSafeBasal(profile);

        if (rate > maxSafeBasal) {
            rT.reason += "adj. req. rate: "+round(rate, 2)+" to maxSafeBasal: "+maxSafeBasal+", ";
            rate = round_basal(maxSafeBasal, profile);
        }

        insulinScheduled = currenttemp.duration * (currenttemp.rate - basal) / 60;
        if (insulinScheduled >= insulinReq * 2) { // if current temp would deliver >2x more than the required insulin, lower the rate
            rT.reason += currenttemp.duration + "m@" + (currenttemp.rate).toFixed(2) + " > 2 * insulinReq. Setting temp basal of " + rate + "U/hr. ";
            console.log("Checkpoint 13");
            return tempBasalFunctions.setTempBasal(rate, 30, profile, rT, currenttemp);
        }

        if (typeof currenttemp.duration === 'undefined' || currenttemp.duration === 0) { // no temp is set
            rT.reason += "no temp, setting " + rate + "U/hr. ";
            console.log("Checkpoint 14");
            return tempBasalFunctions.setTempBasal(rate, 30, profile, rT, currenttemp);
        }

        if (currenttemp.duration > 5 && (round_basal(rate, profile) <= round_basal(currenttemp.rate, profile))) { // if required temp <~ existing temp basal
            rT.reason += "temp " + currenttemp.rate + " >~ req " + rate + "U/hr. ";
            console.log("Checkpoint 15");
            return rT;
        }

        // required temp > existing temp basal
        rT.reason += "temp " + currenttemp.rate + "<" + rate + "U/hr. ";
        console.log("Checkpoint 16 (final)");
        return tempBasalFunctions.setTempBasal(rate, 30, profile, rT, currenttemp);
    }

};

module.exports = determine_basal;
