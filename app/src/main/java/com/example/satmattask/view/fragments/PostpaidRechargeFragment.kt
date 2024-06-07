package com.example.satmattask.view.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.satmattask.R
import com.example.satmattask.adapter.OperatorBottomSheetAdapter
import com.example.satmattask.adapter.RechargeBottomSheetAdapter
import com.example.satmattask.api.ApiInterface
import com.example.satmattask.databinding.FragmentPostpaidRechargeBinding
import com.example.satmattask.databinding.OperatorBottomsheetDialogBinding
import com.example.satmattask.databinding.RechargeplansBottomsheetDialogBinding
import com.example.satmattask.model.getOperators.Result
import com.example.satmattask.model.getRechargePlans.Record
import com.example.satmattask.repository.ResponseSealed
import com.example.satmattask.repository.ServiceRepository
import com.example.satmattask.utils.Utils
import com.example.satmattask.viewmodel.ServiceViewModel
import com.example.satmattask.viewmodel.ServiceViewModelFactory
import com.google.android.material.bottomsheet.BottomSheetDialog

class PostpaidRechargeFragment : Fragment() {
    private lateinit var binding: FragmentPostpaidRechargeBinding
    private lateinit var repository: ServiceRepository
    private lateinit var viewModel: ServiceViewModel
    private lateinit var bottomSheetOperatorsDialog: BottomSheetDialog
    private lateinit var bottomSheetRechargePlansDialog: BottomSheetDialog
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentPostpaidRechargeBinding.inflate(layoutInflater)

        val ApiInterface = Utils.retrofitInstance.create(ApiInterface::class.java)
        repository = ServiceRepository(ApiInterface, requireContext())
        viewModel =
            ViewModelProvider(
                this,
                ServiceViewModelFactory(repository)
            ).get(ServiceViewModel::class.java)


        viewModel.getOperatorResponse.observe(viewLifecycleOwner, Observer {
            Utils.hideDialog()
            when (it) {
                is ResponseSealed.Success -> {
                    if (it.data?.status == true) {
                        var operatorList = ArrayList<Result>()
                        operatorList.addAll(it.data.result)
                        openOperatorBottomSheet(operatorList)
                    } else {
                        Utils.showToast(requireContext(), it.data?.message!!)
                    }
                }

                is ResponseSealed.Error -> {
                    Utils.showToast(requireContext(), it.data?.message!!)
                }
            }

        })


        viewModel.getRechargePlansResponse.observe(viewLifecycleOwner, Observer {
            Utils.hideDialog()
            when (it) {
                is ResponseSealed.Success -> {
                    val planResult = it.data
                    if (planResult?.status == true) {
                        val planList = ArrayList<Record>()
                        planList.addAll(planResult.result?.records!!)
                        if (planList.size > 0) {
                            openPlansBottomSheet(planList)
                        } else {
                            Utils.showToast(requireContext(), "No Plans available!!")
                        }
                    } else {
                        Utils.showToast(requireContext(), it.data?.result?.operator.toString())
                    }
                }

                is ResponseSealed.Error -> {
                    Utils.showToast(requireContext(), it.error?.result?.operator.toString())
                }
            }
        })

        binding.apply {
            postPaidRechargeChooseOperator.setOnClickListener {
                Utils.showDialog(requireContext())
                viewModel.getOperators("mobile")
            }

            postPaidBrowsePlanBTN.setOnClickListener {
                val getPhone = postpaidRechargePhoneTV.text.toString()
                if (getPhone.length != 10) {
                    Utils.showToast(requireContext(), "Please check phone number!!")
                    return@setOnClickListener
                }

                val getOperator = postPaidRechargeChooseOperator.text.toString()
                if (getOperator.equals(getString(R.string.choose_operator))) {
                    Utils.showToast(requireContext(), "Please select an Operator!!")
                    return@setOnClickListener
                }

                viewModel.getRechargePlans(getOperator, getPhone)
                Utils.showDialog(requireContext())
            }
        }
        return binding.root
    }

    private fun openOperatorBottomSheet(operatorList: ArrayList<Result>) {
        bottomSheetOperatorsDialog = BottomSheetDialog(requireContext())
        val bottomView = LayoutInflater.from(requireContext()).inflate(
            com.example.satmattask.R.layout.operator_bottomsheet_dialog,
            null,
            false
        )
        val customViewBinding = OperatorBottomsheetDialogBinding.bind(bottomView)
        bottomSheetOperatorsDialog.setContentView(bottomView)

        val operatorBottomSheetAdapter = OperatorBottomSheetAdapter(::bottomSheetOperatorClicked)
        customViewBinding.dialogRV.apply {
            layoutManager = LinearLayoutManager(
                requireContext(),
                RecyclerView.VERTICAL, false
            )
            adapter = operatorBottomSheetAdapter
        }
        operatorBottomSheetAdapter.differ.submitList(operatorList)

        customViewBinding.dialogCancel.setOnClickListener {
            bottomSheetOperatorsDialog.dismiss()
        }
        bottomSheetOperatorsDialog.show()
    }

    private fun openPlansBottomSheet(planList: ArrayList<Record>) {
        bottomSheetRechargePlansDialog = BottomSheetDialog(requireContext())
        val bottomView = LayoutInflater.from(requireContext()).inflate(
            com.example.satmattask.R.layout.rechargeplans_bottomsheet_dialog,
            null,
            false
        )
        val customViewBinding = RechargeplansBottomsheetDialogBinding.bind(bottomView)
        bottomSheetRechargePlansDialog.setContentView(bottomView)

        val rechargePlansBottomSheetAdapter = RechargeBottomSheetAdapter(::selectedRecharge)
        customViewBinding.rechargePlanRV.apply {
            layoutManager = LinearLayoutManager(
                requireContext(),
                RecyclerView.VERTICAL, false
            )
            adapter = rechargePlansBottomSheetAdapter
        }
        rechargePlansBottomSheetAdapter.differ.submitList(planList)

        customViewBinding.rechargePlanCancel.setOnClickListener {
            bottomSheetRechargePlansDialog.dismiss()
        }
        bottomSheetRechargePlansDialog.show()
    }

    private fun bottomSheetOperatorClicked(result: Result) {
        if (bottomSheetOperatorsDialog != null) {
            bottomSheetOperatorsDialog.dismiss()
        }

        binding.postPaidRechargeChooseOperator.text = result.operatorname
        val operatorImageURL = Utils.BASE_URL + result.operator_image
        Glide.with(requireContext()).load(operatorImageURL)
            .into(binding.postPaidRechargeImage)
    }

    private fun selectedRecharge(amount : String)
    {
        if(bottomSheetRechargePlansDialog!=null)
        {
            bottomSheetRechargePlansDialog.dismiss()
        }
        binding.postpaidRechargeAmountTV.setText(amount.toString())
    }
}