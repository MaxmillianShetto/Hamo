package com.dpsd.hamo.view.ui.home;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dpsd.hamo.R;
import com.dpsd.hamo.databinding.FragmentCommunityRepHomeBinding;
import com.dpsd.hamo.databinding.FragmentGiverHomeBinding;
import com.google.android.gms.maps.MapView;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CommunityRepHomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CommunityRepHomeFragment extends Fragment
{

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private @NonNull FragmentCommunityRepHomeBinding binding;

    public MapView mapView;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public CommunityRepHomeFragment()
    {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment CommunityRepHomeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static CommunityRepHomeFragment newInstance(String param1, String param2)
    {
        CommunityRepHomeFragment fragment = new CommunityRepHomeFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        if (getArguments() != null)
        {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        binding = FragmentCommunityRepHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        mapView = binding.mapView;
        return root;
    }
}