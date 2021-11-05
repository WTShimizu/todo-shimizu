package com.example.todo_shimizu;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import android.os.Handler;
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
import java.util.List;
import java.util.Map;

import static androidx.core.content.res.ResourcesCompat.getDrawable;

public class AddDisplay extends Fragment {
    private SQLiteDatabase db;
    private boolean compFrag = false;
    private TextView compButton;
    private TextView notCompButton;
    private MainActivity mainActivity;
    private ListView listView;
    ArrayList<Integer> ids;
    SimpleAdapter adapter;
    Handler handler;
    private int mListCount;
    private int mPosition;
    private int mYOff;

    public void createList(List<DataList> dataLists) {
        int count = 0;
        Map<String,Object> map;
        ArrayList<Map<String, Object>> data = new ArrayList<>();
        final String[] FROM = {"title", "day", "day2"};
        final int[] TO = {R.id.listViewtitle, R.id.listViewSub, R.id.listViewSub2};
//        ArrayList<Integer> ids = new ArrayList<>();
        ids = new ArrayList<>();
        for (DataList dataList : dataLists) {
            ids.add(dataList.getId());
            String title = dataList.getTitle();
            int day = Integer.parseInt(dataList.getDay());
            int status = Integer.parseInt(dataList.getStatus());
            int compDay = Integer.parseInt(dataList.getCompleteDay());
            if (count > mListCount) {
                break;
            }
            Log.d("tag", title + dataList.getExp() + status + day + compDay);
            count ++;
//            mainActivity.updateId(i+1);
            StringBuilder dayMold = new StringBuilder();
            if (day == 0) {
                dayMold.append("未入力");
            } else {
                dayMold.append(String.valueOf(day));
                dayMold.insert(4, "/");
                dayMold.insert(7, "/");
            }

            StringBuilder compDayMold = new StringBuilder();
            if (status == 0) {
                compDayMold.append("未完了");
            } else {
                if (compDay == 0) {
                    compDayMold.append("未入力");
                } else {
                    compDayMold.append(String.valueOf(day));
                    compDayMold.insert(4, "/");
                    compDayMold.insert(7, "/");
                }
            }

            map =  new HashMap<>();
            map.put("title", title);
            // TODO : 完了日
            map.put("day", compDayMold);
            map.put("day2", dayMold.toString()); // 完了済み
            data.add(map);
        }

        handler.post(new Runnable() {
            @Override
            public void run() {
                adapter = new SimpleAdapter(getActivity(), data, R.layout.listview, FROM, TO);
                listView.setAdapter(adapter);
                if (mPosition != 0 && mYOff != 0) {
                    adapter.notifyDataSetChanged();
                    listView.setSelectionFromTop(mPosition, mYOff);
                }
            }
        });
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.add_page, container, false);
        mainActivity = (MainActivity) getActivity();
        listView = view.findViewById(R.id.addListView);
        ids = new ArrayList<>();
        handler = new Handler();
        // 初期表示List数
        mListCount = 20;

        Threader.AddThreadHttp threadHttp = new Threader.AddThreadHttp(this, compFrag);
        threadHttp.start();
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
                    mPosition = listView.getFirstVisiblePosition();
                    mYOff = listView.getChildAt(0).getTop();

                    mListCount = mListCount + 20;
                    Threader.AddThreadHttp threadHttp = new Threader.AddThreadHttp(AddDisplay.this, compFrag);
                    threadHttp.start();
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
                    mPosition = 0;
                    mYOff = 0;

                    compFrag = false;
                    notCompButton.setBackground(select);
                    notCompButton.setTextColor(Color.WHITE);

                    compButton.setBackground(notSelect);
                    compButton.setTextColor(Color.GRAY);

                    mListCount = 20;
                    Threader.AddThreadHttp threadHttp =
                            new Threader.AddThreadHttp(AddDisplay.this, compFrag);

                    threadHttp.start();
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
                    mPosition = 0;
                    mYOff = 0;

                    compFrag = true;
                    compButton.setBackground(select);
                    compButton.setTextColor(Color.WHITE);

                    notCompButton.setBackground(notSelect);
                    notCompButton.setTextColor(Color.GRAY);

                    mListCount = 20;
                    Threader.AddThreadHttp threadHttp =
                            new Threader.AddThreadHttp(AddDisplay.this, compFrag);
                    threadHttp.start();
                }
            }
        });

        return view;

    }
}