package info.nightscout.androidaps.dialogs

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.common.base.Joiner
import info.nightscout.androidaps.Constants
import info.nightscout.androidaps.R
import info.nightscout.androidaps.activities.ErrorHelperActivity
import info.nightscout.androidaps.data.DetailedBolusInfo
import info.nightscout.androidaps.database.AppRepository
import info.nightscout.androidaps.database.entities.TemporaryTarget
import info.nightscout.androidaps.database.entities.UserEntry.Action
import info.nightscout.androidaps.database.entities.UserEntry.Sources
import info.nightscout.androidaps.database.entities.ValueWithUnit
import info.nightscout.androidaps.database.transactions.InsertAndCancelCurrentTemporaryTargetTransaction
import info.nightscout.androidaps.database.transactions.TsunamiModeSwitchTransaction
import info.nightscout.androidaps.databinding.DialogUamBinding
import info.nightscout.androidaps.extensions.formatColor
import info.nightscout.androidaps.extensions.toVisibility
import info.nightscout.androidaps.interfaces.*
import info.nightscout.shared.logging.LTag
import info.nightscout.androidaps.logging.UserEntryLogger
import info.nightscout.androidaps.plugins.configBuilder.ConstraintChecker
import info.nightscout.androidaps.queue.Callback
import info.nightscout.androidaps.utils.*
import info.nightscout.androidaps.utils.alertDialogs.OKDialog
import info.nightscout.androidaps.utils.extensions.toSignedString
import info.nightscout.androidaps.utils.resources.ResourceHelper
import info.nightscout.shared.SafeParse
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.plusAssign
import java.text.DecimalFormat
import java.util.*
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import kotlin.math.abs
import kotlin.math.max
//test
//import info.nightscout.androidaps.plugins.iob.iobCobCalculator.GlucoseStatus
//import info.nightscout.androidaps.interfaces.IobCobCalculator
//import info.nightscout.androidaps.plugins.iob.iobCobCalculator.GlucoseStatus
//import info.nightscout.androidaps.data.MealData
//testend
class UAMDialog : DialogFragmentWithDate() {

    @Inject lateinit var constraintChecker: ConstraintChecker
    @Inject lateinit var rh: ResourceHelper
    @Inject lateinit var defaultValueHelper: DefaultValueHelper
    @Inject lateinit var profileFunction: ProfileFunction
    @Inject lateinit var commandQueue: CommandQueue
    @Inject lateinit var activePlugin: ActivePlugin
    @Inject lateinit var ctx: Context
    @Inject lateinit var repository: AppRepository
    @Inject lateinit var config: Config
    @Inject lateinit var bolusTimer: BolusTimer
    @Inject lateinit var uel: UserEntryLogger
    //test
    //@Inject lateinit var GlucoseStatusCopy: GlucoseStatus
    //var GlucoseStatusCopy: GlucoseStatus
    //@Inject lateinit var mealData: MealData
    companion object {

        private const val PLUS1_DEFAULT = 0.5
        private const val PLUS2_DEFAULT = 1.0
        private const val PLUS3_DEFAULT = 2.0
    }

    private val disposable = CompositeDisposable()

    private val textWatcher: TextWatcher = object : TextWatcher {
        override fun afterTextChanged(s: Editable) {
            validateInputs()
        }

        override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
        override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
    }

    private var _binding: DialogUamBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private fun validateInputs() {
        val maxInsulin = constraintChecker.getMaxBolusAllowed().value()
        if (binding.amount.value > maxInsulin) {
            binding.amount.value = 0.0
            ToastUtils.showToastInUiThread(context, rh.gs(R.string.bolusconstraintapplied))
        }
    }

