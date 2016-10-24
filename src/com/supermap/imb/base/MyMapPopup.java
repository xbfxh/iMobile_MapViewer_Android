package com.supermap.imb.base;

import java.util.ArrayList;

import com.supermap.data.Workspace;
import com.supermap.imb.appconfig.DefaultDataManager;
import com.supermap.imb.appconfig.MyApplication;
import com.supermap.mapping.MapControl;

import android.os.Bundle;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.view.ContextThemeWrapper;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager.LayoutParams;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

@SuppressLint("NewApi")
public class MyMapPopup extends PopupWindow implements OnClickListener{
	private View     mContentView  = null;
	private TextView mTvTitle      = null;
	
	private ListView mListMaps = null;
	private ArrayList<String> mapList = null;
	private int indexOfMapList = 0;
	private LayoutInflater mInflater = null;
	private MapControl     mMapControl = null;
	private Workspace mWorkspace = null;

	private DefaultDataManager mDefaultDataManager = null;
	
	/**
	 * ���캯��
	 * @param mapControl ��ͼ�ؼ�
	 */
	public MyMapPopup(MapControl mapControl) {
		mMapControl = mapControl;
		mInflater = LayoutInflater.from(mapControl.getContext());
		mDefaultDataManager = MyApplication.getInstance().getDefaultDataManager();
		mWorkspace = mDefaultDataManager.getWorkspace();
		
		initView();
	}
	
	/**
	 * ��ʼ����ʾ������
	 */
	private void initView(){
		mContentView = mInflater.inflate(R.layout.activity_maps, null);
		setContentView(mContentView);
		 
		mTvTitle  = (TextView) mContentView.findViewById(R.id.common_title).findViewById(R.id.tv_title);
		mListMaps = (ListView) mContentView.findViewById(R.id.list_maps);
		mContentView.findViewById(R.id.common_title).findViewById(R.id.btn_back).setOnClickListener(this);
		
		mTvTitle.setText("��ͼչʾ");
		initMapList();
		mListMaps.setAdapter(new MapsAdapter());
	}
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_back:
			dismiss();
			MainActivity.reset();
			break;
		default:
			break;
		}
	}
	

	private static class ViewHolder{
		TextView MapName;
		ImageView MapType;
	}
	
	private class MapsAdapter extends BaseAdapter{
		@Override
		public int getCount() {
			return mapList.size();
		}

		@Override
		public Object getItem(int arg0) {
			return mWorkspace.getMaps().get(arg0);
		}

		@Override
		public long getItemId(int arg0) {
			return 0;
		}

		@Override
		public View getView(int index, View convertView, ViewGroup arg2) {
			ViewHolder holder = null;
			if(convertView == null){
				convertView = mInflater.inflate(R.layout.listview_map_item,	 null);			
				holder = new ViewHolder();
				holder.MapName = (TextView) convertView.findViewById(R.id.tv_map_name);
				holder.MapType = (ImageView) convertView.findViewById(R.id.img_map_type);
				convertView.setTag(holder);
			}else{
				holder = (ViewHolder) convertView.getTag();
			}
			final String name = mapList.get(index);
			holder.MapName.setText(name);
			int type = mDefaultDataManager.getMapTypeResource(name);
			holder.MapType.setImageResource(type);
			convertView.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View arg0) {
					
					
					if(mWorkspace.getMaps().indexOf(name) == -1){
						
						// ������ָ���ĵ�ͼ������ʾ�Ƿ�򿪳�ͼ�Ʒ����ͼ
						popOpenMapInfo(arg0);
					}else {
						
						// �ָ���ͼ�б��Ĭ�ϱ���ɫ
						mMapControl.getMap().close();
						mMapControl.getMap().open(name);
						// �Գ����е�ͼ��������ˢ��
						if(name.equals("��������ͼ")){
							mMapControl.getMap().setFullScreenDrawModel(true);
						}else{
							mMapControl.getMap().setFullScreenDrawModel(false);
						}
						mMapControl.getMap().refresh();
						
//						mListMaps.getChildAt(indexOfMapList).setEnabled(true);
//						arg0.setEnabled(false);                           // �̶���ǰ��Ŀ�ı���ɫ
//						indexOfMapList = mListMaps.indexOfChild(arg0);
						
					}
				}
			});
			return convertView;
		}
	}
	
	/**
	 * ��ʼ����ͼ�б�
	 */
	private void initMapList(){
		mapList = new ArrayList<String>();
		mapList.add("��������ͼ");
		mapList.add("��ͼ�Ʒ���");
		mapList.add("iServerRest��ͼ");
		mapList.add("���ͼ");
		mapList.add("�ȸ��ͼ");
		mapList.add("�ٶȵ�ͼ");
		mapList.add("SIT��ͼ");
		mapList.add("DEM��ͼ");
		mapList.add("SCI��ͼ");
		mapList.add("CAD��ͼ");
		mapList.add("WMS��ͼ");
		mapList.add("Bing��ͼ");
		mapList.add("OpenStreetMap");
		
	}
	
	/**
	 * ��ʾ��ͼ�б�
	 */
	public void show(){
		
		showAt(100, 140, 350, 480);
	}
	
	private void showAt(int x,int y, int width, int height)
	{
		setWidth(MyApplication.dp2px(width));
		setHeight(MyApplication.dp2px(height));
		showAtLocation(mMapControl.getRootView(), Gravity.LEFT|Gravity.TOP,MyApplication.dp2px(x), MyApplication.dp2px(y));
	}
	
	/**
	 * �رյ�ͼ�б�
	 */
	public void dismiss(){
		
		super.dismiss();
		if (mListMaps.getChildAt(indexOfMapList) != null)
			mListMaps.getChildAt(indexOfMapList).setEnabled(true);
	}
	
	/**
	 * ��ʾ��
	 * @param arg0  Android��ǰ��ʾ�ؼ�
	 */
	public void popOpenMapInfo(View arg0){
		AlertDialog.Builder builer = new AlertDialog.Builder(mMapControl.getContext());
		builer.setTitle("ָ���ĵ�ͼ������");
		builer.setMessage("�Ƿ�򿪳�ͼ�Ʒ����ͼ?");
		final View view = arg0;
		builer.setPositiveButton("ȷ��", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// �ָ���ͼ�б��Ĭ�ϱ���ɫ
				int count = mListMaps.getChildCount();
				for(int index=0; index<count; index++ ){
					
					boolean isFind = ((TextView)(mListMaps.getChildAt(index).findViewById(R.id.tv_map_name))).getText().toString().contains("��ͼ�Ʒ���");
					if(isFind){
						mListMaps.getChildAt(index).setEnabled(false);  // ��ʾѡ�г�ͼ�Ʒ����ͼ
						indexOfMapList = index;
					}else{
						mListMaps.getChildAt(index).setEnabled(true);
					}
				}
				
				mMapControl.getMap().open("��ͼ�Ʒ���");
				mMapControl.getMap().refresh();
			}
		});
		builer.setNegativeButton("ȡ��", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
			}
		});
		builer.create().show();
	}
}
