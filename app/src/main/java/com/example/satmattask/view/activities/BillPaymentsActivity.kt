package com.example.satmattask.view.activities

import android.content.res.ColorStateList
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.satmattask.R
import com.example.satmattask.adapter.AllServicesAdapter
import com.example.satmattask.adapter.BillPaymentsAdapter
import com.example.satmattask.databinding.ActivityBillPaymentsBinding
import com.example.satmattask.model.AllServices
import com.example.satmattask.model.BillPaymentsService

class BillPaymentsActivity : AppCompatActivity() {
    private lateinit var binding: ActivityBillPaymentsBinding
    private lateinit var billPaymentsAdapter: BillPaymentsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityBillPaymentsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        statusBar()

        prepareAdapters()
        setUpBillPaymentsService()

        binding.billPaymentsToolbar.setNavigationOnClickListener {
            finish()
        }

//        binding.aa.backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(this, R.color.black))
    }

    private fun setUpBillPaymentsService() {
        val billPaymentsServiceList = arrayListOf<BillPaymentsService>()
        billPaymentsServiceList.clear()

        billPaymentsServiceList.add(
            BillPaymentsService(
                "101",
                "Mobile Prepaid",
                "https://firebasestorage.googleapis.com/v0/b/satmat-task.appspot.com/o/App_related_icons%2Fsmartphone.png?alt=media&token=8dbae4c4-7598-4311-a347-102a6e13c933",
                R.color.colorPrimaryStatusBar
            )
        )

        billPaymentsServiceList.add(
            BillPaymentsService(
                "102",
                "Mobile Postpaid",
                "https://firebasestorage.googleapis.com/v0/b/satmat-task.appspot.com/o/App_related_icons%2Fsmartphone.png?alt=media&token=8dbae4c4-7598-4311-a347-102a6e13c933",
                R.color.colorPrimaryStatusBar
            )
        )

        billPaymentsServiceList.add(
            BillPaymentsService(
                "103",
                "Dth",
                "https://firebasestorage.googleapis.com/v0/b/satmat-task.appspot.com/o/App_related_icons%2Fonline%20(1).png?alt=media&token=e6da248b-8ae5-4ca0-a008-bec5788848bb",
                R.color.browsePlan_color
            )
        )

        billPaymentsServiceList.add(
            BillPaymentsService(
                "104",
                "Electricity",
                "https://firebasestorage.googleapis.com/v0/b/satmat-task.appspot.com/o/App_related_icons%2Fflash.png?alt=media&token=71856a3c-273b-49f0-9c88-0586b323feea",
                R.color.red_bg_color
            )
        )

        billPaymentsServiceList.add(
            BillPaymentsService(
                "105",
                "LPG Cylinder",
                "https://firebasestorage.googleapis.com/v0/b/satmat-task.appspot.com/o/App_related_icons%2Fgas-cylinder.png?alt=media&token=3ad6bff9-4bac-46c8-930c-224d76c08a78",
                R.color.brown_bg_color
            )
        )

        billPaymentsServiceList.add(
            BillPaymentsService(
                "106",
                "Fastag",
                "https://firebasestorage.googleapis.com/v0/b/satmat-task.appspot.com/o/App_related_icons%2Fbarrier.png?alt=media&token=648aae14-e78f-4076-81e4-dd5f9591277b",
                R.color.lightgreen_bg_color
            )
        )

        billPaymentsServiceList.add(
            BillPaymentsService(
                "107",
                "Water",
                "https://firebasestorage.googleapis.com/v0/b/satmat-task.appspot.com/o/App_related_icons%2Ffaucet.png?alt=media&token=8b69929a-3e15-4716-8903-bd31c7e31a91",
                R.color.skyblue_bg_color
            )
        )

        billPaymentsServiceList.add(
            BillPaymentsService(
                "108",
                "Broadband",
                "https://firebasestorage.googleapis.com/v0/b/satmat-task.appspot.com/o/App_related_icons%2Fwifi-router.png?alt=media&token=d54bbf47-7647-47f2-83d6-5acce828e170",
                R.color.black
            )
        )

        billPaymentsServiceList.add(
            BillPaymentsService(
                "109",
                "Insurance",
                "https://firebasestorage.googleapis.com/v0/b/satmat-task.appspot.com/o/App_related_icons%2Finsurance.png?alt=media&token=8071b574-ceaa-423b-b8d4-b19369fe14db",
                R.color.green_bg_color
            )
        )

        billPaymentsServiceList.add(
            BillPaymentsService(
                "110",
                "Piped Gas",
                "https://firebasestorage.googleapis.com/v0/b/satmat-task.appspot.com/o/App_related_icons%2Fflame.png?alt=media&token=40ec73b6-925b-42bf-a81b-0f98027ca3b6",
                R.color.purple_bg_color
            )
        )

        billPaymentsServiceList.add(
            BillPaymentsService(
                "111",
                "Loan Repay",
                "https://firebasestorage.googleapis.com/v0/b/satmat-task.appspot.com/o/App_related_icons%2Floan.png?alt=media&token=cffb5e02-ace5-429c-98ec-9479ccb38f89",
                R.color.blue_bg_color
            )
        )

        billPaymentsServiceList.add(
            BillPaymentsService(
                "112",
                "Municipal Taxes",
                "https://firebasestorage.googleapis.com/v0/b/satmat-task.appspot.com/o/App_related_icons%2Fcity-hall.png?alt=media&token=fecfebf0-f626-4bf3-b1f6-b4d1b085bc25",
                R.color.darkgreen_bg_color
            )
        )

        billPaymentsServiceList.add(
            BillPaymentsService(
                "113",
                "Landline",
                "https://firebasestorage.googleapis.com/v0/b/satmat-task.appspot.com/o/App_related_icons%2Ftelephone.png?alt=media&token=2d77bd8d-d635-4e0d-a958-a9118adbeb08",
                R.color.lightgreen_bg_color
            )
        )

        billPaymentsServiceList.add(
            BillPaymentsService(
                "114",
                "EMI",
                "https://firebasestorage.googleapis.com/v0/b/satmat-task.appspot.com/o/App_related_icons%2Fmoney-bag.png?alt=media&token=5e33c4b0-174b-418f-83bb-1a56dbc7c7a5",
                R.color.darkgreen_bg_color
            )
        )

        billPaymentsServiceList.add(
            BillPaymentsService(
                "115",
                "EMI Payment",
                "https://firebasestorage.googleapis.com/v0/b/satmat-task.appspot.com/o/App_related_icons%2Fcapitol.png?alt=media&token=93603cd7-a4b5-4c63-bab0-917ddc590200",
                R.color.orange_bg_color
            )
        )
        billPaymentsAdapter.differ.submitList(billPaymentsServiceList)
    }

    private fun prepareAdapters() {
        billPaymentsAdapter = BillPaymentsAdapter()
        binding.billPaymentsRV.apply {
            layoutManager = GridLayoutManager(this@BillPaymentsActivity, 3)
            adapter = billPaymentsAdapter
        }
    }

    private fun statusBar() {
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    private fun RecyclerItemClicked(itemId: String, msg: String) {

    }
}