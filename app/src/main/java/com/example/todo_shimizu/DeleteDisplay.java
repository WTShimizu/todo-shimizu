package com.example.todo_shimizu;

import android.app.AlertDialog;
import android.content.DialogInterface;

import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
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
import java.util.List;
import java.util.Map;

import static androidx.core.content.res.ResourcesCompat.getDrawable;

public class DeleteDisplay extends Fragment {
    private boolean compFrag = false;
    private TextView compButton;
    private TextView notCompButton;
    private ListView listView;
    protected MyListItem myListItem;
    private MainActivity mainActivity;

    ArrayList<Integer> ids;
    SimpleAdapter adapter;
    Handler handler;
    int mListCount;

    public void createDelList(List<DataList> dataLists) {
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
            if (count > mListCount) {
                break;
            }
//            mainActivity.updateId(i+1);
            StringBuilder dayMold = new StringBuilder();
            if (day == 0) {
                dayMold.append("未入力");
            } else {
                dayMold.append(String.valueOf(day));
                dayMold.insert(4, "/");
                dayMold.insert(7, "/");
            }

            map =  new HashMap<>();
            map.put("title", title);
            map.put("day", "未完了");
            map.put("day2", dayMold.toString()); // 完了済み
            data.add(map);
        }

        handler.post(new Runnable() {
            @Override
            public void run() {
                adapter = new SimpleAdapter(getActivity(), data, R.layout.listview, FROM, TO);
                listView.setAdapter(adapter);
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.delete_page, container, false);
        mainActivity = (MainActivity) getActivity();

        listView = view.findViewById(R.id.deleteListView);
        ids = new ArrayList<>();
        final String[] FROM = {"title", "day", "day2"};
        final int[] TO = {R.id.listViewtitle, R.id.listViewSub, R.id.listViewSub2};
        handler = new Handler();
        // 初期表示List数
        mListCount = 20;

        Threader.DeleteListThreadHttp threadHttp =
                new Threader.DeleteListThreadHttp(DeleteDisplay.this, compFrag);
        threadHttp.start();

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
                        Threader.DeleteThreadHttp deleteThreadHttp =
                                new Threader.DeleteThreadHttp(ids.get(i), compFrag);
                        deleteThreadHttp.start();
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
                    mListCount = mListCount + 20;
                    Threader.DeleteListThreadHttp threadHttp =
                            new Threader.DeleteListThreadHttp(DeleteDisplay.this, compFrag);
                    threadHttp.start();
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

                    mListCount = 20;
                    Threader.DeleteListThreadHttp threadHttp =
                            new Threader.DeleteListThreadHttp(DeleteDisplay.this, compFrag);
                    threadHttp.start();
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

                    mListCount = 20;
                    Threader.DeleteListThreadHttp threadHttp =
                            new Threader.DeleteListThreadHttp(DeleteDisplay.this, compFrag);
                    threadHttp.start();
                }
            }
        });

        return view;
    }
}