package com.example.satmattask.view.fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.denzcoskun.imageslider.constants.ScaleTypes
import com.denzcoskun.imageslider.models.SlideModel
import com.example.satmattask.R
import com.example.satmattask.adapter.AllServicesAdapter
import com.example.satmattask.api.ApiInterface
import com.example.satmattask.databinding.FragmentHomeBinding
import com.example.satmattask.model.AllServices
import com.example.satmattask.repository.ServiceRepository
import com.example.satmattask.utils.Utils
import com.example.satmattask.view.activities.BillPaymentsActivity
import com.example.satmattask.view.activities.DthRechargeActivity
import com.example.satmattask.view.activities.MobileRechargeActivity
import com.example.satmattask.view.activities.TopUpActivity
import com.example.satmattask.viewmodel.ServiceViewModel
import com.example.satmattask.viewmodel.ServiceViewModelFactory


class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding
    private lateinit var fastServicesAdapter: AllServicesAdapter
    private lateinit var bankingServiceAdapter: AllServicesAdapter
    private lateinit var utilityServiceAdapter: AllServicesAdapter
    private lateinit var travelServiceAdapter: AllServicesAdapter
    private lateinit var eGovernanceServiceAdapter: AllServicesAdapter

    private lateinit var repository: ServiceRepository
    private lateinit var viewModel: ServiceViewModel


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(layoutInflater)

        //Initializing Retrofit Interface and ViewModel.
        val ApiInterface = Utils.retrofitInstance.create(ApiInterface::class.java)
        repository = ServiceRepository(ApiInterface, requireContext())
        viewModel =
            ViewModelProvider(
                this,
                ServiceViewModelFactory(repository)
            ).get(ServiceViewModel::class.java)

        binding.toolbar.setNavigationIcon(R.drawable.ic_profile_icon)
        binding.toolbar.inflateMenu(R.menu.toolbar_menu);


        toolBarItemClick()
        imageSlider()
        prepareAdapters()
        setUpServices()
        setUpBankingServices()
        setUpUtilityServices()
        setUpTravelServices()
        setUpeGovernanceServices()
        return binding.root
    }

    private fun toolBarItemClick() {
        binding.toolbar.setOnMenuItemClickListener { item ->
            when (item.itemId) {
                R.id.toolbar_refresh -> {
                    Utils.showToast(requireContext(), "Refresh")
                }

                R.id.toolbar_volume -> {
                    Utils.showToast(requireContext(), "Volume")
                }

                R.id.toolbar_notifications -> {
                    Utils.showToast(requireContext(), "Notifications")
                }
            }
            false
        }
    }

    private fun setUpeGovernanceServices() {
        val eGovernanceServiceList = arrayListOf<AllServices>()
        eGovernanceServiceList.clear()

        eGovernanceServiceList.add(
            AllServices(
                2,
                "41",
                "PAN UTI",
                "https://firebasestorage.googleapis.com/v0/b/satmat-task.appspot.com/o/App_related_icons%2Futipan.png?alt=media&token=363e4acb-5068-45af-8d2e-9267aa847e53"
            )
        )
        eGovernanceServiceList.add(
            AllServices(
                2,
                "42",
                "PAN NSDL",
                "https://firebasestorage.googleapis.com/v0/b/satmat-task.appspot.com/o/App_related_icons%2FNSDL-696x403.png?alt=media&token=0248efc0-dff2-4bab-8f7f-cfe27cb1f43d"
            )

        )
        eGovernanceServiceAdapter.differ.submitList(eGovernanceServiceList)
    }

    private fun setUpTravelServices() {
        val travelServiceList = arrayListOf<AllServices>()
        travelServiceList.clear()

        travelServiceList.add(
            AllServices(
                2,
                "31",
                "Bus Booking",
                "https://firebasestorage.googleapis.com/v0/b/satmat-task.appspot.com/o/App_related_icons%2Ffront-of-bus.png?alt=media&token=4fdd65ae-9dd9-44a7-b5ee-ab5b704814a6"
            )
        )
        travelServiceList.add(
            AllServices(
                2,
                "32",
                "Train Booking",
                "https://firebasestorage.googleapis.com/v0/b/satmat-task.appspot.com/o/App_related_icons%2Ftrain.png?alt=media&token=41999e3e-ff60-4db8-b97d-a50b5d49acdf"
            )

        )
        travelServiceList.add(
            AllServices(
                2,
                "33",
                "Flight Booking",
                "https://firebasestorage.googleapis.com/v0/b/satmat-task.appspot.com/o/App_related_icons%2Faeroplane.png?alt=media&token=8a015855-3788-4a1b-855d-6aee6145c2e1"
            )
        )
        travelServiceAdapter.differ.submitList(travelServiceList)
    }

    private fun setUpUtilityServices() {
        val utilityServiceList = arrayListOf<AllServices>()
        utilityServiceList.clear()

        utilityServiceList.add(
            AllServices(
                2,
                "21",
                "DTH Recharge",
                "https://firebasestorage.googleapis.com/v0/b/satmat-task.appspot.com/o/App_related_icons%2Fonline%20(1).png?alt=media&token=113692f7-05b7-468b-be72-41234456a3b7"
            )
        )

        utilityServiceList.add(
            AllServices(
                2,
                "22",
                "Mobile Recharge",
                "https://firebasestorage.googleapis.com/v0/b/satmat-task.appspot.com/o/App_related_icons%2Fcell-phone.png?alt=media&token=1e85428a-a727-4809-aff3-e8a5aada761d"
            )

        )
        utilityServiceList.add(
            AllServices(
                2,
                "23",
                "LIC Payment",
                "https://firebasestorage.googleapis.com/v0/b/satmat-task.appspot.com/o/App_related_icons%2Fpngegg%20(1).png?alt=media&token=7e78d3d4-a2bf-422a-bfef-130787e089af"
            )
        )
        utilityServiceList.add(
            AllServices(
                2,
                "24",
                "Electricity",
                "https://firebasestorage.googleapis.com/v0/b/satmat-task.appspot.com/o/App_related_icons%2Fflash.png?alt=media&token=2ad515d3-b1ab-48cd-a3ca-4e933742f84f"
            )
        )
        utilityServiceList.add(
            AllServices(
                2,
                "25",
                "Credit Card Payment",
                "https://firebasestorage.googleapis.com/v0/b/satmat-task.appspot.com/o/App_related_icons%2Fcredit-card.png?alt=media&token=0d713dd3-ace9-45c7-8f90-9e67dde255f3"
            )
        )
        utilityServiceList.add(
            AllServices(
                2,
                "26",
                "CC to CC Payment",
                "https://firebasestorage.googleapis.com/v0/b/satmat-task.appspot.com/o/App_related_icons%2Fcredit-card.png?alt=media&token=0d713dd3-ace9-45c7-8f90-9e67dde255f3"
            )
        )
        utilityServiceList.add(
            AllServices(
                2,
                "27",
                "Manage Cash",
                "https://firebasestorage.googleapis.com/v0/b/satmat-task.appspot.com/o/App_related_icons%2Fanalysis.png?alt=media&token=4cce9a63-57c2-4427-a833-1c26f0e91a91"
            )
        )
        utilityServiceList.add(
            AllServices(
                2,
                "28",
                "Pan Card Service",
                "https://firebasestorage.googleapis.com/v0/b/satmat-task.appspot.com/o/App_related_icons%2Fcredit-card%20(2).png?alt=media&token=649f4e6a-5145-430e-b870-769d1d2f595c"
            )
        )
        utilityServiceList.add(
            AllServices(
                2,
                "29",
                "Fastag Payment",
                "https://firebasestorage.googleapis.com/v0/b/satmat-task.appspot.com/o/App_related_icons%2Ftoll-road.png?alt=media&token=a60e86e2-4c47-401a-97dc-ef4de8299cdd"
            )
        )

        utilityServiceList.add(
            AllServices(
                2,
                "30",
                "Bill Payments",
                "https://firebasestorage.googleapis.com/v0/b/satmat-task.appspot.com/o/App_related_icons%2Fbold-text.png?alt=media&token=6f2c8e81-8e41-43e3-8cb8-4b18f330959c"
            )
        )
        utilityServiceAdapter.differ.submitList(utilityServiceList)
    }

    private fun setUpBankingServices() {
        val bankingServiceList = arrayListOf<AllServices>()
        bankingServiceList.clear()

        bankingServiceList.add(
            AllServices(
                2,
                "11",
                "Cash Withdrawl",
                "https://firebasestorage.googleapis.com/v0/b/satmat-task.appspot.com/o/App_related_icons%2Fcash-withdrawal.png?alt=media&token=54ae94ab-7c29-414a-b089-6e4897d3910e"
            )
        )

        bankingServiceList.add(
            AllServices(
                2,
                "12",
                "Bank Statement",
                "https://firebasestorage.googleapis.com/v0/b/satmat-task.appspot.com/o/App_related_icons%2Fbank-statement.png?alt=media&token=50c322a0-d1a5-4013-8694-f6adf74f0612"
            )
        )
        bankingServiceList.add(
            AllServices(
                2,
                "13",
                "Balance Check",
                "https://firebasestorage.googleapis.com/v0/b/satmat-task.appspot.com/o/App_related_icons%2Fpngegg.png?alt=media&token=96708cae-ed23-4396-81a5-080f39aae75b"
            )
        )
        bankingServiceList.add(
            AllServices(
                2,
                "14",
                "Aadhaar Pay",
                "https://firebasestorage.googleapis.com/v0/b/satmat-task.appspot.com/o/App_related_icons%2FAadhar-Color.png?alt=media&token=ed4e26ba-840f-4293-b41a-01fa02e69d7b"
            )
        )
        bankingServiceList.add(
            AllServices(
                2,
                "15",
                "DMT Payment",
                "https://firebasestorage.googleapis.com/v0/b/satmat-task.appspot.com/o/App_related_icons%2Fbitcoin.png?alt=media&token=c3913780-ec32-48e1-b0b0-075d8d638263"
            )
        )
        bankingServiceList.add(
            AllServices(
                2,
                "16",
                "Loans",
                "https://firebasestorage.googleapis.com/v0/b/satmat-task.appspot.com/o/App_related_icons%2Fcapital.png?alt=media&token=77ee0ea0-be34-41a9-8787-fd1b718f20ab"
            )
        )
        bankingServiceList.add(
            AllServices(
                2,
                "17",
                "Axis Bank Ac Open",
                "https://firebasestorage.googleapis.com/v0/b/satmat-task.appspot.com/o/App_related_icons%2Fpngwing.com.png?alt=media&token=4b51075a-de67-4b9c-a498-de94a8372f9d"
            )
        )
        bankingServiceList.add(
            AllServices(
                2,
                "18",
                "MATM",
                "https://firebasestorage.googleapis.com/v0/b/satmat-task.appspot.com/o/App_related_icons%2Fatm-machine.png?alt=media&token=c01530c8-4a0a-4224-b6bb-cdfb1d6154e6"
            )
        )
        bankingServiceAdapter.differ.submitList(bankingServiceList)
    }

    private fun imageSlider() {
        val imageList = ArrayList<SlideModel>() // Create image list
        imageList.add(
            SlideModel(
                "https://firebasestorage.googleapis.com/v0/b/satmat-task.appspot.com/o/App_related_icons%2Frecharge.jpg?alt=media&token=a7f3eb24-eb48-459d-aec1-f1c54608ef4e",
                "",
                ScaleTypes.FIT
            )
        )
        imageList.add(
            SlideModel(
                "https://firebasestorage.googleapis.com/v0/b/satmat-task.appspot.com/o/App_related_icons%2Fticket.jpg?alt=media&token=9aecf236-2773-4c17-8c70-b051081f99c2",
                "", ScaleTypes.FIT
            )
        )
        imageList.add(
            SlideModel(
                "https://firebasestorage.googleapis.com/v0/b/satmat-task.appspot.com/o/App_related_icons%2Ffastag.jpeg?alt=media&token=56938356-a5fa-44c2-9fba-8e3e8d3bd6eb",
                "",
                ScaleTypes.FIT
            )
        )

        binding.imageSlider.setImageList(imageList)
    }

    private fun prepareAdapters() {
        fastServicesAdapter = AllServicesAdapter(::RecyclerItemClicked)
        binding.fragmentHomeFastServicesRV.apply {
            layoutManager =
                GridLayoutManager(requireContext(), 4)
            adapter = fastServicesAdapter
        }

        bankingServiceAdapter = AllServicesAdapter(::RecyclerItemClicked)
        binding.fragmentHomeBankingRV.apply {
            layoutManager =
                GridLayoutManager(requireContext(), 4)
            adapter = bankingServiceAdapter
        }

        utilityServiceAdapter = AllServicesAdapter(::RecyclerItemClicked)
        binding.fragmentHomeUtilityRV.apply {
            layoutManager =
                GridLayoutManager(requireContext(), 4)
            adapter = utilityServiceAdapter
        }


        travelServiceAdapter = AllServicesAdapter(::RecyclerItemClicked)
        binding.fragmentHomeTravelServicesRV.apply {
            layoutManager =
                GridLayoutManager(requireContext(), 4)
            adapter = travelServiceAdapter
        }


        eGovernanceServiceAdapter = AllServicesAdapter(::RecyclerItemClicked)
        binding.fragmentHomeEGovernanceRV.apply {
            layoutManager =
                GridLayoutManager(requireContext(), 2)
            adapter = eGovernanceServiceAdapter
        }
    }

    private fun setUpServices() {
        val serviceList = arrayListOf<AllServices>()
        serviceList.clear()
        serviceList.add(
            AllServices(
                1,
                "1",
                "Topup",
                "topup.jpg"
            )
        )

        serviceList.add(AllServices(1, "2", "Payout", "payout.jpg"))

        serviceList.add(AllServices(1, "3", "My QR\nCode", "myqr.jpg"))

        serviceList.add(AllServices(1, "4", "Activate \n Services", "activateservice.jpg"))

        fastServicesAdapter.differ.submitList(serviceList)
    }

    private fun RecyclerItemClicked(itemId: String, msg: String) {
        when (itemId) {
            "22" ->
                startActivity(
                    Intent(
                        this.context,
                        MobileRechargeActivity::class.java
                    )
                )

            "21" ->
                startActivity(
                    Intent(
                        this.context,
                        DthRechargeActivity::class.java
                    )
                )

            "30" ->
                startActivity(
                    Intent(
                        this.context,
                        BillPaymentsActivity::class.java
                    )
                )

            "1" ->
                startActivity(
                    Intent(
                        this.context,
                        TopUpActivity::class.java
                    )
                )

            "18" ->{}
//                startActivity(
//                    Intent(
//                        this.context,
//                        MicroAtmActivity::class.java
//                    )
//                )

            else ->
                Utils.showToast(activity?.applicationContext!!, msg + " Clicked")
        }


//        if (itemId == "22") {
////            viewModel.doRecharge("9876543210", "1234", "1234", "998988200")
////            viewModel.rechargeResponse.observe(this, Observer {
////                when (it) {
////                    is Response.Success -> {
////                        Utils.showToast(requireContext(), "API Works fine")
////                    }
////
////                    is Response.Error -> {
////                        Utils.showToast(requireContext(), it.error?.MESSAGE.toString())
////                    }
////                }
////            })
//            startActivity(
//                Intent(
//                    this.context,
//                    MobileRechargeActivity::class.java
//                )
//            )
//        } else if (itemId == "21") {
//            startActivity(
//                Intent(
//                    this.context,
//                    DthRechargeActivity::class.java
//                )
//            )
//        } else if (itemId == "30") {
//            startActivity(
//                Intent(
//                    this.context,
//                    BillPaymentsActivity::class.java
//                )
//            )
//        } else {
//            Utils.showToast(activity?.applicationContext!!, msg + " Clicked")
//        }
    }

}