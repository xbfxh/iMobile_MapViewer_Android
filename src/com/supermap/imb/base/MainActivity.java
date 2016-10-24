package com.supermap.imb.base;

import java.util.ArrayList;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.RadioButton;
import com.supermap.data.Dataset;
import com.supermap.data.DatasetType;
import com.supermap.imb.appconfig.DataManager;
import com.supermap.imb.appconfig.DefaultDataConfig;
import com.supermap.imb.appconfig.DefaultDataManager;
import com.supermap.imb.appconfig.MyApplication;
import com.supermap.mapping.MapControl;
import com.supermap.mapping.MapView;
/**
 * <p>
 * Title:��ͼ��ʾ
 * </p>
 * 
 * <p>
 * Description:
 * ============================================================================>
 * ------------------------------��Ȩ����----------------------------
 * ���ļ�Ϊ SuperMap iMobile ��ʾDemo�Ĵ��� 
 * ��Ȩ���У�������ͼ����ɷ����޹�˾
 * ----------------------------------------------------------------
 * ----------------------------SuperMap iMobile ��ʾDemo˵��---------------------------
 * 1��Demo��飺
 *   չʾ���ߵ�ʸ�����ݡ�Ӱ�����ݣ����ߵ�ͼ�ȵ�ͼ���ݵ���ʾ�������
 * 2��Demo���ݣ�����Ŀ¼��"/SuperMap/Demos/Data/BaseDemo/"
 *              ��ͼ���ݣ�"changchun.smwu", "changchun.udb", "changchun.udd"
 *              ���Ŀ¼��"/SuperMap/License/"
 * 3���ؼ�����/��Ա: 
 *   Workspace.getMaps().get();                ����
 *   Workspace.getDatasources().get();         ����
 *   Datasource.getDatasets().get();           ����
 *   MapControl.getMap();                      ����
 *   MapCOntrol.zoomTo();                      ����
 *   Map.open();                               ����
 *   Map.getLayers().add();                    ����
 *   Map.refresh();                            ����
 *   Map.close();                              ����
 *
 * 4������չʾ
 *   (1)���ߵ�ʸ�����ݡ�Ӱ�����ݣ����ߵ�ͼչʾ��
 *   (2)�򿪹����ռ䣻
 *   (3)������Դ��
 * ------------------------------------------------------------------------------
 * ============================================================================>
 * </p> 
 * 
 * <p>
 * Company: ������ͼ����ɷ����޹�˾
 * </p>
 * 
 */
public class MainActivity extends Activity implements OnClickListener{
	
	private static RadioButton mBtnOpenMap = null;
	private static RadioButton mBtnOpenWorkspace = null;
	private static RadioButton mBtnOpenDatasource = null;
	// ֻ������ܽ���
	private RadioButton mBtnReceiveFocus = null;
	
	private MapView            mMapView = null;
	private static MapControl  mMapControl = null;
	private DefaultDataManager mDefaultDataManager = null;
	private DataManager        mUserDataManager = null;
    private MyMapPopup         mMyMapPopup = null;
    private UserDatasourcePopup mUserDatasourcePopup = null;
    private UserWorkspacePopup  mUserWorkspacePopup = null;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		mMapView = (MapView) findViewById(R.id.mapView);
		mMapControl = mMapView.getMapControl();
		
