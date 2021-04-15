package com.example.todo_shimizu;

import android.app.DatePickerDialog;
import android.content.Context;
import android.database.Cursor;
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

public class EditDisplay extends Fragment implements DatePickerDialog.OnDateSetListener {

    private String dayText;
    private boolean statusFrag = false;
    private int mYear;
    private int mMonth;
    private int mDay;
    private View view;
    private TextView notCompButton;
    private TextView compButton;
    public DatePickerDialog.OnDateSetListener mDateSetListener;
    private boolean status = false;
    private int id = 0;

    public EditDisplay() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.edit_page, container, false);

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();

        Bundle bundle = getArguments();
        if (bundle !=  null) {
            status = bundle.getBoolean("FRAG");
            id = bundle.getInt("_ID");
            Log.d("tag", "編集ID" + String.valueOf(id));
        }
        statusFrag = status;

        MainActivity mainActivity = (MainActivity) getActivity();
        Cursor cursor = mainActivity.readData(status);
        for (int i = 0; i < cursor.getCount(); i++) {
            if (cursor.getInt(4) == id) {
                break;
            }
            cursor.moveToNext();
        }
        EditText title = view.findViewById(R.id.editTitleEdit);
        title.setText(cursor.getString(0));

        TextView dateButton = view.findViewById(R.id.editDateButton);
        StringBuilder dayMold = new StringBuilder();
        if (cursor.getInt(1) == 0) {
            dateButton.setText("");
        } else {
            dayMold.append(String.valueOf(cursor.getInt(1)));
            dayMold.insert(4, "/");
            dayMold.insert(7, "/");
            dateButton.setText(dayMold.toString());
        }

        EditText detailsEdit = view.findViewById(R.id.editDetailsEdit);
        detailsEdit.setText(cursor.getString(2));


        dateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment newFragment = new DatePick();
                newFragment.setTargetFragment(EditDisplay.this, 0);
                newFragment.show(getFragmentManager(), "datePicker");
            }
        });

        ImageView dateIcon = view.findViewById(R.id.editDateIcon);
        dateIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogFragment newFragment = new DatePick();
                newFragment.setTargetFragment(EditDisplay.this, 0);
                newFragment.show(getFragmentManager(), "datePicker");
            }
        });

        FrameLayout layout = view.findViewById(R.id.editHideLayout);
        mainActivity = (MainActivity) getActivity();
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


        notCompButton = view.findViewById(R.id.editNotCompButton);
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

        compButton = view.findViewById(R.id.editCompButton);
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

        if (status) {
            compButton.setBackground(rigSelect);
            compButton.setTextColor(Color.WHITE);

            notCompButton.setBackground(lefNotSelect);
            notCompButton.setTextColor(Color.GRAY);
        } else {
            notCompButton.setBackground(lefSelect);
            notCompButton.setTextColor(Color.WHITE);

            compButton.setBackground(rigNotSelect);
            compButton.setTextColor(Color.GRAY);
        }

        TextView endButton = view.findViewById(R.id.editAddButton);
        endButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("tag", title.getText().toString());
                if (title.getText().toString().length() != 0) {
                    // タイトルが未記入でない場合
                    String titleText = title.getText().toString();

                    String exp = detailsEdit.getText().toString();

                    dayText = dateButton.getText().toString();
                    MainActivity mainActivity = (MainActivity) getActivity();
                    final Calendar date = Calendar.getInstance(TimeZone.getTimeZone("Asia/Tokyo"));
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
                    if (status != statusFrag) {
                        mainActivity.insertData(titleText, exp, statusFrag, dayText, compDay);
                        mainActivity.selectDelete(String.valueOf(cursor.getInt(4)), status);
                    } else {
                        mainActivity.editData(titleText, exp, status, dayText, compDay, cursor.getInt(4));
                    }
                    AddDisplay addDisplay = new AddDisplay();
                    mainActivity.replaceFragmentManager(addDisplay);
                } else {
                    TextView validation = view.findViewById(R.id.editValidation);
                    validation.setText("タイトルは必ず入力してください");
                }
            }
        });

        LinearLayout cancelButton = view.findViewById(R.id.editCancelButton);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity mainActivity = (MainActivity) getActivity();
                AddDisplay addDisplay = new AddDisplay();
                mainActivity.replaceFragmentManager(addDisplay);
            }
        });

    }

    public void updateDisplay(int year, String month, String day) {
        TextView dateButton = view.findViewById(R.id.editDateButton);
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
}
