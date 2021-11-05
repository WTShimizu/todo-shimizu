package com.example.todo_shimizu;

import android.app.DatePickerDialog;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import java.util.Calendar;
import java.util.TimeZone;

import static androidx.core.content.res.ResourcesCompat.getDrawable;

public class NewDisplay extends Fragment implements DatePickerDialog.OnDateSetListener {

//    private Button dateButton;
    private SQLiteDatabase db;
    private UserData userData;
    private String dayText;
    private boolean statusFrag = false;
    private int mYear;
    private int mMonth;
    private int mDay;
    private View view;
    private TextView notCompButton;
    private TextView compButton;
    public DatePickerDialog.OnDateSetListener mDateSetListener;

    public NewDisplay() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.new_page, container, false);

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        EditText title = view.findViewById(R.id.titleEdit);

        final Calendar date = Calendar.getInstance(TimeZone.getTimeZone("Asia/Tokyo"));
        TextView dateButton = view.findViewById(R.id.dateButton);

        String month = null;
        if (String.valueOf(date.get(Calendar.MONTH) + 1).length() == 1) {
            month = "0" + String.valueOf(date.get(Calendar.MONTH) + 1);
        } else {
            month = String.valueOf(date.get(Calendar.MONTH) + 1);
        }

        String day = null;
        if (String.valueOf(date.get(Calendar.DAY_OF_MONTH)).length() == 1) {
            day = "0" + String.valueOf(date.get(Calendar.DAY_OF_MONTH));
        } else {
            day = String.valueOf(date.get(Calendar.DAY_OF_MONTH));
        }


//        dateButton.setText(String.valueOf(date.get(Calendar.YEAR)) +
//                "/" + month + "/" + day);
        dateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment newFragment = new DatePick();
                newFragment.setTargetFragment(NewDisplay.this, 0);
                newFragment.show(getFragmentManager(), "datePicker");
            }
        });
        ImageView dateIcon = view.findViewById(R.id.dateIcon);
        dateIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogFragment newFragment = new DatePick();
                newFragment.setTargetFragment(NewDisplay.this, 0);
                newFragment.show(getFragmentManager(), "datePicker");
            }
        });

        FrameLayout layout = view.findViewById(R.id.hideLayout);
        MainActivity mainActivity = (MainActivity) getActivity();
        InputMethodManager inputMethodManager =
                (InputMethodManager)mainActivity.getSystemService(Context.INPUT_METHOD_SERVICE);
//        layout.dispatchDependentViewsChanged(new );
        layout.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                inputMethodManager.hideSoftInputFromWindow(layout.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                layout.requestFocus();
                return false;
            }
        });

        Drawable lefSelect = getDrawable(getResources(), R.drawable.lef_select, null);
        Drawable lefNotSelect = getDrawable(getResources(), R.drawable.lef_not_select, null);
        Drawable rigSelect = getDrawable(getResources(), R.drawable.rig_select, null);
        Drawable rigNotSelect = getDrawable(getResources(), R.drawable.rig_not_select, null);

        notCompButton = view.findViewById(R.id.notCompButton);
        notCompButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (statusFrag) {
                    statusFrag = false;
                    notCompButton.setBackground(lefSelect);
                    notCompButton.setTextColor(Color.WHITE);

                    compButton.setBackground(rigNotSelect);
                    compButton.setTextColor(Color.GRAY);
                }
            }
        });

        compButton = view.findViewById(R.id.compButton);
        compButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!statusFrag) {
                    statusFrag = true;
                    compButton.setBackground(rigSelect);
                    compButton.setTextColor(Color.WHITE);

                    notCompButton.setBackground(lefNotSelect);
                    notCompButton.setTextColor(Color.GRAY);
                }
            }
        });

        notCompButton.setBackground(lefSelect);
        notCompButton.setTextColor(Color.WHITE);
        compButton.setBackground(rigNotSelect);
        compButton.setTextColor(Color.GRAY);


        TextView endButton = view.findViewById(R.id.addButton);
        endButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                inputMethodManager.hideSoftInputFromWindow(layout.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                layout.requestFocus();
                Log.d("tag", title.getText().toString());
                if (title.getText().toString().length() != 0) {
                    // タイトルが未記入でない場合
                    String titleText = title.getText().toString();
                    TextView detailsEdit = view.findViewById(R.id.detailsEdit);
                    String exp = detailsEdit.getText().toString();

                    dayText = dateButton.getText().toString();
                    MainActivity mainActivity = (MainActivity) getActivity();

                    String month;
                    if (String.valueOf(date.get(Calendar.MONTH) + 1).length() == 1) {
                        month = "0" + String.valueOf(date.get(Calendar.MONTH) + 1);
                    } else {
                        month = String.valueOf(date.get(Calendar.MONTH) + 1);
                    }

                    String day;
                    if (String.valueOf(date.get(Calendar.DAY_OF_MONTH)).length() == 1) {
                        day = "0" + String.valueOf(date.get(Calendar.DAY_OF_MONTH));
                    } else {
                        day = String.valueOf(date.get(Calendar.DAY_OF_MONTH));
                    }

                    String compDay = String.valueOf(date.get(Calendar.YEAR)) +
                            "/" + month + "/" + day;

                    DataList dataList = new DataList();
                    dataList.setTitle(titleText);
                    dataList.setExp(exp);
                    dataList.setStatus(statusFrag ? "0" : "1");
                    dataList.setDay(dayText.replace("/", "").replace("/", ""));
                    dataList.setCompleteDay(compDay.replace("/", "").replace("/", ""));

                    Threader.NewThreadHttp threadHttp = new Threader.NewThreadHttp(dataList, statusFrag);
                    threadHttp.start();
//                    mainActivity.insertData(titleText, exp, statusFrag, dayText, compDay);
                    AddDisplay addDisplay = new AddDisplay();
                    mainActivity.replaceFragmentManager(addDisplay);
                } else {
                    TextView validation = view.findViewById(R.id.addValidation);
                    validation.setText("タイトルは必ず入力してください");
                }
            }
        });

        LinearLayout cancelButton = view.findViewById(R.id.cancelButton);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                inputMethodManager.hideSoftInputFromWindow(layout.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                layout.requestFocus();
                MainActivity mainActivity = (MainActivity) getActivity();
                AddDisplay addDisplay = new AddDisplay();
                mainActivity.replaceFragmentManager(addDisplay);
            }
        });
    }

    public void updateDisplay(int year, String month, String day) {
        TextView dateButton = view.findViewById(R.id.dateButton);
        dateButton.setText(String.valueOf(year) + "/" + month + "/" + day);
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        mYear = year;
        String monthText;
        if (String.valueOf(month).length() == 1) {
            monthText = "0" + String.valueOf(month+1);
        } else {
            monthText = String.valueOf(month);
        }
        String dayText;
        if (String.valueOf(dayOfMonth).length() == 1) {
            dayText = "0" + String.valueOf(dayOfMonth);
        } else {
            dayText = String.valueOf(dayOfMonth);
        }
        updateDisplay(mYear, monthText, dayText);
    }

//    public void showDatePickerDialog(View v) {
//        DialogFragment newFragment = new DatePick();
//        newFragment.show(getFragmentManager(), "datePicker");
//    }

}
