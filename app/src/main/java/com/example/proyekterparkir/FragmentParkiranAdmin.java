package com.example.proyekterparkir;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;

public class FragmentParkiranAdmin extends Fragment {

    private TabLayout tabLayout;
    private ViewPager viewPager;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_parkiran_admin, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Inisialisasi komponen dari layout
        tabLayout = view.findViewById(R.id.tableLayout);
        viewPager = view.findViewById(R.id.viewPagerAdmin);

        tabLayout.setupWithViewPager(viewPager);

        // Setup ViewPager dengan FragmentPagerAdapter
        VPAdapter vpAdapter = new VPAdapter(requireActivity().getSupportFragmentManager(), FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        vpAdapter.addFragment(new PetaParkiran1Fragment(), "Parkiran 1");
        vpAdapter.addFragment(new PetaParkiran2Fragment(), "Parkiran 2");
        vpAdapter.addFragment(new PetaParkiran3Fragment(), "Parkiran 3");
        viewPager.setAdapter(vpAdapter);

    }
}