		prepareData();
		initUI();
	}
	
	/**
	 * ��ʼ��UI
	 */
	private void initUI(){
		mBtnOpenMap        = (RadioButton) findViewById(R.id.btn_map_open);
		mBtnOpenWorkspace  = (RadioButton) findViewById(R.id.btn_wks_open);
		mBtnOpenDatasource = (RadioButton) findViewById(R.id.btn_ds_open);
		mBtnOpenMap.setOnClickListener(this);
		mBtnOpenWorkspace.setOnClickListener(this);
		mBtnOpenDatasource.setOnClickListener(this);
		
		mBtnReceiveFocus = (RadioButton) findViewById(R.id.btn_receivefocus);
		
		findViewById(R.id.btnZoomIn).setOnClickListener(this);
		findViewById(R.id.btnZoomOut).setOnClickListener(this);
		findViewById(R.id.btnViewEntire).setOnClickListener(this);
	}
	
	/**
	 * ��������
	 */
	private void prepareData(){
		final ProgressDialog progress = new ProgressDialog(this);
		progress.setCancelable(false);
		progress.setMessage("���ݼ�����...");
		progress.setProgressStyle(android.R.style.Widget_ProgressBar_Large);
		progress.show();
		new Thread(){
			@Override
			public void run() {
				super.run();
				//��������
				new DefaultDataConfig().autoConfig();
				mDefaultDataManager = MyApplication.getInstance().getDefaultDataManager();
				mUserDataManager = MyApplication.getInstance().getUserDataManager();
				mDefaultDataManager.open();
		    	progress.dismiss();
				runOnUiThread(new Runnable() {
					@Override
					public void run() {
						
						if(mDefaultDataManager.isDataOpen()){
							mMapControl.getMap().setWorkspace(mDefaultDataManager.getWorkspace());
							// �Գ����е�ͼ��������ˢ��
							if(mDefaultDataManager.getDisplayMapName().equals("��������ͼ")){
								mMapControl.getMap().setFullScreenDrawModel(true);
							}else{
								mMapControl.getMap().setFullScreenDrawModel(false);
							}
							//�ж�mapname�Ƿ�Ϊ��
							if(mDefaultDataManager.getDisplayMapName()== null){
								return;
							}
							mMapControl.getMap().open(mDefaultDataManager.getDisplayMapName());
						}else {
							MyApplication.getInstance().ShowError("�����ռ��ʧ�ܣ�");
						}
						mMyMapPopup          = new MyMapPopup(mMapControl);
						mUserDatasourcePopup = new UserDatasourcePopup(mMapControl);
						mUserWorkspacePopup  = new UserWorkspacePopup(mMapControl);
						progress.dismiss();
					}
				});
			}
		}.start();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_map_open:
			mBtnOpenMap.setChecked(true);
			mBtnOpenWorkspace.setChecked(false);
			mBtnOpenDatasource.setChecked(false);
			
			mMyMapPopup.show();
			mUserDatasourcePopup.dismiss();
			mUserWorkspacePopup.dismiss();
			break;
		case R.id.btn_wks_open:
			mBtnOpenMap.setChecked(false);
			mBtnOpenWorkspace.setChecked(true);
			mBtnOpenDatasource.setChecked(false);
			
			mUserWorkspacePopup.show();
			mMyMapPopup.dismiss();
			mUserDatasourcePopup.dismiss();
			break;
		case R.id.btn_ds_open:
			mBtnOpenMap.setChecked(false);
			mBtnOpenWorkspace.setChecked(false);
			mBtnOpenDatasource.setChecked(true);

			mUserDatasourcePopup.show();
			mMyMapPopup.dismiss();
			mUserWorkspacePopup.dismiss();
			break;
		case R.id.btnZoomIn:
			double curScale = mMapControl.getMap().getScale();
			mMapControl.getMap().zoom(2);
			mMapControl.getMap().refresh();
			break;
		case R.id.btnZoomOut:
			curScale = mMapControl.getMap().getScale();
			mMapControl.getMap().zoom(0.5);
			mMapControl.getMap().refresh();
			break;
		case R.id.btnViewEntire:
			//ȫ����ʱ���ֹͣ��ͼ�Ķ���
			mMapControl.cancelAnimation();
			mMapControl.getMap().viewEntire();
			mMapControl.getMap().refresh();
			break;
		default:
			break;
		}
	}
	
	/**
	 * ���ð�ť״̬
	 */
	public static void reset(){
		mBtnOpenMap.setChecked(false);
		mBtnOpenWorkspace.setChecked(false);
		mBtnOpenDatasource.setChecked(false);

	}
}
