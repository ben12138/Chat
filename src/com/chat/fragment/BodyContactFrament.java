package com.chat.fragment;

import java.util.ArrayList;
import java.util.List;

import com.ben.chat.R;
import com.chat.activity.ContactInformationActivity;
import com.chat.activity.MainActivity;
import com.chat.adapter.ContactAdapter;
import com.chat.bean.FriendList;
import com.chat.bean.UserInformation;
import com.chat.util.ActivityCollector;
import com.chat.util.GetFriendList;
import com.chat.util.GetUserInformation;
import com.chat.util.Utils;
import com.chat.view.MyListView;
import com.chat.view.MyScrollView;
import com.chat.view.MyScrollView.OnBorderListener;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.View.OnTouchListener;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.Scroller;
import android.widget.Toast;

public class BodyContactFrament extends Fragment implements OnItemClickListener,OnScrollListener{
	
	private View view = null;
	private MyListView contactListView = null;
	private List<FriendList> contactList = null;
	private ContactAdapter contactAdapter = null;
	private Context context = null;
	private ImageView headImageImageView = null;
	private MyScrollView scrollView = null;
	private ListView listView = null;
	private int visibleLastIndex = 0;   //���Ŀ���������  
    private int visibleItemCount;       // ��ǰ���ڿɼ�������
    private Handler handler = new Handler();
    private int num = 12;
    public static Handler handler2 = new Handler();
	
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v,
    		ContextMenuInfo menuInfo) {
    	// TODO Auto-generated method stub
    	menu.add("delete");
    	menu.add("share");
    	menu.setGroupCheckable(0, false, false);
    	super.onCreateContextMenu(menu, v, menuInfo);
    }
    
	@Override
	public void onAttach(Activity activity) {
		// TODO Auto-generated method stub
		super.onAttach(activity);
		context = activity;
		contactList = GetFriendList.getFriends();
		List<FriendList> item = new ArrayList<FriendList>();
		contactAdapter = new ContactAdapter(activity, R.layout.contact_listview, contactList);
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		view = inflater.inflate(R.layout.main_contact_fragment, container,false);
		scrollView = (MyScrollView) view.findViewById(R.id.scroll_view);
		listView = (ListView) view.findViewById(R.id.contact_information);
		listView.setOnScrollListener(this);
		registerForContextMenu(listView);
		contactListView = (MyListView) view.findViewById(R.id.contact_information);
		contactListView.setAdapter(contactAdapter);
		contactListView.setOnItemClickListener(this);
		headImageImageView = (ImageView) view.findViewById(R.id.head_image);
		headImageImageView.setImageBitmap(Utils.getBitmap(GetUserInformation.getU().getPhoto()));
		handler2 = new Handler(){
	    	public void handleMessage(Message msg) {
	    		super.handleMessage(msg);
	    		if(msg.what == 0){
	    			String name = (String) msg.obj;
					for(int i=0;i<contactList.size();i++){
						if(contactList.get(i).getFriendName().equals(name)){
							contactList.get(i).getU().setIsOnline(0);
							contactAdapter.upDateItem(i, contactListView, contactList.get(i),1);
							break;
						}
					}
	    		}else if(msg.what == 1){
	    			FriendList friend = (FriendList) msg.obj;
	    			int position = msg.arg1;
	    			contactAdapter.upDateItem(position, contactListView , friend,0);
	    		}else if(msg.what == 2){
	    			contactList.remove(msg.arg1);
	    		}else if(msg.what == 3){
	    			headImageImageView.setImageBitmap(Utils.getBitmap(GetUserInformation.getU().getPhoto(),2));
	    		}else if(msg.what == 4){
	    			String name = (String) msg.obj;
					for(int i=0;i<contactList.size();i++){
						if(contactList.get(i).getFriendName().equals(name)){
							contactList.get(i).getU().setIsOnline(1);
							contactAdapter.upDateItem(i, contactListView, contactList.get(i),2);
							break;
						}
					}
	    		}
	    	};
	    };
		return view;
	}
	
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		// TODO Auto-generated method stub
		FriendList friend = contactList.get(position);
		Intent intent = new Intent(context,ContactInformationActivity.class);
		intent.putExtra("contact", friend);
		intent.putExtra("click_position", position);
		startActivity(intent);
	}

	@Override
	public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int scrollState) {
		// TODO Auto-generated method stub
		this.visibleItemCount = visibleItemCount;  
        visibleLastIndex = firstVisibleItem + visibleItemCount - 1; 
	}

	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {
		// TODO Auto-generated method stub
		int itemsLastIndex = contactAdapter.getCount() - 1;    //��ݼ����һ�������  
        int lastIndex = itemsLastIndex + 1;             //���ϵײ���loadMoreView��  
        if (scrollState == OnScrollListener.SCROLL_STATE_IDLE && visibleLastIndex == lastIndex) {  
            //������Զ�����,��������������첽������ݵĴ���  
            Log.i("LOADMORE", "loading...");  
        }
	}  
	
	
	
}