    override fun onSaveInstanceState(savedInstanceState: Bundle) {
        super.onSaveInstanceState(savedInstanceState)
        savedInstanceState.putDouble("tsuDuration", binding.tsuPlusDuration.value)
        savedInstanceState.putDouble("amount", binding.amount.value)
    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        onCreateViewGeneral()
        _binding = DialogUamBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.tsuPlusDuration.setParams(savedInstanceState?.getDouble("tsuDuration")
                                       ?: 0.0, 0.0, Constants.MAX_PROFILE_SWITCH_DURATION, 30.0, DecimalFormat("0"), false, binding.okcancel.ok)

        val maxInsulin = constraintChecker.getMaxBolusAllowed().value()

        binding.amount.setParams(savedInstanceState?.getDouble("amount")
            ?: 0.0, 0.0, maxInsulin, activePlugin.activePump.pumpDescription.bolusStep, DecimalFormatter.pumpSupportedBolusFormat(activePlugin.activePump), false, binding.okcancel.ok, textWatcher)

        binding.plus05.text = sp.getDouble(rh.gs(R.string.key_UAM_button_increment_1), PLUS1_DEFAULT).toSignedString(activePlugin.activePump)
        binding.plus05.setOnClickListener {
            binding.amount.value = max(0.0, binding.amount.value
                + sp.getDouble(rh.gs(R.string.key_UAM_button_increment_1), PLUS1_DEFAULT))
            validateInputs()
        }
        binding.plus10.text = sp.getDouble(rh.gs(R.string.key_UAM_button_increment_2), PLUS2_DEFAULT).toSignedString(activePlugin.activePump)
        binding.plus10.setOnClickListener {
            binding.amount.value = max(0.0, binding.amount.value
                + sp.getDouble(rh.gs(R.string.key_UAM_button_increment_2), PLUS2_DEFAULT))
            validateInputs()
        }
        binding.plus20.text = sp.getDouble(rh.gs(R.string.key_UAM_button_increment_3), PLUS3_DEFAULT).toSignedString(activePlugin.activePump)
        binding.plus20.setOnClickListener {
            binding.amount.value = max(0.0, binding.amount.value
                + sp.getDouble(rh.gs(R.string.key_UAM_button_increment_3), PLUS3_DEFAULT))
            validateInputs()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        disposable.clear()
        _binding = null
    }

    override fun submit(): Boolean {
        if (_binding == null) return false
        val pumpDescription = activePlugin.activePump.pumpDescription
        val insulin = SafeParse.stringToDouble(binding.amount.text)
        val insulinAfterConstraints = constraintChecker.applyBolusConstraints(Constraint(insulin)).value()
        val actions: LinkedList<String?> = LinkedList()
        val units = profileFunction.getUnits()
        val unitLabel = if (units == GlucoseUnit.MMOL) rh.gs(R.string.mmol) else rh.gs(R.string.mgdl)
        val duration = binding.tsuPlusDuration.value.toInt()

        if (insulinAfterConstraints > 0) {
            actions.add(rh.gs(R.string.bolus) + ": " + DecimalFormatter.toPumpSupportedBolus(insulinAfterConstraints, activePlugin.activePump, rh).formatColor(rh, R.color.bolus))
            if (abs(insulinAfterConstraints - insulin) > pumpDescription.pumpType.determineCorrectBolusStepSize(insulinAfterConstraints))
                actions.add(rh.gs(R.string.bolusconstraintappliedwarn, insulin, insulinAfterConstraints).formatColor(rh, R.color.warning))
        }
        val eatingSoonTTDuration = defaultValueHelper.determineEatingSoonTTDuration() //TODO: REMOVE
        val eatingSoonTT = defaultValueHelper.determineEatingSoonTT()

        val time = dateUtil.now()

        val notes = binding.notesLayout.notes.text.toString()
        if (notes.isNotEmpty())
            actions.add(rh.gs(R.string.notes_label) + ": " + notes)

        if (insulinAfterConstraints > 0) {
            activity?.let { activity ->
                OKDialog.showConfirmation(activity, rh.gs(R.string.uam_mode), HtmlHelper.fromHtml(Joiner.on("<br/>").join(actions)), {//MP: String is header string of confirmation window if there is a prebolus
                    /*if (eatingSoonChecked) {
                        uel.log(Action.TT, Sources.UAMDialog, //TODO: CHECK
                            notes,
                            ValueWithUnit.TherapyEventTTReason(TemporaryTarget.Reason.EATING_SOON),
                            ValueWithUnit.fromGlucoseUnit(eatingSoonTT, units.asText),
                            ValueWithUnit.Minute(eatingSoonTTDuration))
                        disposable += repository.runTransactionForResult(InsertAndCancelCurrentTemporaryTargetTransaction(
                            timestamp = System.currentTimeMillis(),
                            duration = TimeUnit.MINUTES.toMillis(eatingSoonTTDuration.toLong()),
                            reason = TemporaryTarget.Reason.EATING_SOON,
                            lowTarget = Profile.toMgdl(eatingSoonTT, profileFunction.getUnits()),
                            highTarget = Profile.toMgdl(eatingSoonTT, profileFunction.getUnits())
                        )).subscribe({ result ->
                            result.inserted.forEach { aapsLogger.debug(LTag.DATABASE, "Inserted temp target $it") }
                            result.updated.forEach { aapsLogger.debug(LTag.DATABASE, "Updated temp target $it") }
                        }, {
                            aapsLogger.error(LTag.DATABASE, "Error while saving temporary target", it)
                        })
                    }*/
                    disposable += repository.runTransactionForResult(TsunamiModeSwitchTransaction(
                        timestamp = System.currentTimeMillis(),
                        duration = TimeUnit.MINUTES.toMillis(duration.toLong()),
                        tsunamiMode = 2
                    )).subscribe({ result ->
                                     result.inserted.forEach { aapsLogger.debug(LTag.DATABASE, "Inserted tsunami mode $it") }
                                     result.updated.forEach { aapsLogger.debug(LTag.DATABASE, "Updated tsunami mode $it") }
                                 }, {
                                     aapsLogger.error(LTag.DATABASE, "Error while saving Tsunami mode.", it)
                                 })
                    if (insulinAfterConstraints > 0) {
                        val detailedBolusInfo = DetailedBolusInfo()
                        detailedBolusInfo.eventType = DetailedBolusInfo.EventType.CORRECTION_BOLUS
                        detailedBolusInfo.insulin = insulinAfterConstraints
                        detailedBolusInfo.context = context
                        detailedBolusInfo.notes = notes
                        detailedBolusInfo.timestamp = time
                            uel.log(Action.BOLUS, Sources.UAMDialog, //TODO: CHECK
                                notes,
                                ValueWithUnit.Insulin(insulinAfterConstraints))
                            commandQueue.bolus(detailedBolusInfo, object : Callback() {
                                override fun run() {
                                    if (!result.success) {
                                        ErrorHelperActivity.runAlarm(ctx, result.comment, rh.gs(R.string.treatmentdeliveryerror), R.raw.boluserror)
                                    } else {
                                        bolusTimer.removeBolusReminder()
                                    }
                                }
                            })
                    }
                })
            }
        } else {
            if (duration > 0) {
                activity?.let { activity ->
                    OKDialog.showConfirmation(activity, rh.gs(R.string.uam_mode), rh.gs(R.string.tsunami_button_no_insulin_message), Runnable {//MP: String is header string of confirmation window if there is a prebolus
                        disposable += repository.runTransactionForResult(TsunamiModeSwitchTransaction(
                            timestamp = System.currentTimeMillis(),
                            duration = TimeUnit.MINUTES.toMillis(duration.toLong()),
                            tsunamiMode = 0
                        )).subscribe({ result ->
                                         result.inserted.forEach { aapsLogger.debug(LTag.DATABASE, "Inserted tsunami mode $it") }
                                         result.updated.forEach { aapsLogger.debug(LTag.DATABASE, "Updated tsunami mode $it") }
                                     }, {
                                         aapsLogger.error(LTag.DATABASE, "Error while saving Tsunami mode.", it)
                                     })
                    })
                }
            } else {
                activity?.let { activity ->
                    OKDialog.show(activity, rh.gs(R.string.uam_mode), rh.gs(R.string.no_action_selected))
                }
            }
        }
        return true
    }
}