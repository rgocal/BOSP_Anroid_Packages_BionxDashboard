package com.bionx.res.fragments;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import android.app.AlertDialog;
import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.view.ViewPager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.Switch;
import android.widget.TextView;

import com.bionx.res.R;
import com.bionx.res.util.Constants;
import com.bionx.res.util.Helpers;
import com.bionx.res.util.Voltage;

public class VoltageControlSettings extends Fragment implements Constants {

    private static final int NEW_MENU_ID=Menu.FIRST+1;
    public static final int DIALOG_EDIT_VOLT = 0;
    private List<Voltage> mVoltages;
    private ListAdapter mAdapter;
    SharedPreferences mPreferences;
    private Voltage mVoltage;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        mVoltages = getVolts(mPreferences);
        mAdapter = new ListAdapter(getActivity());

        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup root, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.voltage_settings, root, false);

        final ListView listView = (ListView) view.findViewById(R.id.ListView);
        final Switch setOnBoot = (Switch) view.findViewById(R.id.applyAtBoot);


        if (mVoltages.isEmpty()) {
            ((TextView) view.findViewById(R.id.emptyList)).setVisibility(View.VISIBLE);
            ((RelativeLayout) view.findViewById(R.id.BottomBar)).setVisibility(View.GONE);
        }

        setOnBoot.setChecked(mPreferences.getBoolean(VOLTAGE_SOB, false));
        setOnBoot
                .setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView,boolean isChecked) {
                        mPreferences.edit().putBoolean(VOLTAGE_SOB,isChecked).apply();
                        if (isChecked){
                            String warningMessage = getString(R.string.volt_info);

                            new AlertDialog.Builder(getActivity())
                                    .setMessage(warningMessage)
                                    .setNegativeButton(getString(R.string.cancel),
                                            new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog,int which) {
                                                    //mPreferences.edit().putBoolean(VOLTAGE_SOB,false).apply();
                                                    setOnBoot.setChecked(false);
                                                }
                                            })
                                    .setPositiveButton(getString(R.string.ok),
                                            new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog,int which) {
                                                    //mPreferences.edit().putBoolean(VOLTAGE_SOB,true).apply();
                                                }
                                            }).create().show();
                        }

                    }
                });

        ((Button) view.findViewById(R.id.applyBtn)).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
			final StringBuilder sb = new StringBuilder();
    		if (Helpers.getVoltagePath() == VDD_PATH) {		
				for (final Voltage volt : mVoltages) {
					if(volt.getSavedMV() != volt.getCurrentMv()){
						for (int i = 0; i < Helpers.getNumOfCpus(); i++) {
							sb.append("busybox echo "
								+ volt.getFreq()+" "+volt.getSavedMV() + " > "
								+ Helpers.getVoltagePath().replace("cpu0","cpu" + i) + " \n");
						}
					}
				}
			}
			else{
				final StringBuilder b = new StringBuilder();
				for (final Voltage volt : mVoltages) {
					b.append(volt.getSavedMV() + " ");
				}
				for (int i = 0; i < Helpers.getNumOfCpus(); i++) {
					sb.append("busybox echo "
					+ b.toString() + " > "
					+ Helpers.getVoltagePath().replace("cpu0","cpu" + i) + " \n");
				}
			}
			Helpers.shExec(sb);

			final List<Voltage> volts = getVolts(mPreferences);
			mVoltages.clear();
			mVoltages.addAll(volts);
			mAdapter.notifyDataSetChanged();
			}
		});

        mAdapter.setListItems(mVoltages);
        listView.setAdapter(mAdapter);
        listView.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,int position, long id) {
                mVoltage = mVoltages.get(position);
                showDialog(DIALOG_EDIT_VOLT);
            }
        });

        return view;
    }
    @Override
    public void onResume() {
        super.onResume();
    }
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.voltage_control_menu, menu);
        Helpers.addItems2Menu(menu,NEW_MENU_ID,getString(R.string.menu_tab),(ViewPager) getView().getParent());
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Helpers.removeCurItem(item,Menu.FIRST+1,(ViewPager) getView().getParent());
        switch (item.getItemId()){
        case R.id.volt_increase:
            IncreasebyStep(25);
            break;
        case R.id.volt_decrease:
            IncreasebyStep(-25);
            break;
        case R.id.reset:
            ResetVolt();
            break;
        }
        return true;
    }

    private void ResetVolt() {
	for (final Voltage volt : mVoltages) {
		SharedPreferences.Editor editor = mPreferences.edit();
                editor.remove(volt.getFreq()).commit();
	}
	final List<Voltage> volts = getVolts(mPreferences);
	mVoltages.clear();
	mVoltages.addAll(volts);
	mAdapter.notifyDataSetChanged();
    }
    private void IncreasebyStep(final int pas) {
	for (final Voltage volt : mVoltages) {
		String value=Integer.toString( Integer.parseInt(volt.getSavedMV())+pas);
		SharedPreferences.Editor editor = mPreferences.edit();
                editor.putString(volt.getFreq(), value).commit();
	}
	final List<Voltage> volts = getVolts(mPreferences);
	mVoltages.clear();
	mVoltages.addAll(volts);
	mAdapter.notifyDataSetChanged();
    }
    
    
    public static List<Voltage> getVolts(final SharedPreferences preferences) {
        final List<Voltage> volts = new ArrayList<Voltage>();
        try {
		BufferedReader br = new BufferedReader(new FileReader(Helpers.getVoltagePath()), 256);
		String line = "";
		if (Helpers.getVoltagePath() == VDD_PATH) {
			while ((line = br.readLine()) != null) {
				line = line.replaceAll("\\s","");
				if (line != "") {
					final String[] values = line.split(":");
					final String freq = values[0];
					final String currentMv = values[1];
					final String savedMv = preferences.getString(freq,currentMv);
					final Voltage voltage = new Voltage();
					voltage.setFreq(freq);
					voltage.setCurrentMV(currentMv);
					voltage.setSavedMV(savedMv);
					volts.add(voltage);
				}
			}
		}
		else{
			while ((line = br.readLine()) != null) {
				final String[] values = line.split("\\s+");
				if (values != null) {
					if (values.length >= 2) {
						final String freq = values[0].replace("mhz:", "");
						final String currentMv = values[1];
						final String savedMv = preferences.getString(freq,currentMv);
						final Voltage voltage = new Voltage();
						voltage.setFreq(freq);
						voltage.setCurrentMV(currentMv);
						voltage.setSavedMV(savedMv);
						volts.add(voltage);
					}
				}
			}
		}
		br.close();
        } catch (FileNotFoundException e) {
            Log.d(TAG, Helpers.getVoltagePath() + " does not exist");
        } catch (IOException e) {
            Log.d(TAG, "Error reading " + Helpers.getVoltagePath());
        }
        return volts;
   }

    private static final int[] STEPS = new int[]{600, 625, 650, 675, 700,
            725, 750, 775, 800, 825, 850, 875, 900, 925, 950, 975, 1000, 1025,
            1050, 1075, 1100, 1125, 1150, 1175, 1200, 1225, 1250, 1275, 1300,
            1325, 1350, 1375, 1400, 1425, 1450, 1475, 1500, 1525, 1550, 1575,
            1600};

    private static int getNearestStepIndex(final int value) {
        int index = 0;
        for (int i = 0; i < STEPS.length; i++) {
            if (value > STEPS[i]) {
                index++;
            } else {
                break;
            }
        }
        return index;
    }

    protected void showDialog(final int id) {
        AlertDialog dialog = null;
        switch (id) {
            case DIALOG_EDIT_VOLT:
                final LayoutInflater factory = LayoutInflater.from(getActivity());
                final View voltageDialog = factory.inflate(R.layout.voltage_dialog,null);

                final EditText voltageEdit = (EditText) voltageDialog.findViewById(R.id.voltageEdit);
                final SeekBar voltageSeek = (SeekBar) voltageDialog.findViewById(R.id.voltageSeek);
                final TextView voltageMeter = (TextView) voltageDialog.findViewById(R.id.voltageMeter);

                final String savedMv = mVoltage.getSavedMV();
                final int savedVolt = Integer.parseInt(savedMv);
                voltageEdit.setText(savedMv);
                voltageEdit.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void afterTextChanged(Editable arg0) {
                    }

                    @Override
                    public void beforeTextChanged(CharSequence arg0, int arg1,int arg2, int arg3) {
                    }

                    @Override
                    public void onTextChanged(CharSequence arg0, int arg1,int arg2, int arg3) {
                        final String text = voltageEdit.getText().toString();
                        int value = 0;
                        try {
                            value = Integer.parseInt(text);
                        } catch (NumberFormatException nfe) {
                            return;
                        }
                        voltageMeter.setText(text + " mV");
                        final int index = getNearestStepIndex(value);
                        voltageSeek.setProgress(index);
                    }

                });

                voltageMeter.setText(savedMv + " mV");
                voltageSeek.setMax(40);
                voltageSeek.setProgress(getNearestStepIndex(savedVolt));
                voltageSeek
                        .setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
                            @Override
                            public void onProgressChanged(SeekBar sb, int progress,boolean fromUser) {
                                if (fromUser) {
                                    final String volt = Integer.toString(STEPS[progress]);
                                    voltageMeter.setText(volt + " mV");
                                    voltageEdit.setText(volt);
                                }
                            }

                            @Override
                            public void onStartTrackingTouch(SeekBar seekBar) {
                                //
                            }

                            @Override
                            public void onStopTrackingTouch(SeekBar seekBar) {
                                //
                            }

                        });

                dialog = new AlertDialog.Builder(getActivity())
                        .setTitle(mVoltage.getFreq()+ getResources().getString(
                                        R.string.ps_volt_mhz_voltage))
                        .setView(voltageDialog)
                        .setPositiveButton(
                                getResources().getString(R.string.ps_volt_save),
                                new DialogInterface.OnClickListener() {
                                    @Override
									public void onClick(DialogInterface dialog,int whichButton) {
                                        //removeDialog(id);
                                        dialog.cancel();
                                        final String value = voltageEdit.getText().toString();
                                        SharedPreferences.Editor editor = mPreferences.edit();
                                        editor.putString(mVoltage.getFreq(), value);
                                        editor.commit();
                                        mVoltage.setSavedMV(value);
                                        mAdapter.notifyDataSetChanged();
                                    }
                                })
                        .setNegativeButton(getString(R.string.cancel),
                                new DialogInterface.OnClickListener() {
                                    @Override
									public void onClick(DialogInterface dialog,int whichButton) {
                                        //removeDialog(id);
                                        dialog.cancel();
                                    }
                                }).create();
                break;
            default:
                break;
        }
        if (dialog != null) {
            dialog.show();
            //dialog.setCancelable(false);
        }
    }

    public class ListAdapter extends BaseAdapter {
        private LayoutInflater mInflater;
        private List<Voltage> results;

        public ListAdapter(Context context) {
            mInflater = LayoutInflater.from(context);
        }

        @Override
        public int getCount() {
            return results.size();
        }

        @Override
        public Object getItem(int position) {
            return results.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {

            final ViewHolder holder;
            if (convertView == null) {
                convertView = mInflater.inflate(R.layout.list_volt, null);
                holder = new ViewHolder();
                holder.mFreq = (TextView) convertView.findViewById(R.id.Freq);
                holder.mCurrentMV = (TextView) convertView.findViewById(R.id.mVCurrent);
                holder.mSavedMV = (TextView) convertView.findViewById(R.id.mVSaved);
                convertView.setTag(holder);
            }
            else {
                holder = (ViewHolder) convertView.getTag();
            }

            final Voltage voltage = mVoltages.get(position);
            holder.setFreq(voltage.getFreq());
            holder.setCurrentMV(voltage.getCurrentMv());
            holder.setSavedMV(voltage.getSavedMV());
            return convertView;
        }

        public void setListItems(List<Voltage> mVoltages) {
            results = mVoltages;
        }

        public class ViewHolder {
            private TextView mFreq;
            private TextView mCurrentMV;
            private TextView mSavedMV;

            public void setFreq(final String freq) {
                mFreq.setText(freq + " Hz");
            }

            public void setCurrentMV(final String currentMv) {
                mCurrentMV.setText(getResources().getString(
                        R.string.ps_volt_current_voltage) + currentMv + " mV");
            }

            public void setSavedMV(final String savedMv) {
                mSavedMV.setText(getResources().getString(
                        R.string.ps_volt_setting_to_apply) + savedMv + " mV");
            }
        }
    }
}

