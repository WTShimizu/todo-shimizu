package com.example.todo_shimizu;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static androidx.core.content.res.ResourcesCompat.getDrawable;

public class AddDisplay extends Fragment {
    private SQLiteDatabase db;
    private boolean compFrag = false;
    private TextView compButton;
    private TextView notCompButton;
    private MainActivity mainActivity;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.add_page, container, false);
        mainActivity = (MainActivity) getActivity();

        final String[] FROM = {"title", "day", "day2"};
        final int[] TO = {R.id.listViewtitle, R.id.listViewSub, R.id.listViewSub2};

        Cursor cursor = mainActivity.readData(compFrag);
        ListView listView = view.findViewById(R.id.addListView);
        ArrayList<Map<String, Object>> data = new ArrayList<>();
        ArrayList<Integer> ids = new ArrayList<>();
        Map<String,Object> map;
        for (int i = 0; i < cursor.getCount(); i++) {
            if (i > 20) {
                break;
            }
//            mainActivity.updateId(i+1);
            StringBuilder dayMold = new StringBuilder();
            if (cursor.getInt(1) == 0) {
                dayMold.append("未入力");
            } else {
                dayMold.append(String.valueOf(cursor.getInt(1)));
                dayMold.insert(4, "/");
                dayMold.insert(7, "/");
            }

            ids.add(cursor.getInt(4));

            map =  new HashMap<>();
            map.put("title", cursor.getString(0));
            map.put("day", "未完了");
            map.put("day2", dayMold.toString()); // 完了済み
            data.add(map);

            Log.d("tag", "listid" + cursor.getInt(4) + " date  " +cursor.getString(0) + "      :" + String.valueOf(cursor.getInt(1))
                    + "      :" + cursor.getString(2) + "      :" + cursor.getString(3));
            cursor.moveToNext();
        }
        cursor.close();

        SimpleAdapter adapter =
                new SimpleAdapter(getActivity(), data, R.layout.listview, FROM, TO);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                // Itemクリック時
                Bundle bundle = new Bundle();
                bundle.putBoolean("FRAG", compFrag);
                bundle.putInt("_ID", ids.get(i));
                Log.d("tag", "編集先ID" + String.valueOf(ids.get(i)));
                EditDisplay editDisplay = new EditDisplay();
                editDisplay.setArguments(bundle);
                mainActivity.replaceFragmentManager(editDisplay);
            }
        });

        listView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView absListView, int i) {
                if (listView.getLastVisiblePosition() == (adapter.getCount() - 1)){
                    int position = listView.getFirstVisiblePosition();
                    int yOffset = listView.getChildAt(0).getTop();
                    Cursor cursor = mainActivity.readData(compFrag);
                    ArrayList data = new ArrayList<>();
                    Map<String,Object> map;
                    for (int x = 0; x < cursor.getCount(); x++) {
                        if (x > adapter.getCount()+20) {
                            break;
                        }
                        StringBuilder dayMold = new StringBuilder();
                        if (cursor.getInt(1) == 0) {
                            dayMold.append("未入力");
                        } else {
                            dayMold.append(String.valueOf(cursor.getInt(1)));
                            dayMold.insert(4, "/");
                            dayMold.insert(7, "/");
                        }
                        map =  new HashMap<>();
                        if (compFrag) {
                            StringBuilder compDayMold = new StringBuilder();
                            compDayMold.append(String.valueOf(cursor.getInt(1)));
                            compDayMold.insert(4, "/");
                            compDayMold.insert(7, "/");
                            map.put("day", compDayMold);
                        } else {
                            map.put("day", "未完了");
                        }
                        map.put("title", cursor.getString(0));
                        map.put("day2", dayMold.toString()); // 完了済み
                        data.add(map);
                        ids.add(cursor.getInt(4));
                        Log.d("tag", "id" + cursor.getInt(4) + " date  " +cursor.getString(0) + "      :" + String.valueOf(cursor.getInt(1))
                                + "      :" + cursor.getString(2) + "      :" + cursor.getString(3));
                        cursor.moveToNext();
                    }

                    cursor.close();
                    int settingLayout = R.layout.listview;

                    SimpleAdapter adapter =
                            new SimpleAdapter(getActivity(), data, settingLayout, FROM, TO);
                    listView.setAdapter(adapter);
                    adapter.notifyDataSetChanged();
                    listView.setSelectionFromTop(position, yOffset);
                }
            }

            @Override
            public void onScroll(AbsListView absListView, int i, int i1, int i2) {

            }
        });

        ImageView addButton = view.findViewById(R.id.rNewButton);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mainActivity.replaceFragmentManager(new NewDisplay());
            }
        });

        ImageView deleteButton = view.findViewById(R.id.rDeleteButton);
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 削除ボタン
                mainActivity.replaceFragmentManager(new DeleteDisplay());
            }
        });

        Drawable select = getDrawable(getResources(), R.drawable.select, null);
        Drawable notSelect = getDrawable(getResources(), R.drawable.not_select, null);

        notCompButton = view.findViewById(R.id.addNoButton);
        notCompButton.setBackground(select);
        notCompButton.setTextColor(Color.WHITE);
        notCompButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.setSelected(!v.isSelected());
                // 未完了ボタン
                if (compFrag) {
                    compFrag = false;
                    notCompButton.setBackground(select);
                    notCompButton.setTextColor(Color.WHITE);

                    compButton.setBackground(notSelect);
                    compButton.setTextColor(Color.GRAY);

                    Cursor cursor = mainActivity.readData(compFrag);
                    ArrayList data = new ArrayList<>();
                    Map<String,Object> map;
                    ids.clear();
                    for (int i = 0; i < cursor.getCount(); i++) {
                        if (i > 20) {
                            break;
                        }
                        StringBuilder dayMold = new StringBuilder();
                        if (cursor.getInt(1) == 0) {
                            dayMold.append("未入力");
                        } else {
                            dayMold.append(String.valueOf(cursor.getInt(1)));
                            dayMold.insert(4, "/");
                            dayMold.insert(7, "/");
                        }
                        ids.add(cursor.getInt(4));
                        map =  new HashMap<>();
                        map.put("title", cursor.getString(0));
                        map.put("day", "未完了");
                        map.put("day2", dayMold.toString());
                        data.add(map);
                        Log.d("tag", "id" + cursor.getInt(4) + " date  " +cursor.getString(0) + "      :" + String.valueOf(cursor.getInt(1))
                                + "      :" + cursor.getString(2) + "      :" + cursor.getString(3));
                        cursor.moveToNext();
                    }
                    cursor.close();
                    SimpleAdapter adapter =
                            new SimpleAdapter(getActivity(), data, R.layout.listview, FROM, TO);
                    listView.setAdapter(adapter);
                    adapter.notifyDataSetChanged();
                }
            }
        });

        compButton = view.findViewById(R.id.addCompButton);
        compButton.setBackground(notSelect);
        compButton.setTextColor(Color.GRAY);
        compButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.setSelected(!v.isSelected());
                // 完了ボタン
                if (!compFrag) {
                    compFrag = true;
                    compButton.setBackground(select);
                    compButton.setTextColor(Color.WHITE);

                    notCompButton.setBackground(notSelect);
                    notCompButton.setTextColor(Color.GRAY);

                    Cursor cursor = mainActivity.readData(compFrag);
                    ArrayList data = new ArrayList<>();
                    Map<String,Object> map;
                    ids.clear();
                    for (int i = 0; i < cursor.getCount(); i++) {
                        if (i > 20) {
                            break;
                        }
                        StringBuilder dayMold = new StringBuilder();
                        if (cursor.getInt(1) == 0) {
                            dayMold.append("未入力");
                        } else {
                            dayMold.append(String.valueOf(cursor.getInt(1)));
                            dayMold.insert(4, "/");
                            dayMold.insert(7, "/");
                        }

                        StringBuilder compDayMold = new StringBuilder();
                        compDayMold.append(String.valueOf(cursor.getInt(5)));
                        compDayMold.insert(4, "/");
                        compDayMold.insert(7, "/");
                        ids.add(cursor.getInt(4));
                        map =  new HashMap<>();
                        map.put("title", cursor.getString(0));
                        map.put("day", compDayMold.toString());
                        map.put("day2", dayMold.toString()); // 完了済み
                        data.add(map);
                        Log.d("tag", "id" + cursor.getInt(4) + " date  " +cursor.getString(0) + "      :" + String.valueOf(cursor.getInt(1))
                                + "      :" + cursor.getString(2) + "      :" + cursor.getString(3));
                        cursor.moveToNext();
                    }
                    cursor.close();
                    SimpleAdapter adapter =
                            new SimpleAdapter(getActivity(), data, R.layout.listview, FROM, TO);
                    listView.setAdapter(adapter);
                    adapter.notifyDataSetChanged();
                }
            }
        });

        return view;

    }
}