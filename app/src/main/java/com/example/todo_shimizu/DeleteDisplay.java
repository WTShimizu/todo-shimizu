package com.example.todo_shimizu;

import android.app.AlertDialog;
import android.content.DialogInterface;
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
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static androidx.core.content.res.ResourcesCompat.getDrawable;

public class DeleteDisplay extends Fragment {
    private SQLiteDatabase db;
    private boolean compFrag = false;
    private TextView compButton;
    private TextView notCompButton;
    private ListView listView;
    protected MyListItem myListItem;
    private MainActivity mainActivity;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.delete_page, container, false);
        mainActivity = (MainActivity) getActivity();

        final String[] FROM = {"title", "day", "day2"};
        final int[] TO = {R.id.listViewtitle, R.id.listViewSub, R.id.listViewSub2};

        Cursor cursor = mainActivity.readData(compFrag);
        ListView listView = view.findViewById(R.id.deleteListView);
        ArrayList<Map<String, Object>> data = new ArrayList<>();
        ArrayList<Integer> ids = new ArrayList<>();
        Map<String,Object> map;
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
            map =  new HashMap<>();
            map.put("title", cursor.getString(0));
            map.put("day", "");
            map.put("day2", dayMold.toString());
            data.add(map);
            ids.add(cursor.getInt(4));
            Log.d("tag", "id" + cursor.getInt(4) + " date  " +cursor.getString(0) + "      :" + String.valueOf(cursor.getInt(1))
                    + "      :" + cursor.getString(2) + "      :" + cursor.getString(3));
            cursor.moveToNext();
        }
        cursor.close();

        SimpleAdapter adapter =
                new SimpleAdapter(getActivity(), data, R.layout.notcomp_listview, FROM, TO);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                // Itemクリック時
                AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
                builder.setTitle("削除");
                builder.setMessage("削除しますか？");
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mainActivity.selectDelete(String.valueOf(ids.get(i)), compFrag);
                        AddDisplay addDisplay = new AddDisplay();
                        mainActivity.replaceFragmentManager(addDisplay);
                    }
                });

                builder.setNegativeButton("キャンセル", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });
                // ダイアログの表示
                AlertDialog dialog = builder.create();
                dialog.show();

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
                            map.put("day", "");
                        }
                        map.put("title", cursor.getString(0));
                        map.put("day2", dayMold.toString());
                        data.add(map);
                        Log.d("tag", "id" + cursor.getInt(4) + " date  " +cursor.getString(0) + "      :" + String.valueOf(cursor.getInt(1))
                                + "      :" + cursor.getString(2) + "      :" + cursor.getString(3));
                        cursor.moveToNext();
                    }

                    cursor.close();
                    int settingLayout;
                    if (compFrag) {
                        settingLayout = R.layout.listview;
                    } else {
                        settingLayout = R.layout.notcomp_listview;
                    }
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

//        Button addButton = view.findViewById(R.id.addPageButton);
//        addButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                // 追加ボタン
//                AddDisplay addDisplay = new AddDisplay();
//                mainActivity.replaceFragmentManager(addDisplay);
//            }
//        });

//        Button deleteButton = view.findViewById(R.id.delDecButton);
//        deleteButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                // 削除ボタン
//            }
//        });

        Drawable select = getDrawable(getResources(), R.drawable.select, null);
        Drawable notSelect = getDrawable(getResources(), R.drawable.not_select, null);


        notCompButton = view.findViewById(R.id.deleteNoButton);
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
                    for (int i = 0; i < cursor.getCount(); i++) {
                        if (i > adapter.getCount()+20) {
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
                        map.put("title", cursor.getString(0));
                        map.put("day", "");
                        map.put("day2", dayMold.toString());
                        data.add(map);
                        Log.d("tag", "id" + cursor.getInt(4) + " date  " +cursor.getString(0) + "      :" + String.valueOf(cursor.getInt(1))
                                + "      :" + cursor.getString(2) + "      :" + cursor.getString(3));
                        cursor.moveToNext();
                    }
                    cursor.close();
                    SimpleAdapter adapter =
                            new SimpleAdapter(getActivity(), data, R.layout.notcomp_listview, FROM, TO);
                    listView.setAdapter(adapter);
                    adapter.notifyDataSetChanged();
                }
            }
        });

        LinearLayout deleteCancelButton = view.findViewById(R.id.deleteCancelButton);
        deleteCancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mainActivity = (MainActivity) getActivity();
                AddDisplay addDisplay = new AddDisplay();
                mainActivity.replaceFragmentManager(addDisplay);
            }
        });

        compButton = view.findViewById(R.id.deleteCompButton);
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
                    for (int i = 0; i < cursor.getCount(); i++) {
                        if (i > adapter.getCount()+20) {
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