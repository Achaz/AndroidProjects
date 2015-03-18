package com.nemah.net.util;

import java.util.ArrayList;
import java.util.List;

import com.nemah.net.Credit;
import com.nemah.net.Customers;
import com.nemah.net.Expenditure;
import com.nemah.net.Login;
import com.nemah.net.MainActivity;
import com.nemah.net.Purchases;
import com.nemah.net.R;
import com.nemah.net.Register;
import com.nemah.net.Sales;
import com.nemah.net.Supplier;
import com.nemah.net.UserBusiness;

import android.app.Fragment;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;

public class SlidingMenuFragment extends Fragment implements OnChildClickListener{
	
	private ExpandableListView sectionListView;
	
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
       
        List<Section> sectionList = createMenu();
               
        View view = inflater.inflate(R.layout.slidingmenu_fragment, container, false);
        this.sectionListView = (ExpandableListView) view.findViewById(R.id.slidingmenu_view);
        this.sectionListView.setGroupIndicator(null);
       
        SectionListAdapter sectionListAdapter = new SectionListAdapter(this.getActivity(), sectionList);
        this.sectionListView.setAdapter(sectionListAdapter);
       
        this.sectionListView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
        	
              @Override
              public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
                return true;
              }
            });
       
        this.sectionListView.setOnChildClickListener(this);
       
        int count = sectionListAdapter.getGroupCount();
        
        for (int position = 0; position < count; position++) {
        	
            this.sectionListView.expandGroup(position);
        }
       
        return view;
    }

    private List<Section> createMenu() {
    	
        List<Section> sectionList = new ArrayList<Section>();

        Section oDemoSection = new Section("Admin");
        oDemoSection.addSectionItem(101, "Register", "ic_ab_post");
        oDemoSection.addSectionItem(102, "Settings", "setting_white_icon");
        oDemoSection.addSectionItem(103, "Home", "ic_action_home");
        oDemoSection.addSectionItem(104, "LogOut", "gnome_session_logout");
       
        Section oGeneralSection = new Section("Activity");
        oGeneralSection.addSectionItem(201, "Purchases", "estrella");
        oGeneralSection.addSectionItem(202, "Sales", "estrella");
        oGeneralSection.addSectionItem(203, "Credit", "estrella");
        oGeneralSection.addSectionItem(204, "Expenditure", "estrella");
        oGeneralSection.addSectionItem(205, "Customers", "estrella");
        oGeneralSection.addSectionItem(206, "Suppliers", "estrella");

        sectionList.add(oDemoSection);
        sectionList.add(oGeneralSection);
        return sectionList;
    }
    public void signOut() {
    	
        Intent intent = new Intent(getActivity(), Login.class);

    	if(Build.VERSION.SDK_INT >= 11){
    		
    	 intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_CLEAR_TASK);
    	 
    	}else{
    		
    	 intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TOP);
    	 
    	}
    	startActivity(intent);
    }

    @Override
    public boolean onChildClick(ExpandableListView parent, View v,
            int groupPosition, int childPosition, long id) {

        switch ((int)id) {
        
        case 101:
            //TODO

        	Intent intent = new Intent(getActivity(),Register.class);
        	startActivity(intent);
        	
            break;
            
        case 102:
            //TODO

        	Intent settings = new Intent(getActivity(),UserBusiness.class);
        	startActivity(settings);
            break;
            
        case 103:
        	
        	  Intent home = new Intent(getActivity(), MainActivity.class);
        	  startActivity(home);
        	
            break;
        case 104:
        	
        	signOut();
        	
            break;
        case 201:
            //TODO

            Intent mobile = new Intent(getActivity(),Purchases.class);
            startActivity(mobile);
            
            break;
            
        case 202:
            //TODO

        	Intent electronics = new Intent(getActivity(), Sales.class);
        	startActivity(electronics);
        	
            break;
            
        case 203:
            //TODO

        	Intent homePage = new Intent(getActivity(),Credit.class);
        	startActivity(homePage);
        	
            break;
            
        case 204:
        	
            //TODO

        	Intent fashion = new Intent(getActivity(),Expenditure.class);
        	startActivity(fashion);
        
            break;
            
        case 205:

        	Intent hobbies = new Intent(getActivity(),Customers.class);
        	startActivity(hobbies);
        	
        	break;
        	
        case 206:

        	Intent Pets = new Intent(getActivity(),Supplier.class);
        	startActivity(Pets);
        	
        	break;
 
        }
       
        return false;
    }

}
