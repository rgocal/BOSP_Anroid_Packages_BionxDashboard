package com.bionx.res.fragments;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bionx.res.R;
import com.bionx.res.util.Constants;
import com.bionx.res.util.Helpers;

public class CPUInfo extends Fragment implements Constants {

    private TextView mKernelInfo;
    private TextView mCPUInfo;
    private TextView mMemInfo;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup root, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.cpu_info, root, false);
        mKernelInfo = (TextView) view.findViewById(R.id.kernel_info);
        mCPUInfo = (TextView) view.findViewById(R.id.cpu_info);
        mMemInfo = (TextView) view.findViewById(R.id.mem_info);
        updateData();
        return view;
    }
    @Override
    public void onResume() {
        super.onResume();
    }
    public void updateData() {
        mKernelInfo.setText("");
        mCPUInfo.setText("");
        mMemInfo.setText("");
        readFile(mKernelInfo, KERNEL_INFO_PATH);
        if (new File(PFK_VER).exists()) {
            mKernelInfo.append("\n");
            mKernelInfo.append(getString(R.string.pfk_info,Helpers.readOneLine(PFK_VER)));
            mKernelInfo.append("\n");
        }
        if (new File(DYNAMIC_DIRTY_WRITEBACK_PATH).exists()) {
            mKernelInfo.append("\n");
            mKernelInfo.append(getString(R.string.dynamic_writeback_info));
            mKernelInfo.append("\n");
        }
        if (new File(DSYNC_PATH).exists()) {
            mKernelInfo.append("\n");
            mKernelInfo.append(getString(R.string.dsync_info));
            mKernelInfo.append("\n");
        }
        if (new File(BLX_PATH).exists()) {
            mKernelInfo.append("\n");
            mKernelInfo.append(getString(R.string.blx_info));
            mKernelInfo.append("\n");
        }
        readFile(mCPUInfo, CPU_INFO_PATH);
        readFile(mMemInfo, MEM_INFO_PATH);
    }

    public void readFile(TextView tView, String fName) {
        FileReader fr = null;
        try {
            fr = new FileReader(fName);
            BufferedReader br = new BufferedReader(fr);
            String line = br.readLine();
            while (null != line) {
                tView.append(line);
                tView.append("\n");
                line = br.readLine();
            }
        } catch (IOException ex) {
        } finally {
            if (null != fr) {
                try {
                    fr.close();
                } catch (IOException e) {
                }
            }
        }
    }
}
